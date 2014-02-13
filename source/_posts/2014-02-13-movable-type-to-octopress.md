---
layout: post
title: "Movable TypeからGitHub+Octopressに乗り換える手順"
date: 2014-02-13 10:53:07 +0900
comments: true
categories: [Blog]
keywords: "Movable Type,GitHub,Octopress,Blog,Liquid,Jekyll,Ruby"
tags: [Movable Type,GitHub,Octopress,Liquid,Jekyll,Ruby]
sharing: true
published: true
amazon_url: ""
amazon_author: ""
amazon_image: ""
amazon_publisher: ""
og_image: ""
---

今まではブログを *ロリポップ* + *Movable Type* でやっていましたが、ブログデザインを変更したくなり、ついでにブログ環境も変更しました。

Movable Type から *WordPress* に乗り換えても良かったんですが、エンジニアっぽいブログ環境ってなんだろうと考えた末、**GitHub Pages** と **Octopress** を使ってブログ環境を作ることにしました。

[Octopress](http://octopress.org/) は [Jekyll](http://jekyllrb.com/) を使ってブログ環境を作るためのフレームワークで、Jekyll はブログのような静的サイトを作るための Ruby のファイルジェネレータになります。

[GitHub Pages](http://pages.github.com/) と組み合わせると無料でブログを始めることができます。

移行でおこなったことの手順です。

<!-- more -->

## 手順

1. 公開用のサーバ設定
    1. GitHub にリポジトリを作成する
    1. Octopress をチェックアウトする
    1. deploy する
1. ローカルのブログ環境の設定
    1. ブログの初期設定をおこなう
    1. テーマを変更する
    1. 最初の記事を書いてみる
1. 環境をバックアップする
    1. BitBucket にリポジトリを作成する
    1. BitBucket にソースをプッシュする
1. プラグインをつかう
    1. 記事にタグをつける


## 公開用のサーバ設定

### GitHub にリポジトリを作成する

まず最初に GitHub にサイト公開用のリポジトリを作成します。

[GitHub](https://github.com/) にログインして、リポジトリを作成します。リポジトリ名は **username.github.io** とします。`username` の部分を自分のアカウント名に読みかえてください。僕の場合は hamasyou.github.io になります。

このリポジトリが公開用のサイトになり、<http://username.github.io/> が URL になります。

![リポジトリ作成](/images/2014-02-13-movable-type-to-octopress-01.png)

### Octopress をチェックアウトする

つぎに、ブログ環境を作成するために [Octopress](http://pages.github.com/) を github からチェックアウトします。`username` のところは自分のアカウント名です。

{% terminal %}
$ git clone git@github.com:imathis/octopress.git username.github.io
Cloning into 'username.github.io'...
remote: Reusing existing pack: 10340, done.
remote: Counting objects: 20, done.
remote: Compressing objects: 100% (20/20), done.
remote: Total 10360 (delta 4), reused 3 (delta 0)
Receiving objects: 100% (10360/10360), 2.73 MiB | 785.00 KiB/s, done.
Resolving deltas: 100% (4949/4949), done.
Checking connectivity... done.

$ cd username.github.io
{% endterminal %}

### deploy する

Octopress の初期設定を行い、deploy を行います。この時点でデプロイしたくない場合は最後のコマンド `bundle exec rake gen_deploy` を実行しないようにします。

{% terminal %}
$ bundle install --path vendor/bundle
$ bundle exec rake install
$ bundle exec rake setup_github_pages
Enter the read/write url for your repository
(For example, 'git@github.com:your_username/your_username.github.io.git)
           or 'https://github.com/your_username/your_username.github.io')
Repository url: <kbd>git@github.com:username/username.github.io.git<kbd>
$ bundle exec rake gen_deploy
{% endterminal %}

## ブログの初期設定を行う

Octopress のブログ設定値は `_config.yml` というファイルになります。[Configuring Octopress](http://octopress.org/docs/configuring/) を参考に変更します。

### テーマを変更する

デフォルトテーマでも十分カッコいいですが、テーマ変更もできます。Octopress のテーマは [3rd Party Octopress Themes](https://github.com/imathis/octopress/wiki/3rd-Party-Octopress-Themes) にまとまっているので、よさそうなテーマを選んでインストールします。

テーマのインストールは、`.themes` ディレクトリにテーマのリポジトリをチェックアウトして `bundle exec rake install['theme-name']` でおこないます。

{% terminal %}
$ git clone git@github.com:kAworu/octostrap3.git .themes/octostrap3
$ bundle exec rake install['octostrap3']
{% endterminal %}

[![Octopress-Themes](/images/2014-02-13-movable-type-to-octopress-02.png)](http://opthemes.com/)

### あたらしい記事を作成する

新しい記事を書くには `bundle exec rake new_post['title']` とします。`title` 部分が記事のファイル名に使われるので、この時点では英語で入力するほうがよいです。

{% terminal %}
$ bundle exec rake new_post['movable-type-to-octopress']
mkdir -p source/_posts
Creating new post: source/_posts/2014-02-13-movable-type-to-octopress.markdown
{% endterminal %}


    ---
    layout: post
    title: "movable-type-to-octopress"
    date: 2014-02-13 13:18:04 +0900
    comments: true
    categories:
    ---

記事のタイトルに日本語を使いたいときはファイル中の `title` 部分を書き換えます。また、カテゴリやタグ、ページのキーワードなどもここで変更できます。

    ---
    layout: post
    title: "Movable TypeからGitHub+Octopressに乗り換える手順"
    date: 2014-02-13 13:18:04 +0900
    comments: true
    categories: [Blog]
    keywords: "Movable Type,GitHub,Octopress,Blog,Liquid,Jekyll,Ruby"
    tags: [Movable Type,GitHub,Octopress,Liquid,Jekyll,Ruby]
    ---

## BitBucket にソースをコミットする

GitHub もリポジトリサービスですが、現在は Public なリポジトリしか無料で作ることができません。そこで、**BitBucket** という別のリポジトリサービスを利用して環境をまるごとバックアップします。

[![BitBucket](http://upload.wikimedia.org/wikipedia/en/f/fc/Bitbucket_Logo.png)](https://bitbucket.org/)

BitBucket は Private なリポジトリが作成できるので、ブログ環境は BitBucket で管理して、サイトデータだけを GitHub にデプロイするようにします。

### リポジトリを作成する

BitBucket にリポジトリを作成する方法は、GitHub とほとんど変わりません。こちらもリポジトリ名を `username.github.io` にしておきます。

![BitBucket リポジトリ作成](/images/2014-02-13-movable-type-to-octopress-03.png)

リポジトリを作成したら、ソースコードをプッシュします。

{% terminal %}
$ git remote add bitbucket git@bitbucket.org:username/username.github.io.git
$ git add .
$ git commit -m 'init'
$ git push bitbucket source
{% endterminal %}


## その他

### 記事にタグをつけられるようにする

Octopress にはいろいろなプラグインがあります。自作することも簡単にできます。デフォルトではタグというものが Octopress にはありませんが、カテゴリ機能を参考にすると簡単にタグを管理できるようになります。

[Octopressでのタグ管理](http://rcmdnk.github.io/blog/2013/04/12/blog-octopress/)

### 独自ドメインで運用する

[Setting up a custom domain with Pages](https://help.github.com/articles/setting-up-a-custom-domain-with-pages) の手順でできます。

要約すると、`CNAME` というファイルを作成して、メインのサイトドメインを登録する。

```plain source/CNAME
hamasyou.com
```

独自ドメインを取ったら、DNS の設定で A レコードを `192.30.252.153` と `192.30.252.154` に向けます。いろいろなサイトで `204.232.175.78` のアドレスを設定するように書かれていますが、変わったっぽい？ですね。不安なら、A レコード3つ登録すればいいと思います。

    hamasyou.com A 204.232.175.78
    hamasyou.com A 192.30.252.153
    hamasyou.com A 192.30.252.154

これで、時間がたてば独自ドメイン名で GitHub のページが表示されるようになります。

### Markdown で `Definition LIST` を使えるようにする

Octopress の使っている Markdown パーサの rdiscount のバージョンを上げます。2.1.7 からデフォルトで *Definition LIST（dl, dt, dd）* の拡張記法が使えるようになっているのでアップデート推奨です。

`Gemfile` を開いて `gem 'rdiscount', '~> 2.0.7'` を `gem 'rdiscount'` に書き換えます。

    - gem 'rdiscount', '~> 2.0.7'
    + gem 'rdiscount

bundle update します。

{% terminal %}
$ bundle update rdiscount
{% endterminal %}

これで Markdown で Definition LIST がデフォルトで使えるようになります。

    これがキーワード
    : キーワードの説明

    <dl>
      <dt>これがキーワード</dt>
      <dd>キーワードの説明</dd>
    </dl>

### Movable Type の記事を Markdown に変換する

Movable Type の記事エクスポートを Markdown 形式に変換するコードを見つけたので、移行にはこれを使いました。ちょっと手はいれましたが。

{% gist 2351046 mt_to_markdown.rb %}
