---
layout: post
title: "Apache Pluto1.1 を触ってみる その1"
date: 2006-04-16 17:52
comments: true
categories: [Blog]
keywords: "Portlet, Java, Portal, JSR-168, Pluto, Apache Pluto, その1"
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

Apache Pluto の覚書きです。Apache Pluto は、Java Portlet 仕様 のリファレンス実装です。この覚書きを書いている段階では、Java Portlet Specification (JSR-168) が Java Portlet 仕様として、まとめられています。今回は、Java Portlet Specification を読んだ覚書きを記録していこうと思います。その後で、Apache Pluto を触った覚書を記録していこうと思います。

この覚書きで使っている環境は次の通りです。

<ul><li>Eclipse 3.1.2</li><li>Java Portlet Specification Version 1.0</li><li>Pluto 1.1 (alpha release 前のもの)</li><li>Tomcat 5.5.9</li><li>J2SE 5.0</li></ul>

参考にしたリソースは次の通りです。

<ul><li><a href="http://jcp.org/aboutJava/communityprocess/final/jsr168/index.html" rel="external nofollow">Java Portlet Specification Version1.0 (JSR-168)</a></li>
<li><a href="http://www.developer.com/java/web/article.php/3554396" rel="external nofollow">Developing Portlets with Apache Pluto</a></li>
<li><a href="http://www.onjava.com/pub/a/onjava/2006/02/01/what-is-a-portlet-2.html?page=1" rel="external nofollow">ONJava.com - What Is a Portlet, Part 2</a></li></ul>

間違い等に気づいた方は、ご連絡いただけると助かります。（英語が苦手なもので --;）


<!-- more -->

<h2>Java Portlet Specification (JSR-168)</h2>

<h3>目次</h3>

<ol>
<li><a href="#言葉の定義（What is ○○?）" rel="external nofollow">言葉の定義（What is ○○?）</a></li>
<li><a href="#Servlet との違いは？" rel="external nofollow">Servlet との違いは？</a></li>
<li><a href="#ポータルページの構成" rel="external nofollow">ポータルページの構成</a></li>
<li><a href="#ポータルのリクエスト処理手順" rel="external nofollow">ポータルのリクエスト処理手順</a></li>
<li><a href="#Portlet API" rel="external nofollow">Portlet API</a></li>
<li><a href="#Dispatching Request to Servlet and JSPs" rel="external nofollow">Dispatching Request to Servlet and JSPs</a></li>
<li><a href="#ユーザ情報" rel="external nofollow">ユーザ情報</a></li>
<li><a href="#セキュリティ" rel="external nofollow">セキュリティ</a></li>
<li><a href="#portlet.xmlの例" rel="external nofollow">portlet.xmlの例</a></li></ol>

<h2 id="言葉の定義（What is ○○?）">言葉の定義（What is ○○?）</h2>

<h3>ポータルとは何か？</h3>

ポータルとは、Personalization（個別化）、Single Sign on（シングルサインオン）、Content aggregation（コンテンツの統合）を行うプレゼンテーション層の Webアプリケーションです。

<h3>Portlet とは何か？</h3>

Portlet とは、ポータルのための Java 技術の一つであり、Portlet コンテナによって管理される、ユーザインターフェースコンポーネントです。

<h3>Portlet コンテナとは何か？</h3>

Portlet コンテナとは、Portlet を管理する実行時環境のことです。Portlet のライフサイクルを管理します。永続化の仕組みや、Portlet の初期化、ポータルページからのリクエストを処理します。

<h3>fragment とは何か？</h3>

Portlet によって生成されるコンテンツは、fragment とよばれます。fragment とは、HTML や XHTML の破片のことです。つまり、完全な HTML ページではなく、例えば一つの div タグからなる要素だったりします。

fragment は、ポータルページに埋め込まれたときに完全なコンテンツになるように、ルールに則って作る必要があります。。このルールは、次のように決められています。

<dl>
<dt class="warn">Warning</dt>
<dd>
<p>Portlet が生成する HTML fragment は、次のタグを含んではいけません： <strong>base</strong>、<strong>body</strong>、<strong>iframe</strong>、<strong>frame</strong>、<strong>frameset</strong>、<strong>head</strong>、<strong>html</strong>、<strong>title</strong></p>

<p>Portlet が生成する XHTML や XHTML-Basic fragment は、次のタグを含んではいけません： <strong>base</strong>、<strong>body</strong>、<strong>iframe</strong>、<strong>head</strong>、<strong>html</strong>、<strong>title</strong></p>

<p>Portlet が生成する fragment がポータルページとしてなじむために、次のタグの使用に注意しなければなりません：<strong>link</strong>、<strong>meta</strong>、<strong>style</strong></p></dd>
</dl>

<h2 id="Servlet との違いは？">Servlet との違いは？</h2>

Portlet は、Servlet と次のような違いがあります。

<ul><li>Portlet の生成するコンテンツは、fragment なので、それ自体では完結したコンテンツにならない</li>
<li>そのため、Portlet と URL とは、直接的な関係を持たない</li>
<li>Web クライアントは、Portlet 単体ではなく、ポータルページとやり取りをする</li>
<li>同時に複数の Portlet がポータルページに存在する</li>
<li>Portlet は、定義されたライフサイクルを持つ</li>
<li>Portlet は、<a href="#Portlet Mode" rel="external nofollow">Window State</a> という定義された状態をもつ</li></ul>

Portlet は Servlet には無い、次の機能をもちます。

<ul><li>設定情報にアクセスする機能を持つ</li>　
<li>Portlet はユーザプロファイルにアクセスできる</li>
<li>Portlet 用のURLを作成する機能を持つ</li>
<li>2つの Session スコープをもつ（<a href="#javax.portlet.PortletSession" rel="external nofollow">詳細後述</a>）</li></ul>

Portlet は、Servlet の次の機能が使えません。

<ul><li>レスポンスの文字エンコードを指定できない</li>
<li>HTML レスポンスヘッダを設定できない</li>
<li>ポータルページへしかページリクエストができない（つまり、Portlet を直接呼び出せない）</li></ul>

同一Portlet アプリケーション内にある Portlet、Servlet、JSP は、ClassLoader、アプリケーションコンテキスト、セッションを共有します。

<h3>Portlet のライフサイクル</h3>

Portlet は次のライフサイクルを持ちます。これらのライフサイクルにあわせて、javax.portlet.Portlet インターフェースに定義された同名のメソッドが呼び出されます

<ol><li>init</li><li>processAction</li><li>render</li><li>destroy</li></ol>

ライフサイクルは、次のように処理されていきます。

<ul><li>Portlet コンテナが起動したとき、もしくは最初に Portlet へのリクエストが発生したときに、Portlet の init メソッドが呼び出されます</li>
<li>後述する <a href="#javax.portlet.PortletURL" rel="external nofollow">ActionURL</a> がリクエストされたときに、processAction メソッドが呼び出されます</li>
<li>processAction の処理が終了した後、もしくは <a href="#javax.portlet.PortletURL" rel="external nofollow">RenderURL</a> がリクエストされたときに、render メソッドが呼び出されます</li>
<li>Portlet インスタンスが不要になったときに、destroy メソッドが呼び出されます</li></ul>

init メソッドは、Portlet インスタンスが初期化される最初の一回だけ呼び出されます。destory メソッドが呼び出されたあとの Portlet インスタンスは、ガーベッジコレクションの対象になります。

<h3 id="Portlet Mode">Portlet Mode</h3>

Portlet の動作モードのことです。下記の3つが決められています。Portletは VIEW モードを必ずサポートしなければなりません。これ以外のモードは、必要に応じてサポートします。カスタムモードもつくることができます。

<dl><dt>VIEW</dt>
<dd>VIEWモードは、コンテンツを静的に画面に表示するモードのことで、一般の閲覧行為にはこのモードが使われます。</dd>
<dt>EDIT</dt>
<dd>EDITモードは、編集系のページに用意されるモードで、CMSなどのPortletは、このモードをサポートします。</dd>
<dt>HELP</dt>
<dd>HELPモードは、Portlet の説明を表示するモードです。</dd></dl>

<h3 id="Window State">Window State</h3>

ポータルページ上のPortletコンテンツは、下記のいずれかの状態を持ちます。

<dl><dt>NORMAL</dt>
<dd>NORMAL 状態はPortletが他のコンテンツと共存している状態です。ポータルページに複数の Portlet が表示されることになります。</dd>
<dt>MAXIMIZED</dt>
<dd>MAXIMIZED 状態は、自分だけがページに表示されている状態です。ポータルページには、自分の Portlet しか表示されません。</dd>
<dt>MINIMIZED</dt>
<dd>MINIMIZED 状態は、自分以外だけがページに表示されている状態です。ポータルページには、自分の Portlet は表示されません。この状態のときは、Portlet はなにもレンダリングするべきではありません。</dd></dl>

<h2 id="ポータルページの構成">ポータルページの構成</h2>

<a href="/images/portal_pluto/portal_page.gif" rel="external nofollow"></a>

<strong>ポータルページ</strong> は、複数の <strong>Portlet Application</strong> で構成されます。Portlet Application は、<strong>コントロール</strong>を持ちます。<a href="#Portlet Mode" rel="external nofollow">Windows State</a> を変更することができます。Portlet Application が生成するコンテンツが <strong>Portlet fragment</strong>です。

<h2 id="ポータルのリクエスト処理手順">ポータルのリクエスト処理手順</h2>

ポータルページは、次のように処理されていきます。ポータルの動作は、次の4つのプロセスに分かれます。

<ol><li>Request Handling</li><li>Action Request</li><li>Render Request</li><li>End of Service</li></ol>

<h3>Request Handling</h3>

Portlet コンテナは、クライアントからのリクエストを受けると、トリガーとなった<a href="#javax.portlet.PortletURL" rel="external nofollow">PortletURL</a> の種類を判別します。 ActionURL の場合は、Portlet の processAction メソッドが呼び出されます。それ以外の場合は、Portlet の render メソッドが呼び出されます。

<a href="/images/portal_pluto/portal_seq.gif" rel="external nofollow"></a>

<h3>Action Request</h3>

Portlet#processAction メソッドが呼び出されます。Portlet に対するリクエストを処理します。<a href="#javax.portlet.PortletURL" rel="external nofollow">PortletURL</a> の種類が RenderURL ではない場合にこのプロセスが処理されます。リクエストに対して、Portlet の状態を変化させたり、ビジネスロジックを実行したりします。Struts のActionのイメージで利用すると良いと思います。ただし、Portlet は、 アクションとレンダリングを区別しているので、processAction ないでは、処理のみを行うようにします。

processAction の引数には、ActionRequest と ActionResponse オブジェクトが渡されます。これは、ServletRequest と ServletResponse とほとんど同じものと考えればよさそうです。ActionRequest には、クライアントから送信されたリクエストパラメータが含まれています。

<dl>
<dt class="notice">リクエストパラメータのライフサイクル</dt>
<dd>リクエストパラメータは、processAction メソッドを抜けると、破棄されてしまいます。つまり、render メソッド内で取得することができません。ただし、RenderURL によってリクエストが呼び出されて、processAction が呼ばれなかった場合は、render メソッド内で、リクエストパラメータを取得することができます。</dd>
</dl>

processAction 内で生成したパラメータで、Render Request に必要なパラメータは、ActionResponse#setRenderParameter メソッドを使って render メソッドまで保持しておくことができます。setRenderParameter メソッドで設定された値は、RenderRequest#getParameter メソッドで取得することができます。これにより、processAction メソッド内で設定したパラメータを render メソッドで取得することが出来ます。なお、setRenderParameter でセットする key と value は、null を許可しません。null の場合は例外がスローされます。

<h3>Render Request</h3>

このプロセスでは、Portlet は、自身の <a href="#Window State" rel="external nofollow">Window State</a> にあわせたコンテンツを出力します。Portlet の render メソッドが呼び出されます。render メソッドの引数の RenderRequest オブジェクトを使って、Portlet の様々な情報にアクセスすることができます。ServletRequest と同じような使い方ができます。

コンテンツを書き出すには、render メソッドの引数に渡される RenderResponse オブジェクトを使います。直接ストリームにコンテンツを書き出す方法や、Servlet や JSP に処理をディスパッチすることもできます。ServletResponse オブジェクトと同じような使い方ができます。

<h3>End of Service</h3>

Portlet コンテナは、Portlet を長い期間ロードしたままにしておかなければならないと言うことは規定されていません。つまり、一度の処理で Portlet は破棄されるかもしれないし、されないかもしれないと言うことです。Portlet が破棄されるときには destroy メソッドが呼び出されます。

<h2 id="Portlet API">Portlet API</h2>

<h3 id="javax.portlet.Portlet">javax.portlet.Portlet</h3>

Portlet クラスが実装するインターフェースです。

<h3 id="javax.portlet.GenericPortlet">javax.portlet.GenericPortlet</h3>

Portletのデフォルト実装です。通常、このクラスを継承して、Portletクラスを作成します。このクラスには、doView、doEdit、doHelp メソッドが用意されています。これらのメソッドは、VIEW モードのときは、doView メソッドという風に、Portal Mode によってレンダリング時に呼び出されます。

<h3 id="javax.portlet.PortletURL">javax.portlet.PortletURL</h3>

Portletは、PortletURLオブジェクトを使ってPortlet のURLを表します。PortletURL オブジェクトは、<a href="#javax.portlet.RenderResponse" rel="external nofollow">javax.portlet.RenderResponse </a>によって生成されます。PortletURLは、つぎの2つの種類があります。

<ul><li><strong>Action URL</strong><br />　Portlet#processAction のトリガーになるURLです。</li>
<li><strong>Render URL</strong><br />　Portlet#render のトリガーになるURLです。このURLが呼び出されたときには、Portlet の processAction は呼び出されません。</li></ul>

<h3 id="javax.portlet.PortletContext">javax.portlet.PortletContext</h3>

Portlet 版 ServletContext です。Portlet コンテナにアクセスする方法を提供します。ここに格納された情報は、同一ポートレットアプリケーション（同一のwarファイルに含まれているPortlet群）のすべてのユーザ、すべてのPortlet で共有されます。PortletContext はポートレットアプリケーションにつき、一つ用意されます。

PortletContext は、Portlet の初期パラメータにアクセスすることができます。また、ServletContext とパラメータを共有することもできます。web.xml に記述した初期化パラメータ等にも、PortletContext からアクセスできます。log メソッドを呼び出すことで Portlet ログファイルにログを書き出すことができます。

PortletContext の情報は、ServletContext の情報と共有されます。つまり、PortletContext に格納した情報は、ServletContext で取得できるし、ServletContext に格納した情報は、PortletContext で取得できます。

<h3 id="javax.portlet.PortletRequest">javax.portlet.PortletRequest</h3>

Portlet 版 HttpServletRequest です。クライアントのリクエストに含まれるパラメータを取得したり、セッションを開始したりすることができます。Portlet コンテナは、<a href="#javax.portlet.ActionRequest" rel="external nofollow">javax.portlet.RenderRequest</a> を用意しています。これらは、processAction メソッド、render メソッドのそれぞれで使われます。<strong>PortletRequest、PortletResponse は、スレッドセーフではありません。</strong>

<h3 id="javax.portlet.ActionRequest">javax.portlet.ActionRequest</h3>

Portlet の processAction メソッドの引数に渡されるリクエストオブジェクトです。PortletRequest を継承していて、アクションリクエスト（<a href="#PortletURL" rel="external nofollow">ActionURL </a> によるリクエスト)を処理するのに使います。ActionURL にセットされたパラメータを取得することができます。

<h3 id="javax.portlet.RenderRequest">javax.portlet.RenderRequest</h3>

Portlet の render メソッドの引数に渡されるリクエストオブジェクトです。 PortletRequest を継承していて、レンダリングリクエスト(<a href="#PortletURL" rel="external nofollow">RenderURL</a> によるリクエスト）を処理するのに使います。RenderURL にセットされたパラメータを取得することができます。

<h3 id="javax.portlet.PortletResponse">javax.portlet.PortletResponse</h3>

Portlet 版 HttpServletResponse です。Portlet コンテナは、<a href="#javax.portlet.ActionResponse" rel="external nofollow">javax.portlet.RenderResponse</a> の2つを用意しています。これらは、processAction メソッド、render メソッドのそれぞれで使われます。

<h3 id="javax.portlet.ActionResponse">javax.portlet.ActionResponse</h3>

processAction メソッドのレスポンスを書き込むオブジェクトです。render メソッドにパラメータを渡すには、 setRenderParameter メソッドを利用します。また、レスポンスをリダイレクトする場合には、sendRedirect メソッドを呼び出します。

<h3 id="javax.portlet.RenderResponse">javax.portlet.RenderResponse</h3>

render メソッドのレスポンスを書き込むオブジェクトです。コンテンツの書き出しや、<a href="#javax.portlet.PortletURL" rel="external nofollow">PortletURL</a> の生成を行います。

<h3 id="javax.portlet.PortletPreferences">javax.portlet.PortletPreferences</h3>

Portlet のプリファレンスにアクセスするためのクラスです。PortletRequest#getPreferences メソッドを呼び出すことで取得できます。プリファレンスは、 /WEB-INF/portlet.xml ファイルに記述します。Portlet 毎に設定します。

プリファレンスの情報は、processAction メソッド中であれば、変更可能です。render メソッドで変更すると、例外がスローされます。

<h3 id="javax.portlet.PortletSession">javax.portlet.PortletSession</h3>

Portlet 版 HttpSession です。PortletSession のスコープには2つあります。

<ol><li>Application Scope</li><li>Portlet Scope</li></ol>

Application Scope は、同一ポートレットアプリケーション内で共有されるスコープです。Portlet Scope は、同一Portlet で共有されるスコープです。Portlet Scope の方が狭いスコープになります。プライベートスコープとも呼ばれるみたいです。スコープを指定するには、引数を3つとる setAttribute メソッドを呼び出します。

Portlet Scope に格納されるオブジェクトのキーは、Portlet コンテナによって、次のように決められるそうです。

<pre>javax.portlet.p.&lt;ID&gt;?&lt;ATTRIBUTE_NAME&gt;</pre>

&lt;ID&gt; は、Portlet で一意に決められる値で、'?' は含まれません。&lt;ATTRIBUTE_NAME&gt; は、setAttribute メソッドの引数に渡した名前です。

PortletSession に格納された情報は、HttpSession と共有されます。つまり、PortletSession に格納した値は、HttpSession で取得でき、HttpSession で格納した値は、PortletSession で取得できます。セッションキーに付ける名前は、パッケージ名を参考に付けるとよいようです。ただし、javax.portlet というプレフィックスは予約されているので、開発者は使わないようにしましょう。

HttpSession に保存された情報は、PortletSession の <strong>APPLICATION_SCOPE</strong> になります。逆もしかりです。

<h2 id="Dispatching Request to Servlet and JSPs">Dispatching Request to Servlet and JSPs</h2>

<h3>ディスパッチの方法</h3>

Portlet#render メソッド中で、PortletRequestDispatcher を使ってリクエストをディスパッチすることができます。PortletRequestDispatcher は、PortletContext オブジェクトから取得することができます。ディスパッチするパスは、'/' で始まる必要があります。PortletContext のルートパスからの相対パスになります。パスには、クエリストリングを付けることが出来ます。getNamedDispatcher メソッドを使うと、サーブレット名を指定して、ディスパッチャを取得することが出来ます。

PortletRequestDispacher によってディスパッチされたサーブレットや JSP には、<strong>HTTP GET</strong> がリクエストされます。

<dl>
<dt class="warn">forward してはいけない場合</dt>
<dd><p>PortletRequestDispacher によって include された Servlet や JSP からは、 RequestDispatcher#forward メソッドを呼び出してはいけません。代わりに RequestDispatcher#include メソッドを呼び出すか、Servlet 内ではアクション処理だけを行い、Portlet の render メソッドで別の Servlet や JSP を include します。</p></dd>
</dl>

<h3>ディスパッチ時に付与されるパラメータとアトリビュート</h3>

getRequestDispatcher メソッドを使って取得したディスパッチャ（getNamedDispatcher以外の方法のこと) を使ってサーブレットやJSPにリクエストをディスパッチした時、HttpServletRequest のアトリビュートに下記の属性がセットされます。

<dl><dt>javax.servlet.include.request_uri</dt><dd>サーブレットのリクエストURI。例：/PortalDemo/index.do</dd>

<dt>javax.servlet.include.context_path</dt><dd>サーブレットのコンテキストパス。例：/PortalDemo</dd>

<dt>javax.servlet.include.servlet_path</dt><dd>サーブレットパス。例：/index.do</dd>

<dt>javax.servlet.include.path_info</dt><dd>パス情報。</dd>

<dt>javax.servlet.include.query_string</dt><dd>クエリストリングの情報。例：param1=PARAM1&amp;param2=PARAM2</dd></dl>

ディスパッチの方法にかかわらず、Portlet からサーブレットやJSPにリクエストをディスパッチしたときには、HttpServletRequest に下記の属性がセットされます。

<dl><dt>javax.portlet.config</dt><dd>javax.portlet.PortletConfig クラスのオブジェクト</dd>

<dt>javax.portlet.request</dt><dd>javax.portlet.RenderRequest クラスのオブジェクト</dd>

<dt>javax.portlet.response</dt><dd>javax.portlet.RenderResponse クラスのオブジェクト</dd></dl>

その他、Portlet で設定した Parameter や Attribute が、サーブレットやJSPから取得できます。

<h3>ディスパッチされたサーブレットの制限事項</h3>

PortletRequestDispatcher を使ってリクエストをディスパッチされたサーブレットには、かなりの制限がほどこされます。

<h4>HttpServletRequest</h4>
<table>    <tr>        <th>メソッド名</th>        <th>制約</th>    </tr>    <tr>         <td><ul>            <li>getProtocolget</li>            <li>RemoteAddr</li>            <li>getRemoteHost</li>            <li>getRealPath</li>            <li>getRequestURL</li>        </ul></td>        <td>null を返す</td>    </tr>    <tr>        <td><ul>            <li>getPathInfo</li>            <li>getPathTranslated</li>            <li>getQueryString</li>            <li>getRequestURI</li>            <li>getServletPath</li>        </ul></td>        <td>PortletRequestDispatcher の情報を返す</td>    </tr>    <tr>        <td><ul>            <li>getScheme</li>            <li>getServletName</li>            <li>getServerPort</li>            <li>getAttribute</li>            <li>getAttributeNames</li>            <li>setAttribute</li>            <li>removeAttribute</li>            <li>getLocale</li>            <li>getLocales</li>            <li>isSecure</li>            <li>getAuthType</li>            <li>getContextPath</li>            <li>getRemoteUser</li>            <li>getUserPrincipal</li>            <li>getRequestedSessionId</li>            <li>isRequestedSessionIdValid</li>        </ul></td>        <td>PortletRequest の同名のメソッドと同じ処理になる</td>    </tr>    <tr>        <td><ul>            <li>getParameter</li>            <li>getParameterNames</li>            <li>getParameterValues</li>            <li>getParameterMap</li>        </ul></td>        <td>PortletRequest の同名のメソッドと同じ処理になる。Java Portlet Specification version1.0 PLT.16.1.1 Query Strings in Request Dispatcher Paths セクションに書かれている定義の通りに動作する</td>    </tr>    <tr>        <td><ul>            <li>getCharacterEncoding</li>            <li>setCharacterEncoding</li>            <li>getContextType</li>            <li>getInputStreamgetReader</li>        </ul></td>        <td>何もしない か nullを返す</td>    </tr>    <tr>        <td><ul>            <li>getContentLength</li>        </ul></td>        <td>常に 0 を返す </td>    </tr>    <tr>        <td><ul>            <li>getHeader</li>            <li>getHeaders</li>            <li>getHeaderNames</li>            <li>getCookies</li>            <li>getDateHeader</li>            <li>getIntHeader</li>        </ul>            </td>        <td><p>PortletRequest の getProperties メソッドによって提供される情報を返す</p>        </td>    </tr>    <tr>        <td><ul><li>getMethod</li></ul></td>        <td>常に GET を返す </td>    </tr></table>

<h4>HttpServletResponse</h4>

<table>    <tr>        <th>メソッド名</th>        <th>制約</th>    </tr>
<tr><td><ul>            <li>encodeRedirectURL</li>            <li>encodeRedirectUrl</li>        </ul></td>        <td>null を返す </td>    </tr>    <tr>        <td><ul>            <li>getCharacterEncoding</li>            <li>setBufferSize</li>            <li>flushBuffer</li>            <li>resetBuffer</li>            <li>reset</li>            <li>getBufferSize</li>            <li>isCommitted</li>            <li>getOutputStream</li>            <li>getWriter</li>            <li>encodeURL</li>            <li>encodeUrl</li>        </ul>            </td>        <td>RenderResponse の同名のメソッドと同じ処理になる</td>    </tr>    <tr>        <td><ul>            <li>setContentType</li>            <li>setContentLength</li>            <li>setLocale</li>            <li>addCookie</li>            <li>sendError</li>            <li>sendRedirect</li>            <li>setDateHeader</li>            <li>addDateHeader</li>            <li>setHeader</li>            <li>addHeader</li>            <li>setIntHeader</li>            <li>addIntHeader</li>            <li>setStatus</li>        </ul>            </td>        <td>何もしない</td>    </tr>    <tr>        <td><ul><li>containsHeader</li></ul></td>        <td>常に false を返す </td>    </tr>    <tr>        <td><ul><li>getLocale</li></ul></td>        <td>RenderResponse の同名のメソッドと同じ処理になる</td>    </tr></table>

<h2 id="ユーザ情報">ユーザ情報</h2>

TBD

<h2 id="セキュリティ">セキュリティ</h2>

Portlet コンテナは、ユーザの権限をチェックして、Portlet にアクセス可能かどうかを確認します。これには、Servlet 仕様で決められている、Role を使います。開発者がプログラマティックにチェックすることもできます。それには、PortletRequest クラスの次のメソッドを使用します。

<dl><dt>getRemoteUser</dt><dd>認証に使われたユーザ名を返します。</dd>
<dt>isUserInRole</dt>
<dd>指定したロール名が、セキュリティロールで許可されているかどうかを確認します。</dd>
<dt>getUserPrincipal</dt>
<dd>ログインユーザの Principal を返します。</dl>

<h2 id="portlet.xmlの例">portlet.xmlの例</h2>

+ <a href="http://www.javaworld.jp/technology_and_programming/-/15962-3.html" rel="external nofollow">java World Online - 「eXo Platform」で体験するポータル・サイト開発</a>





