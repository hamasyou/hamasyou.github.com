---
layout: post
title: "[Rails] carrierwave と fog で Amazon S3 をストレージとして使う"
date: 2012-02-21 21:54
comments: true
categories: [Blog]
keywords: "Rails3,carrierwave,fog,amazon s3,storage"
tags: [Amazon,S3,Rails3]
author: hamasyou
amazon_url: ""
amazon_author: ""
amazon_image: ""
amazon_publisher: ""
---

Rails のプラグイン <a href="https://github.com/jnicklas/carrierwave" rel="external nofollow">carrierwave</a> と fog を使って、<em>Amazon S3</em> をストレージとして使う方法のメモです。

<h3>使用環境</h3>

<ul>
<li>Rails 3.2.1</li>
<li>carrierwave 0.6.0.beta</li>
<li>fog 1.1.2</li>
</ul>

<h3>carrierwave のインストール</h3>

{% terminal %}
$ gem install carrierwave
{% endterminal %}

<h3>config の書き方</h3>

initializers に carrierwave.rb というファイルを作成し、以下のような設定を書きます。

```ruby initializers/carrierwave.rb
#-*- encoding: utf-8 -*-

unless Rails.env.test?
  CarrierWave.configure do |config|
    config.cache_dir = "#{Rails.root}/tmp/uploads"
    config.storage                          = :fog
    config.fog_credentials                  = {
        :provider              => 'AWS',
        :aws_access_key_id     => ENV["AWS_S3_KEY_ID"],
        :aws_secret_access_key => ENV["AWS_S3_SECRET_KEY"]
    }
    config.fog_directory                    = ENV["AWS_S3_BUCKET"]
    config.fog_public                       = false
    config.fog_authenticated_url_expiration = 60
  end
else
  CarrierWave.configure do |config|
    config.storage = :file
  end
end
```

ENV["AWS_S3_KEY_ID"] にはユーザアクセスID、ENV["AWS_S3_SECRET_KEY"] にはシークレットアクセスキー、ENV["AWS_S3_BUCKET"] にはバケット名をそれぞれ設定する。

各値は、Amazon S3 の設定画面で確認できる。

この設定を行い、carrierwave で Uploader を作成して、ファイルをアップロードすると、S3 にファイルを置くことができる。また、S3 上のファイルの URL には、60秒のタイムアウト設定を掛けており、アプリからのアクセスでのみ参照可能なURLが表示されるようになる。（config.fog_public = false、config.fog_authenticated_url_expiration = 60 の設定）
