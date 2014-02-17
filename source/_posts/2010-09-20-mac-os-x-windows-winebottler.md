---
layout: post
title: "Mac OS X で Windows アプリケーションを動かす WineBottler"
date: 2010-09-20 00:07
comments: true
categories: [Blog]
keywords: "Mac OS X,WineBottler, Windowsアプリを動かす"
tags: [MacOSX,VMWare,Windows]
author: hamasyou
amazon_url: ""
amazon_author: ""
amazon_image: ""
amazon_publisher: ""
---

Mac OS X 上で Windows アプリケーションを動かす <em><a href="http://winebottler.kronenberg.org/" rel="external nofollow">WineBottler</a></em> なるものを発見しました。

このアプリケーションを使うと、Windows OS を持っていなくても、VMWare や Boot Camp をもっていなくても、Windows アプリケーションを Mac OS X 上で動かすことができるようです。

Web サイトを Mac で Windows の IE の表示確認をするために使ったので、そのメモです。WineBottler をもっと使いこなすには他のサイトを確認してみてください。

<section>

<h4>WineBottler を使いこなすための参考サイト</h4>

<ul><li><a href="http://www.lifehacker.jp/2010/01/100110_winebottlerwindowsos_x.html" rel="external nofollow">『WineBottler』でWindowsのプログラムをスタンドアローン化し、OS Xアプリに - lifehacker</a></li>
<li><a href="http://www.moongift.jp/2010/01/winebottler/" rel="external nofollow">Windows用ソフトウェアをあたかもMac OSXソフトウェアのように変換する - MOONGIFT</a></li>
</ul>

</section>


<!-- more -->

<h2>インストール</h2>

<h3>必要環境とインストール方法</h3>

<strong>WineBottler</strong> は Intel Mac の OS X で動作します。

<a href="http://winebottler.kronenberg.org/" rel="external nofollow">WineBottler run windows apps on OS X</a> から バイナリファイルをダウンロードします。

<img alt="スクリーンショット（2010-09-20 0.22.49）.png" src="/images/%E3%82%B9%E3%82%AF%E3%83%AA%E3%83%BC%E3%83%B3%E3%82%B7%E3%83%A7%E3%83%83%E3%83%88%EF%BC%882010-09-20%200.22.49%EF%BC%89.png" width="693" class="mt-image-none" style="" />

[download] リンクをクリックして dmg ファイルをダウンロードします。本記事執筆時は、WineBottlerCombo_1.1.44.dmg が最新でした。

dmg ファイルをマウントして、<em>Wine.app</em> と <em>WineBottler.app</em> を Applications フォルダにドラッグします。

<img alt="スクリーンショット（2010-09-20 0.25.14）.png" src="/images/%E3%82%B9%E3%82%AF%E3%83%AA%E3%83%BC%E3%83%B3%E3%82%B7%E3%83%A7%E3%83%83%E3%83%88%EF%BC%882010-09-20%200.25.14%EF%BC%89.png" width="693" class="mt-image-none" style="" />

<dl>
<dt>Wine.app</dt>
<dd><p>Windows アプリケーションを Mac OS X 上で動かすためのアプリケーションです。</p>

<p>インストールした Windows アプリケーションを起動することができます。ファイルマネージャやコマンドプロンプト、レジストリをいじったりもできるようです。</p>

<p>起動するとメニューエクストラに常駐します。</p>
</dd>
<dt>WineBottler.app</dt>
<dd><p>Wine で動かすアプリケーションの管理アプリケーションです。Windows アプリケーションをインストールしたりアプリケーションを起動したりできます。</p>
</dd>
</dl>

<h2>Mac OS X で Windows IE を動かす</h2>

インストールしたら、WineBottler.app を起動します。

[Install Predefined Prefixes] タブを開くと、デフォルトで用意されている Windows アプリケーションの一覧が表示されます。

<img alt="スクリーンショット（2010-09-20 0.33.37）.png" src="/images/%E3%82%B9%E3%82%AF%E3%83%AA%E3%83%BC%E3%83%B3%E3%82%B7%E3%83%A7%E3%83%83%E3%83%88%EF%BC%882010-09-20%200.33.37%EF%BC%89.png" width="693" class="mt-image-none" style="" />

Microsoft Internet Explorer 7 を選択して、[Install] ボタンをクリックします。

インストール先を入力して、あとはウィザードに沿ってインストールします。

インストールが完了すると、選択したインストール先ディレクトリに Microsoft Internet Explorer 7.app ができるので、起動します。

<img alt="スクリーンショット（2010-09-20 0.35.29）.png" src="/images/%E3%82%B9%E3%82%AF%E3%83%AA%E3%83%BC%E3%83%B3%E3%82%B7%E3%83%A7%E3%83%83%E3%83%88%EF%BC%882010-09-20%200.35.29%EF%BC%89.png" width="693" class="mt-image-none" style="" />

<img alt="スクリーンショット（2010-09-20 0.54.54）.png" src="/images/%E3%82%B9%E3%82%AF%E3%83%AA%E3%83%BC%E3%83%B3%E3%82%B7%E3%83%A7%E3%83%83%E3%83%88%EF%BC%882010-09-20%200.54.54%EF%BC%89.png" width="693" class="mt-image-none" style="" />

<h2>WineBottler でデフォルト以外の Windows アプリケーションをインストールする</h2>

<h3>Windows 版 Firefox をインストールする</h3>

<p class="option">完全にオリジナルのアプリケーションをインストールするには、他の参考サイトを確認してみてください。ここでは、Wine に認識されているアプリケーションの中からのインストール方法になります。</p>

WineBottler の [Create Custome Prefixes] タブを開きます。

[Winetricks] から firefox を選択して [Install] ボタンをクリックします。

インストール先を聞かれるので、インストール先を入力してあとはウィザードにしたがってインストールを実行します。

<img alt="スクリーンショット（2010-09-20 10.48.18）.png" src="/images/%E3%82%B9%E3%82%AF%E3%83%AA%E3%83%BC%E3%83%B3%E3%82%B7%E3%83%A7%E3%83%83%E3%83%88%EF%BC%882010-09-20%2010.48.18%EF%BC%89.png" width="693" class="mt-image-none" style="" />

<p class="option">たぶんオリジナルの Windows アプリケーションをインストールするには、[Install File] にインストールしたいアプリケーションを入力して Install するんだと思いますが、未確認です。。</p>

あとは、起動するだけです。

<img alt="スクリーンショット（2010-09-20 0.48.31）.png" src="/images/%E3%82%B9%E3%82%AF%E3%83%AA%E3%83%BC%E3%83%B3%E3%82%B7%E3%83%A7%E3%83%83%E3%83%88%EF%BC%882010-09-20%200.48.31%EF%BC%89.png" width="693" class="mt-image-none" style="" />







