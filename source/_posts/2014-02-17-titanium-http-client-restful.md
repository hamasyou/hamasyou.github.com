---
layout: post
title: "TitaniumのHTTPClientでRESTfulリクエストを送る"
date: 2014-02-17 10:24:58 +0900
comments: true
categories: [Tech]
keywords: "Titanium,http,client,RESTful"
tags: [Titanium,RESTful,API]
sharing: true
published: true
amazon_url: ""
amazon_author: ""
amazon_image: ""
amazon_publisher: ""
og_image: ""
---

[Titanium Mobile](http://www.appcelerator.com/titanium/) の `Titanium.Network.HTTPClient` を使って RESTful リクエストを送る場合の注意点です。

[Titanium Mobile Development Environment](http://www.appcelerator.com/titanium/)

<!-- more -->

Titanium の `HTTPClient` は `GET`、`POST`、`PUT`、`DELETE` などのメソッドに対応していて、標準で RESTful リクエストを送れるようになっています。

### GET の例

```javascript
var url = 'http://localhost:9292/';
var client = Ti.Network.createHTTPClient({
    onload: function(e) { Ti.API.info("Received text: " + this.responseText); },
    onerror: function(e) { Ti.API.info("Receive Error: " + e.error); }
});
client.open('GET', url);
client.send();
```

```console-raw
"REQUEST_METHOD : GET"
"REQUEST_URI : http://localhost:9292/"
"QUERY_STRING : "
"GATEWAY_INTERFACE : CGI/1.1"
"PATH_INFO : /"
"REMOTE_ADDR : 127.0.0.1"
"REMOTE_HOST : localhost"
"SCRIPT_NAME : "
"SERVER_NAME : localhost"
"SERVER_PORT : 9292"
"SERVER_PROTOCOL : HTTP/1.1"
"SERVER_SOFTWARE : WEBrick/1.3.1 (Ruby/2.1.0/2013-12-25)"
"HTTP_HOST : localhost:9292"
"HTTP_X_TITANIUM_ID : 3659d1d4-77c2-4ffc-96d6-20b9d2251f74"
"HTTP_ACCEPT_ENCODING : gzip"
"HTTP_USER_AGENT : Appcelerator Titanium/3.2.1.GA (iPhone Simulator/7.0.3; iPhone OS; ja_JP;)"
"HTTP_CONNECTION : close"
"HTTP_X_REQUESTED_WITH : XMLHttpRequest"
"rack.version : [1, 2]"
"rack.input : #<Rack::Lint::InputWrapper:0x0000010286b4a8>"
"rack.errors : #<Rack::Lint::ErrorWrapper:0x0000010286b318>"
"rack.multithread : true"
"rack.multiprocess : false"
"rack.run_once : false"
"rack.url_scheme : http"
"HTTP_VERSION : HTTP/1.1"
"REQUEST_PATH : /"
{}
127.0.0.1 - - [17/Feb/2014 10:29:45] "GET / HTTP/1.1" 200 - 0.0006
```

### POST の例

`client.open('GET', url)` を `client.open('POST', url)` に変えて実行します。

```javascript
client.open('POST', url);
client.send();
```

```console-raw
"REQUEST_METHOD : POST"
"REQUEST_URI : http://localhost:9292/"
"QUERY_STRING : "
...
{}
127.0.0.1 - - [17/Feb/2014 10:43:06] "POST / HTTP/1.1" 200 - 0.0005
```

### PUT と DELETE の例

同じように `PUT` と `DELETE` でも試してみます。

```javascript
client.open('PUT', url);
client.send();
```

```console-raw
"REQUEST_METHOD : PUT"
"REQUEST_URI : http://localhost:9292/"
"QUERY_STRING : "
...
{}
127.0.0.1 - - [17/Feb/2014 10:45:32] "PUT / HTTP/1.1" 200 - 0.0005
```

```javascript
client.open('DELETE', url);
client.send();
```

```console-raw
"REQUEST_METHOD : DELETE"
"REQUEST_URI : http://localhost:9292/"
"QUERY_STRING : "
...
{}
127.0.0.1 - - [17/Feb/2014 10:46:28] "DELETE / HTTP/1.1" 200 - 0.0009
```

`client.send` でパラメータを送信しなければ、`open` で渡したメソッドでリクエストが投げられています。

## HTTPClient でパラメータを渡して `GET`, `POST`, `PUT`, `DELETE` してみる

次は、パラメータを渡して試してみます。

`GET` は `send` メソッドにパラメータを渡すのではなく URL にクエリを設定して送ります。

```javascript
var url = 'http://localhost:9292/';
var client = Ti.Network.createHTTPClient({
    onload: function(e) { Ti.API.info("Received text: " + this.responseText); },
    onerror: function(e) { Ti.API.info("Receive Error: " + e.error); }
});
client.open('GET', url + '?name=hamasyou');
client.send();
```

```console-raw
"REQUEST_METHOD : GET"
"REQUEST_URI : http://localhost:9292/?name=hamasyou"
"QUERY_STRING : name=hamasyou"
"GATEWAY_INTERFACE : CGI/1.1"
"PATH_INFO : /"
"REMOTE_ADDR : 127.0.0.1"
"REMOTE_HOST : localhost"
"SCRIPT_NAME : "
"SERVER_NAME : localhost"
"SERVER_PORT : 9292"
"SERVER_PROTOCOL : HTTP/1.1"
"SERVER_SOFTWARE : WEBrick/1.3.1 (Ruby/2.1.0/2013-12-25)"
"HTTP_HOST : localhost:9292"
"HTTP_X_TITANIUM_ID : 3659d1d4-77c2-4ffc-96d6-20b9d2251f74"
"HTTP_ACCEPT_ENCODING : gzip"
"HTTP_USER_AGENT : Appcelerator Titanium/3.2.1.GA (iPhone Simulator/7.0.3; iPhone OS; ja_JP;)"
"HTTP_CONNECTION : close"
"HTTP_X_REQUESTED_WITH : XMLHttpRequest"
"rack.version : [1, 2]"
"rack.input : #<Rack::Lint::InputWrapper:0x000001019451b8>"
"rack.errors : #<Rack::Lint::ErrorWrapper:0x00000101945140>"
"rack.multithread : true"
"rack.multiprocess : false"
"rack.run_once : false"
"rack.url_scheme : http"
"HTTP_VERSION : HTTP/1.1"
"REQUEST_PATH : /"
{"name"=>"hamasyou"}
127.0.0.1 - - [17/Feb/2014 10:49:41] "GET /?name=hamasyou HTTP/1.1" 200 - 0.0006
```

`GET` で送られています。次は `POST` で送ってみます。

```javascript
client.open('POST', url);
client.send({name: 'hamasyou'});
```

`POST` は `client.send` にパラメータを渡して送ります。`POST` のデフォルトの `Content-Type` は `application/x-www-form-urlencoded` になります。

```console-raw
"REQUEST_METHOD : POST"
"REQUEST_URI : http://localhost:9292/"
"CONTENT_LENGTH : 13"
"CONTENT_TYPE : application/x-www-form-urlencoded; charset=utf-8"
"QUERY_STRING : "
...
{"name"=>"hamasyou"}
127.0.0.1 - - [17/Feb/2014 10:52:10] "POST / HTTP/1.1" 200 - 0.0006
```

`POST` で `Content-Type: application/json` で送る場合は、`HTTPClient#setRequestHeader` で設定します。

```javascript
client.open('POST', url);
client.setRequestHeader('Content-Type', 'application/json');
client.send(JSON.stringify({name: 'hamasyou'}));
```

```console-raw
"REQUEST_METHOD : POST"
"REQUEST_URI : http://localhost:9292/"
"CONTENT_LENGTH : 19"
"CONTENT_TYPE : application/json"
"QUERY_STRING : "
...
"{\"name\":\"hamasyou\"}"
127.0.0.1 - - [17/Feb/2014 10:56:20] "POST / HTTP/1.1" 200 - 0.0009
```

`PUT` と `DELETE` も `send` でパラメータを送ってみます。

```javascript
client.open('PUT', url);
client.setRequestHeader('Content-Type', 'application/json');
client.send(JSON.stringify({name: 'hamasyou'}));
```

```console-raw
"REQUEST_METHOD : PUT"
"REQUEST_URI : http://localhost:9292/"
"CONTENT_LENGTH : 19"
"CONTENT_TYPE : application/json"
"QUERY_STRING : "
...
"{\"name\":\"hamasyou\"}"
127.0.0.1 - - [17/Feb/2014 10:57:49] "PUT / HTTP/1.1" 200 - 0.0006
```

`PUT` はうまくいきました。次は `DELETE` です。

```javascript
client.open('DELETE', url);
client.setRequestHeader('Content-Type', 'application/json');
client.send(JSON.stringify({name: 'hamasyou'}));
```

```console-raw
"REQUEST_METHOD : POST"
"REQUEST_URI : http://localhost:9292/"
"CONTENT_LENGTH : 19"
"CONTENT_TYPE : application/json"
"QUERY_STRING : "
...
"{\"name\":\"hamasyou\"}"
127.0.0.1 - - [17/Feb/2014 10:58:52] "POST / HTTP/1.1" 200 - 0.0006
```

なんと、`POST` リクエストになってしまいました。。`DELETE` メソッドだけ、パラメータを `body` 部分に含めると `POST` リクエストになってしまうみたいです。。

OAuth 対応の API 等で access_token をパラメータに含めてリクエストする様な API を呼び出すときは要注意です。

できるだけ、認証情報は HTTP Header を使うようにした方がいいのかもしれません。
