---
layout: post
title: "[cocos2d] CCSpriteSheet や CCAnimation を使った時の CCSprite の後始末のしかた"
date: 2010-10-27 22:58
comments: true
categories: [Blog]
keywords: "cocos2d, iPhone, CCSpriteSheet, CCSprite, CCAnimation"
tags: [cocos2d,メモリ管理,iPhone]
author: hamasyou
amazon_url: ""
amazon_author: ""
amazon_image: ""
amazon_publisher: ""
---

cocos2d を使ったときの CCSprite の後始末のしかたのメモです。

CCSpriteSheet から CCSprite を作ったときの破棄と CCAnimation が終わったタイミングでの CCSprite の破棄の方法をメモしています。


<!-- more -->

<h2>CCSpriteSheet から作ったスプライトの後処理</h2>

CCSpriteSheet から CCSprite を作成する場合、次のように書きます。

<pre class="code"><span class="class">CCSpriteSheet</span> *sheet = [<span class="class">CCSpriteSheet</span> spriteSheetWithFile:<span class="literal">@&quot;missile.png&quot;</span>];
[<span class="keyword">self</span> addChild:sheet];
  
<span class="class">CCSprite</span> *missile = [<span class="class">CCSprite</span> spriteWithSpriteSheet:sheet rect:CGRectMake(<span class="num">0</span>, <span class="num">0</span>, <span class="num">32</span>, <span class="num">32</span>)];
[sheet addChild:missile];</pre>

このように、CCSpriteSheet から CCSprite を作成した場合、CCLayer（self） にはスプライトシートのオブジェクトを追加して、スプライトはスプライトシートに追加する形で記述します。

こうすると、スプライトが必要なくなってスプライトを破棄したくなった場合に、スプライトシートから取り除く必要があります。

この時 スプライトシートの removeChild:cleanup: メソッドでは上手くいきません。

それではどうするかというと、スプライトの方の <strong>removeFromParentAndCleanup:</strong> メソッドを使います。

<pre class="code">[sprite removeFromParentAndCleanup:<span class="keyword">YES</span>];</pre>

<h2>CCAction や CCAnimation が終わったタイミングでのスプライトの後処理</h2>

スプライトにアクションやアニメーションを付ける場合は、CCAction や CCAnimation を使います。

そして、アクションやアニメーションが終了したタイミングでスプライトを削除したい場合には、アクションの最後で <strong>CCCallFuncND</strong> を使って <em>removeFromParentAndCleanup:</em> メソッドを呼び出すようにします。（19行目）

次に例を載せておきます。この例は、ミサイルが爆発したときの爆発をアニメーションで表したものです。CCAnimation を使うので、burst.plist を別途記述して、アニメーションの定義をしています。コード例のあとに載せてあるのがそうです。

また、burst.png は 8×8 の爆発を表現する画像が9つ並んだものになっています。

<h4>burst.m</h4>

<pre class="code"><span class="rem">// アニメーションの定義（個別のスプライトの定義）をスプライトキャッシュに登録して、後で呼び出せるようにする</span>
[[<span class="class">CCSpriteFrameCache</span> sharedSpriteFrameCache] addSpriteFramesWithFile:<span class="literal">@&quot;burst.plist&quot;</span>];
 
<span class="rem">// アニメーション定義と対応するスプライトの画像を読み込む</span> 
<span class="class">CCSpriteSheet</span> *sheet = [<span class="class">CCSpriteSheet</span> spriteSheetWithFile:<span class="literal">@&quot;burst.png&quot;</span>];
[<span class="keyword">self</span> addChild:sheet];
 
<span class="rem">// アニメーションの最初のスプライトを読み込む</span>  
<span class="class">CCSprite</span> *sprite = [<span class="class">CCSprite</span> spriteWithSpriteFrameName:<span class="literal">@&quot;burst01&quot;</span>];
sprite.position = <span class="keyword">ccp</span>(<span class="literal">160</span>, <span class="literal">240</span>);
[sheet addChild:sprite];
 
<span class="rem">// アニメーションのコマに対応するスプライトをフレームとして作成する</span>  
<span class="class">NSMutableArray</span> *animFrames = [<span class="class">NSMutableArray</span> array];
<span class="keyword">for</span> (<span class="keyword">int</span> i = <span class="literal">2</span>; i &lt;= <span class="literal">9</span>; i++) {
  <span class="class">CCSpriteFrame</span> *frame = [[<span class="class">CCSpriteFrameCache</span> sharedSpriteFrameCache] spriteFrameByName:[<span class="class">NSString</span> stringWithFormat:<span class="literal">@&quot;burst%02d&quot;</span>, i]];
  [animFrames addObject:frame];
}
 
<span class="rem">// アニメーションフレームをアニメーションにしてスプライトと紐付ける</span>
<span class="rem">// アニメーションの最後に removeFromParentAndCleanup: を呼び出してスプライトを破棄する</span>
<span class="class">CCAnimation</span> *animation = [<span class="class">CCAnimation</span> animationWithName:<span class="literal">@&quot;burst&quot;</span> delay:<span class="literal">0.2f</span> frames:animFrames];
[sprite runAction:[<span class="class">CCSequence</span> actions:
                   [<span class="class">CCAnimate</span> actionWithAnimation:animation restoreOriginalFrame:NO],
                   <em>[<span class="class">CCCallFuncND</span> actionWithTarget:sprite selector:<span class="keyword">@selector</span>(removeFromParentAndCleanup:) data:(<span class="keyword">void</span> *)<span class="keyword">YES</span>]</em>,
                   <span class="keyword">nil</span>]];</pre>

<h4>burst.png</h4>

<img alt="missile.png" src="/images/missile.png" width="64" height="8" class="mt-image-none" style="" />

<h4>burst.plist</h4>

<pre>&lt;?xml version=&quot;1.0&quot; encoding=&quot;UTF-8&quot;?&gt;
&lt;!DOCTYPE plist PUBLIC &quot;-//Apple//DTD PLIST 1.0//EN&quot; &quot;http://www.apple.com/DTDs/PropertyList-1.0.dtd&quot;&gt;
&lt;plist version=&quot;1.0&quot;&gt;
&lt;dict&gt;
  &lt;key&gt;texture&lt;/key&gt;
  &lt;dict&gt;
    &lt;key&gt;width&lt;/key&gt;
    &lt;integer&gt;72&lt;/integer&gt;
    &lt;key&gt;height&lt;/key&gt;
    &lt;integer&gt;8&lt;/integer&gt;
  &lt;/dict&gt;
  &lt;key&gt;frames&lt;/key&gt;
  &lt;dict&gt;
    &lt;key&gt;burst01&lt;/key&gt;
    &lt;dict&gt;
      &lt;key&gt;x&lt;/key&gt;
      &lt;integer&gt;0&lt;/integer&gt;
      &lt;key&gt;y&lt;/key&gt;
      &lt;integer&gt;0&lt;/integer&gt;
      &lt;key&gt;width&lt;/key&gt;
      &lt;integer&gt;8&lt;/integer&gt;
      &lt;key&gt;height&lt;/key&gt;
      &lt;integer&gt;8&lt;/integer&gt;
      &lt;key&gt;originalWidth&lt;/key&gt;
      &lt;integer&gt;8&lt;/integer&gt;
      &lt;key&gt;originalHeight&lt;/key&gt;
      &lt;integer&gt;8&lt;/integer&gt;
    &lt;/dict&gt;
    &lt;key&gt;burst02&lt;/key&gt;
    &lt;dict&gt;
      &lt;key&gt;x&lt;/key&gt;
      &lt;integer&gt;8&lt;/integer&gt;
      &lt;key&gt;y&lt;/key&gt;
      &lt;integer&gt;0&lt;/integer&gt;
      &lt;key&gt;width&lt;/key&gt;
      &lt;integer&gt;8&lt;/integer&gt;
      &lt;key&gt;height&lt;/key&gt;
      &lt;integer&gt;8&lt;/integer&gt;
      &lt;key&gt;originalWidth&lt;/key&gt;
      &lt;integer&gt;8&lt;/integer&gt;
      &lt;key&gt;originalHeight&lt;/key&gt;
      &lt;integer&gt;8&lt;/integer&gt;
    &lt;/dict&gt;
    &lt;key&gt;burst03&lt;/key&gt;
    &lt;dict&gt;
      &lt;key&gt;x&lt;/key&gt;
      &lt;integer&gt;16&lt;/integer&gt;
      &lt;key&gt;y&lt;/key&gt;
      &lt;integer&gt;0&lt;/integer&gt;
      &lt;key&gt;width&lt;/key&gt;
      &lt;integer&gt;8&lt;/integer&gt;
      &lt;key&gt;height&lt;/key&gt;
      &lt;integer&gt;8&lt;/integer&gt;
      &lt;key&gt;originalWidth&lt;/key&gt;
      &lt;integer&gt;8&lt;/integer&gt;
      &lt;key&gt;originalHeight&lt;/key&gt;
      &lt;integer&gt;8&lt;/integer&gt;
    &lt;/dict&gt;
    &lt;key&gt;burst04&lt;/key&gt;
    &lt;dict&gt;
      &lt;key&gt;x&lt;/key&gt;
      &lt;integer&gt;24&lt;/integer&gt;
      &lt;key&gt;y&lt;/key&gt;
      &lt;integer&gt;0&lt;/integer&gt;
      &lt;key&gt;width&lt;/key&gt;
      &lt;integer&gt;8&lt;/integer&gt;
      &lt;key&gt;height&lt;/key&gt;
      &lt;integer&gt;8&lt;/integer&gt;
      &lt;key&gt;originalWidth&lt;/key&gt;
      &lt;integer&gt;8&lt;/integer&gt;
      &lt;key&gt;originalHeight&lt;/key&gt;
      &lt;integer&gt;8&lt;/integer&gt;
    &lt;/dict&gt;
    &lt;key&gt;burst05&lt;/key&gt;
    &lt;dict&gt;
      &lt;key&gt;x&lt;/key&gt;
      &lt;integer&gt;32&lt;/integer&gt;
      &lt;key&gt;y&lt;/key&gt;
      &lt;integer&gt;0&lt;/integer&gt;
      &lt;key&gt;width&lt;/key&gt;
      &lt;integer&gt;8&lt;/integer&gt;
      &lt;key&gt;height&lt;/key&gt;
      &lt;integer&gt;8&lt;/integer&gt;
      &lt;key&gt;originalWidth&lt;/key&gt;
      &lt;integer&gt;8&lt;/integer&gt;
      &lt;key&gt;originalHeight&lt;/key&gt;
      &lt;integer&gt;8&lt;/integer&gt;
    &lt;/dict&gt;
    &lt;key&gt;burst06&lt;/key&gt;
    &lt;dict&gt;
      &lt;key&gt;x&lt;/key&gt;
      &lt;integer&gt;40&lt;/integer&gt;
      &lt;key&gt;y&lt;/key&gt;
      &lt;integer&gt;0&lt;/integer&gt;
      &lt;key&gt;width&lt;/key&gt;
      &lt;integer&gt;8&lt;/integer&gt;
      &lt;key&gt;height&lt;/key&gt;
      &lt;integer&gt;8&lt;/integer&gt;
      &lt;key&gt;originalWidth&lt;/key&gt;
      &lt;integer&gt;8&lt;/integer&gt;
      &lt;key&gt;originalHeight&lt;/key&gt;
      &lt;integer&gt;8&lt;/integer&gt;
    &lt;/dict&gt;
    &lt;key&gt;burst07&lt;/key&gt;
    &lt;dict&gt;
      &lt;key&gt;x&lt;/key&gt;
      &lt;integer&gt;48&lt;/integer&gt;
      &lt;key&gt;y&lt;/key&gt;
      &lt;integer&gt;0&lt;/integer&gt;
      &lt;key&gt;width&lt;/key&gt;
      &lt;integer&gt;8&lt;/integer&gt;
      &lt;key&gt;height&lt;/key&gt;
      &lt;integer&gt;8&lt;/integer&gt;
      &lt;key&gt;originalWidth&lt;/key&gt;
      &lt;integer&gt;8&lt;/integer&gt;
      &lt;key&gt;originalHeight&lt;/key&gt;
      &lt;integer&gt;8&lt;/integer&gt;
    &lt;/dict&gt;
    &lt;key&gt;burst08&lt;/key&gt;
    &lt;dict&gt;
      &lt;key&gt;x&lt;/key&gt;
      &lt;integer&gt;56&lt;/integer&gt;
      &lt;key&gt;y&lt;/key&gt;
      &lt;integer&gt;0&lt;/integer&gt;
      &lt;key&gt;width&lt;/key&gt;
      &lt;integer&gt;8&lt;/integer&gt;
      &lt;key&gt;height&lt;/key&gt;
      &lt;integer&gt;8&lt;/integer&gt;
      &lt;key&gt;originalWidth&lt;/key&gt;
      &lt;integer&gt;8&lt;/integer&gt;
      &lt;key&gt;originalHeight&lt;/key&gt;
      &lt;integer&gt;8&lt;/integer&gt;
    &lt;/dict&gt;
    &lt;key&gt;burst09&lt;/key&gt;
    &lt;dict&gt;
      &lt;key&gt;x&lt;/key&gt;
      &lt;integer&gt;64&lt;/integer&gt;
      &lt;key&gt;y&lt;/key&gt;
      &lt;integer&gt;0&lt;/integer&gt;
      &lt;key&gt;width&lt;/key&gt;
      &lt;integer&gt;8&lt;/integer&gt;
      &lt;key&gt;height&lt;/key&gt;
      &lt;integer&gt;8&lt;/integer&gt;
      &lt;key&gt;originalWidth&lt;/key&gt;
      &lt;integer&gt;8&lt;/integer&gt;
      &lt;key&gt;originalHeight&lt;/key&gt;
      &lt;integer&gt;8&lt;/integer&gt;
    &lt;/dict&gt;
  &lt;/dict&gt;
&lt;/dict&gt;
&lt;/plist&gt;</pre>





