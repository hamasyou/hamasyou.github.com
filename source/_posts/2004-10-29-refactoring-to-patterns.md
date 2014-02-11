---
layout: post
title: "Refactoring to Patterns メモ"
date: 2004-10-29 11:15
comments: true
categories: [Engineer-Soul]
keywords: "Refactoring,Patterns,リファクタリング,パターン,カタログ,Addison Wesley"
tags: []
author: hamasyou
amazon_url: ""
amazon_author: ""
amazon_image: ""
amazon_publisher: ""
---

<p>
[ target="_blank"><img src="http://images-jp.amazon.com/images/P/0321213351.01.MZZZZZZZ.jpg"  border="0" />](http://www.amazon.co.jp/exec/obidos/ASIN/0321213351/sorehabooks-22)
</p>

『[ target="_blank" class="uline">REFACTORING TO PATTERNS](http://www.amazon.co.jp/exec/obidos/ASIN/0321213351/sorehabooks-22)』から、場合別リファクタリングカタログのメモです。本書は[ class="extlink" target="_blank">『リファクタリング―プログラムの体質改善テクニック』](http://www.amazon.co.jp/exec/obidos/ASIN/4894712288/sorehabooks-22)の続編になっています。

例のごとく、自分で読み直したときにどんな場合にどのリファクタリングが使えるかのメモにしようと思ってます。2005年中くらいには、本書の和訳が出版される予定です。

現在、洋書読破中。記事もそのつど更新していくつもりです。(最終更新日：2004/10/29)

本家サイトはこちらになります。カタログ一覧あります。

[ target="_blank" class="extlink">Refactoring To Patterns](http://www.industriallogic.com/xp/refactoring/)


<!-- more -->

<h2>インデックス</h2>

<ul><li>[Form Template Method](#Form Template Method)</li>
<li>[Introduce Polymorphic Creation with Factory Method](#Introduce Polymorphic Creation with Factory Method)</li>
<li>[Chain Constructors](#Chain Constructors)</li>
<li>[Replace One/Many Distinctions with Composite](#Replace One/Many Distinctions with Composite)</li>
<li>[Extract Composite](#Extract Composite)</li>
<li>[Unify Interfaces with Adapter](#Unify Interfaces with Adapter)</li>
<li>[Introduce Null Object](#Introduce Null Object)</li>
<li>[Compose Method](#Compose Method)</li>
<li>[Move Accumulation to Collectiong Parameter](#Move Accumulation to Collectiong Parameter)</li>
<li>[Replace Conditional Dispatcher with Command](#Replace Conditional Dispatcher with Command)</li>
<li>[Move Accumulation to Visitor](#Move Accumulation to Visitor)</li>
<li>[Replace Conditional Logic with Strategy](#Replace Conditional Logic with Strategy)</li>
<li>[Move Embellishment to Decorator](#Move Embellishment to Decorator)</li>
<li>[Replace State-Altering Conditionals with State](#Replace State-Altering Conditionals with State)</li>
<li>[Replace Type Code with Class](#Replace Type Code with Class)</li>
<li>[Replace Implicit Language with Interpreter](#Replace Implicit Language with Interpreter)</li>
<li>[Encapsulate Composite with Builder](#Encapsulate Composite with Builder)</li>
<li>[Replace Constructors with Creation Methods](#Replace Constructors with Creation Methods)</li>
<li>[Move Creation Knowledge to Factory](#Move Creation Knowledge to Factory)</li>
<li>[Encapsulate Classes with Factory](#Encapsulate Classes with Factory)</li>
<li>[Inline Singleton](#Inline Singleton)</li>
<li>[Replace Implicit Tree with Composite](#Replace Implicit Tree with Composite)</li></ul>

<h2>場合別リファクタリングカタログ</h2>

カタログ名のリンクをクリックすると、別ウィンドウで本家のイメージ図が開きます。一緒に見るとイメージがわくかも。

<h3 id="Form Template Method">[ target="_blank" class="extlink">Form Template Method](http://www.industriallogic.com/xp/refactoring/formTemplateMethod.html)</h3>

継承関係にあるサブクラス間の明白な、または微妙な重複コードを取り除く

<h3 id="Introduce Polymorphic Creation with Factory Method">[ target="_blank" class="extlink">Introduce Polymorphic Creation with Factory Method](http://www.industriallogic.com/xp/refactoring/polymorphicCreationFactory.html)</h3>

サブクラスで似たような処理を実装している場合、オブジェクト生成段階で Template Method パターンを用いることによって重複コードを減らすことができる。

オブジェクト生成をポリモーフィズムで行うことで、オブジェクト指向っぽくなります。このリファクタリングを行った後のコードは、サブクラス間で重複した同じようなコードがなくなります。

Factory Method パターンと Template Method パターンはよく同時に使われます。

<h3 id="Chain Constructors">[ target="_blank" class="extlink">Chain Constructors](http://www.industriallogic.com/xp/refactoring/chainConstructors.html)</h3>

コンストラクタで似たような処理が重複している場合、このパターンで重複を除去できる。

<h3 id="Replace One/Many Distinctions with Composite">[ target="_blank" class="extlink">Replace One/Many Distinctions with Composite](http://www.industriallogic.com/xp/refactoring/onemanyDistinctionsWithComposite.html)</h3>

一つのオブジェクトもしくはオブジェクトのコレクションを処理するコードが散らばっているような場合に、重複を除去できる可能性がある。

<h3 id="Extract Composite">[ target="_blank" class="extlink">Extract Composite](http://www.industriallogic.com/xp/refactoring/extractComposite.html)</h3>

階層関係にあるサブクラスで、個々のクラスがコンポジット関係にある場合、実装はほとんど同じである場合が多い。そのようなときに使える。

<h3 id="Unify Interfaces with Adapter">[ target="_blank" class="extlink">Unify Interfaces with Adapter](http://www.industriallogic.com/xp/refactoring/interfacesWithAdapter.html)</h3>

異なったインターフェースを持つからという理由で、単に違うオブジェクトを処理するのなら、Adapterと統一のインターフェースを利用して、共通のロジックを除去出来るかもしれない。

<h3 id="Introduce Null Object">[ target="_blank" class="extlink">Introduce Null Object](http://www.industriallogic.com/xp/refactoring/nullObject.html)</h3>

オブジェクトが null の場合に条件句で何の処理も行わない場合が多くある時、このパターンで重複が減らせるかもしれない。

<h3 id="Compose Method">[ target="_blank" class="extlink">Compose Method](http://www.industriallogic.com/xp/refactoring/composeMethod.html)</h3>

長くて理解しにくいメソッドがある場合、処理を構成する部分をわける。この方法は、リファクタリングの Extract Method を多く用いる。

処理を区切るときの注意点として、<strong>同じ処理の詳細レベルになるようにメソッド化する</strong>ことがあげられる。

このリファクタリングを適用すると、private メソッドや小さな処理単位のメソッドが非常にたくさんできる。その場合は、Extract Class リファクタリングを用いて、クラスを分けるなどして対応するとよい。

<h3 id="Move Accumulation to Collectiong Parameter">[ target="_blank" class="extlink">Move Accumulation to Collectiong Parameter](http://www.industriallogic.com/xp/refactoring/accumulationToCollection.html)</h3>

情報を変換していく作業がある場合、処理を構成する別々のメソッドを定義して、処理の順番で一つの情報を構成するようにする。

<h3 id="Replace Conditional Dispatcher with Command">[ target="_blank" class="extlink">Replace Conditional Dispatcher with Command](http://www.industriallogic.com/xp/refactoring/conditionDispatcherWithCommand.html)</h3>

条件文 (switch 文など)によって処理を分岐させるようなコーディングは、ハードコードになり、新しい条件が付け加わったりリクエストが増えたりした場合に、修正が追いつかなくなる。

対応策として、Command パターンを用いるとよい。Command クラスにパラメータやリクエストを保持させるようにし、処理内容を Command 自体に行わせる。条件パラメータやリクエストが増えたら、新しいCommand クラスを作ることで対応できるようになる。

<h3 id="Move Accumulation to Visitor">[ target="_blank" class="extlink">Move Accumulation to Visitor](http://www.industriallogic.com/xp/refactoring/accumulationToVisitor.html)</h3>

異なったインターフェースのオブジェクトから情報を取得するために、巨大な switch 文を使うくらいなら、Visitor パターンを利用してデータを集めるようにする。

<h3 id="Replace Conditional Logic with Strategy">[ target="_blank" class="extlink">Replace Conditional Logic with Strategy](http://www.industriallogic.com/xp/refactoring/conditionalWithStrategy.html)</h3>

条件によって処理のアルゴリズムが違うような場合は、 条件句をStrategy パターンで置き換える。

このパターンを使う動機の一つとしてこのようなことがあげられる。まず、条件分岐のロジックを書く場合というのはよく、アルゴリズムを決めるためということが多い。Decompose Conditional パターンや [ target="_blank" class="extlink">Compose Method](http://www.industriallogic.com/xp/refactoring/composeMethod.html)パターンを用いてこのロジックの部分をシンプルに、分かりやすくすることが出来る。

しかし一方では、Decompose Conditionalパターンや Compose Method パターンを使ってロジックをメソッドに分割すると、小さなメソッドがたくさん出来てしまう。それを防ぐために、このReplace Conditional Logic with Strategy パターンを使う。

<h3 id="Move Embellishment to Decorator">[ target="_blank" class="extlink">Move Embellishment to Decorator](http://www.industriallogic.com/xp/refactoring/embellishmentToDecorator.html)</h3>

クラスの核となる責務を装飾するようなコードは、条件句で装飾をするかどうかを判断するのではなくて、装飾するコードをDecoratorクラスに委譲するようにする。

Decorator(装飾するクラス)とDecoratee(装飾されるクラス)は同じインターフェースを持つ。しかし、オブジェクトのクラスが一致するかどうかを判定するような場合(instanceof演算子を使う場合)は注意する必要があります。Decorateされたインスタンスは、Decorateeと同じインターフェースを持ちますが、通常は継承関係にはないので、クラスが一致することはありません。

クラスの同一性に依存するようなコードがクライアントにない場合にのみ、Decoratorパターンを使います。Decorator クラスは状態(フィールド)を持たない方がいいです。なぜならDecorator クラスは不必要な状態(フィールド)があったとしても、継承せざるを得なくなるからです。

<h3 id="Replace State-Altering Conditionals with State">[ target="_blank" class="extlink">Replace State-Altering Conditionals with State](http://www.industriallogic.com/xp/refactoring/alteringConditionalsWithState.html)</h3>

オブジェクトの状態遷移が複雑になるとき、Stateパターンを使って状態遷移を簡略化する。

状態遷移のロジックというのは、さまざまなところに重複ロジックが散乱することが多い。状態遷移のロジックを他のクラス(Stateクラス)に移動させることで、コードがシンプルになる。ただし、最初からこのリファクタリングを適用するのではなく、通常はもっと簡単な<i>Extract Method</i>パターンを適用する。

<dl>
<dt class="info">Stateパターンと Strategyパターンの違い</dt>
<dd>Stateパターンと Strategyパターンは、考え方の違いである(最終的なクラス図はほとんど同じ形)。Stateパターンは、状態クラスのインスタンス間で状態遷移のロジックが簡単になるように使われる。一方の Strategyパターンは、ストラテジークラスのインスタンスにアルゴリズムの実行を委譲(delegate)する目的で使う。</dd>
</dl>

<h3 id="Replace Type Code with Class">[ target="_blank" class="extlink">Replace Type Code with Class](http://www.industriallogic.com/xp/refactoring/typeCodeWithClass.html)</h3>

プリミティブ型をつかって型(タイプ)を判別しているようなら、 Type クラスを作って、型の安全性を保証したほうがよい。

<h3 id="Replace Implicit Language with Interpreter">[ target="_blank" class="extlink">Replace Implicit Language with Interpreter](http://www.industriallogic.com/xp/refactoring/implicitLanguageWithInterpreter.html)</h3>

クラスの責務が、プリミティブ型の組み合わせで成り立つような場合、ある種の言語と考えて Interpreter パターンを使うといいかもしれない。

<h3 id="Encapsulate Composite with Builder">[ target="_blank" class="extlink">Encapsulate Composite with Builder](http://www.industriallogic.com/xp/refactoring/compositeWithBulder.html)</h3>

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

<h3 id="Replace Constructors with Creation Methods">[ target="_blank" class="extlink">Replace Constructors with Creation Methods](http://www.industriallogic.com/xp/refactoring/constructorCreation.html)</h3>

クラスにコンストラクタがたくさんあり、プログラマーがどのコンストラクタを呼べばよいのか分かりにくい場合にこのリファクタリングが使える。複数のコンストラクタよりも、Create Method を用意したほうが良い理由は、<b>メソッドに名前が付けられるから、どんな処理が行われるのか名前から判断できる</b>から。(コンストラクタじゃ全部同じ名前で分からない)

<h3 id="Move Creation Knowledge to Factory">[ target="_blank" class="extlink">Move Creation Knowledge to Factory](http://www.industriallogic.com/xp/refactoring/creationWithFactory.html)</h3>

色々なクラスにインスタンス生成の情報が広がってしまっている場合、単一のFactoryクラスを作成することでシンプルになるかもしれない。

インスタンス生成の役割は色々なクラスに広がるべきではない。今回のようなaStringNodeインスタンス生成の方法をaParser、aStringParser、StringNodeに散らばらせるのではなく、NodeFactoryを導入しインスタンス生成と設定をカプセル化すべき。(クラスには単一の責務のみを持たせるべきだから)

生成するインスタンスがロジックによって違ってくるような場合、AbstractFactoryパターンを使ってFactoryを分けることで対応できるようになる。aParserにインスタンス生成の責務を負わせていると、条件分岐のロジックが重複してしまう可能性が高い。

<h3 id="Encapsulate Classes with Factory">[ target="_blank" class="extlink">Encapsulate Classes with Factory](http://www.industriallogic.com/xp/refactoring/classesWithFactory.html)</h3>

クラスのクライアントが直接、実装に依存してしまう関連よりも Factory メソッドを用意して抽象に依存させるようにする。

「<strong>抽象に依存せよ。実装に依存するな。</strong>」は、良いクラス設計のポイントです。このパターンを適用することでクライアントは実装に依存せずに、抽象インターフェースにのみ依存するようになります。

ただし一つ問題があります。このパターンでは、子クラスが増えた場合に Factory クラスを修正しなければなりません。インターフェースの変更(追加)を行わなければならなくなるので、子クラスがほとんど追加されないような場合にだけ使った方が良いと思います。

<h3 id="Inline Singleton">[ target="_blank" class="extlink">Inline Singleton](http://www.industriallogic.com/xp/refactoring/inlinesingleton.html)</h3>

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

<h3 id="Replace Implicit Tree with Composite">[ target="_blank" class="extlink">Replace Implicit Tree with Composite](http://www.industriallogic.com/xp/refactoring/implicitTreeWithComposite.html)</h3>

ツリー構造を構成するような処理を、<i>Composite</i> パターンをを使って分かりやすくします。

XMLデータなどのツリー形式を構成するデータ構造を扱う場合、<i>Composite</i> パターンを使ってクラスを分割すると、ロジックがすっきりすることがあります。


<h2>参考</h2>

+ Refactoring to Patterns のWikiです。随時サンプルコードが増えていく予定のようです。
<img src="http://hamasyou.com/images/img-link.gif"   align="middle" />[ target="_blank" class="uline">Refactorint to Patterns Wiki(サンプルコード有)](http://capsctrl.que.jp/kdmsnr/wiki/RtP/)

+ GoFのデザインパターンを学ぶ定番です。
<div class="rakuten"><table border="0" cellpadding="5" width="400"><tr><td valign="top">[<img src="http://images-jp.amazon.com/images/P/4797327030.09.MZZZZZZZ.jpg"   border="0" />](http://www.amazon.co.jp/exec/obidos/ASIN/4797327030/sorehabooks-22/)</td><td valign="top" />[増補改訂版Java言語で学ぶデザインパターン入門](http://www.amazon.co.jp/exec/obidos/ASIN/4797327030/sorehabooks-22/)<br />結城 浩<br /><iframe scrolling="no" frameborder="0" width="250" height="40" hspace="0" vspace="0" marginheight="0" marginwidth="0" src="http://xml-jp.amznxslt.com/onca/xml3?dev-t=D2JW5SAFEH7L0B&t=goodpic-22&f=http://www.g-tools.com/xsl/aws-price-ffffff.xsl&locale=jp&type=lite&AsinSearch=4797327030"></iframe><br /><br /><font size="-1"><b>おすすめ平均</b><img src="http://g-images.amazon.com/images/G/01/detail/stars-5-0.gif"   /><br /><img src="http://g-images.amazon.com/images/G/01/detail/stars-5-0.gif"   />この本なしにJavaは語れない<br /></font><br />[ /><font size="-1">Amazonで詳しく見る</font>](http://www.amazon.co.jp/exec/obidos/ASIN/4797327030/sorehabooks-22/)<img src="http://www.g-tools.com/img/spacer.gif"   width="50" height="1" />[ /><img src="http://www.g-tools.com/img/powered-by-gtool.gif"   border="0" alt="4797327030"/>](http://www.goodpic.com/mt/aws/)<br /></td></tr></table>
</div>

+ GoF以外のデザインパターンが載ってます。PLoPで扱われている中で主なものを選んであります。
<div class="rakuten"><table border="0" cellpadding="5" width="400"><tr><td valign="top">[<img src="http://images-jp.amazon.com/images/P/4797314397.09.MZZZZZZZ.jpg"   border="0" />](http://www.amazon.co.jp/exec/obidos/ASIN/4797314397/sorehabooks-22/)</td><td valign="top" />[プログラムデザインのためのパターン言語―Pattern Languages of Program Design選集](http://www.amazon.co.jp/exec/obidos/ASIN/4797314397/sorehabooks-22/)<br />PLoPD Editors, 細谷 竜一, 中山 裕子<br /><iframe scrolling="no" frameborder="0" width="250" height="40" hspace="0" vspace="0" marginheight="0" marginwidth="0" src="http://xml-jp.amznxslt.com/onca/xml3?dev-t=D2JW5SAFEH7L0B&t=goodpic-22&f=http://www.g-tools.com/xsl/aws-price-ffffff.xsl&locale=jp&type=lite&AsinSearch=4797314397"></iframe><br /><br /><font size="-1"><b>おすすめ平均</b><img src="http://g-images.amazon.com/images/G/01/detail/stars-5-0.gif"   /><br /><img src="http://g-images.amazon.com/images/G/01/detail/stars-5-0.gif"   />PLoPで提案されたパターンの粋を集めた本<br /><img src="http://g-images.amazon.com/images/G/01/detail/stars-5-0.gif"   />役に立ちそうなパターンがいくつも説明されています<br /></font><br />[ /><font size="-1">Amazonで詳しく見る</font>](http://www.amazon.co.jp/exec/obidos/ASIN/4797314397/sorehabooks-22/)<img src="http://www.g-tools.com/img/spacer.gif"   width="50" height="1" />[ /><img src="http://www.g-tools.com/img/powered-by-gtool.gif"   border="0" alt="4797314397"/>](http://www.goodpic.com/mt/aws/)<br /></td></tr></table>
</div>

+ エンタープライズで使えるパターン集のバイブル的本です。(洋書)
<div class="rakuten"><table border="0" cellpadding="5" width="400"><tr><td valign="top">[<img src="http://images-jp.amazon.com/images/P/0321127420.01.MZZZZZZZ.jpg"   border="0" />](http://www.amazon.co.jp/exec/obidos/ASIN/0321127420/sorehabooks-22/)</td><td valign="top" />[Patterns of Enterprise Application Architecture (Addison-Wesley Signature Series)](http://www.amazon.co.jp/exec/obidos/ASIN/0321127420/sorehabooks-22/)<br />Martin Fowler, David Rice, Matthew Foemmel, Edward Hieatt, Robert Mee, Randy Stafford<br /><iframe scrolling="no" frameborder="0" width="250" height="40" hspace="0" vspace="0" marginheight="0" marginwidth="0" src="http://xml-jp.amznxslt.com/onca/xml3?dev-t=D2JW5SAFEH7L0B&t=goodpic-22&f=http://www.g-tools.com/xsl/aws-price-ffffff.xsl&locale=jp&type=lite&AsinSearch=0321127420"></iframe><br /><br /><font size="-1"><b>おすすめ平均</b><img src="http://g-images.amazon.com/images/G/01/detail/stars-5-0.gif"   /><br /><img src="http://g-images.amazon.com/images/G/01/detail/stars-5-0.gif"   />アーキテクトにとって、必携の一冊<br /></font><br />[ /><font size="-1">Amazonで詳しく見る</font>](http://www.amazon.co.jp/exec/obidos/ASIN/0321127420/sorehabooks-22/)<img src="http://www.g-tools.com/img/spacer.gif"   width="50" height="1" />[ /><img src="http://www.g-tools.com/img/powered-by-gtool.gif"   border="0" alt="0321127420"/>](http://www.goodpic.com/mt/aws/)<br /></td></tr></table>
</div>







