---
layout: post
title: "[Objective-C] retain と release の関係について"
date: 2010-10-12 08:13
comments: true
categories: [Programming]
keywords: "Objective-C,retain,release,@property"
tags: [Objective-C,メモリ管理]
author: hamasyou
amazon_url: ""
amazon_author: ""
amazon_image: ""
amazon_publisher: ""
---

Objective-C ではガーベージコレクション機能が用意されていますが、iPhone アプリ開発などではメモリ管理をガーベージコレクションに任せずに自分で管理する必要があったりします。

メモリ管理のルールとして、自分でメモリを割り当てたものは自分で開放する、自分で retain をしたものは 自分で release するなど、いくつかルールがあるのでそのメモです。


<!-- more -->

<h2>retain と release についてのメモ</h2>

<h3>init〜 で始まるメソッドで確保したオブジェクトは release する必要がある</h3>

Objective-C のオブジェクト初期化メソッドのルールに、<em>init〜</em> で始まるメソッドで確保したオブジェクトは自身で解放する必要がある、というものがあります。

例えば次の例のような場合は、自分で責任をもって <em>release</em> する必要があります。

<pre class="code"><span class="class">NSMutableArray</span> *array = [[<span class="class">NSMutableArray</span> alloc] initWithObjects:<span class="literal">@"hoge"</span>, <span class="literal">@"foo"</span>, <span class="keyword">nil</span>];
 
[array release];</pre>

init〜 で始まらないメソッドで確保したオブジェクトは自身で解放してはいけません。

<pre class="code"><span class="class">NSMutableArray</span> *array = [<span class="class">NSMutableArray</span> arrayWithObjects:<span class="literal">@"hoge"</span>, <span class="literal">@"foo"</span>, <span class="keyword">nil</span>];</pre>

<h3>メソッド内でのみ使用するローカル変数を init〜 で確保した場合は autorelease をつける</h3>

テクニックの一つとして、メソッド内でのみ使用するローカル変数を init〜 メソッドで初期化した場合は autorelease を付けて release し忘れを防ぐようにします。

<section>

<h4>Google Objective-Cスタイルガイド 日本語訳</h4>

参考: <a href="http://www.textdrop.net/google-styleguide-ja/objcguide.xml" rel="external nofollow">Google Objective-Cスタイルガイド 日本語訳</a>

</section>

<pre class="code">- (<span class="keyword">void</span>)doSomething
{
  <span class="class">NSArray</span> *array = [[[<span class="class">NSArray</span> alloc] initWithObjects:<span class="literal">@"hoge"</span>, <span class="literal">@"foo"</span>, <span class="keyword">nil</span>] <span class="keyword">autorelease</span>];
  <span class="comment">// なにかの処理</span>
}</pre>

<h3>メンバ変数を使用してオブジェクトを保持する場合のルール</h3>

次のいずれかの方法でメンバ変数でオブジェクトを保持する場合は dealloc メソッドで release を行う必要があります。

<ol><li>メンバ変数に init〜 で始まるメソッドで初期化したオブジェクトを割り当てている場合</li>
<li>メンバ変数に明示的に retain して保持することを示したオブジェクトを割り当てた場合</li>
<li>プロパティに retain 属性または copy 属性が付いていて、プロパティでオブジェクトを割り当てた場合</li></ol>

<section>

<h4>MyClass.h</h4>

<pre class="code"><span class="keyword">@interface</span> MyClass : <span class="class">NSObject</span> {
  <span class="keyword">@private</span>
  <span class="class">NSMutableArray</span> *array_;
  <span class="class">NSString</span> *message_;
}
 
<span class="keyword">@property</span>(<span class="keyword">retain</span>) <span class="class">NSMutableArray</span> *array;
<span class="keyword">@property</span>(<span class="keyword">copy</span>) <span class="class">NSString</span> *message;
<span class="keyword">@end</span></pre>

<h4>MyClass.m</h4>

<pre class="code"><span class="keyword">#import</span> <span class="literal">&quot;MyClass.h&quot;</span>
<span class="keyword">@implementation</span> MyClass
<span class="keyword">@synthesize</span> array = array_;
<span class="keyword">@synthesize</span> message = message_;
 
- (<span class="keyword">id</span>)init
{ 
  <span class="keyword">if</span> ( (<span class="keyword">self</span> = [<span class="keyword">super</span> init]) ) {
  
    <span class="comment">// 1) メンバ変数に init〜 で設定したオブジェクトを割り当てる場合は dealloc で release する</span>
    array_ = [[<span class="class">NSMutableArray</span> alloc] initWithObjects:<span class="literal">@"hoge"</span>, <span class="literal">@"foo"</span>, <span class="keyword">nil</span>];
  
    <span class="comment">// 2) メンバ変数に明示的に retain してオブジェクトを保持することをマークした場合は dealloc で release する</span>
    message_ = [[array_ objectAtIndex:<span class="literal">0</span>] retain];
  
    <span class="comment">// 3.1) メンバ変数にプロパティでオブジェクトを割り当てた場合は dealloc で release する</span>
    <span class="keyword">self</span>.array = [<span class="class">NSMutableArray</span> arrayWithObjects:<span class="literal">@"hoge"</span>, <span class="literal">@"foo"</span>, <span class="keyword">nil</span>];
    
    <span class="comment">// 3.2) init〜 で始まるメソッドでオブジェクトを割り当てる場合はプロパティ形式で直接受け取ってはいけない。二重の参照カウントになってしまう。</span>
    <span class="comment">//      init〜 で確保したオブジェクトをメンバ変数に割り当てる場合は直接割り当てるか次のようにする</span>
    <span class="class">NSMutableArray</span> *tmp = [[<span class="class">NSMutableArray</span> alloc] initWithObjects:<span class="literal">@"hoge"</span>, <span class="literal">@"foo"</span>, <span class="keyword">nil</span>];
    <span class="keyword">self</span>.array = tmp;
    [tmp release];
  }
  <span class="keyword">return</span> <span class="keyword">self</span>;
}
 
- (<span class="keyword">void</span>)dealloc
{
  <span class="comment">// 適切な release </span>
  <span class="keyword">self</span>.array = <span class="keyword">nil</span>;
  <span class="keyword">self</span>.message = <span class="keyword">nil</span>;
  [<span class="keyword">super</span> dealloc];
}</pre>

</section>

プラクティスとして、init〜 メソッドの内部ではプロパティ形式（3.1みたなやつ）でメンバ変数を初期化するのは避けるほうがよいです。











