---
layout: post
title: "JSF-カスタムコンポーネント覚書き"
date: 2005-10-22 13:44
comments: true
categories: [Engineer-Soul]
keywords: "JSF,Java Server Faces,カスタムコンポーネント,タグハンドラ,レンダラー,作り方,覚書き"
tags: []
author: hamasyou
amazon_url: ""
amazon_author: ""
amazon_image: ""
amazon_publisher: ""
---

<p>
[ target="_blank"><img src="http://images-jp.amazon.com/images/P/4873111978.09._OU09_PE0_SCMZZZZZZZ_.jpg"  border="0" alt="JavaServer Faces完全ガイド" />](http://www.amazon.co.jp/exec/obidos/ASIN/4873111978/sorehabooks-22/249-1404088-2131559?%5Fencoding=UTF8&camp=247&link%5Fcode=xm2)
</p>

JSFでカスタムコンポーネントを作るときの覚書きです。参考にした『[ target="_blank" class="extlink">JavaServer Faces完全ガイド](http://www.amazon.co.jp/exec/obidos/ASIN/4873111978/sorehabooks-22/249-1404088-2131559?%5Fencoding=UTF8&camp=247&link%5Fcode=xm2)』は、ツリーコンポーネントをカスタムコンポーネントとして作る手順が載っていました。

ここでは、カスタムコンポーネントを作るときに必要となるパーツと、注意点などをまとめておこうと思います。カスタムコンポーネントは大きく、入力用のコンポーネントと出力用のコンポーネントの2つに分けれます。入力用のコンポーネントはリクエスト間で値の保存・読み込みやデコードレンダリングなどを行わなければなりません。出力用のコンポーネントの場合は、値を保持し、エンコードレンダリングを行うことで機能を表現します。

ここでは、出力用のコンポーネントの話をしています。説明が断片的なので、上記の本を読んだほうが手っ取り早いかも・・・ ^^;

<section>

<h4>[参考]</h4>

+ [ target="_blnak" class="extlink">JSFによるWebアプリケーション開発/カスタム・コンポーネント](http://www.wakhok.ac.jp/~tomoharu/jsf2004/text/index_c11.html)

</section>


<!-- more -->

<h2>作るもの</h2>

<table>
<tr><th>&nbsp;</th><th>コンポーネント</th><th>レンダラー</th><th>タグハンドラ</th></tr><tr><th>継承</th><td>UIComponentBase</td><td>Renderer</td><td>UIComponentTag<br />UIComponentBodyTag</td></tr><tr><th>famiry名</th><td>任意<br />（getFamiryメソッドで返すようにする）</td><td>任意<br />（コンポーネントとあわせる必要がある。faces-context.xmlに定義する)</td><td>不要</td></tr><tr><th>タイプ</th><td>完全クラス名。定数で定義するとよい。</td><td>完全クラス名。定数で定義するとよい。</td><td>getComponentTypeで使用するコンポーネントタイプを返す。<br />getRendererTypeで使用するレンダラータイプを返す。</td></tr><tr><th>実装するメソッド</th><td>コンポーネントで使用するプロパティのget/set等</td><td>encode系メソッド</td><td>setPropertiesメソッド、タグで受け取る属性のget/set等</td></tr><tr><th>その他</th><td>faces-context.xmlにcomponentの定義を記述</td><td>faces-context.xmlにrendererの定義を記述</td><td>TLDファイルを作成する</td></tr>
</table>

<h2>コンポーネントクラス</h2>

コンポーネントクラスはjavax.faces.component.UIComponentBaseを継承して作ります。似たようなコンポーネントを継承して作ってもいいでしょう。コンポーネントが特殊で、レンダラーの責務を別クラスにする必要がない場合は、コンポーネントの encode系メソッドを実装します。

コンポーネントは、famiryを定義します。同一のfamiry名を持つコンポーネントとレンダラがセットで使われます。コンポーネントのfamiryは getFamiryメソッドで返すようにします。

ValueBindingで値を受け取らない場合は、setValueBinding(String, ValueBinding)をオーバーライドして、特定の値しかValueBindingで受け取らないことを明示するとよい。

<section>

<h4>setValueBindingのオーバーライド例</h4>

<pre class="code"><code><span class="comment">// @Overwride</span>
<span class="keyword">public</span> <span class="keyword">void</span> setValueBinding(<span class="keyword">final</span> String name, ValueBinding bind) {
  <span class="keyword">if</span> (name.equals(<span class="literal">"value"</span>)) {
    <span class="keyword">throw</span> <span class="keyword">new</span> IllegalArgumentException(<span class="literal">"valueは値結合でなければなりません。"</span>);
  }        
  <span class="keyword">super</span>.setValueBinding(name, bind);
}
</code></pre>

</section>

ValueBindingで受け取る値を返すgetメソッドは、getValueBindingメソッドを使って値結合を解決しなければならない。毎回、値結合を解決するとパフォーマンスが悪くなるので、キャッシュするようにする。

<section>

<h4>ValueBindingされている値のgetメソッド例</h4>

<pre class="code"><code><span class="keyword">public</span> Object getValue() {
  <span class="keyword">if</span> (<span class="keyword">this</span>.value != <span class="keyword">null</span>) {
    <span class="keyword">return</span> <span class="keyword">this</span>.value;
  } <span class="keyword">else</span> {
    ValueBinding bind = getValueBinding(<span class="literal">"value"</span>);
    <span class="keyword">if</span> (bind != <span class="keyword">null</span>) {
      <span class="keyword">this</span>.value = bind.getValue(getFacesContext()); 
      <span class="keyword">return</span> <span class="keyword">this</span>.value;
    } <span class="keyword">else</span> {
      <span class="keyword">return</span> <span class="keyword">null</span>;
    }
  }
}
</code></pre>

</section>

<h3>レンダラー</h3>

javax.faces.render.Rendererクラスを継承して作ります。主にオーバーライドして使うのは下のメソッド。

<ul><li>encodeBegin(FacesContext, UIComponent)</li>
<li>encodeChildren(FacesContext, UIComponent)</li>
<li>encodeEnd(FacesContext, UIComponent)</li>
<li>getRendersChildren()</li></ul>

レンダラーは、基本的には自分が処理できるコンポーネントだけをレンダリングします。ただし、&lt;h:dataTable&gt;などは、子に現れる&lt;h:column&gt;などのコンポーネントも処理します。子のコンポーネントの処理も自分で行う場合は、getRendersChildren()で <code>true</code> を返す必要があります。

子のコンポーネントを処理する場合に、例えば次のようにします。

<pre class="code"><code><span class="tag">&lt;h:dataTable <span class="attr">value=</span><span class="value">&quot;#{xxx}&quot;</span> <span class="attr">var=</span><span class="value">&quot;child&quot;</span>&gt;</span> 
  <span class="tag">&lt;h:column&gt;</span> 
    <span class="tag">&lt;h:outputText <span class="attr">value=</span><span class="value">&quot;#{child.name}&quot;</span>/&gt;</span> 
  <span class="tag">&lt;/h:column&gt;</span> 
<span class="tag">&lt;/h:dataTable&gt;</span>
</code></pre>

child の部分に格納される値をセットしておかなければならない場合があります。これは、リクエストにコンポーネントのvar変数が表す名前（ここではchild)をキーとして、値をセットしておくことで対応できます。

<section>

<h4>子コンポーネントに値を受け渡す方法</h4>

<pre class="code"><code>Map requestMap = facesContext.getExternalContext().getRequestMap(); 
requestMap.put(component.getVar(), component.getVarValue());
</code></pre>

</section>

エンコードは、encodeBegin、getRendersChildrenがtrueを返すときencodeChildren、encodeEndの順で処理されます。

<h3>タグハンドラー</h3>

タグにbodyが必要ない場合はjavax.faces.webapp.UIComponentTagクラスを継承します。例えば&lt;h:outputText /&gt; のようなタグの場合です。

タグにbodyが必要な場合は javax.faces.webapp.UIComponentBodyTagクラスを継承します。このクラスの方が多少パフォーマンスが悪いようです。bodyが不要の場合はUIComponentTagを使うようにします。

実装するメソッドは次の2つです。

<ul><li>getComponentType</li><li>getRendererType</li></ul>

前者はコンポーネントのタイプ（完全クラス名にしておくことをおすすめ）を返すようにします。後者はレンダラーのタイプ（完全クラス名にしておくことをおすすめ）を返すようにします。

さらに、タグハンドラーには、タグの属性の値を保持するプロパティを実装します。

<section>

<h4>タグハンドラの例1</h4>

<pre class="code"><code><span class="keyword">import</span> javax.faces.component.UIComponent;
<span class="keyword">import</span> javax.faces.context.FacesContext;
<span class="keyword">import</span> javax.faces.el.ValueBinding;
<span class="keyword">import</span> javax.faces.webapp.UIComponentTag;
 
<span class="keyword">public</span> <span class="keyword">class</span> MyComponentTag <span class="keyword">extends</span> UIComponentTag {
    
  <span class="keyword">private</span> String value;
  <span class="keyword">private</span> String var;
  <span class="keyword">private</span> String onmouseover;
 
  <span class="keyword">protected</span> <span class="keyword">void</span> setProperties(UIComponent component) {
    <span class="keyword">super</span>.setProperties(component);
        
    FacesContext context = getFacesContext();
    MyComponent mycomp = (MyComponent) component;
    <span class="keyword">if</span> (<span class="keyword">this</span>.value != <span class="keyword">null</span>) {
      <span class="keyword">if</span> (UIComponentTag.isValueReference(<span class="keyword">this</span>.value)) {
        ValueBinding bind =
          context.getApplication().createValueBinding(<span class="keyword">this</span>.value);
        component.setValueBinding(<span class="literal">"value"</span>, bind);
      } <span class="keyword">else</span> {
        mycomp.setValue(<span class="keyword">this</span>.value);
      }
    }
    <span class="keyword">if</span> (<span class="keyword">this</span>.var != <span class="keyword">null</span>) {
      mycomp.setVar(this.var);
    }
    <span class="keyword">if</span> (<span class="keyword">this</span>.onmouseover != <span class="keyword">null</span>) {
      <span class="keyword">if</span> (UIComponentTag.isValueReference(<span class="keyword">this</span>.onmouseover)) {
        ValueBinding bind =
          context.getApplication().createValueBinding(<span class="keyword">this</span>.onmouseover);
        component.setValueBinding(<span class="literal">"onmouseover"</span>, bind);
      } <span class="keyword">else</span> {
        mycomp.setOnmouseover(<span class="keyword">this</span>.onmouseover);
      }
    }
  }
 
  <span class="keyword">public</span> String getComponentType() {
    <span class="keyword">return</span> MyComponent.COMPONENT_TYPE;
  }
 
  <span class="keyword">public</span> String getRendererType() {
    <span class="keyword">return</span> MyComponentRenderer.RENDERER_TYPE;
  }
 
  <span class="keyword">public</span> String getOnmouseover() {
    <span class="keyword">return</span> onmouseover;
  }
 
  <span class="keyword">public</span> <span class="keyword">void</span> setOnmouseover(String onmouseover) {
    <span class="keyword">this</span>.onmouseover = onmouseover;
  }
 
  <span class="keyword">public</span> String getValue() {
    <span class="keyword">return</span> value;
  }
 
  <span class="keyword">public</span> <span class="keyword">void</span> setValue(String value) {
    <span class="keyword">this</span>.value = value;
  }
 
  <span class="keyword">public</span> String getVar() {
    <span class="keyword">return</span> var;
  }
 
  <span class="keyword">public</span> <span class="keyword">void</span> setVar(String var) {
    <span class="keyword">this</span>.var = var;
  }
}
</code></pre>
<div class="clear"></div>

</section>

注目するメソッドはsetProperties(UIComponent)です。このメソッドでは、タグの属性で指定された値をコンポーネントにセットする役割があります。　ValueBinding される可能性のある属性は、18行目のように UIComponentTag.isValueReference を使ってValueBinding形式かどうかを判定します。ValueBindingの場合は、FacesContextからValueBindingインスタンスを作成し、UIComponentのsetValueBindingを使ってセットします。

ただの値の場合には、コンポーネントのsetメソッドを使って直接セットします。

<h3>faces-context.xml と タグディスクリプションファイル（TLD)の作成</h3>

faces-context.xml に、カスタムコンポーネントとレンダラーの設定を書きます。TLDファイルには、タグハンドラの設定を書きます。

<h2>参考</h2>

+ 一番詳しい解説書
<div class="rakuten"><table  width="400" border="0" cellpadding="5"><tr><td colspan="2" >[ target="_blank">JavaServer Faces完全ガイド](http://www.amazon.co.jp/exec/obidos/ASIN/4873111978/sorehabooks-22/)</td></tr><tr><td valign="top">[ target="_blank"><img src="http://ec1.images-amazon.com/images/P/4873111978.09._SCMZZZZZZZ_.jpg"   border="0" alt="4873111978" />](http://www.amazon.co.jp/exec/obidos/ASIN/4873111978/sorehabooks-22/)</td><td valign="top" /><font size="-1">ハンス バーグステン Hans Bergsten 岩谷 宏 <br /><br /><iframe scrolling="no" frameborder="0" width="200" height="40" hspace="0" vspace="0" marginheight="0" marginwidth="0" src="http://webservices.amazon.co.jp/onca/xml?Service=AWSECommerceService&SubscriptionId=0G91FPYVW6ZGWBH4Y9G2&AssociateTag=goodpic-22&Operation=ItemLookup&IdType=ASIN&ContentType=text/html&Page=1&ResponseGroup=Offers&ItemId=4873111978&Version=2004-10-04&Style=http://www.g-tools.net/xsl/priceFFFFFF.xsl"></iframe><br />おすすめ平均  <img src="http://g-images.amazon.com/images/G/01/detail/stars-3-0.gif"   border="0" alt="star" /><br /><img src="http://g-images.amazon.com/images/G/01/detail/stars-5-0.gif"   border="0" alt="star" />結局この本が必要になります<br /><img src="http://g-images.amazon.com/images/G/01/detail/stars-4-0.gif"   border="0" alt="star" />JSFを理解するための希少な本<br /><img src="http://g-images.amazon.com/images/G/01/detail/stars-2-0.gif"   border="0" alt="star" />だめ翻訳<br /><img src="http://g-images.amazon.com/images/G/01/detail/stars-1-0.gif"   border="0" alt="star" />非技術者による翻訳？<br /><br />[ target="_blank" />Amazonで詳しく見る](http://www.amazon.co.jp/exec/obidos/ASIN/4873111978/sorehabooks-22/)</font><font size="-2">by [ >G-Tools](http://www.goodpic.com/mt/aws/index.html)</font></td></tr></table></div>

+ リファレンスとしては秀逸
<div class="rakuten"><table width="400"  border="0" cellpadding="5"><tr><td colspan="2" >[ target="_blank">JSF(JavaServer Faces)によるWebアプリケーション開発―Java徹底活用](http://www.amazon.co.jp/exec/obidos/ASIN/4798008303/sorehabooks-22/)</td></tr><tr><td valign="top">[ target="_blank"><img src="http://ec1.images-amazon.com/images/P/4798008303.09._SCMZZZZZZZ_.jpg"   border="0" alt="4798008303" />](http://www.amazon.co.jp/exec/obidos/ASIN/4798008303/sorehabooks-22/)</td><td valign="top" /><font size="-1">川崎 克巳 <br /><br /><iframe scrolling="no" frameborder="0" width="200" height="40" hspace="0" vspace="0" marginheight="0" marginwidth="0" src="http://webservices.amazon.co.jp/onca/xml?Service=AWSECommerceService&SubscriptionId=0G91FPYVW6ZGWBH4Y9G2&AssociateTag=goodpic-22&Operation=ItemLookup&IdType=ASIN&ContentType=text/html&Page=1&ResponseGroup=Offers&ItemId=4798008303&Version=2004-10-04&Style=http://www.g-tools.net/xsl/priceFFFFFF.xsl"></iframe><br />おすすめ平均  <img src="http://g-images.amazon.com/images/G/01/detail/stars-5-0.gif"   border="0" alt="star" /><br /><img src="http://g-images.amazon.com/images/G/01/detail/stars-5-0.gif"   border="0" alt="star" />JSF1.1に対応した初めての本<br /><br />[ target="_blank" />Amazonで詳しく見る](http://www.amazon.co.jp/exec/obidos/ASIN/4798008303/sorehabooks-22/)</font><font size="-2">by [ >G-Tools](http://www.goodpic.com/mt/aws/index.html)</font></td></tr></table></div>

+ 概念を学ぶにはいい本
<div class="rakuten"><table width="400"  border="0" cellpadding="5"><tr><td colspan="2" >[ target="_blank">よくわかるJavaServer Facesのしくみ](http://www.amazon.co.jp/exec/obidos/ASIN/4883732096/sorehabooks-22/)</td></tr><tr><td valign="top">[ target="_blank"><img src="http://ec1.images-amazon.com/images/P/4883732096.09._SCMZZZZZZZ_.jpg"   border="0" alt="4883732096" />](http://www.amazon.co.jp/exec/obidos/ASIN/4883732096/sorehabooks-22/)</td><td valign="top" /><font size="-1">吉田 裕之 松塚 貴英 <br /><br /><iframe scrolling="no" frameborder="0" width="200" height="40" hspace="0" vspace="0" marginheight="0" marginwidth="0" src="http://webservices.amazon.co.jp/onca/xml?Service=AWSECommerceService&SubscriptionId=0G91FPYVW6ZGWBH4Y9G2&AssociateTag=goodpic-22&Operation=ItemLookup&IdType=ASIN&ContentType=text/html&Page=1&ResponseGroup=Offers&ItemId=4883732096&Version=2004-10-04&Style=http://www.g-tools.net/xsl/priceFFFFFF.xsl"></iframe><br />おすすめ平均  <img src="http://g-images.amazon.com/images/G/01/detail/stars-5-0.gif"   border="0" alt="star" /><br /><img src="http://g-images.amazon.com/images/G/01/detail/stars-5-0.gif"   border="0" alt="star" />コンパクトで分かり易い<br /><br />[ target="_blank" />Amazonで詳しく見る](http://www.amazon.co.jp/exec/obidos/ASIN/4883732096/sorehabooks-22/)</font><font size="-2">by [ >G-Tools](http://www.goodpic.com/mt/aws/index.html)</font></td></tr></table></div>




