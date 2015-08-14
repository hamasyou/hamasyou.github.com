---
layout: post
title: "[iOS] Auto Layout を使いこなす。UITableViewCell と UIScrollView 編"
date: 2014-10-09 01:31:20 +0900
comments: true
categories: [Programming]
keywords: "ios,autolayout,swift,uitableviewcell,uiscrollview"
tags: [iOS,AutoLayout,Swift]
sharing: true
published: true
amazon_url: ""
amazon_author: ""
amazon_image: ""
amazon_publisher: ""
og_image: ""

---
iPhone6 と iPhone6 Plus が発売になり、本格的に iOS でも Android のように複数解像度に対応したやり方をしないといけなくなってきました。
iPhone5S までは、縦幅が伸びただけだったので、なんとなく `Auto Layout` を使っていても問題は表面化しづらかったのですが、
iPhone6 で横幅が伸びてしまったことで、適当に `Auto Layout` を使ってコーディングしていると残念なことになっているアプリが結構あります。

僕も iPhone アプリを開発しているのでこのあたりは結構気を使ってはいるんですが、いかんせん自分のやり方がほんとに正しいのか、やや疑問なところもあります。
そこで、今の自分のやり方を晒して、世の iPhone アプリ開発者の人に突っ込んでもらえればと思い記事を書くことにしました。

間違っている箇所もあると思いますので、ドンドンツッコミをお願いします！

<!-- more -->

## この記事で何が出来るようになりたいか

このまとめ記事で、何が出来るようになりたいか。それは次の2点です。

1. `UITableViewCell` （可変長高さ）を `Auto Layout` を使ってキレイにつくりたい
2. `UIScrollView` （可変コンテンツ）を `Auto Layout` を使ってキレイにつくりたい

こんな感じのやつです。

{% img /images/20141008/SS1.png 240 "可変長UITableViewCell" %}
{% img /images/20141008/SS2.png 240 "Auto Layout を使った横スクロールの UIScrollView" %}
{% img /images/20141008/SS3.png 240 "Auto Layout を使った横スクロールの UIScrollView" %}

`UILabel` の中身によって `UITableViewCell` の高さが変わるやつと、`UIScrollView` の中身をページングで切り替えるときに中身の個数を動的に差し替えるやつです。

それぞれ作り方をさらします。

ソースコードは

[hamasyou/autolayout](https://github.com/hamasyou/autolayout)

にあるので、参考にしたい方はどうぞ。


## iOS8 以降だけに対応するのであれば

ちなみに、iOS8 以降だけに対応するのであれば、`TableViewCell.xib` で `ContentView` に対して何も考えずに Auto Layout でレイアウトして、`TableViewController` を継承したクラスで次のように `tableView.rowHeight` に `UITableViewAutomaticDimension` を設定し、`tableView:estimatedHeightForRowAtIndexPath:` で `UITableViewAutomaticDimension` を返すようにするだけで解決します。

```swift
class MyViewClass : UIViewController, UITableViewDelegate {
    @IBOutlet weak var tableView: UITableView!
  
    override func viewDidLoad() {
        super.viewDidLoad()
        tableView.rowHeight = UITableViewAutomaticDimension
    }

    func tableView(tableView: UITableView, estimatedHeightForRowAtIndexPath indexPath: NSIndexPath) -> CGFloat {
        return UITableViewAutomaticDimension
    }
}
```

`tableView` に `estimatedRowHeight` というプロパティがありますが、これに `UITableViewAutomaticDimension` を設定して、`tableView:estimatedHeightForRowAtIndexPath:` を実装しない方法だと上手くいきません。バグなんじゃないか？？。。。

## Auto Layout のおさらい

作り方を晒す前に、まずは **Auto Layout** のおさらいをしておきます。

Auto Layout は Storyboard やプログラムから「制約」を利用してレイアウトを組み立てるものです。基本的に Auto Layout に対応したアプリでは、
`UIView(frame: CGRectMake(xx, xx, xx, xx))` みたいな作成方法はしなくなります。

Auto Layout を使うと、"画面のこのコンポーネントから◯pxずらしてこのコンポーネントを配置する" や "このコンポーネントとこのコンポーネントのサイズを同じにする"
みたいなことをなんとなく直感的に記述（または、設定）できます。

Auto Layout を Strobyboard 上で設定すると、その制約が実際にコンポーネントに反映されるのは、`UIViewController#viewDidLayoutSubviews` のタイミングになるようです。


## UITableViewCell での Auto Layout の作用

UITableViewCell で Auto Layout を使うときに注意しなければならないのは、`awakeFromNib` のタイミングでは、セルの Auto Layout が設定されていないということです。

まぁ、これは当然といえば当然で、カスタムの UITableViewCell は再利用を前提に作られるので、実際にセルのコンテンツが設定されるは `UITableViewDataSource#cellForRowAtIndexPath` の段階になるからです。
このメソッドの中でセルのコンテンツを設定するわけです。


### セルの高さが問い合わせられるのはセルのコンテンツを設定する前！！

で、まぁ普通に UITableView を使っているとドハマリするのが、セルの高さを返す `UITableViewDelegate#heightForRowAtIndexPath` のメソッドが呼び出されるのが、`cellForRowAtIndexPath` の前ということです。
セルの高さを計算できるのはセルのコンテンツが設定された後なんじゃないの？って思うわけですが、高さの問い合わせが来るのはセルのコンテンツを設定する前なのです。。

じゃあ、どうするかというと、僕はもう割りきって、`heightForRowAtIndexPath` の中で `cellForRowAtIndexPath` を呼び出して、セルにコンテンツを設定した後にセルの高さを計算するようにしています。

```swift UITableViewController

    // MARK: - UITableViewDataSource
    
    func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return 2
    }
    
    func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCellWithIdentifier("customCell") as CustomTableViewCell
        
        switch indexPath.row {
        case 0:
            cell.thumbView.image = UIImage(named: "Image1")
            cell.labelView.text  = "いろはにほへとちりぬるをわかよたれそつねならむうゐのおくやまけふこえてあさきゆめみしゑひもせす色はにほへど散りぬるを我が世たれぞ常ならむ有為の奥山今日越えて浅き夢見じ酔ひもせず"
        case 1:
            cell.thumbView.image = UIImage(named: "Image2")
            cell.labelView.text  = "Cozy lummox gives smart squid who asks for job pen."
        default:
            break
        }
        return cell
    }
        
    // MARK: UITableViewDelegate
    
    func tableView(tableView: UITableView, heightForRowAtIndexPath indexPath: NSIndexPath) -> CGFloat {
        let cell = self.tableView(tableView, cellForRowAtIndexPath: indexPath) as CustomTableViewCell
        var newBounds = cell.bounds
        newBounds.size.width = tableView.bounds.width
        cell.bounds = newBounds
        
        cell.setNeedsLayout()
        cell.layoutIfNeeded()
        return cell.preferredView.bounds.height
    }    
    
    func tableView(tableView: UITableView, estimatedHeightForRowAtIndexPath indexPath: NSIndexPath) -> CGFloat {
        return UITableViewAutomaticDimension
    }
```

高さを計算するときに、`cellForRowAtIndexPath` を呼び出してセルにコンテンツを設定した後、セルの width にテーブルビューの width を設定してあげて、一度 cell のレイアウトを更新します。

**セルの width にテーブルビューの width を設定しているのがミソ** です。

Storyboard 上ではカスタムセルの横幅がデフォルトで320pxで作成されるので、iPhone5S まではセルの横幅を気にしなくてもセルの `layoutIfNeeded` だけを呼び出せば勝手に中のコンテンツの高さ決まっているように錯覚していたのですが、
実は Storyboard 上で320pxでセルが作られているのでたまたま偶然いままではうまくいっていただけっぽいです。

本来は、セルの横幅が決まった上でセルのコンテンツの Auto Layout にしたがって `layoutIfNeeded` しないと行けないのです。セルの横幅を設定せずに `layoutIfNeeded` だけでコンテンツの高さに合わせてセルの高さを決めていると、
iPhone6 Plus になったときにセルの高さがおかしくなってしまいます。


### heightForRowAtIndexPath と estimatedHeightForRowAtIndexPath の違い

セルの高さを計算するメソッドは `heightForRowAtIndexPath` ですが `estimatedHeightForRowAtIndexPath` というメソッドも用意されています。
このメソッドは何なのかというと、UITableView は大量のデータを表示する場合でも、表示領域+αの部分だけしかセルをインスタンス化しないという設計になっています。
ですので、`cellForRowAtIndexPath` は表示領域+αの部分分だけ呼び出されるのですが、セルの高さの計算メソッドは、そうではありません。

スクロールバーの範囲を計算したり色々するために、セルの高さの計算は、データがどれだけ大量にあっても全ての数分呼び出されてしまうのです。

で、`heightForRowAtIndexPath` と `estimatedHeightForRowAtIndexPath` の違いですが、`estimatedHeightForRowAtIndexPath` はセルの大まかなサイズを返すメソッドとして用意されています。

`heightForRowAtIndexPath` と `estimatedHeightForRowAtIndexPath` が両方用意されている場合は、全てのセルの高さを一旦 `estimatedHeightForRowAtIndexPath` で計算したあと、
表示領域+α分の回数だけ `heightForRowAtIndexPath` が呼び出されるという形になります。

`estimatedHeightForRowAtIndexPath` を定義しない場合は、全てのデータ分だけ `heightForRowAtIndexPath` が呼び出されるので、データが多い場合は結構きつくなります。
なので、忘れずに `estimatedHeightForRowAtIndexPath` も定義しておきます。

返す値はなんとなくの定数(44pxとか)でもいいですし、`UITableViewAutomaticDimension` を返すでもどちらでもよさそうです。


### setNeedsLayout と layoutIfNeeded は何？

`setNeedsLayout` はそのコンポーネントに、再レイアウトが必要なフラグを設定するメソッドになります。再レイアウトが必要なコンポネントは次のタイミングの `layoutSubviews` の呼び出しの時に再レイアウトされます。

`layoutIfNeeded` はそのコンポーネントが再レイアウトが必要なフラグが立っているかを確認して、再レイアウトが必要であれば `layoutSubviews` を呼び出してくれるメソッドです。

コンポーネントのレイアウトを強制的に更新したい場合は

```swift
view.setNeedsLayout()
view.layoutIfNeeded()
```

のコンボでレイアウトを更新します。この処理を行うと、Auto Layout にしたがってレイアウトが設定されます。

レイアウトを更新したらセルの高さが決まっているはずなので `bounds.height` を返します。


### preferredView って何？

UIView 関連のコンポーネントには、推奨サイズというのが設定されているコンポーネントがあります。例えば UILabel であれば、ラベルの文字数をきっちり表示できるサイズが推奨サイズになりますし、
UIButton であればボタン名が表示できるサイズが推奨サイズになります。

コンポーネントの推奨サイズを取得するメソッドが `UIView#intrinsicContentSize` になります。実はこのメソッド、Auto Layout と深い関係があります。

例えば、UILabel の制約を次のように設定したとします。

{% img /images/20141008/SS4.png 240 "UILabel の制約" %}

親のコンポーネントに対して、Top、Trailing、Leading を 0 に設定しています。こうすることで UILabel が親のコンポーネントの上左右の padding が 0 で表示されるわけです。

ここでのポイントは、bottom に制約を設定していないことです。bottom に制約を設定していないので、本来であればこのコンポーネントは高さが決まらずに ambiguity（曖昧）な状態なはずです。

でも UILabel や UIButton は ambiguity にはなりません。これは、コンポーネントに推奨サイズが設定されているので、そのサイズでコンポーネントが表示されるからです。

UILabel を使って、テキストがどれだけ多くなっても全て表示したいというときには、このように bottom に制約を設定せずに、`Lines` を 0 に、`Line Breaks` を `Character Wrap` に設定します。

{% img /images/20141008/SS5.png 240 "Lines 0 Line Breaks Character Wrap" %}
{% img /images/20141008/SS6.png 240 "Lines 0 Line Breaks Character Wrap" %}


#### 推奨サイズを持たないコンポーネントがある

コンポーネントの中には、推奨サイズを持たないコンポーネントが存在します。`UIView` がそうです。UIView は子のビューのサイズによって自身のサイズが変わるため、
推奨サイズを持ちません。

逆に考えると、中のコンテンツのサイズが動的に変わる場合でも、UIView が親にいる限り、子のコンポーネントは好きにサイズが変わってよく、子のサイズが決まった段階で UIView のサイズ（bounds）が決まるということです。

この性質を使うと、UITableViewCell の中身が複雑に Auto Layout される場合でも、セルの高さを簡単に計算できるようになります。


### セルの中に UIView を配置して、その中にコンテンツを並べる

カスタムの UITableViewCell には、`ContentView` という UIView が予め用意されていますが、この ContentView 、いまいち上手く Auto Layout してくれません。。
やり方が悪いだけのような気もしますが、僕は ContentView の子に UIView を入れるようにして、この UIView の上左右の padding を 0 に設定してレイアウトを始めるようにしています。

{% img /images/20141008/SS7.png 800 "preferredView" %}

bottom の制約を設定しないのがミソです。bottom の制約を設定しないことで、UIView は縦方向に自由に伸び縮みして、子のコンポーネントのサイズに合わせて高さが勝手に変わるようになります。

最後のコンポーネントを配置し終えたら、そのコンポーネントの bottom を親に対して設定をすれば、UIView の高さが子のコンポーネントの Auto Layout から決まるようになります。

{% img /images/20141008/SS9.png 800 %}


### 推奨サイズをもつコンポーネントで位置とサイズで矛盾が起きた場合

上の画像では、最後の要素の UILabel の bottom を親の Preferred View に対して設定していますが、UILabel の推奨サイズの高さと Preferred View からの bottom の位置とでコンフリクトが起きます。

Storyboard 上では、コンフリクトが起きると、赤い矢印マークがでます。実際には UILabel の高さを bottom の位置にまでの高さに設定することで矛盾は起きないのですが、
コレだと、高さが結局 UIView の高さに依存してしまい、UIView の高さは bottom を設定していないことから曖昧なサイズになってしまいます。

これだと思った様なレイアウトになりません。で、こんな時に設定するのが制約の **priority** です。制約同士でコンフリクトが起きた場合に、どちらの制約を優先的に適用するかを決めるのが priority になります。

今回は、*ラベルの高さを推奨サイズで配置して、その位置を元に UIView の高さを決めたい* ので、UILabel の bottom の制約の priority を下げます。UILabel の推奨サイズが決まる priority (Content Hugging Priority) が 251 なので、bottom の priority を 250 以下に設定すれば、
UILabel の推奨サイズを優先的に設定して、その高さからの bottom の位置が決まるようになります。

{% img /images/20141008/SS10.png 240 %}
{% img /images/20141008/SS11.png 500 %}


#### 高さが決まらない UIView を配置すると、Storyboard 上での作業がしずらい場合

高さが決まらない UIView を使うと、Storyboard 上で制約に合わせてコンポーネントの位置を自動調整するときに困ることがあります。

そんな時は、高さ（height）を適当に設定して、制約のところの *Placeholder Remove at build time* にチェックを入れておきます。
こうすることで、Storyboard 上の作業のときだけ制約が設定されて、実行時には制約が削除される状況を作ることができます。

{% img /images/20141008/SS8.png 240 "Remove at build time" %}


### UIView を使って擬似的な Preferred View を作ると高さ可変のセルが簡単につくれる

このように、セルの ContentView の子要素に Preferred View を用意して、コンテンツのサイズで UIView の高さが決まるようにすることで、
Auto Layout を使った配置の中でセルの高さを可変にすることが結構簡単にできるようになります。

{% img /images/20141008/SS1.png 240 "iPhone5S" %}
{% img /images/20141008/SS12.png 240 "iPhone6 Plus" %}

他にもっと良いやり方があるかと思いますが、参考になればと思います。


## UIScrollView で Auto Layout を上手く使う方法

UIScrollView も UITableViewCell と考え方は同じで、中のコンテンツを Auto Layout するときには Preferred View を用意して、
こいつにサイズを計算させて UIScrollView の contentSize を決めてやるようにします。


### UIScrollView のまとめ

UIScrollView はビュー自体の `frame` とコンテンツ部分の `contentSize` の二つの要素を持ちます。frame はスクロールビュー自身の位置やサイズを表します。
contentSize はスクロールビューが表示するコンテンツのサイズを表していて、スクロールビューのサイズよりも大きい contentSize の場合には、スクロールビューの中でスクロールが発生するということになります。


### UIScrollView と Auto Layout

UIScrollView で気を付けないといけないのは、**contentSize は中に含まれるコンポーネントの frame や bounds で決まるわけではない** ということです。

UIScrollView の contentSize はコンポーネントの `intrinsicContentSize` によって決まります。ここが UIScrollView でハマる一番のポイントかなと思います。

つまり、intrinsicContentSize （推奨サイズ）が決まるコンポーネントのみをコンテンツとして表示する場合には contentSize は Auto Layout が自動で計算してくれるということです。

例えば UIImageView を UIScrollView の中に入れたとします。UIImageView の intrinsicContentSize は UIImageView の frame や bounds ではなく、表示する UIImage の size になります。
なので、UIImageView だけを UIScrollView に入れると、UIImageView のサイズをいくら Storyboard 上で設定しても、勝手にスクロールされてしまうのです。

{% img /images/20141008/SS13.png 800 "UIScrollView の罠" %}

{% img /images/20141008/SS14.png 240 "UIScrollView の罠" %}
{% img /images/20141008/SS15.png 240 "UIScrollView の罠" %}
{% img /images/20141008/SS16.png 240 "UIScrollView の罠" %}


### UIScrollView のサイズに合わせて画像をページングする方法

UIScrollView の contentSize は intrinsicContentSize によって決まるというのは、上で書きました。
じゃあ、contentSize をプログラムで上書きしちゃえば上手くスクロールするんでは？ということで、さがすと出てくるのが大体 `viewDidLayoutSubviews` の中で
contentSize を設定すればいいじゃんっていうプログラム例です。

```swift
    override func viewDidLayoutSubviews() {
        super.viewDidLayoutSubviews()

        let contentWidth = CGFloat(preferredViews.count) * scrollView.bounds.width
        let contentHeight = scrollView.bounds.height
        scrollView.contentSize = CGSizeMake(contentWidth, contentHeight)
    }
```

ぶっちゃけ、僕もこの方法が結局妥当かなという結論に達しました。先に UIScrollView に画像を動的に追加して、ページングできるようにするコードを掲載します。

```swift
import UIKit

class SecondViewController: UIViewController {

    @IBOutlet weak var scrollView: UIScrollView!
    
    var preferredViews: [UIView]!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        let images = [UIImage(named: "Image1"), UIImage(named: "Image2"), UIImage(named: "Image3")]
        self.preferredViews = [UIView]()
        
        for image in images {
            let preferredView = UIView(frame: CGRectMake(0, 0, image.size.width, image.size.height))
            let imageView = UIImageView(frame: CGRectMake(0, 0, image.size.width, image.size.height))
            imageView.image = image
            imageView.contentMode = .ScaleAspectFill
            imageView.clipsToBounds = true
            preferredView.setTranslatesAutoresizingMaskIntoConstraints(false)
            imageView.setTranslatesAutoresizingMaskIntoConstraints(false)
            preferredView.addSubview(imageView)
            scrollView.addSubview(preferredView)
            
            preferredViews.append(preferredView)
        }
    }
    
    
    override func updateViewConstraints() {
        super.updateViewConstraints()
        
        var prevView: UIView? = nil
        for preferredView in preferredViews {
            let imageView = preferredView.subviews[0] as UIImageView
            preferredView.addConstraints(NSLayoutConstraint.constraintsWithVisualFormat("H:|[imageView]|",
                options: nil, metrics: nil, views: ["imageView": imageView]))
            preferredView.addConstraints(NSLayoutConstraint.constraintsWithVisualFormat("V:|[imageView]|",
                options: nil, metrics: nil, views: ["imageView": imageView]))
            
            if prevView == nil {
                scrollView.addConstraints(NSLayoutConstraint.constraintsWithVisualFormat("H:|[preferredView(==scrollView)]",
                    options: nil, metrics: nil, views: ["scrollView": scrollView, "preferredView": preferredView]))
            } else {
                scrollView.addConstraints(NSLayoutConstraint.constraintsWithVisualFormat("H:[prevView][preferredView(==scrollView)]",
                    options: nil, metrics: nil, views: ["scrollView": scrollView, "prevView": prevView!, "preferredView": preferredView]))
            }
            scrollView.addConstraints(NSLayoutConstraint.constraintsWithVisualFormat("V:|[preferredView(==scrollView)]",
                options: nil, metrics: nil, views: ["scrollView": scrollView, "preferredView": preferredView]))
            
            prevView = preferredView
        }
        
    }
    
    
    override func viewDidLayoutSubviews() {
        super.viewDidLayoutSubviews()

        let contentWidth = CGFloat(preferredViews.count) * scrollView.bounds.width
        let contentHeight = scrollView.bounds.height
        scrollView.contentSize = CGSizeMake(contentWidth, contentHeight)
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
}
```

{% img /images/20141008/SS17.png 800 %}

{% img /images/20141008/SS18.png 240 %}
{% img /images/20141008/SS19.png 240 %}


UIScrollView の中に画像を3つ表示して、それをページングスクロールするコードになります。Paging Enabled のオプションは Storyboard 上で設定しています。

画像を直接 UIScrollView にいれてしまうと、intrinsicContentSize の問題で表示が意図したとおりにならないので、UIImageView をラップする PreferredView を作成しています。

PreferredView の中に UIImageView を隙間なく配置し、PreferredView を ScrollView の縦横のサイズと同じサイズにして、Auto Layout で左から順番に配置しています。
ぶっちゃけ、この処理であれば、`UICollectionView` を使うほうが早いし楽だし美しいと思います。

UIScrollView で Auto Layout を使って頑張ってスクロールコンテンツを配置したいときに、参考になるといいと思っています。

