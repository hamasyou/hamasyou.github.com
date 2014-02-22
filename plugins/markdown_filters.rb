require 'nokogiri'
require './plugins/post_filters'
require "active_support/core_ext/object/try"

module Jekyll
  class MarkdownFilters < PostFilter
    def pre_render(post)
    end

    def post_render(post)
      if post.is_post?
        post.content.gsub!(/<p>(<a.*?>[^<].*?<\/a>)<\/p>/, '<p><i class="fa fa-hand-o-right"></i> \1</p>')

        doc = Nokogiri::HTML(post.content)

        # link に external nofollow と title を付ける
        doc.search('a[href^="http://"]', 'a[href^="https://"]').each do |link|
          unless link.attr('rel') =~ /nofollow/
            link['rel'] = "#{link['rel']} external nofollow".strip
          end
          unless link.attr('title')
            link['title'] = link.text
          end
        end

        doc.search('p > strong').each do |em|
          em['class'] = 'text-danger'
        end

        # img タグの設定
        doc.search('img').each do |img|
          unless img.attr('class') =~ /img-thumbnail/
            img['class'] = "#{img['class']} img-thumbnail".strip
          end
          unless img.attr('title')
            img['title'] = img['alt']
          end
        end

        post.content = doc.css('body')[0].try(:inner_html) || doc.inner_html
      end
    end
  end
end
