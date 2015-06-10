---
layout: post
title: "[JavaScript] JavaScript のクラス定義のイディオム"
date: 2010-09-07 23:32
comments: true
categories: [Blog]
keywords: "JavaScript, クラス設計, コーディングテクニック"
tags: [JavaScript]
author: hamasyou
amazon_url: ""
amazon_author: ""
amazon_image: ""
amazon_publisher: ""
---

JavaScript でクラス定義するときのメモ。


<!-- more -->

<h2>クラス定義の基本</h2>

JavaScript のクラス定義は function() を使って行う。

<pre class="code"><span class="keyword">var</span> MyClass = <span class="keyword">function</span>(name) {
  <span class="rem">/* ここにプロパティの初期化コードを書く */</span>
  <span class="keyword">this</span>.name = name;
}
 
MyClass.<span class="keyword">prototype</span>.myMethod = <span class="keyword">function</span>() {
  <span class="rem">/* prototype.メソッド名 の形でメソッドを定義する */</span>
  alert(<span class="keyword">this</span>.name);
}
 
<span class="keyword">var</span> obj = <span class="keyword">new</span> MyClass(<span class="str">&quot;hamasyou&quot;</span>);
obj.myMethod();    <span class="rem">// =&gt; &quot;hamasyou&quot;</span>
</pre>

<h2>ネームスペースをつけてクラスを宣言する</h2>

いくつかやり方がありますが、一番素直なやり方で。

<pre class="code"><span class="keyword">var</span> com = {};
<span class="keyword">if</span> (<span class="keyword">typeof</span>(com.hamasyou) == <span class="str">&quot;undefined&quot;</span>) com.hamasyou = {};
(<span class="keyword">function</span>() {
  <span class="keyword">var</span> ns = com.hamasyou;
  ns.MyClass = <span class="keyword">function</span>(name) {
    <span class="rem">/* ここにプロパティの初期化コードを書く */</span>
    <span class="keyword">this</span>.name = name;
  };
  ns.MyClass.<span class="keyword">prototype</span>.myMethod = <span class="keyword">function</span>() {
    <span class="rem">/* ここにメソッドの中身を書く */</span>
    alert(<span class="keyword">this</span>.name);
  };
})();
 
<span class="keyword">var</span> obj = <span class="keyword">new</span> com.hamasyou.MyClass(<span class="str">&quot;hamasyou&quot;</span>);
obj.myMethod();        <span class="rem">// =&gt; &quot;hamasyou&quot;</span>
</pre>

<section>

<h4>ネームスペースあれこれ</h4>

<a href="http://blog.livedoor.jp/dankogai/archives/50754803.html" rel="external nofollow">javascript - ふつうのnamespace - 404 Blog Not Found</a>

<a href="http://d.hatena.ne.jp/onozaty/20070326/p1" rel="external nofollow">[JavaScript]undefine - Enjoy*Study</a>

</section>

<h2>クラス定義もうひとつ</h2>

プロトタイプを上書きして、コンストラクタをプロトタイプに実装する方法。

<a href="http://d.hatena.ne.jp/amachang/20060516/1147778600" rel="external nofollow">JavaScript OOP におけるクラス定義方法 - IT戦記</a>

<pre class="code"><span class="keyword">var</span> com = {};
<span class="keyword">if</span> (<span class="keyword">typeof</span>(com.hamasyou) == <span class="str">&quot;undefined&quot;</span>) com.hamasyou = {};
(<span class="keyword">function</span>() {
  <span class="keyword">var</span> ns = com.hamasyou;
  ns.MyClass = <span class="keyword">function</span>() {
    <span class="keyword">this</span>.initialize.apply(<span class="keyword">this</span>, arguments);
  };
  ns.MyClass.<span class="keyword">prototype</span> = {
    initialize: <span class="keyword">function</span>(name) {
      <span class="keyword">this</span>.name = name;
    },
    myMethod: <span class="keyword">function</span>() {
      <span class="rem">/* ここにメソッドの中身を書く */</span>
      alert(<span class="keyword">this</span>.name);
    }
  };
})();
 
<span class="keyword">var</span> obj = <span class="keyword">new</span> com.hamasyou.MyClass(<span class="str">&quot;hamasyou&quot;</span>);
obj.myMethod();        <span class="rem">// =&gt; &quot;hamasyou&quot;</span>
</pre>




