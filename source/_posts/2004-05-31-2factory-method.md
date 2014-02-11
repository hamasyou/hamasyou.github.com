---
layout: post
title: "実践デザパタ-その2:Factory Methodパターン"
date: 2004-05-31 13:11
comments: true
categories: [Engineer-Soul]
keywords: "デザインパターン,Factory Method,アダプター,GoF,Java"
tags: []
author: hamasyou
amazon_url: ""
amazon_author: ""
amazon_image: ""
amazon_publisher: ""
---

デザインパターンとは、システム設計におけるクラスやインターフェースの関係に名前をつけたものです。GoFの23のパターンが有名です。

デザインパターンというのは、どんなパターンなのか、パターンの目的は何かということを覚えることが非常に重要なのですが、これを実際に適用しようとした場合に、いつ適用していいかが見えてこないとお話になりません。

そこで、自分の勉強も兼ねつつ、パターンの実践時における使用場所や、パターンを適用するきっかけを見つけられるようにメモしておきます。

今回は、インスタンスを生成する責務を切り出した、<strong>Factory Method</strong>パターンです。


<!-- more -->

<h2>Factory Methodパターン</h2>

<p class="option">Factory Methodパターンは、生成するインスタンスの種類が多くなりそうな場合に、
インスタンス生成の責務を外に切り出して、保守性を高めるパターンです。</p>

<img src="http://hamasyou.com/images/factory_method/factory_method.gif" alt="Factory   Methodのクラス図" />

例えば、ショッピングサイトシステムを作っているとしましょう。このショッピングサイトでは、現在では5種類の商品しか販売していないのですが、この先商品の種類が増える可能性があります。しかし、どのような種類の商品が増えるかはわかりません。

この、<strong>将来的に種類が増えるかもしれないが、今はまだわからない</strong>といった状況でよく使われれるパターンです。

Factory Methodパターンを使うと、インスタンスの生成処理で既存のプログラムを修正することなく、新しい種類(例えば本とか、コンピュータといったカテゴリのこと)を追加することができます。

<dl>
<dt class="info">開放・閉鎖原則</dt>
<dd>ただし、インスタンス生成のプログラムは、新規に作る必要があります。既存のコードを修正せずに、新しいコードで修正を行えるように設計することを、<strong>開放・閉鎖原則(Open・Closed Principle)</strong>といいます。</dd>
</dl>

<h2>パターンの適用タイミング</h2>

このパターンは、主に、<strong>インスタンスの種類が将来的に増えそうな場合</strong>に使われます。

もうひとつは、<strong>コンストラクタではない場所でインスタンスの生成を行いたい場合(遅延ロード)</strong>に使えます。コンストラクタを直接呼ばせたくはないが、インスタンスは作成したい時がこの例です。

他には、<strong>ポリモーフィズムを使いたいのだが、インスタンスの生成は共通化したい場合</strong>に使います。

<pre class="code"><code><span class="comment">/* Staffというスーパークラスがあり、AccountantクラスとSalesmanクラスと、
 * Developerクラスがあるとする。*/</span>
Staff staff = <span class="keyword">new</span> Accountant();
staff.calculateSalary();
staff = <span class="keyword">new</span> Salesman();
staff.calculateSalary();
staff = <span class="keyword">new</span> Developer();
staff.calculateSalary();
</code></pre>

上記の例は、Staffスーパークラスで、実際のサブクラスを受けて、ポリモーフィズムを使って、自分の給料を計算させるプログラムだと考えてください。ここでは、それぞれインスタンスをサブクラス名を使って生成しています。

これを、Factory Methodを使って書き換えると

<pre class="code"><code><span class="comment">/* Staffというスーパークラスがあり、AccountantクラスとSalesmanクラスと、
 * Developerクラスがあるとする。*/</span>
Staff staff = StaffFactory.createStaff(args[<span class="literal">0</span>]);
staff.calculateSalary();
</code></pre>

というように、サブクラスのインスタンス生成部を隠蔽することができます。

Factory Methodパターンには、もうひとつ重要な役割がありました。<strong>あるオブジェクトに関連するオブジェクトを、自身で作成させるというものです。</strong>

例えば、部品Aというオブジェクトは、ねじAというオブジェクトを必要とし、部品Bというオブジェクトは、ねじBというオブジェクトを必要とするとします。Factory Methodパターンを使うと、ねじA・ねじBを作成する責務を持つのは、それぞれ部品A、部品Bということになります。

<pre>
ねじ partA = 部品A.createPart();
ねじ partB = 部品B.createPart();
</pre>

Factory Methodパターンは

<ul>
<li>インスタンスの種類が将来増えそうな場合</li>
<li>コンストラクタではない場所でインスタンスを生成したい場合</li>
<li>サブクラスを指定せずにインスタンスを生成したい場合</li>
<li>対となるオブジェクトを関連するオブジェクトに生成させたい場合</li>
</ul>

に使えるパターンだと言えます。


<h2>実装サンプルと参考文献</h2>

Factory Methodパターンの実装方法をもっと詳しく知りたい場合は、下記のサイトにアクセスするのをお勧めします。もしくは、参考書籍を載せておきますので、そちらをお買い求めください。(^^;)

+ TECHSCORE(Factory Methodパターン)

[ target="_blank" class="extlink">TECHSCORE](http://www.techscore.com/tech/DesignPattern/FactoryMethod.html)

+ Open/Closed Principleについての解説

<img src="http://hamasyou.com/images/img-link.gif"   align="middle" />[ target="_blank" class="uline">Open-Closed Principle とデザインパターン](http://homepage3.nifty.com/masarl/article/dp-ocp-2.html)

+ デザインパターンのお勧め書籍

<div class="rakuten"><table border="0" cellpadding="5" width="400"><tr><td valign="top">[<img src="http://images-jp.amazon.com/images/P/4797327030.09.MZZZZZZZ.jpg"   border="0" />](http://www.amazon.co.jp/exec/obidos/ASIN/4797327030/sorehabooks-22/)</td><td valign="top" />[増補改訂版Java言語で学ぶデザインパターン入門](http://www.amazon.co.jp/exec/obidos/ASIN/4797327030/sorehabooks-22/)<br />結城 浩<br /><iframe scrolling="no" frameborder="0" width="250" height="40" hspace="0" vspace="0" marginheight="0" marginwidth="0" src="http://xml-jp.amznxslt.com/onca/xml3?dev-t=D2JW5SAFEH7L0B&t=goodpic-22&f=http://www.g-tools.com/xsl/aws-price-ffffff.xsl&locale=jp&type=lite&AsinSearch=4797327030"></iframe><br /><br /><font size="-1"><b>おすすめ平均</b><img src="http://g-images.amazon.com/images/G/01/detail/stars-5-0.gif"   /><br /><img src="http://g-images.amazon.com/images/G/01/detail/stars-5-0.gif"   />この本なしにJavaは語れない<br /></font><br />[ /><font size="-1">Amazonで詳しく見る</font>](http://www.amazon.co.jp/exec/obidos/ASIN/4797327030/sorehabooks-22/)<img src="http://www.g-tools.com/img/spacer.gif"   width="50" height="1" />[ /><img src="http://www.g-tools.com/img/powered-by-gtool.gif"   border="0" alt="4797327030"/>](http://www.goodpic.com/mt/aws/)<br /></td></tr></table>
</div>

+ 独習シリーズのデザインパターン編。デザインパターンを一人でも学べます。

<div class="rakuten"><table border="0" cellpadding="5" width="400"><tr><td valign="top">[<img src="http://images-jp.amazon.com/images/P/4798104450.09.MZZZZZZZ.jpg"   border="0" />](http://www.amazon.co.jp/exec/obidos/ASIN/4798104450/sorehabooks-22/)</td><td valign="top" />[独習デザインパターン](http://www.amazon.co.jp/exec/obidos/ASIN/4798104450/sorehabooks-22/)<br />株式会社テクノロジックアート ， 長瀬 嘉秀<br /><iframe scrolling="no" frameborder="0" width="250" height="40" hspace="0" vspace="0" marginheight="0" marginwidth="0" src="http://xml-jp.amznxslt.com/onca/xml3?dev-t=D2JW5SAFEH7L0B&t=goodpic-22&f=http://www.g-tools.com/xsl/aws-price-ffffff.xsl&locale=jp&type=lite&AsinSearch=4798104450"></iframe><br /><br /><font size="-1"><b>おすすめ平均</b><img src="http://g-images.amazon.com/images/G/01/detail/stars-5-0.gif"   /><br /><img src="http://g-images.amazon.com/images/G/01/detail/stars-5-0.gif"   />GoF本で挫折した人の為の本<br /></font><br />[ /><font size="-1">Amazonで詳しく見る</font>](http://www.amazon.co.jp/exec/obidos/ASIN/4798104450/sorehabooks-22/)<img src="http://www.g-tools.com/img/spacer.gif"   width="50" height="1" />[ /><img src="http://www.g-tools.com/img/powered-by-gtool.gif"   border="0" alt="4798104450"/>](http://www.goodpic.com/mt/aws/)<br /></td></tr></table>
</div>

+ Sun Microsystemのお墨付き。GoF以外のパターンも学べます。

<div class="rakuten"><table border="0" cellpadding="5" width="400"><tr><td valign="top">[<img src="http://images-jp.amazon.com/images/P/4756141552.09.MZZZZZZZ.jpg"   border="0" />](http://www.amazon.co.jp/exec/obidos/ASIN/4756141552/sorehabooks-22/)</td><td valign="top" />[デザインパターンによるJava実践プログラミング](http://www.amazon.co.jp/exec/obidos/ASIN/4756141552/sorehabooks-22/)<br />スティーヴン シュテルティン, オーラブ マースセン, Stephen Stelting, Olav Maassen, クイック<br /><iframe scrolling="no" frameborder="0" width="250" height="40" hspace="0" vspace="0" marginheight="0" marginwidth="0" src="http://xml-jp.amznxslt.com/onca/xml3?dev-t=D2JW5SAFEH7L0B&t=goodpic-22&f=http://www.g-tools.com/xsl/aws-price-ffffff.xsl&locale=jp&type=lite&AsinSearch=4756141552"></iframe><br /><br /><font size="-1"><b>おすすめ平均</b><img src="http://g-images.amazon.com/images/G/01/detail/stars-5-0.gif"   /><br /><img src="http://g-images.amazon.com/images/G/01/detail/stars-5-0.gif"   />かなりの良書<br /></font><br />[ /><font size="-1">Amazonで詳しく見る</font>](http://www.amazon.co.jp/exec/obidos/ASIN/4756141552/sorehabooks-22/)<img src="http://www.g-tools.com/img/spacer.gif"   width="50" height="1" />[ /><img src="http://www.g-tools.com/img/powered-by-gtool.gif"   border="0" alt="4756141552"/>](http://www.goodpic.com/mt/aws/)<br /></td></tr></table>
</div>

+ UMLを使って、オブジェクト指向のいいとこ取りができます。

<div class="rakuten"><table border="0" cellpadding="5" width="400"><tr><td valign="top">[<img src="http://images-jp.amazon.com/images/P/4774116882.09.MZZZZZZZ.jpg"   border="0" />](http://www.amazon.co.jp/exec/obidos/ASIN/4774116882/sorehabooks-22/)</td><td valign="top" />[UML 500の技](http://www.amazon.co.jp/exec/obidos/ASIN/4774116882/sorehabooks-22/)<br />Windowsプログラミング愛好会<br /><iframe scrolling="no" frameborder="0" width="250" height="40" hspace="0" vspace="0" marginheight="0" marginwidth="0" src="http://xml-jp.amznxslt.com/onca/xml3?dev-t=D2JW5SAFEH7L0B&t=goodpic-22&f=http://www.g-tools.com/xsl/aws-price-ffffff.xsl&locale=jp&type=lite&AsinSearch=4774116882"></iframe><br /><br /><font size="-1"><b>おすすめ平均</b><img src="http://g-images.amazon.com/images/G/01/detail/stars-3-5.gif"   /><br /><img src="http://g-images.amazon.com/images/G/01/detail/stars-2-0.gif"   />たいした「技」は載っていません<br /><img src="http://g-images.amazon.com/images/G/01/detail/stars-5-0.gif"   />私にはよかったと思います。<br /></font><br />[ /><font size="-1">Amazonで詳しく見る</font>](http://www.amazon.co.jp/exec/obidos/ASIN/4774116882/sorehabooks-22/)<img src="http://www.g-tools.com/img/spacer.gif"   width="50" height="1" />[ /><img src="http://www.g-tools.com/img/powered-by-gtool.gif"   border="0" alt="4774116882"/>](http://www.goodpic.com/mt/aws/)<br /></td></tr></table>
</div>

+ デザインパターンだけではなく、ソフトウェア設計の原則やプラクティスまで学びたい人におすすめ

<div class="rakuten"><table border="0" cellpadding="5" width="400"><tr><td valign="top">[<img src="http://images-jp.amazon.com/images/P/4797323361.09.MZZZZZZZ.jpg"   border="0" />](http://www.amazon.co.jp/exec/obidos/ASIN/4797323361/sorehabooks-22/)</td><td valign="top" />[アジャイルソフトウェア開発の奥義](http://www.amazon.co.jp/exec/obidos/ASIN/4797323361/sorehabooks-22/)<br />ロバート・C・マーチン ， 瀬谷 啓介<br /><iframe scrolling="no" frameborder="0" width="250" height="40" hspace="0" vspace="0" marginheight="0" marginwidth="0" src="http://xml-jp.amznxslt.com/onca/xml3?dev-t=D2JW5SAFEH7L0B&t=goodpic-22&f=http://www.g-tools.com/xsl/aws-price-ffffff.xsl&locale=jp&type=lite&AsinSearch=4797323361"></iframe><br /><br /><font size="-1"><b>おすすめ平均</b><img src="http://g-images.amazon.com/images/G/01/detail/stars-5-0.gif"   /><br /><img src="http://g-images.amazon.com/images/G/01/detail/stars-5-0.gif"   />体系だてられた経験的ガイドラインか。<br /></font><br />[ /><font size="-1">Amazonで詳しく見る</font>](http://www.amazon.co.jp/exec/obidos/ASIN/4797323361/sorehabooks-22/)<img src="http://www.g-tools.com/img/spacer.gif"   width="50" height="1" />[ /><img src="http://www.g-tools.com/img/powered-by-gtool.gif"   border="0" alt="4797323361"/>](http://www.goodpic.com/mt/aws/)<br /></td></tr></table>
</div>




