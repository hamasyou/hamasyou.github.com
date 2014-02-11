---
layout: post
title: "実践デザパタ-その15:Visitorパターン"
date: 2005-01-28 15:58
comments: true
categories: [Engineer-Soul]
keywords: "デザインパターン,Visitor,ビジター,GoF,Java,データ構造,振る舞い,分離,拡張,追加"
tags: []
author: hamasyou
amazon_url: ""
amazon_author: ""
amazon_image: ""
amazon_publisher: ""
---

デザインパターンとは、システム設計におけるクラスやインターフェースの関係に名前をつけたものです。
GoFの23のパターンが有名です。デザインパターンというのは、どんなパターンなのか、パターンの目的は何かということを覚えることが非常に重要なのですが、これを実際に適用しようとした場合に、いつ適用していいかが見えてこないとお話になりません。

そこで、自分の勉強も兼ねつつ、パターンの実践時における使用場所や、パターンを適用するきっかけを見つけられるようにメモしておきます。

15番目のパターンはデータ構造と振る舞いを分離して、振る舞いを実行するクラスの変更や拡張を可能にする<strong>Visitor</strong>パターンのメモです。


<!-- more -->

<h2>Visitorパターン</h2>

<p class="option">Visitor パターンは、データ構造と振る舞いを別々のクラスに扱わせて、振る舞いの変更や追加、拡張を可能にするパターンです。</p>

<img src="http://hamasyou.com/images/design_pattern/visitor.gif" alt="Visitorのクラス図" />

Visitorパターンは、<em>データ構造(データの階層構造)が変化しない場合に限り有効なパターンになります</em>。Visitorパターンを使う目的は一つで、<em>データ構造に対して、振る舞いが変化する可能性がある場合に対応しやすくする事です</em>。

クラス図を見ると、インターフェースVisitor には visit というメソッドが2つあります。これはデータ構造が2種類あることを示しています。振る舞いを追加する場合、インターフェースVisitorを実装したクラスを作ることで対応します。

このパターンは<strong>ダブルディスパッチ</strong>とも呼ばれます。データ構造側のポリモーフィズムを使ったディスパッチと、振る舞い側のポリモーフィズムを使ったディスパッチの2回を使っています。

<section>

<h4>mainメソッド</h4>

<pre class="code"><code>Visitor visitor = <span class="keyword">new</span> ConcreteVisitorA(); 
DataElement elementA = <span class="keyword">new</span> ConcreteDataElementA(); 
 
elementA.accept(visitor); 
</code></pre>

<section>

<h4>データ構造クラス</h4>

<pre class="code"><code><span class="keyword">public</span> <span class="keyword">void</span> accept(Visitor visitor) { 
    visitor.visit(this); 
} 
</code></pre>

Visitorパターンの注目する点は、<em>インターフェースVisitのメソッド引数に実装クラス名が指定されていること</em>です。これによって、データ構造ごとにメソッドを実装することができ、メソッドの中身がきれいになります。ここで抽象クラス名を使ってしまうと、メソッドの内部でクラスの種類によって条件分岐の処理をしなければいけなくなります。つまり、Visitorはデータ構造に依存していると言うことです。<em>データ構造が変更になると、Visitor自体も変更する必要があることに注意です</em>。

<h3>Visitor の使いどころ</h3>

{% blockquote 『[ target="_blank" class="extlink">アジャイルソフトウェア開発の奥義](http://www.amazon.co.jp/exec/obidos/ASIN/4797323361/sorehabooks-22)』 %}
Visitorパターンがよく使われるのは、「<em>大きなデータ構造を渡り歩き、レポートを出力するような場合</em>」である。そうすれば、データ構造体オブジェクト自体がレポート生成用のコードを持たなくていい。また、データ構造体のコードを変更しなくても、新しいレポート機能を追加したいときには、新しいVisitorを追加することで実現できる。このことは、レポート機能を別コンポーネントとして分離し、顧客にとって必要な機能だけを個別に配布できることを意味している。


{% endblockquote %}

また、Visitor パターンを使う場合として、「<em>Validation処理(妥当性チェック)</em>」が挙げれると思います。処理するデータ構造を決めてしまって、ValidationクラスをVisitor として実装すれば、必要なチェックを実行時に追加、削除できるようになると思います。

<h2>実装サンプルと参考文献</h2>

+ 日立ソフト(Visitorパターン)
[ target="_blank" class="extlink">日立ソフト](http://www.dmz.hitachi-sk.co.jp/Java/Tech/pattern/gof/visitor.html)

+ Skeleton of GOF's Design Pattern(JavaとC++のサンプルがあります)
[ target="_blank" class="extlink">Visitorの骸骨](http://www002.upp.so-net.ne.jp/ys_oota/mdp/Visitor/index.htm)

+ デザインパターンのお勧め書籍
<div class="rakuten"><table width=400 border="0" cellpadding="5"><tr><td colspan="2">[ target="_blank">増補改訂版Java言語で学ぶデザインパターン入門](http://www.amazon.co.jp/exec/obidos/ASIN/4797327030/sorehabooks-22/)</td></tr><tr><td valign="top">[ target="_blank"><img src="http://images-jp.amazon.com/images/P/4797327030.09.MZZZZZZZ.jpg"   border="0" />](http://www.amazon.co.jp/exec/obidos/ASIN/4797327030/sorehabooks-22/)</td><td valign="top"><font size="-1">結城 浩<br /><br /><iframe scrolling="no" frameborder="0" width="200" height="40" hspace="0" vspace="0" marginheight="0" marginwidth="0" src="http://webservices.amazon.co.jp/onca/xml?Service=AWSECommerceService&SubscriptionId=0G91FPYVW6ZGWBH4Y9G2&AssociateTag=goodpic-22&Operation=ItemLookup&IdType=ASIN&ContentType=text/html&Page=1&ResponseGroup=Offers&ItemId=4797327030&Version=2004-10-04&Style=http://www.g-tools.net/xsl/priceFFFFFF.xsl"></iframe><br /><em>おすすめ平均</em><img src="http://g-images.amazon.com/images/G/01/detail/stars-5-0.gif"   /><br /><img src="http://g-images.amazon.com/images/G/01/detail/stars-5-0.gif"   />JAVA&OOPの入門書<br /><img src="http://g-images.amazon.com/images/G/01/detail/stars-5-0.gif"   />プログラマー必見でしょう<br /><img src="http://g-images.amazon.com/images/G/01/detail/stars-5-0.gif"   />ソースコードが読める<br /><img src="http://g-images.amazon.com/images/G/01/detail/stars-5-0.gif"   />この本なしにJavaは語れない<br /><br />[ target="_blank">Amazonで詳しく見る](http://www.amazon.co.jp/exec/obidos/ASIN/4797327030/sorehabooks-22/)</font><font size="-2">by [G-Tools](http://www.goodpic.com/mt/aws/)</font><br /></td></tr></table></div>

+ 独習シリーズのデザインパターン編。デザインパターンを一人でも学べます。
<div class="rakuten"><table width=400 border="0" cellpadding="5"><tr><td colspan="2">[ target="_blank">独習デザインパターン](http://www.amazon.co.jp/exec/obidos/ASIN/4798104450/sorehabooks-22/)</td></tr><tr><td valign="top">[ target="_blank"><img src="http://images-jp.amazon.com/images/P/4798104450.09.MZZZZZZZ.jpg"   border="0" />](http://www.amazon.co.jp/exec/obidos/ASIN/4798104450/sorehabooks-22/)</td><td valign="top"><font size="-1">株式会社テクノロジックアート長瀬 嘉秀<br /><br /><iframe scrolling="no" frameborder="0" width="200" height="40" hspace="0" vspace="0" marginheight="0" marginwidth="0" src="http://webservices.amazon.co.jp/onca/xml?Service=AWSECommerceService&SubscriptionId=0G91FPYVW6ZGWBH4Y9G2&AssociateTag=goodpic-22&Operation=ItemLookup&IdType=ASIN&ContentType=text/html&Page=1&ResponseGroup=Offers&ItemId=4798104450&Version=2004-10-04&Style=http://www.g-tools.net/xsl/priceFFFFFF.xsl"></iframe><br /><em>おすすめ平均</em><img src="http://g-images.amazon.com/images/G/01/detail/stars-4-5.gif"   /><br /><img src="http://g-images.amazon.com/images/G/01/detail/stars-4-0.gif"   />デザインパターンをはじめるには最適な書<br /><img src="http://g-images.amazon.com/images/G/01/detail/stars-5-0.gif"   />分かりやすいGoFデザインパターンの説明<br /><img src="http://g-images.amazon.com/images/G/01/detail/stars-5-0.gif"   />GoF本で挫折した人の為の本<br /><br />[ target="_blank">Amazonで詳しく見る](http://www.amazon.co.jp/exec/obidos/ASIN/4798104450/sorehabooks-22/)</font><font size="-2">by [G-Tools](http://www.goodpic.com/mt/aws/)</font><br /></td></tr></table></div>

+ Sun Microsystemのお墨付き。GoF以外のパターンも学べます。
<div class="rakuten"><table width=400 border="0" cellpadding="5"><tr><td colspan="2">[ target="_blank">デザインパターンによるJava実践プログラミング](http://www.amazon.co.jp/exec/obidos/ASIN/4756141552/sorehabooks-22/)</td></tr><tr><td valign="top">[ target="_blank"><img src="http://images-jp.amazon.com/images/P/4756141552.09.MZZZZZZZ.jpg"   border="0" />](http://www.amazon.co.jp/exec/obidos/ASIN/4756141552/sorehabooks-22/)</td><td valign="top"><font size="-1">スティーヴン シュテルティンオーラブ マースセンStephen SteltingOlav Maassenクイック<br /><br /><iframe scrolling="no" frameborder="0" width="200" height="40" hspace="0" vspace="0" marginheight="0" marginwidth="0" src="http://webservices.amazon.co.jp/onca/xml?Service=AWSECommerceService&SubscriptionId=0G91FPYVW6ZGWBH4Y9G2&AssociateTag=goodpic-22&Operation=ItemLookup&IdType=ASIN&ContentType=text/html&Page=1&ResponseGroup=Offers&ItemId=4756141552&Version=2004-10-04&Style=http://www.g-tools.net/xsl/priceFFFFFF.xsl"></iframe><br /><em>おすすめ平均</em><img src="http://g-images.amazon.com/images/G/01/detail/stars-5-0.gif"   /><br /><img src="http://g-images.amazon.com/images/G/01/detail/stars-5-0.gif"   />かなりの良書<br /><br />[ target="_blank">Amazonで詳しく見る](http://www.amazon.co.jp/exec/obidos/ASIN/4756141552/sorehabooks-22/)</font><font size="-2">by [G-Tools](http://www.goodpic.com/mt/aws/)</font><br /></td></tr></table></div>

+ UMLを使って、オブジェクト指向のいいとこ取りができます。
<div class="rakuten"><table width=400 border="0" cellpadding="5"><tr><td colspan="2">[ target="_blank">UML 500の技](http://www.amazon.co.jp/exec/obidos/ASIN/4774116882/sorehabooks-22/)</td></tr><tr><td valign="top">[ target="_blank"><img src="http://images-jp.amazon.com/images/P/4774116882.09.MZZZZZZZ.jpg"   border="0" />](http://www.amazon.co.jp/exec/obidos/ASIN/4774116882/sorehabooks-22/)</td><td valign="top"><font size="-1">Windowsプログラミング愛好会<br /><br /><iframe scrolling="no" frameborder="0" width="200" height="40" hspace="0" vspace="0" marginheight="0" marginwidth="0" src="http://webservices.amazon.co.jp/onca/xml?Service=AWSECommerceService&SubscriptionId=0G91FPYVW6ZGWBH4Y9G2&AssociateTag=goodpic-22&Operation=ItemLookup&IdType=ASIN&ContentType=text/html&Page=1&ResponseGroup=Offers&ItemId=4774116882&Version=2004-10-04&Style=http://www.g-tools.net/xsl/priceFFFFFF.xsl"></iframe><br /><em>おすすめ平均</em><img src="http://g-images.amazon.com/images/G/01/detail/stars-3-5.gif"   /><br /><img src="http://g-images.amazon.com/images/G/01/detail/stars-2-0.gif"   />たいした「技」は載っていません<br /><img src="http://g-images.amazon.com/images/G/01/detail/stars-5-0.gif"   />私にはよかったと思います。<br /><br />[ target="_blank">Amazonで詳しく見る](http://www.amazon.co.jp/exec/obidos/ASIN/4774116882/sorehabooks-22/)</font><font size="-2">by [G-Tools](http://www.goodpic.com/mt/aws/)</font><br /></td></tr></table></div>

+ デザインパターンだけではなく、ソフトウェア設計の原則やプラクティスまで学びたい人におすすめ
<div class="rakuten"><table width=400 border="0" cellpadding="5"><tr><td colspan="2">[ target="_blank">アジャイルソフトウェア開発の奥義](http://www.amazon.co.jp/exec/obidos/ASIN/4797323361/sorehabooks-22/)</td></tr><tr><td valign="top">[ target="_blank"><img src="http://images-jp.amazon.com/images/P/4797323361.09.MZZZZZZZ.jpg"   border="0" />](http://www.amazon.co.jp/exec/obidos/ASIN/4797323361/sorehabooks-22/)</td><td valign="top"><font size="-1">ロバート・C・マーチン瀬谷 啓介<br /><br /><iframe scrolling="no" frameborder="0" width="200" height="40" hspace="0" vspace="0" marginheight="0" marginwidth="0" src="http://webservices.amazon.co.jp/onca/xml?Service=AWSECommerceService&SubscriptionId=0G91FPYVW6ZGWBH4Y9G2&AssociateTag=goodpic-22&Operation=ItemLookup&IdType=ASIN&ContentType=text/html&Page=1&ResponseGroup=Offers&ItemId=4797323361&Version=2004-10-04&Style=http://www.g-tools.net/xsl/priceFFFFFF.xsl"></iframe><br /><em>おすすめ平均</em><img src="http://g-images.amazon.com/images/G/01/detail/stars-4-5.gif"   /><br /><img src="http://g-images.amazon.com/images/G/01/detail/stars-4-0.gif"   />いい本だとおもいます<br /><img src="http://g-images.amazon.com/images/G/01/detail/stars-5-0.gif"   />「奥義」の名に恥じない内容<br /><img src="http://g-images.amazon.com/images/G/01/detail/stars-5-0.gif"   />体系だてられた経験的ガイドラインか。<br /><br />[ target="_blank">Amazonで詳しく見る](http://www.amazon.co.jp/exec/obidos/ASIN/4797323361/sorehabooks-22/)</font><font size="-2">by [G-Tools](http://www.goodpic.com/mt/aws/)</font><br /></td></tr></table></div>




