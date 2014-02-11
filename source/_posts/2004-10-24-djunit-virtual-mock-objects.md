---
layout: post
title: "djUnitで Virtual Mock Objects ユニットテスト"
date: 2004-10-24 18:38
comments: true
categories: [Engineer-Soul]
keywords: "djUnit,Virtual Mock Object,ユニットテスト,単体テスト,カバレッジテスト,レポート"
tags: []
author: hamasyou
amazon_url: ""
amazon_author: ""
amazon_image: ""
amazon_publisher: ""
---

<p>
[ target="_blank"><img src="http://images-jp.amazon.com/images/P/4798101281.09.MZZZZZZZ.jpg"  border="0" />](http://www.amazon.co.jp/exec/obidos/ASIN/4798101281/sorehabooks-22)
</p>

「JUnitでユニットテストをするときに、クラス間の関係が問題でテストできない。」なんて事ありませんか？ <abbr title="Test Driven Developement">TDD</abbr>(テスト駆動開発)で行っている場合は、クラスの関係をそのつど考えながら進めていくので、こういった問題はあまり起きません。しかし、TDDはまだまだ敷居が高いと思う開発者が多いように思います。

[ target="_blank" class="extlink">テスト駆動開発考察](http://www.h6.dion.ne.jp/~junpei/tdd.html)

djUnit を使うと、[ target="_blank" class="uline">Virtual Mock Objects](http://www.xprogramming.com/xpmag/virtualMockObjects.htm)という手法を用いて、クラス間の関連を裏技的に変えることが出来るようになります。つまり、クラスがどんなに複雑に絡み合っていても、それらをすべて無視してテストが出来るようになるのです。さらには、『[ target="_blank" class="uline">JCoverage](http://www.jcoverage.com/)』というツールを使ったカバレッジテストの結果をエディタ上で見ることが出来るようになります。

djUnitは JUnti の機能と Aspect の機能を併せ持ったユニットテストツールとなっています。

<section>

<h4>参考</h4>

[ target="_blank" class="extlink">djUnit Wiki](http://works.dgic.co.jp/djwiki/Viewpage.do?pid=@646A556E6974)</p>

</section>


<!-- more -->

<h2>解説</h2>

<h3>JUnitの問題とは？</h3>

JUnitでユニットテストを行うときに問題となる「<strong>クラス間の関係</strong>」は、[ target="_blank" class="extlink">Mock Object(モックオブジェクト)](http://www.ingrid.org/jajakarta/cactus/doc/mockobjects.html)を使ってテストするというのが通例になっているようです。しかし、この Mock Object も、クラス設計によっては使えない場合が多いのです。

例えば、クラスの内部で他のクラスに依存している場合などは、外側(テストケース側)からMock Object を注入することが出来ません。そのため、最近話題の <abbr title="Dependency Injection">DI</abbr>(依存性注入)を使ってテストケースを組み立てることが不可能になります。

<h3>djUnitを使うとどうなるのか</h3>

djUnitは Eclipse のプラグインとして動作する、ユニットテストツールです。JUnitよりも簡単にユニットテストを行うことが出来るようになります。最新版では[ target="_blank" class="extlink">Ant](http://ant.apache.org/)のタスクとして使うことも出来るようになっています。

djUnitを使うと、Virtual Mock Objects という手法を用いて、クラス内部の処理をAspect技術を使って切り替えることが出来てしまいます。例えばテストしたいメソッドの内部で、リソースファイルに依存した処理があるとします。

<pre class="code"><code><span class="keyword">public</span> String getProperty(String key, String defaultValue) {
  ResourceBundle resource = 
        ResourceBundle.getBundle(<span class="literal">"config.properties"</span>);
  <span class="keyword">if</span> (resource == <span class="keyword">null</span>) {
    <span class="keyword">return</span> defaultValue;
  }
 
  String val = resource.getString(key);
  <span class="keyword">if</span> (val == <span class="keyword">null</span> || <span class="literal">""</span>.equals(val)) {
    <span class="keyword">return</span> defaultValue;
  } <span class="keyword">else</span> {
    <span class="keyword">return</span> val;
  }
}
</code></pre>

この例では、リソースバンドルを外部から取得しているため外部に依存してしまっています。つまり、ユニットテストがしにくいのです。本来なら、Mock Object を使って、ResourceBundle を擬似クラスで模倣してテストするのですが、それにはメソッドをリファクタリングする必要がでてきます。

Virtual Mock Objects を使うと、メソッドはそのままで、<b>ResourceBundle#getBundle の戻り値だけを Mock Object に変更することが出来ます</b>。また、<b>ResourceBundle#getString も戻り値も好きなものに変更することもできます</b>。

<section>

<h4>参考</h4>
[ target="_blank" class="extlink">Virtual Mock Objects とは？ djWiki](http://works.dgic.co.jp/djwiki/Viewpage.do?pid=@5669727475616C204D6F636B204F626A65637473E38292E4BDBFE381A3E3819FE38386E382B9E38388)

</section>

<h3>djUnitの使い方</h3>

djUnitの詳しい使い方は、『[ target="_blank" class="extlink">Virtual Mock Objects を使ったテスト](http://works.dgic.co.jp/djwiki/Viewpage.do?pid=@5669727475616C204D6F636B204F626A65637473E38292E4BDBFE381A3E3819FE38386E382B9E38388)』を参考にしてください。非常に詳しく書いてあります。一度つかったら、Virtual Mock Objects の使いやすさを実感できると思います。

djUnitのもう一つの利点としては、カバレッジテストレポートが出力できる点です。djUnit は [ target="_blank" class="extlink">Eclipse](http://www.eclipse.org/) の Plugin として提供されているので統合環境上でカバレッジテストの結果が見えるのはかなり便利です。どの処理がテストされていないかをエディター上に表示してくれる点は、テストケースの抜けを未然に防げるので重宝しそうです。

<section>

<h4>参考</h4>

[ target="_blank" class="extlink">カバレッジレポートを見る](http://works.dgic.co.jp/djwiki/Viewpage.do?pid=@E382ABE38390E383ACE38383E382B8E383ACE3839DE383BCE38388E38292E8A68BE3828B)

<h3>Virtual Mock Objects の気になるところ</h3>

Virtual Mock Objects は非常に簡単に、楽しくユニットテストが実行できる優れたツールだと思います。しかし、いくつか気になる点があります。(個人的にですが・・・)

<pre><ul><li> JUnit + Aspect の概念が分からないと、仕組みが理解しづらいかも</li><li>どんなにクラスの関連が複雑でもとりあえずテストできてしまう</li><li>リファクタリングでよい設計にしようとする気力が薄れる可能性がある</li><li>テストケースが失敗したのが、Aspect が問題なのか処理が問題なのか切り分けがつかない場合があるかもしれない</li><li>JCoverageの結果は参考程度に受け取るべし</li></ul></pre>

テストしづらいプログラムでも何とかなってしまうというのが Virtual Mock Objects の気になるところではありますが、<b>カバレッジツールを使うだけでも djUnit を使う価値はある</b>かと思います(他に良いカバレッジツールがあればdjUnitは不要なのか？？？・・・)。通常はJUnitの代わりとして使い、どうしてもJUnitではテストが難しい部分や、外部に依存してしまっている部分は Virtual Mock Objects を使うというのがいいのかもしれません。どちらにしても、かなり面白いテストツールだと思います。

<h2>参考</h2>

+ djUnitを作っているところです。Wiki に非常に詳しく使い方が載っています。
[ target="_blank" class="extlink">djUnit Wiki](http://works.dgic.co.jp/djwiki/Viewpage.do?pid=@646A556E6974)

+ 上記と同じサイト内の、Virtual Mock Objects に関するページへのリンクです。
[ target="_blank" class="extlink">Virtual Mock Objects を使ったテスト(in djUnit Wiki)](http://works.dgic.co.jp/djwiki/Viewpage.do?pid=@5669727475616C204D6F636B204F626A65637473E38292E4BDBFE381A3E3819FE38386E382B9E38388)

+ エクリプスに関する日本語の Wiki サイトです。djUnitの概要が分かりやすく載っています。
[ target="_blank" class="extlink">エクリプス Wiki](http://eclipsewiki.net/eclipse/?djUnit)

+ Virtual Mock Objects に関するドキュメントです。英文ですが興味深い内容です。
[ target="_blank" class="extlink">Virtual Mock Objects using AspectJ with JUNIT](http://www.xprogramming.com/xpmag/virtualMockObjects.htm)

+ エクリプス本家へのリンクです。djUnitは Eclipse のプラグインとして動作します。
[ target="_blank" class="extlink">Eclipse.org](http://www.eclipse.org/)

+ JUnitの使い方がたっぷり載っている書籍です。
<div class="rakuten"><table border="0" cellpadding="5" width="400"><tr><td colspan="2">[JUnitイン・アクション](http://www.amazon.co.jp/exec/obidos/ASIN/4797325143/sorehabooks-22/)</td></tr><tr><td valign="top">[<img src="http://images-jp.amazon.com/images/P/4797325143.09.MZZZZZZZ.jpg"   border="0" />](http://www.amazon.co.jp/exec/obidos/ASIN/4797325143/sorehabooks-22/)</td><td valign="top"><font size="-1">ビンセント マソル　テッド ハスティード　Vincent Massol　Ted Husted　クイープ<br /><br /><iframe scrolling="no" frameborder="0" width="200" height="40" hspace="0" vspace="0" marginheight="0" marginwidth="0" src="http://webservices.amazon.co.jp/onca/xml?Service=AWSProductData&SubscriptionId=0G91FPYVW6ZGWBH4Y9G2&AssociateTag=goodpic-22&Operation=ItemLookup&IdType=ASIN&ContentType=text/html&Page=1&ResponseGroup=Offers&ItemId=4797325143&Version=2004-10-04&Style=http://www.g-tools.net/xsl/priceFFFFFF.xsl"></iframe><br />[Amazonで詳しく見る](http://www.amazon.co.jp/exec/obidos/ASIN/4797325143/sorehabooks-22/)</font><img src="http://www.goodpic.com/mt/images/spacer.gif"   width="30" height="1" /><font size="-2">by [G-Tools](http://www.goodpic.com/mt/aws/)</font><br /></td></tr></table></div>




