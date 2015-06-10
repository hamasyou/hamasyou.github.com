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

```ruby
require "mail"

Mail.defaults do
  retriever_method :imap, {:address => "imap.gmail.com",
                           :port => 993,
                           :user_name => "<mailaddress@domain.com>",
                           :password => "<password>",
                           :enable_ssl => true}
end

Mail.all(:delete_after_find => true).each do |email|
  begin
    if !email.attachments.blank?
      subject = email.subject   # => 件名（日本語可OK） UTF-8 で取得できる
      body = email.parts[0].body.to_s.encode("UTF-8", "ISO-2022-JP")    # => 本文は UTF-8 に変換する必要がある
      from = email[:from]       # => "\"濱田 章吾\" hamasyou@gmail.com"
      sent_at = email.date
      email.attachments.each do |attachment|
        tmp = File.new("tmp/photos/#{attachment.filename}", "wb")
        tmp << attachment.read.force_encoding("UTF-8")
        tmp.close
      end
    end
  rescue => ignore
    p "[error]:" + ignore.to_s
  end
end
```

ここでは、マルチパートの場合メールのパートの最初に本文があることを決め打ちしています。また、メールのエンコーディングが ISO-2022-JP であることも決め打ちしています。

メール本文のエンコーディングを調べて、それを UTF-8 にするようにしたほうが良いです。




