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

<pre class="console"><kbd>gem install carrierwave</kbd></pre>

<h3>config の書き方</h3>

initializers に carrierwave.rb というファイルを作成し、以下のような設定を書きます。

<pre class="code"><span class="rem">#-*- encoding: utf-8 -*-</span>
 
<span class="keyword">unless</span> Rails.env.test?
  CarrierWave.configure <span class="keyword">do</span> |config|
    config.cache_dir = <span class="str">&quot;#{Rails.root}/tmp/uploads&quot;</span>
    config.storage                          = <em>:fog</em>
    config.fog_credentials                  = {
        :provider              =&gt; <span class="str">'AWS'</span>,
        :aws_access_key_id     =&gt; ENV[<span class="str">&quot;AWS_S3_KEY_ID&quot;</span>],
        :aws_secret_access_key =&gt; ENV[<span class="str">&quot;AWS_S3_SECRET_KEY&quot;</span>]
    }
    config.fog_directory                    = ENV[<span class="str">&quot;AWS_S3_BUCKET&quot;</span>]
    config.fog_public                       = <span class="keyword">false</span>
    config.fog_authenticated_url_expiration = <span class="num">60</span>
  <span class="keyword">end</span>
<span class="keyword">else</span>
  CarrierWave.configure <span class="keyword">do</span> |config|
    config.storage = :file
  <span class="keyword">end</span>
<span class="keyword">end</span></pre>

ENV["AWS_S3_KEY_ID"] にはユーザアクセスID、ENV["AWS_S3_SECRET_KEY"] にはシークレットアクセスキー、ENV["AWS_S3_BUCKET"] にはバケット名をそれぞれ設定する。

各値は、Amazon S3 の設定画面で確認できる。

この設定を行い、carrierwave で Uploader を作成して、ファイルをアップロードすると、S3 にファイルを置くことができる。また、S3 上のファイルの URL には、60秒のタイムアウト設定を掛けており、アプリからのアクセスでのみ参照可能なURLが表示されるようになる。（config.fog_public = false、config.fog_authenticated_url_expiration = 60 の設定）
