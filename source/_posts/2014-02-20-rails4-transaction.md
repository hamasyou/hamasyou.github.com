---
layout: post
title: "Rails4でtransactionの分離レベルを設定する"
date: 2014-02-20 21:42:41 +0900
comments: true
categories: [Tech]
keywords: "Rails4,Transaction,分離レベル"
tags: [Rails4,トランザクション]
sharing: true
published: true
amazon_url: ""
amazon_author: ""
amazon_image: ""
amazon_publisher: ""
og_image: ""
---

Rails4 から `ActiveRecord::Base.transaction` に分離レベルを手軽に設定できるようになったみたいです。

```ruby
ActiveRecord::Base.transaction(isolation: :serializable) do
  params[:ids].each do |id|
    book = Book.find_by(id: id)
    book.update_attributes(title: params[id][:title]
  end
end
```

分離レベルには次の4つを設定できます。

- `:read_uncommitted`
- `:read_committed`
- `:repeatable_read`
- `:serializable`

ActiveRecord で テーブルロックを掛けたかったときに見つけたのでメモしておきます。
