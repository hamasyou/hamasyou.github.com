---
layout: post
title: "GitHubとOctopressで始める無料ブログ"
date: 2014-02-12 19:33:48 +0900
comments: true
categories: [Blog]
keywords: "GitHub,Octopress,無料,ブログ,開設"
tags: [GitHub,Octopress,ブログ]
sharing: true
published: true
amazon_url: ""
amazon_author: ""
amazon_image: ""
amazon_publisher: ""
---

長いもので、hamasyou.com を初めてから9年経ちました。何年かに一回ブログのデザインを更新したくなるのって普通ですよね。

今までずっと *MovableType* を使っていたんですが、markdown で書きたくなったのと、MovableType のデザインのいじり方を忘れてしまったため、ブログ環境を一新する決意をしました。

今回作ったブログ環境は次のとおり。

公開用のサーバ
: <a href="http://pages.github.com/" rel="external nofollow">GitHub Pages</a>
ブログ編集用のCMS
: ローカルマシン上に <a href="http://octopress.org/" rel="external nofollow">Octopress</a> で構築

<!-- more -->

## GitHub Pages とは

<a href="http://pages.github.com/" rel="external nofollow"><img title="GitHub Pages" class="img-thumbnail" src="/images/2014-02-12-github-octopress.png" alt="http://pages.github.com/" width="400" height="310" /></a>

GitHub Pages とは簡単に言うと GitHub のリポジトリを Web サーバとして利用できるサービスです。リポジトリに HTML ファイルをプッシュすると公開される仕組みです。

これがなかなかよく出来ていて、静的ページしか使えませんが、404ページの設定や DNS の CNAME の設定なんかもできます。

リポジトリ名がサイトのホスト名になります（例: `http://hamasyou.github.io`）。ドメインを取得すれば当然、独自ドメインで運用するなんてことも可能です。

## Octopress とは

<a href="http://octopress.org/" rel="external nofollow"><img title="Octopress" class="img-thumbnail" src="http://capture.heartrails.com/400x300?http://octopress.org/" alt="http://octopress.org/" width="400" height="300" /></a>

Octopress は Ruby 用の GitHub Pages スターターキットです。内部で `Jekyll` や `rdiscount` を使っていて、簡単に GitHub Pages を利用する環境が構築できて、markdown ですぐに記事を書き始められます。

Octopress の詳しい使い方は <a href="http://morizyun.github.io/blog/octopress-gitpage-minimum-install-guide/" rel="external nofollow">OctopressでGitHub無料ブログ構築。sourceをBitbucket管理。簡単ガイド！ - 酒と泪とRubyとRailsと</a> が参考になります。

Octopress には様々なテーマが用意されていて、うちのサイトもテーマを使っています。
