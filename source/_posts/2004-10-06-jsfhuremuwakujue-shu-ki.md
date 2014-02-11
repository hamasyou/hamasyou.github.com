---
layout: post
title: "JSFフレームワーク覚書き"
date: 2004-10-06 14:19
comments: true
categories: [Engineer-Soul]
keywords: "JSF,はじめて,フレームワーク,覚書き,解説,Java,Server,Faces"
tags: []
author: hamasyou
amazon_url: ""
amazon_author: ""
amazon_image: ""
amazon_publisher: ""
---

<p>
[ target="_blank"><img src="http://images-jp.amazon.com/images/P/4822221253.09.MZZZZZZZ.jpg" border="0" />](http://www.amazon.co.jp/exec/obidos/ASIN/4822221253/sorehabooks-22)
</p>

JSFは今後期待されるWebアプリケーションフレームワークの一つです。標準規格ということで、普及してくるのではないかと思います。

JSFはユーザインターフェース周りの処理を簡単に実装できるということを目標に開発されたフレームワークで、WebでGUIアプリケーションを作っているような感覚でコーディングが出来ます。

アクションやイベントといった概念を扱っていて、非常に直感的に流れが分かるようなフレームワークになっています。ということで、今回はJSFフレームワークの流れについての覚書きです。

<section>

<h4>参考</h4>

<ul>
<li>『[ target="_blank" class="extlink">はじめてのJSF](http://www.amazon.co.jp/exec/obidos/ASIN/4822221253/sorehabooks-22)』 日経BP社</li>
<li>[ target="_blank" class="extlink">@IT：特別企画  JavaServer Facesを理解する(前編)](http://www.atmarkit.co.jp/fjava/special/jsf01/jsf01.html)</li>
</ul>

</section>


<!-- more -->

<h2>覚書き</h2>

JSFのライフサイクルの覚書きです。JSFには6つのフェーズがあり、それぞれ役割分担がきっちりされています。

<section>

<h4>参考</h4>

[ target="_balnk" class="extlink">JSFライフサイクルの図 (@IT)](http://www.atmarkit.co.jp/fjava/special/jsf01/jsf01.html#ap03)

</section>

JSFはJavaのWebアプリケーションフレームワークですので、サーブレットとして動作します。javax.faces.webapp.FacesServlet というのがそうです。クライアントのリクエストをサーブレットが受け取ってレスポンスを返すという処理は、通常のWebアプリケーションと一緒です。(当たり前ですが・・・)

JSFはリクエストを受け取って、レスポンスを返すまでのサイクルが6つに分かれています。

<ol><li>[Viewの復元(Reconstitute Request Tree)](#Viewの復元)</li><li>[リクエスト値の適用(Apply Request Values)](#リクエスト値の適用)</li><li>[入力値の検証(Process Validations)](#入力値の検証)</li><li>[モデルの更新(Update Model Values)](#モデルの更新)</li><li>[アプリケーションロジックの呼び出し(Invoke Application)](#アプリケーションロジックの呼び出し)</li><li>[レスポンスのレンダリング(Render Response)](#レスポンスのレンダリング)</li></ol>

<h3 id="Viewの復元">Viewの復元(Reconstitute Request Tree)</h3>

レスポンス時に保存してあったコンポーネントツリーを復元するフェーズです。

初めてのリクエストや、JSF以外のページからのリクエストの場合は、コンポーネントツリーが存在しないので、新しくコンポーネントツリーを生成し、[[レスポンスのレンダリング](#レスポンスのレンダリング)]フェーズに移行する。

このように途中のフェーズを省略して[[レスポンスのレンダリング](#レスポンスのレンダリング)]フェーズに移行するためには、FacesContext#renderResponse メソッドを呼び出す。

<pre>
JSFフレームワークは、UIコンポーネントという単位で画面を作っています。UIコンポーネントとは情報を持った塊のことで、下記のようなコンポーネントがある。

<ul><li>フォーム([ target="_blank">UIForm](http://java.sun.com/j2ee/javaserverfaces/1.1/docs/api/javax/faces/component/UIForm.html))</li><li>入力フィールド([ target="_blank">UIInput](http://java.sun.com/j2ee/javaserverfaces/1.1/docs/api/javax/faces/component/UIInput.html))</li><li>ボタン([ target="_blank">UICommand](http://java.sun.com/j2ee/javaserverfaces/1.1/docs/api/javax/faces/component/UICommand.html))</li></ul></pre>

開発者はUIコンポーネントに対して処理を行っていく。サーバー側ではUIコンポーネントは親子関係を持ったツリーの形で保持される。

<h3 id="リクエスト値の適用">リクエスト値の適用(Apply Request Values)</h3>

クライアントが入力した値を、対応するUIコンポーネントに保存する。

入力フィールド(UIInput)であれば値をコンポーネントに保存し、ボタン(UICommand)であればイベントをキューに追加する。<b>この段階ではモデルには値は反映されない。</b>

編集可能なUIコンポーネント([ target="_balnk" class="extlink">EditableValueHolderインターフェース](http://java.sun.com/j2ee/javaserverfaces/1.1/docs/api/javax/faces/component/EditableValueHolder.html)を実装したコンポーネント、例えばUIInputクラス)はこのフェーズで2種類の処理に分岐される。

<section>

<h4>immediate属性が true の場合</h4>

バリデータを呼び出して入力検証を行う。成功した場合UIコンポーネントに値を保存して、ValueChangeEventをキューにつめる。通常この処理は[[入力値の検証](#入力値の検証)]フェーズで行われる。その後、ValueChangeEventのリスナーや、対応するアクションが呼ばれる。

</section>

<section>

<h4>immediate属性が false の場合(属性がない場合)</h4>

UIコンポーネントに値を保存して、次のフェーズ[[入力値の検証](#入力値の検証)]に移行する。

</section>

<pre>
イベントキューにイベントが追加された場合、次のProcess Eventsが処理される。([ target="_balnk" class="extlink">ライフサイクル図](http://www.atmarkit.co.jp/fjava/special/jsf01/jsf01.html#ap03)を参照) immediate属性が true の場合はこのフェーズでイベントがキューに追加されるため、イベントが処理されて[[レスポンスのレンダリング](#レスポンスのレンダリング)]フェーズに移行する(Render Response)。
</pre>

つまり、この後のフェーズが実行されない。これは、例えば「キャンセル」ボタンや「戻る」ボタンを実装したい場合に使う。[[入力値の検証](#入力値の検証)]が行われては困る場合には immediate属性をコントロールするべし。</div>

<h3 id="入力値の検証">入力値の検証(Process Validations)</h3>

[[リクエスト値の適用](#リクエスト値の適用)]フェーズでUIコンポーネントに適用された値を、バリデータによって検証する。ここでは、画面に表示されるUIコンポーネントのみがチェックされる(rendered属性がtrueのもの)。

<pre>
1. UIコンポーネントに保持されている値を型変換する。型変換エラーが出た場合には [ target="_blank" class="extlink">ConverterException](http://java.sun.com/j2ee/javaserverfaces/1.1/docs/api/javax/faces/convert/ConverterException.html)例外をスローする。
2. 型変換された値に対してバリデータを使って検証する。検証エラーが出た場合には [ target="_blank" class="extlink">ValidatorException](http://java.sun.com/j2ee/javaserverfaces/1.1/docs/api/javax/faces/validator/ValidatorException.html)例外をスローする。
3. 例外が発生した場合には、FacesContextにエラーメッセージを追加して、Render Response処理に移行する。
</pre>

このフェーズでバリデーションエラーが出た場合には、モデルの値は更新されずに下の画面に戻る。

通常であれば、[[Viewの復元](#Viewの復元)]フェーズでリクエスト元のコンポーネントルート(UIViewRoot)が設定されて、[[アプリケーションロジックの呼び出し](#アプリケーションロジックの呼び出し)]フェーズで次の画面のコンポーネントルート(UIViewRoot)が設定されて次の画面に遷移する。

しかし、途中でエラーがでて、次の画面のコンポーネントルート(UIViewRoot)が設定されない場合には、元の画面のコンポーネントルート(UIViewRoot)が残っているために、元の画面に戻ってしまうのである。


<h3 id="モデルの更新">モデルの更新(Update Model Values)</h3>

UIコンポーネントに設定された値を、対応するモデル(JavaBean)にセットする。UIコンポーネントの型からモデルの型に変換できずにエラー([ target="_blank" class="extlink">ConvertException](http://java.sun.com/j2ee/javaserverfaces/1.1/docs/api/javax/faces/convert/ConverterException.html))が発生した場合には、Response Renderが呼ばれてレスポンス処理に入る。

<h3 id="アプリケーションロジックの呼び出し">アプリケーションロジックの呼び出し(Invoke Application)</h3>

ボタンを押したなどのアクションに対してロジックを呼び出します。このロジックの結果によって次の画面遷移先を決定します。

イベントが発生するとイベントのリスナーが呼ばれます。リスナーはaddActionListenerメソッドで追加する方法(アクションリスナー)とsetActionメソッドでセットする方法(アクションハンドラ)がある。フレームワークはアクションリスナーを先に呼び出します。

<pre>
リスナーの設定は基本的にはJSP内でタグを使って設定する。
・アクションリスナーを設定するには、UIInputやUICommandコンポーネントに 〜Listener という属性があるので、そこにメソッド・バインディングという方法を用いて指定する。
 
・アクションハンドラを設定するにはUICommandコンポーネントの action という属性にメソッド・バインディングを使って指定する。
</pre>

<h3 id="レスポンスのレンダリング">レスポンスのレンダリング(Render Response)</h3>

次の画面を構築します。FacesContextに設定されているUIViewRootを使って、次の画面を決定します。

<h2>参考</h2>

 [ target="_blank" class="extlink">JSFのタグ一覧](http://dream.mods.jp/first_jsf/part5.html)
 
 [ target="_blank" class="extlink">@IT：特別企画  JavaServer Facesを理解する(前編)](http://www.atmarkit.co.jp/fjava/special/jsf01/jsf01.html)

+ オライリーから発売中のJSFに関する書籍です。
<div class="rakuten"><table border="0" cellpadding="5" width="400"><tr><td valign="top">[<img src="http://images-jp.amazon.com/images/P/4873111978.09.MZZZZZZZ.jpg"   border="0" />](http://www.amazon.co.jp/exec/obidos/ASIN/4873111978/sorehabooks-22/)</td><td valign="top" />[JavaServer Faces完全ガイド](http://www.amazon.co.jp/exec/obidos/ASIN/4873111978/sorehabooks-22/)<br />ハンス バーグステン, Hans Bergsten, 岩谷 宏<br /><iframe scrolling="no" frameborder="0" width="250" height="40" hspace="0" vspace="0" marginheight="0" marginwidth="0" src="http://xml-jp.amznxslt.com/onca/xml3?dev-t=D2JW5SAFEH7L0B&t=goodpic-22&f=http://www.g-tools.com/xsl/aws-price-ffffff.xsl&locale=jp&type=lite&AsinSearch=4873111978"></iframe><br /><br /><font size="-1"><b>おすすめ平均</b><img src="http://g-images.amazon.com/images/G/01/detail/stars-1-5.gif"   /><br /><img src="http://g-images.amazon.com/images/G/01/detail/stars-1-0.gif"   />非技術者による翻訳？<br /><img src="http://g-images.amazon.com/images/G/01/detail/stars-2-0.gif"   />だめ翻訳<br /></font><br />[ /><font size="-1">Amazonで詳しく見る</font>](http://www.amazon.co.jp/exec/obidos/ASIN/4873111978/sorehabooks-22/)<img src="http://www.g-tools.com/img/spacer.gif"   width="50" height="1" />[ /><img src="http://www.g-tools.com/img/powered-by-gtool.gif"   border="0" alt="4873111978"/>](http://www.goodpic.com/mt/aws/)<br /></td></tr></table>
</div>

+ リファレンス的な使い方も出来る結構情報量の多い本です。
<div class="rakuten"><table border="0" cellpadding="5" width="400"><tr><td valign="top">[<img src="http://images-jp.amazon.com/images/P/4798008303.09.MZZZZZZZ.jpg"   border="0" />](http://www.amazon.co.jp/exec/obidos/ASIN/4798008303/sorehabooks-22/)</td><td valign="top" />[JSF(JavaServer Faces)によるWebアプリケーション開発―Java徹底活用](http://www.amazon.co.jp/exec/obidos/ASIN/4798008303/sorehabooks-22/)<br />川崎 克巳<br /><iframe scrolling="no" frameborder="0" width="250" height="40" hspace="0" vspace="0" marginheight="0" marginwidth="0" src="http://xml-jp.amznxslt.com/onca/xml3?dev-t=D2JW5SAFEH7L0B&t=goodpic-22&f=http://www.g-tools.com/xsl/aws-price-ffffff.xsl&locale=jp&type=lite&AsinSearch=4798008303"></iframe><br /><br /><font size="-1"><b>おすすめ平均</b><img src="http://g-images.amazon.com/images/G/01/detail/stars-5-0.gif"   /><br /><img src="http://g-images.amazon.com/images/G/01/detail/stars-5-0.gif"   />JSF1.1に対応した初めての本<br /></font><br />[ /><font size="-1">Amazonで詳しく見る</font>](http://www.amazon.co.jp/exec/obidos/ASIN/4798008303/sorehabooks-22/)<img src="http://www.g-tools.com/img/spacer.gif"   width="50" height="1" />[ /><img src="http://www.g-tools.com/img/powered-by-gtool.gif"   border="0" alt="4798008303"/>](http://www.goodpic.com/mt/aws/)<br /></td></tr></table>
</div>




