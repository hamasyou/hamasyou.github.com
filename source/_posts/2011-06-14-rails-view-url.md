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

```ruby
#-*- encoding: utf-8 -*-

class CallbackController < ApplicationController
  def send
    user = User.new
    user.avatar = params[:user][:avatar]
    user.avatar.url
      # => "/users/avatars/4/original_me.jpg"
    view_context.image_path(user.avatar.url)
      # => "http://image.serverhost/users/avatars/4/original_me.jpg"
  end
end
```

</section>

<section>

<h4>models/user.rb</h4>

```ruby
class User < ActiveRecord::Base
  has_attached_file :avatar
end
```

</section>

<section>

<h4>application.rb</h4>

```ruby
require File.expand_path('../boot', __FILE__)
require 'rails/all'
Bundler.require(:default, Rails.env) if defined?(Bundler)

module MyApp
  class Application < Rails::Application
    config.action_controller.asset_host = "http://image.serverhost"
    ...(略)...
  end
end
```
