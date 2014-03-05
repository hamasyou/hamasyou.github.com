---
layout: post
title: "ムームードメイン+GitHub Pagesで独自ドメインを使う方法"
date: 2014-03-05 12:02:07 +0900
comments: true
categories: [Blog]
keywords: "ムームードメイン, muumuu-domain,GitHub,Pages,独自ドメイン,naked domain"
tags: [GitHub,domain,DNS,ムームードメイン]
sharing: true
published: true
amazon_url: ""
amazon_author: ""
amazon_image: ""
amazon_publisher: ""
og_image: ""
---

GitHub Pages を Naked Domain（サブドメインをつけない hamasyou.com みたいなやつ）で運用する場合の手順です。

<span class="small">GitHub Pages をサブドメイン（www.hamasyou.com みたいなやつ）で運用する場合は、DNS サービスの **CNAME** を **hamasyou.github.io** に設定すれば問題ないのでこの記事は読む必要がありません。</span>

hamasyou.com はムームードメインで取得していますので、ムームードメインを元に説明します。が、ムームードメインに限らず、Apex Alias を設定できない DNS サービスを使うときの参考にしてください。
なんでこんな事をメモするかというと、GitHub Pages を独自ドメインで運用する際に Apex Alias を使わないと **302 Found** のステータスコードが返ってきて、SEO 的に嫌なのと *Facebook のシェアで404 Not Found が出てしまう* からです。

[Fix 302 Redirect Response For GitHub Hosted Site](http://subosito.com/fix-302-redirect-response-for-github-hosted-site)

別に 302 Found がでても構わないっていうひとは GitHub Pages のヘルプを参考にしてやってみてください。

[Setting up a custom domain with Pages - GitHub Help](https://help.github.com/articles/setting-up-a-custom-domain-with-pages)

<!-- more -->

### やりたいこと

やりたいことの整理です。

- GitHub Pages を hamasyou.com で運用する
- www.hamasyou.com を hamasyou.com の CNAME に設定する

hamasyou.com の A レコードを GitHub Pages の IP に設定するだけだと CDN が効かないし、ステータスコードが302で返ってくることがあるのでちゃんと200で返して欲しい。



### 1. 独自ドメインを取得する

まず、独自ドメインを持っていない場合はドメインを取得しましょう。
僕はすでにムームードメインでドメインを取得していたのでアレですが、もしこれからドメインを取るなら、Apex Alias が使えるサービスで取ったほうがいいかもしれません。
そのほうが幸せになれるかも。


### 2. Apex Alias を設定する

ムームードメインのように、Apex Alias も使えないし、Naked Domain に CNAME を割り当てられないサービスの場合は、レンタル DNS サービスを利用します。
**Gehirn DNS** がドメイン2つまで無料で使えるので、今回はここを利用しました。

[Gehirn DNS](http://www.gehirn.jp/dns.html)

アカウント登録をして、ドメインの認証をまずは行います。TXT レコードを使って認証していきます。出てくるダイアログをよく読めば出来ると思います。

[独自ドメインを追加する方法 - Gehirn DNS](http://support.gehirn.jp/manual/dns/domain/)

次に、Apex Alias を登録します。手順は次のサイトを見れば分かると思います。

[Gehirn DNS、ホスト名無しのドメインにエイリアス機能をサポート - ゲヒルンニュース](http://news.gehirn.jp/information/325/)

コントロールパネルで CNAME も登録します。www.hamasyou.com を hamasyou.com に向けたいので、そのように設定します。
レコードの登録のさいは、末尾に `.` が付くので忘れずに。

![登録後01](/images/2014-03-05-github-pages-custom-domain-01.png)

![登録後02](/images/2014-03-05-github-pages-custom-domain-02.png)

Gehirn での設定は以上です。つぎは、ムームードメインのネームサーバの設定をします。


### 3. ネームサーバを変更する

ムームードメインの管理画面に入って、「ネームサーバ設定」で「弊社サービス以外のネームサーバ」を選びます。
ここで、Gehirn のネームサーバの情報を入力します。

![ネームサーバの設定](/images/2014-03-05-github-pages-custom-domain-03.png)

これで、hamasyou.com の DNS レコードが Gehirn のネームサーバで参照されて、Alias レコードで hamasyou.github.io を見に行くようになります。
Apex Alias を設定することで、GitHub Pages の CDN が利用出来るようになるので、参照のパフォーマンスもちょっとはあがるはず！らしいです。
