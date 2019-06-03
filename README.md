# Perri send a message to multiple messenger at once.
We use Telegram and Slack to receive system monitoring message. 
We need to manage message and unify the methods. 
So we start to develop centralized server to easily send message to Telegram and Slack at once.

[![Build Status](https://travis-ci.org/code13k/perri.svg?branch=master)](https://travis-ci.org/code13k/perri)



# Supported messenger
* Telegram
* Slack1
* Webhook



# Configuration
Automatically, default configuration files are created when application is executed. 
You can make default configuration files through just executing application and modify it for you.
You can find it at config directory.

Perri has three configuration files.

## app_config.yml
It's application configuration file.
```yaml
# Server port
port:
  main_http: 59490
  api_http: 59491
```

## channel_config.yml
It's channel configuration file.
```yaml
# Unique channel name 
[channel_name]:
# Telegram
- type: "telegram"
  bot_api_token: ""
  chat_id: ""
  display_tags: true
  merge_duplicate_message: true
# Slack
- type: "slack"
  incoming_webhook_url: ""
  display_tags: true
  merge_duplicate_message: true
# Webhook
- type: "webhook"
  webhook_url: ""
  display_tags: true
  merge_duplicate_message: true
```

## logback.xml
It's Logback configuration file that is famous logging library.
* You can send error log to Telegram.
  1. Uncomment *Telegram* configuration.
  2. Set value of `<botToken>` and `<chatId>`.
       ```xml
       <appender name="TELEGRAM" class="com.github.paolodenti.telegram.logback.TelegramAppender">
           <botToken></botToken>
           <chatId></chatId>
           ...
       </appender>
       ```
  3. Insert `<appender-ref ref="TELEGRAM"/>` into `<root>`
     ```xml
     <root level="WARN">
         <appender-ref ref="FILE"/>
         <appender-ref ref="TELEGRAM"/>
     </root>
     ```
* You can send error log to Slack.
  1. Uncomment *Slack* configuration.
  2. Set value of `<webhookUri>`.
       ```xml
       <appender name="SLACK_SYNC" class="com.github.maricn.logback.SlackAppender">
           <webhookUri></webhookUri>
           ...
       </appender>
       ```
  3. Insert `<appender-ref ref="SLACK"/>` into `<root>`
     ```xml
     <root level="WARN">
         <appender-ref ref="FILE"/>
         <appender-ref ref="SLACK"/>
     </root>
     ```
* You can reload configuration but need not to restart application.



# Server
Perri has two servers. 
One is a main server that send message to messenger.
The other is a restful API server that provide application information and additional functions.



## Main HTTP Server
### Usage
```html
http://example.com:{port}/{channel}?message={message}&tags={tag1,tag2,tag3}
```
* port
  * Server port
  * It's *main_http* in app_config.yml.
* channel
  * Channel name
  * It's *channel* in channel_config.yml
* message
  * Sending message
  * If message length are more than supported maximum length, truncate string with ellipsis.
* tags
  * It's optional
  * Tags of message
  * Comma or space separated
  * Max 20 tags (If tags count are more than maximum, ignore some tags )
  * Max 30 characters per tag (If tag characters more than maximum, ignore tag)

### Example
```html
http://example.com:59490/perri_default_channel?message=hello&tags=world,perri,tag
```

### Channel
#### Telegram
Send message to Telegram using bot id and chat id

#### Slack
Send message to Slack using incoming webhook url

https://api.slack.com/incoming-webhooks

#### Webhook
Send message to specific url via an HTTP POST request.
```json
{
  "channel": "perri_default_channel",
  "message": "webhook message",
  "tags": "tag1,tag2,tag3",
  "duplicated": 2
}
```


## API HTTP Server
### Usage
```html
http://example.com:{port}/{domain}/{method}
```

### Example
```html
http://example.com:59491/app/status
http://example.com:59491/app/hello
http://example.com:59491/app/ping
```

### API

#### GET /app/env
* Get application environments
##### Response
```json
{
  "data":{
    "applicationVersion": "1.4.0",
    "hostname": "hostname",
    "osVersion": "10.11.6",
    "jarFile": "code13k-perri-1.0.0-alpha.1.jar",
    "javaVersion": "1.8.0_25",
    "ip": "192.168.0.121",
    "javaVendor": "Oracle Corporation",
    "osName": "Mac OS X",
    "cpuProcessorCount": 4
  }
}
```
#### GET /app/status
* Get application status
##### Response
```json
{
  "data":{
    "threadInfo":{...},
    "cpuUsage": 2.88,
    "threadCount": 25,
    "currentDate": "2018-10-02T01:15:21.290+09:00",
    "startedDate": "2018-10-02T01:14:40.995+09:00",
    "runningTimeHour": 0,
    "vmMemoryUsage":{...}
  }
}
```
#### GET /app/hello
* Hello, World
##### Response
```json
{"data":"world"}
```

#### GET /app/ping
* Ping-Pong
##### Response
```json
{"data":"pong"}
```
