# Perri send a message to multiple messenger at once.
We use Telegram and Slack to receive system monitoring message. 
We need to manage message and unify the methods. 
So we start to develop centralized server to easily send message to Telegram and Slack at once.

[![Build Status](https://travis-ci.org/code13k/perri.svg?branch=master)](https://travis-ci.org/code13k/perri)



# Supported messenger
* Telegram
* Slack
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

## logback.xml
It's Logback configuration file that is famous logging library.
* You can send error log to Telegram.
* You can reload configuration but need not to restart application.



# Server
Perri has two servers. 
One is a main server that send message to messenger.
The other is a restful API server that provide application information and additional functions.



## Main HTTP Server
### Usage
```html
http://example.com:{port}/{channel_name}?message={message}&tags={tag1,tag2,tag3}
```
* port
  * Server port
  * It's "main_http" in app_config.yml.
* channel_name
  * Channel name
  * It's "channel_name" in channel_config.yml
* message
  * Sending message
  * Maximum length is 3073byte.
* tags
  * It's optional
  * Tags of message
  * Comma separated
  * Maximum length is 512byte.

### Example
```html
http://example.com:59490/perri_default_channel?message=hello&tags=world,perri,tag
```

### Channel
#### Telegram
Send message to Telegram using bot_id and chat_id

#### Slack
Send message to Slack using webhook_url

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
#### GET /app/status
* Get application status and environment.
* Parameter : None
 
```json

{"data":{
    "applicationVersion":"0.1.0-alpha.3",
    "cpuUsage":2.56,
    "threadInfo":{...},
    "vmMemoryFree":"190M",
    "javaVersion":"1.8.0_25",
    "vmMemoryMax":"3,641M",
    "currentDate":"2018-08-27T18:48:58.795+09:00",
    "threadCount":15,
    "startedDate":"2018-08-27T18:48:40.901+09:00",
    "javaVendor":"",
    "runningTimeHour":0,
    "osName":"Mac OS X",
    "cpuProcessorCount":4,
    "vmMemoryTotalFree":"3,585M",
    "hostname":"",
    "osVersion":"10.11.6",
    "jarFile":"code13k-perri-0.1.0-alpha.3.jar",
    "vmMemoryAllocated":"245M",
  }
}
```
#### GET /app/hello
* Hello, World
* Parameter : None

```json
{"data":"world"}
```

#### GET /app/ping
* Ping-Pong
* Parameter : None

```json
{"data":"pong"}
```
