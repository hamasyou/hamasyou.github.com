---
layout: post
title: "[iOS] ReactiveCocoa の RACCommand で、実行中にローディングを出す方法"
date: 2014-09-19 18:13:11 +0900
comments: true
categories: [Programming]
keywords: "iOS,ReactiveCocoa,RACCommand,Loading,Indicator"
tags: [iOS,ReactiveCocoa]
sharing: true
published: true
amazon_url: ""
amazon_author: ""
amazon_image: ""
amazon_publisher: ""
og_image: ""

---

最近 [ReactiveCocoa](https://github.com/ReactiveCocoa/ReactiveCocoa) を使いまくってます。そのなかで調べた Signal の使い方イディオムのメモです。

RACCommand で検索処理とかしてるときに、ローディング画面やインジケータを表示したいということがあると思います。そういうときに使える Signal のイディオムです。コードは Swift で書いています。

*MVVM* で viewModel が searchCommand を実装しているとします。また、ローディングインジケータの表示には [MBProgressHUD](https://github.com/jdg/MBProgressHUD) を使っているとします。

```swift MyViewController#viewDidLoad
searchButton.rac_command = viewModel.searchCommand
searchButton.rac_command.executing
    .subscribeNext({ [weak self] (executing) in
        if let weakSelf = self {
            if executing as Bool {
                MBProgressHUD.showHUDAddedTo(weakSelf.view, animated: true)
            } else {
                MBProgressHUD.hideHUDForView(weakSelf.view, animated: true)
            }
        }
    })
searchButton.rac_command.errors
    .subscribeNext({ (error) in
        // エラー時のメッセージ表示処理
        println(error.localizedDescription)
    })
```

`executing` Signal はこういう風につかうんですね。
