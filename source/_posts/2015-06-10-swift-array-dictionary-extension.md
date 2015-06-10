---
layout: post
title: "Swift の Array と Dictionary の Extension（each と map を実装した）"
date: 2015-06-10 12:01:16 +0900
comments: true
categories: [Programming]
keywords: "Swift, Array, Dictionary, each, map"
tags: [Swift]
sharing: true
published: true
amazon_url: ""
amazon_author: ""
amazon_image: ""
amazon_publisher: ""
og_image: ""

---

## Array に each を実装してみた。

```swift
extension Array {
    func each(block: (T) -> ()) {
        for item in self {
            block(item)
        }
    }

    func each<U>(block: (U) -> ()) {
        for item in self {
            block(item as! U)
        }
    }

    func eachWithIndex(block: (T, Int) -> ()) {
        for (i, item) in enumerate(self) {
            block(item, i)
        }
    }

    func eachWithIndex<U>(block: (U, Int) -> ()) {
        for (i, item) in enumerate(self) {
            block(item as! U, i)
        }
    }
}
```

使い方はこんな感じ。

```swift
[1, 2, 3].each { (n: Int) in println(n) }
[1, 2, 3].each { println($0) }
[1, 2, 3].eachWithIndex { (n: Int, i: Int) in println("index \(i), n \(n)") }
[1, 2, 3].eachWithIndex { println("index \($1), n \($0)") }
```

配列の中身が `AnyObject` だった時に、型指定するときとしない時でどっちも動くようにするのに、メソッドを2つ定義しました。

このへん、*Generics* の達人のかたに、もっといい書き方あるよって教えて欲しいです。


## Swift の Dictionary に map を実装してみた。

ついでに `reduce` も。

```swift
extension Dictionary {
    func map<T>(transform: (Key, Value) -> T) -> [T] {
        return Swift.map(self, transform)
    }

    func reduce<T>(initial: T, combine: (T, (Key, Value)) -> T) -> T {
        return Swift.reduce(self, initial, combine)
    }
}
```

使い方はこんな感じ。

```swift
var dict = [String: String]()
dict["hello"] = "world"

let array = dict.map { (k: String, v: String) -> String in
    return v
}
println(array)      // ["world"]


dict = [String: Int]()
dict["a"] = 1
dict["b"] = 2
dict["c"] = 3
dict["d"] = 4
dict["e"] = 5
let n = dict.reduce(0) { (n, d) -> Int in n + d.1 }    // 15
```

Ruby の map っぽい感じで使えるにしたかったので。
