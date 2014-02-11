---
layout: post
title: "実践デザパタ-その1:Adapterパターン"
date: 2004-05-31 00:13
comments: true
categories: [Engineer-Soul]
keywords: "デザインパターン,Adapter,アダプター,GoF,Java"
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

最初は、仕様の違いを吸収する目的で使われる、<strong>Adapter</strong>パターンです。


<!-- more -->

<h2>Adapterパターン</h2>

<p class="option">Adapterパターンは、仕様の違い(インターフェースの違いとも言う)を吸収する目的で使われるパターンです。</p>

<img src="http://hamasyou.com/images/adapter/adapter.gif" alt="Adapterクラス図" />

例えば、Javaにはプリミティブ型と呼ばれる非オブジェクト型の存在があります。intやshort、long、byte、float、doubleなどです。これをListに追加するとき、Javaのリストはオブジェクト型しか引数に取ることができないので、直接は格納することができません。

この場合、おそらくint → Integer、double → Double といった感じで、オブジェクト型のクラスを使うことになると思います。JavaでAdapterパターンを使用したよい例が、以上のような例です。

これが仕様の違いです。このような時、Adapterパターンを使って、仕様の違いを吸収します。
Adapterパターンは、別名ラッパーとも呼ばれます。

<h2>パターンの適用タイミング</h2>

このパターンは仕様の違いを吸収するだけでなく、Adapterとなるクラスを介して、層を増やすことができます。<strong>一般に層を増やすと、変更に強くなり、保守性が高まります。</strong>

<strong>サードパティ製品、例えばExcelをJavaから操作できるようなライブラリ、をプロジェクトで使う場合</strong>などに、Adapterパターンを使うことができます。サードパティ製品と、プロジェクトドメインのクラスとの間にAdapterを噛ますことで、層を増やし、変更に強くすることができます。

また、<strong>サードパティによって提供されている機能の中に、たまたま必要な機能がほんの少し足りない場合</strong>など、Adapterパターンで実装を隠してしまえば、サードパティ製品を利用している他のところと統一的にコーディングすることができます。

もうひとつ、<strong>ソースコードを直接いじることができないのに機能の追加や呼び出し先の変更を変更する必要がある場合。</strong>

例えば、すでに稼働中のアプリケーションで、ある機能を試しに別の機能に変更してみたい場合などがそうです。この場合は、稼働中の機能と共通のインターフェースを実装したAdapterが別の機能を呼び出すようにするといった使い方ができます。

<ul>
<li>サードパティによって提供されるライブラリを使う場合</li>
<li>統一的なインターフェースを使いたいのだが、ほんの少しだけ機能が足りない場合</li>
<li>ソースコードに手を入れられないのだが、機能追加、機能変更を行いたい場合</li>
</ul>

の内のどれか一つに当てはまる場合、このパターンを使えるタイミングかもしれません。

<h2>実装サンプルと参考文献</h2>

Adapterパターンの実装方法をもっと詳しく知りたい場合は、下記のサイトにアクセスするのをお勧めします。もしくは、参考書籍を載せておきますので、そちらをお買い求めください。(^^;)

+ TECHSCORE(Adapterパターン)

[ target="_blank" class="extlink">TECHSCORE](http://www.techscore.com/tech/DesignPattern/Adapter.html)

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




