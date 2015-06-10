---
layout: post
title: "Apache Pluto1.1 を触ってみる その2"
date: 2006-04-19 00:08
comments: true
categories: [Blog]
keywords: "Portlet, Java, Portal, JSR-168, Pluto, Apache Pluto, その2"
tags: [Apache,Java]
author: hamasyou
amazon_url: ""
amazon_author: ""
amazon_image: ""
amazon_publisher: ""
---

<p>
<a href="http://portals.apache.org/pluto/" rel="external nofollow"></a>
</p>

Apache Pluto の覚書きです。Apache Pluto は、Java Portlet 仕様 のリファレンス実装です。この覚書きを書いている段階では、Java Portlet Specification (JSR-168) が Java Portlet 仕様として、まとめられています。

この覚書きで使っている環境は次の通りです。

<ul><li>Eclipse 3.1.2</li><li>Pluto 1.1 (alpha release 以前)</li><li>Tomcat 5.5.9</li><li>J2SE 5.0</li></ul>

参考にしたリソースは次の通りです。<ul><li><a href="http://jcp.org/aboutJava/communityprocess/final/jsr168/index.html" rel="external nofollow">Developing Portlets with Apache Pluto</a></li></ul>

Pluto の中を追っていったときの覚書です。ソースコードを手元に読んでいただければ、より理解できるのではないかと思います。

間違い等に気づいた方は、ご連絡いただけると助かります。


<!-- more -->

<h2>Apache Pluto1.1 覚書 目次</h2>

<ul>
<li><a href="#chapter1" rel="external nofollow">環境構築</a>
<ul>
<li><a href="#section1" rel="external nofollow">ソースコードをダウンロードする</a></li>
<li><a href="#section2" rel="external nofollow">インストールする</a></li>
</ul>
</li>
<li><a href="#chapter2" rel="external nofollow">Pluto のアーキテクチャ</a>
<ul>
<li><a href="#section3" rel="external nofollow">ポータルもWebアプリケーションだから、サーブレット が使われている</a></li>
<li><a href="#section4" rel="external nofollow">Pluto で使われる主なクラス群</a></li>
<li><a href="#section5" rel="external nofollow">ポートレットコンテナにポートレットを登録するにはどうすればいいのか？</a></li>
<li><a href="#section6" rel="external nofollow">ポートレットを呼び出すぞ。（でも、URL がわからない・・・）</a></li>
<li><a href="#section7" rel="external nofollow">Pluto の設定には、SpringFramework が使われている</a></li>
</ul>
</li>
<li><a href="#chapter3" rel="external nofollow">まとめ</a></li>
</ul>

<h4>Pluto をもうちょっと詳しく見てみる 目次</h4>

<ul>
<li><a href="#chapter4" rel="external nofollow">PortalStartupListener の動作</a>
</li>
<li><a href="#chapter5" rel="external nofollow">PortletDriverServlet の動作</a>
<ul>
<li><a href="#section8" rel="external nofollow">リクエスト種類によって、呼び出しが変わる</a></li>
</ul>
</li>
<li><a href="#chapter6" rel="external nofollow">ポートレットコンテナの動作</a>
<ul>
<li><a href="#section9" rel="external nofollow">ポートレットコンテナの呼び出しを整理しておくと</a></li>
<li><a href="#section10" rel="external nofollow">ポートレットの呼び出し方法</a></li>
</ul>
</li>
<li><a href="#chapter7" rel="external nofollow">エラーで困ったら</a>
<ul>
<li><a href="#section11" rel="external nofollow">error.config.context.null</a></li>
</ul>
</li>
<li><a href="#chapter8" rel="external nofollow">検討事項</a></li>
</ul>

<h2 id="chapter1">環境構築</h2>

今回使うのは、Apache Pluto1.1 です。2006/4/20 現在では、 Pluto のバージョンは 1.0.1 と 1.1 の二つが用意されています。バイナリで配布されているのは 1.0.1 だけなので、今回はソースコードを手に入れるところからはじめます。

Pluto 1.0.1 と Pluto 1.1 は、アーキテクチャが大きく変わったようです。1.1 のほうがよりシンプルに美しくなりました。それに伴って、設定の仕方なども色々と変わっています。 Pluto 1.0.1 を使う場合は、この覚書は参考にならないかもしれませんので注意してください。

今回使うのは、Pluto1.1 です。

<h4>ライセンス</h4>

The Apache Software License, Version 2.0

<h3 id="section1">ソースコードをダウンロードする</h3>

Apache Pluto 1.1 のソースコードは、<a href="http://subversion.tigris.org/" rel="external nofollow">Subversion</a> で管理されています。下記の URL からチェックアウトできます。

<pre><a href="http://svn.apache.org/repos/asf/portals/pluto/trunk/" rel="external nofollow"> http://svn.apache.org/repos/asf/portals/pluto/trunk/</a></pre>

下のコマンドを実行することでも、ソースコードがダウンロードできます。

<pre class="console">> <kbd>svn checkout http://svn.apache.org/repos/asf/portals/pluto/trunk/ pluto-site</kbd></pre>

<h3 id="sectiono2">インストールする</h3>

ソースコードをダウンロードしたら、Apache Pluto のサイトにある <a href="http://portals.apache.org/pluto/getting-started.html" rel="external nofollow">Getting Started</a> を見ながら、インストールを行います。ソースコードをダウンロードしたパスに移動して

<pre class="console">> <kbd>mvn install</kbd></pre>

を実行すると、ずらずらと、ライブラリができるので、Getting Started に書いてある Step にしたがって配置していきます。

配置が終了したら、Tomcat を起動します。<a href="http://localhost:8080/pluto/portal" rel="external nofollow">http://localhost:8080/pluto/portal</a> にアクセスすると、Pluto のポータル画面が表示されます。

<h2 id="chapter2">Pluto のアーキテクチャ</h2>

<h3 id="sectiono3">ポータルもWebアプリケーションだから、サーブレット が使われている</h3>

まず、最初に Pluto がどういう風に動作するのかを簡単にまとめて見ました。（画像をクリックすると大きな画像が表示されます。）

<a href="http://hamasyou.com/images/portal_pluto/pluto_class.gif" rel="external nofollow"></a>

Pluto は、Web ブラウザからリクエストを受けると次のように動作します。

<pre>
1. PortalDriverServlet がポータルに対するリクエストを受け取る
        ↓
2. サーブレットコンテキスト、サーブレットリクエスト、サーブレットレスポンスをそれぞれポートレット API でラップする
        ↓
3. ポートレットコンテナを呼び出す
        ↓
4. ポートレットコンテナは、リクエストパラメータを解析して、どのポートレットが呼び出されたのかを判断する
        ↓
5. 呼び出されたポートレットにリクエストをフォワードする
        ↓
6. ポートレットコンテナは、レンダリングリクエストを表示するページ内の全ポートレットに対して送信する
        ↓
7. ポートレットのレスポンスをマージして、画面をブラウザに返す
</pre>

ポータルアプリケーションもひとつの Web アプリケーションなので、当然サーブレットを使っています。

Tomcat を起動した際に webapp ディレクトリにできる pluto フォルダの下には、典型的な Web アプリケーションの構成ができています。

<img src="http://hamasyou.com/images/portal_pluto/pluto_dir.gif" alt="pluto   のディレクトリ構成" />

{TOMCAT_HOME}/webapps/pluto/WEB-INF/web.xml を開くと、こんな記述があると思います。

<section>

<h4>web.xml 抜粋</h4>

<pre class="code"><code><span class="tag">&lt;servlet&gt;</span>  
  <span class="tag">&lt;servlet-name&gt;</span>plutoPortalDriver<span class="tag">&lt;/servlet-name&gt;</span>  
  <span class="tag">&lt;display-name&gt;</span>Pluto Portal Driver<span class="tag">&lt;/display-name&gt;</span>  
  <span class="tag">&lt;description&gt;</span>Pluto Portal Driver Controller<span class="tag">&lt;/description&gt;</span>  
  <span class="tag">&lt;servlet-class&gt;</span>org.apache.pluto.driver.PortalDriverServlet<span class="tag">&lt;/servlet-class&gt;</span>  
<span class="tag">&lt;/servlet&gt;</span>
</code></pre>

</section>

このクラスが、ポータルアプリケーションの窓口、ポータルアプリケーションのサーブレットです。ポータルにアクセスしたいときは、このサーブレットにアクセスすることになります。web.xml の下のほうにマッピングが書かれていますので、見てみます。

<section>

<h4>web.xml 抜粋</h4>

<pre class="code"></code><span class="tag">&lt;servlet-mapping&gt;</span>  
  <span class="tag">&lt;servlet-name&gt;</span>plutoPortalDriver<span class="tag">&lt;/servlet-name&gt;</span>  
  <span class="tag">&lt;url-pattern&gt;</span>/portal/*<span class="tag">&lt;/url-pattern&gt;</span>  
<span class="tag">&lt;/servlet-mapping&gt;</span>
</code></pre>

</section>

つまり、http://localhost:8080/pluto/portal/ にアクセスすることで、このサーブレットが呼ばれ、ポートレットコンテナに格納されているポートレットが呼び出されるわけです。

なお、ポートレットの初期化は、サーブレットリスナで行われており、<code>org.apache.pluto.driver.PortalStartupListener</code> が使われていました。これについては後の節で詳しく見ることにします。

<h3 id="sectiono4">Pluto で使われる主なクラス群</h3>

Pluto のアーキテクチャを調べた中で出てきた主なクラスは次の4つです。

<dl>
<dt>org.apache.pluto.driver.PortletDriverServelt</dt>
<dd><p>このクラスは、Pluto の本体ともいえるサーブレットの実装クラスです。Web ブラウザからのアクセスは、このサーブレットが受け取ります。このクラスは、Pluto 全体の初期化やポートレットコンテナの作成と初期化を行います。</p></dd>
<dt>org.apache.pluto.PortletContainer</dt>
<dd><p>ポートレットコンテナの実装クラスです。PortletDriverServlet から呼び出されます。内部にはポートレットを格納しています。リクエストを解釈し、適切なポートレットに割り振ることを行います。</p></dd>
<dt>org.apache.pluto.core.PortletServlet</dt>
<dd><p>ポートレットアプリケーション側に配備されるクラスです。ポートレットコンテナからの呼び出しを受け、ポートレットに処理を委譲します。</p></dd>
<dt>org.apache.pluto.driver.tags.PortletRenderTag</dt>
<dd><p>ポートレットのレンダリングを呼び出すためのタグクラスです。JSP に記述され、ポートレットのレンダリングリクエストを発行します。<code>&lt;pluto:render /&gt;</code> であらわされます。</p></dd>
</dl>

<dl>
<dt class="notice">クロスコンテキストを有効にする必要がある</dt>
<dd>ポータルは、複数のWeb アプリケーションをまたがるため、クロスコンテキスト(crossContext) が可能でなければなりません。{TOMCAT_HOME}/conf/Catalina/localhost にコピーした pluto.xml には、<code>crossContext=&quot;true&quot;</code> が記述されています。</dd>
</dl>

<h3 id="sectiono5">ポートレットコンテナにポートレットを登録するにはどうすればいいのか？</h3>

{TOMCAT_HOME}/webapps/pluto/WEB-INF/web.xml を見ると、いくつかのサーブレットの設定に <code><strong>org.apache.pluto.core.PortletServlet</strong></code> というクラスが使われています。サーブレットの <code>init-param</code> 設定で、<code><strong>portlet-name</strong></code> というパラメータが設定されています。

<pre class="code"><code><span class="tag">&lt;servlet&gt;</span>  
  <span class="tag">&lt;servlet-name&gt;</span>AdminPortlet<span class="tag">&lt;/servlet-name&gt;</span>  
  <span class="tag">&lt;servlet-class&gt;</span>org.apache.pluto.core.PortletServlet<span class="tag">&lt;/servlet-class&gt;</span>  
  <span class="tag">&lt;init-param&gt;</span>  
    <span class="tag">&lt;param-name&gt;</span>portlet-name<span class="tag">&lt;/param-name&gt;</span>  
    <span class="tag">&lt;param-value&gt;</span>AdminPortlet<span class="tag">&lt;/param-value&gt;</span>  
  <span class="tag">&lt;/init-param&gt;</span>  
  <span class="tag">&lt;load-on-startup&gt;</span>1<span class="tag">&lt;/load-on-startup&gt;</span>  
<span class="tag">&lt;/servlet&gt;</span>
</code></pre>

どうやら、Pluto1.1 では、ポートレットコンテナ がこの <code>PotletServlet</code> を呼び出して、さらに <code>PortletServlet</code> が ポータルコンテンツ を呼び出す仕組みになっているようです。呼び出される ポータルコンテンツは、<code>portlet-name</code> で指定しています。

<code>portlet-name</code> で指定されているのは、あくまでポートレット名です。では、ポートレットの本体はどこで定義するのかというと、JSR-168 で決められている通り、portlet.xml に定義します。あたりまえですね。

web.xml をもう少し見ていくと、今度は、<code>servlet-mapping</code> が見つかります。

<pre class="code"><code><span class="tag">&lt;servlet-mapping&gt;</span>  
  <span class="tag">&lt;servlet-name&gt;</span>AdminPortlet<span class="tag">&lt;/servlet-name&gt;</span>  
  <span class="tag">&lt;url-pattern&gt;</span>/PlutoInvoker/AdminPortlet<span class="tag">&lt;/url-pattern&gt;</span>  
<span class="tag">&lt;/servlet-mapping&gt;</span>
</code></pre>

<code>/PlutoInvoker/</code> という記述がなんとも不思議です。Pluto 1.1 は、<code>PortletServlet</code> が呼び出される URL にプレフィックスとして、この <code>/PlutoInvoker/</code> という名前を使います。

Portlet 用のサーブレットは、マッピング URL に <code>/PlutoInvoker/</code> を記述するようにします。その後ろの名前は、ポートレット名をつけます。

ちなみに、web.xml と portal.xml の設定は、ポートレットアプリケーション（自分が作るアプリケーション）側に記述します。

<h3 id="sectiono6">ポートレットを呼び出すぞ。（でも、URL がわからない・・・）</h3>

さて、これでポートレットの設定は完了したわけですが、これだけではポートレットは呼び出せません。JSR-168 には、

{% blockquote JSR-168 %}
Portlets are not directly bound to a URL


{% endblockquote %}

とあります。これはつまり、ブラウザのアドレスバーに URL を直接打ち込んでもポートレットを呼び出すことはできませんということを言っています。例えば、ブラウザのアドレスに http://localhost:8080/pluto/PortletInvoker/AdminPortal 入力してもエラーになります。

では、どうすればよいかというと、ポートレットは、ポートレットコンテナから呼び出されることが決められているので、ポートレットコンテナに呼び出しをお願いします。Pluto1.1 の場合、このお願いは {TOMCAT_HOME}/webapps/pluto/WEB-INF/pluto-portal-driver-config.xml に記述することになります。

<section>

<h4>(pluto-portal-driver-config.xml 抜粋)</h4>

<pre class="code"><code><span class="tag">&lt;pluto-portal-driver </span>  
  ...  
  <span class="tag">&lt;portlet-app&gt;</span>  
    <span class="tag">&lt;context-path&gt;</span>/pluto<span class="tag">&lt;/context-path&gt;</span>  
    <span class="tag">&lt;portlets&gt;</span>  
      <span class="tag">&lt;portlet <span class="attr">name=</span><span class="value">&quot;AboutPortlet&quot;</span> /&gt;</span>  
      <span class="tag">&lt;portlet <span class="attr">name=</span><span class="value">&quot;AdminPortlet&quot;</span> /&gt;</span>  
    <span class="tag">&lt;/portlets&gt;</span>  
  <span class="tag">&lt;/portlet-app&gt;</span>  
  ...  
  <span class="tag">&lt;render-config <span class="attr">default=</span><span class="value">&quot;Pluto Admin&quot;</span>&gt;</span>  
    <span class="tag">&lt;page <span class="attr">name=</span><span class="value">&quot;Pluto Admin&quot;</span> <span class="attr">uri=</span><span class="value">&quot;/WEB-INF/themes/pluto-default-theme.jsp&quot;</span>&gt;</span>  
      <span class="tag">&lt;portlet <span class="attr">context=</span><span class="value">&quot;/pluto&quot;</span> <span class="attr">name=</span><span class="value">&quot;AdminPortlet&quot;</span>/&gt;</span>  
    <span class="tag">&lt;/page&gt;</span>  
  <span class="tag">&lt;/render-config&gt;</span>  
<span class="tag">&lt;/pluto-portal-driver&gt;</span>
</code></pre>

</section>

<section>

<h4>portlet-app タグ</h4>

このタグでポートレットの定義を記述します。

<dl>
<dt>context-path タグ</dt><dd><p>ここに、ポートレットのコンテキストパスを記述します。ポートレットは別の Web アプリケーションとして作成できるので、コンテキストも Pluto とは別になります。コンテキスト名に使える文字は 「a-z, A-Z, 0-9, _, /」 です。'.' や '-' は使えないので注意です。</p></dd>
<dt>portlets タグ</dt><dd><p>このタグを使って、このコンテキストに含まれるポートレットの名前を定義します。ここに記述するのは、portlet.xml に定義したポートレットです。</p></dd>
</dl>

</section>

<section>

<h4>render-config タグ</h4>

このタグは、<code>PlutoPortletDriver</code> の設定を記述するところです。ここには、レンダリングに関する設定をおこないます。設定しているのは、ポータルページを呼び出すときのパス名（name 属性）と、ポータルページに使用するページ（uri 属性）、どのポートレットを使うか（portlet タグ）です。portlet タグに記述するのは、<code>portlet-app</code> で記述したコンテキストと、ポートレット名です。

name に指定した値 (ページ名) をブラウザのアドレスバーに打ち込むことで、ポータルページを開くことができます。

<pre>http://localhost:8080/pluto/portal/Pluto%20Admin</pre>

</section>

これで、Pluto を起動したときに、pluto-portal-driver-config.xml が読み込まれ、ポートレット URLを生成したページが表示できるようになります。Pluto は、この設定ファイルを解析して、URL と ポートレットアプリケーションのマッピングを行います。

http://localhost:8080/pluto/portal にアクセスして、ログインした後のページの左上にある Navigation から ページを移動してみましょう。画面が表示されるはずです。

<dl>
<dt class="notice">ポートレットID</dt>
<dd>
<p>すべてのポートレットには、一意の ID が振られます。Pluto の場合、次のようなルールによって ID が決められます。</p>

<pre>[portlet-context-name].[portlet-name]</pre>

<p><code>[portlet-context-name]</code> とは、ポートレットアプリケーションのコンテキスト名のことです。<code>[portlet-name]</code> は、文字通りポートレット名です。これらを '.' でつないだものが <strong>Portlet Definition ID</strong> と呼ばれるポートレットの一意な ID です。</p></dd>
</dl>

<h3 id="sectiono7">Pluto の設定には、SpringFramework が使われている</h3>

{TOMCAT_HOME}/webapps/pluto/WEB-INF/ を見ると、pluto-portal-driver-service-config.xml というファイルがあるのがわかります。このファイル、中を見ると実は SpringFramework の Bean 設定ファイルだったりします。

Pluto の設定が行われている部分を抜粋します。

<section>

<h4>(pluto-portal-driver-service-config.xml 抜粋)</h4>

<pre class="code"><code><span class="tag">&lt;bean <span class="attr">id=</span><span class="value">&quot;DriverConfiguration&quot;</span>  
  <span class="attr">class=</span><span class="value">&quot;org.apache.pluto.driver.config.impl.DriverConfigurationImpl&quot;</span>&gt;</span>  
  <span class="rem">&lt;!-- ===== Portal Services ===== --&gt;</span>  
  <span class="tag">&lt;constructor-arg&gt;</span><span class="tag">&lt;ref <span class="attr">local=</span><span class="value">&quot;PropertyConfigService&quot;</span>/&gt;</span><span class="tag">&lt;/constructor-arg&gt;</span>  
  <span class="tag">&lt;constructor-arg&gt;</span><span class="tag">&lt;ref <span class="attr">local=</span><span class="value">&quot;PortletRegistryConfig&quot;</span>/&gt;</span><span class="tag">&lt;/constructor-arg&gt;</span>  
  <span class="tag">&lt;constructor-arg&gt;</span><span class="tag">&lt;ref <span class="attr">local=</span><span class="value">&quot;RenderConfigService&quot;</span>/&gt;</span><span class="tag">&lt;/constructor-arg&gt;</span>  
 
  <span class="rem">&lt;!-- === Container Services === --&gt;</span>  
  <span class="tag">&lt;constructor-arg&gt;</span><span class="tag">&lt;ref <span class="attr">local=</span><span class="value">&quot;PortalCallbackService&quot;</span>/&gt;</span><span class="tag">&lt;/constructor-arg&gt;</span>  
 
  <span class="rem">&lt;!--  Optional Container Services --&gt;</span>  
  <span class="rem">&lt;!--  
  &lt;property name=&quot;portletPreferencesService&quot; &gt; 
    &lt;ref local=&quot;PortletPreferencesService&quot; /&gt; 
  &lt;/property&gt;  
  &lt;property name =&quot;userAttributeService&quot; &gt; 
    &lt;ref local=&quot;UserAttributeService&quot; /&gt; 
  &lt;/property&gt;  
  --&gt;</span>  
<span class="tag">&lt;/bean&gt;</span>
</code></pre>

</section>

4つのサービスクラスをインジェクションしています。Portal Services とコメントされているサービスクラスは、pluto-portal-driver-service-config.xml を読み込んで、それぞれの担当範囲の設定を返す役割を持っています。

Container Services とコメントされているサービスクラスは、ポートレットコンテナから必要に応じて呼び出されるクラスです。例えば、ポータルのタイトルを設定するときに呼び出されたり、ポートレット URL を生成するクラスの取得に使われたりします。

Pluto の実装を拡張したものに、<a href="http://portals.apache.org/jetspeed-2/" rel="external nofollow">uPortal</a> があります。これらのサービスクラスを拡張して作られています。

<h2 id="chapter3">まとめ</h2>

<ul><li>ポータルはただのWeb アプリケーション</li>
<li>ポートレットもただのWeb アプリケーション</li>
<li>ポータルアプリケーションとポートレットアプリケーションは別々に開発できる</li>
<li>ポータルアプリケーションの設定は{PORTLET_APP}/WEB-INF/portlet.xml を作成することで行う</li>
<li>ポートレットコンテナに対するポートレットの追加・変更や削除は {PLUTO_HOME}/WEB-INF/pluto-portal-driver-config.xml に記述する</li>
<li>画面デザインの変更は、{PLUTO_HOME}/WEB-INF/themes/pluto-default-theme.jsp、portlet-skin.jsp を変更することで行う</li>
<li>ポートレットコンテナの初期化に関する設定は、{PLUTO_HOME}/WEB-INF/pluto-portal-driver-service-config.xml に記述する</li>
</ul>

さらに、ポートレットアプリケーションの開発者がやらなければならないのは、次の点です。

<dl>
<dt class="info">Portlet 開発者がやる作業</dt>
<dd><ol><li>javax.portlet.GenericPortlet を継承したポートレットを作成する</li>
<li>/WEB-INF/portlet.xml を作成し、ポートレットの定義を行う</li>
<li>web.xml に org.apache.pluto.core.PortletServlet を設定し、<code>portal-name</code> を記述する</li>
<li>Pluto の pluto-portal-driver-config.xml に、ポートレットの設定を記述する</li></ol></dd>
</dl>

<h2 id="chapter4">PortalStartupListener の動作</h2>

もうちょっと突っ込んで Pluto を見てみます。といっても、<code>PortalDriverServlet</code> と <code>PortletContainer</code> の動きを追っていくだけですが。

<code>PortalStartupListener</code> の中で、Pluto で使われるいろいろな初期設定が行われます。このクラスは、Pluto がアプリケーションサーバにロードされると起動します。デフォルトの実装では、次の3つが行われます。

<section>

<h4>デフォルトの設定メソッド</h4>

<ul><li>initDriverConfiguration(servletContext);</li>
<li>initAdminConfiguration(servletContext);</li>
<li>initContainer(servletContext);</li>
</ul>

順番に見ていきます。まずは、<code>initDriverConfiguration</code> です。

このメソッドは、<code>DriverConfiguration</code> の生成と初期化を行います。

最初に <code>DriverConfigrationFactory</code> クラスが呼び出されます。 Factory の中で、SpringFramework が呼び出され、 pluto-portal-driver-service-config.xml の内容を元に <code>DriverConfigration</code> が作成されます。

あとは、<code>DriverConfiguration</code> に設定されたサービスクラスの <code>init</code> メソッドが呼び出され、pluto-portal-driver-config.xml のパースを行うという仕組みです。

生成された <code>DriverConfiguration</code> は、<code>ServletContext</code> に <code>&quot;driverConfig&quot;</code>  という名前で格納されます。

つぎに、<code>AdminConfiguration</code> の生成と初期化が行われます。

ほとんどの処理が、<code>DriverConfiguration</code> の同じです。違うのは、<code>ServletContext</code> に格納するときのキーが違うくらいです。 

&quot;driverAdminConfig&quot;</code> という値で格納されます。

<code>AdminConfiguration</code> は、ポートレットの追加や削除がシステム的に行えるものです。ポートレットを管理するツールを作るときに使えそうです。

最後に ポートレットコンテナの生成と初期化です。

ここも大したことをしていません。<code>DriverConfiguration</code> をもとに、コンテナの実装クラスを生成しているだけです。

生成したコンテナは、<code>ServletContext</code> に <code>&quot;portletContainer&quot;</code> というキーで格納されます。

これで Pluto の初期化が完了しました。あとは、<code>PortletDriverServlet</code> と <code>PortletContainer</code> の動作がわかれば、一通り Pluto の動作を追えるかなと思います。

<h2 id="chapter5">PortletDriverServlet の動作</h2>

このクラスは、何をやっているかというと、サーブレット API を ポートレット API にラップしてポートレットコンテナを呼び出すということを行っています。

ポータルは、リクエストパラメータの値によって呼び出すポートレットを判断します。リクエストパラメータとポートレットのマッピングルールは、ポータル製品ごとに決められています。

Pluto 1.1 は、<code>PortalURLParser</code> クラスの <code>parse</code> メソッドで変換ルールを規定しています。ここで、リクエスト URL をポータルで使える形に変換しています。

<h3 id="sectiono8">リクエスト種類によって、呼び出しが変わる</h3>

ポータルに対するリクエストには種類があります。一つはアクションリクエストで、ポートレットの <code>processAction</code> メソッドが呼び出されます。もう一つはレンダリングリクエストで、ポートレットの <code>render</code> メソッドが呼び出されます。

アクションリクエストの場合、ポートレットコンテナの <code>doAction</code> が呼び出されます。これは、ポートレットの呼び出しを行うメソッドになっています。

レンダリングリクエストの場合、画面の描画が行われます。このとき、{PLUTO_HOME}/WEB-INF/themes/pluto-default-theme.jsp の表示されます。

<h2 id="chapter6">ポートレットコンテナの動作</h2>

<h3 id="sectiono9">ポートレットコンテナの呼び出しを整理しておくと</h3>

JSR-168 では、アクションURL がリクエストされたときには、対応するポートレットの <code>processAction</code> メソッドの呼び出しと、ポータルページに含まれるすべてのポートレットの <code>render</code> メソッドが呼び出されるとされています。

{% blockquote JSR-168 %}
If the client request is triggered by an action URL, the portal/portlet-container must first trigger the action request by invoking the <i>processAction</i> method of the targeted portlet. &lt;中略&gt; Then, the portal/portlet-container must trigger the render request by invoking the <i>render</i> method for all the portlets in the portal page with the possible exception of portlets for which their content is being cached.


{% endblockquote %}

<code>PortletDriverServlet</code> から呼び出されるのは、ポートレットコンテナの <code>doAction</code> メソッドです。この <code>doAction</code> メソッドの中でリクエストを解析し、どのポートレットを呼び出すかを決めます。ポートレットの呼び出しには、サーブレットレスポンスの <code>sendRedirect</code> を使っています。

Pluto は、Portlet のレンダリングトリガーを &lt;pluto:render/&gt; タグで行っています。

<h3 id="sectiono10">ポートレットの呼び出し方法</h3>

ポートレットコンテナが、どうやってポートレットを呼び出すかですが、上に書いたようにサーブレットレスポンスの <code>sendRedirect</code> を使っています。このリダイレクト URL のパラメータに、ポートレットコンテナが受け取ったリクエストパラメータ等の情報をエンコードして付与しています。

そして、呼び出されたポートレットプリケーション側で、待ち構えていた <code>org.apache.pluto.core.PortletServlet</code> クラスがパラメータをデコードして、サーブレットリクエストを完成させます。

これで、ポートレットの呼び出しは完了です。

<dl>
<dt class="notice">クロスコンテキストを有効にする！</dt>
<dd>ポートレットの呼び出しを行うには、クロスコンテキストを有効にしておかなければなりません。</dd>
</dl>

ポータルは、異なるコンテキスト間のポートレットを呼び出します。そのため、コンテキストをまたいだ呼び出しが可能なように設定を行う必要があります。

Tomcat の場合、これは、クロスコンテキスト（crossContext）というパラメータで行います。{TOMCAT_HOME}/conf/server.xml に記述するコンテキストの設定に <code>crossContext</code> を設定します。

<h2 id="chapter7">エラーで困ったら</h2>

<h3 id="sectiono11">error.config.context.null</h3>

<pre>org.apache.pluto.PortletContainerRuntimeException: error.config.context.null</pre>

こんなエラーで困ったら、<code>crossContext</code> を true に設定したかどうかを疑ってください。また、コンテキスト名が間違っていないか確認してください。

<h2 id="chapter8">検討事項</h2>

TBD





