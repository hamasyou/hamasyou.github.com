---
layout: post
title: "サードパーティJavaScript"
date: 2014-02-23 17:47:39 +0900
comments: true
categories: [Programming]
keywords: "サードパーティJavaScript,JavaScript,9784048916585"
tags: [JavaScript,読むべき]
sharing: true
published: false
amazon_url: "http://www.amazon.co.jp/gp/product/4048916580?ie=UTF8&camp=247&creativeASIN=4048916580&linkCode=xm2&tag=sorehabooks-22"
amazon_author: "Ben Vinegar (著), Anton Kovalyov (著), 水野貴明 (翻訳)"
amazon_image: "http://ecx.images-amazon.com/images/I/51ptmwaAD1L._AA300_.jpg"
amazon_publisher: "KADOKAWA/アスキー・メディアワークス"
og_image: ""
---

サードパーティ JavaScript とは *異なる Web アドレスから配信される、独立したクライアントコード* のことを意味します。
例えばソーシャルウィジェットやアナリティクス用のトラッカーがそうです。
本書はこのサードパーティ JavaScript をどのように開発するとよいかについて書かれています。

サードパーティ JavaScript には様々な難しいポイントがあります。

- 動的なスクリプト読み込み
- サードパーティ Cookie の保存と読み込み
- HTTP / HTTPS を使ったサーバとの通信

多くの人に使われる JavaScript を開発するには、**数多くの落とし穴や難関を乗り越えなければなりません**。
本書はこういった落とし穴や難関に対する解決策を手順を追って説明してくれます。

本書の著者の一人は [Disqus](http://disqus.com/) という Web サイトに即席のコメントシステムを導入することができる JavaScript アプリケーションを開発しています。
実際に世界中で使われているアプリケーションの経験を元に書かれているので説得力がありますし、「こんな事にも注意しないといけなかったのか！」といった目からウロコな内容も盛りだくさんでおすすめです。

本書を読み終わると「<span class="lead">**IE 無くなったらいいのに！**</span>」って絶対に思います。そして「<span class="lead">**iframe すげーな。使えるやつだ**</span>」って思います（笑

<!-- more -->

以下、おぼえがきとメモです。

<nav id="toc" role="navigation"></nav>

## サードパーティ JavaScript 入門

サードパーティ JavaScript の例には次のようなものがあります。

- 埋め込みウィジェット
- 分析と計測
- Web サービス API のラッパー

### サードパーティ JavaScript 開発が難しい理由

#### 未知のコンテキスト

JavaScript が読み込まれたときに、読み込まれた先（パブリッシャ）のページの環境がどのようになっているかがわかりません。
JavaScript の読み込まれる位置が `<head>` タグだったり、`<body>` タグの一番最後かもしれません。`<head>` タグがない可能性もあります。
**ブラウザに依存するような状況に依存したコードは書くべきではありません**。

ウィジェットを開発しているのであれば、正しく表示されるかも問題になります。
表示されるページの CSS に干渉してしまうようなスタイルの定義も問題になります。

共通して言えることは、サードパーティ JavaScript は外部の環境に依存するべきではないし、**外部の環境を壊すようなことはあってはならない** ということです。

#### 共有されている JavaScript 環境

Web 環境では、JavaScript が実行される環境はグローバルな環境が一つしかありません。グローバル環境を汚さないようにしなければいけませんし、他の JavaScript に壊されることがないように防衛する必要もあります。


#### ブラウザによるクロスドメイン制限

ブラウザの同一生成元ポリシーによって、`XmlHttpRequest` は表示しているページと異なるドメインへアクセスするのを妨げられてしまいます。また、サードパーティ Cookie の保存や読み込みも制限していることが普通です。


## 開発の足場

### 複数のドメインをシミュレートする開発環境

クロスドメインの問題はかなり厄介な問題ですので、開発時点から複数ドメインをシミュレートするのは役にたちます。
OS の `hosts` ファイルを編集して、localhost の エイリアスを二つ用意するだけでこの環境が手に入ります。

```plain-raw hosts
127.0.0.1 publisher.dev
127.0.0.1 widget.dev
```

*「.dev」のような存在しないトップレベルドメインを使う方が、実際に存在する Web サイトのアドレスを差してしまう問題がないので便利* です。


### スクリプトを読み込むスニペット

パブリッシャのページに最初のスクリプトを読み込むスニペットを用意します。スニペットを提供する方法は2つあります。

- 「ブロッキング」を行うタイプの `script` タグによる読み込み
- 非同期でスクリプトを読み込むタイプ


#### ブロッキング読み込み

```html
<script src="http://camerastock.com/widget.js?product=1234"></script>
```

#### ノンブロッキング読み込み

上のようなタグは、そのスクリプトが読み込まれるまでブラウザの処理をとめてしまいます。そのため、このようなスクリプトは `<body>` タグの最後におくことや、`defer` 属性、`async` 属性を使うことで回避できます。
`defer` 属性、`async` 属性をスクリプトタグに指定することで、そのスクリプトが文書のコンテンツを生成しない（`document.write` を使わない）のでページブロッキングをしなくても完全にダウンロードが可能であるとブラウザに伝えることができます。

```html
<script defer src="http://camerastock.com/widget.js?product=1234"></script>
<script async src="http://camerastock.com/widget.js?product=1234"></script>
```

defer 属性
: `defer` 属性が指定されたスクリプトは、**ページが完全に解析された後** に実行が開始されます。
async 属性
: `async` 属性が指定されたスクリプトは、**ダウンロードが完了したすぐ直後** から実行が開始されます。

JavaScript を使って DOM 上に `script` 要素を動的に生成することでも、`async` 属性と同じ挙動でスクリプトを読み込ませることができます。

```html 非同期のスクリプト読み込み
<script>
(function() {
  var node     = document.getElementsByTagName('script')[0],
      script   = document.createElement('script');

  script.src   = 'http://camerastock.com/widget.js?product=1234';
  script.async = true;
  node.parentNode.insertBefore(script, node);
})();
</script>
```

### 最初のスクリプトファイル

最初の JavaScript ファイルの中身はこんな感じになります。

```javascript
var Stork = (function(window, undefined) {
  var Stork = {};

  return Stork;
})(window);
```

一番外側の `Stork` はアプリケーションをカプセル化するための名前空間オブジェクトです。`window` と `undefined` を引数に取っています。これは、*よく使う変数をローカル変数として定義することで、JavaScript のミニファイアが働くようになり変数名を短くすることができる* からです。
また、`undefined` はオリジナルの `undefined` が他の環境によって書き換えられていても問題ないようにするためのテクニックになります。この二つは、*JavaScript ライブラリの作者が非常によく使うテクニック* になります。


### loadScript 関数

外部の JavaScript ライブラリを使いたい場合は、パブリッシャにそのスクリプトを読み込んでもらうようにするのはナンセンスです。
パブリッシャに依存ライブラリを追加してもらうのでは、依存ライブラリに変更が入った際にパブリッシャに変更を依頼しなければならなくなります。

*サードパーティ JavaScript の開発者は依存ライブラリは外部に依存しない形で利用するようにするのが求められます*。次のコードは非同期の JavaScript ローダ関数です。必要なスクリプトの読み込みが完了したら、引数の `callback` 関数を呼び出します。

```javascript
function loadScript(url, callback) {
  var node   = document.getElementsByTagName('script')[0],
      script = document.createElement('script');

  script.src   = url;
  script.async = true;
  node.parentNode.insertBefore(script, node);

  script.onload = script.onreadystatechange = function() {
    var readyState = script.readyState;

    if (!readyState || /complete|loaded/.test(script.readyState)) {
      callback();
      script.onload = script.onreadystatechange = null;
    }
  };
}
```

このように、必要なライブラリは、アプリケーションが実行する前に読み込んでおくようにします。

#### ライブラリの衝突に注意

jQuery のようなよく使われるライブラリをこの方法で読み込む際には、すでに存在するオブジェクトと衝突しないように注意しなければなりません。
衝突を防ぐには **名前空間** を使います。jQuery の場合には衝突を避ける `noConflict` が用意されているので、これを使います。

```javascript
var Stork = Stork || {};
Stork.$ = Stork.jQuery = jQuery.noConflict(true);
```

外部ライブラリを使う場合には、既に読み込まれていても衝突しないように注意する必要があります。


## HTML と CSS のレンダリング


### スタイルを調整する方法

HTML にスタイルを追加で当てる方法には3つの基本的な方法が考えられます。

1. HTML にインラインでスタイルを当てる
1. 関連する CSS ファイルを別途動的に読み込む
1. JavaScript にスタイルシートのルールを埋め込む

#### 1. インラインスタイル

インラインスタイルは他の要素のスタイルと干渉する心配がありません。デメリットは一般の Web サイトでインラインスタイルを使うべきでない理由と同じになります。

#### 2. CSS ファイルをロードする

CSS ファイルを動的にロードする方法は、JavaScript を動的にロードする方法とほとんど同じになります。

```javascript
function loadStylesheet(url) {
  var node = document.getElementsByTagName('script')[0],
      link = document.createElement('link');
  link.rel  = 'stylesheet';
  link.type = 'text/css';
  link.href = url;

  node.parentNode.insertBefore(link, node);
}
```

`link` タグは `rel` 属性と `type` 属性はどちらも必須の属性になります。
CSS が読み込まれたタイミングを知りたい場合には、少し変わった方法が必要になります。ブラウザによっては、`link` タグは `script` タグのように読み込みが終わったタイミングで `load` イベントを発生させない場合があるからです。
CSS が読み込まれたかどうかをチェックするには、要素にスタイルが当たったかどうかを定期的にチェックする必要があります。

{% blockquote %}
JavaScript やその他のデータを読み込むためのライブラリである Yepnope.js では、document.styleSheets を使って新しく挿入されたスタイルシートをスキャンする方法をとっている。
{% endblockquote %}

##### JavaScript で要素の色を調べるときは注意！

**JavaScript で要素の色を調べるときは注意が必要です**。*廃止予定のマイクロソフトのアクセサはオリジナルの16進数の値を返します*。*W3C のアクセサは16進数の値を RGB 表現に変換して返します*。

#### 3. CSS を JavaScript 中に埋め込む

JavaScript の文字列に CSS を含めてしまう方法です。


### 防衛的な HTML と CSS

サードパーティ CSS においては、名前空間 (prefix) をつけるようにするとよいです。

```css
.stork-container { width: 200px; height: 200px; }
```

#### CSS 優先順位

CSS が優先的に当たるルールは次のとおりです。

1. インラインのスタイル(style="...")
1. ID
1. クラス、属性および擬似クラス(:forcus, :hover)
1. 要素(div, span など)、擬似要素(:before, :after)

ただし、`!important` というキーワードでタグ付けされたプロパティは優先順位が最も高くなります。

```css !important の例
.stork-price {
  font-size:       11px  !important;
  color:           #888  !important;
  text-decoration: none  !important;
  display:         block !important;
}
```


ブラウザはスコアシステムを使ってどのルールが優先されるかを計算しています。

| セレクタ / ルールタイプ  | スコア (a, b, c, d) |
|-------------------------|-----------|
| インラインの style 属性  | 1, 0, 0, 0 |
| ID                       | 0, 1, 0, 0 |
| クラス、擬似クラス、属性 | 0, 0, 1, 0 |
| 要素、擬似要素           | 0, 0, 0, 1 |

スコアは `a > b`、`b > c`、`c > d` のように価値が高くなります。(1, 0, 0, 0) は (0, 100, 0, 0) よりも優先されるということです。

- `.stork-container` (0,0,1,0 - クラスセレクタ1つ)
- `.stork-container span` (0,0,1,1 - クラスセレクタ1つ、要素セレクタ1つ)
- `.stork-container .strok-msg` (0, 0, 2, 0 - クラスセレクタ2つ)


#### CSS を過剰に指定すれば優先度は高くなる

パブリッシャのスタイルよりも優先的にスタイルを当てるための簡単な方法は、過剰にルールを指定することです。

```html
<div id="stork-main">
  <div id="stork-container">
    <h3 class="stork-product">Mikon E90 Digital SLR</h3>
    <img src="http://camerastork.com/img/products/1337-small.png" />
    <p class="stork-price">$599</p>
    <p class="stork-rating">4.3/5.0 &bull; 176 Reviews</p>
  </div>
</div>
```

```css
#stork-main #stork-container { ... }
#stork-main #stork-container .stork-product { ... }
#stork-main #stork-container .stork-price { ... }
```


### コンテンツを iframe に埋め込む

*パブリッシャのスタイルルールによる影響を受けずにスタイルを設定する方法は、`iframe` を使うこと* です。

#### src なし iframe

`src` 属性を指定しない `iframe` を作る場合、独立したウィンドウと DOM 環境を有しているという点で強力な効果があります。そして、**親ページで実行されているスクリプトは直接それらのオブジェクトにアクセスが可能** です。
`iframe` はブラウザによって非同期的に処理されるため、`document.write` のようなブロッキングが発生する処理を `iframe` の中で行ってもブラウザが親ページを処理するのを妨げることはありません。
ただし1つ注意点は、*親ページの `onload` イベントはブロッキングしてしまう* 点です。これは、`iframe` のレンダリングが完了したあとで、`document.close()` を呼び出すことで `onload` を強制的に発生させることで回避できます。

#### 外部 iframe

`iframe` のコンテンツが外部から提供されている場合には *ブラウザは親ページでホストされているスクリプトがそのコンテンツにアクセスすることを許可しません*。


## サーバとの通信

サードパーティアプリケーションでは、**クロスドメイン** の問題を扱わなければなりません。クロスドメイン対応には基本的なテクニックがあります。

- JSONP
- サブドメインプロキシ
- クロスオリジンリソース共有 (CORS)

### AJAX とブラウザの同一生成元ポリシー (SOP)

ブラウザは異なる生成元から配信されたドキュメントがそれぞれお互いに分離されることを保証するという、**同一生成元ポリシー (SOP) ** を備えています。
これによって、ドキュメント上のスクリプトが他のドキュメントにアクセスできるのは *同じドメイン*、*同じポート*、*同じプロトコル* である場合に限ることになります。

すべてのブラウザが、`XMLHttpRequest`、`iframe`、それ以外のドキュメント間のメッセージのやり取りの方法についてこの同一生成元ポリシーを適用しています。

重要な点が1つあります。**HTML の script 要素は SOP の適用を受けません**。つまり、外部の JavaScript ファイルの読み込みは可能になっています。
ただし、*読み込まれた JavaScript はパブリッシャのコンテキストで実行されるということは注意が必要* です。
例えば、camerastork.com の widget.js を publisher.dev の index.html で読み込まれたとしても、widget.js は camerastork.com への `XmlHttpRequest` を開始できません。


### JSONP

SOP は「HTML の script 要素はチェックの対象外」という重要な例外をもっています。この例外を使って、例えば次のような JSON データを返す URL を script 要素を使って読み込んでみます。

```json http://thirdpartyjs.com/info.json
{
  "title": "Third-party JavaScript",
  "authors": ["Anton", "Ben"],
  "publisher": "Manning"
}
```

```html
<script src="http://thirdpartyjs.com/info.json"></script>
```

この読み込みはうまくいきます。ただし、問題は、info.json は JavaScript として実行されるということです。
info.json は JavaScript としては正しい構文ではありませんし、もし正しく JavaScript オブジェクトとして評価されたとしても、どこからも使われずに終わってしまいます。

そこで、info.json を次のように変更します。

```javascript http://thirdpartyjs.com/info.js
var jsonResponse = {
  "title": "Third-party JavaScript",
  "authors": ["Anton", "Ben"],
  "publisher": "Manning"
};
```

こうすると、JavaScript として正しい評価がされ、スクリプトの実行が終わると、グローバルの `jsonResponse` 変数にオブジェクトが設定され使えるようになります。
変数ではなく、関数を呼び出すこともできます。そして、**JSONP** というのは、この *script を読み込む際に URL のクエリパラメータを使って呼び出し元がコールバック関数を指定できる仕組み* のことになります。

```javascript

function jsonpCallback(json) {
  console.log(json);
}

var script = document.createElement('script');
script.async = true;
script.src   = 'http://thirdpartyjs.com/info.js?callback=jsonpCallback';
document.body.appendChild(script);
```

#### JSONP の制限

JSONP は **GET リクエストでのみ利用可能** です。RESTful API を提供しているような場合、JSONP に合わせて GET のエンドポイントを用意していあげる必要が出てくるかもしれません。

*JSONP リクエストは必ず非同期で処理されます*。ほとんどないとは思いますが、もしブロッキングが必要になった場合は、JSONP では不可能です。


### サブドメインプロキシ

SOP は生成元のホスト部分を厳密に区別しています。`sub.example.com` と `example.com` は上位レベルのドメインは同じ (example.com) ですが、ブラウザは無関係なドメインとみなします。

デフォルトの動作はこうなりますが、ブラウザは **Web サイトが生成元のホスト名をその上位ドメインに設定することを許可しています**。
そのため、sub.example.com と alt.example.com のような共通の上位ドメインをもつサイト同士であれば両方の生成元を example.com に設定することでお互いに通信することができるようになります。

もし、企業向けの限られた環境だけで使うような JavaScript を作るのであれば、サブドメインの CNAME を割り当ててもらい、同一生成元を上位ドメインに設定することで通信可能にできるかもしれません。

#### document.domain を利用してドキュメントの生成元を変更する

ドキュメントの生成元を変えるには次のようなスクリプトを実行します。

```javascript
document.domain = 'example.com';
```

生成元変更にはいくつか注意点があります。

- 生成元を変更するスクリプトは `example.com` と `sub.example.com` の **両方** で行う必要がある
- `document.domain` を変更できるのは **ページあたり1回だけ**
- `document.domain` を変更することで、**生成元のポートは80にリセット** される

#### AJAX から呼び出すためのサブドメイントンネルファイル

`document.domain` を変更することでドキュメントの生成元を変更することができるようになりましたが、まだこれだけでは AJAX を使ってサブドメインのドキュメントを読み込むことはできません。
*AJAX においては、まずリクエストを送ってみない限り取得するドキュメントが同じドメインに参加しようとしているかを知ることができない* からです。
CORS の仕様では特別なヘッダを使ってプリフライトリクエストを行うことでこの問題を解決していますが、ここではトンネルファイルを使った方法を見ていきます。

##### 非表示の iframe を使う

AJAX リクエストを呼び出すページが呼び出し先のドメインと同じであればリクエストを送ることができますので、予め呼び出し先のドキュメントを同一生成元を変更して読み込めていれば、そのドキュメントを使って AJAX リクエストを送ることができます。

それが、*非表示の iframe を使う方法* です。非表示の iframe で生成元を変更するスクリプトが書かれたページを読み込み、そのページから AJAX リクエストを送るようにするのです。
そうすれば、サブドメインプロキシを通じて、トンネルファイルの iframe 上の非表示ドキュメントから AJAX リクエストを呼び出すことができ、iframe 上のドキュメントと親ページは同じ同一の生成元にいるのでデータの受け渡しも可能になるという仕組みができます。

![サブドメイントンネルファイル](/images/2014-02-23-third-party-javascript-01.png)


##### JSONP と動的フォームを使う

AJAX リクエストを送るためには非表示の iframe をもちいた中間ファイルが必要になります。中間ファイルを用いない方法として、動的にフォームを作成して、フォームをリクエストするという方法があります。

`form` タグには `target` 属性が用意されているので、動的にフォームを作る際に結果を受け取るための `iframe` を作成して、その iframe をターゲットにフォームをポストするといいです。


#### Google のブラウザセキュリティハンドブック

同一生成元ポリシーやそれに伴うセキュリティ上のリスクをもっとよく知りたい場合は、Google の「ブラウザセキュリティハンドブック」のパート2を読むとよいです。

[ブラウザセキュリティハンドブック Part2](https://code.google.com/p/browsersec/wiki/Part2)


### クロスオリジンリソース共有 (CORS)

**クロスオリジンリソース共有 (CORS)** は W3C のワーキングドラフトになっている、きちんと管理された形でドメインをまたがってサーバと通信を行う方法を定めたものです。

CORS では一連の特別な HTTP ヘッダを使ってブラウザとサーバがやり取りを行えるかどうかを決定するようになっています。

#### Origin リクエスト

クロスオリジンの HTTP リクエストを開始する際には、CORS に対応したブラウザは `Origin` と呼ばれる特殊なヘッダを含むリクエストを使って生成元を示すことになっています。

```plain-raw
Origin: http://www.example.com/
```

サーバ側はこのヘッダをチェックして、そのリクエストが許可されるかどうかを決定します。応答するには、サーバは `Access-Control-Allow-Origin` というレスポンスヘッダにクライアントが送ったのと同じ生成元を入れて返さなければなりません。

```plain-raw
Access-Control-Allow-Origin: http://www.example.com/
```

あらゆるところからリクエストを許可するにはワイルドカードを使うこともできます。

```plain-raw
Access-Control-Allow-Origin: *
```

リクエストを許可しない場合は、サーバ側は CORS ヘッダを返さないようにします。また、リクエストに `Origin` ヘッダが存在していない場合は、サーバも CORS ヘッダを送り返すべきではないとされています。

#### CORS で Cookie や認証ヘッダを送信する

デフォルトでは、ブラウザは CORS を行う際には Cookie や HTTP 認証ヘッダを送らないようになっています。
こうした識別情報を送信するべきであると明示する場合には、`XmlHttpRequest` オブジェクトに `withCredentials` プロパティを設定します。

```javascript
var xhr = new XmlHttpRequest();
xhr.withCredentials = true;
```

サーバ側は、識別情報を必要としている場合には `Access-Control-Allow-Origin` ヘッダに加えて、`Access-Control-Allow-Credentials` というヘッダもレスポンスに含める必要があります。
このヘッダがないと、ブラウザはレスポンスを拒否していしまいます。

```plain-raw
Access-Control-Allow-Credentials: true
```


## クロスドメインでの iframe 間通信

パブリッシャのページ上で実行されるアプリケーションコードと iframe 内のサードパーティのドメインで配信されているページとの間でドキュメントをやり取りする方法です。

### HTML5 window.postMessage API

`window.postMessage` はイベントを利用した安全なクロスドメイン通信 API です。
メッセージを送りたいウィンドウオブジェクトを取得して、`postMessage` を送るだけです。
送られる側はウィンドウの `message` イベントで受け取ります。

```html http://publisher.dev/index.html
<!DOCTYPE html>
<html>
<body>
<h1>Publisher</h1>

<iframe id="thirdparty" src="http://thirdparty.dev/index.html">
</iframe>

<script>
var frame = document.getElementById('thirdparty');
frame.addEventListener('load', function(e) {
    var win = frame.contentWindow;
    win.postMessage('Hello World!', 'http://thirdparty.dev/');
});
</script>
</body>
</html>
```

```html http://thirdparty.dev/index.html
<!DOCTYPE html>
<html>
<body>
<h1>thirdparty</h1>
<script>
function receiver(e) {
    console.log(e.data);
    console.log(e.origin);
    console.log(e.source);
}
window.addEventListener('message', receiver, false);
</script>
</body>
</html>
```

### easyXDM を利用したクロスドメインメッセージング

[easyXDM](http://easyxdm.net/wp/)

クロスドメイン間でメッセージをやり取りするためのライブラリに **easyXDM** があります。
このライブラリは複数の異なるクロスドメインメッセージングのテクニックを組み合わせて、そのなかかkらそれぞれのブラウザに適したものを選択してくれます。


## メモ

### ポップアップブロック機能を回避する方法

{% blockquote %}
これを避けるには、新しいウィンドウを常に<strong>ユーザーのアクションの直接の結果</strong>として開くようにすることだ。ユーザーのアクションからウィンドウを開くまでの間に遅延が存在すると、多くのブラウザはそれを悪意ある動作とみなして処理を阻止してしまう。
{% endblockquote %}


## 感想

読んでみて、**本書はサードパーティ JavaScript を書かなくても、サーバ側の API を実装するだけの人も読むべき** だと思いました。
クロスオリジン問題やセキュリティに関する話題も豊富に書かれていて、全てが有用な情報です。

ブラウザとサーバがどんなことをやっているのかをきちんと理解したい人は、是非よんでみてください。絶対に為になります。
