---
layout: post
title: "Struts2 おぼえがき"
date: 2007-11-27 23:24
comments: true
categories: [Engineer-Soul]
keywords: "Struts2, おぼえがき, XWork"
tags: []
author: hamasyou
amazon_url: ""
amazon_author: ""
amazon_image: ""
amazon_publisher: ""
---

Struts2 は WebWorks2 をベースとした MVC フレームワークです。Struts1 と変わらずコマンドパターンのフレームワークになっています。

コマンドパターンの実装部分では、OPENSYMHONY の XWork が使われています。

<h3>Struts2の特徴</h3>

<ul>
<li>WebWorks の後継となる WebWorks2 がベースの MVC フレームワーク</li>
<li>コマンドパターンが使われている（XWork が使われている）</li>
<li>アノテーションと XML ファイルによる設定</li>
<li>ActionForm がない。代わりにアクションにフォームデータを格納する</li>
<li>アクションは POJO で作成できる</li>
<li>アクションがスレッドセーフ</li>
<li>設定ファイルに OGNL 式が書ける</li>
<li>View に JSP、Freemaker、Velocity、XSLT が使える</li>
<li>SpringFramework との連携が考慮されている</li>
<li>Ajax をサポートしている</li>
<li>プラグインによりフレームワークの拡張が行える</li>
</ul>

情報が正確ではない可能性が大いにあるので（ドキュメント読まずにソース見ながら書いているので・・・）、鵜呑みにしないでください。

随時更新予定です。

<section>

<h4>執筆時の環境</h4>

<ul>
<li>JDK1.5.12</li>
<li>[ target="_blank" class="extlink">Struts2 2.0.11](http://struts.apache.org/2.x/)</li>
</ul>

</section>


<!-- more -->

<h2>目次</h2>

<ol>
<li>[アーキテクチャ](#アーキテクチャ)
<ol>
    <li>[アーキテクチャ概要図](#アーキテクチャ概要図)</li>
    <li>[主なクラス](#主なクラス)</li>
    <li>[概要クラス図とシーケンス図](#概要クラス図とシーケンス図)</li>
</ol>
</li>
<li>[Struts2 の主な機能](#Struts2 の主な機能)
<ol>
    <li>[コンフィグレーションファイルの指定](#コンフィグレーションファイルの指定)</li>    
    <li>[Zero コンフィグレーション](#Zero コンフィグレーション)</li>
    <li>[カスタム設定読み込み](#カスタム設定読み込み)</li>
    <li>[Dependency Injection](#Dependency Injection)</li>
    <li>[インターセプタ](#インターセプタ)</li>
    <li>[アクションの作成](#アクションの作成)</li>
    <li>[Result について](#Result について)</li>
</ol>
</li>
</ol>

<h2 id="アーキテクチャ">アーキテクチャ</h2>

<h3 id="アーキテクチャ概要図">アーキテクチャ概要図</h3>

<img src="http://hamasyou.com/images/Struts2/archtecture.jpg" alt="Struts2のアーキテクチャ" />

<h2 id="主なクラス">主なクラス（Struts2、XWork）</h2>

<h3>org.apache.struts2.dispatcher.FilterDispatcher</h3>

Struts2 のリクエストはすべてこのフィルタが受け付け、適切なクラスに処理をディスパッチします。

ディスパッチする先の Dispatcher クラスは、FilterDispatcher#init(FilterConfig) で作成、初期化されます。

<dl>
<dt class="info">URLパターン</dt>
<dd><p>フィルタの url-pattern は「/*」にすることが推奨（JavaDoc によると MUST）されています。</p>

<pre class="code"><code><span class="tag">&lt;filter-mapping&gt;</span> 
  <span class="tag">&lt;filter-name&gt;</span>struts2<span class="tag">&lt;/filter-name&gt;</span> 
  <span class="tag">&lt;url-pattern&gt;</span>/*<span class="tag">&lt;/url-pattern&gt;</span> 
<span class="tag">&lt;/filter-mapping&gt;</span>
</code></pre></dd>
</dl>

「/struts/」で始まるパスがリクエストされると、FilterDispatcher は静的リソースを検索します。

たとえば「http://localhost:8080/struts2/struts/ajax/dojoRequire.js」のようにアクセスされた場合「/struts/」以下のパスをクラスパスから検索します。

デフォルトでは、

<ul>
<li>org.apache.struts2.static</li>
<li>template</li>
<li>org.apache.struts2.interceptor.debugging</li>
</ul>

のクラスパスが指定されていますので、上記URLにアクセスされた場合は「/org/apache/struts2/static/ajax/dojoRequire.js」「/template/ajax/dojoRequire.js」「/org/apache/struts2/interceptor/debugging/dojoRequire.js」からマッチする静的コンテンツを返すようになります。

<h3>org.apache.struts2.dispatcher.mapper.ActionMapper</h3>

Httpリクエストとアクションのマッピングを管理します。リクエストに対応する適切なアクション情報を <code>ActionMapping</code> オブジェクトとして返します。

<h3>org.apache.struts2.dispatcher.Dispatcher</h3>

Dispatcher クラスは Struts2 の設定情報を保持し、 FilterDispatcher から呼び出され実際にリクエストをディスパッチします。

Dispatcher インスタンスはすべてのリクエストで共有されます。

<h3>com.opensymphony.xwork2.config.ConfigurationManager</h3>

設定情報を司るクラスです。複数の <code>ConfigurationProvider</code> を持ち、<code>ConfigurationProvider</code> から情報を受け取り <code>Configuration</code> インスタンスを作成します。

<h3>com.opensymphony.xwork2.config.ConfigurationProvider</h3>

設定情報を提供するクラスです。設定情報ごとにクラスを用意します。たとえば、struts.xml ファイルの設定情報を読み込み、<code>Configuration</code> に設定するのは <code>org.apache.struts2.config.StrutsXmlConfigurationProvider</code> クラスになります。

<h3>com.opensymphony.xwork2.config.Configuration</h3>

設定情報を表すクラスです。

<h3>com.opensymphony.xwork2.ActionProxy</h3>

アクションのプロキシとなるクラスです。アクションの呼び出しを <code>ActionInvocation</code> に委譲します。

<code>ActionProxy</code> の内部では、<code>ActionInvocation</code> が呼び出されます。

<h3>com.opensymphony.xwork2.ActionInvocation</h3>

アクションの呼び出しを行います。アクションの実体とインタセプタを保持しています。

<code>ActionProxy</code> によって初期化され、起動されます。<code>ActionProxy</code> によって invoke() メソッドが呼び出された後は、インタセプタによって invoke() が再帰呼び出しされます。

すべてのインタセプタの呼び出しが完了した後アクションが呼び出されます。その後、Result の呼び出しが行われます。

[<img src="http://hamasyou.com/images/Struts2/invoke.gif" width="500" alt="invoke()の再帰呼び出し" />](http://hamasyou.com/images/Struts2/invoke.gif)

Result の実装として、<code>ServletDispatcherResult</code> などがあります。Result は View の実装（ユーザへのレスポンス）に結び付けられています。

<h3>com.opensymphony.xwork2.interceptor.Interceptor</h3>

アクションの処理を実行する前や後に処理を追加します。

インターセプタはステートレスなクラスとして作成しなければなりません。

<h3>Action</h3>

<code>ActionInvocation</code> から呼び出されるアクションクラスです。

デフォルトでは、<code>ActionInvocation</code> から <code>execute</code> メソッドが呼び出されます。

<pre class="code"><code><span class="keyword">public</span> String execute() <span class="keyword">throws</span> Exception
</code></pre>

<h2 id="概要クラス図とシーケンス図">概要クラス図とシーケンス図</h2>

[<img src="http://hamasyou.com/images/Struts2/action_class_diagram_mini.gif" alt="コマンドパターンのクラス図"  />](http://hamasyou.com/images/Struts2/action_class_diagram.gif)

[<img src="http://hamasyou.com/images/Struts2/action_sequence_mini.gif" alt="アクション実行のシーケンス図" />](http://hamasyou.com/images/Struts2/action_sequence.gif)

Struts2 では、すべてのリクエストが <code><strong>org.apache.struts2.dispatcher.FilterDispatcher</strong></code> を通して処理されます。

<code>FilterDispatcher</code> はリクエストパスからどのアクションを呼び出すかを解決し、<code><strong>org.apache.struts2.dispatcher.Dispatcher</strong></code> の <code>serviceAction</code> を呼び出します。

<code>Dispatcher</code> は <code>serviceAction</code> の中で、<code>ActionProxyFactory</code> を使って <code><strong>ActionProxy</strong></code> を作成して

実際にアクションを実行するのは、<code>ActionInvocation</code> の役割になります。

<h2 id="Struts2 の主な機能">Struts2 の主な機能</h2>

<h3 id="コンフィグレーションファイルの指定">コンフィグレーションファイルの指定</h3>

web.xml の Filter の設定で init-param に <strong>config</strong> というパラメータを設定すると、Struts2 の設定ファイルを指定できます。

デフォルトは「struts-default.xml,struts-plugin.xml,struts.xml」になっています。このファイルは、クラスパス上から検索されます。

<pre class="code"><code><span class="tag">&lt;filter&gt;</span> 
  <span class="tag">&lt;filter-name&gt;</span>struts2<span class="tag">&lt;/filter-name&gt;</span> 
  <span class="tag">&lt;filter-class&gt;</span>org.apache.struts2.dispatcher.FilterDispatcher<span class="tag">&lt;/filter-class&gt;</span> 
  <span class="tag">&lt;init-param&gt;</span> 
    <span class="tag">&lt;param-name&gt;</span>config<span class="tag">&lt;/param-name&gt;</span> 
    <span class="tag">&lt;param-value&gt;</span> 
    struts-default.xml,struts.xml,my-struts.xml 
    <span class="tag">&lt;/param-value&gt;</span> 
  <span class="tag">&lt;/init-param&gt;</span> 
<span class="tag">&lt;/filter&gt;</span>
</code></pre>

<h3 id="Zero コンフィグレーション">Zero コンフィグレーション</h3>

web.xml の Filter の設定で init-param に <strong>actionPackages</strong> というパラメータを設定すると、Struts2 はこのパッケージにあるクラスで、com.opensymphony.xwork2.Action インターフェースを実装したクラスか、「Action」で終わるクラス名を持つクラスを自動で Action クラスとして認識してくれます。

「,」で区切ることで、複数パッケージ指定することもできます。

<pre class="code"><code><span class="tag">&lt;filter&gt;</span> 
  <span class="tag">&lt;filter-name&gt;</span>struts2<span class="tag">&lt;/filter-name&gt;</span> 
  <span class="tag">&lt;filter-class&gt;</span>org.apache.struts2.dispatcher.FilterDispatcher<span class="tag">&lt;/filter-class&gt;</span> 
  <span class="tag">&lt;init-param&gt;</span> 
    <span class="tag">&lt;param-name&gt;</span>actionPackages<span class="tag">&lt;/param-name&gt;</span> 
    <span class="tag">&lt;param-value&gt;</span> 
    com.hamasyou.struts2.webapp 
    <span class="tag">&lt;/param-value&gt;</span> 
  <span class="tag">&lt;/init-param&gt;</span> 
<span class="tag">&lt;/filter&gt;</span>
</code></pre>

上記のように設定した場合、

<img src="http://hamasyou.com/images/Struts2/zero_config.gif" alt="Zeroコンフィグレーション" />

「/usecase1/itemSearch.action」にアクセスすると「com.hamasyou.struts2.webapp.usecase1.ItemRegisterAction」が呼び出されます。

「/usecase2/admin/itemRegister.action」にアクセスすると「com.hamasyou.struts2.webapp.usecase2.admin.ItemRegisterAction」が呼び出されます。

また、「/usecase1/itemSearch!allSearch.action」のように「!メソッド名」でアクションを呼び出すと、指定されたメソッド名が呼び出されます。!メソッド名を指定しない場合は execute メソッドが呼び出されています。

Struts2 のアクションの呼び出しは「*.action」という形になります。（Struts1のときは *.do でした。）

<h3 id="カスタム設定読み込み">カスタム設定読み込み</h3>

Struts2 の起動時に、カスタムプロパティを読み込むことが出来る。

web.xml の init-param に <strong>configProviders</strong> というパラメータを設定すると、Struts2 はこのパラメータに指定されたクラスからカスタムプロパティの読み込みを行うようになります。

指定するクラスは、<strong>com.opensymphony.xwork2.config.ConfigurationProvider</strong> を実装する必要があります。

「,」で区切ることで、クラスを複数指定できます。

<pre class="code"><code><span class="tag">&lt;filter&gt;</span> 
  <span class="tag">&lt;filter-name&gt;</span>struts2<span class="tag">&lt;/filter-name&gt;</span> 
  <span class="tag">&lt;filter-class&gt;</span>org.apache.struts2.dispatcher.FilterDispatcher<span class="tag">&lt;/filter-class&gt;</span> 
  <span class="tag">&lt;init-param&gt;</span> 
    <span class="tag">&lt;param-name&gt;</span>configProviders<span class="tag">&lt;/param-name&gt;</span> 
    <span class="tag">&lt;param-value&gt;</span> 
    com.hamasyou.struts2.webapp.config.MyCustomeConfigurationProvider 
    <span class="tag">&lt;/param-value&gt;</span> 
  <span class="tag">&lt;/init-param&gt;</span> 
<span class="tag">&lt;/filter&gt;</span> 
</code></pre>

<h3 id="Dependency Injection">Dependency Injection</h3>

メソッドかフィールドに <strong>@Inject</strong> を指定することで、型によるインジェクションが行われます。

<section>

<h4>@Inject の例</h4>

<pre class="code"><code>@Inject 
<span class="keyword">private</span> MyObject myObject; 
 
<span class="keyword">private</span> MyObject2 myObject2; 
 
@Inject 
<span class="keyword">public</span> <span class="keyword">void</span> setMyObject2(MyObject2 myObject2) { 
  <span class="keyword">this</span>.myObject2 = myObject2; 
} 
 
<span class="keyword">public</span> String execute() <span class="keyword">throws</span> Exception { 
  System.out.println(<span class="literal">"myObject:"</span> + <span class="keyword">this</span>.myObject); 
  System.out.println(<span class="literal">"myObject2:"</span> + <span class="keyword">this</span>.myObject2); 
  <span class="keyword">return</span> <span class="literal">"success"</span>; 
}
</code></pre>

</section>

インジェクションするオブジェクトの設定は、struts.xml に記述します。

<pre class="code"><code><span class="tag">&lt;?xml version=&quot;1.0&quot; encoding=&quot;UTF-8&quot;?&gt;</span> 
<span class="tag">&lt;!DOCTYPE struts PUBLIC 
  &quot;-//Apache Software Foundation//DTD Struts Configuration 2.0//EN&quot; 
  &quot;http://struts.apache.org/dtds/struts-2.0.dtd&quot;&gt;</span> 
 
<span class="tag">&lt;struts&gt;</span> 
  <span class="tag">&lt;bean <span class="attr">class=</span><span class="value">&quot;com.hamasyou.struts2.webapp.MyObject&quot;</span> /&gt;</span> 
  <span class="tag">&lt;bean <span class="attr">class=</span><span class="value">&quot;com.hamasyou.struts2.webapp.MyObject2&quot;</span> /&gt;</span>   
<span class="tag">&lt;/struts&gt;</span>
</code></pre>

<pre class="console">myObject:com.hamasyou.struts2.webapp.MyObject@135d18b
myObject2:com.hamasyou.struts2.webapp.MyObject2@50078e</pre>

<h3 id="インターセプタ">インターセプタ</h3>

アクションの呼び出しの前に、<code>ActionInvocation</code> によってインタセプタが呼び出されます。

struts-default.xml を見ると、デフォルトのインターセプタとして以下の設定がされているのがわかります。

<section>

<h4>struts-default.xml のインターセプタの設定</h4>

<pre class="code"><code><span class="tag">&lt;interceptors&gt;</span> 
  <span class="tag">&lt;interceptor <span class="attr">name=</span><span class="value">&quot;alias&quot;</span> <span class="attr">class=</span><span class="value">&quot;com.opensymphony.xwork2.interceptor.AliasInterceptor&quot;</span>/&gt;</span> 
  <span class="tag">&lt;interceptor <span class="attr">name=</span><span class="value">&quot;autowiring&quot;</span> <span class="attr">class=</span><span class="value">&quot;com.opensymphony.xwork2.spring.interceptor.ActionAutowiringInterceptor&quot;</span>/&gt;</span> 
  <span class="tag">&lt;interceptor <span class="attr">name=</span><span class="value">&quot;chain&quot;</span> <span class="attr">class=</span><span class="value">&quot;com.opensymphony.xwork2.interceptor.ChainingInterceptor&quot;</span>/&gt;</span> 
  <span class="tag">&lt;interceptor <span class="attr">name=</span><span class="value">&quot;conversionError&quot;</span> <span class="attr">class=</span><span class="value">&quot;org.apache.struts2.interceptor.StrutsConversionErrorInterceptor&quot;</span>/&gt;</span> 
  <span class="tag">&lt;interceptor <span class="attr">name=</span><span class="value">&quot;cookie&quot;</span> <span class="attr">class=</span><span class="value">&quot;org.apache.struts2.interceptor.CookieInterceptor&quot;</span>/&gt;</span> 
  <span class="tag">&lt;interceptor <span class="attr">name=</span><span class="value">&quot;createSession&quot;</span> <span class="attr">class=</span><span class="value">&quot;org.apache.struts2.interceptor.CreateSessionInterceptor&quot;</span> /&gt;</span> 
  <span class="tag">&lt;interceptor <span class="attr">name=</span><span class="value">&quot;debugging&quot;</span> <span class="attr">class=</span><span class="value">&quot;org.apache.struts2.interceptor.debugging.DebuggingInterceptor&quot;</span> /&gt;</span> 
  <span class="tag">&lt;interceptor <span class="attr">name=</span><span class="value">&quot;externalRef&quot;</span> <span class="attr">class=</span><span class="value">&quot;com.opensymphony.xwork2.interceptor.ExternalReferencesInterceptor&quot;</span>/&gt;</span> 
  <span class="tag">&lt;interceptor <span class="attr">name=</span><span class="value">&quot;execAndWait&quot;</span> <span class="attr">class=</span><span class="value">&quot;org.apache.struts2.interceptor.ExecuteAndWaitInterceptor&quot;</span>/&gt;</span> 
  <span class="tag">&lt;interceptor <span class="attr">name=</span><span class="value">&quot;exception&quot;</span> <span class="attr">class=</span><span class="value">&quot;com.opensymphony.xwork2.interceptor.ExceptionMappingInterceptor&quot;</span>/&gt;</span> 
  <span class="tag">&lt;interceptor <span class="attr">name=</span><span class="value">&quot;fileUpload&quot;</span> <span class="attr">class=</span><span class="value">&quot;org.apache.struts2.interceptor.FileUploadInterceptor&quot;</span>/&gt;</span> 
  <span class="tag">&lt;interceptor <span class="attr">name=</span><span class="value">&quot;i18n&quot;</span> <span class="attr">class=</span><span class="value">&quot;com.opensymphony.xwork2.interceptor.I18nInterceptor&quot;</span>/&gt;</span> 
  <span class="tag">&lt;interceptor <span class="attr">name=</span><span class="value">&quot;logger&quot;</span> <span class="attr">class=</span><span class="value">&quot;com.opensymphony.xwork2.interceptor.LoggingInterceptor&quot;</span>/&gt;</span> 
  <span class="tag">&lt;interceptor <span class="attr">name=</span><span class="value">&quot;modelDriven&quot;</span> <span class="attr">class=</span><span class="value">&quot;com.opensymphony.xwork2.interceptor.ModelDrivenInterceptor&quot;</span>/&gt;</span> 
  <span class="tag">&lt;interceptor <span class="attr">name=</span><span class="value">&quot;scopedModelDriven&quot;</span> <span class="attr">class=</span><span class="value">&quot;com.opensymphony.xwork2.interceptor.ScopedModelDrivenInterceptor&quot;</span>/&gt;</span>
  <span class="tag">&lt;interceptor <span class="attr">name=</span><span class="value">&quot;params&quot;</span> <span class="attr">class=</span><span class="value">&quot;com.opensymphony.xwork2.interceptor.ParametersInterceptor&quot;</span>/&gt;</span> 
  <span class="tag">&lt;interceptor <span class="attr">name=</span><span class="value">&quot;prepare&quot;</span> <span class="attr">class=</span><span class="value">&quot;com.opensymphony.xwork2.interceptor.PrepareInterceptor&quot;</span>/&gt;</span> 
  <span class="tag">&lt;interceptor <span class="attr">name=</span><span class="value">&quot;staticParams&quot;</span> <span class="attr">class=</span><span class="value">&quot;com.opensymphony.xwork2.interceptor.StaticParametersInterceptor&quot;</span>/&gt;</span> 
  <span class="tag">&lt;interceptor <span class="attr">name=</span><span class="value">&quot;scope&quot;</span> <span class="attr">class=</span><span class="value">&quot;org.apache.struts2.interceptor.ScopeInterceptor&quot;</span>/&gt;</span> 
  <span class="tag">&lt;interceptor <span class="attr">name=</span><span class="value">&quot;servletConfig&quot;</span> <span class="attr">class=</span><span class="value">&quot;org.apache.struts2.interceptor.ServletConfigInterceptor&quot;</span>/&gt;</span> 
  <span class="tag">&lt;interceptor <span class="attr">name=</span><span class="value">&quot;sessionAutowiring&quot;</span> <span class="attr">class=</span><span class="value">&quot;org.apache.struts2.spring.interceptor.SessionContextAutowiringInterceptor&quot;</span>/&gt;</span>
  <span class="tag">&lt;interceptor <span class="attr">name=</span><span class="value">&quot;timer&quot;</span> <span class="attr">class=</span><span class="value">&quot;com.opensymphony.xwork2.interceptor.TimerInterceptor&quot;</span>/&gt;</span> 
  <span class="tag">&lt;interceptor <span class="attr">name=</span><span class="value">&quot;token&quot;</span> <span class="attr">class=</span><span class="value">&quot;org.apache.struts2.interceptor.TokenInterceptor&quot;</span>/&gt;</span> 
  <span class="tag">&lt;interceptor <span class="attr">name=</span><span class="value">&quot;tokenSession&quot;</span> <span class="attr">class=</span><span class="value">&quot;org.apache.struts2.interceptor.TokenSessionStoreInterceptor&quot;</span>/&gt;</span> 
  <span class="tag">&lt;interceptor <span class="attr">name=</span><span class="value">&quot;validation&quot;</span> <span class="attr">class=</span><span class="value">&quot;org.apache.struts2.interceptor.validation.AnnotationValidationInterceptor&quot;</span>/&gt;</span> 
  <span class="tag">&lt;interceptor <span class="attr">name=</span><span class="value">&quot;workflow&quot;</span> <span class="attr">class=</span><span class="value">&quot;com.opensymphony.xwork2.interceptor.DefaultWorkflowInterceptor&quot;</span>/&gt;</span> 
  <span class="tag">&lt;interceptor <span class="attr">name=</span><span class="value">&quot;store&quot;</span> <span class="attr">class=</span><span class="value">&quot;org.apache.struts2.interceptor.MessageStoreInterceptor&quot;</span> /&gt;</span> 
  <span class="tag">&lt;interceptor <span class="attr">name=</span><span class="value">&quot;checkbox&quot;</span> <span class="attr">class=</span><span class="value">&quot;org.apache.struts2.interceptor.CheckboxInterceptor&quot;</span> /&gt;</span> 
  <span class="tag">&lt;interceptor <span class="attr">name=</span><span class="value">&quot;profiling&quot;</span> <span class="attr">class=</span><span class="value">&quot;org.apache.struts2.interceptor.ProfilingActivationInterceptor&quot;</span> /&gt;</span> 
  <span class="tag">&lt;interceptor <span class="attr">name=</span><span class="value">&quot;roles&quot;</span> <span class="attr">class=</span><span class="value">&quot;org.apache.struts2.interceptor.RolesInterceptor&quot;</span> /&gt;</span>
</code></pre>

</h4>

インターセプタは、塊ごとに名前をつけて管理することが出来ます。それが、インターセプタスタックと呼ばれるものです。

struts-default.xml には、以下のような設定がされています。

<pre class="code"><code><span class="rem">&lt;!-- Basic stack --&gt;</span> 
<span class="tag">&lt;interceptor-stack <span class="attr">name=</span><span class="value">&quot;basicStack&quot;</span>&gt;</span> 
    <span class="tag">&lt;interceptor-ref <span class="attr">name=</span><span class="value">&quot;exception&quot;</span>/&gt;</span> 
    <span class="tag">&lt;interceptor-ref <span class="attr">name=</span><span class="value">&quot;servletConfig&quot;</span>/&gt;</span> 
    <span class="tag">&lt;interceptor-ref <span class="attr">name=</span><span class="value">&quot;prepare&quot;</span>/&gt;</span> 
    <span class="tag">&lt;interceptor-ref <span class="attr">name=</span><span class="value">&quot;checkbox&quot;</span>/&gt;</span> 
    <span class="tag">&lt;interceptor-ref <span class="attr">name=</span><span class="value">&quot;params&quot;</span>/&gt;</span> 
    <span class="tag">&lt;interceptor-ref <span class="attr">name=</span><span class="value">&quot;conversionError&quot;</span>/&gt;</span> 
<span class="tag">&lt;/interceptor-stack&gt;</span>
</code></pre>

アクションは、次のインターセプタスタックがデフォルトで適用されるようになっています。

<pre class="code"><code><span class="tag">&lt;default-interceptor-ref <span class="attr">name=</span><span class="value">&quot;defaultStack&quot;</span>/&gt;</span></code></pre>

struts-default.xml を見るとわかりますが、これらのインターセプタの設定はパッケージごとに行うようです。上記のインターセプタ、インターセプタスタックは、「<strong>struts-default</strong>」というパッケージに定義されています。

自分たちのアクションを struts.xml に登録した際のパッケージは、struts-default を継承する必要がありそうです。

<section>

<h4>struts.xml の例</h4>

<pre class="code"><code><span class="tag">&lt;package <span class="attr">name=</span><span class="value">&quot;mypackage&quot;</span> <span class="attr">namespace=</span><span class="value">&quot;/mypackage&quot;</span> <span class="attr">extends=</span><span class="value">&quot;struts-default&quot;</span>&gt;</span> 
     
    <span class="tag">&lt;action  
      <span class="attr">name=</span><span class="value">&quot;itemSearch&quot;</span>  
      <span class="attr">class=</span><span class="value">&quot;com.hamasyou.struts2.webapp.usecase1.ItemSearchAction&quot;</span>&gt;</span> 
      <span class="tag">&lt;result <span class="attr">name=</span><span class="value">&quot;success&quot;</span>&gt;</span>/WEB-INF/jsp/top/top.jsp<span class="tag">&lt;/result&gt;</span> 
      <span class="tag">&lt;interceptor-ref <span class="attr">name=</span><span class="value">&quot;debugging&quot;</span> /&gt;</span> 
    <span class="tag">&lt;/action&gt;</span> 
     
    <span class="tag">&lt;action  
      <span class="attr">name=</span><span class="value">&quot;itemRegister&quot;</span>  
      <span class="attr">class=</span><span class="value">&quot;com.hamasyou.struts2.webapp.usecase2.admin.ItemRegisterAction&quot;</span>&gt;</span> 
      <span class="tag">&lt;result <span class="attr">name=</span><span class="value">&quot;success&quot;</span>&gt;</span>/WEB-INF/jsp/top/top.jsp<span class="tag">&lt;/result&gt;</span> 
      <span class="tag">&lt;interceptor-ref <span class="attr">name=</span><span class="value">&quot;executeAndWaitStack&quot;</span> /&gt;</span> 
    <span class="tag">&lt;/action&gt;</span>     
         
  <span class="tag">&lt;/package&gt;</span>
</code></pre>

</section>

ここでは、インターセプタを指定しているので、デフォルトのインターセプタ（defaultStack）は使われません。

<h3 id="アクションの作成">アクションの作成</h3>

Struts2 のアクションクラスは POJO で作成することができます。デフォルトでは、execute メソッドを持つクラスを作成すればアクションとして実行することができます。

<pre class="code"><code><span class="keyword">public</span> <span class="keyword">class</span> MyAction { 
  <span class="keyword">public</span> String execute() <span class="keyword">throws</span> Exception { 
    <span class="keyword">return</span> <span class="literal">"success"</span>; 
  } 
}
</code></pre>

アクションの呼び出しは、通常、「struts.xml に記述した &lt;action&gt; の名前 + &quot;.action&quot;」ですが、「struts.xml に記述した &lg;action&gt; の名前 + &quot;!メソッド名.action&quot;」とすることで、execute 以外のメソッドを呼び出すことができます。

また、struts.xml の &lt;action&gt; の設定で、method プロパティを設定することで、execute 以外のメソッドを呼び出すこともできます。

<pre class="code"><code><span class="tag">&lt;action  
    <span class="attr">name=</span><span class="value">&quot;itemRegister&quot;</span>  
    <span class="attr">method=</span><span class="value">&quot;hoge&quot;</span> 
    <span class="attr">class=</span><span class="value">&quot;com.hamasyou.struts2.webapp.usecase2.admin.ItemRegisterAction&quot;</span>&gt;</span> 
    <span class="tag">&lt;result <span class="attr">name=</span><span class="value">&quot;success&quot;</span>&gt;</span>/WEB-INF/jsp/top/top.jsp<span class="tag">&lt;/result&gt;</span> 
<span class="tag">&lt;/action&gt;</span>
</code></pre>

<dl>
<dt class="info">Zero コンフィグレーション</dt>
<dd>クラス名を「Action」で終わる名前にしておくことで、Zero コンフィグレーションを利用して、無設定で Action クラスとして実行することができます。</dd> 
</dl>

<h3 id="Result について">Result について</h3>

以下、後で書く




