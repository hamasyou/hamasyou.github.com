---
layout: post
title: "Spring Framework 覚書き - MVCフレームワーク"
date: 2004-11-06 14:00
comments: true
categories: [Engineer-Soul]
keywords: "Spring,Framework,覚書き,スプリング,フレームワーク,アーキテクチャ,DI,IoC,MVC,Web"
tags: []
author: hamasyou
amazon_url: ""
amazon_author: ""
amazon_image: ""
amazon_publisher: ""
---

<p>
[ target="_blank"><img src="http://images-jp.amazon.com/images/P/0764558315.01.MZZZZZZZ.jpg" border="0" />](http://www.amazon.co.jp/exec/obidos/ASIN/0764558315/sorehabooks-22)
</p>

アメリカではほとんどデフェクトスタンダードとなっている「[ target="_blank" class="extlink"><b>Spring Framework</b>](http://www.springframework.org/)」の覚書きです。Spring は簡単に言うと、<abbr title="Inversion of Control">IoC (制御の反転)</abbr>、またの名を <abbr title="Dependency Injection">DI (依存性注入)</abbr> という仕組みを取り入れた軽量コンテナです。

Springは MVCフレームワークを提供しています。Springのフレームワークは、すべてインターフェースベースになっているので、単一継承の JAVA にとっては非常にありがたいものです。Spring MVC の View には、JSP の他、Velocity、XSLT、JSFなどといったさまざまな技術が使えます。

Validation 機能は、Web システムにとって非常に重要なものとなっています。Springでは、 Validation は Web パッケージと切り離されているので、単体テストも簡単に出来るようになっています。

Springの詳細については、ほかにもっとよいサイト([ target="_blank" class="extlink">Spring-Java/J2EEアプリケーションフレームワークリファレンスドキュメント](http://www.andore.com/money/trans/spring_ref_ja.html)や[ target="_blank" class="extlink">Springフレームワークの紹介](http://www.andore.com/money/trans/spring_ja.html))があるので、そちらを参考にしてください。ここでは、Springを使っていて、ハマった点や気になった点などをメモしていこうと思います。随時更新していくつもりです。間違っている可能性が高いので、気になる点があればコメントをお願いします。

<section>

<h4>参考</h4>

[ target="_blank" class="extlink">Spring Pad - Wiki](http://wiki.bmedianode.com/Spring/?FrontPage)

[ target="_blank" class="extlink">JavaWorld 7月号 2004年](http://direct.idg.co.jp/detail_1.msp?id=1066&class=10005&n=2)

</section>


<!-- more -->

<h2>Spring MVC フレームワーク</h2>

<ul><li>[Spring MVC フレームワークとは](#Spring MVC フレームワークとは)</li>
<li>[Spring MVC で使われる主なクラス / インターフェース](#Spring MVC で使われる主なクラス / インターフェース)</li>
<li>[コントローラ・サーブレットの設定 (web.xml)](#コントローラ・サーブレットの設定 (web.xml))</li>
<li>[ModelAndView のサンプルコード](#ModelAndView のサンプルコード)</li>
<li>[ViewResolverの例](#ViewResolverの例)</li>
<li>[Validateを使った妥当性チェックの方法](#Validateを使った妥当性チェックの方法)</li>
<li>[Bind を使ったエラーメッセージの表示方法](#Bind を使ったエラーメッセージの表示方法)</li>
<li>[型変換時のエラーメッセージを独自のメッセージにする方法](#型変換時のエラーメッセージを独自のメッセージにする方法)</li>
<li>[独自の型変換を使う方法](#独自の型変換を使う方法)</li>
<li>[Command コントローラ一覧](#Command コントローラ一覧)</li>
<li>[コントローラマッピング](#コントローラマッピング)</li>
<li>[ハマった点](#ハマった点)</li>
<li>[Spring MVC フレームワークに出てくる用語](#Spring MVC フレームワークに出てくる用語)</li>
</ul>

<h2 id="Spring MVC フレームワークとは">Spring MVC フレームワークとは</h2>

Spring MVCは、IoC コンテナをベースにした、WebアプリケーションのためのMVCアーキテクチャです。IoCコンテナをベースにしているので、設定は定義ファイルに書くことになります。

Spring MVC が使用する定義ファイルは、WEB-INFフォルダの直下に 「<em>《サーブレット名》-servlet.xml</em>」という名前で作成します。《サーブレット名》は web.xml で設定した名前となります。(今後は、便宜的に 「Web アプリケーション定義ファイル」と呼びます。)

Spring MVC の基本的な処理の流れは以下のようになります。

<pre>
リクエストを受け取る （コントローラーサーブレット）
　　　　　　↓
転送先のコントローラを決定する （ハンドラマッピング）
　　　　　　↓
ビジネスロジックを呼び出す （コントローラ）
　　　　　　↓
ビジネスロジックを呼び出して「モデル」に結果をセットする （ビジネスロジック）
　　　　　　↓
モデルと、それを表示するビュー名のセット （モデル＆ビュー）
　　　　　　↓
ビュー名からビューを解決する （ビューリゾルバ）
　　　　　　↓
モデルの結果を表示する （ビュー）
</pre>

Spring MVC のアーキテクチャは非常に柔軟で、プラグイン形式でワークフローを制御したり、Validation を追加したりすることが出来ます。Web アプリケーション定義ファイルに定義された ID を使って Bean を指定することでプラグインします。

<h2 id="Spring MVC で使われる主なクラス / インターフェース">Spring MVC で使われる主なクラス / インターフェース</h2>

Spring MVC を利用する場合に使われる主なクラスやインターフェースです。

<h3>org.springframework.web.servlet.DispatcherServlet</h3>

コントローラサーブレットの基本クラスです。アダプタクラス設定することで、さまざまなワークフローを行うことが出来ます。デフォルトの設定クラスは以下のようになっています。

<dl>
<dt>HandlerMapping</dt>
<dd>
<p>リクエストとコントローラのマッピングを保持します。デフォルトの設定は BeanNameUrlHandlerMappingです。このクラスは「/」で始まるBean名とURLをマッピングします。多くの場合、SimpleUrlHandlerMapping クラスを変わりに使います。</p>
</dd>
<dt>HandlerAdapter</dt>
<dd><p>ワークフローを制御するために設定されるもの(たぶん)です。デフォルトは SimpleControllerHandlerAdapter です。</p></dd>
<dt>HandlerExceptionResolver</dt>
<dd><p>例外からエラーページビューを解決するためのクラスです。デフォルトは設定されていません（none）。</p></dd>
<dt>ViewResolver</dt>
<dd><p>ビューと名前をマッピングさせるものです。 例えば、 prefix="/WEB-INF/jsp"  suffix=".jsp"  viewname="test"  の場合、/WEB-INF/jsp/test.jsp  というビューを参照することになります。</p>

<p><em>JSPファイルは、WEB-INF 以下のフォルダに置くことをおすすめします。</em>こうすることで、外部から直接アクセスが出来なくなります。コントローラを経由しなければファイルが読めないので、セキュリティ的にも非常に効果的です。</p>

<p>デフォルトは InternalResourceViewResolver を使います。このクラスは、実際にビューの在る無しに関わらず、名前からビューを解決することが出来ます。</p></dd>
<dt>MultipartResolver</dt>
<dd><p>マルチパートのリクエストを処理するものです。「multipartResolver」という名前でBean定義を設定します。デフォルトは設定されていません（none）。</p></dd>
<dt>LocaleResolver</dt>
<dd><p>Webベースロケール(?)を受け付けるかどうかを設定します。デフォルトは AcceptHeaderLocaleResolver クラスです。このクラスは、セッション、クッキーなどを受け付けます。おそらく、ワークフローを処理する条件か何かになると思います。「localeResolver」という名前でBean定義を設定します。</p></dd>
<dt>ThemeResolver</dt>
<dd><p>テーマを受け付けるかどうかを設定します。おそらく、ワークフローを処理する条件か何かだと思います。 「themeResolver」という名前でBean定義を設定します。デフォルトは FixedThemeResolver クラスです。</p></dd>
</dl>

<h3>org.springframework.web.servlet.mvc.Controller</h3>

リクエストとレスポンスを引数にとるコントローラの基本インターフェースです。サーブレットとほとんど同じで、Struts の Action クラスのようなものです。このクラスは、複数のHTTPリクエストで処理できるように、スレッドセーフでなければなりません。

リクエストを受け取ったあと、ロケールやテーマを元にワークフローが決定されます。そして、適切なコントローラが見つかると、handleRequest メソッドが呼び出されます。Webアプリケーションのために、標準的な機能を持つコントローラが用意されています。

<dl>
<dt>org.springframework.web.servlet.mvc.AbstractController</dt>
<dd>[Template Method パターン](http://hamasyou.com/archives/000173)を利用した、便利なコントローラ抽象クラスです。

<section>

<h4>【特徴的な機能】</h4>

<dl>
<dt>Generation of Caching Headers</dt>
<dd><p>世代別キャッシュヘッダーを備えています。指定秒内の同じリクエストに対してはキャッシュされた結果を返します。</p></dd>
<dt>GET/POST のサポート切り替え</dt>
<dd><p>GET/POST のサポートを切り替えます。POST メソッドは処理するのに、GET メソッドはエラーにするといったことが出来ます。</p></dd>
</dl>

</section>
    
<section>

<h4>【設定可能な項目(Web アプリケーション定義ファイルに書く)】</h4>

<dl>
<dt>supportedMethods</dt>
<dd><p>サポートするメソッド(GET, POST, PUT)。カンマ(,)で区切って指定する(例： GET,POST)。デフォルトは『GET,POST』</p></dd>
<dt>requireSession</dt>
<dd><p>セッションの存在をチェックするかどうか(true, false)。この設定を true にしておけば、セッション切れをチェックできます。セッションが切れていたときには ServletException がスローされます。デフォルトは『false』</p></dd>
<dt>cacheSeconds</dt>
<dd><p>キャッシュヘッダーを利用する秒数(数値)。設定した秒内の同じリクエストに対しては、キャッシュされた結果を返します。デフォルトは『-1 (キャッシュを利用しない)』</p></dd>
</dl>

</section>
    
<section>

<h4>【ワークフロー】</h4>

<ol><li>メソッドがサポートされているかのチェック</li>
<li>セッションが要求されているかどうかのチェック</li>
<li>キャッシュヘッダーのチェック</li>
<li>コントローラ処理の呼び出し</li>
</ol>

</section>
</dd>
<dt>org.springframework.web.servlet.mvc.AbstractCommandController</dt>
<dd><p>リクエストからコマンドオブジェクトを作成して、リクエストパラメータをセットするコントローラです。リクエストパラメータを Bean クラスに設定するために利用できます。その際、Validation を使ってパラメータの妥当性チェックを行うことが出来ます。Validation の結果は Errors オブジェクトに格納します。</p>

<p>リクエストパラメータが 「firstName」であった場合、コマンドオブジェクトの「setFirstName」メソッドが呼び出されます。ネスとしたパラメータ、「address.city」といった形も受け入れられます(getAddress().setCity())。</p>

<p>パラメータを独自型や特殊型に変換することも出来ます。その際は、PropertyEditor クラスを利用します。</p>

<section>

<h4>[参考]</h4>

[ target="_blank" class="extlink">Spring Pad - PropertyEditorだよ](http://wiki.bmedianode.com/Spring/?PropertyEditor%A4%C0%A4%E8)

</section>

<section>

<h4>【設定可能な項目】</h4>

<dl><dt>commandName</dt>
<dd><p>リクエストとコマンドオブジェクトを結びつけるときに使う名前(文字列)。デフォルトは『command』。</p></dd>
<dt>commandClass</dt>
<dd><p>コマンドとして使われる完全クラス名(文字列)。デフォルトは『null』。</p></dd>
<dt>validator</dt>
<dd><p>妥当性チェックで使われる名前(Bean 参照)。リストも受け取れます。複数のバリデーションクラスを指定する場合は 「validators」を使います。設定する値は、「&lt;ref bean=&quot;ValidatorID&quot;/&gt;」のように書きます。デフォルトは『null』です。</p></dd>
<dt>validateOnBinding</dt>
<dd><p>リクエストと結び付けられたバリデータを有効にするかどうか(true, false)。デフォルトは『true』。</p></dd>
</dl>

</section>

<p>通常は、このクラス使わずに AbstractFormController を代わりに使います。</p>
</dd>
<dt>org.springframework.web.servlet.mvc.AbstractFormController</dt>
<dd>
<p>リクエストパラメータを自動的に フォームオブジェクトに設定するコントローラです。 Struts の Action とほとんど同じものだと思います。フォームオブジェクトは、 Struts の ActionForm と同じくただの JavaBean です。リクエスト毎に新しいフォームオブジェクトを作成するか、毎回同じフォームオブジェクトを使いまわすかを決められるようです。フォームが送信されたかどうかは 「<em>isFormSubmission(HttpServletRequest)</em>」メソッドの戻り値で判断されます。デフォルトは POST で呼び出された場合に、送信とみなされます。オーバーライドして独自にカスタマイズも可能です(コマンド名が送信されたらフォーム送信とみなすとか)。</p>

<p>サブクラスに、 SimpleFormController があり、Bean定義 を使って処理後のページを設定できるようです。AbstractFormController を直接使う場合は、処理後のページはプログラマティックに設定しなければなりません。</p>

<section>

<h4>【設定可能な項目】</h4>

<dl><dt>bindOnNewForm</dt>
<dd><p>新しいフォームオブジェクトを作成した場合に、リクエストと結びつけるかを決めます(true, false)。結び付けなかった場合は、ただのリクエストパラメータはただのパラメータとして処理されます。デフォルトは『false』。</p></dd>
<dt>sessionForm</dt>
<dd><p>セッションで共有するかどうかの設定(true, false)。デフォルトは『false』</p></dd>
<dt>AbstractCommandControllerで設定できる項目</dt>
<dd><p>AbstractCommandController で設定できる項目が設定できます。</p></dd>
</dl>

</section>
</dd>
<dt>org.springframework.web.servlet.mvc.multiaction.MultiActionController</dt>
<dd><p>メソッド名で複数のリクエストを処理することが出来るクラスです。アクション名をメソッドと結びつけることで、コントローラクラスが膨張するのを防げます。</p>

<section>

<h4>定義するメソッドシグネチャ</h4>

<pre>
ModelAndView 《メソッド名》(HttpServletRequest request, HttpServletResponse response);
ModelAndView 《メソッド名》(HttpServletRequest request, HttpServletResponse response, ExceptionClass exception);
</pre>

</section>

<p>メソッド名とアクション名のマッピングは MethodNameResolver を使います。「methodNameResolver」というプロパティ名に設定します。</p>

<section>

<h4>MethodNameResolver に使える主なクラス</h4>

<dl>
<dt>ParameterMethodNameResolver</dt>
<dd><p>リクエストパラメータを元にメソッド名を決定します。例えば 「<code>http://hamasyou.com/index.view?action=insert</code>」 の場合、 「ModelAndView insert(HttpServletRequest, HttpServletResponse)」 というメソッドが呼ばれます。「action」というパラメータ名をメソッドと結びつけるという設定を行っておく必要があります。</p></dd>
<dt>InternalPathMethodNameResolver</dt>
<dd><p>パス名がそのままメソッド名と結び付けられるクラスです。「<code>http://hamasyou.com/insert.view</code>」 というリクエストパスがあった場合に 「ModelAndView insert(HttpServletRequest, HttpServletResponse)」 が呼ばれることになります。</p></dd>
<dt>PropertiesMethodNameResolver</dt>
<dd><p>マッピングをプロパティファイルに持つクラスです。「/action/insert.view=insert」というマッピング情報をプロパティファイルに書いておくことで、「/action/insert.view」 という名前が含まれるパスが呼ばれると 「ModelAndView insert(HttpServletRequest, HttpServletResponse)」 が呼ばれることになります。</p></dd>
</dl>

</section>
</dd>
</dl>

<h3>org.springframework.web.servlet.ModelAndView</h3>

ビューに対するモデルを保持する単なるホルダーとして機能します。コントローラから返された ModelAndView インスタンスからビューに遷移し、ビューからモデルを使用できるようにします。

ビューには、View を継承したオブジェクトか、 ViewResolver によって解決されるビュー名を指定します。 モデルには、キーとなる文字列と、オブジェクトを設定します。内部では Mapで保持するようです。

ビューにおいてモデルは、EL式を用いて表現することが出来ます。JSPでは、request の attribute にモデルが設定されてるはずです。

<h3>org.springframework.web.servlet.ViewResolver</h3>

ビュー名とビューのマッピングを行うクラスです。ResourceBundleViewResolver クラスは、マッピング情報を設定ファイルに指定できます。InternalResourceViewResolver はマッピングを MVCアプリケーション定義ファイルにしていします。

Spring で言う 「ビュー」とは、レスポンスを返すクラスのことです。例えば、JSTL と JSP を使ってレスポンスを返す場合には、org.springframework.web.servlet.view.JstlView を使用します。Velocity などのテンプレートエンジンや JSF などを使用する場合には、別のビュークラスを指定します([参考](#ViewResolverの例))

<h3>org.springframework.web.servlet.HandlerInterceptor</h3>

ワークフローに処理を付け加えるためのインターフェースです。「インターセプター」という名前の通り、処理を横取りします。例えば、入力パラメータのエンコードに使えます。

<h2 id="コントローラ・サーブレットの設定 (web.xml)">コントローラ・サーブレットの設定 (web.xml)</h2>

コントローラ・サーブレットは web.xml に設定します。サーブレット読み込み時にIoC コンテナのところで書いた Bean定義ファイル を読み込むには、 <em>org.springframework.web.context.ContextLoaderListener</em> を使用します。

<section>

<h4>[参考]</h4>

[ target="_balnk" class="extlink">Spring Pad- Servlet 環境への導入](http://wiki.bmedianode.com/Spring/?Servlet%B4%C4%B6%AD%A4%D8%A4%CE%C6%B3%C6%FE)

</section>

<section>

<h4>サーブレットの例</h4>

<pre class="code"><code><span class="tag">&lt;?xml version=&quot;1.0&quot; ?&gt;</span> 
<span class="tag">&lt;!DOCTYPE web-app PUBLIC &quot;-//Sun Microsystems, Inc.//DTD Web Application 2.3//EN&quot;  
&quot;http://java.sun.com/dtd/web-app_2_3.dtd&quot;&gt;</span> 
<span class="tag">&lt;web-app&gt;</span>    
   
<span class="tag">&lt;listener&gt;</span> 
    <span class="tag">&lt;listener-class&gt;</span> 
    org.springframework.web.context.ContextLoaderListener 
    <span class="tag">&lt;/listener-class&gt;</span> 
<span class="tag">&lt;/listener&gt;</span> 
   
  <span class="rem">&lt;!-- Bean定義ファイルの設定 --&gt;</span> 
  <span class="tag">&lt;context-param&gt;</span> 
    <span class="tag">&lt;param-name&gt;</span>contextConfigLocation<span class="tag">&lt;/param-name&gt;</span> 
    <span class="tag">&lt;param-value&gt;</span>BeanContext.xml<span class="tag">&lt;/param-value&gt;</span> 
  <span class="tag">&lt;/context-param&gt;</span>   
   
  <span class="rem">&lt;!-- 通常のコントローラ・サーブレット --&gt;</span> 
  <span class="tag">&lt;servlet&gt;</span> 
    <span class="tag">&lt;servlet-name&gt;</span>dispatcher<span class="tag">&lt;/servlet-name&gt;</span> 
    <span class="tag">&lt;servlet-class&gt;</span> 
      org.springframework.web.servlet.DispatcherServlet 
    <span class="tag">&lt;/servlet-class&gt;</span> 
    <span class="tag">&lt;load-on-startup&gt;</span>1<span class="tag">&lt;/load-on-startup&gt;</span> 
  <span class="tag">&lt;/servlet&gt;</span> 
   
  <span class="tag">&lt;servlet-mapping&gt;</span> 
    <span class="tag">&lt;servlet-name&gt;</span>dispatcher<span class="tag">&lt;/servlet-name&gt;</span>     
    <span class="tag">&lt;url-pattern&gt;</span>*.do<span class="tag">&lt;/url-pattern&gt;</span> 
  <span class="tag">&lt;/servlet-mapping&gt;</span> 
   
<span class="tag">&lt;/web-app&gt;</span> 
</code></pre>

</section>

<h2 id="ModelAndView のサンプルコード">ModelAndView のサンプルコード</h2>

ModelAndView を使ったビュー表示の例です。

<pre class="code"><code><span class="keyword">public</span> <span class="keyword">class</span> WelcomeForm <span class="keyword">extends</span> SimpleFormController {
  <span class="comment">/**
   * 読み取り専用で処理すること！
   * スレッドセーフでないから、書き込みすると危ない。
   * 本来なら、clone を返すか、ラップするべし。
   */</span>
  <span class="keyword">private</span> Person[] defaultPersons;
 
  <span class="keyword">public</span> WelcomeForm() {
    <span class="keyword">super</span>();
  }
 
  <span class="keyword">public</span> Person[] getDefaultPersons() {
    <span class="keyword">return</span> defaultPersons;
  }
 
  <span class="keyword">public</span> <span class="keyword">void</span> setDefaultPersons(Person[] defaultPersons) {
    <span class="keyword">this</span>.defaultPersons = defaultPersons;
  }
 
  <span class="keyword">protected</span> ModelAndView 
     processFormSubmission(HttpServletRequest request,
                           HttpServletResponse response, 
                           Object command, 
                           BindException errors)
      <span class="keyword">throws</span> Exception {
    Map map = <span class="keyword">new</span> HashMap();
    map.put(<span class="literal">"title"</span>, <span class="literal">"WelcomeFormTitle"</span>);
    map.put(<span class="literal">"persons"</span>, getDefaultPersons());        
    <span class="keyword">return</span> <span class="keyword">new</span> ModelAndView(<span class="literal">"/WEB-INF/jsp/form_list.jsp"</span>, <span class="literal">"model"</span>, map);
  }
}
</code></pre>

Webアプリケーション定義ファイルの設定例です。今回は、コントローラ(WelcomeForm)が SimpleFormController を継承しているので、「<em>commandClass</em>」 を設定しなければなりません。 jp.dip.xlegend.spring.web.cmd.WelcomeCommand は、ただの JavaBeanです。Struts で言うところの、 ActionForm だと思えばいいと思います。

<section>

<h4>Web アプリケーション定義ファイルの設定例</h4>

<pre class="code"><code><span class="rem">&lt;!-- 中略 --&gt;</span> 
<span class="tag">&lt;bean <span class="attr">id=</span><span class="value">&quot;WelcomeForm&quot;</span> 
      <span class="attr">class=</span><span class="value">&quot;jp.dip.xlegend.spring.web.WelcomeForm&quot;</span>&gt;</span> 
  <span class="tag">&lt;property <span class="attr">name=</span><span class="value">&quot;commandClass&quot;</span>&gt;</span> 
    <span class="tag">&lt;value&gt;</span>jp.dip.xlegend.spring.web.cmd.WelcomeCommand<span class="tag">&lt;/value&gt;</span> 
  <span class="tag">&lt;/property&gt;</span> 
  <span class="tag">&lt;property <span class="attr">name=</span><span class="value">&quot;validator&quot;</span>&gt;</span> 
    <span class="tag">&lt;ref <span class="attr">bean=</span><span class="value">&quot;welcomeValidator&quot;</span>/&gt;</span> 
  <span class="tag">&lt;/property&gt;</span> 
  <span class="tag">&lt;property <span class="attr">name=</span><span class="value">&quot;defaultPersons&quot;</span>&gt;</span> 
    <span class="tag">&lt;list&gt;</span> 
      <span class="tag">&lt;ref <span class="attr">bean=</span><span class="value">&quot;person1&quot;</span>/&gt;</span> 
      <span class="tag">&lt;ref <span class="attr">bean=</span><span class="value">&quot;person2&quot;</span>/&gt;</span> 
      <span class="tag">&lt;ref <span class="attr">bean=</span><span class="value">&quot;person3&quot;</span>/&gt;</span> 
    <span class="tag">&lt;/list&gt;</span> 
  <span class="tag">&lt;/property&gt;</span> 
<span class="tag">&lt;/bean&gt;</span> 
 
<span class="tag">&lt;bean <span class="attr">id=</span><span class="value">&quot;person1&quot;</span>  
      <span class="attr">class=</span><span class="value">&quot;jp.dip.xlegend.spring.Person&quot;</span>&gt;</span> 
  <span class="tag">&lt;property <span class="attr">name=</span><span class="value">&quot;name&quot;</span>&gt;</span><span class="tag">&lt;value&gt;</span>山田　太郎<span class="tag">&lt;/value&gt;</span><span class="tag">&lt;/property&gt;</span> 
<span class="tag">&lt;/bean&gt;</span> 
<span class="tag">&lt;bean <span class="attr">id=</span><span class="value">&quot;person2&quot;</span>  
      <span class="attr">class=</span><span class="value">&quot;jp.dip.xlegend.spring.Person&quot;</span>&gt;</span> 
  <span class="tag">&lt;property <span class="attr">name=</span><span class="value">&quot;name&quot;</span>&gt;</span><span class="tag">&lt;value&gt;</span>山田　次郎<span class="tag">&lt;/value&gt;</span><span class="tag">&lt;/property&gt;</span> 
<span class="tag">&lt;/bean&gt;</span> 
<span class="tag">&lt;bean <span class="attr">id=</span><span class="value">&quot;person3&quot;</span>  
      <span class="attr">class=</span><span class="value">&quot;jp.dip.xlegend.spring.Person&quot;</span>&gt;</span> 
  <span class="tag">&lt;property <span class="attr">name=</span><span class="value">&quot;name&quot;</span>&gt;</span><span class="tag">&lt;value&gt;</span>山田　三郎<span class="tag">&lt;/value&gt;</span><span class="tag">&lt;/property&gt;</span> 
<span class="tag">&lt;/bean&gt;</span> 
<span class="rem">&lt;!-- 中略 --&gt;</span>
</code></pre>

</section>

ModelAndView で設定された JSP ファイルの例です。JSTLを使って表示していますが、スクリプトレットを使って表示することも出来ます。

<section>

<h4>JSPファイルの例</h4>

<pre class="code"><code><span class="tag">&lt;%@ page contentType=&quot;text/html; charset=Shift_JIS&quot; %&gt;</span>
<span class="tag">&lt;%@ page import=&quot;java.util.*&quot; %&gt;</span>
<span class="tag">&lt;%@ page import=&quot;jp.dip.xlegend.spring.Person&quot; %&gt;</span>
<span class="tag">&lt;%@ taglib prefix=&quot;c&quot; uri=&quot;http://java.sun.com/jstl/core&quot; %&gt;</span>
<span class="tag">&lt;%@ taglib prefix=&quot;fmt&quot; uri=&quot;http://java.sun.com/jstl/fmt&quot; %&gt;</span>  
 
<span class="tag">&lt;html&gt;</span>
<span class="tag">&lt;head&gt;</span>
<span class="tag">&lt;title&gt;</span>form_list.jsp<span class="tag">&lt;/title&gt;</span>
<span class="tag">&lt;/head&gt;</span>
<span class="tag">&lt;body <span class="attr">bgcolor=</span><span class="value">&quot;#FFFFFF&quot;</span>&gt;</span>
<span class="tag">&lt;form <span class="attr">action=</span><span class="value">&quot;welcome.do&quot;</span> <span class="attr">method=</span><span class="value">&quot;post&quot;</span>&gt;</span>
  EL式を利用した表示 <span class="tag">&lt;br&gt;</span>
  <span class="tag">&lt;h4&gt;</span><span class="tag">&lt;c:out <span class="attr">value=</span><span class="value">&quot;${model.title}&quot;</span>/&gt;</span><span class="tag">&lt;/h4&gt;</span>
  <span class="tag">&lt;c:forEach <span class="attr">items=</span><span class="value">&quot;${model.persons}&quot;</span> <span class="attr">var=</span><span class="value">&quot;person&quot;</span>&gt;</span>
    <span class="tag">&lt;c:out <span class="attr">value=</span><span class="value">&quot;${person.name}&quot;</span>/&gt;</span>
  <span class="tag">&lt;/c:forEach&gt;</span>  
 
  こうしても同じ結果が取得できる。
  <span class="tag">&lt;%
      Map map = (Map)request.getAttribute(&quot;model&quot;);
  %&gt;</span>
 
  <span class="tag">&lt;h4&gt;</span><span class="tag">&lt;%= map.get(&quot;title&quot;) %&gt;</span><span class="tag">&lt;/h4&gt;</span>
  
  <span class="tag">&lt;%
      Person[] persons = (Person[])map.get(&quot;persons&quot;);
      for (int i = 0; i &lt; persons.length; i++) {      
  %&gt;</span>  
  <span class="tag">&lt;%= persons[i].getName() %&gt;</span>
  <span class="tag">&lt;% } %&gt;</span>
 
  <span class="tag">&lt;input <span class="attr">type=</span><span class="value">&quot;submit&quot;</span> <span class="attr">value=</span><span class="value">&quot;送信&quot;</span>&gt;</span>  
<span class="tag">&lt;/form&gt;</span>
<span class="tag">&lt;/body&gt;</span>
<span class="tag">&lt;/html&gt;</span>
</code></pre>

</section>

最後に、結果画面です。

<img src="http://hamasyou.com/images/engineer_soul/spring_jsp_01.gif" alt="結果画面" />

<h2 id="ViewResolverの例">ViewResolverの例</h2>

ViewResolver はビュー名とビューとをマッピングするものです。レスポンスを返すものを「ビュー」と、Springでは呼んでいます。ビューの例としては「JSP」、「JSF」、「Velocity」、「Tiles」、「Excel」、「PDF」などがあります。

ViewResolver を指定する場合には、MVCアプリケーション定義ファイルに「<em>viewResolver</em>」という名前で設定します。

<section>

<h4>ViewResolverの設定例</h4>

<pre class="code"><code><span class="rem">&lt;!-- 中略 --&gt;</span>
<span class="tag">&lt;bean <span class="attr">id=</span><span class="value">&quot;viewResolver&quot;</span> 
      <span class="attr">class=</span><span class="value">&quot;org.springframework.web.servlet.view.InternalResourceViewResolver&quot;</span>&gt;</span>
  <span class="tag">&lt;property <span class="attr">name=</span><span class="value">&quot;viewClass&quot;</span>&gt;</span>
    <span class="tag">&lt;value&gt;</span>org.springframework.web.servlet.view.JstlView<span class="tag">&lt;/value&gt;</span>
  <span class="tag">&lt;/property&gt;</span>
  <span class="tag">&lt;property <span class="attr">name=</span><span class="value">&quot;prefix&quot;</span>&gt;</span>
    <span class="tag">&lt;value&gt;</span>/WEB-INF/jsp/<span class="tag">&lt;/value&gt;</span>
  <span class="tag">&lt;/property&gt;</span>
  <span class="tag">&lt;property <span class="attr">name=</span><span class="value">&quot;suffix&quot;</span>&gt;</span>
    <span class="tag">&lt;value&gt;</span>.jsp<span class="tag">&lt;/value&gt;</span>
  <span class="tag">&lt;/property&gt;</span>
<span class="tag">&lt;/bean&gt;</span>
<span class="rem">&lt;!-- 中略 --&gt;</span>
</code></pre>

</section>

<h2 id="Validateを使った妥当性チェックの方法">Validateを使った妥当性チェックの方法</h2>

データ検証について、『[ target="_blank" class="extlink">実践J2EE](http://www.amazon.co.jp/exec/obidos/ASIN/4797322888/sorehabooks-22)』から、少し抜粋します。(P.532)

{% blockquote 『[ target="_blank" class="extlink">実践J2EE](http://www.amazon.co.jp/exec/obidos/ASIN/4797322888/sorehabooks-22)』 %}
データ検証 (Validation) は、「構文」の検証と「セマンティクス」の検証とに分類されます。構文検証には、データが存在するか、データの長さが許容範囲にあるか、データが有効なフォーマット(数字など)であるかといった、単純な操作が含まれます。通常、これはビジネスロジックではありません。セマンティクス検証はこれより手が込んだもので、ビジネスロジックやデータアクセスまでが含まれます。


{% endblockquote %}

Struts では、構文検証もセマンティクス検証も、アクションフォームの validate メソッドで行っています。しかし、Spring ではWeb 層にとらわれない方法でValidation 処理を行うことができます。Validator インターフェースを実装したクラスがそれに当たります。

ただし、<em>Validator インターフェースでは、サポートするクラスを明示的に指定しなければなりません(supports メソッド)</em>。これだと、フォームごとにアクションフォームのようなクラスをたくさん作った場合に、Validator クラスも作らなければなりません。

そこで、「構文」の検証には、BaseCommandController クラスの 「<em>onBindAndValidate</em>」メソッドをオーバーライドして、「セマンティクス」の検証にだけ、Validator インターフェースを継承したクラスを作成するのがいいと思います。

<h2 id="Bind を使ったエラーメッセージの表示方法">Bind を使ったエラーメッセージの表示方法</h2>

Bind というのは、コマンドオブジェクトのフィールドと、入力パラメータとを結びつける機構です。JSP の入力項目で idと password という入力項目があったとすると、コマンドオブジェクトの setId()、setPassword() というメソッドが自動的に呼ばれるということです。

もちろん、妥当性チェック(Validation) に失敗した場合にエラーメッセージを表示させることもできます。Struts のタグリブを使った動作とほとんど同じ感じです。

<pre class="code"><code><span class="tag">&lt;%@ taglib prefix=&quot;c&quot; uri=&quot;http://java.sun.com/jstl/core&quot; %&gt;</span> 
<span class="tag">&lt;%@ taglib prefix=&quot;spring&quot; uri=&quot;/spring&quot; %&gt;</span> 
<span class="tag">&lt;html&gt;</span> 
<span class="tag">&lt;body&gt;</span> 
<span class="tag">&lt;form <span class="attr">action=</span><span class="value">&quot;entry.do&quot;</span> <span class="attr">method=</span><span class="value">&quot;post&quot;</span>&gt;</span> 
  <span class="tag">&lt;table&gt;</span> 
    <span class="tag">&lt;tr&gt;</span> 
   <span class="tag">&lt;spring:bind <span class="attr">path=</span><span class="value">&quot;person.id&quot;</span>&gt;</span> 
      <span class="tag">&lt;td&gt;</span> 
      ID： 
      <span class="tag">&lt;/td&gt;</span> 
      <span class="tag">&lt;td&gt;</span> 
      <span class="tag">&lt;input <span class="attr">type=</span><span class="value">&quot;text&quot;</span> <span class="attr">name=</span><span class="value">&quot;id&quot;</span> <span class="attr">value=</span><span class="value">&quot;&lt;c:out value=&quot;</span></span>&quot;&gt; 
      <span class="tag">&lt;td&gt;</span>  
      <span class="tag">&lt;font <span class="attr">color=</span><span class="value">&quot;red&quot;</span>&gt;</span><span class="tag">&lt;c:out <span class="attr">value=</span><span class="value">&quot;${status.errorMessage}&quot;</span>/&gt;</span><span class="tag">&lt;/font&gt;</span> 
      <span class="tag">&lt;/td&gt;</span> 
     <span class="tag">&lt;/spring:bind&gt;</span> 
    <span class="tag">&lt;/tr&gt;</span> 
    <span class="tag">&lt;tr&gt;</span> 
      <span class="tag">&lt;spring:bind <span class="attr">path=</span><span class="value">&quot;person.password&quot;</span>&gt;</span> 
      <span class="tag">&lt;td&gt;</span> 
      Password： 
      <span class="tag">&lt;/td&gt;</span> 
      <span class="tag">&lt;td&gt;</span> 
      <span class="tag">&lt;input <span class="attr">type=</span><span class="value">&quot;text&quot;</span> <span class="attr">name=</span><span class="value">&quot;password&quot;</span> <span class="attr">value=</span><span class="value">&quot;&lt;c:out value=&quot;</span></span>&quot;&gt; 
      <span class="tag">&lt;td&gt;</span>  
      <span class="tag">&lt;font <span class="attr">color=</span><span class="value">&quot;red&quot;</span>&gt;</span><span class="tag">&lt;c:out <span class="attr">value=</span><span class="value">&quot;${status.errorMessage}&quot;</span>/&gt;</span><span class="tag">&lt;/font&gt;</span> 
      <span class="tag">&lt;/td&gt;</span> 
      <span class="tag">&lt;/spring:bind&gt;</span> 
    <span class="tag">&lt;/tr&gt;</span> 
<span class="rem">&lt;!-- 中略 --&gt;</span>
</code></pre>

「<em>&lt;spring:bind path=&quot;person.id&quot;&gt;</em>」の部分が Bind している部分です。person という名前で定義されたコマンドオブジェクトの id フィールドと、入力フィールドの id を結び付けています。 「<em>{status.value} </em>」というのは Bind された値を表示する場合に使うものです。入力値を Bind して、妥当性チェックでエラーがあった場合に、元の画面で入力値を再表示させるときに使えます。

「<em>{status.errorMessage}</em>」は妥当性チェック等で、Errors オブジェクトにエラーが設定された場合に表示されます。 Validation クラスのエラーチェック時に、 「errors.rejectValue("id", 《エラーコード》, "Default Message.") メソッド」を呼び出してエラーを設定すると、画面に表示されるようになります。ちなみに「<em>{status.expression}</em>」はバインドするプロパティ名が取得できます。フィールドの name 属性にセットするという使い方が出来ます。

<section>

<h3>Validation で エラーメッセージを追加する方法</h3>

<pre class="code"><code><span class="keyword">public</span> <span class="keyword">void</span> validate(Object command, Errors errors) { 
  Person person = (Person)command; 
  <span class="keyword">if</span> (person.getPassword() == <span class="keyword">null</span>) { 
    errors.rejectValue(<span class="literal">"password"</span>, <span class="literal">"M001"</span>,  
                       <span class="keyword">new</span> String[] { <span class="literal">"Password"</span> },  
                       <span class="literal">"必須入力です。"</span>); 
  } 
} 
</code></pre>

</section>

Bind するときに使った「person」を定義する場所は、Web アプリケーション定義ファイルです。他にも、コントローラのコンストラクタで setCommandName を呼び出してコマンド名を設定しているサンプルもありました。

<section>

<h3>Web アプリケーション定義ファイルでコマンドオブジェクトを指定する</h3>

<pre class="code"><code><span class="tag">&lt;bean <span class="attr">id=</span><span class="value">&quot;EntryFormController&quot;</span> 
      <span class="attr">class=</span><span class="value">&quot;jp.dip.xlegend.spring.web.EntryFormController&quot;</span>&gt;</span> 
  <span class="tag">&lt;property <span class="attr">name=</span><span class="value">&quot;commandName&quot;</span>&gt;</span> 
    <span class="tag">&lt;value&gt;</span>person<span class="tag">&lt;/value&gt;</span> 
  <span class="tag">&lt;/property&gt;</span> 
  <span class="tag">&lt;property <span class="attr">name=</span><span class="value">&quot;commandClass&quot;</span>&gt;</span> 
    <span class="tag">&lt;value&gt;</span>jp.dip.xlegend.spring.Person<span class="tag">&lt;/value&gt;</span> 
  <span class="tag">&lt;/property&gt;</span> 
<span class="rem">&lt;!-- 中略 --&gt;</span>
</code></pre>

</section>

「<em>commandName</em>」 に Bind するときに使ったコマンド名を指定します。「<em>commandClass</em>」 はコマンドのクラス名を書きます。Spring では、入力値をそのままドメインのオブジェクトにマッピングすることが出来ます。Struts では ActionForm を使わなければならず、コントローラで値の詰め替えが必要でしたが、Spring ではその必要はありません。ただ、凝った画面表示をしようとした場合には、画面用のコマンドを用意して処理を呼び出すなどの方法も必要かもしれません。

<img src="http://hamasyou.com/images/engineer_soul/spring_jsp_02.gif" alt="Bindの結果" />

<h2 id="型変換時のエラーメッセージを独自のメッセージにする方法">型変換時のエラーメッセージを独自のメッセージにする方法</h2>

Bind 時に型変換でエラーが出ると次のようなエラーが出力されます。

<pre>Failed to convert property value of type [java.lang.String] to required type [{型名}] for property '{プロパティ名}'; nested exception is ...</pre>

このエラーは、開発時はまだ許せるとして、リリースした後は、ユーザにはまったく意味不明のメッセージになっています。

このメッセージは、MessageResource を使うことで、独自のメッセージにすることが出来ます。メッセージファイルに 「<em>typeMismatch.{プロパティ名}</em>」 というキーでメッセージを定義しておくと、そのメッセージが使えます。そして、置換文字の 0番目「{0}」にプロパティ名が埋め込まれるようになっています。(typeMismatch だけだと、すべての型変換エラーに対するメッセージが定義できる。typeMismatch.{コマンド名}.{プロパティ名} だと、コマンド名で指定されたものにマッチするようになる。さらに、typeMismatch.{型名} だと、型名にマッチするものに対してメッセージを指定できる。)

<section>

<h4>errorMessage.properties</h4>

<pre>
# 日本語は native2ascii をかけておく
typeMismatch={0} は入力形式が無効です。
typeMismatch.java.sql.Timestamp={0} は日付型で入力してください。
typeMismatch.int={0} は整数値で入力してください。
typeMismatch.dateFiled={0} は yyyy/mm/dd 。
typeMismatch.command.dateField={0} は yyyy/mm/dd の形で入力してください。 
</pre>

</section>

詳細に設定するほど優先的に使われるようです。上記のように定義した場合、command.dateFiled プロパティのエラーメッセージは「{0} はyyyy/mm/dd の形で入力してください。」 が使われます。それ以外の型エラーに関しては、「{0} は入力形式が無効です。」が使われます。

<h4>[参考]</h4>

[ target="_blank" class="extlink">Spring Framework API - DefaultMessageCodesResolver](http://www.springframework.org/docs/api/org/springframework/validation/DefaultMessageCodesResolver.html)

<h2 id="独自の型変換を使う方法">独自の型変換を使う方法</h2>

MVC フレームワークでは、CustomeEditor の設定の仕方がちょっと違ったのでメモ。MVC フレームワーク中で CustomeEditor を設定するには、BaseCommandController クラスのメソッド 「<em>initBind(HttpServletRequest request,  ServletRequestDataBinder binder)</em>」メソッドをオーバーライドして、その中で、binder.registerCustomEditor(Class, PropertyEditor) を呼び出します。SimpleFormController は BaseCommandController を継承しています。

<pre class="code"><code><span class="keyword">protected</span> <span class="keyword">void</span> initBinder(HttpServletRequest request, 
                          ServletRequestDataBinder binder)
    <span class="keyword">throws</span> ServletException {        
  CustomDateEditor de =  <span class="keyword">new</span> CustomDateEditor(
            <span class="keyword">new</span> SimpleDateFormat(<span class="literal">"yyyy/MM/dd"</span>), <span class="keyword">true</span>);        
  binder.registerCustomEditor(java.util.Date.class, de);
}
</code></pre>

<h2 id="Command コントローラ一覧">Command コントローラ一覧</h2>

Spring MVC フレームワークで使われるコントローラの中で、基本的なコントローラである Command Controller をまとめます。

<section>

<h4>[参考]</h4>
「[ target="_blank" class="extlink">Spring Framework リファレンス - 12.3.4. CommandControllers](http://www.springframework.org/docs/reference/mvc.html#mvc-controller-command)」

</section>

<table>
<caption>Command Controller 一覧</caption>
<tr><th>クラス名</th><th>説明</th></tr>
<tr><td>AbstractCommandController</td><td>任意のデータオブジェクトにリクエストをマッピングできる コマンドコントローラ を定義するために使います。フォーム機能は提供しませんが妥当性チェック(Validation)機能は提供されます。。明示的にリクエストパラメータをデータオブジェクトせ設定できるので、分かりやすいコントローラです。</td></tr>
<tr><td>AbstractFormController</td><td>フォーム送信機能を提供します。データオブジェクトにバインドされたフォームを表示し、妥当性エラーが発生したときに画面を再表示できます。サポートされる機能は、不正入力送信チェック、妥当性チェック、フォームのワークフローです。このコントローラは、ビューの指定を明示することができません。</td></tr>
<tr><td>SimpleFormController</td><td>指定したデータオブジェクトとのマッピングを行うフォームコントローラの具象クラスです。コマンドオブジェクトと、成功時や失敗時に表示するビュー名を指定することができます。</td></tr>
<tr><td>AbstractWizardFormController</td><td>ウィザード形式のフォームを作るときに指定します。validatePage, processFinish, processCancel メソッドを実装します。この3つのメソッドを適切に実装して、ワークフローを定義します。</td></tr>
</table>

<h2 id="コントローラマッピング">コントローラマッピング</h2>

コントローラサーブレットは、リクエストを処理するコントローラを選択します。どのような条件でコントローラが選択されるかは、Bean 定義で設定されている HandlerMapping インターフェースの実装クラスに任されます。

<dl>
<dt>BeanNameUrlHandlerMapping</dt>
<dd><p>Struts と同じようなマッピング方法で、スラッシュ(&quot;/&quot;)で始まるURLと Bean の名前をマッピングするクラス。DispatcherServlet がデフォルトで使う実装です。</p></dd>
<dt>SimpleUrlHandlerMapping</dt>
<dd><p>URL からリクエストハンドラBean へのマッピングを提供します。「urlMap」プロパティにマッピンを定義します。この定義は Bean 定義ファイルに書きます。</p></dd>
</dl>

<h2 id="ハマった点">ハマった点</h2>

<h3>Bind 時に例外が発生する</h3>

Bind 時に次のような例外がでます。

<pre>javax.servlet.ServletException: Neither Errors instance nor plain target object for bean name 《コマンド名》 available as request attribute</pre>

これは、Bind するコマンドオブジェクトがリクエストに保存されていないのが原因です。コントローラに SimpleFormController を使っている場合、processFormSubmission() メソッドで、super.onSumit(request, response, command, errors) を呼び出すと、例外は発生しなくなりました。参考までに。

<h3>更新系画面の初期表示はどうするのか？</h3>

FormController 系のコントローラを使うと、フォーム送信をデータオブジェクトに自動でバインドしてくれて楽チンです。ですが、初期表示(フォームに何も入力していない状態の画面)を行いたい場合に、どうすればいいのか分かりませんでした。

SimpleFormController の 「<em>formView</em>」プロパティは、初期表示する画面を設定するプロパティだったということに、やっと気づきました。Struts とかだと、初期表示用のアクションを用意していたので Spring でもそうするのかと思ってました。ハマった・・・ (T T;)

それでもって、初期表示に使いたいデータは 「protected Map referenceData(HttpServletRequest) throws Exception」メソッドをオーバーライドして Map につめて返せばいいわけでした。

<h2 id="Spring MVC フレームワークに出てくる用語">Spring MVC フレームワークに出てくる用語</h2>

Spring MVCフレームワークに出てくる用語をまとめておきます。

<dl>
<dt>コントローラサーブレット</dt>
<dd><p>フレームワークを使用するためのエントリポイントとなるサーブレット。コントローラのコントローラであり、アプリケーション固有のリクエストコントローラを呼び出す役割を担います。DispatcherServlet はコントローラサーブレットです。</p></dd>
<dt>コントローラ(リクエストコントローラ)</dt>
<dd><p>リクエストの処理を受け持つサーブレットです。通常、コントローラサーブレットによってリクエストの処理を委譲されます。コントローラはマルチスレッドコンポーネントであり、スレッドセーフでなければなりません。したがって、フィールドやプロパティはリードオンリであることが推奨されます。<i>org.springframework.web.servlet.mvc.Controller</i> インターフェースを実装する必要があります。実装クラスには BaseCommandController, AbstractFormController, SimpleFormController, MultiActionController などがあります。</p></dd>
<dt>ハンドラマッピング</dt>
<dd><p>リクエストをリクエストコントローラに結びつける役割を担います。コントローラサーブレットは、ハンドラマッピングを元にリクエストを委譲するリクエストコントローラを判断します。<i>org.springframework.web.servlet.HandlerMapping</i> インターフェースを実装する必要があります。実装クラスには BeanNameUrlHandlerMapping, SimpleUrlHandlerMapping などがあります。</p>

<p>[ target="_blank" class="extlink">Handler mappings](http://www.springframework.org/docs/reference/mvc.html#mvc-handlermapping)</p></dd>
<dt>ModelAndView</dt>
<dd><p>MVC アーキテクチャにおける モデルとビューのホルダーです。ビュー名にモデルをバインドして、リクエストコントローラから返されます。</p></dd>
<dt>ビュー</dt>
<dd><p>モデルをレンダリングするオブジェクトです。ビューオブジェクトはレスポンスにモデルデータをレンダリングするのが仕事です。<i>org.springframework.web.servlet.View</i> インターフェースを実装します。実装クラスには JstlView, TilesView, VelocityView などがあります。</p></dd>
<dt>ビューリゾルバ</dt>
<dd><p>ModelAndView のビュー名から、ビューオブジェクトを解決するクラスです。実装クラスには UrlBasedViewResolver などがあります。</p>

<p>[ target="_blank" class="extlink">View and resolving them](http://www.springframework.org/docs/reference/mvc.html#mvc-viewresolver)</p></dd>
<dt>ワークフロー</dt>
<dd><p>コントローラにおける処理の流れのことです。ウィザード形式のフォームコントローラを使用する場合など、画面間における必須項目のチェックなどを行えます。</p></dd>
<dt>ハンドラインターセプター</dt>
<dd><p>コントローラを呼ぶ前や、コントローラの処理が終了した後などに、呼び出されるコールバックオブジェクト。インターセプターと名前がつくことから、コントローラの処理を横取りして、処理をなかったことにしたり、装飾を加えたリクエストをコントローラに渡したりできる。<i>HandlerInterceptor</i> インターフェースを実装する必要がある。通常、HandlerInterceptorAdapter クラスを継承して、任意のインターセプトポイントに関するメソッドのみをオーバーライドすればよい。</p></dd>
<dt>妥当性チェック (Validation)</dt>
<dd><p>入力値が処理可能なものかを検査すること。一般に「構文」の検証と「セマンティクス」の検証とがある。構文検証は、データが存在するか、データの長さが許容範囲に日あっているか、データが有効なフォーマットかどうかを調べるものです。セマンティクス検証は、ビジネスロジックやデータアクセスまでが含まれる、手の込んだ検証になります。</p></dd>
</dl>

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







