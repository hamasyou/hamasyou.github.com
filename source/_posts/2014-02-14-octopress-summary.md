---
layout: post
title: "Octopress参考リンクまとめ"
date: 2014-02-14 12:18:53 +0900
comments: true
categories: [Blog]
keywords: "Octopress,Jekyll,Ruby,rdiscount,GitHub Pages"
tags: [Octopress,Jekyll,Ruby]
sharing: true
published: true
amazon_url: ""
amazon_author: ""
amazon_image: ""
amazon_publisher: ""
og_image: ""
---

**Octopress** の設定や運用で参考にしたサイトのまとめです。

<!-- more -->

### インストール、初期設定

インストールや初期設定周りでお世話になったサイトです。

- [OctopressでGitHub無料ブログ構築。sourceをBitbucket管理。簡単ガイド！](http://morizyun.github.io/blog/octopress-gitpage-minimum-install-guide/)
- [Octopressはじめました | mimemo](http://www.miukoba.net/blog/2013/01/05/start-octopress/)
- [Markdownで書いてGitで管理するブログ「Octopress」の始め方](http://tantant.jp/blog/Octopress/installing-octopress/)
- [Tag: Octopress - rcmdnk's blog](http://rcmdnk.github.io/blog/tags/octopress/)

独自ドメインで運用するときにお世話になりました。

- [あなたのgithub pagesを無料で高速化する方法](http://mojavy.com/blog/2014/02/13/faster-github-pages/)
- [GitHub Pages に独自のトップレベルドメイン（TLD）を割り当てる](http://bekkou68.hatenablog.com/entry/2013/01/05/210902)

404 Not Found ページの作り方。

- [404 not found - Octopress Tips](http://rcmdnk.github.io/blog/2013/03/23/octopress/#not-found)

### カテゴリリストとタグリストのプラグイン

- [Octopressでカテゴリリストをつくる](http://alotofwe.github.io/blog/2013/08/05/octopressdekategoririsutowotukuru/)
- [Octopressでのタグの運用](http://rcmdnk.github.io/blog/2013/04/12/blog-octopress/)

カテゴリリスト、タグリストの plugin ソースの中で URL を組み立てる部分があります。その部分下のように変更しておいたほうが無難です。

カテゴリやタグなどに `/` が入るような単語（例えば O/Rマッピング）の場合に URL が一致しなくなってしまうので。

```ruby
- category_url = File.join(category_dir, category.gsub(/_|\P{Word}/, '­').gsub(/­{2,}/, '­').downcase)
+ category_url = category_dir + category.to_url
```

### 関連ページ、ソーシャル系のボタンを表示する

関連ページの表示やソーシャル系ボタン（いいね、シェア、はてブ等）を表示するための設定系サイトです。

- [OctopressでRelated Posts(関連エントリー)を表示させるようにした](http://blog.glidenote.com/blog/2012/01/04/octopress-related-posts/)
- [Octopress - 関連記事の表示！](http://www.mk-mode.com/octopress/2012/12/26/octopress-add-related-post/)
- [5分でできる簡単 Octopress セッティング](http://morizyun.github.io/blog/octopress-hatena-disqus-new-tab/)
- [シェアボタンを非同期読み込みでまとめて設定](http://rcmdnk.github.io/blog/2013/11/26/blog-octopress/)

### Octopress で記事を書くときの高速化

`isolate` コマンドを使うと単体記事になるので高速化できますよという内容。

- [Octopressのpreviewを高速化する](http://gam0022.net/blog/2013/09/28/speed-up-octopress-site-generation/)


### まとめのまとめ

- [Octorpessについてのここまでのまとめ](http://rcmdnk.github.io/blog/2014/02/02/blog-octopress/)
