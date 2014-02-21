---
layout: post
title: "JavaScriptで学ぶ関数型プログラミング"
date: 2014-02-21 19:39:56 +0900
comments: true
categories: [Programming]
keywords: "JavaScript,関数型,プログラミング,9784873116600"
tags: [JavaScript,関数型,オライリー]
sharing: true
published: true
amazon_url: "http://www.amazon.co.jp/gp/product/4873116600?ie=UTF8&camp=247&creativeASIN=4873116600&linkCode=xm2&tag=sorehabooks-22"
amazon_author: "Michael Fogus (著), 和田 祐一郎 (翻訳)"
amazon_image: "http://ecx.images-amazon.com/images/I/414qGlNpcHL.AA300_.jpg"
amazon_publisher: "オライリージャパン"
og_image: ""
---

{% blockquote 内容紹介 %}
本書はJavaScriptを使って関数型プログラミングを学ぶ書籍です。関数型言語としてJavaScriptを理解し、使用することにより、コードがより洗練され、美しく、そして読みやすいものになることを目的としています。JavaScriptビルトインのデータ型を上手に利用するための基本知識やJavaScriptにおける関数の持つ特性など、関数型プログラミングの技術とその考え方について解説します。また実際のJavaScriptコーディングに関数型プログラミングのエッセンスを加えるポイントをサンプルを使って丁寧に説明します。関数型プログラミングに精通した著者が書き下ろした本書は、テクニックを増やし、コーディングのイマジネーションを広げたいエンジニア必携の一冊です。
{% endblockquote %}

JavaScript は純粋な関数型プログラミング言語ではありません。が、**関数型言語として JavaScript を理解し使用することでコードがより洗練され、読みやすいものになる** はずという筆者の考えがこの本には現れています。

関数型プログラミングは JavaScript においては一つのスタイルにしかすぎず、必要な場面で適切に使うというアプローチをとることで、よりよく JavaScript を使えるようになります。

<!-- more -->

## おぼえがき

### 関数型プログラミングとは

{% blockquote %}
関数型プログラミングとは、値を抽象の単位に変換する関数を使用して行うプログラミングであり、それらを使ってソフトウェアシステムを構築することである。
{% endblockquote %}

オブジェクト指向では、問題領域をオブジェクトという単位に分解して対処していきますが、関数型プログラミングでは、問題を **関数** のパーツに分解します。

オブジェクト指向のアプローチは問題を「**名詞**」に分解して行う傾向があるのに対して、関数型プログラミングでは、問題を「**動詞**」に分解する傾向があります。

関数を通して、与えられた値を「**変換**」することができます。

```plain-raw
markdown → toHTML → postProcess → modifyDOM
         │        │             │
      テキスト   HTML       編集されたHTML
```

### 高階関数

**高階関数** とは、関数を引数にとり新しい関数を生成して返す関数のことです。

例えば、JavaScript の Array には `sort` 関数があります。`sort` は引数に `compareFunction` を取ります。値の大小を比較する関数を `lessOrEqual`、この関数を `compareFunction` の仕様に変換する関数を `compare` とします。

```javascript
function lessOrEqual(x, y) {
  return x <= y;
}

function compare(func) {
  return function(x, y) {
    if (func(x, y)) {
      return -1;
    } else if (func(y, x)) {
      return 1;
    } else {
      return 0;
    }
  };
}

[100, 1, 0, 10, -1, -2, -1].sort(compare(lessOrEqual));
// => [-2, -1, -1, 0, 1, 10, 100]
```

`compare` 関数は大小比較を行う関数を引数にとり、`compareFunction` の仕様に則った関数を生成して返す高階関数になります。


### JavaScript における関数型プログラミング

- 関数による抽象の定義（`existy` や `truthy`）
- 関数を他の関数の引数に渡すことによる新たな動作の実現

```javascript
function existy(x) { return x != null; }
function truthy(x) { return (x !== false) && existy(x); }

[null, undefined, 1, 2, false].map(existy);
// => [false, false, true, true, true]
[null, undefined, 1, 2, false].map(truthy);
// => [false, false, true, true, false]
```

これこそが、**関数型プログラミング** です。関数型プログラミングは強力な *データ抽象* とともに実装されて役に立つものです。


### 命令型プログラミングと関数型プログラミングの違いの例

*99本のビール* の歌の歌詞を構築するプログラムを命令型プログラミングと関数型プログラミングで書いてみた例です。

```javascript 命令型プログラミング
var lyrics = [];

for (var bottles = 99; bottles > 0; bottles--) {
  lyrics.push(bottles + '本のビールが残ってる');
  lyrics.push(bottles + '本のビール');
  lyrics.push('ひとつ取って、隣に回せ');
  if (bottles === 1) {
    lyrics.push('もうビールは残ってない');
  }
}
```

関数型プログラミングのアプローチで書くとこうなります。

```javascript 関数型プログラミング
// var _ = require('underscore');
function lyricsSegment(n) {
  return _.chain([])
    .push(n + '本のビールが残ってる')
    .push(n + '本のビール')
    .push('ひとつ取って、隣に回せ')
    .tap(function(lyrics) {
        if (n < 1) {
          lyrics.push('もうビールは残ってない');
        }
    })
    .value();
}

function song(start, end, lyricGen) {
  return _.reduce(_.range(start, end, -1),
    function(acc, n) {
      return acc.concat(lyricGen(n));
    }, []);
}

song(99, 0, lyricSegment);
```

*命令型プログラミングは一度きりの実装となり、再利用が難しいものになります。*

### コレクション中心プログラミング

関数型プログラミングは、コレクションに入った多数のアイテムに同じ操作を適用するようなタスクを行う際にとても便利です。

一般的な関数型プログラミングによって主張されるコレクション中心の考え方のポイントは、その処理を行うために一貫性のある語彙を確立し、包括的に揃えた関数を再利用出来るようにすることです。

{% blockquote Alan Perlis %}
10種類のデータ構造上で10の関数を動かすよりも、ひとつのデータ構造上で100の関数を動かすほうがよい。
{% endblockquote %}

関数型プログラミングでは、*それぞれの関数がすこしずつデータを変換し、最後の解に至る* ようにします。

### 値ではなく、関数を使え

ある値を何度か繰り返す関数はよい関数ですが、ある計算を何度か繰り返す関数のほうがよりよい関数です。

```javascript よい関数
function repeat(times, VALUE) {
  return _.map(_.range(times), function() { return VALUE; });
}

repeat(4, 'Major');
// => ["Major", "Major", "Major", "Major"]
```

```javascript よりよい関数
function repeatedly(times, func) {
  return _.map(_.range(times), func);
}

repeatedly(4, function() { return 'Major'; });
// => ["Major", "Major", "Major", "Major"]
repeatedly(3, function() { return Math.floor((Math.random() * 10) + 1); });
// => [1, 3, 8]
```

関数型スタイルのプログラミングでは、*実行の主体となるオブジェクトを引数に取る関数が好まれます*。


## メモ

本書の中に出てきた、関数とかのメモ。

### predicate

常に真偽値（`true` or `false`）を返す関数を **predicate** と呼びます。

```javascript
function lessOrEqual(x, y) {
  return x <= y;
}
```

### existy と truthy

`existy` は与えられた値が存在するかどうかを返す関数です。`truthy` は与えられた値が `true` とみなされるかどうかを返す関数です。

```javascript
function existy(x) {
  return x != null;
}

function truthy(x) {
  return (x !== false) && existy(x);
}
```

`truthy` は0を `true` と判定します。これは JavaScript の標準の動作とは違いますが、0を `false` と同一とみなすのは C 言語の遺物であると筆者は考えているようです。

### JavaScript 関数の設計指針

JavaScript ではある条件が `true` の場合のみ処理を行い、それ以外の場合には `undefined` か `null` を返す動作が使いやすい場合が多い。


### メタプログラミングとは

{% blockquote %}
プログラミングは何かを行うためにコードを書くものであるが、メタプログラミングは何かを解釈する方法を変更するためにコードを書くものである。
{% endblockquote %}

### 第一級関数

第一級関数は他のデータ型と同じように、扱うことができる関数のことです。

- 変数に格納できる
- 必要に応じて生成できる
- 他の関数の引数として渡すことができる
- 関数の戻り値として返すことができる

### コレクション中の `null` に対する防御用の関数 `fnull`

`fnull` は関数と可変長引数をとる関数です。与えられた関数の実行を行う際に `undefined` か `null` の値の場合にはデフォルト値を使うようにして関数を呼び出します。

```javascript fnull
function fnull(func /* , 可変長のデフォルト引数 */) {
  var defaults = _.rest(arguments);
  return function( /* args */ ) {
    var args = _.map(arguments, function(e, i) { return existy(e) ? e : defaults[i]; });
    return func.apply(null, args);
  };
}

var safeMulti = fnull(function(total, n) { return total * n; }, 1, 1);
_.reduce([1, 2, 3, null, 5], safeMulti);
// => 30
```

## 感想

JavaScript の関数の基本的なところから説明をはじめて、**関数型プログラミング**、**クロージャ**、**高階関数**、**カリー化**、**再帰**、**フィルタ**、**パイプライン**等の関数の設計方法がふんだんに解説されています。

説明がとても丁寧で分かりやすく、関数型プログラミングのことを知らない人でも理解しやすいと思います。

JavaScript は Java に次いで最も多く利用されている言語ですので、JavaScript をよりよく使うために本書を活用してみてはいかがでしょうか。おすすめです。
