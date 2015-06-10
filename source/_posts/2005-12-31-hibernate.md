---
layout: post
title: "HIBERNATE覚書き"
date: 2005-12-31 08:11
comments: true
categories: [Blog]
keywords: "Hibernate,O/Rマッピング,インピーダンスミスマッチ,覚え書き"
tags: [Hibernate,O/Rマッピング]
author: hamasyou
amazon_url: ""
amazon_author: ""
amazon_image: ""
amazon_publisher: ""
---

<p>
<a href="http://www.amazon.co.jp/exec/obidos/ASIN/4797330805/sorehabooks-22/503-4739464-7671922?%5Fencoding=UTF8&camp=247&link%5Fcode=xm2" rel="external nofollow"></a>
</p>

Hibernate は O/R マッピングツールと呼ばれる、リレーショナルデータベースとオブジェクトモデルとの間を埋めるフレームワークです。

リレーショナルデータベースとオブジェクトモデルとのミスマッチには、下記のようなものがあります。

<ul><li>オブジェクトの粒度に関する問題</li><li>サブタイプ（継承）に関する問題</li><li>オブジェクトの同一性（識別子）に関する問題</li><li>オブジェクト同士の関連に関する問題</li><li>オブジェクトの検索に関する問題</li></ul>

Hibernateは、このようなミスマッチを解決する方法を提供します。

{% blockquote 『Hibernate イン アクション』はじめに より %}
リレーショナルシステムにおけるテーブル形式のデータ表現は、オブジェクト指向のJavaアプリケーションで使用されているオブジェクトのネットワークとは根本的に異なっている。この違いが、いわゆるオブジェクト/リレーショナルパラダイムのミスマッチだ。


{% endblockquote %}


<!-- more -->

<h2>Hibernateのアーキテクチャ</h2>

Hibernateが提供するインターフェースは、大きく分けて4つに分類できます。

<ul><li>CRUDとクエリ操作を行うためのインターフェース</li><li>Hibernateを設定するためのインターフェース</li><li>イベントをハンドリングするためのコールバックインターフェース</li><li>マッピング機能を拡張するためのインターフェース</li></ul>

<h3>CRUDとクエリ操作を行うためのインターフェース</h3>

<table>
<tr>
<th>インターフェース/クラス</th>
<th>説明</th>
</tr>
<tr>
<td>Session</td>
<td><ul><li>コネクションとトランザクションの概念を扱うもの</li><li>Sessionインスタンスの生成と破棄にはコストはかからない</li><li>通常、一度のリクエストにつきひとつのSessionを生成する</li><li><strong>スレッドセーフではない</strong></li></ul></td>
</tr>
<tr>
<td>SessionFactory</td>
<td><ul><li>Sessionインターフェースを返す</li><li>Sessionと比べ、生成にコストがかかる</li><li>通常、アプリケーション全体でひとつのインスタンスを使いまわす</li><li><strong>データベースひとつにつき、ひとつのSessionFactoryインスタンスを用意する。複数のデータベースを利用する際には、それだけのSessionFactoryインスタンスが必要</strong></li><li>マッピングファイルを保持する</li></ul></td>
</tr>
<tr>
<td>Transaction</td>
<td><ul><li>トランザクション実装を抽象化する</li></ul></td>
</tr>
<tr>
<td>Query</td>
<td><ul><li>データベースへのクエリ発行を行う</li><li>クエリはネイティブのSQLかHQLで記述される</li><li>生成したSession以外では使えない</li></ul></td>
</tr>
<tr>
<td>Criteria</td>
<td><ul><li>データベースへのクエリをオブジェクト表現を使って生成する</li></ul></td>
</tr>
</table>

<h3>Hibernateを設定するためのインターフェース</h3>

<table>
<tr>
<td>Configuration</td>
<td><ul><li>Hibernateのプロパティファイル(デフォルトはhibernate.cfg.xml)を読み込んで、SessionFactoryを生成する</li></ul></td>
</tr>
</table>

<h3>イベントをハンドリングするためのコールバックインターフェース</h3>

<table>
<tr>
<td>Lifecycle</td>
<td><ul><li>永続化オブジェクトが実装する</li><li>自身のライフサイクルイベントの通知を受け取ることができる</li><li>Hibernate特有のインターフェースのため、移植性が低下する</li></ul></td>
</tr>
<tr>
<td>Validation</td>
<td><ul><li>永続化オブジェクトが実装する</li><li>永続化オブジェクトのinsert時、update時に呼び出される</li><li>Hibernate特有のインターフェースのため、移植性が低下する</li></ul></td>
</tr>
<tr>
<td>Interceptor</td>
<td><ul><li>Hibernate特有のインターフェースだが、永続化オブジェクトには実装せずに、インタセプタ用のクラスに実装する</li><li><strong>イベントのコールバックを受け取るためには、通常はこのインターフェースを利用する</li></ul></td>
</tr>
</table>

<h3>マッピング機能を拡張するためのインターフェース</h3>

<table>
<tr>
<td>UserType</td>
<td><ul><li>ユーザ定義型を実装する場合に利用する</li></ul></td>
</tr>
<tr>
<td>CompositeUserType</td>
<td><ul><li>ユーザ定義型を実装する場合に利用する</li></ul></td>
</tr>
<tr>
<td>拡張ポイント</td>
<td><ul><li>主キーの生成時(IdentifierGenerator)</li><li>SQL方言(Dialect)</li><li>キャッシュ(Cache、CacheProvider)</li><li>JDBCコネクション管理(ConnectionProvider)</li><li>トランザクション管理(TransactionFactory、Transaction、TransactionManagerLookup)</li><li>O/Mマッピング(ClassPersister)</li><li>プロパティアクセス(PropertyAccessor)</li><li>プロキシ(ProxyFactory)</li></ul></td>
</tr>
</table>

<h2>バグ？</h2>

<h3>PostgreSQLでスキーマ指定すると...</h3>

<dl>
<dt>環境</dt>
<dd><ul><li>PostgreSQL8.0.0</li><li>Hibernate3.1</li></ul></dd>
</dl>

上記の環境で、PostgreSQLに<em>public以外</em>のスキーマでテーブルを作成したときちょっとハマったのでメモ。

<h4>現象</h4>

<ul><li>マッピングファイルにschemaを指定するだけでは、Hibernateで構築されるSQL文にスキーマが記述されない</li><li>PostgreSQL8.0でスキーマを明示した場合、PostgreSQLがテーブル名・カラム名を小文字に自動変換して解釈する</li></ul>

<h4>失敗時にHibernateが構築したSQL文</h4>

<pre>Hibernate: select max(MESSAGE_ID) from <em>MESSAGE</em>
Caused by: java.sql.SQLException: ERROR: <em>relation &quot;message&quot; does not exist</em></pre>

<h4>対策</h4>

<ul><li>hibernate.cfg.xmlにdefault_schemaを指定する</li><li>テーブル名・カラム名をダブルクウォート(&quot;&quot;)で囲む</li></ul>

Hibernateの設定ファイルにデフォルトスキーマを指定しなければいけない。また、スキーマ名を指定してSQLを実行すると(例：select * from hibernate.MESSAGE)、PostgreSQLがテーブル名・カラム名を小文字で解釈してしまう。回避するためには、ダブルクウォートで囲む必要がある。

<p class="option">Hibernateのマッピングファイルで、カラム名・テーブル名を「`（バッククウォート）」で囲むことで、SQL文生成時に Hibernate がダブルクウォートを自動で付けてくれます。</p>

Hibernate設定ファイルです。

<section>

<h4>hibernate.cfg.xml</h4>

<pre class="code"><code><span class="tag">&lt;hibernate-configuration&gt;</span> 
 <span class="tag">&lt;session-factory&gt;</span>     
  <span class="rem">&lt;!-- Database connection settings --&gt;</span> 
  <span class="tag">&lt;property <span class="attr">name=</span><span class="value">&quot;connection.driver_class&quot;</span>&gt;</span> 
   org.postgresql.Driver 
  <span class="tag">&lt;/property&gt;</span> 
  <span class="tag">&lt;property <span class="attr">name=</span><span class="value">&quot;connection.url&quot;</span>&gt;</span> 
   jdbc:postgresql://localhost:5432/hibernate_db 
  <span class="tag">&lt;/property&gt;</span> 
  <span class="tag">&lt;property <span class="attr">name=</span><span class="value">&quot;connection.username&quot;</span>&gt;</span>postgres<span class="tag">&lt;/property&gt;</span> 
  <span class="tag">&lt;property <span class="attr">name=</span><span class="value">&quot;connection.password&quot;</span>&gt;</span>postgres<span class="tag">&lt;/property&gt;</span> 
  <span class="tag">&lt;property <span class="attr">name=</span><span class="value">&quot;default_schema&quot;</span>&gt;</span>hibernate<span class="tag">&lt;/property&gt;</span> 
</code></pre>

</section>

マッピングファイルです。

<section>

<h4>マッピングファイル(*.hbm.xml)</h4>

<pre class="code"><code><span class="tag">&lt;hibernate-mapping&gt;</span> 
  <span class="tag">&lt;class <span class="attr">name=</span><span class="value">&quot;com.hamasyou.hibernate.hello.Message&quot;</span> 
    <span class="attr">schema=</span><span class="value">&quot;hibernate&quot;</span> 
    <span class="attr">table=</span><span class="value">&quot;&amp;quot;MESSAGE&amp;quot;&quot;</span>&gt;</span> 
    <span class="tag">&lt;id <span class="attr">name=</span><span class="value">&quot;id&quot;</span> <span class="attr">column=</span><span class="value">&quot;&amp;quot;MESSAGE_ID&amp;quot;&quot;</span>&gt;</span> 
      <span class="tag">&lt;generator <span class="attr">class=</span><span class="value">&quot;increment&quot;</span>/&gt;</span> 
    <span class="tag">&lt;/id&gt;</span> 
    <span class="tag">&lt;property <span class="attr">name=</span><span class="value">&quot;text&quot;</span> <span class="attr">column=</span><span class="value">&quot;&amp;quot;MESSAGE_TEXT&amp;quot;&quot;</span>/&gt;</span> 
    <span class="tag">&lt;many-to-one  
      <span class="attr">name=</span><span class="value">&quot;nextMessage&quot;</span>  
      <span class="attr">column=</span><span class="value">&quot;&amp;quot;NEXT_MESSAGE_ID&amp;quot;&quot;</span> 
      <span class="attr">cascade=</span><span class="value">&quot;all&quot;</span>/&gt;</span> 
    <span class="tag">&lt;/class&gt;</span> 
<span class="tag">&lt;/hibernate-mapping&gt;</span>
</code></pre>

</section>

&amp;quot;と書いてある部分を「`(バッククウォート）」に変更することで、Hibernate の機能で引用符を付けてくれるようになります。

Hibernateが生成したSQL文

<pre>Hibernate: select max(&quot;MESSAGE_ID&quot;) from hibernate.&quot;MESSAGE&quot;
Hibernate: insert into hibernate.&quot;MESSAGE&quot; (&quot;MESSAGE_TEXT&quot;, &quot;NEXT_MESSAGE_ID&quot;, &quot;MESSAGE_ID&quot;) values (?, ?, ?)</div>

<h2>参考</h2>

+ JSF、SpringFramework、Hibernateの3つを同時に学べる良書
<div class="rakuten"><table  width="400" border="0" cellpadding="5"><tr><td colspan="2"><a href="http://www.amazon.co.jp/exec/obidos/ASIN/4839917779/sorehabooks-22/ref=nosim/" rel="external nofollow">G-Tools</a></font></td></tr></table></div>

+ 薄いながらもよくまとまっています
<div class="rakuten"><table width="400"  border="0" cellpadding="5"><tr><td colspan="2"><a href="http://www.amazon.co.jp/exec/obidos/ASIN/487311215X/sorehabooks-22/ref=nosim/" rel="external nofollow">G-Tools</a></font></td></tr></table></div>

+ ProシリーズのHibernate本。おそらくかなりの良書だと思われます（読んでない^^;)
<div class="rakuten"><table width="400"  border="0" cellpadding="5"><tr><td colspan="2"><a href="http://www.amazon.co.jp/exec/obidos/ASIN/1590595114/sorehabooks-22/ref=nosim/" rel="external nofollow">G-Tools</a></font></td></tr></table></div>

+ ProfessionalシリーズのHibernate本。こちらもかなりの良書だと思われます
<div class="rakuten"><table  border="0" cellpadding="5"><tr><td colspan="2"><a href="http://www.amazon.co.jp/exec/obidos/ASIN/0764576771/sorehabooks-22/ref=nosim/" rel="external nofollow">G-Tools</a></font></td></tr></table></div>




