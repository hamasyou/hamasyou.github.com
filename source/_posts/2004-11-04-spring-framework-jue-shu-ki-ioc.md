---
layout: post
title: "Spring Framework 覚書き - IoC"
date: 2004-11-04 01:14
comments: true
categories: [Blog]
keywords: "Spring,Framework,覚書き,スプリング,フレームワーク,アーキテクチャ,DI,IoC,トランザクション"
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

Springが他のIoCコンテナと差別化を計っている点として、「<b>ビジネスオブジェクトを管理する方法にフォーカスを当てている</b>」という点があります。また、「レイヤー化されたアーキテクチャであるため、局所的にSpringアーキテクチャを利用できる」、「テストドリブンプロジェクトに理想的なフレームワークになるように設計されている」という利点があります。

Springの詳細については、ほかにもっとよいサイト(<a href="http://www.andore.com/money/trans/spring_ref_ja.html" rel="external nofollow">Springフレームワークの紹介</a>)があるので、そちらを参考にしてください。ここでは、Springを使っていて、ハマった点や気になった点などをメモしていこうと思います。随時更新していくつもりです。間違っている可能性が高いので、気になる点があればコメントをお願いします。

<section>

<h4>参考</h4>

<a href="http://wiki.bmedianode.com/Spring/?FrontPage" rel="external nofollow">Spring Pad - Wiki</a>

<a href="http://direct.idg.co.jp/detail_1.msp?id=1066&class=10005&n=2" rel="external nofollow">JavaWorld 7月号 2004年</a>

</section>


<!-- more -->

<h2>IoCコンテナ</h2>

<ul><li><a href="#IoCコンテナとは" rel="external nofollow">IoCコンテナとは</a></li>
<li><a href="#IoC で使われる主なクラス / インターフェース" rel="external nofollow">IoC で使われる主なクラス / インターフェース</a></li>
<li><a href="#XmlBeanFactory を使った、サンプルコード" rel="external nofollow">XmlBeanFactory を使った、サンプルコード</a></li>
<li><a href="#ApplicationContext を使った場合" rel="external nofollow">ApplicationContext を使った場合</a></li>
<li><a href="#bean-context.xml (Bean定義ファイル)の中身" rel="external nofollow">bean-context.xml (Bean定義ファイル)の中身</a></li>
<li><a href="#CustomEditorConfigurer を使った、特殊型へのマッピング" rel="external nofollow">CustomEditorConfigurer を使った、特殊型へのマッピング</a></li>
<li><a href="#MessageResource の取り扱い" rel="external nofollow">MessageResource の取り扱い</a></li>
<li><a href="#ApplicationContextの取得方法その2" rel="external nofollow">ApplicationContextの取得方法その2</a></li>
<li><a href="#BeanFactoryLocator が必要な理由" rel="external nofollow">BeanFactoryLocator が必要な理由</a></li>
<li><a href="#Beanのプロパティを別ファイルに定義する方法" rel="external nofollow">Beanのプロパティを別ファイルに定義する方法</a></li>
<li><a href="#メソッドの実行結果をBeanのプロパティに設定する" rel="external nofollow">メソッドの実行結果をBeanのプロパティに設定する</a></li>
<li><a href="#ハマった点" rel="external nofollow">ハマった点</a></li></ul>

<h2 id="IoCコンテナとは">IoCコンテナとは</h2>

Spring の IoC コンテナはXMLで定義された Bean定義ファイル に基づいて、オブジェクトの生成や初期化、関連付けを行います。

オブジェクトの管理に使うこともできます。例えば、Singleton (インスタンスが常に一つしか存在しないクラス) として扱いたい場合にも、Bean定義ファイルに設定することで Singleton として扱えるようになります。

<h2 id="IoC で使われる主なクラス / インターフェース">IoC で使われる主なクラス / インターフェース</h2>

IoCコンテナを利用する場合に使われる主なクラスやインターフェースです。

<h3>org.springframework.context.ApplicationContext</h3>

このクラスはIoCの核となるものです。以下のような機能を ApplicationContext は備えています。

<ul><li>BeanFactory 機能</li>
<li>国際化機能</li>
<li>リソースファイルのロード機能</li>
<li>複数コンテキストの扱い</li>
<li>イベントの通知</li>
</ul>

<h3>org.springframework.beans.factory.BeanFactory</h3>

IoCコンテナの核となるインターフェース。このインターフェースの実装クラスが、Beanインスタンスを生成し、初期値を設定してくれる。ただし、メモリ容量などの縛りがない限り通常は ApplicationContext の使用が推奨されています。

通常は、ApplicationContext を使用します。

<h3>org.springframework.context.support.FileSystemXmlApplicationContext</h3>

XMLファイルを読み込んで Beanインスタンスを生成します。おそらく、一般的な ApplicationContext の実装だと思います。

<h3>org.springframework.beans.factory.xml.XmlBeanFactory</h3>

Beanの定義ファイル(XML)を読み込んでインスタンスの生成や初期化を行うクラス。XMLのDTDは「<a href="http://www.springframework.org/dtd/spring-beans.dtd" rel="external nofollow">http://www.springframework.org/dtd/spring-beans.dtd</a>」が利用できます。BeanFactory を利用する場合にはこのクラスを使います。

<h3>org.springframework.beans.factory.InitializingBean</h3>

このインターフェースを実装しているクラスは、IoC コンテナが BeanFactoryがインスタンスを生成して初期値を設定した後に呼び出されるようになります。このインターフェースに定義されている afterPropertiesSet() で初期化処理を行うことが出来るようになります。これは、Bean定義の init-method を指定したのと同じ動作になります。

POJOのクラスは、Springに依存させたくない場合がほとんどでしょう。通常、このインターフェースを実装するよりも、Bean定義に init-method を指定したほうが幸せになれます。

<h3>org.springframework.beans.factory.DisposableBean</h3>

このインターフェースを実装しているクラスは、BeanFactory のデストラクタ処理の中で呼ばれるようになります。通常、Bean中のリソースを開放したい場合に使います。destroy() メソッドを実装します。 Bean定義の destroy-method を指定したのと同じ動作です。

POJOのクラスは、Springに依存させたくない場合がほとんどでしょう。通常、このインターフェースを実装するよりも、Bean定義に destroy-method を指定したほうが幸せになれます。

<h3>org.springframework.beans.factory.config.CustomEditorConfigurer</h3>

Bean定義の中で、プロパティの型が特殊なもの (独自クラスや Timestamp型など) は、Bean定義ファイルの中に直接値を書いても、BeanCreationException が発生してしまいます。原因は型が一致しないからです。このクラスは、指定した型に独自のコンバート処理を施すことが出来るようになります。<a href="#CustomEditorConfigurer を使った、特殊型へのマッピング" rel="external nofollow">サンプルコード</a>を参考にしてください。

<section>

<h4>デフォルトで用意されている主なカスタムエディター</h4>

<dl>
<dt>org.springframework.beans.propertyeditors.CustomBooleanEditor</dt>
<dd><p>true, false という文字列を Boolean に変換します。</p></dd>
<dt>org.springframework.beans.propertyeditors.CustomDateEditor</dt>
<dd><p>日付文字列を日付型に変換します。日付文字列は、コンストラクタに渡すフォーマッタで指定できます。</p></dd>
<dt>org.springframework.web.multipart.support.ByteArrayMultipartFileEditor</dt>
<dd><p>マルチパートファイルをバイトの配列に変換します。</p></dd>
<dt>org.springframework.beans.propertyeditors.CustomNumberEditor</dt>
<dd><p>Integer, Long, Short, Double, Float 等の数値型への変換を行います。</p>

<p>Webアプリケーションで使用する場合は、通例では BaseCommandController の initBinder メソッドで、binder.registerCustomEditor を呼び出すようにする。</p></dd>
<dt>org.springframework.beans.propertyeditors.StringTrimmerEditor</dt>
<dd><p>空文字 を null に変換するエディターです。Webアプリケーションで使えるかも。</p></dd>
</dl>

</section>

<h3>org.springframework.beans.factory.access.BeanFactoryLocator</h3>

BeanFactory クラスをルックアップするためのインターフェースです。このインターフェースの実装クラスは、主に Singleton で使われます。Singleton で使われる理由は「<a href="#BeanFactoryLocator が必要な理由" rel="external nofollow">BeanFactoryLocator が必要な理由</a>」を見てください。

<h2 id="XmlBeanFactory を使った、サンプルコード">XmlBeanFactory を使った、サンプルコード</h2>

ここでは、まず XmlBeanFactory を作成しています。これが IoC コンテナの本体です。

<section>

<h4>XmlBeanFactory のサンプル</h4>

<pre class="code"><code><span class="keyword">package</span> com.hamasyou.spring; 
 
<span class="keyword">import</span> java.io.FileInputStream; 
<span class="keyword">import</span> java.math.BigDecimal; 
<span class="keyword">import</span> junit.framework.TestCase; 
<span class="keyword">import</span> org.springframework.beans.factory.BeanFactory; 
<span class="keyword">import</span> org.springframework.beans.factory.xml.XmlBeanFactory; 
 
<span class="keyword">public</span> <span class="keyword">class</span> ExsampleBeanTest <span class="keyword">extends</span> TestCase { 
  <span class="keyword">public</span> <span class="keyword">void</span> testDataAccess() <span class="keyword">throws</span> Exception { 
    BeanFactory factory =  
      <span class="keyword">new</span> XmlBeanFactory(<span class="keyword">new</span> FileInputStream(<span class="literal">"bean-context.xml"</span>)); 
    PersonAccessDAO dao = (PersonAccessDAO)factory.getBean(<span class="literal">"personAccessDAO"</span>);
    Person person = dao.findPerson(BigDecimal.valueOf(1)); 
        
    assertEquals(<span class="literal">"IDは 1 のはず"</span>, 1, person.getId().intValue()); 
  } 
} 
</code></pre>

</section>

<h2 id="ApplicationContext を使った場合">ApplicationContext を使った場合</h2>

ApplicationContext は、BeanFactory に国際化メッセージ機能や、イベント通知機能を備えたものです。複数のコンテキストを扱え、Bean定義ファイルを分割することが出来ます。BeanFactory と使い方はほとんどかわりません。プロジェクトでは、BeanFactory よりも ApplicationContext を利用するほうが多いと思います。

<h3>BeanFactory よりも ApplicationContext をつかう</h3>

BeanFactory よりも ApplicationContext を主に使いましょう。ApplicationContext は、AOP のサポートや分散トランザクションのサポートなど、 BeanFactory よりも高機能を備えています。

<section>

<h4>ApplicationContext のサンプル</h4>

<pre class="code"><code><span class="keyword">public</span> <span class="keyword">void</span> testApplicationContext() <span class="keyword">throws</span> Exception { 
  FileSystemXmlApplicationContext context = 
    <span class="keyword">new</span> FileSystemXmlApplicationContext(  
        <span class="keyword">new</span> String[] { <span class="literal">"bean-context.xml"</span>, 
                       <span class="literal">"bean-context2.xml"</span>, 
                       <span class="literal">"custom-edit.xml"</span> } ); 
  PersonAccessDAO dao = 
      (PersonAccessDAO)context.getBean(<span class="literal">"personAccessDAO"</span>);         
  Person person = dao.findPerson(BigDecimal.valueOf(1)); 
         
  assertEquals(<span class="literal">"IDは 1 のはず"</span>, 1, person.getId().intValue());         
} 
</code></pre>

</section>

<h2 id="bean-context.xml (Bean定義ファイル)の中身">bean-context.xml (Bean定義ファイル)の中身</h2>

Bean定義ファイルには、Beanの設定を書いておきます。プロジェクトで使い始めたら、あっという間にBeanファイルが大きくなりすぎてしまう気がします。<b>ApplicationContext</b> なら、複数のコンテキストを扱えるので、Bean 定義を複数ファイルに分割することが出来ます。

ApplicationContext クラスを使うと、Bean 定義ファイルを分割することができます。ApplicationContext を使う場合、bean-context.xml ではなく <strong>applicationContext.xml</strong> というファイル名を使うのが慣習です。

<section>

<h4>bean-context.xml</h4>

<pre class="code"><code><span class="tag">&lt;?xml version=&quot;1.0&quot; encoding=&quot;UTF-8&quot;?&gt;</span> 
<span class="tag">&lt;!DOCTYPE beans PUBLIC &quot;-//SPRING//DTD BEAN//EN&quot; 
 &quot;http://www.springframework.org/dtd/spring-beans.dtd&quot;&gt;</span> 
<span class="tag">&lt;beans&gt;</span> 
  <span class="tag">&lt;bean <span class="attr">id=</span><span class="value">&quot;dataSource&quot;</span>  
        <span class="attr">class=</span><span class="value">&quot;org.springframework.jdbc.datasource.DriverManagerDataSource&quot;</span>&gt;</span> 
    <span class="tag">&lt;property <span class="attr">name=</span><span class="value">&quot;driverClassName&quot;</span>&gt;</span> 
      <span class="tag">&lt;value&gt;</span>org.hsqldb.jdbcDriver<span class="tag">&lt;/value&gt;</span> 
    <span class="tag">&lt;/property&gt;</span> 
    <span class="tag">&lt;property <span class="attr">name=</span><span class="value">&quot;url&quot;</span>&gt;</span> 
      <span class="tag">&lt;value&gt;</span>jdbc:hsqldb:hsql://localhost<span class="tag">&lt;/value&gt;</span> 
    <span class="tag">&lt;/property&gt;</span> 
    <span class="tag">&lt;property <span class="attr">name=</span><span class="value">&quot;username&quot;</span>&gt;</span> 
      <span class="tag">&lt;value&gt;</span>sa<span class="tag">&lt;/value&gt;</span> 
    <span class="tag">&lt;/property&gt;</span> 
    <span class="tag">&lt;property <span class="attr">name=</span><span class="value">&quot;password&quot;</span>&gt;</span> 
      <span class="tag">&lt;value&gt;</span><span class="tag">&lt;/value&gt;</span> 
    <span class="tag">&lt;/property&gt;</span>     
  <span class="tag">&lt;/bean&gt;</span> 
   
  <span class="tag">&lt;bean <span class="attr">id=</span><span class="value">&quot;personAccessDAO&quot;</span>  
        <span class="attr">class=</span><span class="value">&quot;jp.dip.xlegend.spring.dao.SimpleAccessDAO&quot;</span>&gt;</span> 
    <span class="tag">&lt;property <span class="attr">name=</span><span class="value">&quot;dataSource&quot;</span>&gt;</span> 
      <span class="tag">&lt;ref <span class="attr">local=</span><span class="value">&quot;dataSource&quot;</span>/&gt;</span> 
    <span class="tag">&lt;/property&gt;</span> 
  <span class="tag">&lt;/bean&gt;</span> 
<span class="tag">&lt;/beans&gt;</span>
</code></pre>

</section>

<h2 id="CustomEditorConfigurer を使った、特殊型へのマッピング">CustomEditorConfigurer を使った、特殊型へのマッピング</h2>

例えば Timestamp 型を Bean に設定したい場合、ファイルに直接値を書いても、BeanCreationException が発生してしまいます。特殊型へのマッピングには、いくつか方法がありますが、下記はそのうちの一つです。

<section>

<h4>[参考]</h4>
+ <a href="http://wiki.bmedianode.com/Spring/?BeanFactory%A4%CE%A5%AB%A5%B9%A5%BF%A5%DE%A5%A4%A5%BA" rel="external nofollow">Spring Pad - BeanFactory のカスタマイズ</a>

</section>

<section>

<h4>Timestamp型へのマッピングがあるBean定義</h4>

<pre class="code"><code><span class="tag">&lt;?xml version=&quot;1.0&quot; encoding=&quot;UTF-8&quot;?&gt;</span> 
<span class="tag">&lt;!DOCTYPE beans PUBLIC &quot;-//SPRING//DTD BEAN//EN&quot; 
 &quot;http://www.springframework.org/dtd/spring-beans.dtd&quot;&gt;</span> 
<span class="tag">&lt;beans&gt;</span> 
  <span class="tag">&lt;bean <span class="attr">id=</span><span class="value">&quot;employee&quot;</span> <span class="attr">class=</span><span class="value">&quot;jp.dip.xlegend.spring.Employee&quot;</span>&gt;</span> 
    <span class="tag">&lt;constructor-arg <span class="attr">index=</span><span class="value">&quot;0&quot;</span> <span class="attr">type=</span><span class="value">&quot;java.math.BigDecimal&quot;</span>&gt;</span> 
      <span class="tag">&lt;value&gt;</span>1<span class="tag">&lt;/value&gt;</span> 
    <span class="tag">&lt;/constructor-arg&gt;</span> 
    <span class="tag">&lt;property <span class="attr">name=</span><span class="value">&quot;name&quot;</span>&gt;</span> 
      <span class="tag">&lt;value&gt;</span>Employee Syougo Hamada<span class="tag">&lt;/value&gt;</span> 
    <span class="tag">&lt;/property&gt;</span> 
    <span class="tag">&lt;property <span class="attr">name=</span><span class="value">&quot;birthday&quot;</span>&gt;</span> 
      <span class="tag">&lt;value&gt;</span>2004-11-04 00:48:32.00<span class="tag">&lt;/value&gt;</span> 
    <span class="tag">&lt;/property&gt;</span>     
  <span class="tag">&lt;/bean&gt;</span>     
   
  <span class="tag">&lt;bean <span class="attr">id=</span><span class="value">&quot;customEditorConfig&quot;</span> 
        <span class="attr">class=</span><span class="value">&quot;org.springframework.beans.factory.config.CustomEditorConfigurer&quot;</span>&gt;</span> 
    <span class="tag">&lt;property <span class="attr">name=</span><span class="value">&quot;customEditors&quot;</span>&gt;</span> 
      <span class="tag">&lt;map&gt;</span> 
        <span class="tag">&lt;entry <span class="attr">key=</span><span class="value">&quot;java.sql.Timestamp&quot;</span>&gt;</span> 
          <span class="tag">&lt;bean <span class="attr">class=</span><span class="value">&quot;jp.dip.xlegend.spring.custom.TimestampEditor&quot;</span>/&gt;</span> 
        <span class="tag">&lt;/entry&gt;</span> 
      <span class="tag">&lt;/map&gt;</span> 
    <span class="tag">&lt;/property&gt;</span> 
  <span class="tag">&lt;/bean&gt;</span>   
<span class="tag">&lt;/beans&gt;</span>
</code></pre>

</section>

これが 独自コンバータの実装です。java.beans.PropertyEditorSupport クラスを継承して作ると簡単になります。

<section>

<h4>TimestampEditor クラスの実装</h4>

<pre class="code"><code><span class="keyword">package</span> com.hamasyou.spring.custom;
 
<span class="keyword">import</span> java.beans.PropertyEditorSupport;
<span class="keyword">import</span> java.sql.Timestamp;
 
<span class="keyword">public</span> <span class="keyword">class</span> TimestampEditor <span class="keyword">extends</span> PropertyEditorSupport {
  <span class="keyword">public</span> String getAsText() {
    <span class="keyword">return</span> getValue().toString();
  }
 
  <span class="keyword">public</span> <span class="keyword">void</span> setAsText(String text) <span class="keyword">throws</span> IllegalArgumentException {
    setValue(Timestamp.valueOf(text));
  }
}
</code></pre>

</section>

<h2 id="MessageResource の取り扱い">MessageResource の取り扱い</h2>

ApplicationContext は MessageResource を扱えるようになっています。実装は <b>StaticMessageSource</b> や<b>ResourceBundleMessageSource</b> が使えるようです。 Bean定義ファイルに 「<b>messageSource</b>」という ID で Bean を定義することで、MessageResource の実装を切り替えられるようです。

この方法を使うと、MessageResource を使うクラスが ApplicationContext に依存してしまいます。依存しないようにするには、アダプターとか使うと良いかもしれません。Beanクラスに ApplicationContext を渡すためには、 <i>ApplicationContextAware</i>  インターフェースを実装すれば良いです。setApplicationContext というメソッドを使い、ApplicationContext にアクセスできます。

<pre class="code"><code><span class="tag">&lt;bean <span class="attr">id=</span><span class="value">&quot;messageSource&quot;</span>
    <span class="attr">class=</span><span class="value">&quot;org.springframework.context.support.ResourceBundleMessageSource&quot;</span>&gt;</span>
  <span class="tag">&lt;property <span class="attr">name=</span><span class="value">&quot;basenames&quot;</span>&gt;</span>
    <span class="tag">&lt;list&gt;</span>
      <span class="tag">&lt;value&gt;</span>application_messages<span class="tag">&lt;/value&gt;</span>
    <span class="tag">&lt;/list&gt;</span>
  <span class="tag">&lt;/property&gt;</span>
<span class="tag">&lt;/bean&gt;</span>
</code></pre>

<h2 id="ApplicationContextの取得方法その2">ApplicationContextの取得方法その2</h2>

BeanFactory や ApplicationContext は 直接 <code><span class="keyword">new</span></code> することもできますが、設定ファイルに実装クラスを書いておきたい場合もあるでしょう。そんな場合には、「<b>SingletonBeanFactoryLocator</b>」、「<b>ContextSingletonBeanFactoryLocator</b>」を使えます。詳しくは、<a href="http://wiki.bmedianode.com/Spring/?FrontPage" rel="external nofollow">邪悪な Singleton</a>』を参考にするといいと思います。

{% blockquote 本書 %}
BeanFactory や ApplicationContext は Singleton オブジェクトとして扱うべきです。大きく2つの理由があります。

<ul><li>サードパティのコードが BeanFactory や ApplicationContext を使って、オブジェクトをインスタンス化したときにも、こちら側で設定した Bean定義を生かせるようにしたい</li>
<li>Webアプリケーションのようなリソースが制限されるアプリのときに、最小のリソースですむようにしたい</li>
</ul>


{% endblockquote %}

<code>BeanFactoryLocator</code> は XML 定義ファイルを読み込んでコンテナを生成してくれます。読み込む定義ファイル名は定義されているようです。

<dl><dt>SingletonBeanFactoryLocator</dt>
<dd><p>BeanFactory コンテナを生成するクラス。定義ファイル名は「beanRefFactory.xml」</p></dd>
<dt>ContextSingletonBeanFactoryLocator</dt>
<dd><p>ApplicationContext コンテナを生成するクラス。定義ファイル名は「beanRefContext.xml」</p></dd>
</dl>

このファイルの中に、例えば次のように書きます。

<pre class="code"><code><span class="tag">&lt;?xml version=&quot;1.0&quot; encoding=&quot;UTF-8&quot;?&gt;</span>
<span class="tag">&lt;!DOCTYPE beans PUBLIC &quot;-//SPRING//DTD BEAN//EN&quot; 
&quot;http://www.springframework.org/dtd/spring-beans.dtd&quot;&gt;</span>
<span class="tag">&lt;beans&gt;</span>
  <span class="tag">&lt;bean <span class="attr">id=</span><span class="value">&quot;context&quot;</span> 
      <span class="attr">class=</span><span class="value">&quot;org.springframework.context.support.ClassPathXmlApplicationContext&quot;</span>&gt;</span>
    <span class="tag">&lt;constructor-arg&gt;</span>
      <span class="tag">&lt;list&gt;</span>
        <span class="tag">&lt;value&gt;</span>bean-factory.xml<span class="tag">&lt;/value&gt;</span>
        <span class="tag">&lt;value&gt;</span>bean-context.xml<span class="tag">&lt;/value&gt;</span>
      <span class="tag">&lt;/list&gt;</span>
    <span class="tag">&lt;/constructor-arg&gt;</span>
  <span class="tag">&lt;/bean&gt;</span>
<span class="tag">&lt;/beans&gt;</span>
</code></pre>

そして、BeanFactory、ApplicationContext を生成するコードはこんな感じになります。

<pre class="code"><code>BeanFactoryLocator locator = ContextSingletonBeanFactoryLocator.getInstance();
BeanFactoryReference ref = locator.useBeanFactory(<span class="literal">"context"</span>);
ApplicationContext context = (ApplicationContext) ref.getFactory();
Person person = (Person)context.getBean(<span class="literal">"personBean"</span>);
ref.release();
</code></pre>

<h2 id="BeanFactoryLocator が必要な理由">BeanFactoryLocator が必要な理由</h2>

<a href="http://wiki.bmedianode.com/Spring/?%BC%D9%B0%AD%A4%CASingleton" rel="external nofollow">Spring Pad 「邪悪なSingleton」</a> の中でこんな部分がありました。

{% blockquote <a href="http://wiki.bmedianode.com/Spring/?%BC%D9%B0%AD%A4%CASingleton" rel="external nofollow">Spring Pad 「邪悪なSingleton」</a> %}
なぜこういうものが必要かというと，コンテナの外でインスタンス化されたオブジェクトからコンテナを使いたい場合があるから，ということです．世の中にはすでに様々なフレームワークがあって，インスタンス化はそれらにおまかせというのはIoCなコンテナに限った話ではありません．そのようなフレームワークをいろいろと組み合わせて使う場合，あっちでインスタンス化されたオブジェクトからこっちのコンテナでDependency Injectionされたオブジェクトを使いたいということもあるでしょう．そのような場合のために，Spring Frameworkが用意してくれているのが BeanFactoryLocator です．だから"Glue code"なんですね．


{% endblockquote %}

つまり、SpringFramework の外にあるオブジェクトから、SpringFramework が管理しているオブジェクトに触りたいときに使うのが BeanFactoryLocator というわけです。

<h2 id="Beanのプロパティを別ファイルに定義する方法">Beanのプロパティを別ファイルに定義する方法</h2>

Beanのプロパティを別ファイルに定義しておいて、実行時に設定させたい場合があります。例えば、データベース接続情報など、Bean定義ファイルの中身は直接触らせたくないけど、お客様にデータベースの接続情報を変更して欲しい場合などです。そんなときは、データベースの接続情報だけをリソースファイルとして定義しておき、置換処理を行うことで、Beanのプロパティを設定できます。

<section>

<h4>置換処理でプロパティを設定する</h4>

<pre class="code"><code><span class="tag">&lt;bean <span class="attr">id=</span><span class="value">&quot;propertyConfigurer&quot;</span>
  <span class="attr">class=</span><span class="value">&quot;org.springframework.beans.factory.config.PropertyPlaceholderConfigurer&quot;</span>&gt;</span>
  <span class="tag">&lt;property <span class="attr">name=</span><span class="value">&quot;locations&quot;</span>&gt;</span> 
    <span class="tag">&lt;list&gt;</span> 
      <span class="tag">&lt;value&gt;</span>jdbc.properties<span class="tag">&lt;/value&gt;</span> 
    <span class="tag">&lt;/list&gt;</span> 
  <span class="tag">&lt;/property&gt;</span> 
 <span class="tag">&lt;/bean&gt;</span> 
  
<span class="tag">&lt;bean <span class="attr">id=</span><span class="value">&quot;dataSource&quot;</span>  
      <span class="attr">class=</span><span class="value">&quot;org.apache.commons.dbcp.BasicDataSource&quot;</span>  
      <span class="attr">destroy-method=</span><span class="value">&quot;close&quot;</span>&gt;</span> 
  <span class="tag">&lt;property <span class="attr">name=</span><span class="value">&quot;driverClassName&quot;</span>&gt;</span> 
    <span class="tag">&lt;value&gt;</span>${jdbc.driverClassName}<span class="tag">&lt;/value&gt;</span> 
  <span class="tag">&lt;/property&gt;</span> 
  <span class="tag">&lt;property <span class="attr">name=</span><span class="value">&quot;url&quot;</span>&gt;</span> 
    <span class="tag">&lt;value&gt;</span>${jdbc.url}<span class="tag">&lt;/value&gt;</span> 
  <span class="tag">&lt;/property&gt;</span> 
  <span class="tag">&lt;property <span class="attr">name=</span><span class="value">&quot;username&quot;</span>&gt;</span> 
    <span class="tag">&lt;value&gt;</span>${jdbc.username}<span class="tag">&lt;/value&gt;</span> 
  <span class="tag">&lt;/property&gt;</span> 
  <span class="tag">&lt;property <span class="attr">name=</span><span class="value">&quot;password&quot;</span>&gt;</span> 
    <span class="tag">&lt;value&gt;</span>${jdbc.password}<span class="tag">&lt;/value&gt;</span> 
  <span class="tag">&lt;/property&gt;</span>     
<span class="tag">&lt;/bean&gt;</span>
</code></pre>

</section>

PropertyPlaceholderConfigurer クラスは、設定ファイル中の 「<b>${置換文字列}</b>」とマッチするキーが、locations で指定したリソースファイルの中にあれば、置換します。結構使えると思います。

<h2 id="メソッドの実行結果をBeanのプロパティに設定する">メソッドの実行結果をBeanのプロパティに設定する</h2>

コンテナ管理される Bean のプロパティに、メソッドの実行結果を設定したい場合があります。例えば、データベースの値であったり、設定ファイルを読み込んだ値だったりするものです。その場合は、<b>org.springframework.beans.factory.config.MethodInvokingFactoryBean</b> という FactoryBean をつかう事ができます。詳しくは、「<a href="http://www.andore.com/money/trans/spring_ref_p4_ja.html#doc1_3.14" rel="external nofollow">メソッド起動の結果にビーンプロパティを設定する</a>」を参考に。

MethodInvoke の実行結果はキャッシュされるようです。キャッシュされないようにするには、setSingleton プロパティに false を設定すればよさそうです。

<h2 id="ハマった点">ハマった点</h2>

<h3>インスタンスの初期化をコンテナの外から渡すには</h3>

コンテナ管理のオブジェクトは、必要なときに生成され、初期化されます。生成時に必要な引数や、初期化時の値は通常 Bean 定義ファイルに書きます。

コンテナ管理されるオブジェクトの初期化に、コンテナ管理されていないオブジェクトを渡したい場合はどうするのでしょうか？例えば、GUI の入力値保持するオブジェクトをコンテナ管理されるオブジェクトの初期化に使いたい場合などです。

おそらく、「<b>コンテナ管理されるオブジェクトをコンテナ管理されないオブジェクトに依存させることは出来ない</b>」はずです。そもそも、DIコンテナで扱うべきものというのは、<a href="http://e-words.jp/w/E382B3E383B3E3839DE383BCE3838DE383B3E38388.html" rel="external nofollow">コンポーネント</a> の単位であるはずです。コンポーネントとして切り出すときの指針として、設定ファイルとして書けるか？というのがあります。GUI の入力は設定ファイルとして書けるはずありませんから、DIで扱うべきものではないと言うのが、僕の意見です。

Seasar2 フレームワークでは、こういう用途にも対応できる機能が備わっています。もしかしたら、SpringFramework にも後のバージョンで可能になるかもしれません。

<section>

<h4>[参考]</h4>

『<a href="http://hamasyou.com/archives/000238" rel="external nofollow">Dependency Injection の乱用！？</a>』

</section>

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


















