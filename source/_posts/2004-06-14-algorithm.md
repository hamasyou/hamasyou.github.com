---
layout: post
title: "ループ構造の正しさを証明してやる！"
date: 2004-06-14 03:37
comments: true
categories: [Blog]
keywords: "Code Reading,プログラミング,ループ,妥当性,バリアント,インバリアント"
tags: [アルゴリズム]
author: hamasyou
amazon_url: ""
amazon_author: ""
amazon_image: ""
amazon_publisher: ""
---

<p>
<a href="http://www.amazon.co.jp/exec/obidos/ASIN/4839912653/sorehabooks-22" rel="external nofollow"></a>
</p>

<a href="http://www.amazon.co.jp/exec/obidos/ASIN/4839912653/sorehabooks-22" rel="external nofollow">Code Reading</a>を読んで、ループ性能(妥当性)に関しての議論を考えるときに、有効な手段があると書いてあった。<b>「バリアント(variant)」と「インバリアント(invariant)」</b>という概念を使う方法だ。「バリアント(variant)」とは、変わりやすいとか変更されるといった意味を持っている。

逆に「インバリアント(invariant)」は、変わらないとか不変なとかという意味を持っているらしい。

この「バリアント(variant)」と「インバリアント(invariant)」を使って、ループ構造が正しいかを証明する方法が、<a href="http://www.amazon.co.jp/exec/obidos/ASIN/4839912653/sorehabooks-22" rel="external nofollow">Code Reading</a>の2章に載っていたので、忘れないうちに復習しておきたいと思います。

それにしても、<a href="http://www.amazon.co.jp/exec/obidos/ASIN/4839912653/sorehabooks-22" rel="external nofollow">Code Reading</a>は面白いですね。技術者のツボをついた本だと思います。


<!-- more -->

<h2>概要と解説</h2>

ループ処理の開始と終了の時点で共に成り立つ条件を<b>ループインバリアント</b>と呼ぶそうです。直訳すると、「回る不変式」 ・・・何のこっちゃ(--;)。

{% blockquote 本書 %}
このループ処理中にインバリアント(不変式)が常に成り立つことを証明し、かつループ終了時に期待する値が得られたことを示すことができるようにインバリアント(不変式)を定めることで、ループのアルゴリズムが正しい結果の範囲内で機能することを保証できます。


{% endblockquote %}

(・・・わかったようなわからないような)

まあ要するに、ある定めた式がループ中に常に真になり続けたままループが終了したら、このループは正しく終了したってことが証明できるらしいです。ループが終了することを証明したり、期待した値を示す式を定めたりって事をしないと証明にならないらしいのですが。

とりあえず、例を見ながら整理しておきます。配列の最大値を求めるループがあるとします。

<pre class="code"><code>max = array[n];
<span class="keyword">while</span> (n--) {
  <span class="keyword">if</span> (array[n] &gt; max) {
    max = array[n];
  }
}
</code></pre>
<div class="clear"></div>

つぎに、以下の式をループ終了時に期待する結果を返す式として定義します。

<pre class="code"><code>max = maximum{array[0 : n)}</code></pre>

maximum{array[0 : n)} は、配列 array の要素 0からn -1の中から最大の値を返します。

この結果を証明するためのインバリアント(不変式)は次のように定義します。ループ中の max = array[n]; で示される部分を抜き出した感じです。

<pre class="code"><code>max = maximum{array[x : n)}</code></pre>

<b>このインバリアント(不変式)が常に成り立つことが証明できれば、ループが正しいということになります。</b>逆に考えると、インバリアント(不変式)を常に真に保つようなコーディングをすればよいとも言えます。

このインバリアント(不変式)は、maxに最初の値がセットされたときに開始されるので、ループの最初では妥当性が保たれます。2行目の nのデクリメントで maximum{array[x-1 : n)} は、要素の範囲が広がり、maxの値よりも大きい値を持つ可能性がでてきました。インバリアント(不変式)を真に保つために、3行目のif文で新しい要素がmaxよりも大きいかを調べ、4行目の代入式でmaxを現時点での最大に設定しなおしています。

これでインバリアント(不変式)は、2度目のループでも真に保たれることを保証できました。ループを繰り返してもインバリアント(不変式)は真になることがわかります。whileループが終了する条件は、n==0になったときですので、ループが終了することも確認できました。

ということで、最大値を返す<code>max = maximum{array[0 : n)}</code>は期待する値(最大値)を返すことを証明できました。

以上が、「バリアント｣と「インバリアント」を使った<b>ループ構造が正しいかどうかの証明</b>ということになります。(たぶん・・・)

こんな風にして、ループ処理の正しさを証明する方法があるということを、Code Readingを読んでしりました。結構奥が深くて、楽しめそうだなぁと思いつつ、次の章に読みふけっていくのであった・・・。



