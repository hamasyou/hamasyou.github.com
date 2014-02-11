---
layout: post
title: "実践デザパタ-その6:Decoratorパターン"
date: 2004-06-03 14:49
comments: true
categories: [Blog]
keywords: "デザインパターン,Decorator,デコレータ,GoF,Java"
tags: [デザインパターン]
author: hamasyou
amazon_url: ""
amazon_author: ""
amazon_image: ""
amazon_publisher: ""
---

デザインパターンとは、システム設計におけるクラスやインターフェースの関係に名前をつけたものです。
GoFの23のパターンが有名です。デザインパターンというのは、どんなパターンなのか、パターンの目的は何かということを覚えることが非常に重要なのですが、これを実際に適用しようとした場合に、いつ適用していいかが見えてこないとお話になりません。

そこで、自分の勉強も兼ねつつ、パターンの実践時における使用場所や、パターンを適用するきっかけを見つけられるようにメモしておきます。

6回目は、機能拡張を統一的な方法で、自由に行うことができる<b>Decorator</b>パターンです。


<!-- more -->

<h2>Decoratorパターン</h2>

<p class="option">Decoratorパターンは、既存のクラスに機能を付け加えたい場合、
既存機能に、少しだけ修飾を加えたい場合に使われるパターンです。</p>

<img src="http://hamasyou.com/images/design_pattern/decorator.gif" alt="Decoratorのクラス図" />

Javaのクラスライブラリの例として、java.ioパッケージが最初に思い浮かびます。Javaには入出力(I/O)機能として、java.ioパッケージが用意されています。ファイルから一行ずつ読み取り、行番号をつけてファイルに書き込むという処理があるとします。

<pre class="code"><code><span class="keyword">import</span> java.io.*;
<span class="keyword">public</span> <span class="keyword">class</span> LineNumberAllocator {
  <span class="keyword">public</span> <span class="keyword">void</span> allocateLineNumber(String inFile, String outFile) 
      <span class="keyword">throws</span> IOException {
    LineNumberReader in = <span class="keyword">new</span> LineNumberReader(<span class="keyword">new</span> FileReader(inFile));
    PrintWriter out = 
      <span class="keyword">new</span> PrintWriter(<span class="keyword">new</span> BufferedWriter(<span class="keyword">new</span> FileWriter(outFile)));
 
    String line = <span class="keyword">null</span>;
    <span class="keyword">while</span> ( (line = in.readLine()) != <span class="keyword">null</span> ) {
      out.println(in.getLineNumber() + <span class="literal">":"</span> + line);
    }
    out.close();
    in.close();
  }	
}
</code></pre>
<div class="clear"></div>

4,5行目がDecoratorパターンが使われている部分です。FileReaderクラスの機能に行番号をつけるために、デコレートしているのが5行目です。FileWriterクラスのFile書き込み処理をバッファリングして、行ごとに書き出すためにデコレートしているのが6行目になります。

<b>Decoratorパターンは、機能の拡張を、「元となるオブジェクト + 自クラスの機能」という方法で、実装しています。</b>上記Javaでの例(太文字)の部分からなんとなく、雰囲気はつかめるかと思います。(newでオブジェクトを作り、機能拡張するクラスに渡している部分です。)

<h2>パターンの適用タイミング</h2>

Decoratorパターンは、すでにあるクラスに対して機能拡張を行うことを目的としています。オブジェクト指向に<b class="red">開放・閉鎖原則(Open・Closed Principle)</b>というのがあります。

クラスの拡張には開いていて、クラスの修正には閉じている。<b>つまり機能追加を自由にできるが、既存のクラスには修正を必要としない</b>というのがこの原則の意味するところです。Decoratorパターンは、まさにこの原則に則っています。

<strong>既存のクラスに機能を拡張したい場合</strong>、<strong>オブジェクトに修飾する形で、機能を付け加えたい場合</strong>に使えるパターンです。

<h2>実装サンプルと参考文献</h2>

Decoratorパターンの実装方法をもっと詳しく知りたい場合は、下記のサイトにアクセスするのをお勧めします。もしくは、参考書籍を載せておきますので、そちらをお買い求めください。(^^;

+ 日立ソフト(Decoratorパターン)
<a href="http://www.dmz.hitachi-sk.co.jp/Java/Tech/pattern/gof/decorator.html" rel="external nofollow">日立ソフト</a>

+ Skeleton of GOF's Design Pattern(JavaとC++のサンプルがあります)
<a href="http://www002.upp.so-net.ne.jp/ys_oota/mdp/Decorator/index.htm" rel="external nofollow">DECORATORの骸骨</a>

+ オブジェクト倶楽部(デザインパターン入門)
<a href="http://www.objectclub.jp/technicaldoc/pattern/DPforJavaProgrammers" rel="external nofollow">オブジェクト倶楽部</a>

+ デザインパターンのお勧め書籍
<div class="rakuten"><table border="0" cellpadding="5" width="400"><tr><td valign="top"><a href="http://www.amazon.co.jp/exec/obidos/ASIN/4797327030/sorehabooks-22/" rel="external nofollow"></a><br /></td></tr></table>
</div>

+ 独習シリーズのデザインパターン編。デザインパターンを一人でも学べます。
<div class="rakuten"><table border="0" cellpadding="5" width="400"><tr><td valign="top"><a href="http://www.amazon.co.jp/exec/obidos/ASIN/4798104450/sorehabooks-22/" rel="external nofollow"></a><br /></td></tr></table>
</div>

+ Sun Microsystemのお墨付き。GoF以外のパターンも学べます。
<div class="rakuten"><table border="0" cellpadding="5" width="400"><tr><td valign="top"><a href="http://www.amazon.co.jp/exec/obidos/ASIN/4756141552/sorehabooks-22/" rel="external nofollow"></a><br /></td></tr></table>
</div>

+ UMLを使って、オブジェクト指向のいいとこ取りができます。
<div class="rakuten"><table border="0" cellpadding="5" width="400"><tr><td valign="top"><a href="http://www.amazon.co.jp/exec/obidos/ASIN/4774116882/sorehabooks-22/" rel="external nofollow"></a><br /></td></tr></table>
</div>

+ デザインパターンだけではなく、ソフトウェア設計の原則やプラクティスまで学びたい人におすすめ
<div class="rakuten"><table border="0" cellpadding="5" width="400"><tr><td valign="top"><a href="http://www.amazon.co.jp/exec/obidos/ASIN/4797323361/sorehabooks-22/" rel="external nofollow"></a><br /></td></tr></table>
</div>




