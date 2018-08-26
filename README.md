# Perri
[![Build Status](https://travis-ci.org/code13k/perri.svg?branch=master)](https://travis-ci.org/code13k/perri)


## Overview
We use Telegram and Slack to receive system monitoring message. 
We need to manage message and unify the methods. 
So we start to develop centralized server to easily send message to Telegram and Slack at once.
Perri send a message to multiple messenger at once.



## Supported messenger
* Telegram
* Slack
* Webhook



## Configuration
Automatically, default configuration files are created when application is executed. 
You can make default configuration files through just executing application and modify it for you.
You can find it at config directory.

Perri has three configuration files.

### app_config.yml
It's application configuration file.
```yaml
# Server port
port:
  main_http: 59490
  api_http: 59491
```

### channel_config.yml
It's channel configuration file.
```yaml
# Unique channel name 
[channel_name]:
# Telegram
- type: "telegram"
  bot_id: ""
  chat_id: ""
  display_tags: true
  merge_duplicate_message: true
# Slack
- type: "slack"
  webhook_url: ""
  display_tags: true
  merge_duplicate_message: true
# Webhook
- type: "webhook"
  webhook_url: ""
  display_tags: true
  merge_duplicate_message: true
```

### logback.xml
It's Logback configuration file that is famous logging library.
* You can send error log to Telegram.
* You can reload configuration but not need to restart application.



## Server
Perri has two servers. 
One is a main server that send message to messenger.
The other is a restful API server that provide application information and additional functions.



### API


