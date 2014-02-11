---
layout: post
title: "実践デザパタ-その10:Builderパターン"
date: 2004-06-09 09:29
comments: true
categories: [Engineer-Soul]
keywords: "デザインパターン,Builder,ビルダー,GoF,Java"
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

10回目になりました。インスタンスの生成手順や生成内容が複雑な場合に、インスタンス生成作業を軽減する<b>Builder</b>パターンのメモです。


<!-- more -->

<h2>Builderパターン</h2>

<p class="option">Builderパターンは、インスタンス生成手順が複雑な場合やインスタンス生成構成が複雑な場合に、生成を簡略化するパターンです。</p>

インスタンス生成手順が複雑な場合というのは、例えば次のような場合です。

<ul><li>インスタンスの生成が他のオブジェクトとの関連で成り立っている</li><li>外部リソースを読み取る処理によって作られるインスタンス</li><li>データベースからデータを読み取り、加工しながら作成されるインスタンス</li></ul>

[ target="_blank">Template Method](http://hamasyou.com/archives/000173)パターンで取り扱った、インスタンス生成手順を規則正しく決めたい場合なども、Builderパターンは応用できます。

手順に関して言うと、Template MethodパターンとBuilderパターンの違いは、<b>インスタンス生成の役割を誰が負うのか</b>というところです。Template Methodパターンは、インスタンス生成の手順をスーパークラス(親クラス)で決めます。一方Builderパターンは、生成の手順はDirectorクラス(他のクラス)が責任を負います。

Builderパターンで一番のポイントは、<strong>誰がインスタンスの生成手順を知っているのか</strong>というところです。Directorクラスが知っているというのがその答えです。

もうひとつのポイントは、<b>Directorを使うユーザは、Builderで作られたインスタンスが何かということを知っていなければならない</b>という点でしょう。どういうことかというと、まずクラス図を見てください。

<img src="http://hamasyou.com/images/builder/builder1.gif"/>

「User」クラスが「Director」クラスを呼び出して作成した「Product」を使用する点に注目です。「User」クラスは「Director」がどんな「Product」を生成するのかを知っていなければ使いようがないという点を表しています。

実装の点から見ると、DirectorをBuilder毎に作り成果物の取得で固有のProductを返すようにするか、成果物を抽象化しておき、取得時にキャストして使うかということです。

<h2>パターンの適用タイミング</h2>

Builderパターンの長所は、UserがProductの生成手順をすべてDirectorに任せているという部分です。これによって、<b>Productの生成手順が変わったり、どんなに複雑な処理をしていても、Userからしてみれば、変更する必要がない</b>のです。

設計時の注意点ですが、<b>Builderが提供する生成手順は、増やしたり減らしたりするのが困難である</b>ということを覚えて置いてください。オブジェクト指向の原則である<b>[ target="_blank" class="extlink">開放・閉鎖原則](http://www.alles.or.jp/~torutk/oojava/oo/develop/011.html)</b>では、拡張には開いていて(Open)、修正には閉じている(Closed)必要があります。ConcreteBuilderの拡張は、容易にできますが、親クラスのBuilderの機能を増やすことは、子クラス全体に修正が及ぶ可能性があります。

これを回避するためには、<b>十分な設計と変更に強い構造をBuilderに持たせることです。</b>

Builderパターンのまとめです。<strong>インスタンス生成に手順がある場合</strong>、<strong>外部リソースを使ってインスタンスを作成しなければいけない場合</strong>、<strong>コンストラクタで引数の違うものがたくさんできてしまった場合</strong>

<h2>実装サンプルと参考文献</h2>

Builderパターンの実装方法をもっと詳しく知りたい場合は、下記のサイトにアクセスするのをお勧めします。もしくは、参考書籍を載せておきますので、そちらをお買い求めください。(^^;

+ 日立ソフト(Builderパターン)
[ target="_blank" class="extlink">日立ソフト](http://www.dmz.hitachi-sk.co.jp/Java/Tech/pattern/gof/builder.html)

+ Skeleton of GOF's Design Pattern(JavaとC++のサンプルがあります)
[ target="_blank" class="extlink">Builderの骸骨](http://www002.upp.so-net.ne.jp/ys_oota/mdp/Builder/index.htm)

+ TECHSCORE(生成に関するパターン)
[ target="_blank" class="extlink">TECHSCORE Builderパターン](http://www.techscore.com/tech/DesignPattern/Builder.html)

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




