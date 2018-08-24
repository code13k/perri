# Perri



## Overview
We are using Telegram and Slack to receive system monitoring message.
So we are developing centralized server to easily send message to Telegram and Slack at once.
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
### channel_config.yml
It's channel configuration file.
### logback.xml
Logback, that is famous logging library, configuration file.



## Server
Perri has two servers. 

One is a main server that send message to messenger.

The other is a restful API server that provide application information and additional functions.



### API


