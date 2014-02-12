---
layout: post
title: "[Titanium] Titanium Module Development メモ - その2"
date: 2010-09-11 10:14
comments: true
categories: [Blog]
keywords: "iPhone, Titanium, クラス設計, Objective-C"
tags: [iPhone,Titanium]
author: hamasyou
amazon_url: ""
amazon_author: ""
amazon_image: ""
amazon_publisher: ""
---

Titanium Module Development のメモその2です。

<h4>参考</h4>

<ul><li><a href="http://d.hatena.ne.jp/donayama/searchdiary?word=*[Titanium]" rel="external nofollow">JP::HSJ::Junknews::HatenaSide</a></li></ul>



<!-- more -->

<h2>Titanium の基本クラスを拡張する</h2>

Titanium の Module をつくる方法は基本的には次のドキュメントを理解すればよい。

<section>

<h4>Titanium Module SDK for iPhone/iPad</h4>

<a href="http://developer.appcelerator.com/doc/mobile/iphone/module_sdk" rel="external nofollow">Titanium Module SDK for iPhone/iPad - Appcelerator</a>

</section>

既存のクラス、例えば TiUIWebView を継承して独自クラスを作成したい場合の手順を以下にメモ。（ほとんどは上の Titanium Module SDK の PDF の内容のままですが。）

<h2>手順</h2>

<ol>
<li>モジュール作成環境を整える</li>
<li>モジュールを生成する</li>
<li>既存クラスを継承する</li>
</ol>

<h2>モジュール作成環境を整える</h2>

モジュール作成に必要な環境は次の通り。

<section>

<h4>必要環境</h4>

<dl>
<dt>Intel Base の Mac OSX 10.5 以上</dt><dd></dd>
<dt>XCode 3.2 以上</dt><dd></dd>
<dt>iOS 4.0 SDK 以上</dt><dd></dd>
<dt>Titanium 1.4 Mobile SDK 以上</dt><dd></dd>
</dl>

iPhone アプリ開発を Titanium でやろうとしているひとは、おそらく環境は整っているかと。

</section>

<h3>titanium コマンドが通るようにする</h3>

ターミナルから titanium コマンドが通るようにする。.bash_profile、.bashrc、.zshrc などの自分の使っているシェルの設定ファイルに次の一行を加える。

<pre>alias titanium='/Library/Application\ Support/Titanium/mobilesdk/osx/1.4.0/titanium.py'</pre>

<strong>Titanium 1.4 Mobile SDK</strong> 以外のバージョンを使っている場合は、osx/1.4.0 のところを適宜読み替える。

記述が終わったらファイルを保存して、設定を再読込する。

<pre class="console">[hamasyou]$ <kbd>source ~/.bash_profile</kbd></pre>

titanium コマンドが実行できることを確認する。

<pre class="console">[hamasyou]$ <kbd>titanium</kbd>
Appcelerator Titanium
Copyright (c) 2010 by Appcelerator, Inc.
 
commands:
 
  create      - create a project
  run         - run an existing project
  help        - get help
</pre>

<h2>モジュールを生成する</h2>

titanium create コマンドを使って新規モジュールを作成する。

<pre class="console">[hamasyou]$ <kbd>titanium create --platform=iphone --type=module --name=MyWebView --id=com.hamasyou</kbd>
Appcelerator Titanium XCode templates installed
Created iphone module project
</pre>

iPhone 用のモジュールを作成するので、 --platform には iphone を指定する。作るのはモジュールなので --type には module を指定、--name にはモジュールの名前を指定し、--id にはモジュールのIDを指定する。

コマンドが成功すると、--name で指定した名前のディレクトリが作成されているはず。ディレクトリの中に、mywebview.xcodeproj という XCode のプロジェクトが作成されているはずなので、XCode で開く。ターミナルで open mywebview.xcodeproj とすれば開ける。

<section>

<h3>XCode で環境が「Base SDK Missing」となっている場合</h3>

iOS SDK を4.1にバージョンアップした場合、titanium コマンドで作成した XCode のプロジェクトテンプレートの SDK のバージョンと一致せずに Base SDK Missing となっている場合がある。

その場合、「プロジェクト - プロジェクト設定を編集」を開いて、「ベースSDK」の値を 「iOS デバイス 4.1」に変更するとよい。

<img alt="スクリーンショット（2010-09-11 10.47.15）.png" src="http://hamasyou.com/blog/archives/images/%E3%82%B9%E3%82%AF%E3%83%AA%E3%83%BC%E3%83%B3%E3%82%B7%E3%83%A7%E3%83%83%E3%83%88%EF%BC%882010-09-11%2010.47.15%EF%BC%89.png" width="693" class="mt-image-none" style="" />

</section>

ここまでできたら、一度モジュールをビルドしてみる。ターミナルで次のコマンドを実行する。

<pre class="console">[hamasyou]$ <kbd>titanium run</kbd>
[DEBUG] Build settings from command line:
[DEBUG] PLATFORM_NAME = iphoneos
[DEBUG] SDKROOT = /Developer/Platforms/iPhoneOS.platform/Developer/SDKs/iPhoneOS4.1.sdk
[DEBUG] === BUILD NATIVE TARGET mywebview OF PROJECT mywebview WITH CONFIGURATION Release ===
[DEBUG] Check dependencies
[DEBUG] ProcessPCH /var/folders/ms/msSj36DoHze872qn4jHjxE+++TI/-Caches-/com.apple.Xcode.501/SharedPrecompiledHeaders/ComHamasyou_Prefix-eliigzwlyyxsppgxalaxmfcmkzcw/ComHamasyou_Prefix.pch.gch ComHamasyou_Prefix.pch normal armv6 objective-c com.apple.compilers.gcc.4_2
[DEBUG] cd /Users/hamasyou/Documents/Temp/MyWebView
[DEBUG] setenv LANG en_US.US-ASCII
...(中略)
[INFO] [object ComHamasyouModule] loaded
[DEBUG] loading: /var/folders/ms/msSj36DoHze872qn4jHjxE+++TI/-Tmp-/mLtELnQti/mywebview/Resources/com.hamasyou.js, resource: var/folders/ms/msSj36DoHze872qn4jHjxE+++TI/-Tmp-/mLtELnQti/mywebview/Resources/com_hamasyou_js
[INFO] module is => [object ComHamasyouModule]
[DEBUG] application booted in 23.256958 ms
</pre>

しばらく待つと iPhone シミュレータが起動し、真っ白の画面が表示されるはず。ターミナル中に次の行があることを確認し、これが表示されていればモジュールは実行出来ている。

<pre>[INFO] module is => [object ComHamasyouModule]</pre>

<img alt="スクリーンショット（2010-09-11 10.54.53）.png" src="http://hamasyou.com/blog/archives/images/%E3%82%B9%E3%82%AF%E3%83%AA%E3%83%BC%E3%83%B3%E3%82%B7%E3%83%A7%E3%83%83%E3%83%88%EF%BC%882010-09-11%2010.54.53%EF%BC%89.png" width="414" height="770" class="mt-image-none" style="" />

<h2>既存クラスを継承する</h2>

<h3>View クラスを作成する</h3>

独自の View モジュールを作成する。XCode で「ファイル - 新規ファイル...」を選択し、TiUIView テンプレートを選択して、View クラスを作成する。

ファイル名は「<em>モジュール名 + クラス名 + View</em>」という規約で作成する必要がある。今回は、ComHamasyouMyWebView.m と入力した。ファイルの保存場所を Classes 以下にし忘れないようにして View クラスを作成する。

<img alt="スクリーンショット（2010-09-11 10.56.29）.png" src="http://hamasyou.com/blog/archives/images/%E3%82%B9%E3%82%AF%E3%83%AA%E3%83%BC%E3%83%B3%E3%82%B7%E3%83%A7%E3%83%83%E3%83%88%EF%BC%882010-09-11%2010.56.29%EF%BC%89.png" width="693" class="mt-image-none" style="" />

<img alt="スクリーンショット（2010-09-11 10.58.12）.png" src="http://hamasyou.com/blog/archives/images/%E3%82%B9%E3%82%AF%E3%83%AA%E3%83%BC%E3%83%B3%E3%82%B7%E3%83%A7%E3%83%83%E3%83%88%EF%BC%882010-09-11%2010.58.12%EF%BC%89.png" width="693" class="mt-image-none" style="" />

<h3>ComHamasyouMyWebView に TiUIWebView を継承させる</h3>

作成した ComHamasyouMyWebView.h を開き、#import "TiUIWebView.h" を追加し、TiUIWebView を継承する。このとき、#import "TiUIView.h" を削除しないようにする。<em>TiUIView.h をインポートしていないと、なぜか TiUIWebView を解決できなかったので注意。</em>

<section>

<h4>ComHamasyouMyWebView.h</h4>

<pre class="code"><span class="keyword">#import</span> <span class="literal">&quot;TiUIView.h&quot;</span>
<span class="keyword">#import</span> <span class="literal">&quot;TiUIWebView.h&quot;</span>
 
<span class="keyword">@interface</span> ComHamasyouMyWebView : TiUIWebView {
 
<span class="keyword">@private</span>
 
}
 
<span class="keyword">@end</span></pre>

</section>

<h3>ViewProxy クラスを作成する</h3>

Objective-C のコードと JavaScript のコードを橋渡しする Proxy クラスを作成する。「ファイル - 新規ファイル」から TiViewProxy を選択する。

<img alt="スクリーンショット（2010-09-11 11.05.46）.png" src="http://hamasyou.com/blog/archives/images/%E3%82%B9%E3%82%AF%E3%83%AA%E3%83%BC%E3%83%B3%E3%82%B7%E3%83%A7%E3%83%83%E3%83%88%EF%BC%882010-09-11%2011.05.46%EF%BC%89.png" width="693" class="mt-image-none" style="" />

ファイル名は View と同じように「<em>モジュール名 + クラス名 + ViewProxy</em>」とする必要がある。今回は、ComHamasyouMyWebViewProxy とした。保存場所を Classes 以下にすることを忘れずに。

<img alt="スクリーンショット（2010-09-11 11.06.02）.png" src="http://hamasyou.com/blog/archives/images/%E3%82%B9%E3%82%AF%E3%83%AA%E3%83%BC%E3%83%B3%E3%82%B7%E3%83%A7%E3%83%83%E3%83%88%EF%BC%882010-09-11%2011.06.02%EF%BC%89.png" width="693" class="mt-image-none" style="" />

<img alt="スクリーンショット（2010-09-11 11.08.13）.png" src="http://hamasyou.com/blog/archives/images/%E3%82%B9%E3%82%AF%E3%83%AA%E3%83%BC%E3%83%B3%E3%82%B7%E3%83%A7%E3%83%83%E3%83%88%EF%BC%882010-09-11%2011.08.13%EF%BC%89.png" width="506" height="388" class="mt-image-none" style="" />

<h3>example/app.js を変更して、作ったモジュールをテストする</h3>

example/app.js を開き、次のように書き換える。

<pre class="code"><span class="rem">// open a single window</span>
<span class="keyword">var</span> window = Ti.UI.createWindow({
  backgroundColor:<span class="str">'white'</span>
});
 
<span class="keyword">var</span> myModule = require(<span class="str">'com.hamasyou'</span>);
Ti.API.info(<span class="str">&quot;module is =&gt; &quot;</span> + myModule);
 
<span class="keyword">var</span> myWebView = myModule.createMyWebView({
  url: <span class="str">&quot;http://hamasyou.com/&quot;</span>
});
window.add(myWebView);
 
window.open();</pre>

ここまでできたら、再度 titanium run コマンドを実行してみる。iPhone シミュレータ上で Web ページが表示されれば無事に、TiUIWebView　を継承した自分のモジュールが呼び出されている。

<img alt="スクリーンショット（2010-09-11 11.11.53）.png" src="http://hamasyou.com/blog/archives/images/%E3%82%B9%E3%82%AF%E3%83%AA%E3%83%BC%E3%83%B3%E3%82%B7%E3%83%A7%E3%83%83%E3%83%88%EF%BC%882010-09-11%2011.11.53%EF%BC%89.png" width="414" height="770" class="mt-image-none" style="" />

あとは、好きなように拡張していけばよい。




