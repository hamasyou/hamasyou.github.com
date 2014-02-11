---
layout: post
title: "実践デザパタ-その12:Strategyパターン"
date: 2004-06-19 23:31
comments: true
categories: [Engineer-Soul]
keywords: "デザインパターン,Strategy,ストラテジ,GoF,Java,振る舞い"
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

12番目は処理手順やアルゴリズムをスムーズに交換できるようにする<b>Strategy</b>パターンのメモです。


<!-- more -->

<h2>Strategyパターン</h2>

<p class="option">　Strategyパターンは、実行時に処理のアルゴリズムを決定したいときに使うパターンです。</p>

処理のアルゴリズムとは、例えばソートの方法であったり、数値の計算の仕方であったりします。

<img src="http://hamasyou.com/images/design_pattern/strategy.gif" alt="Strategyのクラス図" />

1つ目のアルゴリズムを使うと計算は速いがメモリをたくさん使う。2つ目のアルゴリズムを使うと計算は遅いがメモリは少なくすむ。1つ目のアルゴリズムと2つ目のアルゴリズムを使うかは、ユーザに選択させたい場合など、動的にアルゴリズムを切り替える必要があります。こういった場合に使えるパターンとなります。

Strategyパターンは、[ class="extlink" target="_blank">コンポジション](http://mikata.curiocube.com/oop/part2/ch11_composition.html)という手法を用いて実装されます。コンポジションの主な利用用途として、インスタンス間の緩やかな関係を築く場合に使われます。Strategyパターンの様に実行時に何かを決めたいという場合は、その処理を専門で行うインスタンスとして切り出し、そのインスタンスへの[ class="extlink" target="_blank">委譲](http://www.netlaputa.ne.jp/~hijk/study/oo/ooglossary.html)を用いるという手法が良くとられます。これがコンポジションの主な実装になります。

<h2>パターンの適用タイミング</h2>

Strategyパターンの使い時はわかりやすいと思います。処理の方法やアルゴリズムを動的(実行時)に切り替えたい場合に使います。

Strategyパターンと同じ実装方法で同じクラス図になるパターンに、[ class="extlink" target="_blank">Stateパターン](http://www.hellohiro.com/pattern/state.htm)というのがあります。このパターンは、<b>インスタンスの状態を動的に変更したい場合に使う</b>ものです。<b class="red">Strategyパターンとは概念が違います。</b>Strategyパターンは、動的に処理方法やアルゴリズムを変化させます。

<h2>実装サンプルと参考文献</h2>

Strategyパターンの実装方法をもっと詳しく知りたい場合は、下記のサイトにアクセスするのをお勧めします。もしくは、参考書籍を載せておきますので、そちらをお買い求めください。(^^;

+ 日立ソフト(Strategyパターン)
[ target="_blank" class="extlink">日立ソフト](http://www.dmz.hitachi-sk.co.jp/Java/Tech/pattern/gof/strategy.html)

+ Skeleton of GOF's Design Pattern(JavaとC++のサンプルがあります)
[ target="_blank" class="extlink">Strategyの骸骨](http://www002.upp.so-net.ne.jp/ys_oota/mdp/Strategy/index.htm)

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




