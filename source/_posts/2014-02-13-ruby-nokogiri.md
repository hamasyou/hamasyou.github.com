---
layout: post
title: "Ruby-Nokogiriで取り出した要素を置き換える"
date: 2014-02-13 16:19:09 +0900
comments: true
categories: [Programming]
keywords: "Ruby,Nokogiri,gem"
tags: [Ruby,Nokogiri,gem]
sharing: true
published: true
amazon_url: ""
amazon_author: ""
amazon_image: ""
amazon_publisher: ""
og_image: ""
---

Ruby の HTML パーサに **Nokogiri** があります。基本的な使い方は

- [Nokogiri-GitHub](https://github.com/sparklemotion/nokogiri)
- [RubyのNokogiriでギコギコスクレイピングだ](http://www.absolute-keitarou.net/blog/?p=634)
- [Nokogiriでスクレイピング](http://qiita.com/w650/items/e663fa2430145c456c4d)

とかを参考にしてもらえばいいんですが、パースした要素の特定の属性を置き換えたいとか追加で属性を追加したい時のメモです。

<!-- more -->

使い方は

```plain
gem install 'nokogiri'
```

```ruby
require 'nokogiri'

doc = Nokogiri::HTML(html_text)

# 外部リンクに rel="external nofollow" と title 属性を付ける
doc.search('a[href^="http://"]', 'a[href^="https://"]').each do |link|
  link['rel'] = "#{link['rel']} external nofollow".strip
  link['title'] = link.text unless link.attr('title')
end
html_text = doc.css('body')[0].inner_html
```

Nokogiri に渡す HTML 文字列は `<html>` から始まる必要はなく、部分的な HTML 文字列でも OK です。

部分 HTML を置き換えた場合は、取り出しは `doc.css('body')[0].inner_html` になります。`doc.to_html` だと `<!DOCTYPE html><html>` から始まる文字列になってしまうので注意。

### 文字コードを指定する

Nokogiri は日本語に対応していますが、デフォルトで *UTF-8* の文字エンコーディングになっているようです。なので Shift_JIS や EUC-JP の HTML を与えるとうまくパースできません。

そんな時は、次のようなコードで UTF-8 に変換してやれば OK です。

```ruby
doc = Nokogiri::HTML(html_text, nil, 'utf-8')
```
