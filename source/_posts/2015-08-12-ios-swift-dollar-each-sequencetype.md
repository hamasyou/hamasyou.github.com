---
layout: post
title: "[iOS]Swift の Dollar ライブラリの each 関数を SequenceType に対応する拡張"
date: 2015-08-12 16:47:11 +0900
comments: true
categories: [Programming]
keywords: "iOS,Swift,Dollar,extension,each,SequenceType"
tags: [Swift,Memo]
sharing: true
published: true
amazon_url: ""
amazon_author: ""
amazon_image: ""
amazon_publisher: ""
og_image: ""

---

Swift のメモ書きです。

[ankurp/Dollar.swift](https://github.com/ankurp/Dollar.swift) という JavaScript の Lo-Dash や Underscore と同じ感じでコレクションを扱うためのライブラリがありますが、 `each` が配列しか受け付けることが出来ないので、`SequenceType` を受け取れるように拡張する方法のメモです。

```swift
import Dollar

extension $ {
    class func each<T: SequenceType>(array: T, callback: (T.Generator.Element) -> ()) -> T {
        for elem in array {
            callback(elem)
        }
        return array
    }
}

$.each(["a", "b", "c"]) { (s: String) in println(s) }
$.each(["key": "val"]) { (e: (String, String)) in println("\(e.0) = \(e.1)") }
// a
// b
// c
// key = val
```
