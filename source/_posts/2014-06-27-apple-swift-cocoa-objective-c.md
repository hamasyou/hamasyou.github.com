---
layout: post
title: "Using Swift with Cocoa and Objective-C"
date: 2014-06-27 00:03:15 +0900
comments: true
categories: [Programming]
keywords: "Swift,Apple,Cocoa,Objective-C"
tags: [Swift,Apple,Cocoa,Objective-C]
sharing: true
published: true
amazon_url: ""
amazon_author: ""
amazon_image: ""
amazon_publisher: ""
og_image: ""

---

**Swift** を Cocoa や Objective-C と一緒に使う方法の覚書です。

なお、Swift で作成したアプリは iOS8 と OS X Yosemite がリリースされた時点で審査に入れるようです。

[Using Swift with Cocoa and Objective-C](https://developer.apple.com/library/prerelease/ios/documentation/Swift/Conceptual/BuildingCocoaApps/index.html)

<!-- more -->

<div id="toc"></div>

## Basic Setup

Swift から Objective-C API を呼び出すことができる。逆に、Objective-C から Swift のコードを呼び出すこともできる。

Swift と Objective-C のプロジェクトの大きな違いは、Swift にはヘッダーファイルがない。Swift には全ての情報が `.swift` ファイルに含まれている。

### import

Swift でもフレームワークやライブラリを読み込むには `import` を使う。

```swift
import Foundation
```

`Foundation` を読み込むことで、NSDate, NSURL, NSMutableData などを含む全ての Foundation API、全てのメソッド、プロパティ、カテゴリを利用出来るようになる。

### Objective-C と Swift のマッピング

- Objective-C の型は同等の Swift の型にマッピングされる。`id` は `AnyObject`
- Objective-C のコア型は Swift の型にマッピングされる。`NSString` が `String` のように
- Objective-C のコンセプト機能は同等の Swift のコンセプト機能にマッピングされる。ポインタが Optionals と対応するように

C++ のコードは直接 Swift には取り込めない。代わりに Objective-C か C で作ったラッパーを作り、それを取り込む。


## Interoperability

### Initialization

Objective-C の `init` メソッド、`initWith` メソッドは、`init` で切られて `With` の部分が捨てられる。捨てられて残った部分が第一引数のパラメータ名に対応する。

```objective-c Objective-C
UITableView *myTableView = [[UITableVIew alloc] initWithFrame:CGRectZero style:UITableViewStyleGrouped];
```

```swift Swift
let myTableView: UITableView = UITableView(frame: CGRectZero, style: .Grouped)
```

Objective-C のファクトリメソッドは `convenience initializer` にマッピングされている。

```objective-c Objective-C
UIColor *color = [UIColor colorWithRed:0.5 green:0.0 blue:0.5 alpha:1.0];
```

```swift Swift
let color = UIColor(red: 0.5, green: 0.0, blue: 0.5, alpha: 1.0)
```

### Methods

Objective-C のメソッド名がそのまま Swift のメソッドになり、第一引数名が省略される。残りの引数名はそのまま残る。

```objective-c Objective-C
[myTableView insertSubview:mySubview atIndex:2];
```

```swift Swift
myTableView.insertSubview(mySubview, atIndex: 2)
```


### id

Swift ではどんな型も表すタイプとして `AnyObject` を用意している。Objective-C の `id` は `AnyObject` に対応する。

Objective-C のメソッドを呼び出すのに、キャストは必要ない。`@objc` 属性がメソッドに修飾されているので、そのまま呼び出すことができる。

```swift
let futureDate = myObject.dateByAddingTimeInterval(10)
let timeSinceNow = myObject.timeIntervalSinceNow
```

AnyObject から `as` によるキャストでは、ダウンキャストが成功するかどうかの保証ができない。そのため、`if-let` 構文を使ってチェックすること。

```swift
let userDefaults = NSUserDefaults.standardUserDefaults()
let lastRefreshDate: AnyObject? = userDefaults.objectForKey("LastRefreshDate")
if let date = lastRefreshDate as? NSDate {
    println("\(date.timeIntervalSinceReferenceDate)")
}
```


### Closure and Block

Objective-C の `block` は Swift の `Closure` に対応する。

```objective-c Objective-C
void (^completionBlock)(NSData *, NSError *) = ^(NSData *data, NSError *error) {/* ... */}
```

```swift Swift
let completionBlock: (NSData, NSError) -> Void = {data, error in /* ... */}
```


### @objc アノテーション

@objc アノテーションを使うことで、Swift のコードを Objective-C の中から呼び出すことができるようになる。

`@IBOutlet`、`@IBAction`、`@NSManaged` を使うと暗黙的に @objc が付与される。


### Swift コードから Objective-C コードへの変換

メソッドはほとんどそのままマッピングされる。init コンストラクタは、第一引数に `initWith` がつくように変換される。

```swift Swift
func playSong(name: String)
init(songName: String, artist: String)
```

```objective-c Objective-C
- (void)playSong:(NSString *)name
- (instancetype)initWithSongName:(NSString *)songName artist:(NSString *)artist
```


### Selector

Objective-C のセレクタは Swift では Selector 構造体にマッピングされる。

```swift
let mySelector: Selector = "tappedButton:"
```

```swift
import UIKit
class MyViewController: UIViewController {
    let myButton = UIButton(frame: CGRect(x: 0, y: 0, width: 100, height: 50))
    
    init(nibName nibNameOrNil: String!, bundle nibBundleOrNil: NSBundle!) {
        super.init(nibName: nibName, bundle: nibBundle)
        myButton.targetForAction("tappedButton:", withSender: self)
    }
    
    func tappedButton(sender: UIButton!) {
        println("tapped button")
    }
}
```


## Writing Swift Classes with Objective-C Behavior

Objective-C のプロトコルは Swift の protocol に対応する。Objective-C の NSObject プロトコルは `NSObjectProtocol` になっている。


### Integrating with Interface builder

Swift でも Interface Builder の機能は利用出来る。Outlet、Actions、ライブレンダリングは Swift でも可能。

Swift では @IBOutlet、@IBAction をプロパティやメソッドに付与する。

```swift
class MyViewController: UIViewController {
    @IBOutlet var button: UIButton
    @IBOutlet var textFields: UITextField[]
    @IBAction func buttonTapped(AnyObject) {
        println("button tapped!")
    }
}
```

`@IBDesignable` と `@IBInspectable` をつかうと Interface Builder で編集が出来る。

```swift
@IBDesignable
class MyCustomView: UIView {
    @IBInspectable var textColor: UIColor
    @IBInspectable var iconHeight: CGFloat
    /* ... */
}
```


## Working with Cocoa Data Types

### String（NSString）、Number（NSNumber）

NSString を利用したい場合は `as` を使ってキャストする。もしくは、最初から `NSString` 型を使う。

```swift
let str = "123"
(str as NSString).floatValue

import Foundation
let myString: NSString = "123"
if let integerValue = (myString as String).toInt() {
    println("\(myString) is the integer \(integerValue)")
}
```

NSNumber も同じ。

```swift
let n = 42
let m: NSNumber = n
```


### Array

NSArray は `AnyObject[]` にマッピングされる。


### Foundation Data Types

NSSize、NSPoint はそのまま利用出来る。

```swift
let size = NSSize(width: 20, height: 20)
let rect = NSRect(x: 50, y: 50, width: 100, height: 100)
```


### NSLog

NSLog はそのままの構文で利用出来る。

```swift
NSLog("%.7f", pi)
```


### NSError and Error Reporting

```swift
var writeError : NSError?
let written = myString.writeToFile(path, atomically: false,
    encoding: NSUTF8StringEncoding,
    error: &writeError)
if !written {
    if let error = writeError {
        println("write failure: \(error.localizedDescription)")
    }
}

func contentsForType(typeName: String! error: NSErrorPointer) -> AnyObject! {
    if cannotProduceContentsForType(typeName) {
        if error {
            error.memory = NSError(domain: domain, code: code, userInfo: [:])
        }
        return nil
    }
    // ...
}
```
