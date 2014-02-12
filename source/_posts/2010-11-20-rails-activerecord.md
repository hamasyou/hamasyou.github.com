---
layout: post
title: "[Rails] ActiveRecord でツリー関係の関連を定義する"
date: 2010-11-20 01:54
comments: true
categories: [Blog]
keywords: "Rails, ActiveRecord, Ruby, ツリー関係"
tags: [ActiveRecord,Rails,Ruby]
author: hamasyou
amazon_url: ""
amazon_author: ""
amazon_image: ""
amazon_publisher: ""
---

Ruby on Rails で、ActiveRecord を使ってツリー関係の関連を定義する方法のメモです。

ツリー関係を表す関連にはひとつのテーブルで表す方法と関連テーブルを使って表す方法とがありますが、今回は関連テーブルを使ってツリー関係を表す方法のメモです。

環境は Rails3.0.1、ActiveRecord3.0.1 で確認しています。


<!-- more -->

<h2>ActiveRecord でツリー関係を表す方法</h2>

下のようなツリー関係のあるモデルを、<em>belongs_to</em>、<em>has_one</em>、<em>has_many</em> を使って表す方法のメモです。

<img alt="スクリーンショット（2010-11-20 0.58.43）2.png" src="/images/%E3%82%B9%E3%82%AF%E3%83%AA%E3%83%BC%E3%83%B3%E3%82%B7%E3%83%A7%E3%83%83%E3%83%88%EF%BC%882010-11-20%200.58.43%EF%BC%892.png" width="693" class="mt-image-none" style="" />

<h3>モデルの説明</h3>

このモデルは、カテゴリ（Category）とカテゴリ関係（CategoryRel）の二つのテーブルを使ってカテゴリのツリー関係を表したものです。

カテゴリ（Category）は親カテゴリを has_one で、サブカテゴリを has_many で保持します。

カテゴリ関連（CategoryRel）は belongs_to で親カテゴリとサブカテゴリをそれぞれ定義しています。

<h4>Category モデル</h4>

<pre class="code">
<span class="keyword">class</span> <span class="class">Category</span> &lt; <span class="class">ActiveRecord::Base</span>
 
  <span class="comment"># 親カテゴリ</span>
  <span class="keyword">has_one</span> <span class="symbol">:parent_rels</span>, <span class="symbol">:class_name</span> =&gt; <span class="literal">&quot;CategoryRel&quot;</span>, <span class="symbol">:foreign_key</span> =&gt; <span class="literal">&quot;sub_category_id&quot;</span>
  <span class="keyword">has_one</span> <span class="symbol">:parent_category</span>, <span class="symbol">:through</span> =&gt; <span class="symbol">:parent_rels</span>, <span class="symbol">:source</span> =&gt; <span class="symbol">:parent_category</span>
 
  <span class="comment"># サブカテゴリ</span>
  <span class="keyword">has_many</span> <span class="symbol">:category_rels</span>
  <span class="keyword">has_many</span> <span class="symbol">:sub_categories</span>, <span class="symbol">:through</span> =&gt; <span class="symbol">:category_rels</span>, <span class="symbol">:source</span> =&gt; <span class="symbol">:sub_category</span>
<span class="keyword">end</span></pre>

<h4>CategoryRel モデル</h4>

<pre class="code">
<span class="keyword">class</span> <span class="class">CategoryRel</span> &lt; <span class="class">ActiveRecord::Base</span>
  <span class="comment"># 親カテゴリ</span>
  <span class="keyword">belongs_to</span> <span class="symbol">:parent_category</span>, <span class="symbol">:class_name</span> =&gt; <span class="literal">&quot;Category&quot;</span>, <span class="symbol">:foreign_key</span> =&gt; <span class="literal">&quot;category_id&quot;</span>
  <span class="comment"># サブカテゴリ</span>
  <span class="keyword">belongs_to</span> <span class="symbol">:sub_category</span>, <span class="symbol">:class_name</span> =&gt; <span class="literal">&quot;Category&quot;</span>, <span class="symbol">:foreign_key</span> =&gt; <span class="literal">&quot;sub_category_id&quot;</span>
<span class="keyword">end</span></pre>

<h3>has_many :through :source</h3>

関連をひとつだけもつ場合には <strong>has_one</strong> を、1対多、多対多 を表す場合には <strong>has_many</strong> を使います。<em>:through</em> オプションと <em>:source</em> オプションはそれぞれ次のような意味です。

<dl>
<dt>:through</dt>
<dd><p>has_many で定義した関連をどういう経路で取得するかを示す。</p></dd>
<dt>:source</dt>
<dd><p>:through で示された経路の先で、どの関連を使用するかを示す。</p></dd>
</dl>

<h3>親カテゴリを表す has_one と外部キーの指定の仕方</h3>

今回の例で見ていくと、カテゴリは親カテゴリを 0 or 1 持ちます。なので、has_one を指定しています。

親カテゴリの定義では、最初に直接の関連であるカテゴリ関連の定義をしています。それが、<code>has_one :parent_rels, :class_name =&gt; &quot;CategoryRel&quot;, :foreign_key =&gt; &quot;sub_category_id&quot;</code> の部分です。

この関連は、:class_name で指定したクラスの関連、すなわちカテゴリ関連になります。カテゴリ関連との関係を <em>:foreign_key</em> で指定しています。親カテゴリは、カテゴリ関連の sub_category_id が自分のIDと一致するレコードのカテゴリのことになるので、:foreign_key には自分のIDと対応するカラムである sub_category_id を指定しています。

<h4>関連のショートカットを定義する</h4>

次に、毎回 parent_rels 関連をたどって親カテゴリオブジェクトを取得するのは面倒くさいので、:through を使ってショートカットを定義しています。それが <code> has_one :parent_category, :through =&gt; :parent_rels, :source =&gt; :parent_category</code> の部分です。

<em>:through</em> にはどの経路を使ってオブジェクトを得るかを指定します。:source には :through でたどった先に関連が二つ以上ある場合にどれを使うかを指定します。カテゴリ関連には親カテゴリとサブカテゴリを表す関連があるので、親カテゴリの関連である :parent_category を指定しています。

<p class="option"><em>:source の考え方</em><br />最初、:source は関連の元（ソース）はどっちか？を表すオプションだと思っていたので、自分のインスタンスから見て自分を表すのはどれか？を指定していましたが、これは間違いです。<br /><br />:source オプションは自分がどっちを表すものではなく、<strong>:through で表される経路の取得先を示す</strong>ものです。なので、自分から見て親カテゴリを表す関連はどっちか？を指定します。</p>

<h3>サブカテゴリを表す has_many</h3>

サブカテゴリも、基本的には親カテゴリと同じです。サブカテゴリは複数取り得るので <em>has_many</em> を指定しています。

最初に直接の関連であるカテゴリ関連を has_many で定義します。定義名のみを書いておくと、ActiveRecord がモデル名の規約から適切に関連を定義してくれます。

今回は、カテゴリ関連の複数形である :category_rels を has_many で定義しているので、ActiveRecord が自動的に CateogryRel モデルの関連を張ってくれます。

次に、<code>has_many :sub_categories, :through =&gt; :category_rels, :source =&gt; :sub_category</code> の定義で category_rels の関連を経由して、カテゴリ関連の :sub_category で定義されている関連をサブカテゴリとして定義しています。

<h2>まとめ</h2>

説明がわかりずらく、逆にわからなくなったかもしれませんが、要は、has_many :through でたどった先に複数の関連がある場合（厳密には規約から推測できない関連があった場合）、:source を使ってどの関連をソースにしてオブジェクトを集めるかを指定する必要があるということです。





