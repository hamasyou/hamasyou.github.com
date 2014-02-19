require 'twitter'

class TwitterBot

  class << self
    def update(post)
      jekyll_config = IO.read('_config.yml')
      yaml          = YAML.load(jekyll_config)
      url           = post.id
      title         = post.title


      twitter_client ||= Twitter::REST::Client.new do |config|
        config.consumer_key        = yaml['twitter_consumer_key']
        config.consumer_secret     = yaml['twitter_consumer_secret']
        config.access_token        = yaml['twitter_access_token']
        config.access_token_secret = yaml['twitter_access_token_secret']
      end

      begin
        twitter_client.update("記事かきました / #{title} - #{yaml['title']} #{url}")
      rescue => e
        p e
      end
    end
  end

end
