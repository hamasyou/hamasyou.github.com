---
layout: post
title: "Ruby から Gmail を受信する（マルチパート編）"
date: 2011-03-17 22:24
comments: true
categories: [Blog]
keywords: "Ruby, GMail, imap, メール受信"
tags: [Gmail,imap,Ruby]
author: hamasyou
amazon_url: ""
amazon_author: ""
amazon_image: ""
amazon_publisher: ""
---

Ruby1.9.2 で mail を使って Gmail から添付ファイルやメール本文を受信する方法のメモです。

かなり力技的なところがあるので、もっといいプログラムになると思いいますが、メモということで。

<section>

<h4>バージョン</h4>

<dl><dt>Ruby</dt><dd>1.9.2</dd>
<dt>Rails</dt><dd>3.0.4</dd>
<dt>mail</dt><dd>2.2</dd>
</dl>

</section>



<!-- more -->

<h2>必要なライブラリ</h2>

<h3>mikel / mail</h3>

<pre><a href="https://github.com/mikel/mail" rel="external nofollow">mail</a></pre>

Ruby1.8 までは、TMail というライブラリを使うとメール処理を簡易に書けるようですが、Ruby1.9から文字エンコーディング周りの問題でこのライブラリを使うと良いことを知りました。

<pre class="console">gem install mail</pre>

Rails で使う場合は次のようにします。

<pre class="console">rails plugin install https://github.com/mikel/mail</pre>

<h2>サンプルソースコード</h2>

<pre class="code">require <span class="str">&quot;mail&quot;</span>
 
Mail.defaults <span class="keyword">do</span>
  retriever_method :imap, {<span class="symbol">:address</span> =&gt; <span class="str">&quot;imap.gmail.com&quot;</span>,
                           <span class="symbol">:port</span> =&gt; <span class="num">993</span>,
                           <span class="symbol">:user_name</span> =&gt; <span class="str">&quot;&lt;mailaddress@domain.com&gt;&quot;</span>,
                           <span class="symbol">:password</span> =&gt; <span class="str">&quot;&lt;password&gt;&quot;</span>,
                           <span class="symbol">:enable_ssl</span> =&gt; <span class="keyword">true</span>}
<span class="keyword">end</span>
 
Mail.all(<span class="symbol">:delete_after_find</span> =&gt; <span class="keyword">true</span>).each <span class="keyword">do</span> |email|
  <span class="keyword">begin</span>
    <span class="keyword">if</span> !email.attachments.blank?
      subject = email.subject   <span class="rem"># =&gt; 件名（日本語可OK） UTF-8 で取得できる</span>
      body = email.parts[<span class="num">0</span>].body.to_s.encode(<span class="str">&quot;UTF-8&quot;</span>, <span class="str">&quot;ISO-2022-JP&quot;</span>)    <span class="rem"># =&gt; 本文は UTF-8 に変換する必要がある</span>
      from = email[<span class="symbol">:from</span>]       <span class="rem"># =&gt; &quot;\&quot;濱田 章吾\&quot; hamasyou@gmail.com&quot;</span>
      sent_at = email.date
      email.attachments.each <span class="keyword">do</span> |attachment|
        tmp = File.new(<span class="str">&quot;tmp/photos/#{attachment.filename}&quot;</span>, <span class="str">&quot;wb&quot;</span>)
        tmp &lt;&lt; attachment.read.force_encoding(<span class="str">&quot;UTF-8&quot;</span>)
        tmp.close
      <span class="keyword">end</span>
    <span class="keyword">end</span>
  <span class="keyword">rescue</span> =&gt; ignore
    p <span class="str">&quot;[error]:&quot;</span> + ignore.to_s
  <span class="keyword">end</span>
<span class="keyword">end</span></pre>

ここでは、マルチパートの場合メールのパートの最初に本文があることを決め打ちしています。また、メールのエンコーディングが ISO-2022-JP であることも決め打ちしています。

メール本文のエンコーディングを調べて、それを UTF-8 にするようにしたほうが良いです。




