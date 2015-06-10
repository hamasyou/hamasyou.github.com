---
layout: post
title: "jpmobile と kaminari を使ったときに invalid byte sequence in Shift_JIS が表示される対応"
date: 2011-06-14 11:29
comments: true
categories: [Blog]
keywords: "Rails, Ruby, jpmobile, kaminari"
tags: [jpmobile,kaminari,Rails,Ruby]
author: hamasyou
amazon_url: ""
amazon_author: ""
amazon_image: ""
amazon_publisher: ""
---

jpmobile と kaminari を組み合わせたときに invalid byte sequence in Shift_JIS が表示される問題の原因と対策です。

<section>

<h4>環境</h4>

<dl><dt>Rails</dt><dd>3.0.7</dd><dt>jpmobile</dt><dd>0.1.6</dd><dt>kaminari</dt><dd>0.12.4</dd></dl>

</section>


<!-- more -->

jpmobile の mobile_filter を通して kaminari でページングを行うときに、invalid byte sequence in Shift_JIS エラーが出る場合があります。

これは、kaminari で生成するページングリンク内に、UTF-8 の文字エンコーディングの日本語文字が入っているのが原因です。

kaminari がページリンクを作成するときに、日本語文字をパーセントエンコード（URLエンコード）していて、jpmobile の mobile_filter がエンコードされている文字を UTF-8 だと認識出来ずに表示してしまうのが原因になっています。

この組み合わせで kaminari のページリンク内に日本語パラメータがはいってしまうのは、おそらくフォームのサブミットボタンの value が原因だと思います。

ほとんどの場合、サブミットボタンの value は必要ないと思うので、submit_tag の :name オプションに nil を設定してサブミットボタンの value を送らないようにするといいかと思います。

```html+erb
<%= form_tag user_path do %>
  <%= select_tag "category", options_from_collection_for_select(Category.all, :id, :label, params[:category]) %>
  <%= submit_tag "表示", :name => nil %>
<% end %>
```
