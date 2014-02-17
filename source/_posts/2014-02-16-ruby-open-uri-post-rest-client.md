---
layout: post
title: "open-uri-postを使うとrest-clientが変な動きするをする"
date: 2014-02-16 21:45:53 +0900
comments: true
categories: [Programming]
keywords: "open-uri-post,rest-client,ruby,gem"
tags: [Ruby,gem]
sharing: true
published: true
amazon_url: ""
amazon_author: ""
amazon_image: ""
amazon_publisher: ""
og_image: ""
---

ruby の `open-uri` を POST に対応させる `open-uri-post` というライブラリがありますが、これを使うと `rest-client` の gem が変な動きをするようです。

<!-- more -->

`open-uri-post` を `require` すると `RestClient.post` が GET のリクエストになってうまくリクエストできなくなってしまいました。。

[[ruby]open-uriをPOST対応させる](http://d.hatena.ne.jp/urekat/20070201/1170349097)

[rest-client/rest-client](https://github.com/rest-client/rest-client)

下のサンプルでは、そもそもリクエストが送れなかったりもします。。

### open-uri-post だけの場合

```ruby
require './open-uri-post.rb'
open('http://localhost:9292', {'postdata' => 'name=hamasyou'}).read
```

```console
"CONTENT_LENGTH : 13"
"CONTENT_TYPE : application/x-www-form-urlencoded"
"GATEWAY_INTERFACE : CGI/1.1"
"PATH_INFO : /"
"QUERY_STRING : "
"REMOTE_ADDR : 127.0.0.1"
"REMOTE_HOST : localhost"
"REQUEST_METHOD : POST"
"REQUEST_URI : http://localhost:9292/"
"SCRIPT_NAME : "
"SERVER_NAME : localhost"
"SERVER_PORT : 9292"
"SERVER_PROTOCOL : HTTP/1.1"
"SERVER_SOFTWARE : WEBrick/1.3.1 (Ruby/2.1.0/2013-12-25)"
"HTTP_ACCEPT_ENCODING : gzip;q=1.0,deflate;q=0.6,identity;q=0.3"
"HTTP_ACCEPT : */*"
"HTTP_USER_AGENT : Ruby"
"HTTP_HOST : localhost:9292"
"rack.version : [1, 2]"
"rack.input : #<Rack::Lint::InputWrapper:0x000001029c65f0>"
"rack.errors : #<Rack::Lint::ErrorWrapper:0x000001029c6578>"
"rack.multithread : true"
"rack.multiprocess : false"
"rack.run_once : false"
"rack.url_scheme : http"
"HTTP_VERSION : HTTP/1.1"
"REQUEST_PATH : /"
{"name"=>"hamasyou"}
127.0.0.1 - - [16/Feb/2014 22:02:26] "POST / HTTP/1.1" 200 - 0.0005
```

`CONTENT_LENGTH: 13` になっていて、パラメータもきちんと送れています。

### rest-client だけの場合

```ruby
require 'rest_client'
RestClient.post('http://localhost:9292', name: 'hamasyou')
```

```console
"CONTENT_LENGTH : 13"
"CONTENT_TYPE : application/x-www-form-urlencoded"
"GATEWAY_INTERFACE : CGI/1.1"
"PATH_INFO : /"
"QUERY_STRING : "
"REMOTE_ADDR : 127.0.0.1"
"REMOTE_HOST : localhost"
"REQUEST_METHOD : POST"
"REQUEST_URI : http://localhost:9292/"
"SCRIPT_NAME : "
"SERVER_NAME : localhost"
"SERVER_PORT : 9292"
"SERVER_PROTOCOL : HTTP/1.1"
"SERVER_SOFTWARE : WEBrick/1.3.1 (Ruby/2.1.0/2013-12-25)"
"HTTP_ACCEPT : */*; q=0.5, application/xml"
"HTTP_ACCEPT_ENCODING : gzip, deflate"
"HTTP_USER_AGENT : Ruby"
"HTTP_HOST : localhost:9292"
"rack.version : [1, 2]"
"rack.input : #<Rack::Lint::InputWrapper:0x0000010299d380>"
"rack.errors : #<Rack::Lint::ErrorWrapper:0x0000010299d290>"
"rack.multithread : true"
"rack.multiprocess : false"
"rack.run_once : false"
"rack.url_scheme : http"
"HTTP_VERSION : HTTP/1.1"
"REQUEST_PATH : /"
{"name"=>"hamasyou"}
127.0.0.1 - - [16/Feb/2014 22:03:29] "POST / HTTP/1.1" 200 - 0.0005
```

こっちも、きちんとリクエストできています。

### open-uri-post と rest-client を同時に使うと

```ruby
require './open-uri-post.rb'
require 'rest_client'

# open('http://localhost:9292', {'postdata' => 'name=hamasyou'}).read
RestClient.post('http://localhost:9292', name: 'hamasyou')
RestClient.get('http://localhost:9292', params: {name: 'hamasyou'})
```

`open-uri-post` の方の `open` メソッドはうまく動きますが、RestClient が動かなくなります。

```console
/vendor/bundle/ruby/2.1.0/gems/rest-client-1.6.7/lib/restclient/abstract_response.rb:48:in `return!': 408 Request Timeout (RestClient::RequestTimeout)
```

RequestTimeout が出てしまいました。。ちなみに、`GET` リクエストの方はうまく動きます。`open-uri-post` と `rest-client` は一緒に使わないほうがいいですね。

