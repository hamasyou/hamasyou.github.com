---
layout: post
title: "Octopress の rel=\"canonical\" の設定がおかしい件"
date: 2014-02-15 14:21:58 +0900
comments: true
categories: [Blog]
keywords: "Octopress,rel,canonical"
tags: [Octopress,SEO]
sharing: true
published: true
amazon_url: ""
amazon_author: ""
amazon_image: ""
amazon_publisher: ""
og_image: ""
---

Octopress の初期の **rel="canonical"** の値がおかしいので直しました。

<!-- more -->

初期値では `_includes/head.html` の中に


{% raw %}
```html _includes/head.html
<link rel="canonical" href="{{ canonical }}">
```
{% endraw %}

となっていますが、これだと吐き出される個別ページの HTML は

```html view-source
<head>
...
<link rel="canonical" href="http://hamasyou.com/blog/2014/02/15/octopress-canonical">
...
</head>
```

のように、`href` の末尾が `/` で終わっていません。Octopress (Jekyll) は `title名` ディレクトリの下に index.html を生成するという形式になっているので、 `http://hamasyou.com/blog/2014/02/15/octopress-canonical/` が canonical として正しいはずです。

なので、`canonical` は

{% raw %}
```html _includes/head.html
<link rel="canonical" href="{{ canonical }}/">
```
{% endraw %}

のように、末尾に `/` を入れてやる必要があります。

[Facebook URL Linter](https://developers.facebook.com/tools/debug) を使ったときに警告が出たので気づきました。
