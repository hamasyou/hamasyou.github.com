module Jekyll
  class TagList < Liquid::Tag

    def initialize(tag_name, markup, tokens)
      @opts = {}
      if markup.strip =~ /\s*counter:(\w+)/iu
        @opts['counter'] = ($1 == 'true')
        markup           = markup.strip.sub(/counter:\w+/iu, '')
      end
      super
    end

    def render(context)
      html    = ""
      config  = context.registers[:site].config
      tag_dir = config['root'] + config['tag_dir'] + '/'
      tags    = context.registers[:site].tags
      html << '<ul class="list-inline list-group-item">'
      tags.keys.sort_by { |str| str.downcase }.each do |tag|
        url = tag_dir + tag.to_url
        html << "<li><a class=\"label label-default\" href=\"#{url}\">#{tag}</a></li>"
      end
      html << '</ul>'
      html
    end
  end
end

Liquid::Template.register_tag('tag_list', Jekyll::TagList)
