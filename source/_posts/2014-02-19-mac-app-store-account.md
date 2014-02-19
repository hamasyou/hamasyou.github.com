---
layout: post
title: "MacのApp Storeで「ほかのアカウントで使用可能なアップデートがあります」が出てアップデート出来ないときの対応"
date: 2014-02-19 11:46:56 +0900
comments: true
categories: [Blog]
keywords: "Mac,App Store,アカウント,アップデート"
tags: [Mac,App Store,アップデート]
sharing: true
published: true
amazon_url: ""
amazon_author: ""
amazon_image: ""
amazon_publisher: ""
og_image: ""
---

先日、古い MacBook Pro の Xcode のアップデートを行おうとしたときに、「**ほかのアカウントで使用可能なアップデートがあります。このアプリケーションをアップデートするには、購入時に使用したアカウントでサインインしてください。**」ってでてアップデートできなかったときにやった対応メモです。

<!-- more -->

アップデートできなかった原因は、*Spotlight* のインデックス更新を止めていたからでした。なので、Spotlight のインデックスを再構築すればアップデートできるようになりました。

Spotlight インデックスを再構築する以外にも、**一回アプリをゴミ箱に入れてアップデート** すれば直るというのも Google 先生に聞いたら教えてくれました。

## Spotlight インデックスの再構築手順

まず、Spotlight のインデクサが起動していないと再構築できないので、先に起動させてから再構築します。

ターミナルを開いて次のコマンドを入力します。

{% terminal %}
$ sudo launchctl load -w /System/Library/LaunchDaemons/com.apple.metadata.mds.plist
$ sudo mdutil -E /
{% endterminal %}

再構築が開始されると、画面の右上の Spotlight マークが点滅します。

![Spotlight 索引再構築](/images/2014-02-19-mac-app-store-account-01.png)

再構築は結構時間がかかりますが、全部終わるのをまたなくても、僕の場合はアップデートができるようになりました。
