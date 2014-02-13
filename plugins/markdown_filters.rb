require 'nokogiri'
require './plugins/post_filters'

module Jekyll
  class MarkdownFilters < PostFilter
    def pre_render(post)
    end

    def post_render(post)
      if post.ext.match('html|textile|markdown|md|haml|slim|xml')
        post.content.gsub!(/<p>(<a.*?>[^<].*?<\/a>)<\/p>/, '<i class="fa fa-hand-o-right"></i> \1')

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

        # img に img-thumbnail を付ける
        doc.search('img').each do |img|
          unless img.attr('class') =~ /img-thumbnail/
            img['class'] = "#{img['class']} img-thumbnail".strip
          end
        end

        post.content = doc.inner_html
      end
    end
  end
end
