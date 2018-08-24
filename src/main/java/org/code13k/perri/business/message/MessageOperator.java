package org.code13k.perri.business.message;

import org.code13k.perri.business.message.sender.BasicSender;
import org.code13k.perri.business.message.sender.SenderFactory;
import org.code13k.perri.model.MessageOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.function.Consumer;

public class MessageOperator {
    // Logger
    private static final Logger mLogger = LoggerFactory.getLogger(MessageOperator.class);

    // Data
    private ArrayList<MessageOperation> mQueue = new ArrayList<>();

    /**
     * Constructor
     */
    public MessageOperator() {
        mLogger.info("MessageOperator()");
        runMessageOperationThread();
    }

    /**
     * Add message operation to queue
     * <p>
     * TODO Does it need to check duplicates?
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
     * message operation count in ready
     */
    public int queueReadyCount() {
        return mQueue.size();
    }


    /**
     * Operate message operation
     */
    private void operate(MessageOperation messageOperation) {
        if(messageOperation!=null) {
            final String type = messageOperation.getChannelInfo().getType();
            BasicSender sender = SenderFactory.getSender(messageOperation);
            sender.send(messageOperation, new Consumer<Integer>() {
                @Override
                public void accept(Integer result) {
                    if (result == BasicSender.SendResult.SUCCESS) {
                        mLogger.debug("The operation has succeeded. (" + type + ")");
                    } else if (result == BasicSender.SendResult.FAILURE) {
                        mLogger.error("The operation has failed. (" + type + ")");
                    } else if (result == BasicSender.SendResult.TEMPORARY_FAILURE) {
                        mLogger.error("The operation has failed. But it's temporary. Try again. (" + type + ")");
                        // TODO Impl
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
        thread.setName("MessageOperationThread");
        thread.start();
    }
}

