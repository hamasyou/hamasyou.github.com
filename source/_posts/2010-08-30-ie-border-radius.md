---
layout: post
title: "IE で border-radius を使う方法"
date: 2010-08-30 00:29
comments: true
categories: [Blog]
keywords: "jQuery,InternetExplorer, CSS3"
tags: [IE,CSS3,角丸]
author: hamasyou
amazon_url: ""
amazon_author: ""
amazon_image: ""
amazon_publisher: ""
---

CSS3 から使えるようになった border-radius を IE でも使う方法です。IE でも使うと言っては語弊がありますが、IE でも角丸を表現する方法です。それは Blog で使っている方法をメモ。


<!-- more -->

<h2>IE で角丸を表現する方法</h2>

ご存じの通り、IE6、IE7、IE8 では CSS3 の <strong>border-radius</strong> を使って角丸を表現することはできません。んじゃ、どうやって角丸を実現するのかというと次のような方法があります。

<dl>
<dt>border-radius.htc を使う</dt>
<dd><p><em>VML</em> を利用して、behavior を使って border-radius を実現する方法</p></dd>
<dt>border-radius.js を使う</dt>
<dd><p>JavaScript を使って border-radius を実現する方法</p></dd>
<dt>JQuery Corner を使う</dt>
<dd><p>jQuery Corner という jQuery プラグインを使って border-radius を実現する方法</dd>
</dl>

さくっと Google 先生に聞いたところ、3つくらいやり方があるようでした。で、うちのブログでは3つめの、<strong>JQuery Corner</strong> を使いました。

<h3>border-radius.htc のダメなところ</h3>

さっと検索したところ、border-radius.htc が一番簡単に角丸を実現出来そうだったのですが、如何せん互換モードでしか動かない。。うちのサイトは、HTML5 の標準モードで作っているので、これが動かない！！

ちなみに、HTML5 で標準モードは DOCTYPE に html を与えてやればいいだけらしいです。

<pre>&lt;!DOCTYPE html&gt;</pre>

まぁ、互換モードでしか動かないとなると、使えないということで、これは×。

<h4>ってか、標準モードでも動くように手をいれてる人発見</h4>

<a href="http://www.revulo.com/blog/20071014.html" rel="external nofollow">VML を利用して IE で border-radius が使えるようにする」 れぶろぐ</a>

これ、試したらうまくいくのかな？やってないけど。

<h3>border-radius.js のダメなところ</h3>

border-radius.js は、javaScript を使って角丸を表現することができるらしいです。適用したい要素の class 属性に html5jp-border-radius という値を指定すればよいらしいですが、これがめんどくさい。というか、<em>そもそも IE8 でうごかない</em>という。。。自宅の環境、IE8 しか入ってないし、確認できないと意味ないじゃん。。ということでこれも×。

<h3>ということで、最後の頼みの綱 jQuery</h3>

jQuery 大好きな僕としては、これに賭ける！ということで導入してみました。

<a href="http://www.malsup.com/jquery/corner/" rel="external nofollow">JQuery Corner</a>

これを使うと、角丸にしたいところを下みたいな感じでやれちゃう。

<pre class="code"><code>$("div").corner();</code></pre>

ただ、要注意なのが、<em>白背景のブロック要素に対して行うと、ボーダーが見えなくなっちゃう</em>ので角丸になってるかよく分かりません。。。ということで、どうすればいいかというと、外側に一つラッパーを作ってやってラッパーの背景にボーダー色を設定し、ラッパーとのマージン差で線ぽっく見せると。

こんな感じ。

<section>

<h4>jQuery Corner の使用例</h4>

<pre class="code"><code><span class="keyword">var</span> $wrapper = $(<span class="str">&quot;&lt;div/&gt;&quot;</span>);
$(<span class="str">&quot;.hoge&quot;</span>).wrap($wrapper);
$(<span class="str">&quot;.hoge&quot;</span>).css(<span class="str">&quot;margin&quot;</span>, <span class="str">&quot;0&quot;</span>);
$wrapper = $(<span class="str">&quot;.hoge&quot;</span>).parent();  <span class="rem">// なぜかこれが必要。。。</span>
$wrapper.css(<span class="str">&quot;background-color&quot;</span>, <span class="str">&quot;ここに線の色を指定する&quot;</span>);
$wrapper.css(<span class="str">&quot;margin&quot;</span>, <span class="str">&quot;0&quot;</span>);
$wrapper.css(<span class="str">&quot;padding&quot;</span>, <span class="str">&quot;1px&quot;</span>);
$wrapper.corner(<span class="str">&quot;5px&quot;</span>);
$(<span class="str">&quot;.hoge&quot;</span>).corner(<span class="str">&quot;5px&quot;</span>);</code></pre>

</section>

適当に div タグ作って角丸にしたい要素（.hoge）に wrap します。んで、角丸にしたい要素の margin を 0 に設定してラッパーとの間をなくします。

その後、JQuery Corner の corner を使ってラッパーと角丸にしたい要素の両方を丸くした後、padding で線の太さ分だけずらします。

一応、うまくいってます。下の枠が一応テスト用ということで。

<h4>テスト要素</h4>

<script type="text/javascript">
jQuery(function($) {
  var $wrapper = $("<div/>");
  $("#radius-target").wrap($wrapper);
  $("#radius-target").css("margin", "0");
 
  $wrapper = $("#radius-target").parent();
  $wrapper.css("background-color", "#31B5D6");
  $wrapper.css("margin", "0");
  $wrapper.css("padding", "2px");     // 2px の枠線
  $wrapper.corner("tl 12px");         // 左上だけ角丸にする
  $("#radius-target").corner("tl 12px");
});
</script>

<div id="radius-target" style="background-color: #FFF; padding: 15px;">ほげほげほげほげほげほげほげほげ<br />ほげほげほげほげほげほげほげほげ<br />ほげほげほげほげほげほげほげほげ</div>

<h3>JQuery Corner についてもう少し</h3>

<section>

<h4>ダウンロード</h4>

<a href="http://www.malsup.com/jquery/corner/" rel="external nofollow">JQuery Corner</a>

</section>

jQuery Corner ですが、CSS3 の border-radius 、Webkit の -webkit-border-radius、 Mozira の -moz-border-radius のいずれかが利用可能な場合にはデフォルトでそちらのスタイルを適用します。なので、あらかじめ CSS でこれらの設定をしておいた後、IE のためだけに jQuery でごりごりやるという形になるかと思います。なんて IE に対して手厚い保護なんでしょうか。。。

で、IE かどうか（というか、上記の3属性が使えるかどうか）を判定するのに、<code>$.support.borderRadius</code> が使えます。

<pre class="code"><code><span class="keyword">if</span> (!($.support.borderRadius)) {
  <span class="comment">// ここに IE のための処理を。。</span>
}</code></pre>

と、こんな感じで。ちなみに、デフォルト動作（border-radius 要素を使うかどうか）は、$.fn.corner.defaults.useNative で切り替えできます。

<pre class="code"><code>$.fn.corner.defaults.useNative = <span class="keyword">false</span>;</code></pre>




