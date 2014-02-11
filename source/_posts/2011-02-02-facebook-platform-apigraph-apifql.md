---
layout: post
title: "Facebook Platform API（Graph API、FQL）"
date: 2011-02-02 19:55
comments: true
categories: [Blog]
keywords: "facebook, Graph API, FQL"
tags: [API,facebook,FQL,Graph]
author: hamasyou
amazon_url: ""
amazon_author: ""
amazon_image: ""
amazon_publisher: ""
---

Facebook Platform の API についてのメモ。間違っているところがあるかも。。。


<!-- more -->

<h2>ソーシャルグラフとは</h2>

ソーシャルグラフとは、簡単に言うと「<strong>人と人とのつながりを表したもの</strong>」です。Facebook はこのソーシャルグラフにアクセスするための API を提供してくれています。

Facebook が提供してくれている API のうち、ソーシャルグラフにアクセスするための API として <strong>Graph API</strong> と <strong>FQL（Facebook Query Language）</strong>、<strong>Legacy APIs</strong> を提供しています。

Legacy APIs は古い API 群で、Graph API に移行しろと Document で書かれているので今後は使わないほうがいいと思います。

<h2>Graph API</h2>

Graph API は、Facebook のソーシャルグラフにアクセスするための API です。Facebook ではソーシャルグラフ上のオブジェクトを次の二つのカテゴリに分けています。

<ol><li>Objects</li><li>Connections</li></ol>

Graph API は、Objects の Connections をたどって別の Objects を取得するような使い方をします。

<a href="http://developers.facebook.com/docs/reference/api/" rel="external nofollow">Graph API - facebook.com</a>

<h3>Objects</h3>

ソーシャルグラフ上のノードにあたるモノを表します。それぞれ属性（プロパティ）をもちます。

<ul>
<li>Users</li>
<li>Pages</li>
<li>Events</li>
<li>Groups</li>
<li>Applications</li>
<li>Status messages</li>
<li>Photos</li>
<li>Photo albums</li>
<li>Profile pictures</li>
<li>Videos</li>
<li>Notes</li>
<li>Checkins</li>
</ul>

<h3>Connections</h3>

Objects を結ぶ関連を表します。いいね！や自分で付けたタグなどがこれに当たります。

<ul>
<li>Friends</li>
<li>News feed</li>
<li>Profile feed (Wall)</li>
<li>Likes</li>
<li>Movies</li>
<li>Music</li>
<li>Books</li>
<li>Notes</li>
<li>Photo Tags</li>
<li>Photo Albums</li>
<li>Video Tags</li>
<li>Video Uploads</li>
<li>Events</li>
<li>Groups</li>
<li>Checkins</li>
</ul>

<h2>FQL（Facebook Query Language）</h2>

FQL はソーシャルグラフを SQL を使って取得できるような API です。FQL でアクセスできるテーブルは <a href="http://developers.facebook.com/docs/reference/fql/" rel="external nofollow">Facebook Query Language (FQL) - Facebook.com</a> を参考にしてみてください。

FQL を使うと、Graph API で取得することが難しかった「<em>この人がいいね！している写真を30件取得する</em>」のようなことができます。

あと、Graph API と FQL は全く別のものとして考えたほうが良いと思います。同じ like を取得するにしても、Graph API で取得する like と FQL の like テーブルを検索するのでは、思っていたのと違う情報が取れてきます。

Graph API の方の like は API で like を POST したデータが入っていて、例えば facebook 内で大学に対していいね！した情報等が入っています。逆に、FQL の like テーブルに入っているデータは、どうやら facebook の外のサイトでいいね！した情報のみが入っていて、facebook 内で大学等にいいね！すると、page_fan というテーブルに情報が入るようです。

FQL は複数クエリを同時に発行してバッチのように使うこともできます。

<h3>FQL を試す環境</h3>

<a href="http://developers.facebook.com/docs/reference/rest/fql.query" rel="external nofollow">fql.query - facebook.com</a>

<h2>FQL の制約</h2>

<ol>
<li>ひとつのテーブルにしかアクセスできない</li>
<li>インデックスが付いたカラムしか検索条件に指定できない（ただし、user_id カラムなど、ログインユーザのID:me() で指定できる場合もある）</li>
<li>検索条件でテーブルのプライマリーキーを使う場合、存在しない値を検索するとエラーになる</li>
</ol>

特に3.はハマりやすいので注意です。次のクエリはエラーになります。

<pre class="code">SELECT url FROM link WHERE link_id IN (SELECT object_id FROM like WHERE user_id = me())</pre>

link テーブルの link_id は identifier なので、IN で戻ってくる object_id がすべて存在していないといけないのです。結構ハマりやすいので注意！

<section>

<h3>参考</h3>

<a href="http://www.madin.jp/diary/?date=20101214" rel="external nofollow">[Facebook]Facebook アプリ作ろうぜ (その7 Like の取得が面倒な件について、あと FQL について) - コーヒーサーバは香炉である</a>

</section>





