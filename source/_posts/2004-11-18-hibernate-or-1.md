---
layout: post
title: "Hibernate で O/Rマッピング してみる ： 基本編"
date: 2004-11-18 14:01
comments: true
categories: [Engineer-Soul]
keywords: "Hibernate,O/Rマッピング,フレームワーク,データベース,継承関係,HQL"
tags: []
author: hamasyou
amazon_url: ""
amazon_author: ""
amazon_image: ""
amazon_publisher: ""
---

<p>
[ target="_blank"><img src="http://images-jp.amazon.com/images/P/193239415X.01.MZZZZZZZ.jpg"   border="0" />](http://www.amazon.co.jp/exec/obidos/ASIN/193239415X/sorehabooks-22)
</p>

Hibernate は O/R マッピングツールと呼ばれる、リレーショナルデータベースとオブジェクトモデルとの間を埋めるフレームワークです。

関連するテーブルのオブジェクトを管理する方法が、十数種類あります。[ target="_blank" class="extlink">Index of Relationships](http://www.xylax.net/hibernate/)< のサイトに、Hibernate でサポートされる関係の一覧が載っています。

ドキュメントも豊富にそろっていて、[ target="_blank" class="extlink">リファレンスマニュアル](http://www.hibernate.org/hib_docs/reference/ja/html/) (日本語翻訳済み) が非常に参考になります。マニュアルの中から、気になった部分や使えそうな部分だけを抜き出しておきます。

<section>

<h4>[参考]</h4>

+ [ target="_blank" class="extlink">Hibernate リファレンスマニュアル](http://www.hibernate.org/hib_docs/reference/ja/html/)

+ [ target="_blank" class="extlink">Hibernate Pad](http://wiki.bmedianode.com/Hibernate/)

</section>


<!-- more -->

<h2>Hibernate の基本的なこと</h2>

<ol>
<li>[Hibernate で表現できる関係](#Hibernate で表現できる関係)</li>
<li>[HQL 文を書くときの注意点](#HQL 文を書くときの注意点)</li>
<li>[マッピングファイルについてあれこれ](#マッピングファイルについてあれこれ)</li>
<li>[SpringFramework との連携時、テストでLazyInitializationExceptionが出る場合の対処](#Spring との連携時、テストでLazyInitializationExceptionが出る場合の対処)</li>
<li>[org.hibernate.HibernateException: CGLIB Enhancement failed: &lt;className&gt;](#org.hibernate.HibernateException: CGLIB Enhancement failed: &lt;className&gt;)</li>
</ol>

<h2 id="Hibernate で表現できる関係">Hibernate で表現できる関係</h2>

Hibernate はリレーショナルデータベースのほとんどの関係を表現することが出来ます。その一部が下記のような関連です。

<dl><dt>1. One-to-one (一対一)</dt>
<dd>2つのオブジェクトが同一のIDを共有する場合。親子関係がこれにあたります。</dd>
<dt>2. One-to-many (一対多)</dt>
<dd>オブジェクトのプロパティがコレクションの場合。Hibernate は、コレクションに Set, List, Bag, 独自のコレクション型が使用できます。<q>
Bag は Set と似ていて、等価なものを一意に扱います。ただし、同じオブジェクトを2回以上登録できます。同じオブジェクトが何回登録されたかを管理できます。</q>
</dd>
<dt>3. Many-to-many (多対多)</dt>
<dd>双方向の関係で、多対一の関係の場合。両者のオブジェクトのプロパティに、相手のクラスのコレクションが保持されている状態。</dd>
<dt>4. Inheritance (継承)</dt>
<dd>2つのオブジェクトに is-a の関係がある場合。テーブル的には、同一のテーブルに格納される場合。もしくは、同一の主キーをもつテーブルが個々のクラスに対して存在する。</dd>
<dt>5. Component (コンポーネント)</dt>
<dd>複数のクラスを一つのテーブルにマッピングする方法の一つ。</dd>
</dl>

<section>

<h4>[参考]</h4>

+ [ target="_blank" class="extlink">Index of Relationships](http://www.xylax.net/hibernate/)

</section>

<h2 id="HQL 文を書くときの注意点">HQL 文を書くときの注意点</h2>

HQL の基本的な文法は[ target="_blank">リファレンスマニュアル](http://www.hibernate.org/hib_docs/reference/ja/html/queryhql.html) を参考にしてください。

(基本事項抜粋)

<h3>HQLはクエリの大文字・小文字を区別しない</h3>

ただし、HQL中のオブジェクト参照(プロパティ)は大文字・小文字の区別をします。

<h3>from 句に書くのは、クラス名</h3>

大文字・小文字の区別が必要です。パッケージを含めた完全クラス名を指定することもできます。マッピングファイルで auto-import=&quot;false&quot; を指定した場合は、完全クラス名を指定する必要があります。

<h3>HQL中でクラスのプロパティを指定するときは、クラス名に別名を付ける必要がある</h3>

クラスの別名は、 <i>from Worker as w</i> のように <b>as</b> を使って指定します。HQL は、結構複雑なものまで書けるので、それなりに使えるかも。

<h3>バインド変数には、名前を付ける</h3>

HQL 文には、バインド変数を使うことが出来ます。 PreparedStatement でおなじみの &quot;?&quot; と、<b>:id</b> のような、任意の名前を与える方法が使えます。&quot;?&quot; を使った場合には、インデックス順にバインド値を設定しなければなりません。名前を与えた場合には、Query オブジェクトを使って、任意の順番でバインドできます。

<pre class="code"><code>Query q = s.createQuery(<span class="literal">"from Worker as w where w.id = :id1 or w.id = :id2"</span>); 
q.setParameter(<span class="literal">"id1"</span>, <span class="keyword">new</span> Integer(1), Hibernate.INTEGER); 
q.setParameter(<span class="literal">"id2"</span>, <span class="keyword">new</span> Integer(2), Hibernate.INTEGER); 
</code></pre>

<h3>クエリの本体をマッピング文書内に定義する</h3>

クエリ文字列をJavaコードの外に定義することができます。<b>CDATAセクション</b>を使って定義します。

<section>

<h4>Worker.hbm.xml</h4>

<pre class="code"><code><span class="tag">&lt;hibernate-mapping&gt;</span> 
<span class="tag">&lt;query <span class="attr">name=</span><span class="value">&quot;com.hamasyou.hibernate.query.methodName&quot;</span>&gt;</span> 
  <span class="tag">&lt;![CDATA[ 
    from com.hamasyou.hibernate.entity.Worker as worker 
    where worker.age &lt;= :age 
  ]]&gt;</span> 
<span class="tag">&lt;/query&gt;</span> 
 
<span class="tag">&lt;/hibernate-mapping&gt;</span>
</code></pre>

</section>

呼び出すには、プログラムから、クエリ名を指定すればよいです。

<pre class="code"><code>Query q = session.getNamedQuery(<span class="literal">"com.hamasyou.hibernate.query.methodName"</span>); 
q.setInteger(<span class="literal">"age"</span>, <span class="keyword">new</span> Integer(22)); 
</code></pre>

<h2 id="マッピングファイルについてあれこれ">マッピングファイルについてあれこれ</h2>

<h3>マッピングファイルの粒度</h3>

<ul type="square">
<li>マッピングファイルは、クラス毎に作成する</li>
<li>マッピング情報は XDoclet で書く</li>
<li>XDoclet で書くことのメリットは、クラスとマッピングの対応が分かりやすいこと</li>
<li>デメリットは、マッピングファイルに手を入れたときに、上書きされてしまう可能性があること</li>
</ul>

マッピング情報は、XDoclet の形式で、ソースコード中に埋め込むのが良いような気がします。確かに、POJO のクラスに Hibernate の情報が入り込むのは、別のフレームワークを使おうとした場合に不利ではありますが、たぶんほとんどの場合そういうことはないはずです。XDoclet で定義すると、クラスとマッピングが同じ場所に存在することになるので、対応が分かりやすいというメリットがあります。

とはいえ、やっぱり XDoclet の形式で書くよりも XML で記述したほうが 保守はしやすいか・・・。

<section>

<h4>[参考]</h4>

+ [ target="_blank" class="extlink">HIbernate XDoclet Attribute 一覧](http://xdoclet.sourceforge.net/xdoclet/tags/hibernate-tags.html)

</section>

XDoclet 形式でマッピングを書いた場合問題となるのが、生成されたマッピングファイルを手で修正していた場合に、もう一回ファイルを生成すると上書きされてしまうことです。マージファイルを使って、上手く上書きされることがないようにする必要があります。

<h3>マージファイルを使う方法</h3>

Ant の Hibernate XDoclet の mergedir に設定したフォルダの下に、パッケージを作り、<b>hibernate-properties-{クラス名}.xml</b> というファイルを置いておきます。中身がそのままマージファイルとして所定の位置に埋め込まれます。XDoclet として書けない静的なプロパティなどを、このファイルに書いておきます。

ちなみに、[ target="_blank">JBoss-IDE](http://www.devx.com/opensource/Article/20242) という Eclipse のプラグインを使うと XDoclet の補完が効くようなって便利です。

<h3>Hibernate 用の XDoclet 解析 Ant スクリプト例</h3>

<pre class="code"><code><span class="rem">&lt;!-- パスの設定 --&gt;</span> 
<span class="tag">&lt;path <span class="attr">id=</span><span class="value">&quot;id.xdoclet.classpath&quot;</span>&gt;</span> 
  <span class="tag">&lt;fileset <span class="attr">dir=</span><span class="value">&quot;C:\\java\\XDoclet\\xdoclet-1.2.2&quot;</span>  
      <span class="attr">includes=</span><span class="value">&quot;*.jar&quot;</span>/&gt;</span> 
<span class="tag">&lt;/path&gt;</span>   
       
<span class="rem">&lt;!-- Hibernate XDoclet の解析 --&gt;</span> 
<span class="tag">&lt;target <span class="attr">name=</span><span class="value">&quot;xdoclet&quot;</span>&gt;</span> 
  <span class="tag">&lt;taskdef <span class="attr">name=</span><span class="value">&quot;hibernatedoclet&quot;</span> 
      <span class="attr">classname=</span><span class="value">&quot;xdoclet.modules.hibernate.HibernateDocletTask&quot;</span> 
      <span class="attr">classpathref=</span><span class="value">&quot;id.xdoclet.classpath&quot;</span>/&gt;</span> 
    
    <span class="tag">&lt;hibernatedoclet <span class="attr">destdir=</span><span class="value">&quot;${src.dir}&quot;</span> 
        <span class="attr">excludedtags=</span><span class="value">&quot;@version,@author,@todo&quot;</span> 
        <span class="attr">force=</span><span class="value">&quot;false&quot;</span> 
        <span class="attr">mergedir=</span><span class="value">&quot;.&quot;</span> 
        <span class="attr">verbose=</span><span class="value">&quot;false&quot;</span>&gt;</span> 
        <span class="tag">&lt;fileset <span class="attr">dir=</span><span class="value">&quot;${src.dir}&quot;</span> <span class="attr">includes=</span><span class="value">&quot;**/*.java&quot;</span>/&gt;</span> 
        <span class="tag">&lt;hibernate <span class="attr">version=</span><span class="value">&quot;2.0&quot;</span>/&gt;</span> 
    <span class="tag">&lt;/hibernatedoclet&gt;</span> 
<span class="tag">&lt;/target&gt;</span>
</code></pre>

<h3>XDoclet で書くときの注意点</h3>

<b>@hibernate.id</b> タグには、主キー項目を指定します。このとき、type 属性を付けると、composite-id だと解釈されるのか、エラーがでます。type 属性は Hibernate が適切なマッピングを自動で探してくれるので、ほとんどの場合指定しなくていいようです。ちなみに、指定する場合は、 HIbernate 型で指定します。

<h3>主キーの項目の型</h3>

主キー項目の型は、null が代入できる型にしておいた方がいいみたいです。つまり、オブジェクト型にしておくといいです。Hibernate は永続化されたかどうかを判断するのに、主キー項目の値が null かどうかで判定します (デフォルトの動作)。永続化されたかどうかの判断に別の値を指定したい場合には、 @hibernate.id  unsaved-value を設定します。

<h3>ビジネスキーの同値性ってどいういこと！？</h3>

Hibernate は、テーブルの主キーとオブジェクトIDとを別々に考えるようです。

{% blockquote [ target="_blank" class="extlink">Hibernate 入門記 - 永続クラス](http://d.hatena.ne.jp/koichik/20040521#1085133463) %}
なんですが，単純に ID プロパティの同値性を判断してもいけないみたい．というのもですね，永続クラスの新しいインスタンスが生成された時点では，ID プロパティの値は設定されないらしいんですね．あう，だから ID プロパティは null にできる型を推奨なのでしょうか？ そんな気がする．

そんなわけで，結局のところは 「ビジネスキー」 の同値性を使って判定しろとのことです．．． つまり Hibernate を使う際には，主キーは Hibernate 用に INTEGER とかで無意味な ID にしておいて，業務的なキーは主キーにするなってことですか？ まぁ，主キーは候補キーの一つに過ぎないというのも事実な訳ですが．．． DB屋さんに反発食らわないかなぁ？(^^;


{% endblockquote %}

<h3>主キーと候補キー</h3>

候補キーというのは、エンティティを一意に識別する属性の、最小単位の集合のことです。社会保障番号や、DNA なんかは、人を特定する候補キーになります。

主キーは、候補キーから選ばれたデータベース上のレコードを一意に識別するキーのことです。つまり、候補キーから選ばれます。単に、シーケンシャルな値でもかまいません。要は NULL を含まず、レコードが一意に識別できれさえすればいいのです。

つまり、主キーとは、RDB が便宜的に使うキーなのです。Hibernate では、オブジェクトID は、RDB に格納された段階で決まります。つまり、オブジェクトID は主キーなのです。

<h3>HQL を使って外部結合</h3>

HQL を使って検索する場合、find() メソッドを使って外部結合を行うには明示的にHQL文を書く必要があるみたいです。詳しくは、「[ target="_blank" class="extlink">Hibernate 入門記 - HSQLDB の外部結合](http://d.hatena.ne.jp/koichik/20040828#1093699339)」 とか 「[ target="_blank"  class="extlink" />Hibernate 入門記 - 外部結合まとめ](http://d.hatena.ne.jp/koichik/20040920#1095687899)」 とか 「[ target="_blank" class="extlink" />Hibernate 入門記 - HQL と outer-join 属性](http://d.hatena.ne.jp/koichik/20041016#1097911005)」 を参考に。

<h2 id="Spring との連携時、テストでLazyInitializationExceptionが出る場合の対処">SpringFramework との連携時、テストでLazyInitializationExceptionが出る場合の対処</h2>

SpringFramework と Hibernate を連携させてコーディングを行う場合、SessionFactory を SpringFramework から提供してもらうようにします。これは、トランザクション管理を、コンテナに任せようという意図で、SpringFramework + Hibernate では一般的に行われる処理です。

SpringFramework + Hibernate で Web アプリケーションを作る場合、「<b>Open Session In View</b>」という手法を使って、Session オブジェクトのオープンとクローズを、View で行うという方法をとることがあります。これは、Hibernate で Lazy load（遅延ロード）を行う場合に、View で遅延ロードが行われる場合があり、Session が閉じられてしまって、<b>LazyInitializationException</b> がでてしまうことがあるからです。

Web で使う場合はいいのですが、単体テストなどで、SpringFramework + Hibernate を使いたい場合、Session のオープンとクローズを View で行うことができません（Open Session In View を使うと、Webに依存したテストになってしまうから）。そこで、TransactionSynchronizationManager というクラスを使って、テスト時に、Session 管理を行う方法が、「[ target="_blank" class="extlink">Open Session in View and testing](http://forum.hibernate.org/viewtopic.php?t=929167)」 に載っています。これで、テスト時にも、SpringFramework + Hibernate でテストできるようになります。

<h2 id="org.hibernate.HibernateException: CGLIB Enhancement failed: &lt;className&gt;">org.hibernate.HibernateException: CGLIB Enhancement failed: &lt;className&gt;</h2>

classNameで定義されているクラスのコンストラクタが <strong>private</strong> になっているのが原因かもしれません。クラスをインスタンス化できない場合にスローされます。

対処方法は、例外の出ているクラスのコンストラクタを <strong>パッケージプライベート</strong>、<strong>protected</strong>、<strong>public</strong>のいずれかに変更します。

<h2>参考</h2>

+ Hibernate のリファレンスドキュメント (日本語)
[ target="_blank" class="extlink">Hibernate Reference Document](http://www.hibernate.org/hib_docs/reference/ja/html/index.html)

+ Hibernate だけでなく、データベース設計にまで触れられていておすすめ。
<div class="rakuten"><table width="400" border="0" cellpadding="5"><tr><td colspan="2">[Hibernate in Action (In Action)](http://www.amazon.co.jp/exec/obidos/ASIN/193239415X/sorehabooks-22/)</td></tr><tr><td valign="top">[<img src="http://images-jp.amazon.com/images/P/193239415X.09.MZZZZZZZ.jpg"   border="0" />](http://www.amazon.co.jp/exec/obidos/ASIN/193239415X/sorehabooks-22/)</td><td valign="top"><font size="-1">Christian BauerGavin King<br /><br /><iframe scrolling="no" frameborder="0" width="200" height="40" hspace="0" vspace="0" marginheight="0" marginwidth="0" src="http://webservices.amazon.co.jp/onca/xml?Service=AWSECommerceService&SubscriptionId=0G91FPYVW6ZGWBH4Y9G2&AssociateTag=goodpic-22&Operation=ItemLookup&IdType=ASIN&ContentType=text/html&Page=1&ResponseGroup=Offers&ItemId=193239415X&Version=2004-10-04&Style=http://www.g-tools.net/xsl/priceFFFFFF.xsl"></iframe><br /><b>おすすめ平均</b><img src="http://g-images.amazon.com/images/G/01/detail/stars-5-0.gif"   /><br /><img src="http://g-images.amazon.com/images/G/01/detail/stars-5-0.gif"   />おもしろくてためになる<br /><br />[Amazonで詳しく見る](http://www.amazon.co.jp/exec/obidos/ASIN/193239415X/sorehabooks-22/)</font><img src="http://www.goodpic.com/mt/images/spacer.gif"   width="30" height="1" /><font size="-2">by [G-Tools](http://www.goodpic.com/mt/aws/)</font><br /></td></tr></table></div>

+ 薄いながらも十分な情報量。HibernateとSpringにも触れられています。
<div class="rakuten"><table width="400" border="0" cellpadding="5"><tr><td colspan="2">[軽快なJava―Better,Faster,Lighter Java](http://www.amazon.co.jp/exec/obidos/ASIN/487311201X/sorehabooks-22/)</td></tr><tr><td valign="top">[<img src="http://images-jp.amazon.com/images/P/487311201X.09.MZZZZZZZ.jpg"   border="0" />](http://www.amazon.co.jp/exec/obidos/ASIN/487311201X/sorehabooks-22/)</td><td valign="top"><font size="-1">ブルース・A. テイトジャスティン ゲットランドBruce A. TateJustin Gehtland岩谷 宏<br /><br /><iframe scrolling="no" frameborder="0" width="200" height="40" hspace="0" vspace="0" marginheight="0" marginwidth="0" src="http://webservices.amazon.co.jp/onca/xml?Service=AWSECommerceService&SubscriptionId=0G91FPYVW6ZGWBH4Y9G2&AssociateTag=goodpic-22&Operation=ItemLookup&IdType=ASIN&ContentType=text/html&Page=1&ResponseGroup=Offers&ItemId=487311201X&Version=2004-10-04&Style=http://www.g-tools.net/xsl/priceFFFFFF.xsl"></iframe><br /><b>おすすめ平均</b><img src="http://g-images.amazon.com/images/G/01/detail/stars-5-0.gif"   /><br /><img src="http://g-images.amazon.com/images/G/01/detail/stars-5-0.gif"   />率直な筆者の経験は必読<br /><img src="http://g-images.amazon.com/images/G/01/detail/stars-5-0.gif"   />シンプル<br /><br />[Amazonで詳しく見る](http://www.amazon.co.jp/exec/obidos/ASIN/487311201X/sorehabooks-22/)</font><img src="http://www.goodpic.com/mt/images/spacer.gif"   width="30" height="1" /><font size="-2">by [G-Tools](http://www.goodpic.com/mt/aws/)</font><br /></td></tr></table></div>

+ Hibernate の基本的な使い方が載っています。
<div class="rakuten"><table width="400" border="0" cellpadding="5"><tr><td colspan="2">[Web+DB press (Vol.18)](http://www.amazon.co.jp/exec/obidos/ASIN/4774119016/sorehabooks-22/)</td></tr><tr><td valign="top">[<img src="http://images-jp.amazon.com/images/P/4774119016.09.MZZZZZZZ.jpg"   border="0" />](http://www.amazon.co.jp/exec/obidos/ASIN/4774119016/sorehabooks-22/)</td><td valign="top"><font size="-1">WEB+DB Press編集部<br /><br /><iframe scrolling="no" frameborder="0" width="200" height="40" hspace="0" vspace="0" marginheight="0" marginwidth="0" src="http://webservices.amazon.co.jp/onca/xml?Service=AWSECommerceService&SubscriptionId=0G91FPYVW6ZGWBH4Y9G2&AssociateTag=goodpic-22&Operation=ItemLookup&IdType=ASIN&ContentType=text/html&Page=1&ResponseGroup=Offers&ItemId=4774119016&Version=2004-10-04&Style=http://www.g-tools.net/xsl/priceFFFFFF.xsl"></iframe><br />[Amazonで詳しく見る](http://www.amazon.co.jp/exec/obidos/ASIN/4774119016/sorehabooks-22/)</font><img src="http://www.goodpic.com/mt/images/spacer.gif"   width="30" height="1" /><font size="-2">by [G-Tools](http://www.goodpic.com/mt/aws/)</font><br /></td></tr></table></div>

+ 開発者のための Hibernate の解説書が日本語で登場しました。
<div class="rakuten"><table width=400 border="0" cellpadding="5"><tr><td colspan="2">[ target="_blank">Hibernate](http://www.amazon.co.jp/exec/obidos/ASIN/487311215X/sorehabooks-22/)</td></tr><tr><td valign="top">[ target="_blank"><img src="http://images-jp.amazon.com/images/P/487311215X.09.MZZZZZZZ.jpg"   border="0" />](http://www.amazon.co.jp/exec/obidos/ASIN/487311215X/sorehabooks-22/)</td><td valign="top"><font size="-1">James Elliott<br /><br /><iframe scrolling="no" frameborder="0" width="200" height="40" hspace="0" vspace="0" marginheight="0" marginwidth="0" src="http://webservices.amazon.co.jp/onca/xml?Service=AWSECommerceService&SubscriptionId=0G91FPYVW6ZGWBH4Y9G2&AssociateTag=goodpic-22&Operation=ItemLookup&IdType=ASIN&ContentType=text/html&Page=1&ResponseGroup=Offers&ItemId=487311215X&Version=2004-10-04&Style=http://www.g-tools.net/xsl/priceFFFFFF.xsl"></iframe><br /><b>おすすめ平均</b><img src="http://g-images.amazon.com/images/G/01/detail/stars-4-0.gif"   /><br /><img src="http://g-images.amazon.com/images/G/01/detail/stars-4-0.gif"   />コンパクトにまとまっている良書<br /><br />[ target="_blank">Amazonで詳しく見る](http://www.amazon.co.jp/exec/obidos/ASIN/487311215X/sorehabooks-22/)</font><font size="-2">by [G-Tools](http://www.goodpic.com/mt/aws/)</font><br /></td></tr></table></div>







