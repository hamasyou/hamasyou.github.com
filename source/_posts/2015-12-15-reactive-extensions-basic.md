---
layout: post
title: "Reactive Extensions Basic"
date: 2015-12-15 09:53:12 +0900
comments: true
categories: [Programming]
keywords: "Reactive Extensions,Rx,RxSwift"
tags: [Reactive,Rx]
sharing: true
published: true
amazon_url: ""
amazon_author: ""
amazon_image: ""
amazon_publisher: ""
og_image: ""

---

**Reactive Extensions (Rx)** の覚書です。主に RxSwift をベースにしていますが、ReactiveX 全般に通じると思います。

細かいニュアンスは間違っているかも。。

基本用語の整理です。

<!-- more -->

<div id="toc"></div>

## Reactive Extensions

大雑把に言うと、**Observer Pattern (オブザーバパターン)** と **Stream/Sequence Programming** を合わせたものが **Reactive Extensions** です。

### Observable

オブザーバパターンにおいて、観測される対象を Subject と呼びますが、Rx では観測される対象のことを **Observable** と呼びます。観測する側は *Observer (オブザーバ）* なので、観測される側は *Observable* なわけです。

Rx では、Observable なものを `subscribe` (購読) することで、Observable から送られてくるデータを受け取ります。この、Observable からデータが流れてくることを **Stream (ストリーム)** と呼びます。流れてくるデータは **Element** と呼ばれます。

```swift
someObservable
    .subscribe { (e: Event<Element>) in
        switch e {
        case .Next(let val):
        case .Error(let error):
        case .Completed:
        }
    .scopedDispose()
```

ストリームには状態が3種類あり、それぞれ `Next`、`Error`、`Completed` になります。

#### Next

ストリームから次のデータが流れてきたことを表します。正常処理と考えて構いません。

#### Error

Observable な対象が何かしらエラーを起こし、観測したかったデータが流れてこなかったことを表します。Rx では、Error を起こしたストリームは閉じられます。つまり、次のデータが流れてこないことを表します。

#### Completed

Observable な対象のストリームが閉じられ、この Observable な対象からは今後データが流れてこないことを表します。


#### dispose

観測を取りやめたい時には、`dispose` を呼び出します。いつ dispose するかを使い分けられるように、`Dispose` の実装がいろいろあります。

### Subject

**Observable** は観測対象のことでしたが、Rx においては観測可能な事柄のことを **Subject (事象)** と呼ぶほうが一般的だったりします。Subject を観測するわけですが、観測するデータのことは **Behavior (振る舞い)** と呼ばれます。`Subject` / `Behavior` は Reactive Extensions における一般的な説明となりますが、こと Rx Programming においては、`Observable` と `Subject`、`Element` と `Behavior` は似て非なるものとして扱われています。

## Hot and Cold Observables

Observable な対象は、2つの種類にわけられます。`Hot` と `Cold` です。

### Hot Observable

Hot な Observable とは、観測対象である Observable が作られたときから、任意のタイミングでデータを送信することができるものになります。

実はこの Hot Observable のことを、Rx Programming では *Subject* と呼ばれています。Subject は任意のタイミングでデータをストリームに流すことができ、同時に Observable なわけです。

Subject は任意のタイミングで **Behavior (振る舞い)** を決められることから、Subject / Behavior はセットで使われる言葉になります。

```swift
    let msg = PublishSubject<String>()
    
    msg.subscribeNext { print($0) }
    
    msg.onNext("Hello World")
    msg.onCompleted()
```

### Cold Observable

Cold な Obserable は、任意のタイミングでデータを流すことができません。いつデータが流れてくるのかわかりませんし、観測対象がいなければ、そもそも振る舞いが起きることもありません。

よく、API 通信を Observable にしますが、subscribe (観測) を忘れると、API 呼び出しがされないことになりますので注意が必要です。

```swift
    API.call()
        .subscribeNext {
            // When to occur?
        }        
```


## Rx でプログラミングすると何が嬉しいか

### テスタビリティが向上

Rx は UI プログラミングで語られることが多いと思いますが、プログラムのあらゆる境界を Observable でつなぐことで、テスタビリティが向上します。

Observable でつなぐことにより、直接のデータの生成元に依存せずにプログラムを記述することができるようになるので、テスト時だけデータ生成部分をいじることで、例えば API 通信結果を画面に表示するようなプログラムでも、API 通信を行うことなくデータを生成し、Stream に流すことで色々な依存関係を排除することが出来るのです。

```swift
func something(dataSource: Observable<DataSource>) {
    dataSource.
        subscribeNext {
            // do something
        }
}

// development
something(API.call())
// test
something(just(DummyDataSource()))

```

### 非同期処理

また、Rx には Stream の観測を任意のスレッドで行うスケジューリングの機能が備わっているものがほとんどですので、時間の掛かるデータ生成処理はバックグラウンドで行い、UI の表示処理のところだけメインスレッドで行うといったことを容易に行なえます。

```swift
API.call()
    .map { $0.toViewModel() }
    .subscribeOn(BackgroundScheduler.sharedInstance)    
    .observeOn(MainScheduler.sharedInstance)
    .subscribeNext { (viewModel) in
        // UI バインディング
    }
    .addDisposeBag(disposeBag)    
```

### データソースの更新をリアルタイムに同期

プログラムを書いていると、データソースの値を加工して別のところで使うという場面が多々あります。

このとき、何も考えずにプログラムすると、データソースが変更されても結果は変わらないという挙動になります。

```swift
var a = 1
var b = 2
var c = ""

if a + b >= 0 {
    c = "\(a + b) is positive"
}

a = 4

print(c)        // ""
```

こういった挙動でかまわないという場合もあるでしょうが、ほとんどの場合データソースの変化に合わせて挙動が変わってくれたほうが嬉しかったりします。そんな時は全部 Observable にします。

```swift
let a = Variable(1)
let b = Variable(2)

let c = combineLatest(a, b) { $0 + $1 }
            .filter { $0 >= 0 }
            .map { \($0) is positive" }            

c.subscribeNext {
    print($0)
}

a.value = 4
```

## Operators

Rx には、Stream を操作するためのいろいろな演算が用意されています。Stream を別の形に変換する `map` や、複数の Stream を結合する `merge` や `concat` がなどがあります。

Stream の合成は使いドコロとしては API 通信などの時間の掛かる処理とキャッシュデータを返すストリームとを合成するなどが考えられます。

```swift
sequenceOf(1, 2, 3, 4, 5)
    .map { $0 * $0 }
    .subscribeNext {
        print($0)
    }
    .scopedDispose()
    

let dataFromCache = CacheStore.fromCache()
dataFromCache.concat(API.call())
    .subscribeNext {
    }
    .addDisposeBag(disposeBag)
```

