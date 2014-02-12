---
layout: post
title: "[Titanium] Titanium Mobile で iPhone 開発 - ハマった点"
date: 2010-09-03 09:35
comments: true
categories: [Blog]
keywords: "iPhone, Titanium, Objective-C"
tags: [iPhone,Titanium]
author: hamasyou
amazon_url: ""
amazon_author: ""
amazon_image: ""
amazon_publisher: ""
---

<strong>Titanium Mobile</strong> は iPhone、Android アプリケーションを JavaScript でコーディングできるクロスプラットフォーム環境です。

Titanium を使っていてハマった点をメモしておきます。

<h4>Titanium Mobile のダウンロード</h4>

<ul><li><a href="http://www.appcelerator.com/products/titanium-mobile-application-development/" rel="external nofollow">Titanium Mobile - Appcelerator</a></li></ul>


<!-- more -->

<h2>Titanium で Run Emulator - Launch しても反応が無い</h2>

多分、内部でエラーが発生しているか、単に反応が無いだけかも。一旦シミュレータを終了させるか、Stop ボタンを押して、再度 Launch してみる。

それでもダメなら、 <code>build/iphone/build/build.log</code> を見てみる。ファイルの最後の方にエラー内容がでているので、心当たりを修正する。

<h3>build.log に FileNotFound のようなエラーがでてる場合</h3>

Titanium のプロジェクトが壊れている可能性が大。一度ソースコードをどこかに退避して、Edit タブから Delete Project する。そのあと、New Project で再度プロジェクトをつくり直してソースコードを上書き（Resources フォルダを上書き）してみる。これで治ることがほとんど。

<h3>シミュレータは動くけど、実機のほうで Install Error になる</h3>

iPhone 実機の iOS のバージョンと XCode に入っている SDK のバージョンが一致してないかも。一度 XCode を立ち上げて、ウィンドウ - オーガナイザ を開いてみて、動かす実機の iPhone の iOS のバージョンと XCode のサポートしているバージョンが違わないかを確認する。

<img alt="iPhone オーガナイザ.gif" src="http://hamasyou.com/blog/archives/images/iPhone%20%E3%82%AA%E3%83%BC%E3%82%AC%E3%83%8A%E3%82%A4%E3%82%B6.gif" width="693" class="mt-image-none" style="" />

<h2>プロジェクト名に Titanium という単語が入るとエラーになる</h2>

Titanium でプロジェクトを作成するときに、プロジェクト名に Titanium っていう単語が入るとコンパイルエラーになるっぽい（上の FileNotFound 系の原因かも）。 TestTitanium とか、TitaniumDemo とかエラーになるので注意する。

<h2>コーディング時の注意</h2>

<h3>Titanium.Contacts でグループをつくるときの注意</h3>

Titanium.Contacts.createGroup({ name: "HOGE" }) でグループつくると内部で setName が呼ばれないため、後で group.name で取得しようとしたときにアプリが落ちます。Titanium.Contacts.createGroup でグループを作ったあとに group.name = "HOGE" でグループ名設定する必要があるっぽい。

<pre class="code"><code><span class="keyword">var</span> group = Titanium.Contacts.createGroup();
group.name = <span class="literal">"HOGE"</span>;</code></pre>

<h3><s>JavaScript で eval() は使えない</s></h3>

<s>Titanium の JavaScript で <em>eval()</em> は使えません。試しに alert(eval(&quot;1+1&quot;)); と記述してみると、ブラウザでは2と表示されますが Titanium ではエラーになります。</s>

<section>

<h4>2010/09/15追記</h4>

eval() 使うことができます。KitchenSink のサンプルにもあります。僕が使えないと勘違いしていた理由は、alert(eval("1+1")); とするとシミュレータがエラーで落ちるからで、alert(eval("1+1;")); とすると動くことを確認しました。。

</section>

Titanium は JavaScript で iPhone アプリケーションを開発できますが、JavaScript のすべてが使えるわけではないので注意！

<section>

<h4>JSON データのパースには JSON.parse() が使える</h4>

ちなみに、Titanium.Network.HTTPClient を使って取得したデータを JSON 形式に変換するのに、eval(&quot;(&quot; + this.responseText + &quot;)&quot;); とする場面では、JSON.parse(this.responseText) が使えます。

</section>

<h3>日本語のファイル名を使っていると、build.py でエラーになる</h3>

JavaScript ファイル名に日本語ファイル名を使っていると build.py でエラーが発生します。

<pre class="console">Exception detected in script:
Traceback (most recent call last):
  File "/Library/Application Support/Titanium/mobilesdk/osx/1.4.0/iphone/builder.py", line 707, in main
    dump_resources_listing(project_dir,o)
  File "/Library/Application Support/Titanium/mobilesdk/osx/1.4.0/iphone/builder.py", line 157, in dump_resources_listing
    out.write("  %s %s\n" % (string.ljust(p,120),string.ljust(s,8)))
UnicodeEncodeError: 'ascii' codec can't encode characters in position 25-30: ordinal not in range(128)</pre>




