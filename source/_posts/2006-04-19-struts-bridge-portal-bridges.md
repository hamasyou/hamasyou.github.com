---
layout: post
title: "Struts Bridge - Portal Bridges"
date: 2006-04-19 23:04
comments: true
categories: [Engineer-Soul]
keywords: "Portal Bridges, Struts Bridge, Portal, Portlet, Java, 覚書"
tags: []
author: hamasyou
amazon_url: ""
amazon_author: ""
amazon_image: ""
amazon_publisher: ""
---

<p>
[ target="_blank"><img src="http://portals.apache.org/images/apache-portals.gif"   alt="Portal Bridges" />](http://portals.apache.org/pluto/)
</p>

Portal Bridges は、Portlet 以外で作られた Web フレームワークと、JSR-168 に準拠した Portlet を橋渡しするライブラリです。The Apache Software Foundation によって提供されています。

今回の覚書では、Portal Bridges の中の Struts Bridges について書き留めておきます。

ちなみに、対応している Struts のバージョンは、1.2.4  と 1.2.7 だそうです。

<section>

<h4>[参考]</h4>

+ [ target="_blank" class="extlink">Struts Bridge - Portal Bridges](http://portals.apache.org/bridges/multiproject/portals-bridges-struts/index.html)

</section>


<!-- more -->

<h2>Struts Bridge 目次</h2>

<ul>
<li>[言葉の定義おさらい](#chapter1)
</li>
<li>[Portal Bridgesとは](#chapter2)
</li>
<li>[Struts をブリッジしてみる](#chapter3)
<ul>
<li>[なんか、めちゃめちゃ難しいとかいわれてますが・・・](#section1)</li>
<li>[コードやJSPを変更しなくても Portlet に対応できる。ただし、条件が・・・](#section2)</li>
</ul>
</li>
<li>[Struts Bridge が提供するもの](#chapter4)
<ul>
<li>[PortletURL を Struts 用のURLに変える](#section3)</li>
<li>[ServletAPI にアクセスする標準の方法を提供](#section4)</li>
<li>[必要ならば、ServletContextProvider を拡張できます](#section5)</li>
<li>[Portlet のAction リクエストを自動的に Struts のアクションとレンダリングに分ける](#section6)</li>
<li>[ActionRequest と RenderRequest の間で、アトリビュートを受け渡す](#section7)</li>
<li>[Tags サポート](#section8)</li>
<li>[RequestProcessor が用意されている](#section9)</li>
<li>[Portal モードと、スタンドアローンモードを同時に使える](#section10)</li>
</ul>
</li>
</ul>

<h4>Struts Bridge を使ってみる 目次</h4>

<ul>
<li>[Struts アプリケーションを Portlet に対応させる](#chapter5)
<ul>
<li>[web.xml を編集する](#section11)</li>
<li>[struts-config.xml を編集する](#section12)</li>
<li>[JSP で使っている Struts HTML Taglib を変更する](#section13)</li>
<li>[struts-portlet-config.xml ファイルを作成する](#section14)</li>
<li>[portlet.xml を作成する](#section15)</li>
</ul>
</li>
</ul>

<h2 id="chapter1">言葉の定義おさらい</h2>

<dl><dt>ActionRequest</dt>
<dd>Portlet#processAction を呼び出すトリガーとなるリクエストのこと。アクションリクエスト。</dd>
<dt>RenderRequest</dt>
<dd>Portlet#render を呼び出すトリガーとなるリクエストのこと。レンダリングリクエスト</dd>
<dt>リクエストURL</dt>
<dd>HttpServletRequest#getRequestURL メソッドで取得できる URL のこと。クライアントがリクエストを行った URL。</dd>
<dt>PortletURL</dt>
<dd>Portlet を呼び出すためのURL。Portlet は、URL とは直接的な結びつきが無いため、通常 RenderResponse クラスが生成した URL を使う。</dd>
<dt>リクエストパラメータ</dt>
<dd>URL の後ろにくっつくパラメータのこと。 http://localhost/servlet/index.do?abc=hoge の場合、'?' より後ろがリクエストパラメータと呼ばれる。</dd>
</dl>

<h2 id="chapter2">Portal Bridgesとは</h2>

Portal Briges は、Portlet 以外で作られた Web フレームワークと、JSR-168 に準拠した Portlet を橋渡しするライブラリです。

Struts、JSF、PHP、Perl、Velocity をサポートしています。

<h2 id="chapter3">Struts をブリッジしてみる</h2>

<h3 id="section1">なんか、めちゃめちゃ難しいとかいわれてますが・・・</h3>

Apache の Portal Briges のページを見ると、Struts のようなひとつのリクエストを一度で解釈するようなフレームワークは、Portlet 環境に乗せにくいとかいてあります。

たしかに、Portlet はリクエストの処理と描画処理がわかれているので、この二つを同時に行うようなフレームワークとは相性が悪い気はします。

そこで、Portal Bridges はどうするかというと、

2回のリクエストをもって Servlet のリクエストが完結するような独自の実装を提供し、Struts 側には影響が少なくてすむような方法をとります。

<h3 id="section2">コードやJSPを変更しなくても Portlet に対応できる。ただし、条件が・・・</h3>

うまいこと作ってあるアプリケーションであれば、コードやJSPファイルを変更せずに Portlet に対応できるそうです。その条件とは

<section>

<h4>条件</h4>

<ol><li>Struts Action configuration にしたがって、適切な MVC アーキテクチャになっていること</li>
<li>JSP ファイルの中で、すべてのリソースへのリンクが Struts Tag を使って記述されていること</li></ol>

</section>

逃げ道として、Struts アプリケーションを単独で動かすことも可能だそうです。その場合には、Portlet の機能は使うことができません。

<h2 id="chapter4">Struts Bridge が提供するもの</h2>

<h3 id="section3">PortletURL を Struts 用のURLに変える</h3>

Struts は、リクエストURL をもとに Action を判断します。一方で、Portlet は、リクエストパラメータで Action を判断します。

ここに、ひとつの大きな差異が生まれているわけで、Struts Bridge は PortletURL （つまりリクエストパラメータ）を Struts が呼び出されたURL（リクエストURL）に変換する機能を持ちます。

何をやるかというと単純で、Portlet コンテナが渡してくれたURLをもとに Struts 側の Servlet で、HttpServletRequest をつくり直してしまうのです。

<h3 id="section4">ServletAPI にアクセスする標準の方法を提供</h3>

Portlet コンテナや Portlet は、独自の実装で ServletContext や HttpServletRequest、HttpServletResponse をラップしています。

そういう、実装に依存しない形で、ServletContext や HttpServletRequest などを使えるようにするために、ServletContextProvider という機能を提供しています。

Portlet の実装に依存しないように ServletAPI を使いたいときに、このクラスを使います。

<h3 id="section5">必要ならば、ServletContextProvider を拡張できます</h3>

ServletContextProvider の実装は、必要に応じて拡張できます。

<h3 id="section6">Portlet のAction リクエストを自動的に Struts のアクションとレンダリングに分ける</h3>

Portlet の仕様で、ActionRequest の場合は、レンダリングの処理を行ってはいけないという決まりがあります。しかし、Struts からしてみればひとつのリクエストには、アクションとレンダリングで対応するというのが普通です。

なので、Portlet コンテナは ActionrRequest のつもりで Portlet を起動したのに、Struts によってレンダリングまでされてしまっては困るのです。

そこで、Struts Bridge は、Struts の中でレンダリングがされても、RenderRequest が来るまでは、内部で保持したまま処理を続けるという機能があるわけです。

他にも色々とやってくれるみたいです。

<ul><li>ActionRequest 時のコンテキストを StrutsRenderContext に保存する</li>
<li>StrutsRenderContext には、ActionForm や ActionMessages/ActionErrors も保存される</li>
<li>RenderRequest がやってきたら、StrutsRenderContext に保存されているコンテキストを復元する</li>
<li>StrutsRenderContext は一度しか復元されない</li>
<li>StrutsRenderContext は ActionRequest 直後の RenderRequest に対して一度だけ機能する</li>
<li>ただし、例外があり、入力エラーがあったときだけ、ちょっと違う動きになる</li>
<li>ActionRequest の後に ActionErrors が見つかったときには、入力元のページに遷移するようになる</li>
<li>Redirect するときは、Web Browser で行うようにするべし</li>
<li>Redirect をStruts の中で行った場合、Portlet が予期しない動きをする可能性がある</li></ul>

<h3 id="section7">ActionRequest と RenderRequest の間で、アトリビュートを受け渡す</h3>

Struts の中では、Action 中で作ったメッセージを JSP に表示したいといったときに、HttpServletRequest#setAttribute を使います。

Struts 中でセットしたアトリビュートは、ActionRequest に格納されます。しかし、レンダリングには RenderRequest が使われるため、このままではアトリビュートの情報が消えてしまいます。

そこで、ActionRequest から RenderRequest に渡したいアトリビュートを XML に記述することで、StrutsServlet が自動で受け渡しを行ってくれる機能があります。(ActionResponse#setRenderParameter をつかいます）

ActionMessages や ActionErrors などは、受け渡すようにする必要があります。

<h3 id="section8">Tags サポート</h3>

JSP の中で使っている Tag を拡張、もしくは Portlet 仕様にあわせたものが提供されます。

JSP の中で使われている URL が ActionURL なのか、 RenderURL なのかは、Portlet からすると、すごく重要なことです。このあたりの設定を JSP を触らずに規定することができる機能が用意されています。

<h3 id="section9">RequestProcessor が用意されている</h3>

PortletRequestPorcessor というものが用意されています。struts-config.xml に、この RequestProcessor を使うように指定します。

なお、PortletTilesRequestProcessor というのも用意されています。Tiles を使うときには、こちらを使います。

<h3 id="section10">Portal モードと、スタンドアローンモードを同時に使える</h3>

Struts アプリケーションを、Portal とスタンドアローンとで、同時に動かすことができます。

<h4>[参考]</h4>

+ [ target="_blank" class="extlink">Struts Bridge - uPortal Wiki](http://www.ja-sig.org/wiki/display/PLT/Struts+Bridge)

今回は、[ target="_blank" class="extlink">uPortal](http://www.uportal.org/) という Portlet 製品に Struts アプリケーションを乗せることを考えます。

<h2 id="chapter5">Struts アプリケーションを Portlet に対応させる</h2>

変更が必要なファイル

<ul><li>JSP ファイル（一部）</li>
<li>/WEB-INF/web.xml</li>
<li>/WEB-INF/struts-config.xml</li></ul>

新しく作る必要があるファイル（追加する必要があるファイル）

<ul><li>/WEB-INF/portlet.xml</li>
<li>/WEB-INF/struts-portlet-config.xml</li>
<li>/WEB-INF/lib/portals-bridges-common-xxx-SNAPSHOT.jar</li>
<li>/WEB-INF/lib/portals-bridges-struts-xxx-SNAPSHOT.jar</li>
<li>/WEB-INF/lib/uPortalContextProvider.jar</li></ul>

<h3 id="section11">web.xml を編集する</h3>

ActionServlet の代わりに次のクラスを使うように変更します。

<div class="code">org.apache.portals.bridges.struts.PortletServlet</div>

Portlet を使うように設定を追加します。

<section>

<h4>(Portlet 設定例)</h4>

<pre class="code"><code><span class="tag">&lt;servlet&gt;</span> 
  <span class="tag">&lt;servlet-name&gt;</span>JPetstorePortlet<span class="tag">&lt;/servlet-name&gt;</span> 
  <span class="tag">&lt;display-name&gt;</span>JPetstorePortlet Wrapper<span class="tag">&lt;/display-name&gt;</span> 
  <span class="tag">&lt;description&gt;</span>Automated generated Portlet Wrapper<span class="tag">&lt;/description&gt;</span> 
  <span class="tag">&lt;servlet-class&gt;</span>org.apache.pluto.core.PortletServlet<span class="tag">&lt;/servlet-class&gt;</span> 
  <span class="tag">&lt;init-param&gt;</span> 
      <span class="tag">&lt;param-name&gt;</span>portlet-class<span class="tag">&lt;/param-name&gt;</span> 
      <span class="tag">&lt;param-value&gt;</span>org.apache.portals.bridges.struts.StrutsPortlet<span class="tag">&lt;/param-value&gt;</span> 
  <span class="tag">&lt;/init-param&gt;</span> 
  <span class="tag">&lt;init-param&gt;</span> 
      <span class="tag">&lt;param-name&gt;</span>portlet-guid<span class="tag">&lt;/param-name&gt;</span> 
      <span class="tag">&lt;param-value&gt;</span>jpetstore.JPetstorePortlet<span class="tag">&lt;/param-value&gt;</span> 
  <span class="tag">&lt;/init-param&gt;</span> 
<span class="tag">&lt;/servlet&gt;</span> 
 
<span class="tag">&lt;servlet-mapping&gt;</span> 
  <span class="tag">&lt;servlet-name&gt;</span>JPetstorePortlet<span class="tag">&lt;/servlet-name&gt;</span> 
  <span class="tag">&lt;url-pattern&gt;</span>/JPetstorePortlet/*<span class="tag">&lt;/url-pattern&gt;</span> 
<span class="tag">&lt;/servlet-mapping&gt;</span>
</code></pre>

</section>

portlet-guid パラメータは、[context-name].[portlet-name] で記述します。Pluto の Portlet Definition ID のことです。

<h3 id="section12">struts-config.xml を編集する</h3>

struts-config.xml の &lt;controller&gt; 設定に、次のように記述します。

<pre class="code"><code><span class="tag">&lt;controller <span class="attr">pagePattern=</span><span class="value">&quot;$M$P&quot;</span> <span class="attr">inputForward=</span><span class="value">&quot;false&quot;</span>  
<span class="attr">processorClass=</span><span class="value">&quot;org.apache.portals.bridges.struts.PortletRequestProcessor&quot;</span> /&gt;</span>
</code></pre>

<h3 id="section13">JSP で使っている Struts HTML Taglib を変更する</h3>

JSP 中で使っている struts-html Taglib の URI を次のように変更します。

<pre>&lt;@ taglib uri=&quot;http://portals.apache.org/bridges/struts/tags-portlet-html&quot; prefix=&quot;html&quot; %&gt;</pre>

もしくは

<pre>&lt;@ taglib uri=&quot;http://portals.apache.org/bridges/struts/tags-portlet-html-el&quot; prefix=&quot;html-el&quot; %&gt;</pre>

この変更を行うことで、<strong>html:link</strong>、<strong>html:rewrite</strong> タグが拡張され、次の3つの属性を指定することができるようになります。（true / false で指定可能。排他選択）

<ul><li>actionURL</li><li>renderURL</li><li>resourceURL</li></ul>

何も指定しない場合は、renderURL=&quot;true&quot; が指定されたものとして扱われます。

actionURL、 renderURL、 resourceURL の指定が無い場合にどの種類の URL として扱われるかというのは、次に説明する struts-portlet-config.xml で変更可能です。

struts-portlet-config.xml と Taglib で指定した URL 種類が違う場合は、Taglib の方が優先されます。

<h3 id="section14">struts-portlet-config.xml ファイルを作成する</h3>

このファイルは、taglib によって出力されるURL がどの種類の URL （actionURL、renderURL、resourceURL） かを支持するものです。

<pre class="code"><span class="tag">&lt;?xml version=&quot;1.0&quot; encoding=&quot;UTF-8&quot;?&gt;</span> 
<span class="tag">&lt;config&gt;</span> 
  <span class="tag">&lt;render-context&gt;</span> 
    <span class="tag">&lt;attribute <span class="attr">name=</span><span class="value">&quot;errors&quot;</span>/&gt;</span> 
    <span class="tag">&lt;attribute <span class="attr">name=</span><span class="value">&quot;message&quot;</span> <span class="attr">keep=</span><span class="value">&quot;true&quot;</span>/&gt;</span> 
  <span class="tag">&lt;/render-context&gt;</span> 
  <span class="tag">&lt;portlet-url-type&gt;</span> 
    <span class="tag">&lt;action <span class="attr">path=</span><span class="value">&quot;/shop/add&quot;</span>/&gt;</span> 
    <span class="tag">&lt;action <span class="attr">path=</span><span class="value">&quot;/shop/switch&quot;</span>/&gt;</span> 
    <span class="tag">&lt;action <span class="attr">path=</span><span class="value">&quot;/shop/remove&quot;</span>/&gt;</span> 
    <span class="tag">&lt;action <span class="attr">path=</span><span class="value">&quot;/shop/signoff&quot;</span>/&gt;</span> 
    <span class="tag">&lt;action <span class="attr">path=</span><span class="value">&quot;/shop/viewCategory&quot;</span>/&gt;</span> 
    <span class="tag">&lt;action <span class="attr">path=</span><span class="value">&quot;/shop/viewItem&quot;</span>/&gt;</span> 
    <span class="tag">&lt;action <span class="attr">path=</span><span class="value">&quot;/shop/viewProduct&quot;</span>/&gt;</span> 
    <span class="tag">&lt;action <span class="attr">path=</span><span class="value">&quot;/shop/viewCart&quot;</span>/&gt;</span> 
    <span class="tag">&lt;action <span class="attr">path=</span><span class="value">&quot;/shop/newOrder&quot;</span>/&gt;</span> 
    <span class="tag">&lt;render <span class="attr">path=</span><span class="value">&quot;/shop/newOrderForm&quot;</span>/&gt;</span> 
    <span class="tag">&lt;action <span class="attr">path=</span><span class="value">&quot;/shop/listOrders&quot;</span>/&gt;</span> 
    <span class="tag">&lt;resource <span class="attr">path=</span><span class="value">&quot;/images/&quot;</span>/&gt;</span> 
  <span class="tag">&lt;/portlet-url-type&gt;</span> 
<span class="tag">&lt;/config&gt;</span> </pre>

<dl><dt>render-context タグ</dt>
<dd><p>Portlet では、ActionRequest で受け取ったパラメータは、RenderRequest では受け取ることができません。RenderRequest でも受け取るには、ActionRequest の処理中に setRenderParameter メソッドを呼び出す必要があります。</p>

<p>render-context に記述した属性に関しては、ブリッジによって自動的に ActionRequest から RenderRequest に引き渡してくれるようになります。keep 属性を true にすると、別リクエスト間でも保持するようになります。（つまり、セッションに格納されます）</p></dd>
<dt>portlet-url-type タグ</dt>
<dd><p>JSP 中で記述されている URL が、ActionURL なのか、RenderURL なのか、ResponseURL なのかを指定します。</p>

<p>portlet-url-type の属性の default を action とすることで、URL のデフォルトを render から action へ変更することができます。</p>

<pre class="code"><code><span class="tag">&lt;config&gt;</span> 
  <span class="tag">&lt;portlet-url-type <span class="attr">default=</span><span class="value">&quot;action&quot;</span>/&gt;</span> 
<span class="tag">&lt;config&gt;</span>
</code></pre></dd>
</dl>

<h3 id="section15">portlet.xml を作成する</h3>

portlet.xml ファイルを作成します。例を次に記述しておきます。

<pre class="code"><code><span class="tag">&lt;?xml version=&quot;1.0&quot; encoding=&quot;UTF-8&quot;?&gt;</span> 
<span class="tag">&lt;portlet-app <span class="attr">xmlns=</span><span class="value">&quot;http://java.sun.com/xml/ns/portlet/portlet-app_1_0.xsd&quot;</span> <span class="attr">version=</span><span class="value">&quot;1.0&quot;</span> 
 <span class="attr">xmlns:xsi=</span><span class="value">&quot;http://www.w3.org/2001/XMLSchema-instance&quot;</span> 
 <span class="attr">xsi:schemaLocation=</span><span class="value">&quot;http://java.sun.com/xml/ns/portlet/portlet-app_1_0.xsd 
 http://java.sun.com/xml/ns/portlet/portlet-app_1_0.xsd&quot;</span>&gt;</span> 
   <span class="tag">&lt;portlet&gt;</span> 
    <span class="tag">&lt;portlet-name&gt;</span>JPetstorePortlet<span class="tag">&lt;/portlet-name&gt;</span> 
    <span class="tag">&lt;portlet-class&gt;</span>org.apache.portals.bridges.struts.StrutsPortlet<span class="tag">&lt;/portlet-class&gt;</span> 
    <span class="tag">&lt;init-param&gt;</span> 
      <span class="tag">&lt;name&gt;</span>ServletContextProvider<span class="tag">&lt;/name&gt;</span> 
      <span class="tag">&lt;value&gt;</span>ca.mun.portal.bridges.PortalServletContextProvider<span class="tag">&lt;/value&gt;</span> 
    <span class="tag">&lt;/init-param&gt;</span> 
    <span class="tag">&lt;init-param&gt;</span> 
      <span class="tag">&lt;name&gt;</span>ViewPage<span class="tag">&lt;/name&gt;</span> 
      <span class="tag">&lt;value&gt;</span>/index.shtml<span class="tag">&lt;/value&gt;</span> 
    <span class="tag">&lt;/init-param&gt;</span> 
    <span class="tag">&lt;init-param&gt;</span> 
      <span class="tag">&lt;name&gt;</span>HelpPage<span class="tag">&lt;/name&gt;</span> 
      <span class="tag">&lt;value&gt;</span>/help.shtml<span class="tag">&lt;/value&gt;</span> 
    <span class="tag">&lt;/init-param&gt;</span> 
    <span class="tag">&lt;expiration-cache&gt;</span>-1<span class="tag">&lt;/expiration-cache&gt;</span> 
    <span class="tag">&lt;supports&gt;</span> 
      <span class="tag">&lt;mime-type&gt;</span>text/html<span class="tag">&lt;/mime-type&gt;</span> 
      <span class="tag">&lt;portlet-mode&gt;</span>VIEW<span class="tag">&lt;/portlet-mode&gt;</span> 
      <span class="tag">&lt;portlet-mode&gt;</span>HELP<span class="tag">&lt;/portlet-mode&gt;</span> 
    <span class="tag">&lt;/supports&gt;</span> 
    <span class="tag">&lt;portlet-info&gt;</span> 
      <span class="tag">&lt;title&gt;</span>JPetstore<span class="tag">&lt;/title&gt;</span> 
      <span class="tag">&lt;keywords&gt;</span>Struts<span class="tag">&lt;/keywords&gt;</span> 
    <span class="tag">&lt;/portlet-info&gt;</span> 
  <span class="tag">&lt;/portlet&gt;</span> 
<span class="tag">&lt;/portlet-app&gt;</span>
</code></pre>

ca.mun.portal.bridges.PortalServletContextProvider というクラスは、ServletContextProvider の実装クラスです。北米のほうで実装されたものがあるということで、今回はこれを使います。uPortal / pluto で使用可能です。

<section>

<h4>ca.mun.portal.bridges.PortalServletContextProvider.java</h4>

<pre class="code"><code><span class="keyword">package</span> ca.mun.portal.bridges; 
 
<span class="keyword">import</span> javax.portlet.GenericPortlet; 
<span class="keyword">import</span> javax.portlet.PortletRequest; 
<span class="keyword">import</span> javax.portlet.PortletResponse; 
<span class="keyword">import</span> javax.servlet.ServletContext; 
<span class="keyword">import</span> javax.servlet.http.HttpServletRequest; 
<span class="keyword">import</span> javax.servlet.http.HttpServletResponse; 
 
<span class="keyword">import</span> org.apache.pluto.core.impl.ActionRequestImpl; 
<span class="keyword">import</span> org.apache.pluto.core.impl.PortletContextImpl; 
<span class="keyword">import</span> org.apache.pluto.core.impl.RenderRequestImpl; 
<span class="keyword">import</span> org.apache.portals.bridges.common.ServletContextProvider; 
 
<span class="comment">/** 
 * PortalServletContextProvider supplies access to 
 * the Servlet context of uPortal Portlet. 
 *  
 * @author Satish Sekharan 
 */</span> 
<span class="keyword">public</span> <span class="keyword">class</span> PortalServletContextProvider <span class="keyword">implements</span> ServletContextProvider { 
 
  <span class="keyword">public</span> ServletContext getServletContext(GenericPortlet portlet) { 
    <span class="keyword">return</span>
  ((PortletContextImpl) portlet.getPortletContext()).getServletContext();
  }  
 
  <span class="keyword">public</span> HttpServletRequest getHttpServletRequest(GenericPortlet portlet, PortletRequest request) {         
    <span class="keyword">return</span> 
(HttpServletRequest) ((HttpServletRequestWrapper) request).getRequest();
  } 
 
  <span class="keyword">public</span> HttpServletResponse getHttpServletResponse(GenericPortlet portlet, 
        PortletResponse response) { 
    <span class="keyword">return</span>
(HttpServletResponse) ((HttpServletResponseWrapper) response).getResponse();
  }
}
</code></pre>

</section>




