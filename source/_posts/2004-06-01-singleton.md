---
layout: post
title: "実践デザパタ-その3:Singletonパターン"
date: 2004-06-01 01:59
comments: true
categories: [Blog]
keywords: "デザインパターン,Singleton,シングルトン,GoF,Java"
tags: [デザインパターン]
author: hamasyou
amazon_url: ""
amazon_author: ""
amazon_image: ""
amazon_publisher: ""
---

デザインパターンとは、システム設計におけるクラスやインターフェースの関係に名前をつけたものです。
GoFの23のパターンが有名です。
デザインパターンというのは、どんなパターンなのか、パターンの目的は何かということを覚えることが非常に重要なのですが、これを実際に適用しようとした場合に、いつ適用していいかが見えてこないとお話になりません。

そこで、自分の勉強も兼ねつつ、パターンの実践時における使用場所や、パターンを適用するきっかけを見つけられるようにメモしておきます。

今回は、インスタンスを一つしか作らせたくない、<b>Singleton</b>パターンです。


<!-- more -->

<h2>Singletonパターン</h2>

<p class="option">Singletonパターンは、クラスのインスタンスを一つしか作りたくない、作らせたくない場合に使われるパターンです。</p>

例えば、アプリケーションの設定項目を保持するクラスやメッセージをつかさどるクラスのようなシステムに一つのインスタンスしか必要の無い場合に使います。Factory Methodパターンと一緒にも良く使われます。Factoryクラスはインスタンスを一つしか作る必要はないです。

<b>クラスのインスタンスが一つしか生成されないことを保証する場合</b>に使われます。メッセージをつかさどるクラスのインスタンスはシステムにたった一つだけしか生成してほしくは無いはずです。なぜなら、複数のインスタンスがいると、それぞれのインスタンスが異なるメッセージを保持する可能性が出てきてしまうからです。Singletonで実装しない場合もありえますが、ほとんどの場合、このようなときはSingletonパターンを使います。

Singletonパターンを適用するときに注意する点として、<b>インスタンスが一つしかないので不用意に状態を変更しないように気をつけなければなりません。</b>不用意に変更できるようにしてしまうと、思わぬところでバグが発生してしまう可能性があります。

<h2>パターンの適用タイミング</h2>

Singletonのインスタンスは、イメージ的には<b>staticメソッドのみを持つUtil系のクラスに状態を持たせたもの</b>と考えることができます。ということは、Util系のクラスを作りたいのだが、状態も持たせたい場合に使えるパターンだと言えます。

もうひとつは、<b>インスタンスを一つしか生成したくない、同じインスタンスであることを保証したい場合</b>に使います。メッセージ管理クラスのような、システムに一つしか必要なくかつ、いつでも同じインスタンスの状態でいて欲しいクラスに、このパターンを適用します。

つまり、<strong>Util系のクラスに状態を持たせたい場合</strong>、<strong>システムに一つだけ、常に同じ状態のインスタンスを保証したい場合</strong>に使えるパターンです。

<h2>実装サンプルと参考文献</h2>

Singletonパターンの実装方法をもっと詳しく知りたい場合は、下記のサイトにアクセスするのをお勧めします。もしくは、参考書籍を載せておきますので、そちらをお買い求めください。(^^;)

+ TECHSCORE(Singletonパターン)
<a href="http://www.techscore.com/tech/DesignPattern/Singleton.html" rel="external nofollow">TECHSCORE</a>

+ Singletonパターンについての解説
<a href="http://www.tetras.co.jp/yada/j_java_singleton_r.htm" rel="external nofollow">Singletonパターン</a>

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




