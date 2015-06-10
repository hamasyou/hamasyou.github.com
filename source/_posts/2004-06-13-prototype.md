---
layout: post
title: "実践デザパタ-その11:Prototypeパターン"
date: 2004-06-13 13:33
comments: true
categories: [Blog]
keywords: "デザインパターン,Prototype,プロトタイプ,GoF,Java"
tags: [デザインパターン]
author: hamasyou
amazon_url: ""
amazon_author: ""
amazon_image: ""
amazon_publisher: ""
---

デザインパターンとは、システム設計におけるクラスやインターフェースの関係に名前をつけたものです。
GoFの23のパターンが有名です。デザインパターンというのは、どんなパターンなのか、パターンの目的は何かということを覚えることが非常に重要なのですが、これを実際に適用しようとした場合に、いつ適用していいかが見えてこないとお話になりません。

そこで、自分の勉強も兼ねつつ、パターンの実践時における使用場所や、パターンを適用するきっかけを見つけられるようにメモしておきます。

11個目はインスタンスを新しく作るときに、既存のインスタンスのコピーを作成する<b>Prototype</b>パターンのメモです。


<!-- more -->

<h2>Prototypeパターン</h2>

<p class="option">Prototypeパターンは、既存のインスタンスのコピーを使って、生成の複雑なインスタンスや、少しだけ違うインスタンスをたくさん作りたいときに使えるパターンです。</p>

どんな時にこのパターンが使えるかというと、例えば株のシミュレーションプログラムを作っているとします。株価がこの先どのように変化するかを調べたいときに、現時点の株インスタンスの状態を複製して、別々の条件に当てはめたい場合、まったく同じ株インスタンスが必要となります。こんな状況にPrototypeパターンが使えます。

他には、ドローツール(お絵かきソフト)を作っているとすると、ユーザの書いた絵インスタンスは、とても複雑な状態になっていると考えられます。このインスタンスは、プログラムで同じものを作るのは、非常に大変です。(描いた手順をすべて覚えておいて、作るとなると時間もコストもかかる)こんなとき、絵インスタンスの複製を作ることができれば、描いた手順なんて必要なくなります。

3つ目は、テンプレートとなるインスタンスがすでに存在する場合です。テンプレートインスタンスを複製して、少し状態を変化させることで、同じような処理を何度も行わなくてすむようになります。

<h2>パターンの適用タイミング</h2>

Webアプリケーションなどを作っていると、<a href="http://yougo.ascii24.com/gh/10/001099.html" rel="external nofollow">ウィザード形式</a>の処理を行う場合があります。入力して、確認して、登録するという一連の処理がある場合、登録時にエラーが発生してしまったら、入力時点まで、インスタンスの状態を戻さなければなりません。
こんなとき、インスタンスの複製を作っておいて、複製の方で登録処理を行い、エラーが出たら、複製元のインスタンスを処理するようにすれば複雑な処理は一切いらなくなります。

Prototypeパターンが使えそうな状況は、<strong>インスタンスを複製(コピー)しておいて、試しになにか処理してみる場合</strong>、<strong>複雑な生成過程を経たインスタンスをもう一度作りたい場合</strong>、<strong>テンプレートインスタンスが存在し、コピーすることで処理が楽になる場合</strong>

Prototypeパターンを使うときの注意点は、コピーの深さを間違えないことです。最近の言語は、オブジェクトの参照を扱います。コピーの深さを間違えると、見た目だけコピーされているが、実は中身は同じ参照だったということがありえます。詳しい解説は、<a href="http://www.amazon.co.jp/exec/obidos/ASIN/4894714361/sorehabooks-22" rel="external nofollow">Effective Java</a>という本が参考になります。

Web上だとSun Microsystemsの<a href="http://sdc.sun.co.jp/java/techtips/2001/index.html" rel="external nofollow">JDC Tech Tips 日本語版</a>がよさそうでした。(※このページは会員制です。無料ですので登録してから、読んでみてください。)

<h2>実装サンプルと参考文献</h2>

Prototypeパターンの実装方法をもっと詳しく知りたい場合は、下記のサイトにアクセスするのをお勧めします。もしくは、参考書籍を載せておきますので、そちらをお買い求めください。(^^;

+ 日立ソフト(Prototypeパターン)
<a href="http://www.dmz.hitachi-sk.co.jp/Java/Tech/pattern/gof/prototype.html" rel="external nofollow">日立ソフト</a>

+ Skeleton of GOF's Design Pattern(JavaとC++のサンプルがあります)
<a href="http://www002.upp.so-net.ne.jp/ys_oota/mdp/Prototype/index.htm" rel="external nofollow">Prototypeの骸骨</a>

+ TECHSCORE(生成に関するパターン)
<a href="http://www.techscore.com/tech/DesignPattern/Prototype.html" rel="external nofollow">TECHSCORE Prototypeパターン</a>

+ デザインパターンのお勧め書籍
<div class="rakuten"><table border="0" cellpadding="5" width="400"><tr><td valign="top"><a href="http://www.amazon.co.jp/exec/obidos/ASIN/4797327030/sorehabooks-22/" rel="external nofollow"></a><br /></td></tr></table>
</div>

+ 独習シリーズのデザインパターン編。デザインパターンを一人でも学べます。
<div class="rakuten"><table border="0" cellpadding="5" width="400"><tr><td valign="top"><a href="http://www.amazon.co.jp/exec/obidos/ASIN/4798104450/sorehabooks-22/" rel="external nofollow"></a><br /></td></tr></table>
</div>

+ Sun Microsystemのお墨付き。GoF以外のパターンも学べます。
<div class="rakuten"><table border="0" cellpadding="5" width="400"><tr><td valign="top"><a href="http://www.amazon.co.jp/exec/obidos/ASIN/4756141552/sorehabooks-22/" rel="external nofollow"></a><br /></td></tr></table>
</div>

+ UMLを使って、オブジェクト指向のいいとこ取りができます。
<div class="rakuten"><table border="0" cellpadding="5" width="400"><tr><td valign="top"><a href="http://www.amazon.co.jp/exec/obidos/ASIN/4774116882/sorehabooks-22/" rel="external nofollow"></a><br /></td></tr></table>
</div>

+ デザインパターンだけではなく、ソフトウェア設計の原則やプラクティスまで学びたい人におすすめ
<div class="rakuten"><table border="0" cellpadding="5" width="400"><tr><td valign="top"><a href="http://www.amazon.co.jp/exec/obidos/ASIN/4797323361/sorehabooks-22/" rel="external nofollow"></a><br /></td></tr></table>
</div>




