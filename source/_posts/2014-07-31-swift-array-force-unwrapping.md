---
layout: post
title: "SwiftでForce Unwrapping型の配列に値を追加できない"
date: 2014-07-31 11:28:11 +0900
comments: true
categories: [Programming]
keywords: "Swift,Apple,Lang,Force Unwrapping,!"
tags: [Swift]
sharing: true
published: true
amazon_url: ""
amazon_author: ""
amazon_image: ""
amazon_publisher: ""
og_image: ""

---

この記事は Xcode6 beta4 を元に記述しています。

### Swift の配列は mutable か immutable

Swift では配列を **mutable/immutable** の区別なく定義することができるようになりました。

```swift
var numbers = [1, 2, 3]
numbers[0] = 0
println(numbers)    // [0, 2, 3]
```

`let` で定義すると **immutable**、`var` で定義すると **mutable** になり、配列の代入はすべてコピーで行われるようになっています。


### Force UnWrapping 型!

で、ハマったのが次のようなコードです。

```swift
var numbers: [Int]! = [1, 2, 3]
numbers[0] = 0    // error: '@lvalue $T7' is not identical to 'Int'
```

Force Unwrapping 型とでも言うんでしょうかね？変数に必ず値が入ってることを保証するために `!` が付いた型です。
この型で定義した配列には、値の追加や変更ができなくなっています。。

これで何が困るかというと、`UIKit` 使って `UIViewController` のサブクラスにプロパティを定義する際に
初期化を `viewDidLoad` で行う時には、`!` をつけないと `initializer` が必要になってしまうので、プロパティの定義には `!` を付けていました。

そうすると、*mutable* で扱いたかったプロパティなのに、変更できないという問題にぶち当たったわけです。。どうすんだこれ。。。

#### コンパイルエラーの例

```swift
class MyViewController: UIViewController {

  var numbers: [Int]!
  
  override func viewDidLoad() {
    super.viewDidLoad()
    
    self.numbers = [1, 2, 3]
  }
  
  
  func myFunc() {
    self.numbers[0] = 0     // コンパイルエラー
  }
}
```

#### うまくいく例 

対応としては、一旦変数で受けて、変更後に元に戻すっていうので何とかなりますが、コンパイラの方でなんとかならんもんですかね。。

```swift
  func myFunc() {
    var nums: [Int] = self.numbers
    nums[0] = 0
    self.numbers = nums
  }
```


