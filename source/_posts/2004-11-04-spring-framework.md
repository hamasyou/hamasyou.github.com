---
layout: post
title: "Spring Framework 覚書き - トランザクション管理"
date: 2004-11-04 13:38
comments: true
categories: [Engineer-Soul]
keywords: "Spring,Framework,覚書き,スプリング,フレームワーク,アーキテクチャ,DI,IoC,トランザクション管理,JTA"
tags: []
author: hamasyou
amazon_url: ""
amazon_author: ""
amazon_image: ""
amazon_publisher: ""
---

<p>
[ target="_blank"><img src="http://images-jp.amazon.com/images/P/0764558315.01.MZZZZZZZ.jpg"  border="0" />](http://www.amazon.co.jp/exec/obidos/ASIN/0764558315/sorehabooks-22)
</p>

アメリカではほとんどデフェクトスタンダードとなっている「[ target="_blank" class="extlink"><b>Spring Framework</b>](http://www.springframework.org/)」の覚書きです。Spring は簡単に言うと、<abbr title="Inversion of Control" >IoC (制御の反転)</abbr>、またの名を <abbr title="Dependency Injection">DI (依存性注入)</abbr> という仕組みを取り入れた軽量コンテナです。

Springはとても抽象化した方法で、トランザクション管理を行います。抽象化というのは実装に依存していないという意味です。アプリケーションサーバが持つトランザクション機能や、 JDBCのようなリソースを使った DataSource に関しても、共通の方法でトランザクション管理を提供しています。

Springの詳細については、ほかにもっとよいサイト([ target="_blank" class="extlink">Spring-Java/J2EEアプリケーションフレームワークリファレンスドキュメント](http://www.andore.com/money/trans/spring_ref_ja.html)や[ target="_blank" class="extlink">Springフレームワークの紹介](http://www.andore.com/money/trans/spring_ja.html))があるので、そちらを参考にしてください。ここでは、Springを使っていて、ハマった点や気になった点などをメモしていこうと思います。随時更新していくつもりです。間違っている可能性が高いので、気になる点があればコメントをお願いします。

<section>

<h4>参考</h4>

[ target="_blank" class="extlink">Spring Pad - Wiki](http://wiki.bmedianode.com/Spring/?FrontPage)

[ target="_blank" class="extlink">JavaWorld 7月号 2004年](http://direct.idg.co.jp/detail_1.msp?id=1066&class=10005&n=2)

</section>


<!-- more -->

<h2>トランザクション管理</h2>

<ul><li>[トランザクション管理機能](#トランザクション管理機能)</li></ul>

<h2 id="トランザクション管理機能">トランザクション管理機能</h2>

Springのトランザクション機能は、トランザクションマネジャというものが管理します。マネジャの設定もBean定義書で行います。Springのトランザクションマネジャは、<abbr title="Java Transaction API">JTA</abbr> をサポートしていない DataSource に対しても宣言的なトランザクションをかけることが出来る。

宣言的なトランザクションとは、トランザクション境界(トランザクションの開始と終了)を宣言して、<abbr title="Plain Old Java Object">POJO</abbr> のメソッドにトランザクション処理を行うことが出来るものです。例外が発生した場合に自動的にロールバックを行わせるものだと考えてください。

<section>

<h4>[参考]</h4>
[ target="_blank" class="extlink">Spring Pad - TransactionProxyFactoryBean](http://wiki.bmedianode.com/Spring/?TransactionProxyFactoryBean)

<h3>org.springframework.transaction.PlatformTransactionManager</h3>

Springのトランザクションマネジャの基本インターフェースのようです。

<dl><dt>グローバルトランザクション</dt>
<dd>リソースを使わないトランザクション。JTAを使ってアプリケーションサーバが処理します。</dd>
<dt>ローカルトランザクション</dt>
<dd>JDBCなどのリソースを使用したトランザクション処理。</dd>
</dl>

Springは、グローバルトランザクションとローカルトランザクションを透過的に扱うためにこのインターフェースを用意しています。

<h3>org.springframework.transaction.TransactionDefinition</h3>

<section>

<h4>トランザクション設定を定義するインターフェース</h4>

<dl><dt>PROPAGATION_REQUIRED</dt>
<dd>トランザクションを実行する。</dd>
<dt>PROPAGATION_REQUIRES_NEW</dt>
<dd>メソッド呼出し毎に新しいトランザクションを実行する。</dd>
<dt>PROPAGATION_MANDATORY</dt>
<dd>トランザクションを開始している必要がある。</dd>
<dt>PROPAGATION_NOT_SUPPORTED</dt>
<dd>トランザクションの開始に関わらず、メソッド呼び出しはトランザクションに含まれない。</dd>
<dt>PROPAGATION_SUPPORTS</dt>
<dd>トランザクションが開始されていれば、メソッドが実行される。開始されていなければ実行されない。</dd>
<dt>PROPAGATION_NEVER </dt>
<dd>トランザクションに参加しない。</dd>
</dl>

</section>

コミット対象にする例外を追加するには、トランザクション属性に 「+《例外の型》」 をつける。ロールバック対象の例外を追加するには 「-《例外の型》」 をつける。

<pre>PROPAGATION_REQUIRED, +java.io.IOException</pre>

<h3>org.springframework.jdbc.datasource.DataSourceTransactionManager</h3>

一つのDataSourceのためのトランザクション実装です。DataSourceごとに一つのスレッドが対応します。JDBCのConnectionを利用してトランザクションを制御します。

<h2>参考</h2>

+ Spring Framework の本家です。
[ target="_blank" class="extlink">Spring Framework](http://www.springframework.org/)<img src="/images/linkext.gif" alt="linkext" />

+ Spring Framework の 日本語 Wiki です。大量の情報があります。
[ target="_blank" class="extlink">Spring Pad](http://wiki.bmedianode.com/Spring/?FrontPage)<img src="/images/linkext.gif" alt="linkext" />

+ Spring-Java/J2EEアプリケーションフレームワークリファレンスドキュメント (日本語訳)
[ target="_blank" class="extlink">Spring-Java/J2EEアプリケーションフレームワークドキュメント](http://www.andore.com/money/trans/spring_ref_ja.html)<img src="/images/linkext.gif" alt="linkext" />

+ Spring フレームワークに関しての概要です。TECHSCORE さんの記事は読みやすいなぁ (^^ ;
[ target="_blank" class="extlink">TECHSCORE - Spring Framework](http://www.techscore.com/tech/Java/Spring/1.html)<img src="/images/linkext.gif" alt="linkext" />

+ Spring を含めた、軽量コンポーネントのお話です。
<div class="rakuten"><table width="400" border="0" cellpadding="5"><tr><td colspan="2">[軽快なJava―Better,Faster,Lighter Java](http://www.amazon.co.jp/exec/obidos/ASIN/487311201X/sorehabooks-22/)</td></tr><tr><td valign="top">[<img src="http://images-jp.amazon.com/images/P/487311201X.09.MZZZZZZZ.jpg"   border="0" />](http://www.amazon.co.jp/exec/obidos/ASIN/487311201X/sorehabooks-22/)</td><td valign="top"><font size="-1">ブルース・A. テイトジャスティン ゲットランドBruce A. TateJustin Gehtland岩谷 宏<br /><br /><iframe scrolling="no" frameborder="0" width="200" height="40" hspace="0" vspace="0" marginheight="0" marginwidth="0" src="http://webservices.amazon.co.jp/onca/xml?Service=AWSProductData&SubscriptionId=0G91FPYVW6ZGWBH4Y9G2&AssociateTag=goodpic-22&Operation=ItemLookup&IdType=ASIN&ContentType=text/html&Page=1&ResponseGroup=Offers&ItemId=487311201X&Version=2004-10-04&Style=http://www.g-tools.net/xsl/priceFFFFFF.xsl"></iframe><br /><b>おすすめ平均</b><img src="http://g-images.amazon.com/images/G/01/detail/stars-5-0.gif"   /><br /><img src="http://g-images.amazon.com/images/G/01/detail/stars-5-0.gif"   />率直な筆者の経験は必読<br /><img src="http://g-images.amazon.com/images/G/01/detail/stars-5-0.gif"   />シンプル<br /><br />[Amazonで詳しく見る](http://www.amazon.co.jp/exec/obidos/ASIN/487311201X/sorehabooks-22/)</font><img src="http://www.goodpic.com/mt/images/spacer.gif"   width="30" height="1" /><font size="-2">by [G-Tools](http://www.goodpic.com/mt/aws/)</font><br /></td></tr></table></div>

+ Spring の ロッドジョンソンが贈る、J2EE技術者のためのバイブル
<div class="rakuten"><table width="400" border="0" cellpadding="5"><tr><td colspan="2">[実践J2EE システムデザイン&業務運用[仮題・予定価格]](http://www.amazon.co.jp/exec/obidos/ASIN/4797322888/sorehabooks-22/)</td></tr><tr><td valign="top">[<img src="http://images-jp.amazon.com/images/P/4797322888.09.MZZZZZZZ.jpg"   border="0" />](http://www.amazon.co.jp/exec/obidos/ASIN/4797322888/sorehabooks-22/)</td><td valign="top"><font size="-1">ロッド・ジョンソン<br /><br /><iframe scrolling="no" frameborder="0" width="200" height="40" hspace="0" vspace="0" marginheight="0" marginwidth="0" src="http://webservices.amazon.co.jp/onca/xml?Service=AWSProductData&SubscriptionId=0G91FPYVW6ZGWBH4Y9G2&AssociateTag=goodpic-22&Operation=ItemLookup&IdType=ASIN&ContentType=text/html&Page=1&ResponseGroup=Offers&ItemId=4797322888&Version=2004-10-04&Style=http://www.g-tools.net/xsl/priceFFFFFF.xsl"></iframe><br /><b>おすすめ平均</b><img src="http://g-images.amazon.com/images/G/01/detail/stars-4-5.gif"   /><br /><img src="http://g-images.amazon.com/images/G/01/detail/stars-5-0.gif"   />Spring Freamworkの作者に迫れる唯一の本<br /><img src="http://g-images.amazon.com/images/G/01/detail/stars-3-0.gif"   />坊主にくけりゃ袈裟までにくい?<br /><img src="http://g-images.amazon.com/images/G/01/detail/stars-4-0.gif"   />内容は充実、ただ経験、印象に頼るところも<br /><img src="http://g-images.amazon.com/images/G/01/detail/stars-5-0.gif"   />まさに実践まさに必携<br /><img src="http://g-images.amazon.com/images/G/01/detail/stars-5-0.gif"   />「現場主義」といったスタンスが根底に貫かれている<br /><br />[Amazonで詳しく見る](http://www.amazon.co.jp/exec/obidos/ASIN/4797322888/sorehabooks-22/)</font><img src="http://www.goodpic.com/mt/images/spacer.gif"   width="30" height="1" /><font size="-2">by [G-Tools](http://www.goodpic.com/mt/aws/)</font><br /></td></tr></table></div>

+ Spring のロッドジョンソンによる Spring ユーザのための本 (洋書)
<div class="rakuten"><table width="400" border="0" cellpadding="5"><tr><td colspan="2">[Professional Java Development With The Spring Framework](http://www.amazon.co.jp/exec/obidos/ASIN/0764574833/sorehabooks-22/)</td></tr><tr><td valign="top">[<img src="http://images-jp.amazon.com/images/P/0764574833.01.MZZZZZZZ.jpg"   border="0" />](http://www.amazon.co.jp/exec/obidos/ASIN/0764574833/sorehabooks-22/)</td><td valign="top"><font size="-1">Rod JohnsonJuergen HoellerALEF ARENDSENDMITRIY KOPYLENKOTHOMAS RISBERG<br /><br /><iframe scrolling="no" frameborder="0" width="200" height="40" hspace="0" vspace="0" marginheight="0" marginwidth="0" src="http://webservices.amazon.co.jp/onca/xml?Service=AWSECommerceService&SubscriptionId=0G91FPYVW6ZGWBH4Y9G2&AssociateTag=goodpic-22&Operation=ItemLookup&IdType=ASIN&ContentType=text/html&Page=1&ResponseGroup=Offers&ItemId=0764574833&Version=2004-10-04&Style=http://www.g-tools.net/xsl/priceFFFFFF.xsl"></iframe><br />[Amazonで詳しく見る](http://www.amazon.co.jp/exec/obidos/ASIN/0764574833/sorehabooks-22/)</font><img src="http://www.goodpic.com/mt/images/spacer.gif"   width="30" height="1" /><font size="-2">by [G-Tools](http://www.goodpic.com/mt/aws/)</font><br /></td></tr></table></div>

+ SpringでWebアプリケーションを作りながら、Springの全体像がわかりやすく解説されています。
<div class="rakuten"><table width=400 border="0" cellpadding="5"><tr><td colspan="2">[ target="_blank">Java press (Vol.41)](http://www.amazon.co.jp/exec/obidos/ASIN/4774122793/sorehabooks-22/)</td></tr><tr><td valign="top">[ target="_blank"><img src="http://images-jp.amazon.com/images/P/4774122793.09.MZZZZZZZ.jpg"   border="0" />](http://www.amazon.co.jp/exec/obidos/ASIN/4774122793/sorehabooks-22/)</td><td valign="top"><font size="-1"><br /><br />[ target="_blank">Amazonで詳しく見る](http://www.amazon.co.jp/exec/obidos/ASIN/4774122793/sorehabooks-22/)</font>    <font size="-2">by [ >G-Tools](http://www.goodpic.com/mt/aws/)</font><br /></td></tr></table></div>







