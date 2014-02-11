---
layout: post
title: "実践デザパタ-その11:Prototypeパターン"
date: 2004-06-13 13:33
comments: true
categories: [Engineer-Soul]
keywords: "デザインパターン,Prototype,プロトタイプ,GoF,Java"
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

11個目はインスタンスを新しく作るときに、既存のインスタンスのコピーを作成する<b>Prototype</b>パターンのメモです。


<!-- more -->

<h2>Prototypeパターン</h2>

<p class="option">Prototypeパターンは、既存のインスタンスのコピーを使って、生成の複雑なインスタンスや、少しだけ違うインスタンスをたくさん作りたいときに使えるパターンです。</p>

どんな時にこのパターンが使えるかというと、例えば株のシミュレーションプログラムを作っているとします。株価がこの先どのように変化するかを調べたいときに、現時点の株インスタンスの状態を複製して、別々の条件に当てはめたい場合、まったく同じ株インスタンスが必要となります。こんな状況にPrototypeパターンが使えます。

他には、ドローツール(お絵かきソフト)を作っているとすると、ユーザの書いた絵インスタンスは、とても複雑な状態になっていると考えられます。このインスタンスは、プログラムで同じものを作るのは、非常に大変です。(描いた手順をすべて覚えておいて、作るとなると時間もコストもかかる)こんなとき、絵インスタンスの複製を作ることができれば、描いた手順なんて必要なくなります。

3つ目は、テンプレートとなるインスタンスがすでに存在する場合です。テンプレートインスタンスを複製して、少し状態を変化させることで、同じような処理を何度も行わなくてすむようになります。

<h2>パターンの適用タイミング</h2>

Webアプリケーションなどを作っていると、[ class="extlink">ウィザード形式](http://yougo.ascii24.com/gh/10/001099.html)の処理を行う場合があります。入力して、確認して、登録するという一連の処理がある場合、登録時にエラーが発生してしまったら、入力時点まで、インスタンスの状態を戻さなければなりません。
こんなとき、インスタンスの複製を作っておいて、複製の方で登録処理を行い、エラーが出たら、複製元のインスタンスを処理するようにすれば複雑な処理は一切いらなくなります。

Prototypeパターンが使えそうな状況は、<strong>インスタンスを複製(コピー)しておいて、試しになにか処理してみる場合</strong>、<strong>複雑な生成過程を経たインスタンスをもう一度作りたい場合</strong>、<strong>テンプレートインスタンスが存在し、コピーすることで処理が楽になる場合</strong>

Prototypeパターンを使うときの注意点は、コピーの深さを間違えないことです。最近の言語は、オブジェクトの参照を扱います。コピーの深さを間違えると、見た目だけコピーされているが、実は中身は同じ参照だったということがありえます。詳しい解説は、[ class="extlink" target="_blank">Effective Java](http://www.amazon.co.jp/exec/obidos/ASIN/4894714361/sorehabooks-22)という本が参考になります。

Web上だとSun Microsystemsの[ class="extlink" target="_blank">JDC Tech Tips 日本語版](http://sdc.sun.co.jp/java/techtips/2001/index.html)がよさそうでした。(※このページは会員制です。無料ですので登録してから、読んでみてください。)

<h2>実装サンプルと参考文献</h2>

Prototypeパターンの実装方法をもっと詳しく知りたい場合は、下記のサイトにアクセスするのをお勧めします。もしくは、参考書籍を載せておきますので、そちらをお買い求めください。(^^;

+ 日立ソフト(Prototypeパターン)
[ target="_blank" class="extlink">日立ソフト](http://www.dmz.hitachi-sk.co.jp/Java/Tech/pattern/gof/prototype.html)

+ Skeleton of GOF's Design Pattern(JavaとC++のサンプルがあります)
[ target="_blank" class="extlink">Prototypeの骸骨](http://www002.upp.so-net.ne.jp/ys_oota/mdp/Prototype/index.htm)

+ TECHSCORE(生成に関するパターン)
[ target="_blank" class="extlink">TECHSCORE Prototypeパターン](http://www.techscore.com/tech/DesignPattern/Prototype.html)

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




