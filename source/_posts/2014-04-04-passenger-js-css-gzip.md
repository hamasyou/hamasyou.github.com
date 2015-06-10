---
layout: post
title: "Railsが用意したasset precompile済みのgzファイルを返すapache confの設定"
date: 2014-04-04 18:45:30 +0900
comments: true
categories: [Tech]
keywords: "apache,asset,precompile,passenger,rails"
tags: [apache,asset,precompile,rails]
sharing: true
published: true
amazon_url: ""
amazon_author: ""
amazon_image: ""
amazon_publisher: ""
og_image: ""

---

Passenger で Rails アプリを動かすときに、`assets:precompile` 済みの `gz` ファイルを返すようにする apache の設定方法例のメモ。

いただき物です。


```apache
<VirtualHost *:80>
   # ...
   <LocationMatch "^/assets/.*$">
     # Some browsers still send conditional-GET requests if there's a
     # Last-Modified header or an ETag header even if they haven't
     # reached the expiry date sent in the Expires header.
     #Header unset Last-Modified
     #Header unset ETag
     FileETag Size
     # RFC says only cache for 1 year
     ExpiresActive On
     ExpiresDefault "access plus 1 year"

     SetEnv no-gzip

     RewriteEngine on
     #RewriteLog /tmp/rewrite.log
     #RewriteLogLevel 15
     # Make sure the browser supports gzip encoding before we send it
     RewriteCond %{HTTP:Accept-Encoding} \b(x-)?gzip\b
     RewriteCond %{REQUEST_FILENAME}.gz -f
     RewriteRule ^(.+) \$1.gz [L]
   </LocationMatch>

   <FilesMatch \.css\.gz$>
       ForceType text/css
       Header set Content-Encoding gzip
   </FilesMatch>

   <FilesMatch \.js\.gz$>
       ForceType text/javascript
       Header set Content-Encoding gzip
   </FilesMatch>
</VirtualHost>
```
