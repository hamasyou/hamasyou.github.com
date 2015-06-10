---
layout: post
title: "[Titanium] Titanium Module Development メモ - その1"
date: 2010-09-10 02:24
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

Titanium Module Development のメモです。

<h4>参考</h4>

<ul><li><a href="http://d.hatena.ne.jp/donayama/searchdiary?word=*[Titanium]" rel="external nofollow">JP::HSJ::Junknews::HatenaSide</a></li></ul>


<!-- more -->

<h2>Module の種類</h2>

Module として作成できるものは次の通り。

<table>
<tr><th>種類</th><th>説明</th></tr>
<tr><td>Proxy</td><td>ネイティブコード（Objective-C）と JavaScript の橋渡しをするクラス</td></tr>
<tr><td>ViewProxy</td><td>View のレンダリングに対応する Proxy</td></tr>
<tr><td>View</td><td>Titanium で描画できる UI コンポーネントを表すクラス</td></tr>
<tr><td>Module</td><td>Module セットを表すクラス</td></tr>
</table>

Module クラスはモジュールとして提供するパッケージにひとつだけ存在し、Proxy、ViewProxy、View は Module パッケージ内にいくつ存在してもよい。以下、モジュール名を com.hamasyou と定義するものとする。クラス名等のプレフィックスにつく ComHamasyou はモジュール名を表す。

<h2>Proxy</h2>

Proxy クラスは <em>TiProxy</em> を継承して作成する。クラス名を &quot;Module 名 + クラス名 + Proxy&quot; にすることで、JavaScript から次のように呼び出すことができるようになる。例えば、ComHamasyouMyOriginalProxy という名前で Proxyクラスを作成すると次のようになる。

<section>

<h4>JavaScript のコード</h4>

<pre class="code"><span class="keyword">var</span> my_module = <span class="keyword">require</span>(<span class="literal">&quot;com.hamasyou&quot;</span>);
<span class="keyword">var</span> obj = my_module.createMyOriginal({ ... });</pre>

</section>

<section>

<h4>ComHamasyouMyOriginalProxy.h</h4>

<pre class="code"><span class="keyword">#import</span> <span class="literal">&quot;TiProxy.h&quot;</span>
<span class="keyword">@interface</span> ComHamasyouMyOriginalProxy : TiProxy {
}
<span class="keyword">@end</span></pre>

</section>

<section>

<h4>ComHamasyouMyOriginalProxy.m</h4>

<pre class="code"><span class="keyword">#import</span> <span class="literal">&quot;ComHamasyouMyOriginalProxy.h&quot;</span>
<span class="keyword">@implementation</span> ComHamasyouMyOriginalProxy
<span class="keyword">@end</span></pre>

</section>

<h3>Proxy Methods のシグネチャ</h3>

<pre>- (id)methodName:(id)args</pre>

<div class="option"><span style="color: #0000FF">マクロの内容は要確認。Titanium SKD 1.4.0 で内容が変わっているっぽい。。。</span>
args 引数は <em>NSArray</em> になっている。実際に引数がひとつの場合には <em>ENSURE_SINGLE_ITEM(args, type)</em> のマクロを使って単一引数にする。
<em>ENSURE_UI_THREAD</em> etc...</div>

<h3>TiUtils を使ってメソッド引数を型に変換する</h3>

(id)args で受け取った引数は、TiUtils クラスを使って型に変換できる。

<pre class="code"><span class="keyword">#import</span> <span class="literal">&quot;TiUtils.h&quot;</span>
NSInteger f = [TiUtils intValue:args];
NSString *s1 = [TiUtils stringValue:arg];
NSString *s2 = [TiUtils stringValue:@"key" properties:dict def:@"default"];</pre>

<h3>Proxy のプロパティ</h3>

@property を使って Proxy クラスのプロパティを宣言することで、JavaScript からプロパティを参照できる。

<pre>@property (nonatomic, readwrite, assign) NSString *propertyName;</pre>

getter/setter を用意して、任意の処理を追加することもできる。

<pre class="code">- (<span class="keyword">void</span>)setPropertyName:(<span class="keyword">id</span>)value {
}
 
- (<span class="keyword">id</span>)propertyName {
  <span class="keyword">return</span> propertyName;
}</pre>

createMyOriginal コンストラクタメソッドの引数にプロパティを渡した場合、setter メソッドが定義されていれば setter メソッドが、定義されていなければ dynprops という NSDictionary にプロパティが設定される。

<pre class="code"><span class="keyword">var</span> obj = my_module.createMyOriginal({ name: <span class="literal">&quot;hamasyou&quot;</span> });
obj.age = 28;</pre>

設定されたプロパティをコード内で使用する場合には、<em>valueForKey</em>、<em>valueForUndefinedKey</em> を使う。valueForKey は getter から、valueForUndefinedKey は dynprops からそれぞれ値を取り出す。




