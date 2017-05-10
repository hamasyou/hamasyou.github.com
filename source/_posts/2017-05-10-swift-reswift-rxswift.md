---
layout: post
title: "[iOS] ReSwift の State を RxSwift っぽく使えるようにする"
date: 2017-05-10 11:08:30 +0900
comments: true
categories: [Blog]
keywords: "iOS,Swift,ReSwift,RxSwift"
tags: [iOS,Swift,ReSwift,RxSwift]
amazon_url: ""
amazon_title: ""
amazon_author: ""
amazon_image: ""
amazon_publisher: ""
og_image: ""

---

Swift ライブラリの [ReSwift](https://github.com/ReSwift/ReSwift) を [RxSwift](https://github.com/ReactiveX/RxSwift) に合わせて使えるようにするメモです。

## ReSwift とは

ReSwift とは、iOS アプリを [Redux](https://github.com/reactjs/redux) のように作れるようにしてくれるライブラリです。

スマホアプリを作っていると

- アプリの状態管理を一元管理したい
- アプリの状態が変わったら、UI を最新に更新したい

という要求が出てくると思います。そんなときに、ReSwift でステートを一元管理し、UI の更新を RxSwift の I/F に合わせて使えると結構便利です。

<!-- more -->

## ソースコード

ということで、メモ書きなので、さっとソースを貼り付けておきます。

```swift

/// アプリの `Store` クラス
/// Rx 拡張を行うために独自クラスを作成する
class AppStore : Store<AppState> {

    required convenience init(reducer: @escaping (Action, AppState?) -> AppState, state: State?) {
        self.init(reducer: reducer, state: state, middleware: [])
    }

}

/// AppStore を Rx 化するためのプロキシ
class AppStoreProxy<SelectedState> : StoreSubscriber {

    typealias StoreSubscriberStateType = SelectedState

    private weak var source: AppStore?
    private(set) var subject = PublishSubject<StoreSubscriberStateType>()

    fileprivate init(source: AppStore) {
        self.source = source
    }

    func newState(state: StoreSubscriberStateType) {
        subject.on(.next(state))
    }

    deinit {
        subject.on(.completed)
    }
}


extension AppStore : ReactiveCompatible {
}


extension Reactive where Base: AppStore {

    func state() -> Observable<AppStore.State> {
        return state { $0 }
    }

    func state<SelectedState: StateType>(selector: @escaping ((AppStore.State) -> SelectedState)) -> Observable<SelectedState> {
        return Observable.create { observer in
            let proxy = AppStoreProxy<SelectedState>(source: self.base)
            _ = proxy.subject.bind(to: observer)

            self.base.subscribe(proxy, transform: { (subscriber: Subscription<AppState>) -> Subscription<SelectedState> in
                subscriber.select(selector)
            })

            return Disposables.create {
                self.base.unsubscribe(proxy)
            }
        }
    }

    func state<SelectedState: StateType & Identifiable>(selector: @escaping ((AppStore.State) -> SelectedState)) -> Observable<SelectedState> {
        return Observable.create { observer in
            let proxy = AppStoreProxy<SelectedState>(source: self.base)
            _ = proxy.subject.bind(to: observer)

            self.base.subscribe(proxy, transform: { (subscriber: Subscription<AppState>) -> Subscription<SelectedState> in
                subscriber.select(selector)
            })

            return Disposables.create {
                self.base.unsubscribe(proxy)
            }
        }
    }
}
```

これで、ReSwift の `State` を次のように書くことができます。

```swift
store.rx.state { $0.userState }
  .map { $0.name }
  .bind(to: nameLabel.rx.text)
  .disposed(by: disposeBag)
```


## オブザーブしているステートが更新されたときのみ通知して欲しい時

ReSwift を RxSwift のように使うだけであれば、上の拡張でいいのですが、たくさんステートが出来てくると、
関係ないステートが更新されたときにも変更が通知されてしまって、パフォーマンスを気にすることが出てくるかもしれません。

そんなときは、`distinctUntilChanged` を使って、監視しているステートが更新されたかどうかを確認するようにするといいと思います。

`State` 自体を `==` で比較できるように、State に `Identifier` を導入して、更新されたかどうかをチェック出来るようにしてみます。
(というのも、State は構造体(`struct`) で作ることになると思うので、同一のステートかどうかの一致が大変なのです。)

### State に一意性を持たせる

```swift
/// 識別子コンポーネント
struct IdentifiableComponent : Hashable {

    typealias Identifier = UInt64

    /// インスタンス生成毎に一意になる値を生成するクラス
    private struct Counter {
        static let lock = DispatchSemaphore(value: 1)
        static var count: Identifier = 0
        static func getAndIncrement() -> Identifier {
            lock.wait()
            defer { lock.signal() }
            count += 1
            return count
        }
    }

    /// インスタンスの識別子
    private(set) var identifier: Identifier = Counter.getAndIncrement()

    var hashValue: Int { return identifier.hashValue }

    mutating func update() {
        identifier = Counter.getAndIncrement()
    }

    static func ==(lhs: IdentifiableComponent, rhs: IdentifiableComponent) -> Bool {
        return lhs.identifier == rhs.identifier
    }
}

protocol HasIdentifiableComponent : Equatable {
    var identifiableComponent: IdentifiableComponent { get }
}

/// 識別子を持つタイプ
protocol Identifiable : HasIdentifiableComponent {
}

extension Identifiable {

    var identifier: IdentifiableComponent.Identifier {
        return identifiableComponent.identifier
    }    

    static func ==(lhs: Self, rhs: Self) -> Bool {
        return lhs.identifiableComponent == rhs.identifiableComponent
    }
}
```

複雑そうなことをやっていますが、要はインスタンスを生成するたびに一意の数値を割り当てて、
状態が更新されたらその値をインクリメントするという方法で、数値比較だけで状態が変わったかを判断できるようにしています。

この識別子コンポーネントを State に持たせるようにして

```swift

struct UserState : StateType, Identifiable {

  fileprivate(set) var identifiableComponent = IdentifiableComponent()

  var name: String?
}

// MARK: - Reducer
extension UserState {    
    static func reducer(state: UserState?, action: Action) -> UserState {
        var state = state ?? UserState()

        switch action {
        case let action as UserActions.SetName:
            state.name = action.name
            state.identifiableComponent.update()

        default:
            break
        }

        return state
    }
```

こんな感じで、ステートが更新したときに、`identifiableComponent.update()` を呼び出すと状態が更新されたことをマークします。
これで、ステートが更新されたときだけ UI を更新するようなコードを次のように書けるようになります。

```swift
store.rx.state { $0.userState }
  .distinctUntilChanged()
  .map { $0.name }
  .bind(to: nameLabel.rx.text)
  .disposed(by: disposeBag)
```

パフォーマンスも気にせず使えるようになりました。

既存のプロジェクトからの抜粋なので、一部省略している箇所があります。
