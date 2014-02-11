---
layout: post
title: "Rails のコントローラの中で View の画像URLを取得する方法"
date: 2011-06-14 09:09
comments: true
categories: [Blog]
keywords: "Rails, Ruby, view_context"
tags: [Rails3,Ruby1.9]
author: hamasyou
amazon_url: ""
amazon_author: ""
amazon_image: ""
amazon_publisher: ""
---

コントローラの中で、ビューの image_tag 等で表示される画像の URL を取得したい時があります。

そんな時は <em>view_context</em> が使えます。

<section>

<h4>環境</h4>

<dl><dt>Rails</dt><dd>3.0.7</dd><dt>Ruby</dt><dd>1.9.2-p180</dd><dt>Paperclip</dt><dd>2.3.11</dd></dl>

</section>


<!-- more -->

コントローラの中でビューの image_tag に渡すパスを取得したい時があります。

例えば、Paperclip を使って画像を管理しているときに、外部サービスに Paperclip の画像の URL を渡したい時などです。

Paperclip を使って画像を管理していると、<code>url</code> メソッドで取得できる URL に、コントローラの中では ActionController の AssetHost の設定が付与されません。

そんな時は、<strong>view_context</strong> を使うと、ビューのコンテキストで評価してくれるので、ActionController の AssetHost の設定もきくようになります。

<section>

<h4>コントローラの例</h4>

<pre class="code"><span class="rem">#-*- encoding: utf-8 -*-</span>
 
<span class="keyword">class</span> CallbackController &lt; ApplicationController
  <span class="keyword">def</span> send
    user = User.new
    user.avatar = params[<span class="symbol">:user</span>][<span class="symbol">:avatar</span>]
    user.avatar.url
      <span class="rem"># =&gt; &quot;/users/avatars/4/original_me.jpg&quot;</span>
    <strong>view_context</strong>.image_path(user.avatar.url)
      <span class="rem"># =&gt; &quot;http://image.serverhost/users/avatars/4/original_me.jpg&quot;</span>
  <span class="keyword">end</span>
<span class="keyword">end</span></pre>

</section>

<section>

<h4>models/user.rb</h4>

<pre class="code"><span class="keyword">class</span> User &lt; ActiveRecord::Base
  has_attached_file <span class="symbol">:avatar</span>
<span class="keyword">end</span></pre>

</section>

<section>

<h4>application.rb</h4>

<pre class="code"><span class="keyword">require</span> File.expand_path(<span class="str">'../boot'</span>, <span class="keyword">__FILE__</span>)
<span class="keyword">require</span> <span class="str">'rails/all'</span>
Bundler.require(<span class="symbol">:default</span>, Rails.env) <span class="keyword">if</span> <span class="keyword">defined?</span>(Bundler)
 
<span class="keyword">module</span> MyApp
  <span class="keyword">class</span> Application &lt; Rails::Application
    config.action_controller.asset_host = <span class="str">&quot;http://image.serverhost&quot;</span>
    ...(略)...
  <span class="keyword">end</span>
<span class="keyword">end</span></pre>




