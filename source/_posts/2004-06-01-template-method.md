---
layout: post
title: "実践デザパタ-その4:Template Methodパターン"
date: 2004-06-01 17:10
comments: true
categories: [Blog]
keywords: "デザインパターン,Template Method,テンプレートメソッド,GoF,Java"
tags: [デザインパターン]
author: hamasyou
amazon_url: ""
amazon_author: ""
amazon_image: ""
amazon_publisher: ""
---

デザインパターンとは、システム設計におけるクラスやインターフェースの関係に名前をつけたものです。GoFの23のパターンが有名です。デザインパターンというのは、どんなパターンなのか、パターンの目的は何かということを覚えることが非常に重要なのですが、これを実際に適用しようとした場合に、いつ適用していいかが見えてこないとお話になりません。

そこで、自分の勉強も兼ねつつ、パターンの実践時における使用場所や、パターンを適用するきっかけを見つけられるようにメモしておきます。

今回は、処理の順番を規制するためにつかう、<b>Template Method</b>パターンです。


<!-- more -->

<h2>Template Methodパターン</h2>

<p class="option">Template Methodパターンは、処理の流れがほとんど同じなんだけど、一部ちょっとだけ違うときに、その部分を入れ替えて使えるパターンです。</p>

<img src="/images/template_method/template_method.gif" alt="Template   Method のクラス図" />

うーん、ちょっと説明がわかりにくそうですね。このパターンが使われている例としては、<a href="http://allabout.co.jp/career/swengineer/closeup/CU20030525A/" rel="external nofollow">フレームワーク</a>があります。

メール送信のフレームワークがあるとします。

<pre>1. 送信先の宛先を設定する
2. メールのタイトルを設定する
3. メールの本文を設定する
4. メールを送信する</pre>

と、このような流れでメールを送信するとしましょう。普通のメールを送るには、この流れに沿ってメールを作成して、送信すればいいのです。が、HTMLメールを送信したい場合、「3. メールの本文を設定する」でHTML用のメール本文を作成する必要があります。

メール送信をクラスに割り当てたときには、PlainTextMailクラスとHTMLMailクラスの二つができてしまうことになります。この2つのクラスは、メールを送信するという機能は非常に似ていて、違うところとしては「3. メール本文を設定する」だけだとします。

このようなときに、Template Methodパターンを適用して、親クラスにAbstractTemplateMailクラスみたいなのを作って、メール配信の一連の処理は実装しておき、サブクラスのPlainTextMailクラスとHTMLMailクラスでは、メールの本文を作成する部分だけオーバーライドするといった手法がとれます。これでしたら、同じ機能(宛先を設定したりタイトルを設定する部分）でも、一度だけしか実装していないことになり、保守性がアップします。

<b>オブジェクト指向プログラミングでは、同じことは2度書かない(コピー&ペーストしない)という原則があります。</b>同じ処理は共通にまとめて、違う部分だけ実装するのが、良いプログラミングです。

Template Methodパターンは、まさにこの、<strong>共通化されている部分は定義しておき、違う部分だけをサブクラスで実装する</strong>という優れたパターンです。

<h2>パターンの適用タイミング</h2>

このパターンは、あらかじめ共通化できる部分を抜き出しておく必要があります。先ほどの例だと、メール配信の部分というのは共通化できるということが、設計段階でわかっていなければなりません。つまり、このパターンを使えるタイミングは、<b>共通化を行いたい処理が出てきた場合</b>といえるでしょう。

もうひとつは、<b>サブクラスで必ず実行して欲しい処理がある場合</b>です。Template Methodパターンの実装の仕方によって、サブクラスでオーバーライドしなければならないメソッドを制御することができます。

設計における注意点として、このパターンは処理の流れを抽象化したものであるので、あまりに流れを大まかに区切りすぎたりすると、サブクラスで実装するメソッドの単位が大きくなってしまいます。注意してください。

<strong>共通部分と差分部分が明確に区別でき、処理に流れがある場合</strong>、<strong>サブクラスで実装して欲しい処理がある場合</strong>上記の場合に、Template Methodパターンの適用をおすすめします。

<h2>実装サンプルと参考文献</h2>

Template Methodパターンの実装方法をもっと詳しく知りたい場合は、下記のサイトにアクセスするのをお勧めします。もしくは、参考書籍を載せておきますので、そちらをお買い求めください。(^^;)

+ TECHSCORE(Template Methodパターン)
<a href="http://www.techscore.com/tech/DesignPattern/TemplateMethod.html" rel="external nofollow">TECHSCORE</a>

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




