---
layout: post
title: "Click@J2EE Web Application Framework"
date: 2006-03-12 17:00
comments: true
categories: [Engineer-Soul]
keywords: "Click,click,J2EE,Web Application,Framework,フレームワーク,クリック"
tags: []
author: hamasyou
amazon_url: ""
amazon_author: ""
amazon_image: ""
amazon_publisher: ""
---

<strong>[ target="_blank" class="extlink">Click](http://click.sourceforge.net/)</strong> は、J2EE Webアプリケーションのフレームワークです。<strong>ページ指向 + イベントドリブン</strong> なプログラミングスタイルでアプリケーションを開発します。[ target="_blank" class="extlink">Velocity](http://jakarta.apache.org/velocity/) を内部で使っていて、テンプレートモデルで、ページを作成します。

ここでは、Click の覚書きをしておきます。あくまで個人用なので、あてにしないようにお願いします。間違い等発見された方は、ご連絡いただけると助かります。

<dl>
<dt class="info">使用バージョン</dt>
<dd><ul><li>version Click 0.1.8</li></ul>

<p>Clickはドキュメントがしっかりできているので、ドキュメントを読むのが一番です</p></dd>
</dl>

<section>

<h4>参考</h4>

+ [ target="_blank" class="extlink">[SourceForge]Click Home](http://click.sourceforge.net/)
+ [ target="_blank" class="extlink">新・たけぞう瀕死の日記](http://www3.vis.ne.jp/~asaki/p_diary/diary.cgi?Search=%5BClick%5D)
+ [ target="_blank" class="extlink">Click 勉強記](http://d.hatena.ne.jp/tanigon/searchdiary?word=%2a%5bClick%5d)

</section>


<!-- more -->

<h2>覚書き</h2>

<ul><li>[Clickとは](#Clickとは)</li>
<li>[Page](#Page)</li>
<li>[Control](#Control)</li>
<li>[ページ遷移の仕組み](#ページ遷移の仕組み)</li>
<li>[ClickをSpring Frameworkと一緒に使う](#ClickをSpring Frameworkと一緒に使う)</li></ul>

<h2 id="Clickとは">Clickとは</h2>

Clickはコンポーネント＆ページ指向のイベントドリブンアーキテクチャを持つ、J2EE Webアプリケーションフレームワークです。シンプルなアーキテクチャになっていて、JSFよりも直感的でわかりやすいプログラミングができます。

Clickには大きく2つのコンポーネントが用意されています。一つは、Pageです。これは、その名の通り、一つのページを表すコンポーネントです。もう一つは、Controlで、これは、ページの要素(例えばリンクやボタンなど）を表します。

ページは複数のコントロールを持つことができます。ページのレンダリング時には、これらのコントロールをVelocityテンプレートと照らし合わせてレンダリングを行います。コントロールに付けた名前が、Velocityテンプレート中で使えるモデルの名前になります。

Clickの本体は、net.sf.click.ClickServlet です。このクラスか、このクラスを継承したサブクラスをサーブレットとして使います。

Clickの慣習で、マッピングパターンは、<em>*.htm</em> にするようです。

<pre class="code"><code><span class="tag">&lt;servlet&gt;</span> 
  <span class="tag">&lt;servlet-name&gt;</span>click-servlet<span class="tag">&lt;/servlet-name&gt;</span> 
  <span class="tag">&lt;servlet-class&gt;</span>net.sf.click.ClickServlet<span class="tag">&lt;/servlet-class&gt;</span> 
  <span class="tag">&lt;load-on-startup&gt;</span>0<span class="tag">&lt;/load-on-startup&gt;</span> 
<span class="tag">&lt;/servlet&gt;</span> 
 
<span class="tag">&lt;servlet-mapping&gt;</span> 
  <span class="tag">&lt;servlet-name&gt;</span>click-servlet<span class="tag">&lt;/servlet-name&gt;</span> 
  <span class="tag">&lt;url-pattern&gt;</span>*.htm<span class="tag">&lt;/url-pattern&gt;</span> 
<span class="tag">&lt;/servlet-mapping&gt;</span>
</code></pre>

<h3 id="Page">net.sf.click.Page</h3>

一ページを表すクラスです。開発者はこのクラスを継承してページクラスを作成します。ページには、複数のControlを持たせることができます。

ページには、次の2つの種類のメソッドを定義することができます。

<ol><li>ページのライフサイクルに関するイベントハンドリングメソッド</li><li>コントロールのイベントリスナメソッド</li></ol>

<h3>ページのライフサイクルに関するイベントハンドリングメソッド</h3>

ページは、あらかじめ決められたイベントハンドリングポイントが用意されています。以下のメソッドをオーバーライドすることで、イベントハンドリングを実装できます。

<dl>
<dt>onInit()</dt><dd>サーブレットがリクエストを受けて、ページをロードしたときに呼び出されます</dd>
<dt>onSecurityCheck()</dt><dd>onInit()の後に呼び出されます。アクセス制御等のセキュリティ確認を行える場所です</dd>
<dt>onGet()</dt><dd>Getメソッドのリクエストの場合に呼び出されます。onSecurityCheck()の後に呼び出されます</dd>
<dt>onPost()</dt><dd>Postメソッドのリクエストの場合に呼び出されます。onSecurityCheck()の後に呼び出されます</dd>
<dt>onDestroy()</dt><dd>ページが廃棄されるときに呼び出されます</li></dd>
</dl>

これらのメソッドは、ページのイベントシーケンスにしたがって呼び出されます。

<h4>参考</h4>
+ [ target="_blank" class="extlink">Click#Page-Execution](http://click.sourceforge.net/docs/pages.html#page-execution)

<h3>コントロールのイベントリスナメソッド</h3>

リスナメソッドをコントロールに登録することで、コントロールに対するイベントが発生した場合にハンドリング処理ができるようになります。イベントリスナメソッドは、次の2つのルールを満たさなければなりません。

<ol><li>引数は無し</li><li>boolean もしくは java.lang.Boolean を戻り値で返す</li></ol>

イベントリスナメソッドの戻り値は、次のような意味を持ちます。

<dl>
<dt>true の場合</dt><dd><p>続けて他のコントロールの onProcess() メソッドが呼び出されます。その後、ページのonGet() メソッド または、 onPost() メソッドが呼び出されます。</p></dd>
<dt>falseの場合</dt><dd><p>他のコントロールの処理は実行されません。また、ページの onGet()、onPost() メソッドも呼び出されません。そのままページの遷移処理、またはレンダリング処理に入ります。</p></dd>
</dl>

上記の動作から分かることは、イベントハンドリング処理で、ページの遷移等を行う場合は、戻り値を false にする必要があるということです。ページ遷移を伴わないイベントハンドリング処理の場合は、true を返すようにします。

<h3>サーブレットAPIを使うには</h3>

Clickには、コンテキスト（net.sf.click.Context）と言うものがあります。ClickServletは、リクエストを受けると、要求されたページとコンテキストを結び付けます。ページ内のメソッドからは、このコンテキストを通して、通常のServletAPIを使用することができます。コンテキストを取得するには、Page#getContext() メソッドを呼び出します。

<h3>コントロールはデフォルトコンストラクタの中で追加する</h3>

ページが要求されると、ClickServletは、ページのインスタンス化を行います。このとき、引数のないデフォルトコンストラクタが呼び出されます。
※ ただし、この動作はClickServletのデフォルト実装によるもので、サブクラスでは、インスタンス化の方法をオーバーライドすることができます。

インスタンス化されたページは、そのままページの処理シーケンスに突入します。そのため、インスタンス化された時点で静的なコントロールは new しておくことが望ましいです。もちろん、処理シーケンス中で、動的にコントロールを作成することも可能です。

<pre class="code"><code><span class="keyword">public</span> <span class="keyword">class</span> Login <span class="keyword">extends</span> Page {
  <span class="keyword">private</span> Form loginForm = <span class="keyword">new</span> Form(<span class="literal">"loginForm"</span>);
  <span class="keyword">public</span> Login() {
    <span class="keyword">this</span>.loginForm.add(<span class="keyword">new</span> TextField(<span class="literal">"userName"</span>, <span class="literal">"ユーザ名"</span>, <span class="keyword">true</span>));
    <span class="keyword">this</span>.loginForm.add(<span class="keyword">new</span> PasswordField(<span class="literal">"password"</span>, <span class="keyword">true</span>));
    <span class="keyword">this</span>.loginForm.add(<span class="keyword">new</span> Submit(<span class="literal">"login"</span>, <span class="literal">" ログイン "</span>, <span class="keyword">this</span>, <span class="literal">"onLoginClicked"</span>));
    <span class="keyword">super</span>.addControl(<span class="keyword">this</span>.loginForm);
  }
 
  <span class="comment">/* その他の実装 */</span>
}
</code></pre>

コントロールを動的に追加するには、onGet() メソッドか onPost() メソッド中で実装すると良いと思います。

<pre class="code"><code>@Override
<span class="keyword">public</span> <span class="keyword">void</span> onGet() {
  <span class="keyword">this</span>.onHandleRequest();
}
 
@Override
<span class="keyword">public</span> <span class="keyword">void</span> onPost() {        
  <span class="keyword">this</span>.onHandleRequest();
}
 
<span class="keyword">protected</span> <span class="keyword">void</span> onHandleRequest() {
  String userName = 
        <span class="keyword">super</span>.getContext().getRequestParameter(<span class="literal">"userName"</span>);
  <span class="keyword">if</span> (<span class="literal">"admin"</span>.equals(userName)) {
    <span class="keyword">super</span>.addControl(<span class="keyword">new</span> Label(<span class="literal">"menu"</span>, <span class="literal">"管理者メニュー"</span>));
  } <span class="keyword">else</span> {
    <span class="keyword">super</span>.addControl(<span class="keyword">new</span> Label(<span class="literal">"menu"</span>, <span class="literal">"一般メニュー"</span>));
  }
}
</code></pre>

<h3>モデルを画面に表示する</h3>

addModel メソッドを呼びだして、名前を指定してモデルを登録します。ここで指定した名前で Velocityテンプレートから参照することができます。

<h2 id="Control">net.sf.click.Control</h2>

ページを構成するコンポーネント（例えば、フォームやリンク、ボタン、テキストボックスなど）です。コントロールには次のような大分類に分けられます。

<dl>
<dt>ActionLink</dt><dd>リンクを表します。リスナを登録することで、コールバック（イベント）を受け取ることができます</dd>
<dt>Field</dt><dd>フォームと一緒に使われる入力フィールドです。フィールドは、自分自身をバリデーションします</dd>
<dt>Form</dt><dd>入力フォームです。自分自身とフィールドのバリデーション機能を持ちます。また、フォームは、独自のレイアウトを持ちます</dd>
</dl>

<h3>ActionLink</h3>

アンカーを持つリンクです。

リスナを登録することで、リンクがクリックされたときのイベントをハンドリングできます。

<h4>参考</h4>

+ [ target="_blank" class="extlink">Control#Callback](http://click.sourceforge.net/docs/controls.html#control-callback)

<h3>Field</h3>

フォームの入力フィールドです。様々な種類があります。フィールドは、自分自身でバリデーションを行うことができます。必須入力、最大・最小文字長等のチェックを行います。

<h4>参考</h4>

+ [ target="_blank" class="extlink">Control#Field](http://click.sourceforge.net/docs/click-api/net/sf/click/control/Field.html)

<h3>Form</h3>

フォームコントロールです。フォームに入力フィールドを追加して使います。フォームは、レイアウトフォーマットを持っています。フィールドの数等によってフォームのレイアウトが自動で設定されます。また、フォームには、入力値をオブジェクトに変換する役割もあります。

<h4>参考</h4>

+ [ target="_blank" class="extlink">Control#Form](http://click.sourceforge.net/docs/click-api/net/sf/click/control/Form.html)

<h2 id="ページ遷移の仕組み">ページ遷移の仕組み</h2>

Click には3つのページ遷移の方法があります。

<ul><li>Forward</li><li>Redirect</li><li>テンプレート置き換え</li></ul>

<h3>Forward</h3>

サーブレットの request.getRequestDispatcher を使ってページを遷移します。Fowardさせるには、Pageクラスのイベントハンドラで、setFoward() を呼び出します。

<pre class="code"><code>@Override
<span class="keyword">public</span> <span class="keyword">void</span> onPost() {
  <span class="keyword">super</span>.setForward(<span class="literal">"login.htm"</span>);
}
</code></pre>

setForward() に与えるページパスは、パスの先頭に "/" 先頭にを含めません。フォワード先にパラメータを送りたい場合は、getContext().setRequestAttribute() を呼び出します。

<h4>参考</h4>

+ [ target="_blank" class="extlink">Page#Forward Parameter Passing](http://click.sourceforge.net/docs/pages.html#page-navigation)

getContext().createPage() を呼び出して、遷移先のページを作成するというやり方もあります。この例も、上の「参考」に例があります。

<h3>Redirect</h3>

サーブレットの response.sendRedirect() を呼び出して、ページを遷移します。リダイレクトしたい場合は、Page#setRedirect() を呼び出して、リダイレクト先のパスを設定します。リダイレクト先のパスの先頭に "/" を付けると、コンテキストルートからのパスという意味になります。（つまり、パスの先頭にコンテキスト名が付与されます）

<h3>テンプレート置き換え</h3>

setForward() も setRedirect() もされていない場合は、Velocityテンプレートをレンダリングします。使われるVelocityテンプレートは、Page#getTemplate() の戻り値になります。これは、デフォルトで、パス名と同じファイルになります。getTemplate() をオーバーライドして、テンプレート名を変えることで、出力を変えることができます。

<h2 id="ClickをSpring Frameworkと一緒に使う">ClickをSpring Frameworkと一緒に使う</h2>

Clickを [ target="_blank" class="extlink">Spring Framework](http://www.springframework.org/) と一緒に使う方法です。Clickをダウンロードするとついてくる click-extras.jar を使います。このライブラリに含まれている net.sf.click.extras.spring.SpringClickServlet を ClickServlet の代わりに使います。そして、Spring Framework のアプリケーションコンテキスト定義（applicationContext.xml）のパスを与えてやります。

<pre class="code"><code><span class="tag">&lt;servlet&gt;</span>
   <span class="tag">&lt;servlet-name&gt;</span>click-servlet<span class="tag">&lt;/servlet-name&gt;</span>
   <span class="tag">&lt;servlet-class&gt;</span>net.sf.click.extras.spring.SpringClickServlet<span class="tag">&lt;/servlet-class&gt;</span>
   <span class="tag">&lt;init-param&gt;</span>
     <span class="tag">&lt;param-name&gt;</span>spring-path<span class="tag">&lt;/param-name&gt;</span>
     <span class="tag">&lt;param-value&gt;</span>/WEB-INF/applicationContext.xml<span class="tag">&lt;/param-value&gt;</span>
   <span class="tag">&lt;/init-param&gt;</span>
   <span class="tag">&lt;load-on-startup&gt;</span>0<span class="tag">&lt;/load-on-startup&gt;</span>
<span class="tag">&lt;/servlet&gt;</span>
</code></pre>




