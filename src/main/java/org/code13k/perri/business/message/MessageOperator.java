package org.code13k.perri.business.message;


import org.code13k.perri.business.message.sender.BasicSender;
import org.code13k.perri.business.message.sender.SenderFactory;
import org.code13k.perri.model.MessageOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;

public class MessageOperator {
    // Logger
    private static final Logger mLogger = LoggerFactory.getLogger(MessageOperator.class);

    // Const
    private static final int MAX_RETRY_COUNT = 5;

    // Data
    private Timer mRetryTimer = new Timer("perri-retry-timer");
    private ArrayList<MessageOperation> mQueue = new ArrayList<>();
    private long mSentMessageCount = 0;

    /**
     * Constructor
     */
    public MessageOperator() {
        mLogger.trace("MessageOperator()");
        runMessageOperationThread();
    }

    /**
     * Add message operation to queue
     */
    public void add(MessageOperation messageOperation) {
        synchronized (mQueue) {
            if (messageOperation != null) {
                // Merge duplicate message
                if (true == messageOperation.getChannelInfo().isMergeDuplicateMessage()) {
                    int index = mQueue.indexOf(messageOperation);
                    if (index >= 0) {
                        mLogger.debug("Message is duplicated : " + messageOperation);
                        MessageOperation existMessageOperation = mQueue.get(index);
                        existMessageOperation.increaseMessageCount();
                        mLogger.debug("Duplicated message count : " + existMessageOperation.getMessageCount());
                    } else {
                        mLogger.debug("Message is not duplicated : " + messageOperation);
                        mQueue.add(messageOperation);
                    }
                }

                // Not merge
                else {
                    mQueue.add(messageOperation);
                }
            }
        }
    }

    /**
     * Number of ready message
     */
    public int getReadyMessageCount() {
        return mQueue.size();
    }

    /**
     * Number of sent message
     */
    public long getSentMessageCount() {
        return mSentMessageCount;
    }

    /**
     * Operate message operation
     */
    private void operate(MessageOperation messageOperation) {
        if (messageOperation != null) {
            final String type = messageOperation.getChannelInfo().getType();
            BasicSender sender = SenderFactory.getSender(messageOperation);
            sender.send(messageOperation, new Consumer<Integer>() {
                @Override
                public void accept(Integer result) {
                    if (result == BasicSender.SendResult.SUCCESS) {
                        mLogger.debug("The operation has succeeded. (" + type + ")");
                        mSentMessageCount++;
                    } else if (result == BasicSender.SendResult.FAILURE) {
                        mLogger.error("The operation has failed. (" + type + ")");
                    } else if (result == BasicSender.SendResult.TEMPORARY_FAILURE) {
                        mLogger.error("The operation has failed. But it's temporary. Try again. (" + type + ")");

                        // Retry operation
                        if (messageOperation.getRetryCount() < MAX_RETRY_COUNT) {
                            messageOperation.increaseRetryCount();
                            mLogger.info("messageOperation = " + messageOperation);
                            mRetryTimer.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    add(messageOperation);
                                }
                            }, 3000 * messageOperation.getRetryCount());
                        } else {
                            // FAILURE
                            mLogger.error("Failed to retry. The operation has failed. (" + type + ")");
                        }
                    }
                }
            });
        }
    }

    /**
     * Message operation thread
     */
    private void runMessageOperationThread() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        // Get operation
                        MessageOperation messageOperation = null;
                        synchronized (mQueue) {
                            if (mQueue.size() > 0) {
                                messageOperation = mQueue.get(0);
                                mQueue.remove(0);
                            }
                        }

                        // Has no job
                        if (messageOperation == null) {
                            try {
                                Thread.sleep(1000);
                            } catch (Exception e) {
                                // Nothing
                            }
                            continue;
                        }

                        // Has job
                        operate(messageOperation);
                    } catch (Exception e) {
                        mLogger.error("Exception occurred when running message operation", e);
                    }
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.setPriority(Thread.MIN_PRIORITY);
        thread.setName("perri-message-operation-thread");
        thread.start();
    }
}

