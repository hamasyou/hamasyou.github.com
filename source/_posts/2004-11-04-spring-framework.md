---
layout: post
title: "Spring Framework 覚書き - トランザクション管理"
date: 2004-11-04 13:38
comments: true
categories: [Blog]
keywords: "Spring,Framework,覚書き,スプリング,フレームワーク,アーキテクチャ,DI,IoC,トランザクション管理,JTA"
tags: [Spring]
author: hamasyou
amazon_url: ""
amazon_author: ""
amazon_image: ""
amazon_publisher: ""
---

<p>
<a href="http://www.amazon.co.jp/exec/obidos/ASIN/0764558315/sorehabooks-22" rel="external nofollow"></a>
</p>

アメリカではほとんどデフェクトスタンダードとなっている「<a href="http://www.springframework.org/" rel="external nofollow"></a>」の覚書きです。Spring は簡単に言うと、<abbr title="Inversion of Control" >IoC (制御の反転)</abbr>、またの名を <abbr title="Dependency Injection">DI (依存性注入)</abbr> という仕組みを取り入れた軽量コンテナです。

Springはとても抽象化した方法で、トランザクション管理を行います。抽象化というのは実装に依存していないという意味です。アプリケーションサーバが持つトランザクション機能や、 JDBCのようなリソースを使った DataSource に関しても、共通の方法でトランザクション管理を提供しています。

Springの詳細については、ほかにもっとよいサイト(<a href="http://www.andore.com/money/trans/spring_ref_ja.html" rel="external nofollow">Springフレームワークの紹介</a>)があるので、そちらを参考にしてください。ここでは、Springを使っていて、ハマった点や気になった点などをメモしていこうと思います。随時更新していくつもりです。間違っている可能性が高いので、気になる点があればコメントをお願いします。

<section>

<h4>参考</h4>

<a href="http://wiki.bmedianode.com/Spring/?FrontPage" rel="external nofollow">Spring Pad - Wiki</a>

<a href="http://direct.idg.co.jp/detail_1.msp?id=1066&class=10005&n=2" rel="external nofollow">JavaWorld 7月号 2004年</a>

</section>


<!-- more -->

<h2>トランザクション管理</h2>

<ul><li><a href="#トランザクション管理機能" rel="external nofollow">トランザクション管理機能</a></li></ul>

<h2 id="トランザクション管理機能">トランザクション管理機能</h2>

Springのトランザクション機能は、トランザクションマネジャというものが管理します。マネジャの設定もBean定義書で行います。Springのトランザクションマネジャは、<abbr title="Java Transaction API">JTA</abbr> をサポートしていない DataSource に対しても宣言的なトランザクションをかけることが出来る。

宣言的なトランザクションとは、トランザクション境界(トランザクションの開始と終了)を宣言して、<abbr title="Plain Old Java Object">POJO</abbr> のメソッドにトランザクション処理を行うことが出来るものです。例外が発生した場合に自動的にロールバックを行わせるものだと考えてください。

<section>

<h4>[参考]</h4>
<a href="http://wiki.bmedianode.com/Spring/?TransactionProxyFactoryBean" rel="external nofollow">Spring Pad - TransactionProxyFactoryBean</a>

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
<a href="http://www.springframework.org/" rel="external nofollow">Spring Framework</a><img src="/images/linkext.gif" alt="linkext" />

+ Spring Framework の 日本語 Wiki です。大量の情報があります。
<a href="http://wiki.bmedianode.com/Spring/?FrontPage" rel="external nofollow">Spring Pad</a><img src="/images/linkext.gif" alt="linkext" />

+ Spring-Java/J2EEアプリケーションフレームワークリファレンスドキュメント (日本語訳)
<a href="http://www.andore.com/money/trans/spring_ref_ja.html" rel="external nofollow">Spring-Java/J2EEアプリケーションフレームワークドキュメント</a><img src="/images/linkext.gif" alt="linkext" />

+ Spring フレームワークに関しての概要です。TECHSCORE さんの記事は読みやすいなぁ (^^ ;
<a href="http://www.techscore.com/tech/Java/Spring/1.html" rel="external nofollow">TECHSCORE - Spring Framework</a><img src="/images/linkext.gif" alt="linkext" />

+ Spring を含めた、軽量コンポーネントのお話です。
<div class="rakuten"><table width="400" border="0" cellpadding="5"><tr><td colspan="2"><a href="http://www.amazon.co.jp/exec/obidos/ASIN/487311201X/sorehabooks-22/" rel="external nofollow">G-Tools</a></font><br /></td></tr></table></div>

+ Spring の ロッドジョンソンが贈る、J2EE技術者のためのバイブル
<div class="rakuten"><table width="400" border="0" cellpadding="5"><tr><td colspan="2"><a href="http://www.amazon.co.jp/exec/obidos/ASIN/4797322888/sorehabooks-22/" rel="external nofollow">G-Tools</a></font><br /></td></tr></table></div>

+ Spring のロッドジョンソンによる Spring ユーザのための本 (洋書)
<div class="rakuten"><table width="400" border="0" cellpadding="5"><tr><td colspan="2"><a href="http://www.amazon.co.jp/exec/obidos/ASIN/0764574833/sorehabooks-22/" rel="external nofollow">G-Tools</a></font><br /></td></tr></table></div>

+ SpringでWebアプリケーションを作りながら、Springの全体像がわかりやすく解説されています。
<div class="rakuten"><table width=400 border="0" cellpadding="5"><tr><td colspan="2"><a href="http://www.amazon.co.jp/exec/obidos/ASIN/4774122793/sorehabooks-22/" rel="external nofollow">G-Tools</a></font><br /></td></tr></table></div>







