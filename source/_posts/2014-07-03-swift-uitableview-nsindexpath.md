---
layout: post
title: "Swift で UITableView 関連の操作をする際に NSIndexPath を作る方法"
date: 2014-07-03 11:53:41 +0900
comments: true
categories: [Programming]
keywords: "Swift,Apple,UITableView,NSIndexPath"
tags: [Swift]
sharing: true
published: true
amazon_url: ""
amazon_author: ""
amazon_image: ""
amazon_publisher: ""
og_image: ""

---

Swift で `UITableView` 関連のロジックを書くときに、`NSIndexPath` を作るときは

```swift
let row = 0, section = 0
NSIndexPath(forRow: row, inSection: section)
```

のイニシャライザを使うべし。`NSIndexPath(index: row)` だと `row` の参照ができなくてエラーになる。 

