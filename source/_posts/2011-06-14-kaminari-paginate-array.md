---
layout: post
title: "kaminari の paginate_array が便利"
date: 2011-06-14 09:31
comments: true
categories: [Blog]
keywords: "Ruby, kaminari, paginate_array"
tags: [plugin,Rails,Ruby]
author: hamasyou
amazon_url: ""
amazon_author: ""
amazon_image: ""
amazon_publisher: ""
---

will_paginate に代わるページングのライブラリ、kaminari ですが、ページングが scope 扱いで配列に対して使えなかったのでちょっと不便なときがあったのですが、なんと paginate_array なるものを発見しました。

<section>

<h4>環境</h4>

<dl><dt>kaminari</dt><dd>0.12.4</dd></dl>

</section>


<!-- more -->

<a href="http://rubygems.org/gems/kaminari" rel="external nofollow">kaminari</a> の <strong>Kaminari.paginate_array</strong> が激しく便利！

もともと、kaminari が扱う page は scope だったかと思います。なので、配列（Array）に対しては使えませんでした。

で、ちょっとソースを見ていたらびっくりするものを発見！

<strong>Kaminari.paginate_array</strong>

なんと、配列をページングできるようになっていました。これで、Entity.all したものもページングできるようになりますね。

使い方は、scope の方の page と同じ。

```ruby
Kaminari.paginate_array(array_obj).page(params[:page])
```

激しくべんり！

<section>

<h4>kaminari の解説で参考になるサイト</h4>

<a href="http://memo.yomukaku.net/entries/238" rel="external nofollow">Kaminariの使い方 Rails3時代のpaginationの標準候補 - YomuKaku Memo</a>

</section>




