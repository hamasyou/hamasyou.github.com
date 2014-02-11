---
layout: post
title: "Spring Framework 覚書き - JDBC抽象化"
date: 2004-11-04 13:35
comments: true
categories: [Blog]
keywords: "Spring,Framework,覚書き,スプリング,フレームワーク,アーキテクチャ,DI,IoC,JDBC,抽象化"
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

アメリカではほとんどデフェクトスタンダードとなっている「<a href="http://www.springframework.org/" rel="external nofollow"></a>」の覚書きです。Spring は簡単に言うと、<abbr title="Inversion of Control">IoC (制御の反転)</abbr>、またの名を <abbr title="Dependency Injection">DI (依存性注入)</abbr> という仕組みを取り入れた軽量コンテナです。

Springは JDBCを抽象化した層を持っています。JDBCを使ったプログラミングで、共通化できる部分や、煩雑な部分をすっきりとした統一的な方法でアクセスできる手段を提供してくれます。

Springの詳細については、ほかにもっとよいサイト(<a href="http://www.andore.com/money/trans/spring_ref_ja.html" rel="external nofollow">Springフレームワークの紹介</a>)があるので、そちらを参考にしてください。ここでは、Springを使っていて、ハマった点や気になった点などをメモしていこうと思います。随時更新していくつもりです。間違っている可能性が高いので、気になる点があればコメントをお願いします。

<section>

<h4>参考</h4>

<a href="http://wiki.bmedianode.com/Spring/?FrontPage" rel="external nofollow">Spring Pad - Wiki</a>

<a href="http://direct.idg.co.jp/detail_1.msp?id=1066&class=10005&n=2" rel="external nofollow">JavaWorld 7月号 2004年</a>

</section>


<!-- more -->

<h2>JDBC抽象化フレームワーク</h2>

<ul><li><a href="#JDBC抽象化層とは？" rel="external nofollow">JDBC抽象化層とは？</a></li>
<li><a href="#JDBC抽象化層で使われる主な クラス / インターフェース" rel="external nofollow">JDBC抽象化層で使われる主な クラス / インターフェース</a></li>
<li><a href="#JdbcTemplateを使った基本的なSQL文の実行" rel="external nofollow">JdbcTemplateを使った基本的なSQL文の実行</a></li>
<li><a href="#DataSource の設定の仕方" rel="external nofollow">DataSource の設定の仕方</a></li>
<li><a href="#SqlFunction クラスを使った単一問い合わせのサンプル" rel="external nofollow">SqlFunction クラスを使った単一問い合わせのサンプル</a></li>
</ul>

<h2 id="JDBC抽象化層とは？">JDBC抽象化層とは？</h2>

Springは、JDBCをラップした抽象化層を提供しています。これは、JDBCを使ってプログラミングを行う際に必ず必要となる、Connectionのオープン / クローズや Statement、ResultSet の生成と破棄をテンプレート化して提供するものであると考えてよいと思います。

もう一つは、<em>SQLException を使わないで永続化処理が書ける</em>というものです。通常、データベースへの処理は SQLException を明示的に処理しなければなりません。しかし、実際のアプリケーションでSQLExceptionをキャッチして処理するということはあまりありません。なぜなら、ほとんどのSQLExceptionの原因はアプリケーション内からでは復帰が難しいものだからです。

JDBC抽象化層は、SQLException よりももっと抽象化した<em>実行時例外である org.springframework.dao.DataAccessException</em>を扱います。実行時例外であるため、開発者は無意味な SQLException をわざわざキャッチしなくてすむようになります。アプリケーション側で復帰できそうな例外に関してはキャッチして処理することも可能です。

JDBC抽象化層では、テーブルに対する検索(SELECT)や更新(INSERT / UPDATE / DELETE)を一つのオブジェクトとして定義する方法が提供されています。一つのSQL文に対して、一つのクラスを対応させる感じです。クラス数が非常に多くなっちゃいそうですね・・・。

<h2 id="JDBC抽象化層で使われる主な クラス / インターフェース">JDBC抽象化層で使われる主な クラス / インターフェース</h2>

JDBC抽象化層で使われる主なクラスとインターフェースです。

<h3>org.springframework.dao.DataAccessException</h3>

JDBC抽象化層で使われるトップレベルのデータアクセス実行時例外クラス。開発者は必要であれば、このクラスのサブクラスを適切に処理することができます。

<h3>org.springframework.jdbc.datasource.DriverManagerDataSource</h3>

Beanのプロパティを使ってJDBCドライバ構成を組み立てます。このクラスは、<strong>常に新しいコネクションオブジェクトを返します</strong>。既知のサブクラスに SingleConnectionDataSource クラスがあります。

このクラスは、常に同一のコネクションを使いまわします。マルチスレッド環境では使えません。

<section>

<h4>Bean定義ファイルで宣言するときに設定する属性</h4>

<dl><dt>driverClassName</dt><dd>JDBCドライバの完全クラス名です。</dd>
<dt>url</dt><dd>データベースへのURLです。</dd>
<dt>username</dt><dd>データベースへつなぐユーザ名</dd>
<dt>password</dt><dd>データベースへつなぐパスワード</dd>
</dl>

</section>

接続設定を直接書くのではなくて、設定ファイルに定義しておいて、その値を読み込むといったことも出来ます。詳しくは 『<a href="http://www.andore.com/money/trans/spring_ref_p4_ja.html#doc1_3.6.2" rel="external nofollow">Spring-Java/J2EE アプリケーションフレームワークドキュメント (3.6.2 PropertyPlaceholderConfigurer)</a>』をどうぞ。

<h3>org.springframework.jdbc.object.MappingSqlQuery</h3>

テーブルに関する検索処理を行う、再利用可能な問い合わせクラスです。サブクラスでは、mapRow メソッドによって、 ResultSet を オブジェクトに変換する処理が書けます。オブジェクト変換時に、SQL文に設定したパラメータ(PreparedStatementのパラメータ)が必要な場合には、org.springframework.jdbc.object.MappingSqlQueryWithParameters を使用します。

SqlQuery#findObject メソッドは、ユニークなオブジェクトを返すメソッドとなります。そのため、SQL文の結果がたった一つになるようにしなければなりません。主キーを指定するSQL文を作るのが一般的な使い方でしょう。

mapRow メソッドは、実行したSQL文の結果セットが存在する場合(レコードがある場合)のみ呼び出されます。

<dl>
<dt>findObject メソッド</dt><dd>SELECT文を実行します。実行結果が一つだけ存在する場合に使います。</dd>
<dt>execute メソッド</dt><dd>SELECT文を実行します。実行結果が複数ある場合に使います。</dd>
</dl>

<h3>org.springframework.jdbc.object.SqlUpdate</h3>

SQLの更新を表すクラスです。MappingSqlQueryと同様に再利用な可能(PreparedStatement式)なオブジェクトになります。update / execute メソッドを呼び出すことで、更新処理(INSERT, UPDATE, DELETE)が実行されます。

<dl><dt>update メソッド</dt><dd>INSERT文、UPDATE文、DELETE文を実行します。</dd></dl>

<h3>org.springframework.jdbc.core.JdbcTemplate</h3>

JDBC抽象化層のコアクラスです。通常のSQLクエリを抽象化します。RowCallbackHandler を使って、ResultSet をオブジェクトに変換する処理が書けます(<a href="#JdbcTemplateを使った基本的なSQL文の実行" rel="external nofollow">サンプルコード</a>参照)。

<dl>
<dt>query メソッド</dt>
<dd><p>問い合わせSQL文を発行します。引数に現れる <code>ResultSetExtractor</code>、<code>RowCallbackHandler</code>、<code>RowMapper</code> はいづれも ResultSet に何らかの処理を加えられるものです。通常、オブジェクトへの変換を行います。</p></dd>
<dt>execute メソッド</dt>
<dd><p>あらゆる種類のSQL文を実行します。引数にとるコールバックインターフェースは、execute メソッドが呼ばれたときに呼び出されます。</p></dd>
<dt>update メソッド</dt>
<dd><p>更新系(INSERT, UPDATE, DELETE) のSQL文を発行します。</p></dd>
</dl>

<h3>org.springframework.jdbc.core.support.JdbcDaoSupport</h3>

このクラスは、サブクラスに DataSource を提供できるようにしたクラスです。Bean定義書で DataSource の設定を書いておくと、IoCコンテナが自動的に DataSource を生成してセットしておいてくれます。サブクラスで JdbcTemplate を利用することが期待されているみたいです。

<h3>org.springframework.jdbc.object.SqlFunction</h3>

単一の結果を返す、SQL問い合わせのラッパークラスです。例えば、テーブルレコードのカウントをとりたい場合などに利用できます。サンプルコードは後述。基本的には SqlFunction の返す結果は int 型になるようです。他の方が使いたい場合には、runGeneric() メソッドを呼び出せばいいです。

<h2 id="JdbcTemplateを使った基本的なSQL文の実行">JdbcTemplateを使った基本的なSQL文の実行</h2>

JdbcTemplate クラスを使ったSQL文の実行方法です。ここでは、RowCallbackHandler クラスを使って、検索結果をオブジェクトに変換する処理までやっています。更新系の処理なら、JdbcTemplate オブジェクトを作成して、execute メソッドの引数にSQL文を渡してやるだけで実行されるようです。

<section>

<h4>JdbcTemplateの例</h4>

<pre class="code"><code><span class="keyword">public</span> List findPersons() <span class="keyword">throws</span> DataAccessException {
  JdbcTemplate template = <span class="keyword">new</span> JdbcTemplate(getDataSource());
  <span class="keyword">final</span> List result = <span class="keyword">new</span> ArrayList();
  template.query(<span class="literal">"select ID, NAME, AGE from PERSON"</span>, 
                  <span class="keyword">new</span> RowCallbackHandler() {
    <span class="keyword">public</span> <span class="keyword">void</span> processRow(ResultSet rs) <span class="keyword">throws</span> SQLException {
      Person p = <span class="keyword">new</span> Person(rs.getBigDecimal(1));
      p.setName(rs.getString(2));
      p.setAge(rs.getBigDecimal(3));
      result.add(p);
    }
  });
  <span class="keyword">return</span> result;
}
 
<span class="keyword">public</span> List findPersonsAsMap() {
  <span class="comment">/*
   * List の要素は Map オブジェクト
   * key が 列名 で value が値
   * [ {ID=1, NAME=Taro, AGE=22}, {ID=2, NAME=Hanako,AGE=25} ]
   */</span>
  JdbcTemplate jt = <span class="keyword">new</span> JdbcTemplate(getDataSource());
  List result = jt.queryForList(<span class="literal">"select * from PERSON"</span>);
  <span class="keyword">return</span> result;
}
 
<span class="keyword">public</span> <span class="keyword">void</span> addPerson(BigDecimal id, String name, BigDecimal age) {
  JdbcTemplate jt = <span class="keyword">new</span> JdbcTemplate(getDataSource());
  jt.update(<span class="literal">"insert into PERSON (ID, NAME, AGE) "</span> +
            <span class="literal">"  values (?, ?, ?) "</span>,
            <span class="keyword">new</span> Object[] {id, name, age});
}
</code></pre>

</section>

<h2 id="DataSource の設定の仕方">DataSource の設定の仕方</h2>

JdbcDaoSupport クラスには getDataSource() というデータ接続を返すメソッドが用意されています。DataSource の設定は、Bean定義に例えば次のように書きます。

<section>

<h4>DataSourceのBean定義例</h4>

<pre class="code"><code><span class="tag">&lt;bean <span class="attr">id=</span><span class="value">&quot;dataSource&quot;</span> 
    <span class="attr">class=</span><span class="value">&quot;org.springframework.jdbc.datasource.DriverManagerDataSource&quot;</span>&gt;</span>
 
  <span class="tag">&lt;property <span class="attr">name=</span><span class="value">&quot;driverClassName&quot;</span>&gt;</span>
    <span class="tag">&lt;value&gt;</span>org.hsqldb.jdbcDriver<span class="tag">&lt;/value&gt;</span>
  <span class="tag">&lt;/property&gt;</span>
 
  <span class="tag">&lt;property <span class="attr">name=</span><span class="value">&quot;url&quot;</span>&gt;</span>
    <span class="tag">&lt;value&gt;</span>jdbc:hsqldb:hsql://localhost<span class="tag">&lt;/value&gt;</span>
  <span class="tag">&lt;/property&gt;</span>
 
  <span class="tag">&lt;property <span class="attr">name=</span><span class="value">&quot;username&quot;</span>&gt;</span>
    <span class="tag">&lt;value&gt;</span>sa<span class="tag">&lt;/value&gt;</span>
  <span class="tag">&lt;/property&gt;</span>
 
  <span class="tag">&lt;property <span class="attr">name=</span><span class="value">&quot;password&quot;</span>&gt;</span>
    <span class="tag">&lt;value&gt;</span><span class="tag">&lt;/value&gt;</span>
  <span class="tag">&lt;/property&gt;</span>    
<span class="tag">&lt;/bean&gt;</span>
</code></pre>

</section>

<h2 id="SqlFunction クラスを使った単一問い合わせのサンプル">SqlFunction クラスを使った単一問い合わせのサンプル</h2>

SqlFunction クラスを使うと、単一の問い合わせを表現することができます。通常、単一問い合わせの結果は int 型になるみたいです。複数件の結果が返ってきたり、オブジェクトに変換したりする処理が発生する場合には、 JdbcTemplate の例のような方法を使います。

<section>

<h4>テーブルのレコード数を返す例</h4>

<pre class="code"><code><span class="keyword">public</span> <span class="keyword">int</span> countRows() {
  SqlFunction sf = 
    <span class="keyword">new</span> SqlFunction(dataSource, <span class="literal">"select count(*) from mytable"</span>);
  sf.compile();
  <span class="keyword">return</span> sf.run();
}
</code></pre>

</section>

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




