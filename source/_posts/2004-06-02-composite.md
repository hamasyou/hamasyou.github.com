---
layout: post
title: "実践デザパタ-その5:Compositeパターン"
date: 2004-06-02 13:37
comments: true
categories: [Blog]
keywords: "デザインパターン,Composite,コンポジット,GoF,Java"
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

5回目は、再帰的な概念を表すことに使える<b>Composite</b>パターンです。


<!-- more -->

<h2>Compositeパターン</h2>

<p class="option">Compositeパターンは、複数のオブジェクトの集合を管理したい場合に使われるパターンです。</p>

難しい言い方をすると、「全体-部分」のモデルを同一視するパターンといえます。

<img src="http://hamasyou.com/images/design_pattern/composite.gif" alt="Compositeのクラス図" />

よくある例として、Windowsのフォルダとファイルがあります。フォルダが「全体」を表し、ファイルが「部分」を表します。フォルダの中にはまたフォルダがあり、その中にはまたフォルダがある...というような再起構造も持っています。

もう少し抽象度の高い例として、木があります。木には枝(全体)があり、枝には葉っぱがついているものもあれば、枝がまた伸びているものもあります。これもCompositeパターンの例です。

3つめの例として、階層構造があります。会社勤めの人はイメージがつくと思いますが、会社には「会社-部署-課-班」のような階層があると思います。これもCompositeパターンなのです。

つまり、Compositeパターンとは、<strong>再帰的、階層的な概念を簡単に扱う</strong>パターンといえます。

実装の段階でCompositeパターンの候補になるのは、リスト中のすべてのオブジェクトが<b>まったく等価</b>に取り扱われるものだけに限られます。リスト中からある条件のオブジェクトだけに処理を行うといった場合には、Compositeパターンは使うべきではありません。

<dl>
<dt class="info">ちょっと一言</dt>
<dd>Compositeパターンを適用すると、構造に柔軟性がでます。詳しい実装方法や、クラス図は参考文献を参照してもらいたいのですが、Compositeとなる「全体」とLeafとなる「部分」の間に、動的に階層を付け加えることができます。</dd>
</dl>

<h2>パターンの適用タイミング</h2>

Compositeパターンは、<b>再帰的構造をもつ場合</b>に考えることができます。Windowsのフォルダとファイルの例だと、フォルダに対して、サイズを計算させる場合、フォルダの中にあるファイルのサイズと、サブフォルダのサイズを求めます。そして、サブフォルダの中のファイルのサイズと...このように、再帰的に処理を行いたい場合に使えます。

<b>階層構造のあるシステムの場合</b>にも考えることができるでしょう。この場合も、再起構造と同じように考えられます。

<strong>再起的処理を施したい場合</strong>、<strong>階層構造(ツリー構造)で物事を管理したい場合</strong>、<strong>複数を表すオブジェクトと単数を表すオブジェクトを同じように扱いたい場合</strong>。これらの場合、Compositeパターンの適用を考えることができるでしょう。

<h2>実装サンプルと参考文献</h2>

Compositeパターンの実装方法をもっと詳しく知りたい場合は、下記のサイトにアクセスするのをお勧めします。もしくは、参考書籍を載せておきますので、そちらをお買い求めください。

ひとつだけ、このパターンを使う上で、実装に注意する点があります。「全体」と「部分」とを同じように扱うことを目的としてこのパターンを適用したときに、例外的に「部分」だけに持たせたいメソッドがあったらどうしましょう?

いくつか解答はあるのですが、Compositeパターンは共通インターフェースを「全体」と「部分」で使います。このとき、意図していない使われ方をした方には例外をスローするというのが
私が一番好きな方法です。参考文献の「オブジェクト倶楽部」のサイトを見ながら考えるとわかりやすいかも知れません (^^;

+ 日立ソフト(Compositeパターン)
<a href="http://www.dmz.hitachi-sk.co.jp/Java/Tech/pattern/gof/composite.html" rel="external nofollow">日立ソフト</a>

+ オブジェクト倶楽部(上から1/4位のところにCompositeパターンの解説有)
<a href="http://www.objectclub.jp/technicaldoc/pattern/DPforJavaProgrammers" rel="external nofollow">オブジェクト倶楽部</a>

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




