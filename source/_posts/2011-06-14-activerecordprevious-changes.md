---
layout: post
title: "ActiveRecord#previous_changes で変更のあった属性を取り出す"
date: 2011-06-14 09:43
comments: true
categories: [Blog]
keywords: "Rails3, ActiveRecord, previous_changes"
tags: [ActiveRecord,Rails3]
author: hamasyou
amazon_url: ""
amazon_author: ""
amazon_image: ""
amazon_publisher: ""
---

ActiveRecord のモデルで変更のあった属性だけを抜き出す方法に、previous_changes メソッドが使える。

<section>

<h4>環境</h4>

<dl><dt>Rails</dt><dd>3.0.7</dd></dl>

</section>


<!-- more -->

ActiveRecord で更新があった属性を取得するメソッドに、previous_changes があります。

これを使えば、データ更新後に、更新した属性のみをメールに書いて送る！みたいな処理が簡単に書けますね。

<dl><dt>previous_changes</dt>
<dd>Returns a Hash of previous changes before the object was persisted, with the attribute names as the keys, and the values being an array of the old and new value for that field.</dd></dl>

<pre class="code">user = User.find(params[<span class="symbol">:id</span>])
user.name
  <span class="rem"># =&gt; &quot;Syougo Hamada&quot;</span>
user.name = <span class="str">&quot;hamasyou&quot;</span>
user.age
  <span class="rem"># =&gt; 28</span>
user.age = <span class="num">29</span>
user.save
 
user.previous_changes
  <span class="rem"># =&gt; {&quot;name&quot;=&gt;[&quot;Syougo Hamada&quot;, &quot;hamasyou&quot;], &quot;age&quot;=&gt;[28, 29]}</span></pre>





