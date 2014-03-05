---
layout: post
title: "Webサービスをスタートアップする10の手順（Rails編）"
date: 2014-03-05 09:44:22 +0900
comments: true
categories: [Tech]
keywords: "WebService,startup,スタートアップ,手順,Rails"
tags: [WebService,StartUp,Rails]
sharing: true
published: true
amazon_url: ""
amazon_author: ""
amazon_image: ""
amazon_publisher: ""
og_image: ""
---

アイデアを思いついたときに Web サービスを作ってスタートアップする手順メモです。Rails 編としていますが、たぶん別のバージョンは書かないでしょう。。

手順は思いついた順（というか、前に作ったサービスの手順）で書いていますので、抜け漏れとかあると思いますがコメントあればよろしくおねがいします。

ちなみに、最近作ったサービスはこちら。よかったら使ってみてください。

[Web上のメディアのすべてにヤジを飛ばすサービス - 野次る](http://booboo.mountposition.co.jp/)

<!-- more -->

## 1. アイデアを揉む

思いついたアイデアをすぐに実現するのはいいことだと思いますが、世の中では2パターンで語られていますね。

1. 思いついたサービスは誰のためのサービス？
1. そのサービスは自分で使う？

誰のためのサービスかという問は、すなわちどんな問題を解決するサービスなのか？ってことです。
「スタートアップが失敗する理由」みたいなキーワードで Google 先生に聞けばイロイロ教えてくれるかと。

まぁでも、**思いついたサービスを自分で使ってみたいと思えるのなら創るべき** でしょう！僕はそう思います。


## 2. プロジェクトを作成する

さて、実際にサービスをつくっていきます。まずは Rails のプロジェクトを作らねばなりません。
プロジェクトのスタートアップ手順は Qiita にメモを残していますのでそっちを参照してみてください。

[Rails4プロジェクトを新規で立ち上げる時の設定メモ - Qiita](http://qiita.com/hamasyou/items/dc319f399fc851e4ba42)


## 3. デザインを決める

デザインとか多少でもかじった人なんかはすんなり行くんでしょうけど、デザインだけはまるでダメっていう人もいると思います。
そんなときは、[Bootstrap](http://getbootstrap.com/) 使いましょう。

Bootstrap も Sass が提供されるようになりましたので、Sass ファイルをダウンロードしておくと、後でデザインを弄りたい時とか便利です。
ダウンロードした Sass ファイルは `vendor/assets` に入れましょう。

```
- vendor
  - assets
    - fonts
      - bootstrap
        glyphicons-halflings-regular.eot
        glyphicons-halflings-regular.svg
        glyphicons-halflings-regular.ttf
        glyphicons-halflings-regular.woff
    - javascripts
      - bootstrap
        affix.js
        alert.js
        button.js
        carousel.js
        collapse.js
        dropdown.js
        modal.js
        popover.js
        scrollspy.js
        tab.js
        tooltip.js
        transition.js
      bootstrap.js
    - stylesheets
      - bootstrap
        _alerts.scss
        _badges.scss
        _breadcrumbs.scss
        _button-groups.scss
        _buttons.scss
        _carousel.scss
        _close.scss
        _code.scss
        _component-animations.scss
        _dropdowns.scss
        _forms.scss
        _glyphicons.scss
        _grid.scss
        _input-groups.scss
        _jumbotron.scss
        _labels.scss
        _list-group.scss
        _media.scss
        _mixins.scss
        _modals.scss
        _navbar.scss
        _navs.scss
        _normalize.scss
        _pager.scss
        _pagination.scss
        _panels.scss
        _popovers.scss
        _print.scss
        _progress-bars.scss
        _responsive-utilities.scss
        _scaffolding.scss
        _tables.scss
        _theme.scss
        _thumbnails.scss
        _tooltip.scss
        _type.scss
        _utilities.scss
        _variables.scss
        _wells.scss
        bootstrap.scss
```

`app/assets/javascripts/application.js` と `app/assets/stylesheets/application.css` に bootstrap のファイルを追加します。

```js app/assets/javascripts/application.js
//= require jquery
//= require jquery_ujs
//= require bootstrap
//= require_self
```

```css app/assets/stylesheets/application.css
/*
 *= require bootstrap
 *= require_self
 */
```

僕は `require tree` は削除しちゃう派で、require が必要なファイルは自分で記述するようにしています。`fonts` フォルダは `asset pipeline` に含まれないので明示的に `config/application.rb` で読み込むようにします。

```ruby
#...
module YourApplication
  class Application < Rails::Application
    # ...
    config.assets.paths << "#{Rails.root}/vendor/assets/fonts"
  end
end
```


## 4. テンプレートに BOILERPLATE を使う

デザインと順番が前後するかもしれませんが、最近はレスポンシブデザインがはやりですよね。
HTML5 でレスポンシブ対応となるとキマリ文句みたいなテンプレートがあるわけなんですが、いちいち調べる物めんどくさいので僕は **BOILERPLATE** を使っています。

[HTML5 ★ BOILERPLATE - The web's most popular front-end template](http://html5boilerplate.com/)

BOILERPLATE をダウンロードして、プロジェクトディレクトリに配置します。`index.html` の中身をごっそり `app/views/layouts/application.html.erb` にいれこみます。
このとき、`csrf_meta_tags` を間違って消さないように注意です。

BOILERPLATE を見ると分かるように、最近は JavaScript ファイルを body 部の最後に置くようになっています。DOM の構築をブロックしないようにするためですね。
で、Rails では `turbolinks` っていう機能があって、まぁいろいろとこの辺がバッティングしてウザイので、`turbolinks` を使わないようにします。

[Rails 4 で turbolinks をオフにする方法 - Qiita](http://qiita.com/kazz187/items/12737363d62b9c91993c)


## 5. レイアウトファイルをいじる（SEO対策）

プログラムを書き始める前に、先にデザイン回りを調整しておくのがいいかと思います。

いろいろ追加すると大体こんな感じのテンプレートになります。

```html+erb
<!DOCTYPE html>
<!--[if lt IE 7]>      <html class="no-js lt-ie9 lt-ie8 lt-ie7"> <![endif]-->
<!--[if IE 7]>         <html class="no-js lt-ie9 lt-ie8"> <![endif]-->
<!--[if IE 8]>         <html class="no-js lt-ie9"> <![endif]-->
<!--[if gt IE 8]><!--> <html class="no-js"> <!--<![endif]-->
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title><%= content_for?(:title) ? "#{yield(:title)} - " : '' %><%= Settings.app_name %></title>
<meta name="viewport" content="width=device-width, initial-scale=1">

<meta name="keywords" content="<%= content_for?(:keywords) ? "#{yield(:keywords)}" : "#{Settings.keywords}" %>">
<meta name="description" content="<%= content_for?(:description) ? "#{yield(:description)}" : "#{Settings.description}" %>">

<meta property="og:title" content="<%= content_for?(:title) ? "#{yield(:title)} - " : '' %><%= Settings.app_name %>" />
<meta property="og:type" content="article" />
<meta property="og:url" content="<%= request.url %>" />
<meta property="og:site_name" content="<%= Settings.app_name %>" />
<meta property="og:description" content="<%= content_for?(:description) ? "#{yield(:description)}" : "#{Settings.description}" %>" />
<meta property="fb:app_id" content="<%= Settings.facebook.app_id %>" />

<%= stylesheet_link_tag    "normalize", media: "all" %>
<%= stylesheet_link_tag    "application", media: "all" %>
<%= javascript_include_tag 'modernizr-2.6.2.min' %>
<%= csrf_meta_tags %>
</head>
<body>

<header class="navbar navbar-default" role="banner">
  <div class="container">
    <div class="navbar-header">
      <a href="/" class="navbar-brand"><%= Settings.app_name %></a>
      <button class="navbar-toggle" type="button" data-toggle="collapse" data-target="#navbar-main">
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
      </button>
    </div>
    <nav class="navbar-collapse collapse" id="navbar-main" rel="navigation">
      <ul class="nav navbar-nav">
        <li></li>
      </ul>
    </nav>
  </div>
</header>

<!--[if lt IE 7]>
    <p class="browsehappy">You are using an <strong>outdated</strong> browser. Please <a href="http://browsehappy.com/">upgrade your browser</a> to improve your experience.</p>
<![endif]-->

<div class="container" role="main">
<%= yield %>
</div>

<footer role="contentinfo">
  &copy; <%= Date.today.year %> <%= Settings.copyright %>
</footer>

<%= javascript_include_tag "application" %>
</body>
</html>
```

`Settings` クラスは `rails_config` という gem を使った設定ファイルを読み込むものです。
**SEO** 対策系の `title` タグや `og` タグ周りの設定を意識したほうがいいです。

サイトマップの生成もここで検討しておいたほうがいいでしょう。

[meta-tagsとsitemap_generatorで始めるRails 4.0時代のSEO対策 - 酒と泪とRubyとRailsと](http://morizyun.github.io/blog/meta-tags-sitemap-generator-rails-seo/)


## 6. コア部分をつくり込む

やっとプログラミング開始です。コア部分を作りこんでいきます。ここは気合が続く限り一気に作ったほうがいいと思います。
僕の場合は、サービス開発は結構集中力とやる気の持続がポイントになってくるので。

やる気の部分は結構重要で、1ヶ月とかかけて作るようだと案外あきちゃったりするんですよね。。エラー表示のところとかもちゃんとつくっていきましょう。

[RailsでBootstrapを使ったときにフォームエラーをキレイに表示する方法 - Qiita](http://qiita.com/hamasyou/items/22c50959490a3b52dbaf)


## 7. 404ページと500ページを用意する

コア部分ができたら、404ページと500ページを用意します。
Rails にはこの辺をうまく扱う方法がありますのでそれを使います。

`config/routes.rb` の最後のルートに、次のルールを追加します。

```ruby
match '*not_found' => 'application#render_404', via: :all
```

規定のルールにマッチしないルートは、`application_controller#render_404` に飛ばされます。ここで404エラーの処理を記述します。
同じく、`application_controller` に500エラーをハンドリングする処理も記述します。

```ruby app/controllers/application_controller.rb`
class ApplicationController < ActionController::Base
  protect_from_forgery with: :exception

  unless Rails.configuration.consider_all_requests_local
    rescue_from Exception, with: :render_500
    rescue_from AbstractController::ActionNotFound, with: :render_404
    rescue_from ActionController::RoutingError, with: :render_404
    rescue_from ActiveRecord::RecordNotFound, with: :render_404
  end

  def render_404(exception = nil)
    if exception
      logger.warn "Rendering 404 with exception: #{exception.message}"
    end
    render template: 'errors/error_404', status: 404, layout: 'application', content_type: 'text/html'
  end

  def render_500(exception = nil)
    if exception
      logger.fatal "Rendering 500 with exception: #{exception.message}"
      logger.fatal request.env.map { |key, val| "#{key} => #{val}" }
      logger.fatal exception.backtrace.join("\n")
    end

    respond_to do |format|
      format.html { render template: 'errors/error_500', status: 500, layout: 'application', content_type: 'text/html' }
      format.json { render json: {message: exception.message}, status: 500 }
    end
  end
end
```

`app/views/errors` ディレクトリに `error_404.html.erb` と `error_500.html.erb` を作成して、それぞれ中身を記述します。


## 8. ソーシャル流入用のボタン類と Google Analytics を配置する

作ったサービスをユーザに宣伝してもらえるようにソーシャル系のボタンを用意するのを忘れないようにします。
また、Google Analytics も忘れないように設定します。
Facebook はアプリIDが必要になりますので別途登録してアプリIDを入手しておきます。Google Analytics も同じくですね。

ちなみに、ソーシャル系サービスはいろいろありますが、Facebook、Twitter、はてな、Google+、Pocket あたりを僕は抑えています。

ソーシャル系サービスで配布されている JavaScript を非同期で読み込むための JavaScript を作成します。
忘れずに `application.js` にも追加しておきます。

```js+erb app/assets/javascripts/sharing.js.erb
(function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
(i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
})(window,document,'script','//www.google-analytics.com/analytics.js','ga');

ga('create', '<%= Settings.google.analytics_tracking_id %>', 'hamasyou.com');
ga('send', 'pageview');

(function (w, d) {
  var j, s = d.getElementsByTagName('script')[0],
  a = function (u, i) {
    if (!d.getElementById(i)) {
      j = d.createElement('script');
      j.type = 'text/javascript';
      j.src = u;
      j.async = true;
      if (i) {j.id = i;}
      s.parentNode.insertBefore(j, s);
    }
  };
  a('//platform.twitter.com/widgets.js', 'twitter-wjs');
  a('//b.st-hatena.com/js/bookmark_button.js','hatebu-js');
  a('//apis.google.com/js/plusone.js','plusone-js');
  a('//connect.facebook.net/ja_JP/all.js#appId=<%= Settings.facebook.app_id %>&xfbml=1','facebook-jssdk');
  a('//widgets.getpocket.com/v1/j/btn.js?v=1','pocket-btn-js');
})(this, document);
```

ボタンを集めた部分テンプレートを用意します。

```html+erb app/views/shared/_sharing.html.erb
<% cache url do %>
  <ul class="sharing list-inline">
    <li class="hatena">
      <a href="http://b.hatena.ne.jp/entry/<%= url %>"
         class="hatena-bookmark-button"
         rel="nofollow"
         data-hatena-bookmark-layout="standard-balloon"
      ><img src="http://b.st-hatena.com/images/entry-button/button-only.gif"
            alt="Add to Hatena Bookmark" /></a>
    </li>
    <div id="fb-root"></div>
    <li class="facebook">
      <div class="fb-like"
           data-share="false"
           data-layout="button_count"
           data-show-faces="false"
           data-font="verdana"
           data-href="<%= url %>"></div>
    </li>
    <li class="facebook_share">
      <div class="fb-share-button"
           data-type="button_count"
           data-font="verdana"
           data-href="<%= url %>"></div>
    </li>
    <li class="twitter">
      <a href="http://twitter.com/share"
         class="twitter-share-button"
         rel="nofollow"
         data-url="<%= url %>"
         data-via="<%= Settings.twitter.data_via %>"
         data-counturl="<%= url %>">Tweet</a>
    </li>
    <li class="googleplus">
      <div class="g-plusone"
           data-size="medium"
           data-href="<%= url %>"></div>
    </li>
    <li class="pocket">
      <a href="https://getpocket.com/save"
         class="pocket-btn" data-lang="en"
         data-save-url="<%= url %>"
         data-pocket-count="horizontal"
         data-pocket-align="left">Pocket</a>
    </li>
  </ul>
<% end %>
```

`url` 変数は部分テンプレートを読み込むときに渡すシェアしたいページの URL です。最後に CSS も用意しておきます。

```scss app/assets/stylesheets/sharging.css.scss
.sharing {
  li {
    height: 23px;
    vertical-align: bottom;
  }
}
li.hatena {
}
li.twitter {
}
li.googleplus {
}
li.facebook {
}
.fb_iframe_widget > span {
  vertical-align: baseline !important;
}
li.facebook_share {
}
#fb-root { display: none; }
li.pocket {
}
```

ボタンの長さ等の調整が必要であれば、ここで調整します。`app/assets/stylesheets/application.css` に登録するのを忘れずに。


## 9. SCM にプロジェクトを保存する

ここまで来たら後はサーバに載っけて公開するだけですが、作ったプロジェクトはソースコードリポジトリに登録しておきましょう。
リポジトリサービスとしては [GitHub](https://github.com/) が有名ですが、GitHub は無料で使う分には公開リポジトリしか作れません。
そこで、同じくリポジトリサービスで非公開リポジトリが作成できる [BitBucket](https://bitbucket.org/) を使うといいと思います。


## 10. サービス公開用のサーバを用意する

最後にサービス公開用のサーバを用意するわけですが、いきなり [AWS](http://aws.amazon.com/jp/) をつかうのもいいんですが AWS は個人で使うにはちょっとお高いんですよね。。
VPS を借りるなら [Sakura VPS](http://vps.sakura.ad.jp/) とかのほうが安くすみます。が、無料でサーバを利用したいのであれば [Heroku](https://www.heroku.com/‎) がいいかと思います。

[Rails4でheroku Pushまでの最短手順 [Haml/bootstrap 3.0/postgresql or MySQL] - 酒と泪とRubyとRailsと](http://morizyun.github.io/blog/heroku-rails4-postgresql-introduction/)


## サービスを大きくしていく

サービスが軌道に乗ってきたら機能追加したり、独自ドメインをとったりしてサービスを大きくしていくといいと思います。

大体こんな感じで僕はいつもサービスを作っています。ちなみに、最近作ったサービスはこちら。よかったら使ってやってください。

[Web上のメディアのすべてにヤジを飛ばすサービス - 野次る](http://booboo.mountposition.co.jp/)
