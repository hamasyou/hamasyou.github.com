---
layout: post
title: "[ReactiveCocoa] catchTo の使いどころ"
date: 2014-08-29 19:44:49 +0900
comments: true
categories: [Programming]
keywords: "ReactiveCocoa,iOS,catchTo"
tags: [ReactiveCocoa,iOS]
sharing: true
published: true
amazon_url: ""
amazon_author: ""
amazon_image: ""
amazon_publisher: ""
og_image: ""
---

**ReactiveCocoa** で `catchTo` の使い方を覚えたのでメモ。

リアクティブプログラミングの詳細は省くとして、`RACSignal` のイベントには `next` と `completed` と `error` があります。それぞれ、`subscribeNext`、`subscribeCompleted`、`subscribeError` でハンドリングできるやつです。

で、HTTP API 等を呼び出す際に API 呼び出しの結果を JSON にパースして、結果をモデルに設定するみたいなことをやりたい時に、次のように行います。（Swift で記述しています。）

```swift
RAC(self, "model") <~ API.loadData()

class API {
    class func loadData() -> RACSignal {
        return RACSignal.createSignal({ (subscriber: RACSubscriber!) in
            let url = NSURL(string: "http://localhost:300/search")
            let configuration = NSURLSessionConfiguration.defaultSessionConfiguration()
            configuration.HTTPAdditionalHeaders = ["Accept": "application/json"]
            let session: NSURLSession = NSURLSession(configuration: configuration)
            let task: NSURLSessionDataTask = session.dataTaskWithURL(url, completionHandler: { (data: NSData!, response: NSURLResponse!, error: NSError!) in
                if error == nil {
                    // data をパースしてモデル化
                    subscriber.sendNext(model)
                    subscriber.sendCompleted()
                } else {
                    subscriber.sendError(error)
                }
                session.invalidateAndCancel()
            })
            task.resume()
            return nil
        })
    }
}
```

こうすると、API の呼び出しが成功した場合には `subscriber.sendNext` の結果がきちんと `self.model` に設定されるわけですが、API の呼び出しが失敗して `subscriber.sendError` が呼び出されてしまうと、例外がなげられます。

ではどうするか。

### catchTo を使って sendError に備える

そこで `catchTo` を使います。

```swift
RAC(self, "model") <~ API.loadData().catchTo(RACSignal.empty())
```

`catchTo` は `sendError` が呼び出された場合に代わりに投げる `RACSignal` を指定します。そうすることで、ネットワークエラー等で API の呼び出しが失敗した場合には、`RACSignal.empty()` で何も起きなかったことになる（正確には sendCompleted が呼び出される）ようになります。

sendError 時に処理を行いたい場合には `catch` を代わりに使用します。

```swift
RAC(self, "model") <~ API.loadData()
    .catch({ (error: NSError!) -> RACSignal! in
        return RACSignal.empty()
    })
```

以上です。

