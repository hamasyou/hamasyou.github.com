---
layout: post
title: "canonicalなURLってなにがいいんだろう？"
date: 2014-03-04 12:27:08 +0900
comments: true
categories: [Blog]
keywords: "canonical,URL,Rails,RESTful"
tags: [Blog,Rails,RESTful]
sharing: true
published: true
amazon_url: ""
amazon_author: ""
amazon_image: ""
amazon_publisher: ""
og_image: ""
---

ちょっと疑問に思ったこと。Web の SEO をやっていると **canonical** ページっていうのを聞いたことがあるとおもいます。
canonical ページとは、Google 先生曰く「*複数の類似した内容のページの中で優先されるページです。*」だそうです。
僕は **URL の正規化** に使うものだという認識ですが、間違ってませんよね？？

で、この canonical URL ってどう付けたらいいんだろう？っていう疑問です。

<!-- more -->

## canonical URL ってどっち？

### RESTful リソースの URL

Web サービスやっていると、**RESTful** な URL 設計大事！ってなりますよね。
Rails なんかをやっていると、ルーティングが自動的に RESTful になってて便利です。こんな感じ。

```console-raw
{% raw %}
       hypermedia GET    /hypermedia(.:format)            hypermedia#index
                  POST   /hypermedia(.:format)            hypermedia#create
  new_hypermedium GET    /hypermedia/new(.:format)        hypermedia#new
 edit_hypermedium GET    /hypermedia/:id/edit(.:format)   hypermedia#edit
      hypermedium GET    /hypermedia/:id(.:format)        hypermedia#show
                  PATCH  /hypermedia/:id(.:format)        hypermedia#update
                  PUT    /hypermedia/:id(.:format)        hypermedia#update
                  DELETE /hypermedia/:id(.:format)        hypermedia#destroy
{% endraw %}
```

こういうルーティングだと、`http://example.com/hypermedia/1` みたいな感じでリソースにアクセスできます。
で、例えば、作ってる Web サービスで、`hypermedia/1` リソースの別名で、hypermedia の URL を使ってもアクセスできるようにしたい。こんな感じで。

```console-raw
http://example.com/hamasyou.com
```

ルーティングに

```ruby
get '*url', to: 'hypermedia#show', format: false
```

を追加しておくと `hypermedia#show` の中で `params[:url]` から `hamasyou.com` っていう値をとれるようになります。
なので、こういう URL に別名付けてちょっとクールな感じの Web サービスを気取ってみたくなりました。

### どっちがいいの？

で、本題ですが、こういうリソースの別名扱いの URL があるとき、canonical URL として指定するのは

1. `/hypermedia/:id`
1. `*url`

のどっちがいいんだろう？という疑問です。HTML 的に書くとこんな感じ。

```html
<link rel="canonical" href="http://example.com/hypermedia/1">
<link rel="canonical" href="http://example.com/hamasyou.com">
```

どっちでもいい。好みの問題って感じもしなくもないですが、皆さんどっちを選ぶのが多いんでしょう？
