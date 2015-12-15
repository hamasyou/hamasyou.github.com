---
layout: post
title: "[Swift] RxSwift で iBeacon の RangeBeaconsInRegion を 5秒間隔で受け取る方法"
date: 2015-11-25 15:25:47 +0900
comments: true
categories: [Programming]
keywords: "Swift,RxSwift,CoreLocation,iBeacon,Range,interval"
tags: [Swift,iOS,CoreLocation,iBeacon,Range]
sharing: true
published: true
amazon_url: ""
amazon_author: ""
amazon_image: ""
amazon_publisher: ""
og_image: ""

---

CoreLocation を使って iBeacon の Ranging を行う場合、iOS だと 1秒間隔で通知されます。

[[参考] iBeacon(3) - リージョン監視とレンジング - Enamel Systems](http://enamelsystems.com/0011/z)

そこで、1秒間隔だと通知間隔が短すぎるので、5秒間隔とか1分間隔とかに変えたい時にどうするかですが、`CLLocationManager` クラスに設定があればよかったのですが、特になさそうなので、`RxSwift` を使ってストリームのフィルタで対応する方法のメモです。

`CLLocationManager` の初期設定とかは参考サイトを見てください。

[ReactiveX/RxSwift](https://github.com/ReactiveX/RxSwift/)

```swift
locationManager.rx_didRangeBeaconsInRegion
    .sample(interval(5, MainScheduler.sharedInstance))
    .subscribeNext { (tuple: ([CLBeacon]!, CLBeaconRegion!)) in
        debugPrint("beacons: \(tuple.0), region: \(tuple.1)")
    }
    .addDisposableTo(disposeBag)
```

`Rx` の `sample` を使って、ストリームを `interval` 毎にサンプリングしてやるだけです。

[Time-shifted sequences - Introduction to Rx](http://www.introtorx.com/content/v1.0.10621.0/13_TimeShiftedSequences.html#Sample)

`RxSwift` も使いやすくて、めっちゃ便利です！
