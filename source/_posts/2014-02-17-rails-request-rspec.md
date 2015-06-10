---
layout: post
title: "RailsでつくるRESTful APIのrequest specを書く"
date: 2014-02-17 14:09:38 +0900
comments: true
categories: [Programming]
keywords: "rails,restful,api,request spec"
tags: [Rails,RESTful,spec]
sharing: true
published: true
amazon_url: ""
amazon_author: ""
amazon_image: ""
amazon_publisher: ""
og_image: ""
---

Rails 環境で RESTful API を提供する場面があると思います。API もコントローラで提供するので、コントローラのテストで記述してもいいのですが、RESTful API であれば、**request spec** を使うほうがよさそうなので request spec を書くメモです。

- Rails 4.0.2
- rspec 2.14.1

<!-- more -->

## テスト用のプロジェクトを整える

まずは、チュートリアル用のプロジェクトを作成します。

{% terminal %}
$ rails new specbox --skip-bundle
$ cd specbox
{% endterminal %}

`Gemfile` を開いて次の gem を追記します。

```ruby Gemfile
group :development, :test do
  gem 'rspec-rails'
  gem 'guard-rspec'
  gem 'factory_girl_rails'
  gem 'spring'
  gem 'spring-commands-rspec'
end
```

rspec と scaffold の設定を行います。

{% terminal %}
$ bundle install --path vendor/bundle
$ bundle exec rails g rspec:install
$ bundle exec guard init rspec
$ bundle exec rails g model Book title:string author:string price:integer
$ bundle exec rails g scaffold_controller Books
$ bundle exec rake db:migrate
$ bundle exec rake db:test:prepare
{% endterminal %}

`config/routes.rb` にルーティングを追加します。

```ruby config/routes.rb
Specbox::Application.routes.draw do
  ...
  resources :books
  ...
end
```

テストを実行します。

{% terminal %}
$ bundle exec guard
15:12:01 - INFO - Run all
15:12:01 - INFO - Running all specs
..**..........................

Pending:
  Book add some examples to (or delete) /Users/hamasyou/tmp/specbox/spec/models/book_spec.rb
    # No reason given
    # ./spec/models/book_spec.rb:4
  BooksHelper add some examples to (or delete) /Users/hamasyou/tmp/specbox/spec/helpers/books_helper_spec.rb
    # No reason given
    # ./spec/helpers/books_helper_spec.rb:14

Finished in 0.18066 seconds
30 examples, 0 failures, 2 pending

Randomized with seed 14063
{% endterminal %}

これで rspec の設定ができました。それでは *request spec* を書いていきます。

## request spec を書く

*request spec* は他の rspec のテストとほとんど変わりありません。テストは `spec/requests` フォルダの下に作成していきます。

`scaffold_controller` でコントローラを作成したので、すでに `spec/requests/books_spec.rb` があるはずです。テストコードを見てみます。

```ruby spec/requests/books_spec.rb
require 'spec_helper'

describe "Books" do
  describe "GET /books" do
    it "works! (now write some real specs)" do
      # Run the generator again with the --webrat flag if you want to use webrat methods/matchers
      get books_path
      response.status.should be(200)
    end
  end
end
```

request spec もコントローラのテストと同じように、`get`、`post`、といったメソッドを使ってテストを行います。`books_spec.rb` を次のように書き換えてみます。

```ruby spec/requests/books_spec.rb
require 'spec_helper'

describe 'Books' do
  describe 'GET /books' do

    it '登録されている本が返されること' do
      book = FactoryGirl.create(:book)
      get '/books', format: 'json'
      expect(response).to be_success
      expect(json).to be_a_kind_of(Array)
      expect(json[0]['id']).to eq book.id
    end
  end
end
```

request spec はこういう感じで、**実際に呼び出す URL を記述する** のと、**json の中身をチェックする** のが大事だと思っています。

コントローラのテストではビューの中身までチェックしませんし、ビューのテストとコントローラのテストを2つ書くのはテストを把握しづらいので、request spec のテストのように一箇所で同時にテストするのが効果的だと思います。

API はインターフェース（*入力パラメータ* と *出力形式* ）が大事なのでここをきっちり押さえておくテストを書く必要があります。

で、テストケースを変えると当然テストが失敗するので、テストが通るようにヘルパを書き換えます。

### request_helpers.rb を作成する

request spec を書くときに便利なメソッドをいくつか用意しておきます。

RESTful API は出力を json で返すものが多いと思うので、json を扱いやすくするヘルパーを用意します。

また、 `AuthenticationHelper` は認証が必要な API を呼び出すときの便利メソッドとして記述してありますが、不要であれば削除、仕様が違うようであれば書きなおしてつかいます。

次のファイルを `spec/support/request_helpers.rb` として作成します。

```ruby spec/support/request_helpers.rb
module Requests
  module JsonHelpers
    def json
      @json ||= JSON.parse(response.body)
    end
  end

  module AuthenticationHelper
    %w(get post put delete).each do |method_name|
      define_method("auth_#{method_name}") do |uri, auth_user, params = {}, env = {}, &block|
        __send__(method_name, uri, params, env.reverse_merge(HTTP_AUTHORIZATION: "token #{auth_user.access_token}"), &block)
      end
    end
  end
end
```

このヘルパを `spec/spec_helper.rb` に追加します。

```ruby spec/spec_helper.rb
RSpec.configure do |config|
  ...
  config.include Requests::JsonHelpers, type: :request
  config.include Requests::AuthenticationHelper, type: :request
  ...
end
```

これでテストが通るようになりました。

## まとめ

Rails で RESTful な API を作ることはよくあると思いますが、今までは API のテストもコントローラのテストで行っていました。

でも、request spec を使ったほうが **呼び出しのURL**、**必要なパラメータ**、**出力形式** を素直にテストとして書けるので、API のテストは request spec を使うようにするといいかなと思います。
