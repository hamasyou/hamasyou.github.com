---
layout: post
title: "[iOS8対応]UITableViewのSeparatorInsetsをゼロにする方法"
date: 2014-12-25 11:03:14 +0900
comments: true
categories: [iOS,Apple,スマホアプリ]
keywords: "iOS8,UIAppearance,UITableView,UITableViewCell,SeparatorInsets,layoutMargins"
tags: [iOS8,UIAppearance]
sharing: true
published: true
amazon_url: ""
amazon_author: ""
amazon_image: ""
amazon_publisher: ""
og_image: ""

---

## iOS8 から separatorInset だけだと区切り線が広がらない

こんにちは。iOS8 から SeparatorInsets をゼロにする方法が変わりました。今までの `UITableView` の `separatorInset` に `UIEdgeInsetsZero` を入れるだけだと、iOS7 では効きますが、iOS8 では効かなくなりました。

```swift
tableView.separatorInset = UIEdgeInsetsZero
```

{% img /images/2014-12-25-tableview.png 320 "図1" %}

## iOS8 にも対応するには

iOS8 からは `UIView` に追加された `layoutMargins` というプロパティにも `UIEdgeInsetsZero` をセットする必要があります。ただこのプロパティ、iOS8 以降でしか利用できないプロパティなので、分岐が必要になります。。

```swift
tableView.layoutMargins = UIEdgeInsetsZero
cell.layoutMargins = UIEdgeInsetsZero
```

### 全体に適用する簡単な例

全体に適用するには UIAppearance を使うと便利です。iOS7 と iOS8 に対応したコードは次のように書けます。

```swift
let version = NSString(string: UIDevice.currentDevice().systemVersion).doubleValue
        
UITableView.appearance().separatorInset = UIEdgeInsetsZero
UITableViewCell.appearance().separatorInset = UIEdgeInsetsZero
if version >= 8 {
    UITableView.appearance().layoutMargins = UIEdgeInsetsZero
    UITableViewCell.appearance().layoutMargins = UIEdgeInsetsZero
}
```

{% img /images/2014-12-25-tableview2.png 320 "図2" %}
