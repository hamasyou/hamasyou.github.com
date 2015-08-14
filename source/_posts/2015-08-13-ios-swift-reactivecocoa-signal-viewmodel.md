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
amazon_url: ""
amazon_author: ""
amazon_image: ""
amazon_publisher: ""
og_image: ""

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
