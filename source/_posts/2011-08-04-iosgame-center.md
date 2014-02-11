---
layout: post
title: "[iOS] Game Center を使った対戦プログラミングの手順メモ"
date: 2011-08-04 11:38
comments: true
categories: [Programming]
keywords: "iOS, Game Center, Game Kit"
tags: [iOS,iPhone]
author: hamasyou
amazon_url: ""
amazon_author: ""
amazon_image: ""
amazon_publisher: ""
---

iOS 4.1 から、正式に <em>Game Center</em> が使えるようになりました。Game Center を使うと、ゲームに簡単に対戦やスコア、達成目標などの機能を追加できます。

{% blockquote 『Game Kit プログラミングガイド』より %}
Game Centerは、プレー中のゲームについての情報をプレーヤー同士で共有したり、ほかのプレー ヤーと一緒にマルチプレーヤー対戦に参加できるソーシャルゲーミングサービスです。Game Center は、ワイヤレスネットワークと携帯電話ネットワークのどちらからでもサービスを提供できます。 Game Centerの主な機能は、次の通りです。

<ul><li><em>認証</em> - プレーヤーはGame Centerでセキュアなアカウントを作成し、iOSベースのデバイスでGame Centerにアクセスできます。</li>
<li><em>友だち</em> - プレーヤーはGame Centerのほかのプレーヤーを友だちとして登録できます。友だちは、最近遊んだゲームなどプレーヤーの詳細情報を確認できます。</li>
<li><em>Leaderboard</em> - プレーヤーのスコアをGame Centerに記録したりGame Centerから取得したりできます。
<li><em>アチーブメント(Achievement、成績)</em> - そのゲームでのプレーヤーのアチーブメントを管理します。アチーブメントは、Game Centerサービスに記録され、Game Centerアプリケーションとゲームの中で閲覧できます。</li>
<li><em>オートマッチ</em> - Game Centerを介して複数のプレーヤーとつながるネットワークゲームを作成できます。プレーヤーは友だちを招待したり、まだ会ったことのないプレーヤーと接続して対戦できます。プレーヤーは、ゲームを実行していないときでも対戦への招待を受信できます。その場合、ゲームが自動的に起動し、招待が処理されます。</li>
<li><em>ボイス</em> - 対戦に接続されたプレーヤー間で音声通信を行うことができます。</li></ul>


{% endblockquote %}

Game Center を使うためのプログラミングは形式的ながら、視点が二つ（自分と対戦相手）あるため、どの機能が何に当たるのかを理解していないと混乱しがちです。

本記事は、自分で Game Center を使ったアプリを作ったときの、メモをまとめたものです。

<section>

<h4>開発時の環境</h4>

<dl><dt>iOS SDK バージョン</dt><dd>4.3</dd>
<dt>クライアント iOS バージョン</dt><dd>自分: iOS 4.3.2 &nbsp;&nbsp;対戦相手: iOS 4.3.5</dd></dl>

</section>


<!-- more -->

<script type="text/javascript">$(document).ready(function() { create_toc(); });</script>

<h2>Game Center を使った対戦機能開発のメモ</h2>

Game Center を使ったアプリの開発手順は、<a href="http://developer.apple.com/jp/devcenter/ios/library/documentation/GameKit_Guide.pdf" rel="external nofollow">『Game Kit プログラミングガイド』</a> - Apple Developer（PDF）に詳しく載っています。わかりやすい説明で読めば大体理解できると思います。対戦相手とのマッチングの部分だけ、自分と相手がいるのと、非同期で処理がされていくためどのメソッドがどの役割かが最初は混乱するかもしれません。

Leaderboard とアチーブメントはたぶん、そんなに混乱しないはず。

<h2>用語のまとめ</h2>

<dl>
<dt>プレイヤー</dt>
<dd>
<p>Game Center でプレイヤーとは、<strong>GKPlayer</strong> クラスで表される Game Center で認証されたユーザのことを指します。GKPlayer インスタンスの <em>alias</em> プロパティでプレイヤーのニックネームを取得できます。</p>

<p>端末上で認証したプレイヤーのこと（自分自身）を特に <strong>GKLocalPlayer</strong> クラスで表します。GKLocalPlayer からは友だちの一覧などの情報を取得できます。</p>
</dd>
<dt>マッチメイク</dt>
<dd>
<p>対戦相手を探す機能のことです。対戦相手は友だちへのゲーム招待（相手に PUSH 機能で通知がいく）と自動マッチングで探すことができます。</p>

<p>当然ですが、一つのアプリで、招待機能（<em>Request</em>）と招待受理機能（<em>Invite</em>）を実装する必要があります。</p>
</dd>
<dt>GKMatchRequest</dt>
<dd><p>対戦相手を探す際の対戦要求を表すクラス。対戦相手の人数など、細かな条件を設定できます。</p></dd>
<dt>GKInvite</dt>
<dd><p>対戦要求によって招待されたことをあわらすクラスです。</p></dd>
<dt>GKMatch</dt>
<dd><p>マッチングされたネットワークセッションを表すクラスです。このオブジェクトに対してデータを送ることで、通信相手にデータが届きます。</p></dd>
</dl>

<h2>Game Center を使ったプログラミングの手順</h2>

Game Center を使ったプログラミングの流れは次のようになります。

<ol>
<li>Game Center が利用可能かどうかを確認する。一度確認すればよい。[<a href="#title4" rel="external nofollow">コード</a>]</li>
<li>アプリケーション起動時に、できるだけはやく Game Center への認証を済ませる。これは、その後のゲーム招待を処理するために必要。[<a href="#title5" rel="external nofollow">コード</a>]</li>
<li>認証が済んだら、ゲーム招待を処理するハンドラを登録する。<em>ゲーム招待を受理してアプリが起動された場合はアプリのトップ画面が表示されるので、アプリのトップで Game Center の認証を行い、続けてゲーム招待を処理するハンドラを登録するべき。</em>そうしないと、せっかくの招待が無駄になる。[<a href="#title6" rel="external nofollow">コード</a>]</li>
<li>自分で対戦要求を作成する場合は、<strong>GKMatchRequest</strong> を作成してマッチメイク画面を開く。招待された側は、ゲーム招待処理ハンドラが呼び出されて、ゲーム招待（<strong>GKInvite</strong>）を利用してマッチメイク画面を開く。[<a href="#title7" rel="external nofollow">コード</a>]</li>
<li>自分で対戦相手を探す場合は、対戦要求を作成する。[<a href="#title8" rel="external nofollow">コード</a>]</li>
<li>対戦相手が決まったら、コールバックメソッドを実装して対戦を開始する。[<a href="#title9" rel="external nofollow">コード</a>]</li>
<li>対戦相手を自動で決める場合は <strong>GKMatchmaker</strong> の findMatchForRequest メソッドを利用する。（後で書く）</li>
<li>対戦が開始されたら、<strong>GKMatch</strong> に対してデータを送ることで通信ができる。[<a href="#title10" rel="external nofollow">コード</a>]</li>
<li>通信を受け取るには、<strong>GKMatchDelegate</strong> プロトコルを実装し、GKMatch の delegate に設定する。[<a href="#title11" rel="external nofollow">コード</a>]</li>
</ol>

<h2>プログラミングコードの断片</h2>

<h3>Game Center が利用出来るかどうか</h3>

SDK が Game Center に対応しているか（GKLocalPlayer クラスが参照できるかで確認）と iOS が 4.1 以降であるかを確認する。

Game Center が利用出来るかどうかは、最初の一度だけ行えば良い。すなわち、メンバ変数にキャッシュできる。以降、gameCenterAvailable 変数がそれ。

<pre class="code">- (<span class="keyword">BOOL</span>)isGameCenterAvailable
{
  <span class="rem">// Test for Game Center availability</span>
  <span class="class">Class</span> gameKitLocalPlayerClass = NSClassFromString(<span class="str">@&quot;GKLocalPlayer&quot;</span>);
  <span class="keyword">BOOL</span> localPlayerAvailable = (gameKitLocalPlayerClass != <span class="keyword">nil</span>);
  
  <span class="rem">// Test if device is running iOS 4.1 or higher</span>
  <span class="class">NSString</span> *requireSysVer = <span class="str">@&quot;4.1&quot;</span>;
  <span class="class">NSString</span> *currentSysVer = [[<span class="class">UIDevice</span> currentDevice] systemVersion];
  <span class="keyword">BOOL</span> isOSVer41 = ([currentSysVer compare:requireSysVer options:NSNumericSearch] != NSOrderedAscending);
  
  <span class="keyword">return</span> localPlayerAvailable &amp;&amp; isOSVer41;
}</pre>

<h3>Game Center の認証</h3>

authenticateWithCompletionHandler メソッドで Game Center を使って認証できる。認証済みでない場合は、認証ダイアログが表示される。

認証が済んだら、できるだけはやくゲーム招待を処理するためのハンドラを登録する。

<pre class="code">- (<span class="keyword">void</span>)authenticateLocalPlayer 
{
  <span class="keyword">if</span> (gameCenterAvailable) {
    <span class="class">GKLocalPlayer</span> *localPlayer = [<span class="class">GKLocalPlayer</span> localPlayer];
    <span class="keyword">if</span> (!localPlayer.authenticated) {
      [localPlayer authenticateWithCompletionHandler:^(<span class="class">NSError</span> *error) {
        <span class="keyword">self</span>.error = error
        
        <span class="keyword">if</span> (error == <span class="keyword">nil</span>) {
          <span class="rem">// ゲーム招待を処理するためのハンドラを設定する</span>
          [<span class="keyword">self</span> initMatchInviteHandler];
        }
      }];
    }
  }
}</pre>

<h3>ゲーム招待を処理するハンドラを登録する</h3>

<strong>GKMatchmaker</strong> の inviteHandler にブロックを渡すことで、招待が処理された際にコールバックされるようになる。

{% blockquote 『Game Kit プログラミングガイド』より %}
<ul><li>acceptedInviteパラメータは、ゲームが別のプレーヤーから直接招待を受け取るとnil以外の値になります。この場合は、ほかのプレーヤーのゲームがすでに対戦要求を作成しています。 そのため、招待された側のデバイスで実行しているアプリケーションで対戦要求を作成する必要はありません。</li>
<li>playersToInviteパラメータは、対戦をホストするGame Centerアプリケーションから直接ゲームが起動されるとnil以外の値になります。このパラメータは、ゲームが対戦に招待すべきプレーヤーを示したプレーヤー識別子の配列を保持します。ゲームは新しい対戦要求を作成し、 通常通りにパラメータを割り当ててから、対戦要求のplayersToInviteプロパティをplayersToInviteパラメータで渡された値に設定する必要があります。マッチメーク画面が表示されると、すでに対戦に参加しているプレーヤーのリストがあらかじめ読み込まれます。</li>
</ul>


{% endblockquote %}

<pre class="code">- (<span class="keyword">void</span>)initMatchInviteHandler
{
    <span class="keyword">if</span> (gameCenterAvailable) {
    [<span class="class">GKMatchmaker</span> sharedMatchmaker].inviteHandler = ^(<span class="class">GKInvite</span> *acceptedInvite, <span class="class">NSArray</span> *playersToInvite) {
      <span class="rem">// 既存のマッチングを破棄する</span>
      <span class="keyword">self</span>.currentMatch = <span class="keyword">nil</span>;
      
      <span class="keyword">if</span> (acceptedInvite) {
        <span class="rem">// ゲーム招待を利用してマッチメイク画面を開く</span>
        [<span class="keyword">self</span> showMatchmakerWithInvite:acceptedInvite];
      } <span class="keyword">else</span> <span class="keyword">if</span> (playersToInvite) {
        <span class="rem">// 招待するユーザを指定してマッチメイク要求を作成する</span>
        <span class="class">GKMatchRequest</span> *request = [[[<span class="class">GKMatchRequest</span> alloc] init] autorelease];
        request.minPlayers = <span class="num">2</span>;
        request.maxPlayers = <span class="num">2</span>;
        request.playersToInvite = playersToInvite;
        
        [<span class="keyword">self</span> showMatchmakerWithRequest:request];
      }
    };
  }
}</pre>

<h3>マッチメイク画面を開く</h3>

マッチメイク画面を開くには、ViewController が必要。マッチメイク要求（Request）かゲーム招待（Invite）かどちらかを使ってマッチメイク画面を開く。

インターフェースはほとんど同じになる。

<pre class="code">- (<span class="keyword">void</span>)showMatchmakerWithRequest:(<span class="class">GKMatchRequest</span> *)request
{
  <span class="class">GKMatchmakerViewController</span> *viewController = [[[<span class="class">GKMatchmakerViewController</span> alloc] initWithMatchRequest:request] autorelease];
  viewController.matchmakerDelegate = <span class="keyword">self</span>;
  [<span class="keyword">self</span> presentModalViewController:viewController animated:<span class="keyword">YES</span>];
}
 
- (<span class="keyword">void</span>)showMatchmakerWithInvite:(<span class="class">GKInvite</span> *)invite
{
  <span class="class">GKMatchmakerViewController</span> *viewController = [[[<span class="class">GKMatchmakerViewController</span> alloc] initWithInvite:invite] autorelease];
  viewController.matchmakerDelegate = <span class="keyword">self</span>;
  [<span class="keyword">self</span> presentModalViewController:viewController animated:<span class="keyword">YES</span>];
}</pre>

<h3>自分で対戦要求を作成する場合</h3>

自分で対戦要求を作成する場合は、<strong>GKMatchRequest</strong> を作って、マッチメイク画面を表示する。

対戦要求は任意のタイミングで作成すればよい。例えば、メニューで「ふたりで対戦する」ボタンが押されたとき等。

<pre class="code">- (<span class="keyword">void</span>)requestMatch
{
  <span class="class">GKLocalPlayer</span> *localPlayer = [<span class="class">GKLocalPlayer</span> localPlayer];
  <span class="keyword">if</span> (localPlayer.authenticated) {
    <span class="rem">// 対戦相手を決める</span>
    <span class="class">GKMatchRequest</span> *request = [[[<span class="class">GKMatchRequest</span> alloc] init] autorelease];
    request.minPlayers = <span class="num">2</span>;
    request.maxPlayers = <span class="num">2</span>;
    
    [<span class="keyword">self</span> showMatchmakerWithRequest:request];
  }
}</pre>

<h3>対戦相手が決まった際に呼び出されるコールバック</h3>

対戦相手が決まるたびに、呼び出されるコールバックメソッド。これは <strong>GKMatchmakerViewController</strong> の matchmakerDelegate を経由して呼び出される。

コールバックを受け取るには、<strong>GKMatchmakerViewControllerDelegate</strong> プロトコルを実装している必要がある。

matchStarted はゲームがスタートしているかどうかを表すメンバ変数。<em>match.expectedPlayerCount</em> は対戦要求にあと何人の必要プレイヤーがいるかを表す値。0になれば、必要なプレイヤーが揃ったことになる。

<pre class="code">- (<span class="keyword">void</span>)matchmakerViewController:(<span class="class">GKMatchmakerViewController</span> *)viewController didFindMatch:(<span class="class">GKMatch</span> *)match
{
  [<span class="keyword">self</span> dismissModalViewController];
  <span class="keyword">self</span>.currentMatch = match;
  
  <span class="rem">// 全ユーザが揃ったかどうか</span>
  <span class="keyword">if</span> (!matchStarted &amp;&amp; match.expectedPlayerCount == <span class="num">0</span>) {
    matchStarted = <span style="keyword">YES</span>;
    <span class="rem">// ゲーム開始の処理</span>
  }
}</pre>

<h3>対戦相手にデータを送る</h3>

対戦相手がきまり、現在の通信セッションを表す <strong>GKMatch</strong> が取得できたら、GKMatch に対して sendDataToAllPlayers メソッドを呼び出してデータを送信する。

これによって対戦相手にデータが送られる。

GKMatchSendDataUnreliable モードは、いわゆる UDP で送信するイメージ。GKMatchSendDataReliable モードは TCP で送るイメージ。通常は GKMatchSendDataUnreliable でよいはず。

データの内容や型は、アプリ側で好きに決めることができる。ほとんどの場合、カスタムの構造体を作成してデータを送信する。構造体を作れば、sizeof(myData) でバイトサイズを取得できる。

<pre class="code">- (<span class="keyword">void</span>)sendDataToAllPlayers:(<span class="keyword">void</span> *)data sizeInBytes:(<span class="class">NSUInteger</span>)sizeInBytes
{
  <span class="keyword">if</span> (gameCenterAvailable) {
    <span class="class">NSError</span> *error = <span class="keyword">nil</span>;
    <span class="class">NSData</span> *packetData = [<span class="class">NSData</span> dataWithBytes:data length:sizeInBytes];
    [currentMatch sendDataToAllPlayers:packetData withDataMode:GKMatchSendDataUnreliable error:&amp;error];
    <span class="keyword">self</span>.error = error;
  }
}</pre>

<h3>対戦相手からデータを受け取る</h3>

対戦相手からデータを受け取るには、GKMatch のデリゲートに GKMatchDelegate を実装したインスタンスを割り当てます。

対戦相手からデータを受け取った場合には、次のコールバックメソッドが呼び出されます。

<pre class="code">- (<span class="keyword">void</span>)match:(<span class="class">GKMatch</span> *)match didReceiveData:(<span class="class">NSData</span> *)data fromPlayer:(<span class="class">NSString</span> *)playerID
{
  <span class="rem">// データを受け取ってアプリで利用する</span>
}</pre>

<h2>対戦機能をテストする方法</h2>

Game Center の機能をテストするために、Apple はサンドボックス環境を用意してくれています。サンドボックス環境は、シミュレータ上でも実機でもどちらでも使うことができます。

ただし、<strong>シミュレータ上ではマッチメイクの招待の送受信はできません。</strong>マッチメイクのテストをするには、実機を二台用意する必要があります。

実機を二台用意したら次の手順でテストできます。

<ol>
<li>iTunes Connect にアプリを登録して、Game Center 機能を有効にする。<a href="https://itunesconnect.apple.com/docs/iTunesConnect_DeveloperGuide_JP.pdf" rel="external nofollow">『iTuens Connect デベロッパガイド』</a>参照</li>
<li>実機を二台用意する。</li>
<li>それぞれの実機にテストアプリをインストールする。（Xcode 上でデバッグビルドで起動すれば入る）</li>
<li>サンドボックス環境にスイッチするために、テストアプリ内から認証機能を実行する。</li>
<li>アプリを閉じて、Game Center アプリを立ち上げ、サンドボックス環境であることを確認して友だちを招待する</li>
<li>もう一台の実機のほうでもサンドボックス環境にスイッチし、Game Center アプリを開き友だちリクエストを承認する</li>
<li>アプリに戻り、マッチメイク機能をテストする</li>
</ol>

<p class="option">マッチメイクの招待には PUSH 配信を利用するので、実機で Game Center の通知機能をオンするのを忘れずに。</p>

<h3>サンドボックス環境にスイッチする</h3>

もし、すでに Game Center にサインインしている場合は、一度 Game Center アプリを起動して、サインアウトしておきます。

アプリを立ち上げて、認証機能を実行すると、下の図のようなダイアログが表示されます。

<img alt="Game Center 認証画面" src="http://hamasyou.com/blog/archives/images/%E5%86%99%E7%9C%9F.PNG" width="320" height="480" class="mt-image-none" style="" />

Game Center のアカウントを持っていない場合は、ここで開発用のアカウントを作成します。すでに持っている場合は、Use Existing Account を選択して、既存のアカウントでサインインします。

サンドボックス環境と通常の環境とのスイッチは、開発中のアプリケーションでサインインしたかどうかで決まります。アカウントは同じものを使えます。

サンドボックス環境にスイッチできたかどうかは、一度アプリを終了させ、Game Center アプリを起動することで確認できます。

<img alt="GameCenterサンドボックス" src="http://hamasyou.com/blog/archives/images/%E5%86%99%E7%9C%9F%20%281%29.PNG" width="320" height="480" class="mt-image-none" style="" />

<h3>サンドボックス環境で友だちを招待する</h3>

サンドボックス環境の Game Center アプリから、友だちリクエストを送ります。このリクエストは、サンドボックス環境内のアカウントに対して送信されるため、 相手もサンドボックス環境にスイッチする必要があります。

二台目の実機のほうもサンドボックス環境にスイッチして、Game Center アプリを起動すると、リクエストタブに友だちリクエストが着ているはずです。

<h3>サンドボックス環境でマッチメイク機能をテストする</h3>

二台ともサンドボックス環境にスイッチできたら、ゲーム内から友だちを招待して対戦機能をテストできます。

下の図は、<a href="#title8" rel="external nofollow">requestMatch</a> メソッドを呼び出した時の画面です。

<img alt="Game Center マッチメイク画面" src="http://hamasyou.com/blog/archives/images/%E5%86%99%E7%9C%9F%20%282%29.PNG" width="320" height="480" class="mt-image-none" style="" />

<h2>cocos2d 本おすすめ</h2>

<div class="bookInfo"> 
<div class="bookImg"> 
<a href="http://www.amazon.co.jp/gp/product/4844330411?ie=UTF8&tag=sorehabooks-22&linkCode=xm2&camp=247&creativeASIN=4844330411" rel="external nofollow"></a> 
</div> 
<ul><li><a href="http://www.amazon.co.jp/gp/product/4844330411?ie=UTF8&tag=sorehabooks-22&linkCode=xm2&camp=247&creativeASIN=4844330411" rel="external nofollow"></a></li><li>Steffen Itterheim (著), 畑 圭輔 (監修), 坂本 一樹 (監修), 加藤 寛人 (監修), 高丘 知央 (監修), 株式会社 クイープ (翻訳) </li><li>インプレスジャパン</li></ul> 
<div class="clear"></div> 
</div>

cocos2d は Objective-C で書かれたゲームエンジン、フレームワークです。2D ゲームを作成するに当たっての結構簡単にわかりやすい機能を提供してくれます。

cocos2d は最近 Version 1.0 が正式にリリースされました。<em>OpenGL ES</em> のコードを隠していながら、UIViewController も提供してくれるようになったため、Game Center を使ったアプリともスムーズに連携できるようになりました。

また、『<a href="http://www.amazon.co.jp/gp/product/4844330411?ie=UTF8&tag=sorehabooks-22&linkCode=xm2&camp=247&creativeASIN=4844330411" rel="external nofollow">cocos2dで作る iPhone＆iPadゲームプログラミング</a>』の中にも1章まるまるつかって、Game Center を使ったプログラミングの解説がされています。

おすすめの一冊です。




