---
layout: post
title: "[Objective-C] Xcode の単体テスト環境"
date: 2010-10-19 17:46
comments: true
categories: [Programming]
keywords: "Objective-C, 単体テスト環境, OCUnit, SenTestingKit"
tags: [Objective-C,OCUnit,単体テスト]
author: hamasyou
amazon_url: ""
amazon_author: ""
amazon_image: ""
amazon_publisher: ""
---

iPhone アプリで利用している Xcode 上に単体テスト環境を構築する手順のメモです。

GUI の構築が多い iPhone アプリ開発ですが、一部ロジックもあります。ロジックのテストはあったほうがいいし、なきゃ不安。

ということで単体テスト環境の構築方法をいろいろしらべてやってみた手順のまとめです。



<!-- more -->

<script type="text/javascript">$(document).ready(function() { create_toc(); });</script>

<h2>開発環境</h2>

僕の Xcode の環境です。この環境でテスト環境を作っていきます。

<dl>
<dt>OS のバージョン</dt>
<dd><p>Mac OS X 10.6.4 Snow Leopard</p></dd>
<dt>Xcode のバージョン</dt>
<dd><p>3.2.4</p></dd>
<dt>iOS のバージョン</dt>
<dd><p>4.1</p></dd>
</dl>

<h2>単体テスト環境の作り方 - 手順</h2>

Xcode には 単体テスト（<em>OCUnit</em>）用の <em>SenTestingKit</em> というフレームワークが用意されています。

ここでは、iPhone 用のプロジェクトで OCUnit を使った単体テスト環境を構築する手順を紹介します。

<h3>手順1 - テスト対象のプロジェクトを作成する</h3>

まずは、iPhone アプリ開発用のプロジェクトを作成します。すでにプロジェクトがある場合にはそれを使います。

<img alt="スクリーンショット（2010-10-19 16.40.39）.png" src="http://hamasyou.com/blog/archives/images/%E3%82%B9%E3%82%AF%E3%83%AA%E3%83%BC%E3%83%B3%E3%82%B7%E3%83%A7%E3%83%83%E3%83%88%EF%BC%882010-10-19%2016.40.39%EF%BC%89.png" width="693" class="mt-image-none" style="" />

プロジェクト名は OCUnitDemo としました。

<img alt="スクリーンショット（2010-10-19 16.43.25）.png" src="http://hamasyou.com/blog/archives/images/%E3%82%B9%E3%82%AF%E3%83%AA%E3%83%BC%E3%83%B3%E3%82%B7%E3%83%A7%E3%83%83%E3%83%88%EF%BC%882010-10-19%2016.43.25%EF%BC%89.png" width="693" class="mt-image-none" style="" />

ここでビルドしてみて、エラーがでないことを確かめます。

<h3>手順2 - テスト用ターゲットを作成する</h3>

つぎに、テスト用のターゲットを作成します。「プロジェクト」→「新規ターゲット」をたどり <em>Unit Test Bundle</em> を選びます。

<img alt="スクリーンショット（2010-10-19 16.45.39）.png" src="http://hamasyou.com/blog/archives/images/%E3%82%B9%E3%82%AF%E3%83%AA%E3%83%BC%E3%83%B3%E3%82%B7%E3%83%A7%E3%83%83%E3%83%88%EF%BC%882010-10-19%2016.45.39%EF%BC%89.png" width="693" class="mt-image-none" style="" />

ターゲット名はなんでもよいのですが、ここでは UnitTest としました。

<h3>手順3 - テストクラスを置くためのフォルダを作成する</h3>

つぎに、テストクラスを置くためのフォルダとグループを作成します。この手順は省略しても構いませんがテストクラスとアプリケーションのクラスが同一のフォルダにあると分かりづらくなるので分けておくほうが良いと思います。

Finder でプロジェクトのフォルダを開き TestCases というフォルダを作成します。

<img alt="スクリーンショット（2010-10-19 17.03.41）.png" src="http://hamasyou.com/blog/archives/images/%E3%82%B9%E3%82%AF%E3%83%AA%E3%83%BC%E3%83%B3%E3%82%B7%E3%83%A7%E3%83%83%E3%83%88%EF%BC%882010-10-19%2017.03.41%EF%BC%89.png" width="693" class="mt-image-none" style="" />

Xcode に戻って左のツリーの最初にあるプロジェクトアイコン（OCUnitDemo）を右クリックして「追加」→「既存のファイル」をクリックして今作った TestCases を選択します。

このとき「デスティネーショングループのフォルダに項目をコピーする（必要な場合）」のチェックは外しておきます。

また、<em>「ターゲットに追加」で UnitTest にのみチェックが入っている状態にします。</em>

<img alt="スクリーンショット（2010-10-19 17.07.14）.png" src="http://hamasyou.com/blog/archives/images/%E3%82%B9%E3%82%AF%E3%83%AA%E3%83%BC%E3%83%B3%E3%82%B7%E3%83%A7%E3%83%83%E3%83%88%EF%BC%882010-10-19%2017.07.14%EF%BC%89.png" width="438" height="403" class="mt-image-none" style="" />

<h3>手順4 - テストクラスを作成する</h3>

つぎに、テストクラスを作成します。今追加した TestCases フォルダを右クリックし「追加」→「新規ファイル」から Objective-C test case class を選択します。

ファイル名はなんでもよいです。ここでは FirstTest.m としました。保存場所が TestCases、ターゲットが UnitTest になっていることを確認して作成します。

<img alt="スクリーンショット（2010-10-19 17.09.03）.png" src="http://hamasyou.com/blog/archives/images/%E3%82%B9%E3%82%AF%E3%83%AA%E3%83%BC%E3%83%B3%E3%82%B7%E3%83%A7%E3%83%83%E3%83%88%EF%BC%882010-10-19%2017.09.03%EF%BC%89.png" width="693" class="mt-image-none" style="" />

ここまでできたら、一度ビルドしてみます。「ビルド」→「ビルド」をクリックしてみます。

おそらくエラーが3件（もしくは2件）、警告が1件でるはずです。次はこれを直していきます。

<img alt="スクリーンショット（2010-10-19 17.14.21）.png" src="http://hamasyou.com/blog/archives/images/%E3%82%B9%E3%82%AF%E3%83%AA%E3%83%BC%E3%83%B3%E3%82%B7%E3%83%A7%E3%83%83%E3%83%88%EF%BC%882010-10-19%2017.14.21%EF%BC%89.png" width="693" class="mt-image-none" style="" />

<p class="option">エラーのうち、1件はテストのエラー、2件（もしくは1件）は Xcode3.2.4 で iOS4.1 以上を使っている場合にでるエラーになります。警告はガーベージコレクションが無効になっていることを示すもののはずです。</p>

<h3>手順5 - テストクラスを修正する</h3>

iPhone アプリ開発用のプロジェクトでテストクラスを作成すると、テストケースは次のようになっていると思います。

<section>

<h4>FirstTest.m</h4>

<pre class="code"><span class="keyword">#if USE_APPLICATION_UNIT_TEST</span>     <span class="rem">// all code under test is in the iPhone Application</span>
 
- (<span class="keyword">void</span>) testAppDelegate {
  
  <span class="keyword">id</span> yourApplicationDelegate = [[<span class="class">UIApplication</span> sharedApplication] delegate];
  <span class="keyword">STAssertNotNil</span>(yourApplicationDelegate, <span class="literal">@&quot;UIApplication failed to find the AppDelegate&quot;</span>);
  
}
 
<span class="keyword">#else</span>
<span class="rem">//...</span></pre>

</section>

1行目の #if USE_APPLICATION_UNIT_TEST というところがエラーの一つめのテストが失敗した原因になります。iPhone アプリ開発のプロジェクトでプロジェクトを作成するとこのマクロが追加されるようです。

今回はロジックの単体テストをおこなうつもりなので、この部分は削除してしまいます。削除したあとのコードは次のようになりました。

<section>

<h4>FirstTest.h</h4>

<pre class="code"><span class="keyword">#import</span> <span class="literal">&lt;SenTestingKit/SenTestingKit.h&gt;</span>
<span class="rem">//#import &quot;application_headers&quot; as required</span>
 
<span class="keyword">@interface</span> FirstTest : <span class="class">SenTestCase</span> {
 
}
 
- (<span class="keyword">void</span>) testMath;              <span class="rem">// simple standalone test</span>
 
<span class="keyword">@end</span></pre>

</section>

<section>

<h4>FirstTest.m</h4>

<pre class="code"><span class="keyword">#import</span> <span class="literal">&quot;FirstTest.h&quot;</span>
 
<span class="keyword">@implementation</span> FirstTest
 
- (<span class="keyword">void</span>) testMath {
  
  <span class="keyword">STAssertTrue</span>((<span class="num">1</span>+<span class="num">1</span>)==<span class="num">2</span>, <span class="literal">@&quot;Compiler isn't feeling well today :-(&quot;</span> );
  
}
 
<span class="keyword">@end</span></pre>

</section>

UIKit に関する #import 文も削除しました。

<h3>手順6 その1 - An internal error occurred when handling command output: -[XCBuildLogCommandInvocationSection setTestsPassedString:]: unrecognized selector sent to instance エラー対策をする</h3>

つぎに、なにやら長ったらしいエラーの対策をします。

<pre>An internal error occurred when handling command output: -[XCBuildLogCommandInvocationSection setTestsPassedString:]: unrecognized selector sent to instance 0x207543c40</pre>

これは Xcode3.2.4 で iOS4.1 の SDK をいれるとでるエラーらしいです。対応として、次の github にあるソースコードをプロジェクトに追加します。

<section>

<h4>gist:58296</h4>

<a href="http://gist.github.com/586296" rel="external nofollow">gist:586296 - GitHub</a>

</section>

ファイルをダウンロードまたは、中身をコピーして、TestCases の下にソースコードとして追加します。追加後のツリーはこんな感じになります。

<img alt="スクリーンショット（2010-10-19 17.34.12）.png" src="http://hamasyou.com/blog/archives/images/%E3%82%B9%E3%82%AF%E3%83%AA%E3%83%BC%E3%83%B3%E3%82%B7%E3%83%A7%E3%83%83%E3%83%88%EF%BC%882010-10-19%2017.34.12%EF%BC%89.png" width="693" class="mt-image-none" style="" />

おそらく、Xcode のバージョンアップでそのうち直ると思いますが、それまではこの対応が必要になりそうです。

<h3>手順6 その2 - An internal error occurred when handling command output: -[XCBuildLogCommandInvocationSection setTestsPassedString:]: unrecognized selector sent to instance エラー対策をする</h3>

手順6 その1 を試してみても長ったらしいエラーがなくならない場合は、次の手順を試してみてください。その1で治らないということはおそらく iOS のバージョンが 4.1 ではないのだと思います。iPad アプリのテストの場合などは、こちらを試すと良いと思います。

<ol>
<li>左側に出ている「グループとファイル」のツリーから、[ターゲット]-[UnitTest] を開きます</li>
<li>[スクリプトを実行] をダブルクリックして、情報ペインを表示します</li>
<li>&quot;${SYSTEM_DEVELOPER_DIR}/Tools/RunUnitTests&quot; の行の最後に 1&gt; /dev/null を追記します</li>
</ol>

<img alt="スクリーンショット（2010-11-06 12.15.47）.png" src="http://hamasyou.com/blog/archives/images/%E3%82%B9%E3%82%AF%E3%83%AA%E3%83%BC%E3%83%B3%E3%82%B7%E3%83%A7%E3%83%83%E3%83%88%EF%BC%882010-11-06%2012.15.47%EF%BC%89.png" width="693" class="mt-image-none" style="" />

<h4>参考</h4>

<a href="http://stackoverflow.com/questions/3516745/sentestcase-in-xcode-3-2-and-xcbuildlogcommandinvocationsection-errors" rel="external nofollow">SenTestCase in Xcode 3.2 and XCBuildLogCommandInvocationSection Errors - stackoverflow</a>

<h3>手順7 - ビルドしてみる</h3>

ここまでできたらビルドしてみます。エラーがなくなればテストが上手く通ったことになります。

<img alt="スクリーンショット（2010-10-19 17.35.58）.png" src="http://hamasyou.com/blog/archives/images/%E3%82%B9%E3%82%AF%E3%83%AA%E3%83%BC%E3%83%B3%E3%82%B7%E3%83%A7%E3%83%83%E3%83%88%EF%BC%882010-10-19%2017.35.58%EF%BC%89.png" width="693" class="mt-image-none" style="" />

FirstTest.m の testMath メソッドの中身を変えて、テストが失敗するようにしてビルドを行うと、エラーが発生することを確認します。エラーがきちんと発生していればテスト環境が整ったことになります。

<img alt="スクリーンショット（2010-10-19 17.36.27）.png" src="http://hamasyou.com/blog/archives/images/%E3%82%B9%E3%82%AF%E3%83%AA%E3%83%BC%E3%83%B3%E3%82%B7%E3%83%A7%E3%83%83%E3%83%88%EF%BC%882010-10-19%2017.36.27%EF%BC%89.png" width="693" class="mt-image-none" style="" />

あとは、テスト対象のクラスをごにょごにょしたりして単体テストを満喫すれば良いと思います。

<h3>手順8 - テスト対象のクラスをコンパイルする</h3>

上までの手順が整ったら、テストを書いて行けばよいのですが、実際にテスト対象クラスを #import したところでちょっとつまずいたのでメモです。

UnitTest ターゲットを作ってテストケースクラスはこっちに追加することは上に書きましたが、実際のテスト対象クラスは OCUnitDemo ターゲットに追加されているだけで、UnitTest ターゲットに追加されていません。

UnitTest ターゲット側でテスト対象のクラスを見えるようにするには、テスト対象のクラスを UnitTest ターゲットの[ソースをコンパイル] のフェーズにドラッグ＆ドロップして追加する必要があります。

<img alt="スクリーンショット（2010-11-06 15.58.45）.png" src="http://hamasyou.com/blog/archives/images/%E3%82%B9%E3%82%AF%E3%83%AA%E3%83%BC%E3%83%B3%E3%82%B7%E3%83%A7%E3%83%83%E3%83%88%EF%BC%882010-11-06%2015.58.45%EF%BC%89.png" width="693" class="mt-image-none" style="" />

<h3>（おまけ？）手順9 - UnitTest ターゲットの設定変更</h3>

僕の環境では手順7まででエラーなくテストが実行出来るようになったのですが、一部プラットフォーム（アーキテクチャのベース SDK）を iOS4.1 から Mac OS X に変えたり、Objective-C ガーベージコレクションの非対応を必須に変えたりしないといけないみたいな情報をちらちら見かけたので、手順7までで上手くいかない場合には試してみてください。

<h2>参考</h2>

参考にしたサイトです。

<ul>
<li><a href="http://okajima.air-nifty.com/b/2008/07/iphone_577f.html" rel="external nofollow">iPhoneと単体テスト環境構築メモ - 人生を書き換える者すらいた。</a></li>
<li><a href="http://sites.google.com/site/smgakusyuu/ocunit" rel="external nofollow">OCUnitの使い方（Cocoa Application編） - [SM gakusyuu];</a></li>
<li><a href="http://d.hatena.ne.jp/rabbit2go/20100919/1284859385" rel="external nofollow">Xcode3.2.4のユニットテスト失敗に対処する - Basic</a></li>
<li><a href="http://blazingcloud.net/2010/02/20/test-driven-development-for-iphone/" rel="external nofollow">Test Driven Development for iPhone - Blazing Cloud</a></li>
</ul>








