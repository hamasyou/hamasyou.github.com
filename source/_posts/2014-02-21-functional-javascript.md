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

JavaScript では関数型プログラミングは一つのスタイルにしかすぎず、必要な場面で適切に使うというアプローチをとることでよりよいコードや使いやすい関数になります。

<!-- more -->

## 関数型プログラミング

![関数型プログラミング](/images/2014-02-21-functional-javascript-02.jpg)


本書のおぼえがきです。本書の中から気になった箇所だけ抜き出しているので、意味が通じないかもしれません。。気になったら買って読んでみてください。

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

これこそが、**関数型プログラミング** です。関数型プログラミングは強力な **データ抽象** とともに実装されて役に立つものです。


### 命令型プログラミングと関数型プログラミングの違いの例

「99本のビール」の歌の歌詞を構築するプログラムを命令型プログラミングと関数型プログラミングで書いてみた例です。

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

**命令型プログラミングは一度きりの実装となりがちで、再利用が難しいものになります**。

### コレクション中心プログラミング

関数型プログラミングは、*コレクションに入った多数のアイテムに同じ操作を適用するようなタスク* を行う際にとても便利です。

一般的な関数型プログラミングによって主張されるコレクション中心の考え方のポイントは、その処理を行うために一貫性のある語彙を確立し、包括的に揃えた関数を再利用出来るようにすることです。

{% blockquote Alan Perlis %}
10種類のデータ構造上で10の関数を動かすよりも、ひとつのデータ構造上で100の関数を動かすほうがよい。
{% endblockquote %}

関数型プログラミングでは、**それぞれの関数がすこしずつデータを変換し、最後の解に至る** ようにします。

### 値ではなく、関数を使え

ある値を何度か繰り返す関数はよい関数ですが、**ある計算を何度か繰り返す関数のほうがよりよい** 関数です。

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


## 関数を作る関数を作る意義

### カリー化

![カリー化](/images/2014-02-21-functional-javascript-01.jpg)

カリー化された関数というのは、*引数を一つ受け取るたびに新しい関数を返す関数* のことです。`invoker` 関数はカリー化された関数といえます。

```javascript カリー化された関数
invoker('reverse', Array.prototype.reverse)([1,2,3]);
// => [3,2,1]
```

JavaScript は変数の値の変更に対しておおらかな言語です。オブジェクト指向のカプセル化のような事をする場合、クロージャを使うと便利です。

クロージャを使うと、生成時のコンテキストにもとづいて特定の動作を行うように「*設定された*」関数を返すことができます。この設定された関数は外部からカプセル化されており、関数の抽象を高めます。

カリー化された関数も同じ考え方になります。

### JavaScript でカリー化を行う意味

まず、シンプルな高階関数をつくります。関数を引数に取り、引数をひとつだけ取るように限定された関数を返す関数です。

```javascript curry
function curry(func) {
  return function(arg) {
    return func(arg);
  };
}
```

JavaScript の関数は **引数をいくら渡してもエラーになりません**。そこで、いくつかの「*専門化を行う*」引数をオプションで取る場合がよくあります。

例えば `parseInt` という関数は、第2引数に n 進数の n をオプションで受け取ります。

```javascript parseInt
parseInt('11');
// => 11
parseInt('11', 2);
// => 3
```

`parseInt` を第一級関数として使うと第2引数が原因で混乱するケースがあります。

```javascript
['11', '11', '11', '11'].map(parseInt);
// => [11, NaN, 3, 4]
```

JavaScript の `map` は与えられた関数の第1引数に *要素*、第2引数に *インデックス*、第3引数に *元の配列* を渡します。そのため、こんな結果になってしまいます。

ここで、先ほど定義した `curry` 関数を利用します。与えられた関数に一つだけ引数を渡すように強制してみます。

```javascript
['11', '11', '11', '11'].map(curry(parseInt));
// => [11, 11, 11, 11]
```

引数を2つ取るようにする `curry2` は次のようになります。

```javascript curry2
function curry2(func) {
  return function(secondArg) {
    return function(firstArg) {
      return func(firstArg, secondArg);
    };
  };
}

parseIntByBinary = curry2(parseInt)(2);
parseIntByBinary('111');
// => 7
parseIntByBinary('10');
// => 2
```

このように、**カリー化は JavaScript の関数の動作を「専門化」させるための有効なテクニック** になります。


## 再帰

![再帰](/images/2014-02-21-functional-javascript-03.jpg)

再帰が関数型プログラミングに重要である3つの理由。

- 共通の問題のサブセットに単一の抽象を使用する
- 可変の状態を隠蔽することができる
- 遅延評価や無限データ構造の処理を行う手段のひとつ

配列の長さを調べる関数を再帰の考え方で解くと。

1. もし配列が空であれば長さは0
1. 配列の最初の要素を取り出し、残りを自身の関数に渡した実行結果に1を加える

```javascript length
function length(array) {
  if (_.isEmpty(array)) {
    return 0;
  } else {
    return 1 + length(_.rest(array));
  }
}
```

再帰をうまく実装するヒントは、「**値は大きな問題に内包された小さな問題によって組み立てられるものである**」と認識することです。

*基本的な場合を考える* ことで、分解の最初の一歩を踏み出しやすくなります。


## 純粋関数と冪等とデータの不変性

![データの普遍性](/images/2014-02-21-functional-javascript-05.jpg)

### 純粋関数

**純粋関数** とは、その関数が外部に一切の影響を与えないことが静的に保証されている関数です。純粋性のルールはつぎのようになります。

- 結果は引数として与えられた値からのみ計算される
- 関数の外部で変更される可能性のあるデータに一切依存しない
- 関数実行部の外側に存在する何かの状態を一切変更しない

[純粋関数](https://github.com/k3kaimu/d-manual/blob/master/function.md#%E7%B4%94%E7%B2%8B%E9%96%A2%E6%95%B0pure)


### 冪等（べきとう）

RESTful なアーキテクチャが一般化するにつれて **冪等（べきとう）** の考え方も一般に浸透してきました。冪等とは、*あるアクションを何度行っても一度行った場合とまったくおなじ効果をもたらす* というものです。冪等である関数は次の条件を満たします。

```javascript 冪等な関数
someFunc(arg) == _.compose(someFunc, someFunc)(arg);
```

ある引数を与えて実行するということは、その関数を2回連続で呼び出しても同じ結果を返します。

### 不変性

JavaScript はダイナミックな言語です。*純粋関数のポリシーに縛られるがゆえに、関数のダイナミックさを犠牲にする必要はありません*。

しかし、変数は変更するたびにその変数を参照するタイミングによって異なる値が返ってきます。*プログラムのすべての状態変更を除去することは不可能ですが、可能なかぎり減らすほうがよい* です。

そして、*関数型プログラミングの理想な状況とは、可変なものが全く存在しない* という状況です。

関数を **抽象の基本的な単位** としてみる利点は、関数の実装内容が外部に「*漏れる*」ことがなければ、関数の利用側は関数の実装に無関係であることです。

つまり、関数型プログラミングにおける関数でも、内部で命令的なプログラミングをしていたり、変数の状態を変更していたとしても、外部にもれなければ問題にはならないということです。

JavaScript は **関数が変数の境界をつくる** ので、ローカル変数の状態変更は関数が外部に漏れるのを防いでくれます。

{% blockquote Rich Hickey http://clojure.org/transients %}
誰もいない森で木が倒れたら、音がするでしょうか？
不変性を持った戻り値を生成するために、純粋関数がローカルデータを変異させたとしたら、それは良いことなのでしょうか？
{% endblockquote %}

結局のところ、答えは **Yes** です。

### 不変性を保つためにとりうる手段

JavaScript のオブジェクトフィールドは常に参照可能なので、オブジェクトは不変ではありません。不変性を保つために関数の実装側がとりえる手段は実質つぎの3つのどれかになります。

- 浅いコピーで十分な場合は `_.clone` を使ってコピーする
- 深いコピーが必要な場合は `deepClone` を使う
- 純粋関数を使ってコードを記述する


## メモ

![メモ](/images/2014-02-21-functional-javascript-04.jpg)

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

### invoker

`invoker` は関数を返す関数です。引数にメソッド名を取り、メソッドの実行主体のオブジェクトを渡すとそのオブジェクトでメソッドを実行する関数を返します。

```javascript invoker
function invoker(NAME, METHOD) {
  return function(target /* , 任意の引数 */) {
    if (!existy(target)) fail('Must provide a target');

    var targetMethod = target[NAME];
    var args         = _.rest(arguments);
    return doWhen((existy(targetMethod) && METHOD == targetMethod), function() {
      return targetMethod.apply(target, args);
    });
  };
}

var rev = invoker('reverse', Array.prototype.reverse);
_.map([[1, 2, 3]), rev);
// => [[3, 2, 1]]
```

関数型のスタイルでは、*関数の実行主体となるオブジェクトを引数にとる関数の形が好まれます*。

### メソッドチェーン

メソッドチェーンは「*流暢な*」API を構築するために有用なパターンです。流暢な API とは例えば次のようなものです。

```javascript 流暢な API の例
createPerson()
  .setFirstName('Mike')
  .setLastName('Fogus')
  .setAge(108)
  .toString();
```

メソッドチェーンを実現する「*魔法*」は *チェーン内のそれぞれのメソッドが同じホストのオブジェクトの参照を返す* ことです。

上の方のメモで、関数が前提条件に合わない引数を受け取った場合は `undefined` か `null` を返すのがよいとありました。メソッドチェーンで使うことを考えている関数の場合は `undefined`、`null`、`同じホストのオブジェクト` のどれを返せばいいんでしょうかね。。。


## 感想

JavaScript の関数の基本的なところから説明をはじめて、**関数型プログラミング**、**クロージャ**、**高階関数**、**カリー化**、**再帰**、**フィルタ**、**パイプライン**等の関数の設計方法がふんだんに解説されています。

説明がとても丁寧で分かりやすく、関数型プログラミングのことを知らない人でも理解しやすいと思います。

関数型プログラミングを学ぶと、**純粋関数**、**データの不変性** というテーマに当たります。JavaScript にはないですが、Ruby ではメソッドに `!` が付いた自分自身を変更する破壊的なメソッドがあります。関数型プログラミングとオブジェクト指向プログラミングをうまい具合に取り入れている言語だと再認識しました。

JavaScript で関数型プログラミングを学ぶというテーマですが、*関数型プログラミングを学ぶことで他の言語の面白い点も見えてくる* というのが読了しての感想です。

JavaScript は Java に次いで最も多く利用されている言語ですので、**JavaScript をよりよく使うため、他の関数型言語をより楽しむため** に本書を活用してみてはいかがでしょうか。おすすめです。
