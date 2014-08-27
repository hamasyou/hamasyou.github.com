---
layout: post
title: "Swift での Dictionary<String, AnyObject> の扱いメモ"
date: 2014-08-27 23:54:25 +0900
comments: true
categories: [Programming]
keywords: "Swift, iOS, Apple, Objective-C, Dictionary"
tags: [Swift]
sharing: true
published: true
amazon_url: ""
amazon_author: ""
amazon_image: ""
amazon_publisher: ""
og_image: ""

---

Swift で `Dictionary` を扱うときのメモです。API 呼び出しのレスポンスを JSON で扱いたい時に `Dictionary<String, AnyObject>` として扱う際のポイントです。

```swift
typealias JSONDictionary = Dictionary<String, AnyObject>
let json = NSJSONSerialization.JSONObjectWithData(data, options: .MutableContainers, error: nil) as JSONDictionary

let str1 = json["foobar"]! as String    // String
let str2 = json["foobar"] as AnyObject? as? String  // String?

let str3 = json["foobar"] as String     // これはコンパイルエラー '(String, AnyObject)' is not convertible to 'String'
```

最後のはなぜコンパイルエラーになるかというと、`Dictionary` の `subscript` が2種類定義されていて、期待したのと違う方が呼び出されているからです。

```swift
struct Dictionary<Key : Hashable, Value> : CollectionType, DictionaryLiteralConvertible {
    ...
    subscript (i: DictionaryIndex<Key, Value>) -> (Key, Value) { get }
    subscript (key: Key) -> Value?
    ...
}
```

`json["foobar"]` の戻り値は `(Key, Value)` か `Value?` のどちらかですが、`as String` を付けた際に Optional ではないと判断されてしまい `(Key, Value)` が戻り値の型と判定されます。それでコンパイルエラーになるわけですね。

期待した通りに取得するには、`json["foobar"]` の戻り値を `Value?` として扱う必要があるので、

```swift
json["foobar"]!
json["foobar"] as AnyObject?
```
のどちらかでアクセスする必要があるわけです。`!` を付けると unwrap されるので `nil` が入っていると実行時エラーになります。逆に `as AnyObject? as? String` でアクセスすると Optional 型になってしまいます。

API のインターフェースと相談して、どちらの型で処理するか決めるといいんじゃないかと思います。
