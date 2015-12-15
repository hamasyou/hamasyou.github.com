---
layout: post
title: "[iOS/Swift]ReactiveCocoa 3.0 で ViewModel から Signal をセルフ・コントロールするイディオム"
date: 2015-08-13 11:57:28 +0900
comments: true
categories: [Programming]
keywords: "iOS,Swift,ReactiveCocoa,ViewModel,Signal,pipe"
tags: [iOS,Swift,メモ]
sharing: true
published: true
amazon_url: "https://www.amazon.co.jp/dp/B018GGYUDM?tag=sorehabooks-22&camp=243&creative=1615&linkCode=as1&creativeASIN=B018GGYUDM&adid=1SJX75TJX473M6KG85MC&"
amazon_title: "Reactive Programming with Swift [Kindle版]"
amazon_author: "Cecil Costa"
amazon_image: "http://ecx.images-amazon.com/images/I/51KsmrdZneL.SS300_.jpg"
amazon_publisher: "Packt Publishing"
og_image: "http://ecx.images-amazon.com/images/I/51KsmrdZneL.SS300_.jpg"

---

[ReactiveCocoa](https://github.com/ReactiveCocoa/ReactiveCocoa) が v.3.0-RC.1 がでて、そろそろ製品に Swift で ReactiveCocoa が使えるようになりそうな感じになってきました。

いつも `Signal` を自分で制御するイディオムを忘れてしまうので、メモ書きです。

```swift
import Foundation
import UIKit
import ReactiveCocoa

class MyViewModel {
    let (changed, sink) = Signal<Void, NoError>.pipe()
    
    func doAction() {
        // do something
        sendNext(sink, ())
    }    
}


class MyViewController : UIViewController {
    let viewModel = MyViewModel()

    override func viewDidLoad() {
        super.viewDidLoad()
        
        viewModel.changed |> observe(next: debugPrintln)
        
        viewModel.doAction()              
    }
}
```
