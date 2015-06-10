---
layout: post
title: "[Dropbox] .app ファイルを共有すると動かなくなる"
date: 2010-09-25 13:14
comments: true
categories: [Blog]
keywords: "Dropbox, .app, Mac"
tags: [Dropbox]
author: hamasyou
amazon_url: ""
amazon_author: ""
amazon_image: ""
amazon_publisher: ""
---

<strong>Dropbox</strong> で .app ファイルを共有すると、転送先で動かなくなるという現象が発生したので、解決方法のメモです。


<!-- more -->

<h2>Dropbox で .app ファイルを共有すると、実行権限が外れる</h2>

<a href="https://www.dropbox.com/" rel="external nofollow">Dropbox</a> で他人とフォルダを共有し、アプリケーションを共有する場合があるかと思います。

Mac 限定だと思いますが、.app を直接フォルダに置いておくと、Dropbox が実行権限を削除してしまい、コンソールに下のようなエラーが発生します。

<pre>posix_spawn(&quot;実行したファイル名&quot;): Permission denied</pre>

Mac のアプリケーション .app は、実行ファイルのように見えてディレクトリになっていて、.app ディレクトリの下に Contents/MacOS/実際の実行ファイル というふうになっています。

この MacOS ディレクトリの下にある実際の実行ファイルの実行権限が Dropbox で共有を行うと外れてしまうようです。

<h3>対処法</h3>

Terminal を開いて、.app/Contents/MacOS ディレクトリに移動して、 chmod +x ファイル名 を行えば実行出来るようになります。

または、.app の形で直接置かなければよいので、Zip で固めてディレクトリに置くのもよいでしょう。




