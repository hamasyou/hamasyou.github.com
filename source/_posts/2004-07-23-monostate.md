---
layout: post
title: "実践デザパタ-その13:Monostateパターン"
date: 2004-07-23 04:14
comments: true
categories: [Blog]
keywords: "デザインパターン,Monostate,Singleton,GoF,振る舞い,構造"
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

13番目は唯一性を保証するSingletonパターンとは別の方法である<b>Monostate</b>パターンのメモです。


<!-- more -->

<h2>Monostateパターン</h2>

<p class="option">Monostateパターンは、それぞれのインスタンスが状態をあたかも一つしか持っていないように振る舞うパターンです。</p>

Monostateクラスである条件は、<b>すべてのインスタンスで振る舞いが同じ</b>であればよいのです。実装はいたって簡単で、すべての変数をstaticにするだけです。メソッドはすべてstaticではないことに注意してください。

Monostateとは、たった一つの状態のみを持つというもので、どのインスタンスオブジェクトも同じ振る舞いを行います。Singletonパターンとは、「構造」と「振る舞い」のどちらに視点を置いているかが違うだけです。

Singletonパターンは、「<b>構造</b>」に着目しています。インスタンスをたった一つしか作らせない「構造」になっているからです。逆に、Singletonパターンを実装する場合は、必ずインスタンスが一つになるような構造にしておかなければなりません。

Monostateパターンは、「<b>振る舞い</b>」に着目しています。インスタンスを生成する方法に制約はなく、あくまでオブジェクトの「振る舞い」がどのインスタンスにおいても同じであるということを規制します。

SingletonパターンとMonostateパターンは非常に似通っていながら、少し違いがあります。Monostateクラスに対するテストケースは、Singletonクラスに適用できます。しかし、Singletonクラスのテストケースは、必ずしもすべてMonostateクラスに適用できない場合があります。Singletonクラスは常に同じインスタンスを返すのに対して、Monostateクラスはインスタンスはそれぞれ異なり振る舞いだけが同一のインスタンスになるからです。

<h3>SingletonクラスとMonostateクラスの主な相違点</h3>

<h4>Singleton</h4>

<dl>
<dt>継承できない</dt>
<dd>Singletonクラスから派生されるクラスは、必ずしもSingletonにならない。派生クラスをSingletonとして機能させたければ、適切なインスタンス変数とstaticメソッドを用意する必要がある。</dd>
</dl>

<h4>Monostate</h4>

<dl><dt>継承できる</dt>
<dd>Monostateクラスの派生クラスはMonostateクラスになる。親クラスのすべての変数を共有できます。</dd>
<dt>ポリモーフィズム可能</dt>
<dd>Monostateのメソッドはstaticではないので、派生クラスの中でオーバーライドできます。Singletonクラスでは、そもそもインスタンスが一つしかないので、ポリモーフィズムにならない。</dd>
</dl>

<h2>パターンの適用タイミング</h2>

SingletonパターンとMonostateパターンの適用するタイミングは、以下のような場合です。<strong>Singletonパターンを導入するのは、「構造」の観点からインスタンスを一つに制限したい場合</strong>、</strong>Monostateパターンを導入するのは、「振る舞い」の観点からインスタンスを制限したい場合</strong>

SingletonパターンとMonostateパターンはどちらも、オブジェクトの唯一性を保証する場合に使うことができます。その中でも、Monostateパターンは、<b>ポリモーフィズムを利用したい場合</b>に使うことができます。

<h2>実装サンプルと参考文献</h2>

+ SingletonパターンとMonostateパターン
<a href="http://blog.goo.ne.jp/meiamipapa/e/ae13a17af6305d5b6bb4208d402ccaa3" rel="external nofollow">ジャズと育児とオブジェクト</a>

+ Monostate
<a href="http://www.hyuki.com/dp/dpinfo.html#Monostate" rel="external nofollow">デザインパターン紹介</a>

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





