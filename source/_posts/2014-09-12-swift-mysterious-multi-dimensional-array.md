---
layout: post
title: "[Swift] 多次元配列の定義の謎"
date: 2014-09-12 11:21:20 +0900
comments: true
categories: [Programming]
keywords: "Swift,Apple,iOS,多次元配列"
tags: [Swift]
sharing: true
published: true
amazon_url: ""
amazon_author: ""
amazon_image: ""
amazon_publisher: ""
og_image: ""

---

Xcode6 GM でましたね！ Beta1 の頃から Swift 触ってますが、まぁ言語仕様がよく変わること（笑。

で、いつか直るだろうと思ってたけど、GM でも直っていない Swift の多次元配列の定義の謎に関してです。だれか理由分かる人いたら教えてください。。

下のようなコードがあるとします。

```swift

typealias NameAndValue = Dictionary<String, String>

let dict: [[NameAndValue]] = [
  [
     ["name": "",    "value": ""],
  ],
  [
     ["name": "",    "value": ""],
  ]]
```

`typealias` で別名つけて2次元配列を定義しているだけです。要素が `Dictionary` になっています。ここまでは普通にコンパイルが通ります。

ですが、これをこんなふうに...

```swift

let dict: [[NameAndValue]] = [
  [
     ["name": "",    "value": ""],
     ["name": "",    "value": ""],
     ["name": "",    "value": ""],
     ["name": "",    "value": ""],
     ["name": "",    "value": ""],
     ["name": "",    "value": ""],
     ["name": "",    "value": ""],
     ["name": "",    "value": ""],
     ["name": "",    "value": ""],
     ["name": "",    "value": ""],
     ["name": "",    "value": ""],
     ["name": "",    "value": ""],
     ["name": "",    "value": ""],
     ["name": "",    "value": ""],     
     ["name": "",    "value": ""],
     ["name": "",    "value": ""],
     ["name": "",    "value": ""],
     ["name": "",    "value": ""],
     ["name": "",    "value": ""],                         
  ],
  [
     ["name": "",    "value": ""],
  ]]
```

配列の要素を増やしていきます。そうするとだいたい **10件くらい** からコンパイルが遅くなり、**15件過ぎたくらいから** コンパイルエラーが出るようになります。

```
Cannot convert the expression's type '[[NameAndValue]]' to type 'StringLiteralConvertible'
```

意味がわからんです。。

仕方ないので

```swift
var dict: [[NameAndValue]] = [[],[]]
dict[0].append(["name": "",    "value": ""])
dict[0].append(["name": "",    "value": ""])
dict[0].append(["name": "",    "value": ""])
dict[0].append(["name": "",    "value": ""])
dict[0].append(["name": "",    "value": ""])
dict[0].append(["name": "",    "value": ""])
dict[0].append(["name": "",    "value": ""])
dict[0].append(["name": "",    "value": ""])
dict[0].append(["name": "",    "value": ""])
dict[0].append(["name": "",    "value": ""])
dict[0].append(["name": "",    "value": ""])
dict[0].append(["name": "",    "value": ""])
dict[0].append(["name": "",    "value": ""])
dict[0].append(["name": "",    "value": ""])
dict[0].append(["name": "",    "value": ""])
dict[0].append(["name": "",    "value": ""])
dict[0].append(["name": "",    "value": ""])
dict[0].append(["name": "",    "value": ""])

dict[1].append(["name": "",    "value": ""])
dict[1].append(["name": "",    "value": ""])
dict[1].append(["name": "",    "value": ""])
dict[1].append(["name": "",    "value": ""])
dict[1].append(["name": "",    "value": ""])
```        

こうやって、プログラムでデータ登録するようにしました。。
