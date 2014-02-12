---
layout: post
title: "[Titanium] Titanium Mobile で iPhone 開発 - その1"
date: 2010-08-31 01:10
comments: true
categories: [Blog]
keywords: "iPhone, Titanium, Objective-C"
tags: [iPhone,Titanium]
author: hamasyou
amazon_url: ""
amazon_author: ""
amazon_image: ""
amazon_publisher: ""
---

iPhone アプリの開発をしていて一番の難は <em>Objective-C を使わないといけない</em>ことだと思う。Objective-C が大好きな人もいるかもしれないけど、僕はあの言語の気持ち悪さ（構文的な意味で）が嫌いです。。

で、どうにかならんもんかと調べてみるとなんと、<strong>JavaScript</strong> を使って開発することが出来る開発プラットフォームがあるじゃないですか。

というこで早速使ってみましたよ、<strong>Titanium Mobile</strong>！読み方は、<em>タイタニウム</em> らしいですが、チタニウムのが言いやすいよね。。

<h4>参考</h4>

<ul><li><a href="http://d.hatena.ne.jp/donayama/searchdiary?word=*[Titanium]" rel="external nofollow">JP::HSJ::Junknews::HatenaSide</a></li></ul>



<!-- more -->

<h2>まずはインストール</h2>

<h3>Titanium Mobile をダウンロードしてインストールする</h3>

iPhone アプリを JavaScript で開発できるというこの Titanium は、正確には Titanium Desktop　と<em>Titanium Mobile</em> の二つがあるみたいです。

Titanium Desktop の方には興味ないので、以下 Titanium と言ったら Titanium Mobile のことです。早速 <a href="http://www.appcelerator.com/products/titanium-mobile-application-development/" rel="external nofollow">Titanium Mobile - appcelerator</a> でダウンロードします。

<img alt="appcelerator_titanium.jpg" src="/images/appcelerator_titanium.jpg" width="693" class="mt-image-none" style="" />

<h3>iPhone アプリ開発には iPhone SDK が必要</h3>

iPhone アプリ開発に iPhone SDK が必要なように、Titanium で iPhone アプリ開発する際にも当然 iPhone SDK が必要になります。ということは・・・、そう、Titanium 開発には Mac が必要なのだ！

<p class="option">Titanium はクロスプラットフォーム開発環境と呼ばれていて、同じソースコードから iPhone アプリと Android アプリの両方を作ってくれます。Android アプリを開発するだけなら、Windows でもOKです。何たって Android の開発言語は Java ですから。</p>

とはいえ、iPhone アプリ開発しようって言うんだから、Mac くらいもってますね。で、もう一つ、実機でアプリを動かそうと思ったら、そう、Apple Developer 登録（個人会員は年間参加費 ¥10,800）が必要になります。シミュレータだけで動かすなら登録は不要ですが。

実機で動かす手順とか、Apple Developer Program のアクティベーションの注意点（氏名とか住所とか英語じゃないとエラーになって先に進めなくなるとか。。）は、Google 先生にきいてください。

<section>

<h4>Apple Developere Program のアクティベーション失敗あれこれ参考</h4>

<a href="http://itfun.jp/2009/09/iphone-developer-program.html" rel="external nofollow">iPhone Developer Program のアクティベーションに失敗と対応 - itFun.jp</a>

</section>

<h3>Titanium のドキュメントは Kitchen Sink</h3>

Titanium をインストールした後なにするかというと、<strong>Kitchen Sink</strong> というデモアプリをプロジェクトにインポートして動かしてみます。これで、Titanium の動作確認 + 困ったときのドキュメント（サンプル）の役割を果たします。

Titanium はまだまだ世に出て浅いためか、ドキュメントがほとんどありません。日本語ドキュメントなんてもってのほかです。でも、この
 Kitchen Sink サンプルが Titanium で出来るほぼすべての機能を網羅したサンプルになっているのです。しかもソースコード付き。API Doc を見るよりもサンプルコードを見た方がいいです。

<section>

<h4>Kitchen Sink のインストール方法</h4>

<a href="http://developer.appcelerator.com/doc/kitchensink" rel="external nofollow">Kitchen Sink Download Doc - Appcelerator</a>

</section>

Kitchen Sink は Git を使ってダウンロードします。

<section>

<h4>Git - 分散バージョン管理システム</h4>

<a href="http://git-scm.com/" rel="external nofollow">Git - the fast version control system</a>

</section>

手順通りに Kitchen Sink をプロジェクトにインポートしてもダメなときは、新規でプロジェクトを作って、ソースコードだけをコピーすると良いです。Titanium 環境でのソースコードは次のところにあります。

<pre>$PROJECT_ROOT$/Resources</pre>

Resources 以下のファイルを全部丸ごとコピーしちゃえばOKです。Titanium では Resources フォルダ以下にソースコードを起きます。

<section>

<h4>プロジェクトのフォルダ構成</h4>

<dl>
<dt>app.js</dt>
<dd>アプリケーションのエントリポイント。このファイルが最初に呼び出される。</dd>
<dt>/iphone フォルダ</dt>
<dd>iPhone 用のリソースファイルを置く。</dd>
<dt>/android</dt>
<dd>Android 用のリソースファイルを置く。</dd>
</dl>

</section>

Kitchen Sink が動けば、とりあえず Titanium の動作は完了です。残りは、別の機会に！

<h2>ハマッたポイント</h2>

<h3>プロジェクト名に Titanium という文字を含めると謎のビルドエラー</h3>

自分で Titanium 環境を作ったところ、<em>プロジェクト名に Titanium が含まれる</em> とビルド時にエラーになるようです。気をつけてください。




