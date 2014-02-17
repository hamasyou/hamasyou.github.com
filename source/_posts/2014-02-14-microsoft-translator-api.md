---
layout: post
title: "RubyからMicrosoft Translator APIを呼び出す"
date: 2014-02-14 15:40:59 +0900
comments: true
categories: [Tech]
keywords: "Ruby,Microsoft Translator API,Restful"
tags: [Ruby,Microsoft Translator API,RESTful]
sharing: true
published: true
amazon_url: ""
amazon_author: ""
amazon_image: ""
amazon_publisher: ""
og_image: ""
---

Google の翻訳 API が有料化されたことで、無料で使える翻訳 API が実質 **Microsoft Translator API** 一択になってしまいました。

実際に使ってみようと思ったときに、いろいろなサイトをぐるぐる回ったり、API の呼び出し方が変わっていたりしてとまどったので、手順のおさらいをしておきます。

<!-- more -->

## Microsoft Translator API

Microsoft Translator API は Microsfot が提供する翻訳サービスで REST インターフェースを備えています。

利用するには Microsoft Azure Marketplace で Microsoft Translator の利用登録が必要です。

### Microsoft Translator 登録を行う

翻訳 API を使うには Azure Marketplace で利用登録が必要になります。

[Microsoft Azure Marketplace](https://datamarket.azure.com/dataset/1899a118-d202-492c-aa16-ba21c33c06cb)

アカウントを持っていない人はアカウントを作成して、月額0円のプランを購入します。

![Microsoft Translator の購入](/images/2014-02-14-microsoft-translator-api-01.png)

### アプリケーション登録を行う

次に、Azure Marketplace でアプリケーションの登録を行います。

[開発者 アプリケーション登録 - Microsoft Azure Marketplace](https://datamarket.azure.com/developer/applications)

![アプリケーション登録](/images/2014-02-14-microsoft-translator-api-02.png)

翻訳 API だけを利用する場合には *リダイレクトURI* は利用しませんが、必須入力なので `http://localhost/` と入力しておきます。`https` で登録しろと怒られますが無視して登録できます。

ここで登録した *クライアントID* 、*顧客の秘密* が API 呼び出しの時に使う **client_id** 、 **client_secret** になります。

### Ruby から呼び出してみる

翻訳 API を呼び出すには、呼び出しの前に OAuth を通さなければなりません。OAuth を通すと得られる *access_token* を使って翻訳 API を呼び出します。

OAuth を通すには POST リクエストが必要なので、サンプルでは Ruby の `open-uri` を post 対応したものを使っています。

[[ruby]open-uriをPOST対応させる](http://d.hatena.ne.jp/urekat/20070201/1170349097)


```ruby
require 'cgi'
require 'open-uri-post'
require 'rexml/document'
require 'json'

CLIENT_ID       = '<クライアントID>'
CLIENT_SECRET   = '<顧客の秘密>'
AUTHORIZE_URL   = 'https://datamarket.accesscontrol.windows.net/v2/OAuth2-13'
TRANSLATION_URL = 'http://api.microsofttranslator.com/V2/Http.svc/Translate'
SCOPE           = 'http://api.microsofttranslator.com'

def get_access_token
  access_token = nil
  open(AUTHORIZE_URL,
       'postdata' => "grant_type=client_credentials&client_id=#{CGI.escape(CLIENT_ID)}&client_secret=#{CGI.escape(CLIENT_SECRET)}&scope=#{CGI.escape(SCOPE)}") do |f|
    json           = JSON.parse(f.read)
    access_token   = json['access_token']
  end
  access_token
end


def translate_text(text)
  access_token = get_access_token
  res = open("#{TRANSLATION_URL}?from=en&to=ja&text=#{URI.escape(text)}",
             'Authorization' => "Bearer #{access_token}").read
  xml = REXML::Document.new(res)
  xml.root.text
end

puts translate_text('Hello World')
# => "ハローワールド"
```
