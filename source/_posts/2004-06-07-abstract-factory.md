---
layout: post
title: "実践デザパタ-その9:Abstract Factoryパターン"
date: 2004-06-07 12:28
comments: true
categories: [Blog]
keywords: "デザインパターン,Abstract Factory,アブストラクト ファクトリ,GoF,Java"
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

9回目は、抽象概念を用いて、関連オブジェクトの集合を作成する<b>Abstract Factory</b>パターンのメモです。


<!-- more -->

<h2>Abstract Factoryパターン</h2>

<p class="option">Abstract Factoryパターンは、抽象化を用いて、関連オブジェクトの集合を作成するパターンです。</p>

<b>具象クラスを指定せずに、関連オブジェクトや従属オブジェクトの集合を生成するための規約を提供する</b>のがこのパターンの目的です。抽象的な話だと、わかったようでわからないということになりかねないので、自分なりに具体的な例でまとめておきます。

僕の中で、<b>Abstract Factoryパターンのイメージは、「<a href="http://hamasyou.com/archives/000171" rel="external nofollow">Factory Method</a> + ポリモーフィズム</b>」だと思っています。Factory Methodパターンには、関連オブジェクトを自分の責務で生成するというものがありました。この目的で使うFactoryクラスを生成するFactoryがこのAbstract Factoryです。

つまり、関連オブジェクトを生成するFactoryの種類がたくさんあるので、Factoryを生成するFactoryを作った。それがAbstract Factoryだということです。このイメージで、Abstract Factoryパターンのクラス図を見るとわかりやすいと思います。

<img src="http://hamasyou.com/images/abstract_factory/abstract_factory1.gif"   style="margin:5px" />

「OSFactory」がAbstract Factoryクラスになっていて、「WindowsFactory」、「MacintoshFactory」がそれぞれFactoryクラスです。

Abstract Factoryパターンは、今のようなOS毎に提供するリソースが違う場合に使われたり、GUIの生成時に使われたりします。JavaのAWTで使われているToolkitクラスはこのパターンが使われています。プラットフォームごとに実装が異なっています。

Toolkitでは、この他 Bridgeパターンの使われています。Abstract Factoryで生成した部品の実装をBridgeで管理して、プラットフォーム毎に異なる実装を隠しています。

<h2>パターンの適用タイミング</h2>

Abstract Factoryパターンの長所は、新たな実装を提供するのに柔軟性があるという点です。先ほどのOSの例だと、Factoryクラスに「UnixFactory」クラスが増えても、<a href="http://www.alles.or.jp/~torutk/oojava/oo/develop/011.html" rel="external nofollow">開放・閉鎖原則</a>は守られています。

逆に、Abstract Factoryパターンの欠点は、関連オブジェクト(先ほどの例だと「ブラウザ」「ファイルシステム」)の種類が増えた場合、修正箇所がすべてのFactoryクラスに及んでしまう点です。この場合だと、<a href="http://www.alles.or.jp/~torutk/oojava/oo/develop/011.html" rel="external nofollow">開放・閉鎖原則</a>は守られていません。<strong>Abstract Factoryインターフェースを適切に定義しておく必要がある</strong>ということです。

<strong>関連オブジェクトが複数あり、生成の責務を自分が持つ場合</strong>、<strong>リソースの関連に対して、複数の実装がありえる場合</strong>。これらの場合に、Abstract Factoryパターンが使えると思います。

<h2>実装サンプルと参考文献</h2>

Abstract Factoryパターンの実装方法をもっと詳しく知りたい場合は、下記のサイトにアクセスするのをお勧めします。もしくは、参考書籍を載せておきますので、そちらをお買い求めください。(^^;

+ 日立ソフト(Abstract Factoryパターン)
<a href="http://www.dmz.hitachi-sk.co.jp/Java/Tech/pattern/gof/abstract_factory.html" rel="external nofollow">日立ソフト</a>

+ Skeleton of GOF's Design Pattern(JavaとC++のサンプルがあります)
<a href="http://www002.upp.so-net.ne.jp/ys_oota/mdp/AbstractFactory/index.htm" rel="external nofollow">Abstract Factoryの骸骨</a>

+ Codian(生成に関するパターン)
<a href="http://www.kab-studio.biz/Programing/Codian/DesignPattern/02.html" rel="external nofollow">生成に関するパターン</a>

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




