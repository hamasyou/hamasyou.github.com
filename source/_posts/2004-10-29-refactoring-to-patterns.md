---
layout: post
title: "Refactoring to Patterns メモ"
date: 2004-10-29 11:15
comments: true
categories: [Blog]
keywords: "Refactoring,Patterns,リファクタリング,パターン,カタログ,Addison Wesley"
tags: [リファクタリング,デザインパターン]
author: hamasyou
amazon_url: ""
amazon_author: ""
amazon_image: ""
amazon_publisher: ""
---

<p>
<a href="http://www.amazon.co.jp/exec/obidos/ASIN/0321213351/sorehabooks-22" rel="external nofollow"></a>
</p>

『<a href="http://www.amazon.co.jp/exec/obidos/ASIN/0321213351/sorehabooks-22" rel="external nofollow">『リファクタリング―プログラムの体質改善テクニック』</a>の続編になっています。

例のごとく、自分で読み直したときにどんな場合にどのリファクタリングが使えるかのメモにしようと思ってます。2005年中くらいには、本書の和訳が出版される予定です。

現在、洋書読破中。記事もそのつど更新していくつもりです。(最終更新日：2004/10/29)

本家サイトはこちらになります。カタログ一覧あります。

<a href="http://www.industriallogic.com/xp/refactoring/" rel="external nofollow">Refactoring To Patterns</a>


<!-- more -->

<h2>インデックス</h2>

<ul><li><a href="#Form Template Method" rel="external nofollow">Form Template Method</a></li>
<li><a href="#Introduce Polymorphic Creation with Factory Method" rel="external nofollow">Introduce Polymorphic Creation with Factory Method</a></li>
<li><a href="#Chain Constructors" rel="external nofollow">Chain Constructors</a></li>
<li><a href="#Replace One/Many Distinctions with Composite" rel="external nofollow">Replace One/Many Distinctions with Composite</a></li>
<li><a href="#Extract Composite" rel="external nofollow">Extract Composite</a></li>
<li><a href="#Unify Interfaces with Adapter" rel="external nofollow">Unify Interfaces with Adapter</a></li>
<li><a href="#Introduce Null Object" rel="external nofollow">Introduce Null Object</a></li>
<li><a href="#Compose Method" rel="external nofollow">Compose Method</a></li>
<li><a href="#Move Accumulation to Collectiong Parameter" rel="external nofollow">Move Accumulation to Collectiong Parameter</a></li>
<li><a href="#Replace Conditional Dispatcher with Command" rel="external nofollow">Replace Conditional Dispatcher with Command</a></li>
<li><a href="#Move Accumulation to Visitor" rel="external nofollow">Move Accumulation to Visitor</a></li>
<li><a href="#Replace Conditional Logic with Strategy" rel="external nofollow">Replace Conditional Logic with Strategy</a></li>
<li><a href="#Move Embellishment to Decorator" rel="external nofollow">Move Embellishment to Decorator</a></li>
<li><a href="#Replace State-Altering Conditionals with State" rel="external nofollow">Replace State-Altering Conditionals with State</a></li>
<li><a href="#Replace Type Code with Class" rel="external nofollow">Replace Type Code with Class</a></li>
<li><a href="#Replace Implicit Language with Interpreter" rel="external nofollow">Replace Implicit Language with Interpreter</a></li>
<li><a href="#Encapsulate Composite with Builder" rel="external nofollow">Encapsulate Composite with Builder</a></li>
<li><a href="#Replace Constructors with Creation Methods" rel="external nofollow">Replace Constructors with Creation Methods</a></li>
<li><a href="#Move Creation Knowledge to Factory" rel="external nofollow">Move Creation Knowledge to Factory</a></li>
<li><a href="#Encapsulate Classes with Factory" rel="external nofollow">Encapsulate Classes with Factory</a></li>
<li><a href="#Inline Singleton" rel="external nofollow">Inline Singleton</a></li>
<li><a href="#Replace Implicit Tree with Composite" rel="external nofollow">Replace Implicit Tree with Composite</a></li></ul>

<h2>場合別リファクタリングカタログ</h2>

カタログ名のリンクをクリックすると、別ウィンドウで本家のイメージ図が開きます。一緒に見るとイメージがわくかも。

<h3 id="Form Template Method"><a href="http://www.industriallogic.com/xp/refactoring/formTemplateMethod.html" rel="external nofollow">Form Template Method</a></h3>

継承関係にあるサブクラス間の明白な、または微妙な重複コードを取り除く

<h3 id="Introduce Polymorphic Creation with Factory Method"><a href="http://www.industriallogic.com/xp/refactoring/polymorphicCreationFactory.html" rel="external nofollow">Introduce Polymorphic Creation with Factory Method</a></h3>

サブクラスで似たような処理を実装している場合、オブジェクト生成段階で Template Method パターンを用いることによって重複コードを減らすことができる。

オブジェクト生成をポリモーフィズムで行うことで、オブジェクト指向っぽくなります。このリファクタリングを行った後のコードは、サブクラス間で重複した同じようなコードがなくなります。

Factory Method パターンと Template Method パターンはよく同時に使われます。

<h3 id="Chain Constructors"><a href="http://www.industriallogic.com/xp/refactoring/chainConstructors.html" rel="external nofollow">Chain Constructors</a></h3>

コンストラクタで似たような処理が重複している場合、このパターンで重複を除去できる。

<h3 id="Replace One/Many Distinctions with Composite"><a href="http://www.industriallogic.com/xp/refactoring/onemanyDistinctionsWithComposite.html" rel="external nofollow">Replace One/Many Distinctions with Composite</a></h3>

一つのオブジェクトもしくはオブジェクトのコレクションを処理するコードが散らばっているような場合に、重複を除去できる可能性がある。

<h3 id="Extract Composite"><a href="http://www.industriallogic.com/xp/refactoring/extractComposite.html" rel="external nofollow">Extract Composite</a></h3>

階層関係にあるサブクラスで、個々のクラスがコンポジット関係にある場合、実装はほとんど同じである場合が多い。そのようなときに使える。

<h3 id="Unify Interfaces with Adapter"><a href="http://www.industriallogic.com/xp/refactoring/interfacesWithAdapter.html" rel="external nofollow">Unify Interfaces with Adapter</a></h3>

異なったインターフェースを持つからという理由で、単に違うオブジェクトを処理するのなら、Adapterと統一のインターフェースを利用して、共通のロジックを除去出来るかもしれない。

<h3 id="Introduce Null Object"><a href="http://www.industriallogic.com/xp/refactoring/nullObject.html" rel="external nofollow">Introduce Null Object</a></h3>

オブジェクトが null の場合に条件句で何の処理も行わない場合が多くある時、このパターンで重複が減らせるかもしれない。

<h3 id="Compose Method"><a href="http://www.industriallogic.com/xp/refactoring/composeMethod.html" rel="external nofollow">Compose Method</a></h3>

長くて理解しにくいメソッドがある場合、処理を構成する部分をわける。この方法は、リファクタリングの Extract Method を多く用いる。

処理を区切るときの注意点として、<strong>同じ処理の詳細レベルになるようにメソッド化する</strong>ことがあげられる。

このリファクタリングを適用すると、private メソッドや小さな処理単位のメソッドが非常にたくさんできる。その場合は、Extract Class リファクタリングを用いて、クラスを分けるなどして対応するとよい。

<h3 id="Move Accumulation to Collectiong Parameter"><a href="http://www.industriallogic.com/xp/refactoring/accumulationToCollection.html" rel="external nofollow">Move Accumulation to Collectiong Parameter</a></h3>

情報を変換していく作業がある場合、処理を構成する別々のメソッドを定義して、処理の順番で一つの情報を構成するようにする。

<h3 id="Replace Conditional Dispatcher with Command"><a href="http://www.industriallogic.com/xp/refactoring/conditionDispatcherWithCommand.html" rel="external nofollow">Replace Conditional Dispatcher with Command</a></h3>

条件文 (switch 文など)によって処理を分岐させるようなコーディングは、ハードコードになり、新しい条件が付け加わったりリクエストが増えたりした場合に、修正が追いつかなくなる。

対応策として、Command パターンを用いるとよい。Command クラスにパラメータやリクエストを保持させるようにし、処理内容を Command 自体に行わせる。条件パラメータやリクエストが増えたら、新しいCommand クラスを作ることで対応できるようになる。

<h3 id="Move Accumulation to Visitor"><a href="http://www.industriallogic.com/xp/refactoring/accumulationToVisitor.html" rel="external nofollow">Move Accumulation to Visitor</a></h3>

異なったインターフェースのオブジェクトから情報を取得するために、巨大な switch 文を使うくらいなら、Visitor パターンを利用してデータを集めるようにする。

<h3 id="Replace Conditional Logic with Strategy"><a href="http://www.industriallogic.com/xp/refactoring/conditionalWithStrategy.html" rel="external nofollow">Replace Conditional Logic with Strategy</a></h3>

条件によって処理のアルゴリズムが違うような場合は、 条件句をStrategy パターンで置き換える。

このパターンを使う動機の一つとしてこのようなことがあげられる。まず、条件分岐のロジックを書く場合というのはよく、アルゴリズムを決めるためということが多い。Decompose Conditional パターンや <a href="http://www.industriallogic.com/xp/refactoring/composeMethod.html" rel="external nofollow">Compose Method</a>パターンを用いてこのロジックの部分をシンプルに、分かりやすくすることが出来る。

しかし一方では、Decompose Conditionalパターンや Compose Method パターンを使ってロジックをメソッドに分割すると、小さなメソッドがたくさん出来てしまう。それを防ぐために、このReplace Conditional Logic with Strategy パターンを使う。

<h3 id="Move Embellishment to Decorator"><a href="http://www.industriallogic.com/xp/refactoring/embellishmentToDecorator.html" rel="external nofollow">Move Embellishment to Decorator</a></h3>

クラスの核となる責務を装飾するようなコードは、条件句で装飾をするかどうかを判断するのではなくて、装飾するコードをDecoratorクラスに委譲するようにする。

Decorator(装飾するクラス)とDecoratee(装飾されるクラス)は同じインターフェースを持つ。しかし、オブジェクトのクラスが一致するかどうかを判定するような場合(instanceof演算子を使う場合)は注意する必要があります。Decorateされたインスタンスは、Decorateeと同じインターフェースを持ちますが、通常は継承関係にはないので、クラスが一致することはありません。

クラスの同一性に依存するようなコードがクライアントにない場合にのみ、Decoratorパターンを使います。Decorator クラスは状態(フィールド)を持たない方がいいです。なぜならDecorator クラスは不必要な状態(フィールド)があったとしても、継承せざるを得なくなるからです。

<h3 id="Replace State-Altering Conditionals with State"><a href="http://www.industriallogic.com/xp/refactoring/alteringConditionalsWithState.html" rel="external nofollow">Replace State-Altering Conditionals with State</a></h3>

オブジェクトの状態遷移が複雑になるとき、Stateパターンを使って状態遷移を簡略化する。

状態遷移のロジックというのは、さまざまなところに重複ロジックが散乱することが多い。状態遷移のロジックを他のクラス(Stateクラス)に移動させることで、コードがシンプルになる。ただし、最初からこのリファクタリングを適用するのではなく、通常はもっと簡単な<i>Extract Method</i>パターンを適用する。

<dl>
<dt class="info">Stateパターンと Strategyパターンの違い</dt>
<dd>Stateパターンと Strategyパターンは、考え方の違いである(最終的なクラス図はほとんど同じ形)。Stateパターンは、状態クラスのインスタンス間で状態遷移のロジックが簡単になるように使われる。一方の Strategyパターンは、ストラテジークラスのインスタンスにアルゴリズムの実行を委譲(delegate)する目的で使う。</dd>
</dl>

<h3 id="Replace Type Code with Class"><a href="http://www.industriallogic.com/xp/refactoring/typeCodeWithClass.html" rel="external nofollow">Replace Type Code with Class</a></h3>

プリミティブ型をつかって型(タイプ)を判別しているようなら、 Type クラスを作って、型の安全性を保証したほうがよい。

<h3 id="Replace Implicit Language with Interpreter"><a href="http://www.industriallogic.com/xp/refactoring/implicitLanguageWithInterpreter.html" rel="external nofollow">Replace Implicit Language with Interpreter</a></h3>

クラスの責務が、プリミティブ型の組み合わせで成り立つような場合、ある種の言語と考えて Interpreter パターンを使うといいかもしれない。

<h3 id="Encapsulate Composite with Builder"><a href="http://www.industriallogic.com/xp/refactoring/compositeWithBulder.html" rel="external nofollow">Encapsulate Composite with Builder</a></h3>

Compositeパターンを使った再帰的な構造を組み立てる場合に Builder を使うとクライアントはシンプルになる。

通常、Compositeパターンを使って構造を組み立てる場合、実装クラスに関連してしまう(カタログの場合 TagNode クラス)。Compositeを組み立てる Builder クラスを用意することで、実装を知らずにクライアントは構造を組み立てることが出来るようになる。

<dl>
<dt class="info">このパターンを使う動機</dt>
<dd>
<pre>
１． 複雑なオブジェクトを構築しなければならない場合にクライアントの実装はシンプルに保ちたい
２． クライアントをCompositeの詳細から切り離したい
</pre>
</dd>
</dl>

CompositeパターンとBuilderパターンは同時に使われることが多い。理由は2番の動機のように、クライアントがCompositeの詳細を知らなくてもすむようにしたいから。このように設計しておくことで、Compositeの実装が変わった場合にBuilderでクライアントの変更を吸収出来る場合が多くなる

<h3 id="Replace Constructors with Creation Methods"><a href="http://www.industriallogic.com/xp/refactoring/constructorCreation.html" rel="external nofollow">Replace Constructors with Creation Methods</a></h3>

クラスにコンストラクタがたくさんあり、プログラマーがどのコンストラクタを呼べばよいのか分かりにくい場合にこのリファクタリングが使える。複数のコンストラクタよりも、Create Method を用意したほうが良い理由は、<b>メソッドに名前が付けられるから、どんな処理が行われるのか名前から判断できる</b>から。(コンストラクタじゃ全部同じ名前で分からない)

<h3 id="Move Creation Knowledge to Factory"><a href="http://www.industriallogic.com/xp/refactoring/creationWithFactory.html" rel="external nofollow">Move Creation Knowledge to Factory</a></h3>

色々なクラスにインスタンス生成の情報が広がってしまっている場合、単一のFactoryクラスを作成することでシンプルになるかもしれない。

インスタンス生成の役割は色々なクラスに広がるべきではない。今回のようなaStringNodeインスタンス生成の方法をaParser、aStringParser、StringNodeに散らばらせるのではなく、NodeFactoryを導入しインスタンス生成と設定をカプセル化すべき。(クラスには単一の責務のみを持たせるべきだから)

生成するインスタンスがロジックによって違ってくるような場合、AbstractFactoryパターンを使ってFactoryを分けることで対応できるようになる。aParserにインスタンス生成の責務を負わせていると、条件分岐のロジックが重複してしまう可能性が高い。

<h3 id="Encapsulate Classes with Factory"><a href="http://www.industriallogic.com/xp/refactoring/classesWithFactory.html" rel="external nofollow">Encapsulate Classes with Factory</a></h3>

クラスのクライアントが直接、実装に依存してしまう関連よりも Factory メソッドを用意して抽象に依存させるようにする。

「<strong>抽象に依存せよ。実装に依存するな。</strong>」は、良いクラス設計のポイントです。このパターンを適用することでクライアントは実装に依存せずに、抽象インターフェースにのみ依存するようになります。

ただし一つ問題があります。このパターンでは、子クラスが増えた場合に Factory クラスを修正しなければなりません。インターフェースの変更(追加)を行わなければならなくなるので、子クラスがほとんど追加されないような場合にだけ使った方が良いと思います。

<h3 id="Inline Singleton"><a href="http://www.industriallogic.com/xp/refactoring/inlinesingleton.html" rel="external nofollow">Inline Singleton</a></h3>

不適切なSingletonの使い方をしている部分をリファクタリングします。

Singletonパターンを必要としない場面は下記のようなときがあります。

<ul><li>Singletonオブジェクトを使うクラスが、Singletonオブジェクトを引数などで簡単に渡してもらえる場合</li><li>些細なメモリやパフォーマンスを改善するためだけに使われるSingletonオブジェクト</li><li>下層のクラスが同一層のリソース以外を使う目的で使われているSingletonオブジェクト</li></ul>

Singletonを使うべき理由は以下だけです。

<strong>たった一つのインスタンスであることを保証したい場合とグローバルアクセスポイントを提供したい場合</strong>

<dl>
<dt class="info">図の説明</dt>
<dd>
<p>カタログに載ってる図は、アプリケーション層にある Blackjackクラスが 上位層の Console クラスに依存している。リファクタリング前のコードでは、ConsoleクラスをHitStayResponseオブジェクトを上位層から下位層に渡すためのRegistの役割に使っている。</p>

<p>リファクタリング後のコードでは、素直に Blackjackクラスにセッターメソッドを用意して、無駄なRegistクラスを作らないように修正されている。</p>
</dd>
</dl>

<h3 id="Replace Implicit Tree with Composite"><a href="http://www.industriallogic.com/xp/refactoring/implicitTreeWithComposite.html" rel="external nofollow">Replace Implicit Tree with Composite</a></h3>

ツリー構造を構成するような処理を、<i>Composite</i> パターンをを使って分かりやすくします。

XMLデータなどのツリー形式を構成するデータ構造を扱う場合、<i>Composite</i> パターンを使ってクラスを分割すると、ロジックがすっきりすることがあります。


<h2>参考</h2>

+ Refactoring to Patterns のWikiです。随時サンプルコードが増えていく予定のようです。
<img src="http://hamasyou.com/images/img-link.gif"   align="middle" /><a href="http://capsctrl.que.jp/kdmsnr/wiki/RtP/" rel="external nofollow">Refactorint to Patterns Wiki(サンプルコード有)</a>

+ GoFのデザインパターンを学ぶ定番です。
<div class="rakuten"><table border="0" cellpadding="5" width="400"><tr><td valign="top"><a href="http://www.amazon.co.jp/exec/obidos/ASIN/4797327030/sorehabooks-22/" rel="external nofollow"></a><br /></td></tr></table>
</div>

+ GoF以外のデザインパターンが載ってます。PLoPで扱われている中で主なものを選んであります。
<div class="rakuten"><table border="0" cellpadding="5" width="400"><tr><td valign="top"><a href="http://www.amazon.co.jp/exec/obidos/ASIN/4797314397/sorehabooks-22/" rel="external nofollow"></a><br /></td></tr></table>
</div>

+ エンタープライズで使えるパターン集のバイブル的本です。(洋書)
<div class="rakuten"><table border="0" cellpadding="5" width="400"><tr><td valign="top"><a href="http://www.amazon.co.jp/exec/obidos/ASIN/0321127420/sorehabooks-22/" rel="external nofollow"></a><br /></td></tr></table>
</div>







