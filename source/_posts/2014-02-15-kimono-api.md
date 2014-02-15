---
layout: post
title: "ブラウザ操作で簡単にスクレイピングAPIが作れる「kimono」"
date: 2014-02-15 19:57:19 +0900
comments: true
categories: [Tech]
keywords: "スクレイピング,API,kimono"
tags: [kimono,スクレイピング,API]
sharing: true
published: true
amazon_url: ""
amazon_author: ""
amazon_image: ""
amazon_publisher: ""
og_image: ""
---

「**kimono**」はウェブサイトをブラウザ操作で簡単にスクレイピングして API 化してくれるサービスです。

[kimono - Turn websites into structured APIs from your browser in seconds](http://www.kimonolabs.com/)

![kimono](/images/2014-02-15-kimono-api-01.png)

<!-- more -->

スクレイピングしたいサイトの URL を入力すると抜き出す要素を選択する画面になります。

最近ページングにも対応したので、ページングのあるサイトも簡単に取得できるようになります。

![要素の選択](/images/2014-02-15-kimono-api-02.png)

![プレビュー画面](/images/2014-02-15-kimono-api-03.png)

スクレイピングの頻度もリアルタイムや何時間毎のような設定ができます。

できた API は言語ごとに呼び出し例が用意されているので便利です。こういう所気が効いていますね。

![API 詳細](/images/2014-02-15-kimono-api-04.png)

実際に呼び出してみるとちゃんと結果が取得できます。

```ruby misc/kimono.rb
require 'rest_client'
require 'json'
require 'pp'

response = RestClient.get 'http://www.kimonolabs.com/api/4b5q146s?apikey=81c2b7add1263b7e459f5ed58b5f6504'
json = JSON.parse(response.to_str)
pp json
```

{% terminal %}
$ gem install rest_client
$ bundle exec ruby misc/kimono.rb
{"name"=>"archives",
 "lastrunstatus"=>"success",
 "lastsuccess"=>"Sat Feb 15 2014 11:27:17 GMT+0000 (UTC)",
 "nextrun"=>"Sat Feb 15 2014 11:57:15 GMT+0000 (UTC)",
 "frequency"=>"halfhourly",
 "newdata"=>false,
 "results"=>
  {"collection1"=>
    [{"title"=>
       {"text"=>"ガンダムUCのシナンジュが好き",
        "href"=>"http://hamasyou.com/blog/2014/02/15/gundam-unicorn/"},
      "category"=>
       {"text"=>"Blog", "href"=>"http://hamasyou.com/blog/categories/blog/"}},
     {"title"=>
       {"text"=>"Octopress の rel=”canonical” の設定がおかしい件",
        "href"=>"http://hamasyou.com/blog/2014/02/15/octopress-canonical/"},
      "category"=>
       {"text"=>"Blog", "href"=>"http://hamasyou.com/blog/categories/blog/"}},
     {"title"=>
       {"text"=>"ビューティフルコード",
        "href"=>"http://hamasyou.com/blog/2014/02/14/4873113636/"},
...
{% endterminal %}

[Yahoo Pipes](http://pipes.yahoo.com/pipes/) のような有名なサービスもありますが、簡単にウェブサイトをスクレイピングしたい用途であれば *kimono* はオススメのサービスです。
