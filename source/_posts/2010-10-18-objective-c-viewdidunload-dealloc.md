---
layout: post
title: "[iPhone] UIViewController の dealloc と viewDidUnload"
date: 2010-10-18 17:52
comments: true
categories: [Programming]
keywords: "iPhone, UIKit, UIViewController, viewDidUnload, dealloc, メモリ管理, SDK"
tags: [iPhone,UIKit,メモリ管理]
author: hamasyou
amazon_url: ""
amazon_author: ""
amazon_image: ""
amazon_publisher: ""
---

UIViewController でメモリの確保と破棄を行う際に viewDidUnload と dealloc をどのように使い分けたらよいか間違って覚えている人が多いように思います。

Web上にあるサンプルや書籍でさえも間違っているものが多くあります。

本記事では、UIViewController の viewDidUnload と dealloc の使い分けを整理していこうと思います。


<!-- more -->

<h2>UIViewController のメモリ管理まとめ</h2>

<h3>メモリの確保と破棄</h3>

UIViewController 内で確保したメモリの破棄場所は、結構ややこしいです。とくに <em>viewDidUnload</em> と <em>dealloc</em> の違いは抑えておく必要があります。

NSObject のメモリ管理の定石である <strong>init で確保したメモリは dealloc で破棄する</strong>ことは間違えないのですが UIViewController では viewDidload で確保したメモリを viewDidUnload で破棄して dealloc で破棄しない例がよくあるので間違えないようにしなければなりません。

<p class="option"><strong>viewDidload で確保したメモリは dealloc でも破棄しなければならない</strong></p>

viewDidload と viewDidUnload は名前が似ているため対のメソッドのように思いがちですが<em>そうではありません</em>。

<h3>viewDidUnload</h3>

viewDidUnload はメモリ不足警告をアプリが受け取った場合に didReceiveMemoryWarning メソッドから呼び出されるものです。

メモリ不足警告が起こらない場合には viewDidUnload は呼び出されません。なので、viewDidload で確保したメモリの破棄処理を viewDidUnload のみに記述し dealloc に記述しないと、メモリ不足警告がでない場合にメモリの破棄がされません。

<h3>メモリの確保と破棄の正確な例</h3>

UIViewController でのメモリ確保と破棄の定石は次のようになります。

<ul>
<li>init で確保したメモリは dealloc で破棄する</li>
<li>viewDidload で確保したメモリは viewDidUnload と dealloc で破棄する</li>
</ul>

なお、init と viewDidload の使い分けは次のような感じで行えばよいとおもいます。

<dl>
<dt>init</dt>
<dd><p>インスタンス変数の初期化処理</p></dd>
<dt>viewDidload</dt>
<dd><p>ビューが作成された際に行う処理。動的に画面部品を作成したり、サービス（現在位置の取得等）を実行したりする</p></dd>
</dl>

<h3>サンプルコード</h3>

インスタンス変数 myData_ 、locationManager_ をそれぞれ init と viewDidload で初期化します。

locationManager_ は viewDidUnload と dealloc でそれぞれ初期化し、viewDidUnload の方では release 後に nil を設定しています。

これは、nil を設定せずに他の場所で呼び出されるとメモリアクセスエラーが発生するためです。

Objective-C は nil に対しての呼出は何も起こらないことになっているので、viewDidUnload で nil を設定しておくことで dealloc で再度 release しても問題がでないようにしています。

<pre class="code"><span class="rem">//</span>
<span class="rem">//  SampleViewController.m</span>
<span class="rem">//  SampleApp</span>
<span class="rem">//</span>
<span class="rem">//  Created by 濱田 章吾 on 10/10/18.</span>
<span class="rem">//  Copyright 2010 hamasyou. All rights reserved.</span>
<span class="rem">//</span>
 
<span class="keyword">#import</span> <span class="str">&quot;SampleViewController.h&quot;</span>
 
<span class="keyword">@implementation</span> SampleViewController
 
<span class="keyword">@synthesize</span> myData = myData_;
<span class="keyword">@synthesize</span> locationManager = locationManager_;
 
<span class="rem">// Interface Builder で作成する UIViewController の指定イニシャライザ</span>
- (<span class="keyword">id</span>)initWithCoder:(<span class="class">NSCoder</span> *)aDecoder {
  <span class="keyword">if</span> ( (<span class="keyword">self</span> = [<span class="keyword">super</span> initWithCoder: aDecoder]) ) {
    myData_ = [[<span class="class">NSMutableArray</span> alloc] init];
  }
  <span class="keyword">return</span> <span class="keyword">self</span>;
}
 
- (<span class="keyword">void</span>)viewDidLoad {
  [<span class="keyword">super</span> viewDidLoad];
  
  <span class="keyword">if</span> (!locationManager_) {
    locationManager_ = [[<span class="class">CLLocationManager</span> alloc] init];
  }
  locationManager_.delegate = <span class="keyword">self</span>;
  locationManager_.desiredAccuracy = kCLLocationAccuracyBest;
  locationManager_.distanceFilter = <span class="num">100</span>;
  [locationManager_ startUpdatingLocation];
}
 
- (<span class="keyword">void</span>)viewDidUnload {
  [locationManager_ release];
  locationManager_ = <span class="keyword">nil</span>;
  
  [<span class="keyword">super</span> viewDidUnload];
}
 
- (<span class="keyword">void</span>)dealloc {
  [myData_ release];
  [locationManager_ release];
 
  [<span class="keyword">super</span> dealloc];
}
 
<span class="keyword">@end</span></pre>






