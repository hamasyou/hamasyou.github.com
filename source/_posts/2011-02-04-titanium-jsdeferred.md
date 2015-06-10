---
layout: post
title: "[Titanium] JSDeferred を使って現在位置の取得を順次処理する"
date: 2011-02-04 20:32
comments: true
categories: [Blog]
keywords: "Titanium, JSDeferred"
tags: [JSDeferred,Titanium]
author: hamasyou
amazon_url: ""
amazon_author: ""
amazon_image: ""
amazon_publisher: ""
---

Titanium で、JSDeferred を使って順次処理するサンプルです。

{% blockquote  %}
<site><a href="http://gihyo.jp/dev/feature/01/jsdeferred/0001" rel="external nofollow">JSDeferredで，面倒な非同期処理とサヨナラ - gihyo.jp</a></site>
{% endblockquote %}

<section>

<section>

<h4>バージョン -- 追記（2011/03/23）</h4>

<dl><dt>Titanium</dt><dd>1.5.1</dd>
<dt>JSDeferred</dt><dd>0.4.0</dd></dl>

Titanium 1.6.1 で動かない問題は、<a href="http://twitter.com/Seasons" rel="external nofollow">@Seasons</a> さんがパッチを書いてくださいました。感謝！！ パッチが公開されたらリンクします！

<a href="http://d.hatena.ne.jp/Seasons/" rel="external nofollow">Seasons.NET</a>

</section>

<h3>サンプルコード</h3>

```javascript
Titanium.include(Titanium.App.appURLToPath("app://lib/jsdeferred.js"));
Deferred.define();

var currentLocation = null;
(function() {
  var deferred = new Deferred();
  Titanium.Geolocation.addEventListener("location", function(e) {
    currentLocation = e.coords;
    deferred.call();
  });
  return deferred;
})().
next(function() {
  alert(currentLocation);
});

alert("ここは必ずしも、alert(currentLocation)の後に呼び出されるとはかぎらない!");
```

alert(currentLocation) としているところに現在位置を取得した後のコードを記述することで、現在位置を取得して何か処理をするというのを順序を保証して処理することができるようになります。

ただし、next の外側の処理は非同期で進んでしまうため、必ずしも、alert(currentLocation) との順序は保たれません。

</section>

<a href="http://www.appcelerator.com/products/titanium-mobile-application-development/" rel="external nofollow">Titanium Mobile - appcelerator</a>

<a href="http://cho45.stfuawsc.com/jsdeferred/" rel="external nofollow">JSDeferred</a>

<h3>Titanium で include でファイルを読むときのテクニック</h3>

```javascript
Titanium.include(Titanium.App.appURLToPath("app://lib/jsdeferred.js"));
```

上記の様に、 app: プロトコルを使用して、appURLToPath メソッドでファイルを読み込むことで、Titanium のResources フォルダからの相対パスで書けるようになります。

