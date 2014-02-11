---
layout: post
title: "HerokuでRuby1.9とRails3を使う"
date: 2010-06-02 10:44
comments: true
categories: [Engineer-Soul]
keywords: "Ruby, Ruby on Rails, Heroku"
tags: [Rails3,Ruby1.9]
author: hamasyou
amazon_url: ""
amazon_author: ""
amazon_image: ""
amazon_publisher: ""
---

<p id="heroku-logo" style="background-color: #29264d;">
[ style="display: block; text-indent: -9999px; width: 177px; height: 56px; overflow: hidden; background-image: url(http://nav.heroku.com/images/logos/logo.png); background-repeat: no-repeat;">Heroku](http://heroku.com)
</p>

[ target="_blank" class="extlink">Heroku](http://heroku.com/) はRubyで作成したWebアプリケーションをホスティングしてくれるサービスで、Ruby On Railsを無料で動作させることができます。

Heroku上に作成したGitのリポジトリに対してcommitを行うと、Heroku側で処理がフックされRailsがデプロイされるようになります。

2010年6月1日現在、Heroku上ではRuby1.8.6、Ruby1.8.7(beta)、Ruby1.9.1(beta)を使用することができます。また、Rails3で作成したWebアプリケーションを動作させることができるようになっています。

この記事では、Heroku上でRuby1.9.1(beta)を使用し、Rails3をデプロイするところまでの手順をメモしておきます。

※ Heroku 上で動くサンプルアプリ作りました。 [ target="_blank" class="extlink">Kaffetter（カフェッター） http://kaffetter.heroku.com/](http://kaffetter.heroku.com/)

<h4>Heroku でアプリを動かす手順</h4>

<ul>
<li>Herokuでアカウントを作成する</li>
<li>gemでheroku managment APIをインストールする</li>
<li>ローカルにRails環境を構築する</li>
<li>gitでHeroku上のリポジトリにコミットする</li>
</ul>

<dl>
<dt class="note">この記事を書くのに使った環境</dt>
<dd>
<ul><li>Mac OSX 10.6.3 Snow Leopard</li><li>Git 1.7.0.3</li><li>Ruby1.9.1</li><li>Rails 3.0.0 beta3</li><li>gem 1.3.6</li></ul></dd>
</dl>


<!-- more -->

<h2>あらかじめ用意しておくもの</h2>

<dl>
  <dt>Git</dt>
  <dd>Heroku上のリポジトリにコミットするためにGitを使用します。</dd>  
  <dt>SSL接続用の公開鍵</dt>
  <dd>GitでHeroku上のリポジトリにコミットする際に、ホームディレクトリの.sshフォルダにid_rsa.pubという名前で公開鍵が必要になります（$HOME/.ssh/id_rsa.pub）。あらかじめ<code>ssh-keygen</code>コマンド等を使用して作成しておきます。</dd>
</dl>

<h2>Heroku上にRails3のWebアプリを公開する</h2>

<h3>Herokuでアカウントを作成する</h3>

[ target="_blank" class="extlink">http://heroku.com/](http://heroku.com/) にアクセスし、画面上にある「Sign Up」からアカウントを作成します。

<img src="http://hamasyou.com/images/heroku/sc1.png" width="673px" alt="Sign Up" />

メールアドレスを入力すると確認メールが届くので、メール上の認証リンクをたどるとアカウントが作成されます。

<h3>gemでheroku managment APIをインストールする</h3>

gemを使ってHeroku管理用のAPIをインストールします。

<pre class="console">
> <kbd>sudo gem install heroku</kbd>
Successfully installed heroku-1.9.9
1 gem installed
Installing ri documentation for heroku-1.9.9...
Installing RDoc documentation for heroku-1.9.9...
</pre>

<h3>ローカルにRails環境を構築する</h3>

railsコマンドを使用して、Railsの開発環境を作成します。

<pre class="console">
> <kbd>rails new heroku-demo</kbd>
      <span style="color:Lime">create</span>
      <span style="color:Lime">create</span> README
      <span style="color:Lime">create</span> .gitignore
      <span style="color:Lime">create</span> Rakefile
      <span style="color:Lime">create</span> config.ru
      <span style="color:Lime">create</span> Gemfile
      <span style="color:Lime">create</span> app
      <span style="color:Lime">create</span> app/controllers/application_controller.rb
      <span style="color:Lime">create</span> app/helpers/application_helper.rb
      <span style="color:Lime">create</span> app/views/layouts/application.html.erb
      <span style="color:Lime">create</span> app/models
      <span style="color:Lime">create</span> config
      <span style="color:Lime">create</span> config/routes.rb
      <span style="color:Lime">create</span> config/application.rb
      <span style="color:Lime">create</span> config/environment.rb
      <span style="color:Lime">create</span> config/environments
      <span style="color:Lime">create</span> config/environments/development.rb
      <span style="color:Lime">create</span> config/environments/production.rb
      <span style="color:Lime">create</span> config/environments/test.rb
      <span style="color:Lime">create</span> config/initializers
      <span style="color:Lime">create</span> config/initializers/backtrace_silencers.rb
      <span style="color:Lime">create</span> config/initializers/inflections.rb
      <span style="color:Lime">create</span> config/initializers/mime_types.rb
      <span style="color:Lime">create</span> config/initializers/secret_token.rb
      <span style="color:Lime">create</span> config/initializers/session_store.rb
      <span style="color:Lime">create</span> config/locales
      <span style="color:Lime">create</span> config/locales/en.yml
      <span style="color:Lime">create</span> config/boot.rb
      <span style="color:Lime">create</span> config/database.yml
      <span style="color:Lime">create</span> db
      <span style="color:Lime">create</span> db/seeds.rb
      <span style="color:Lime">create</span> doc
      <span style="color:Lime">create</span> doc/README_FOR_APP
      <span style="color:Lime">create</span> lib
      <span style="color:Lime">create</span> lib/tasks
      <span style="color:Lime">create</span> lib/tasks/.gitkeep
      <span style="color:Lime">create</span> log
      <span style="color:Lime">create</span> log/server.log
      <span style="color:Lime">create</span> log/production.log
      <span style="color:Lime">create</span> log/development.log
      <span style="color:Lime">create</span> log/test.log
      <span style="color:Lime">create</span> public
      <span style="color:Lime">create</span> public/404.html
      <span style="color:Lime">create</span> public/422.html
      <span style="color:Lime">create</span> public/500.html
      <span style="color:Lime">create</span> public/favicon.ico
      <span style="color:Lime">create</span> public/index.html
      <span style="color:Lime">create</span> public/robots.txt
      <span style="color:Lime">create</span> public/images
      <span style="color:Lime">create</span> public/images/rails.png
      <span style="color:Lime">create</span> public/stylesheets
      <span style="color:Lime">create</span> public/stylesheets/.gitkeep
      <span style="color:Lime">create</span> public/javascripts
      <span style="color:Lime">create</span> public/javascripts/application.js
      <span style="color:Lime">create</span> public/javascripts/controls.js
      <span style="color:Lime">create</span> public/javascripts/dragdrop.js
      <span style="color:Lime">create</span> public/javascripts/effects.js
      <span style="color:Lime">create</span> public/javascripts/prototype.js
      <span style="color:Lime">create</span> public/javascripts/rails.js
      <span style="color:Lime">create</span> script
      <span style="color:Lime">create</span> script/rails
      <span style="color:Lime">create</span> test
      <span style="color:Lime">create</span> test/performance/browsing_test.rb
      <span style="color:Lime">create</span> test/test_helper.rb
      <span style="color:Lime">create</span> test/fixtures
      <span style="color:Lime">create</span> test/functional
      <span style="color:Lime">create</span> test/integration
      <span style="color:Lime">create</span> test/unit
      <span style="color:Lime">create</span> tmp
      <span style="color:Lime">create</span> tmp/sessions
      <span style="color:Lime">create</span> tmp/sockets
      <span style="color:Lime">create</span> tmp/cache
      <span style="color:Lime">create</span> tmp/pids
      <span style="color:Lime">create</span> vendor/plugins
      <span style="color:Lime">create</span> vendor/plugins/.gitkeep
> <kbd>cd heroku-demo</kbd>
</pre>

<h3>gitでHeroku上のリポジトリにコミットする</h3>

<code>git init</code>コマンドを使って、Rails環境をgitで管理し、Heroku上のリポジトリにコミットします。

まずは、<code>git init</code> で開発環境をgitで管理します。

<pre class="console">
> <kbd>git init</kbd>
Initialized empty Git repository in /Users/hamasyou/Documents/Works/work/heroku-demo/.git/
</pre>

次に、作ったばかりのRails環境をすべてローカルのgitリポジトリにコミットします。

<pre class="console">
> <kbd>git add .</kbd>
> <kbd>git commit -m "new app"</kbd>
[master (root-commit) 7b21637] new app
 39 files changed, 9038 insertions(+), 0 deletions(-)
 create mode 100644 .gitignore
 create mode 100644 Gemfile
 create mode 100644 README
 create mode 100644 Rakefile
 create mode 100644 app/controllers/application_controller.rb
 create mode 100644 app/helpers/application_helper.rb
 create mode 100644 app/views/layouts/application.html.erb
 create mode 100644 config.ru
 create mode 100644 config/application.rb
 create mode 100644 config/boot.rb
 create mode 100644 config/database.yml
 create mode 100644 config/environment.rb
 create mode 100644 config/environments/development.rb
 create mode 100644 config/environments/production.rb
 create mode 100644 config/environments/test.rb
 create mode 100644 config/initializers/backtrace_silencers.rb
 create mode 100644 config/initializers/inflections.rb
 create mode 100644 config/initializers/mime_types.rb
 create mode 100644 config/initializers/secret_token.rb
 create mode 100644 config/initializers/session_store.rb
 create mode 100644 config/locales/en.yml
 create mode 100644 config/routes.rb
 create mode 100644 db/seeds.rb
 create mode 100644 doc/README_FOR_APP
 create mode 100644 lib/tasks/.gitkeep
 create mode 100644 public/404.html
 create mode 100644 public/422.html
 create mode 100644 public/500.html
 create mode 100644 public/favicon.ico
 create mode 100644 public/images/rails.png
 create mode 100644 public/index.html
 create mode 100644 public/javascripts/application.js
 create mode 100644 public/javascripts/controls.js
 create mode 100644 public/javascripts/dragdrop.js
 create mode 100644 public/javascripts/effects.js
 create mode 100644 public/javascripts/prototype.js
 create mode 100644 public/javascripts/rails.js
 create mode 100644 public/robots.txt
 create mode 100644 public/stylesheets/.gitkeep
 create mode 100755 script/rails
 create mode 100644 test/performance/browsing_test.rb
 create mode 100644 test/test_helper.rb
 create mode 100644 vendor/plugins/.gitkeep
</pre>

次に、<code>heroku create</code>コマンドを使って、Heroku上にリポジトリを作成します。

<pre class="console">
> <kbd>heroku create heroku-demo</kbd>
Creating heroku-demo...... done
Created http://heroku-demo.heroku.com/ | git@heroku.com:heroku-demo.git
Git remote heroku added
</pre>

<dl>
<dt class="notice">Notice</dt>
<dd>最初に<code>heroku create</code>コマンドを実行すると、SSLで接続する旨のメッセージが表示されることがあります。ここであらかじめ用意しておいた$HOME/.ssh/id_rsa.pubが使われます。</dd>
</dd>
</dl>

最後にHeroku上のリポジトリにローカルのコミットを反映させるのですが、その前に、Heroku上で使用するRubyのバージョンを変更しなければいけません。

<code>heroku stack</code>コマンドを実行すると現在Heroku上でどのRubyのバージョンが使われているのかがわかります。

<pre class="console">
> <kbd>heroku stack</kbd>
* aspen-mri-1.8.6
  bamboo-ree-1.8.7 (beta)
  bamboo-mri-1.9.1 (beta)
</pre>

Rails3はRuby1.9.1で動かしますので、<code>heroku stack:migrate bamboo-mri-1.9.1</code>と入力してHeroku上のRubyの動作バージョンを変更します。

<pre class="console">
> <kbd>heroku stack:migrate bamboo-mri-1.9.1</kbd>
-----> Preparing to migrate heroku-demo
       aspen-mri-1.8.6 -> bamboo-mri-1.9.1
 
       NOTE: You must specify ALL gems (including Rails) in manifest
 
       Please read the migration guide:        http://docs.heroku.com/bamboo
 
-----> Migration prepared.
       Run 'git push heroku master' to execute migration.
</pre>

最後に、<code>git push heroku master</code>コマンドで、Herokuにローカルのコミットを反映させます。

<pre class="console">
> <kbd>git push heroku master</kbd>
Warning: Permanently added the RSA host key for IP address 'xx.xxx.xxx.xx' to the list of known hosts.
Counting objects: 62, done.
Delta compression using up to 4 threads.
Compressing objects: 100% (47/47), done.
Writing objects: 100% (62/62), 80.12 KiB, done.
Total 62 (delta 2), reused 0 (delta 0)
 
-----> Heroku receiving push
-----> Migrating from aspen-mri-1.8.6 to bamboo-mri-1.9.1
 
-----> Gemfile detected, running Bundler
       Unresolved dependencies detected; Installing...
       Fetching source index from http://rubygems.org/
       Using rake (0.8.7) from system gems
       Installing abstract (1.0.0) from rubygems repository at http://rubygems.org/
       Installing builder (2.1.2) from rubygems repository at http://rubygems.org/
       Installing i18n (0.3.7) from rubygems repository at http://rubygems.org/
       Installing memcache-client (1.8.3) from rubygems repository at http://rubygems.org/
       Installing tzinfo (0.3.22) from rubygems repository at http://rubygems.org/
       Installing activesupport (3.0.0.beta3) from rubygems repository at http://rubygems.org/
       Installing activemodel (3.0.0.beta3) from rubygems repository at http://rubygems.org/
       Installing erubis (2.6.5) from rubygems repository at http://rubygems.org/
       Installing rack (1.1.0) from rubygems repository at http://rubygems.org/
       Installing rack-mount (0.6.3) from rubygems repository at http://rubygems.org/
       Installing rack-test (0.5.4) from rubygems repository at http://rubygems.org/
       Installing actionpack (3.0.0.beta3) from rubygems repository at http://rubygems.org/
       Using mime-types (1.16) from system gems
       Installing polyglot (0.3.1) from rubygems repository at http://rubygems.org/
       Installing treetop (1.4.8) from rubygems repository at http://rubygems.org/
       Installing mail (2.2.1) from rubygems repository at http://rubygems.org/
       Installing text-hyphen (1.0.0) from rubygems repository at http://rubygems.org/
       Installing text-format (1.0.0) from rubygems repository at http://rubygems.org/
       Installing actionmailer (3.0.0.beta3) from rubygems repository at http://rubygems.org/
       Installing arel (0.3.3) from rubygems repository at http://rubygems.org/
       Installing activerecord (3.0.0.beta3) from rubygems repository at http://rubygems.org/
       Installing activeresource (3.0.0.beta3) from rubygems repository at http://rubygems.org/
       Installing bundler (0.9.25) from rubygems repository at http://rubygems.org/
       Installing thor (0.13.6) from rubygems repository at http://rubygems.org/
       Installing railties (3.0.0.beta3) from rubygems repository at http://rubygems.org/
       Installing rails (3.0.0.beta3) from rubygems repository at http://rubygems.org/
       Installing sqlite3-ruby (1.2.5) from rubygems repository at http://rubygems.org/
       with native extensions <span style="color:Lime">Your bundle is complete! Use `bundle show gemname` to see where a bundled gem is installed.</span>
       Locking environment
-----> Rails app detected
-----> Detected Rails is not set to serve static_assets
       Installing rails3_serve_static_assets... done
       Compiled slug size is 3.9MB
-----> Launching........... done
       http://heroku-demo.heroku.com deployed to Heroku
 
-----> Migration complete, your app is now running on bamboo-mri-1.9.1
 
To git@heroku.com:heroku-demo.git
* [new branch]      master -> master
</pre>

[ target="_blank" class="extlink">http://heroku-demo.heroku.com/](http://heroku-demo.heroku.com/)にアクセスして、Railsの画面が表示されれば完了です。あとは、ローカルで開発して、<code>git push heroku master</code>でHeroku上にコミットを反映するたびにRailsがデプロイされます。

























