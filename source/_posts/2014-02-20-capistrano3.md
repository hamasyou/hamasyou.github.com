---
layout: post
title: "Capistrano3をRailsで使ってみた感想"
date: 2014-02-20 11:25:05 +0900
comments: true
categories: [Tech]
keywords: "Rails,Capistrano3"
tags: [Rails,Capistrano3]
sharing: true
published: true
amazon_url: ""
amazon_author: ""
amazon_image: ""
amazon_publisher: ""
og_image: ""

---

**Capistrano3** がかなりシンプルになって使いやすくなったのでプロジェクトで使ってみました。うん。なかなかよさげです。

Capistrano2 にあった *copy_via* がなくなっていたので、デプロイ先のサーバから Git リポジトリにアクセスできるようにしないといけなかったりとちょっと面倒なこともありましたが概ね良好です。

<!-- more -->

[capistrano / capistrano](https://github.com/capistrano/capistrano)

Capistrano3 の導入はこの記事がオススメです。

[capistrano 3.x系を使ってrailsをデプロイ](http://threetreeslight.com/post/68344998681/capistrano-3-x-rails)

Capistrano3 になって、独自DSLから Rake に変わりました。なので、コマンドも結構すっきりして書きやすくなりました。

サンプルプロジェクトで使った `config/deploy.rb` の例です。

```ruby config/deploy.rb
# config valid only for Capistrano 3.1
lock '3.1.0'

set :application, 'capbox'
set :repo_url, 'git@github.com:hamasyou/git_demo.git'
set :deploy_to, '/var/webapp/capbox'

set :linked_files, %w{config/database.yml}
set :linked_dirs, %w{bin log tmp/pids tmp/cache tmp/sockets vendor/bundle public/system solr}

namespace :deploy do

  before :restart, :start_solr do
    on roles(:app), in: :groups do
      within release_path do
        with rails_env: fetch(:rails_env) do
          execute :rake, 'sunspot:solr:stop'
          execute :rake, 'sunspot:solr:start'
        end
      end
    end
  end

  desc 'Restart application'
  task :restart do
    on roles(:app), in: :sequence, wait: 5 do
      execute :touch, release_path.join('tmp/restart.txt')
    end
  end

  after :publishing, :restart

  after :restart, :sitemap do
    on roles(:app), in: :groups, limit: 3, wait: 10 do
      # Here we can do anything such as:
      within release_path do
        with rails_env: fetch(:rails_env) do
          execute :rake, 'tmp:cache:clear'
          execute :rake, 'sitemap:refresh'
        end
      end
    end
  end
end
```

タスクの実行時に指定できる `sequence`、`groups`、`parallel` の説明はこちら。

{% blockquote Capistrano Version 3 Release Announcement http://capistranorb.com/2013/06/01/release-announcement.html %}
Other modes for parallelism include:

```ruby
    # Capistrano 3.0.x
    on :all, in: :groups, max: 3, wait: 5 do
      # Take all servers, in groups of three which execute in parallel
      # wait five seconds between groups of servers.
      # This is perfect for rolling restarts
    end

    on :all, in: :sequence, wait: 15 do
      # This takes all servers, in sequence and waits 15 seconds between
      # each server, this might be perfect if you are afraid about
      # overloading a shared resource, or want to defer the asset compilation
      # over your cluster owing to worries about load
    end

    on :all, in: :parallel do
      # This will simply try and execute the commands contained within
      # the block in parallel on all servers. This might be perfect for kicking
      # off something like a Git checkout or similar.
    end
```
{% endblockquote %}

Capistrano2 と互換性がないですが、新規プロジェクトには使えるんじゃないかと。
