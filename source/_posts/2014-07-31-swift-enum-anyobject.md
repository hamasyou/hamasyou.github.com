---
layout: post
title: "SwiftはenumをAnyObject型の変数に入れられない"
date: 2014-07-31 10:39:48 +0900
comments: true
categories: [Programming]
keywords: "Swift,Apple,Lang,Enum,AnyObject"
tags: [Swift]
sharing: true
published: true
amazon_url: ""
amazon_author: ""
amazon_image: ""
amazon_publisher: ""
og_image: ""

---

Swift で *enum* を扱う際にハマった問題です。確認は *Xcode6 beta4* で行っています。

Swift では `enum` を `AnyObject` 型の変数に入れることができません。コンパイルエラーになります。`Any` 型なら代入できます。

```swift
enum SomeType: Int {
    case None = 0
    case Something
}

let something = SomeType.None
something                                     // Enum Value

let anything: AnyObject = SomeType.Something  // error: type 'SomeType' does not conform to protocol 'AnyObject'

let any: Any = SomeType.Something             // Enum Value
```

Swift の `enum` は `AnyObject` protocol を実装していないので、`AnyObject` 型の変数に入れることが出来ません。
これの何が不便かというと、`enum` は UIKit でよく使われていて、[ReactiveCocoa](https://github.com/ReactiveCocoa/ReactiveCocoa) を Swift で使う際に
`enum` で設定する例えば `UITableViewCellAccessoryType` なんかを使いたい場合にコンパイルエラーになってしまいます。

```swift
// これはコンパイルエラーになる
RAC(self, "cell.accessoryType") = RACObserve(self, "viewModel.checked").map { ($0 as Bool) ? UITableViewCellAccessoryType.Checkmark : UITableViewCellAccessoryType.None }

// こうしないといけない。。
RACObserve(self.viewModel, "checked")
    .subscribeNext { [weak self] arg in
        let checked = arg as Bool
        self!.accessoryType = checked ? .Checkmark : .None
}
```

既存の Objective-C ライブラリで `id` が使われている箇所が、Swift では `AnyObject` に対応するので、
Closure 等で `id` を引数に取ったり、`id` を戻り値にしていたりする箇所が `AnyObject` になってしまい、
`enum` を利用することができません。

変数に代入するだけなら `Any` 型にすればいいんですが、既存ライブラリとの連携では難しそうです。。

これ、何かやり方ないんでしょうかね。。
