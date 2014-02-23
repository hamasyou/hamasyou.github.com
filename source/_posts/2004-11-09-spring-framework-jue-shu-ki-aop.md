---
layout: post
title: "Spring Framework 覚書き - AOP"
date: 2004-11-09 10:14
comments: true
categories: [Blog]
keywords: "Spring,Framework,覚書き,スプリング,フレームワーク,アーキテクチャ,DI,IoC,AOP,アスペクト,横断的関心,Pointcut,Advice,インターセプト"
tags: [Spring]
author: hamasyou
amazon_url: ""
amazon_author: ""
amazon_image: ""
amazon_publisher: ""
---

<p>
<a href="http://www.amazon.co.jp/exec/obidos/ASIN/0764558315/sorehabooks-22" rel="external nofollow"></a>
</p>

アメリカではほとんどデフェクトスタンダードとなっている「<a href="http://www.springframework.org/" rel="external nofollow"></a>」の覚書きです。Spring は簡単に言うと、<abbr title="Inversion of Control">IoC (制御の反転)</abbr>、またの名を <abbr title="Dependency Injection">DI (依存性注入)</abbr> という仕組みを取り入れた軽量コンテナです。

<abbr title="Aspect Oriented Programming">AOP</abbr>(アスペクト指向プログラミング) はクラスの直接的な責務ではない、各モジュールから共通で使われる処理を、独立して切り出す手法です。「クラスの直接的な責務でない」とは、例えば「ログ」や「トランザクション」、「認証」などです。多くのクラスに重複コードが生まれてしまうような処理は、<b>アスペクト(横断的関心事)</b> として別のモジュールにしてしまうという手法をとることが出来ます。Spring AOP は、このアスペクトを扱うものです。
参考：<a href="http://www.amazon.co.jp/exec/obidos/ASIN/4797326387/sorehabooks-22" rel="external nofollow">『AspectJによるアスペクト指向プログラミング入門』 ソフトバンクパブリッシング</a>
　
Springの詳細については、ほかにもっとよいサイト(<a href="http://www.andore.com/money/trans/spring_ref_ja.html" rel="external nofollow">Springフレームワークの紹介</a>)があるので、そちらを参考にしてください。ここでは、Springを使っていて、ハマった点や気になった点などをメモしていこうと思います。随時更新していくつもりです。間違っている可能性が高いので、気になる点があればコメントをお願いします。

<section>

<h4>参考</h4>

<a href="http://wiki.bmedianode.com/Spring/?FrontPage" rel="external nofollow">Spring Pad - Wiki</a>

<a href="http://direct.idg.co.jp/detail_1.msp?id=1066&class=10005&n=2" rel="external nofollow">JavaWorld 7月号 2004年</a>

</section>


<!-- more -->

<h2>Spring AOP フレームワーク</h2>

<ul><li><a href="#Spring AOP 用語" rel="external nofollow">Spring AOP 用語</a></li>
<li><a href="#Spring AOP とは" rel="external nofollow">Spring AOP とは</a></li>
<li><a href="#Spring AOP で使われる主なクラス / インターフェース" rel="external nofollow">Spring AOP で使われる主なクラス / インターフェース</a></li>
<li><a href="#ProxyFactoryBean を使ったサンプルコード" rel="external nofollow">ProxyFactoryBean を使ったサンプルコード</a></li>
<li><a href="#Advisor とは？" rel="external nofollow">Advisor とは？</a></li>
<li><a href="#ハマった点" rel="external nofollow">ハマった点</a></li>
</ul>

<h2 id="Spring AOP 用語">Spring AOP 用語</h2>

Spring AOP で出てくる主要な用語まとめです。

<dl><dt><a name="アスペクト"></a>アスペクト (Aspect)</dt>
<dd>共通の処理としてモジュールから呼び出されるもの。</dd>
<dt><a name="インターセプト"></a>インターセプト (Intercept)</dt>
<dd>メソッドの呼び出しタイミングで、振る舞いを挿入すること。</dd>
<dt><a name="ジョインポイント"></a>ジョインポイント (Joinpoint)</dt>
<dd>アスペクトコードが挿入できる位置を表したもの。例えば、メソッド呼び出しの前後などは、ジョインポイントになる。</dd>
<dt><a name="アドバイス"></a>アドバイス (Advice)</dt>
<dd>ジョインポイントに埋め込まれるアスペクトコードのこと。4つの種類がある。
<dl><dt>Around Advice</dt>
<dd>ジョインポイントの前後で実行される。</dd>
<dt>Before Advice</dt>
<dd>ジョインポイントの前に実行される。</dd>
<dt>After Advice</dt>
<dd>ジョインポイントの後に実行される。2つの種類がある。
<dl><dt>After returning Advice</dt>
<dd>ジョインポイントでの処理が正常に終了した後に実行される。</dd>
<dt>Throws Advice</dt>
<dd>ジョインポイントでの処理で例外が発生した場合に実行される。</dd>
</dl>
</dd>
</dl>
</dd>
<dt><a name="ポイントカット"></a>ポイントカット (Pointcut)</dt>
<dd>ジョインポイントの集合のこと。条件にあうジョインポイントだけを抜き出すことも可能。</dd>
<dt<a name="イントロダクション"></a>イントロダクション (Introduction)</dt>
<dd>アドバイスとしてメソッドやフィールドを挿入すること。ソースコード上で実装していないインターフェースなどを実行時に付け加えることが出来るので、デバッグは難しくなる。</dd>
<dt><a name="ウィーブ"></a>ウィーブ (Weave)</dt>
<dd><p>アスペクトをジョインポイントやポイントカットに織り込むこと。</p>

<section>

<h4>[静的ウィーブと動的ウィーブ]</h4>

 静的ウィーブとは、コンパイル時にアスペクトを織り込むこと。静的ウィーブを提供している実行環境には、「AspectJ」などがある。逆に、動的時にウィーブするには、プロキシを使って実行コードを隠蔽してやる必要があります。

</section>
</dd>
</dl>

アスペクト用語を図にするとこんな感じでしょうか？

<img src="http://hamasyou.com/images/engineer_soul/aspect_image.gif" alt="アスペクト指向イメージ図" />

<h2 id="Spring AOP とは">Spring AOP とは</h2>

Spring AOP は<a href="#インターセプト" rel="external nofollow">イントロダクション</a>は使えます。

Spring AOP は、<a href="#ジョインポイント" rel="external nofollow">アスペクト</a>を織り込む方法として、インターセプトを使います。処理の流れはこんな感じです。

<ol><li>Spring は実行時に、アスペクト対象クラスのプロキシオブジェクトを生成します。</li><li>そして、プロキシオブジェクトの内部で、アスペクト対象クラスの処理を実行します。</li><li>ジョインポイントとして、アスペクト対象クラスが指定されると、プロキシの中でインターセプトクラスのアドバイスメソッドが呼ばれます。</li></ol>

Spring AOP では、<a href="#ポイントカット" rel="external nofollow">アドバイス</a>を一緒に扱う「<b>アドバイザー (Advisor)</b>」というものを使います。任意のポイントカットとアドバイスを組み合わせて使います。

<h2 id="Spring AOP で使われる主なクラス / インターフェース">Spring AOP で使われる主なクラス / インターフェース</h2>

Spring AOP を利用する場合に使われる主なクラスやインターフェースです。

<h3>org.springframework.aop.framework.ProxyFactoryBean</h3>

AOP Proxy 戦略を行うための FactoryBean です。簡単に言うと、Spring で AOP を行うときに使う、FactoryBean です。

<section>

<h4>【設定する項目】</h4>

<dl><dt>interceptorNames</dt>
<dd>インターセプトするクラスを設定します。<i>org.aopalliance.intercept.MethodInterceptor</i> を実装したクラス参照の ID を指定します。リスト値を取ることが出来るので、クラス名の並び順を考える必要があります。(指定した順に適用される)</dd>
<dt>target</dt>
<dd>アスペクト対象の Bean 参照(ref bean)を指定します。</dd>
<dt>proxyInterfaces</dt>
<dd>プロキシに使用するクラス名の配列を指定します。特に指定のない場合は、書かなくても問題ありません。</dd>
</dl>

</section>

<h3>org.springframework.aop.Pointcut</h3>

<a href="#ポイントカット" rel="external nofollow">ポイントカット</a>はアドバイスの挿入地点であるジョインポイントの部分集合です。クラス名やメソッド名のマッチング条件を指定することにより、ジョインポイントの集合を表すことが出来ます。

<h3>org.springframework.aop.MethodMatcher</h3>

<a href="#ポイントカット" rel="external nofollow">ポイントカット</a>の一部です。呼び出されたメソッドがポイントカットの集合に含まれるかどうかを判定します。静的な呼び出しである場合には、引数が2つの matches メソッドが呼び出されます。動的な呼び出しである場合には、引数が3つの matches メソッドが呼び出されます。

Spring AOP では、ポイントカットは Union (和集合) と Intersection (交差) で表せます。 org.springframework.aop.support.Pointcuts クラスを使います。

<section>

<h4>【主な MethodMatcher】</h4>

<dl><dt>TrueMethodMatcher</dt>
<dd>すべてのメソッドにマッチするマッチャーです。</dd>
<dt>NameMatchMethodPointcut</dt>
<dd>メソッド名を正規表現を使って表せるマッチャーです。</dd>
<dt>RegexpMethodPointcutAdvisor</dt>
<dd>正規表現を使ってポイントカットを表せるアドバイザーです。</dd>
</dl>

</section>

<h3>org.aopalliance.aop.Advice</h3>

<a href="#アドバイス" rel="external nofollow">アドバイス</a>はポイントカットに挿入する処理を定義するインターフェースです。Spring では「Around Advice」、「Before Advice」、「Throws Advice」、「After returning Advice」の 4 種類をサポートしています。

<dl><dt>org.aopalliance.intercept.MethodInterceptor</dt>
<dd><p>Around Advice を実装するためのインターフェースです。Around Advice は アドバイスの処理が、アスペクト対象の処理の前後に及ぶため、自分で Advice のタイミングを実装しなければなりません。Advice の処理の中で、 invocation.proceed() を呼び出す必要があります。このインターフェースの実装の仕方によっては、4種類全部の Advice を表現できます。</p></dd>
<dt>org.springframework.aop.MethodBeforeAdvice</dt>
<dd>Before Advice を実装するためのインターフェースです。before() メソッド中にアドバイスコードを実装します。</dd>
<dt>org.springframework.aop.ThrowsAdvice</dt>
<dd>
<p>Throws Advice を実装するためのインターフェースです。インターフェースで定義されたメソッドはありません。</p>

<pre>
public void afterThrowing(java.lang.refrect.Method m, Object[] args, Object target, Throwable subclass)
</pre>

<p>の形式でメソッドを定義しておくと、SpringFramework によって呼ばれます。</p>

<p>第4引数の Throwable は、キャッチしたい例外クラスを指定することで、例外にマッチするときだけ呼ばれるようになります。</p>
</dd>
<dt>org.springframework.aop.AfterReturningAdvice</dt>
<dd>After returning Advice を実装するためのインターフェースです。処理が正常に終了した場合に呼び出されます。</dd>
</dl>

<h3>org.springframework.aop.Advisor</h3>

<a href="#ポイントカット" rel="external nofollow">アドバイス</a>を一緒に扱うインターフェースです。Spring の世界では、おそらく「<b>インターセプト = アドバイス</b>」と言えると思います。

<h2 id="ProxyFactoryBean を使ったサンプルコード">ProxyFactoryBean を使ったサンプルコード</h2>

ProxyFactoryBean の使い方サンプルです。Bean 定義書に ProxyFactoryBean を使う指定をして、その<b> ID に プロキシでラップした Bean を取り出すときの名前を付ける</b> だけです。

<section>

<h4>bean-context.xml の例</h4>

<pre class="code"><code><span class="tag">&lt;?xml version=&quot;1.0&quot; encoding=&quot;UTF-8&quot;?&gt;</span>
<span class="tag">&lt;!DOCTYPE beans PUBLIC &quot;-//SPRING//DTD BEAN//EN&quot; 
&quot;http://www.springframework.org/dtd/spring-beans.dtd&quot;&gt;</span>
<span class="tag">&lt;beans&gt;</span>
  <span class="tag">&lt;bean <span class="attr">id=</span><span class="value">&quot;calcTarget&quot;</span> 
      <span class="attr">class=</span><span class="value">&quot;jp.dip.xlegend.spring.aop.Calculator&quot;</span>/&gt;</span>   
 
  <span class="tag">&lt;bean <span class="attr">id=</span><span class="value">&quot;debugInterceptor&quot;</span> 
      <span class="attr">class=</span><span class="value">&quot;org.springframework.aop.interceptor.DebugInterceptor&quot;</span>/&gt;</span>
 
  <span class="tag">&lt;bean <span class="attr">id=</span><span class="value">&quot;calc&quot;</span>
       <span class="attr">class=</span><span class="value">&quot;org.springframework.aop.framework.ProxyFactoryBean&quot;</span>&gt;</span>
      <span class="tag">&lt;property <span class="attr">name=</span><span class="value">&quot;target&quot;</span>&gt;</span>
        <span class="tag">&lt;ref <span class="attr">bean=</span><span class="value">&quot;calcTarget&quot;</span>/&gt;</span>
      <span class="tag">&lt;/property&gt;</span>
      <span class="tag">&lt;property <span class="attr">name=</span><span class="value">&quot;interceptorNames&quot;</span>&gt;</span>
      <span class="tag">&lt;list&gt;</span>
        <span class="tag">&lt;value&gt;</span>debugInterceptor<span class="tag">&lt;/value&gt;</span>
      <span class="tag">&lt;/list&gt;</span>
    <span class="tag">&lt;/property&gt;</span>
  <span class="tag">&lt;/bean&gt;</span>
<span class="tag">&lt;/beans&gt;</span>
</code></pre>

</section>

「ProxyFactoryBean」の定義のところの ID を使って Bean を取り出しています。

<section>

<h4>メインクラス</h4>

<pre class="code"><code><span class="keyword">public</span> <span class="keyword">static</span> <span class="keyword">void</span> main(String[] args) <span class="keyword">throws</span> Exception {
  BeanFactory factory = 
      <span class="keyword">new</span> XmlBeanFactory(<span class="keyword">new</span> FileInputStream(<span class="literal">"bean-context.xml"</span>));
  Calculator cal = (Calculator)factory.getBean(<span class="literal">"calc"</span>);
  cal.add(10, 20);
} 
</code></pre>

</section>

アスペクト対象の Calculator クラスです。アスペクト対象のクラスは、<strong>JavaBeanでなければなりません</strong>。デフォルトコンストラクタ(引数無しのコンストラクタ)が無いと、プロキシを作る時にエラーになります。

<section>

<h4>アスペクト対象のクラス</h4>

<pre class="code"><code><span class="keyword">public</span> <span class="keyword">class</span> Calculator {    
  <span class="keyword">public</span> Calculator() {
    <span class="keyword">super</span>();
  }
 
  <span class="keyword">public</span> <span class="keyword">int</span> add(<span class="keyword">int</span> a, <span class="keyword">int</span> b) {      
    System.out.println(<span class="literal">"add("</span> + a + <span class="literal">", "</span> + b + <span class="literal">")"</span>);
    <span class="keyword">return</span> a + b;
  }
}
</code></pre>

実行結果です。

<pre class="console"><code>Debug interceptor: count=1 invocation= ...
add(10, 20)
Debug interceptor: next returned
</code></pre>

<h2 id="Advisor とは？">Advisor (アドバイザー)とは？</h2>

Advisor (アドバイザー)とは、<a href="#ポイントカット" rel="external nofollow">アドバイス</a>をセットにしたものと考えればよさそうです。アドバイスとはアスペクトとして付け加える処理のことです。ポイントカットはアドバイスを付け加える位置のことです。つまり、<b>アドバイザーとは、自身がアドバイスであり、ポイントカットである</b>わけです。

<section>

<h4>アドバイザーの例</h4>

<pre class="code"><code><span class="tag">&lt;?xml version=&quot;.&quot; encoding=&quot;UTF-&quot;?&gt;</span>
<span class="tag">&lt;!DOCTYPE beans PUBLIC &quot;-//SPRING//DTD BEAN//EN&quot;
  &quot;http://www.springframework.org/dtd/spring-beans.dtd&quot;&gt;</span>
<span class="tag">&lt;beans&gt;</span>
  <span class="tag">&lt;bean <span class="attr">id=</span><span class="value">&quot;calcTarget&quot;</span>
      <span class="attr">class=</span><span class="value">&quot;jp.dip.xlegend.spring.aop.Calculator&quot;</span>/&gt;</span>
 
   <span class="tag">&lt;bean <span class="attr">id=</span><span class="value">&quot;debugInterceptor&quot;</span>
      <span class="attr">class=</span><span class="value">&quot;org.springframework.aop.interceptor.DebugInterceptor&quot;</span>/&gt;</span>
 
   <span class="tag">&lt;bean <span class="attr">id=</span><span class="value">&quot;advisor&quot;</span>
<span class="attr">class=</span><span class="value">&quot;org.springframework.aop.support.NameMatchMethodPointcutAdvisor&quot;</span>&gt;</span>
    <span class="tag">&lt;property <span class="attr">name=</span><span class="value">&quot;advice&quot;</span>&gt;</span>
      <span class="tag">&lt;ref <span class="attr">bean=</span><span class="value">&quot;debugInterceptor&quot;</span>/&gt;</span>
    <span class="tag">&lt;/property&gt;</span>
    <span class="tag">&lt;property <span class="attr">name=</span><span class="value">&quot;mappedNames&quot;</span>&gt;</span>
      <span class="tag">&lt;list&gt;</span>
        <span class="tag">&lt;value&gt;</span>insert*<span class="tag">&lt;/value&gt;</span>
        <span class="tag">&lt;value&gt;</span>update*<span class="tag">&lt;/value&gt;</span>
      <span class="tag">&lt;/list&gt;</span>
    <span class="tag">&lt;/property&gt;</span>
  <span class="tag">&lt;/bean&gt;</span>
 
  <span class="tag">&lt;bean <span class="attr">id=</span><span class="value">&quot;calc&quot;</span>
       <span class="attr">class=</span><span class="value">&quot;org.springframework.aop.framework.ProxyFactoryBean&quot;</span>&gt;</span>
    <span class="tag">&lt;property <span class="attr">name=</span><span class="value">&quot;target&quot;</span>&gt;</span>
      <span class="tag">&lt;ref <span class="attr">bean=</span><span class="value">&quot;calcTarget&quot;</span>/&gt;</span>
    <span class="tag">&lt;/property&gt;</span>
    <span class="tag">&lt;property <span class="attr">name=</span><span class="value">&quot;interceptorNames&quot;</span>&gt;</span>
      <span class="tag">&lt;list&gt;</span>
        <span class="tag">&lt;value&gt;</span>advisor<span class="tag">&lt;/value&gt;</span>
      <span class="tag">&lt;/list&gt;</span>
    <span class="tag">&lt;/property&gt;</span>
  <span class="tag">&lt;/bean&gt;</span>
<span class="tag">&lt;/beans&gt;</span>
</code></pre>
<div class="clear"></div>

</section>

11行目でアドバイザーを定義しています。アドバイザーは「ポイントカット」と「アドバイス」の組み合わせでした。13〜15行目で「アドバイス」を設定しています。<b>インターセプタはアドバイス</b>だということですね。16〜21行目で「ポイントカット」を正規表現で指定しています。最後に ProxyFactoryBean の interseptorNames に 定義したアドバイザーを指定して、アスペクト対象を設定すれば、AOP の出来上がりです。

<h2 id="ハマった点">ハマった点</h2>

<h3>BeanCreationException が発生する</h3>

取得しようとしている Bean に 「<b>デフォルトコンストラクタがない</b>」と、このエラーが発生することがあります。基本的に Spring の Bean 定義ファイルに書くクラスには、「デフォルトコンストラクタ」を書いておきましょう。案外忘れがち・・・

<h3>Spring の ポイントカットは、クラス単位</h3>

Bean 定義書を見ると分かるのですが、ProxyFactoryBean の target には単一のクラスしか書けません。ポイントカットに関しても、ターゲットののメソッドしかサポートしません。

トランザクション管理などで、複数のクラスを指定したい場合には、「<b>AutoProxy</b>」 と呼ばれる仕組みを使う必要があるとのことです。そのうち使ってみようかと思います。

<h2>参考</h2>

+ Spring Framework の本家です。
<a href="http://www.springframework.org/" rel="external nofollow">Spring Framework</a><img src="/images/linkext.gif" alt="linkext" />

+ Spring Framework の 日本語 Wiki です。大量の情報があります。
<a href="http://wiki.bmedianode.com/Spring/?FrontPage" rel="external nofollow">Spring Pad</a><img src="/images/linkext.gif" alt="linkext" />

+ Spring-Java/J2EEアプリケーションフレームワークリファレンスドキュメント (日本語訳)
<a href="http://www.andore.com/money/trans/spring_ref_ja.html" rel="external nofollow">Spring-Java/J2EEアプリケーションフレームワークドキュメント</a><img src="/images/linkext.gif" alt="linkext" />

+ Spring フレームワークに関しての概要です。TECHSCORE さんの記事は読みやすいなぁ (^^ ;
<a href="http://www.techscore.com/tech/Java/Spring/1.html" rel="external nofollow">TECHSCORE - Spring Framework</a><img src="/images/linkext.gif" alt="linkext" />

+ Spring を含めた、軽量コンポーネントのお話です。
<div class="rakuten"><table width="400" border="0" cellpadding="5"><tr><td colspan="2"><a href="http://www.amazon.co.jp/exec/obidos/ASIN/487311201X/sorehabooks-22/" rel="external nofollow">G-Tools</a></font><br /></td></tr></table></div>

+ Spring の ロッドジョンソンが贈る、J2EE技術者のためのバイブル
<div class="rakuten"><table width="400" border="0" cellpadding="5"><tr><td colspan="2"><a href="http://www.amazon.co.jp/exec/obidos/ASIN/4797322888/sorehabooks-22/" rel="external nofollow">G-Tools</a></font><br /></td></tr></table></div>

+ Spring のロッドジョンソンによる Spring ユーザのための本 (洋書)
<div class="rakuten"><table width="400" border="0" cellpadding="5"><tr><td colspan="2"><a href="http://www.amazon.co.jp/exec/obidos/ASIN/0764574833/sorehabooks-22/" rel="external nofollow">G-Tools</a></font><br /></td></tr></table></div>

+ SpringでWebアプリケーションを作りながら、Springの全体像がわかりやすく解説されています。
<div class="rakuten"><table width=400 border="0" cellpadding="5"><tr><td colspan="2"><a href="http://www.amazon.co.jp/exec/obidos/ASIN/4774122793/sorehabooks-22/" rel="external nofollow">G-Tools</a></font><br /></td></tr></table></div>



