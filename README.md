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
* app_config.yaml
* channel_config.yaml
* logback.xml

## Server
Perri has two servers. 

One is a main server that send message to messenger.

The other is a restful API server that provide application information and additional functions.

### API


