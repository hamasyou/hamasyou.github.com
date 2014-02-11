---
layout: post
title: "[JavaScript] JavaScript の変数宣言のスコープ"
date: 2010-09-24 10:57
comments: true
categories: [Blog]
keywords: "JavaScript, 変数, スコープ"
tags: [JavaScript]
author: hamasyou
amazon_url: ""
amazon_author: ""
amazon_image: ""
amazon_publisher: ""
---

JavaScript で忘れがちな変数宣言に関してのメモ。


<!-- more -->

<h2>変数の宣言</h2>

<h3>変数宣言は関数のどこでしても関数の先頭から有効</h3>

変数を宣言せずに使用すると "変数" is not defined と怒られます。

<pre class="code">alert(hoge);
<span class="rem">// =&gt; hoge is not defined</span></pre>

使用する場所よりも後で宣言していると <em>undefined</em> となります。

宣言はされていても、初期化されていないということです。

<pre class="code">alert(hoge);
 
<span class="keyword">var</span> hoge = <span class="str">&quot;Hello World&quot;</span>;
<span class="rem">// =&gt; undefined</span></pre>

宣言は関数のどこでしても有効になるが、初期化や代入は宣言順になります。

<h3>変数宣言は同一関数内のすべての場所で有効</h3>

if 文や for ループの中で変数を宣言しても、やっぱり関数の先頭で宣言したこととになります。

if 文で条件が一致しない場合の方で宣言していても有効になる。（undefined なのは宣言はされているが初期化されていないからです。）

<pre class="code">alert(hoge);
 
if (0) {
  <span class="keyword">var</span> hoge = <span class="str">&quot;Hello World&quot;</span>;
}
<span class="rem">// =&gt; undefined</span></pre>

<h3>関数がネストされているところで宣言されるのはだめ</h3>

関数の中で関数を宣言して、そこで変数を宣言してもダメ。同一の関数内でのみ変数宣言は有効になります。

<pre class="code">(<span class="keyword">function</span>() {
  alert(hoge);
  (<span class="keyword">function</span>() {
    <span class="keyword">var</span> hoge = <span class="str">&quot;Hello World&quot;</span>;
  })();
})();
<span class="rem">// =&gt; hoge is not defined</span></pre>

<h3>まとめ: 変数宣言は関数の最初に行う</h3>

変数宣言は関数の最初にまとめて行うのがよさそうです。




