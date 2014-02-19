---
layout: post
title: "RailsのI18n.localizeでnilもゆるすようにすると捗る"
date: 2014-02-19 22:30:13 +0900
comments: true
categories: [Tech]
keywords: "Rails,I18n,localize,nil"
tags: [Rails,I18n]
sharing: true
published: true
amazon_url: ""
amazon_author: ""
amazon_image: ""
amazon_publisher: ""
og_image: ""
---

Rails の `I18n.localize` は `nil` を渡すと例外が発生します。でも、コレだといろいろ使いづらいのでいつも

```ruby config/initializers/relaxed_i18n.rb
module I18n
  class << self
    alias_method :original_localize, :localize
    def localize(object, options = {})
      object.present? ? original_localize(object, options) : ''
    end
  end
end
```

こんな感じのコードを `config/initializers` の下において使っています。`nil` を渡すと空文字を返します。

今のところコレで問題は起きていないのでオススメしてもいいかなと。
