#!/usr/bin/env ruby

require 'date'
require 'mustache'
require 'stringex'
require 'cgi'

export     = File.open(ARGV[0])
target_dir = ARGV[1]

class Post < Mustache
  self.template = <<-MARKDOWN
---
layout: post
title: "{{title}}"
date: {{formatted_date}}
comments: {{comments}}
categories: [{{category}}]
keywords: "{{keyword}}"
tags: [{{tags}}]
author: {{author}}
amazon_url: "{{{cf50_amazonurl}}}"
amazon_author: "{{{cf50_cf_4}}}"
amazon_image: "{{{cf50_url}}}"
amazon_publisher: "{{{cf50_cf}}}"
---

{{{formatted_body}}}
MARKDOWN

  attr_accessor :author, :basename, :title, :category, :keyword, :tags, :comments, :status, :body, :cf50_amazonurl, :cf50_cf_4, :cf50_url, :cf50_cf, :cf50_cf_5

  def initialize
    @body = []
  end

  def date=(date)
    case date
    when String
      @date = DateTime.strptime(date, "%m/%d/%Y %I:%M:%S %p")
    else
      @date = date
    end
  end

  def date
    @date
  end

  def formatted_date
    @date.strftime("%Y-%m-%d %H:%M")
  end

  def comments=(comments)
    @comments = comments == "1" ? true : false
  end

  def formatted_body
    body.join.
      gsub(/<a href="(.*?)".*>(.*?)<\/a>/i, '<a href="\1" rel="external nofollow">\2</a>').
      gsub("<!--more-->", "\n\n<!-- more -->\n\n")
  end

  def file_name
    file_name = if self.cf50_amazonurl != '' && (self.cf50_amazonurl =~ /product\/(.*?)[\?\/]/ || self.cf50_amazonurl =~ /ASIN\/(.*?)\//)
       $1
    else
      self.basename.to_url
    end

    "#{date.strftime("%Y-%m-%d")}-#{file_name}.md"
  end
end

posts = [Post.new]

KEYWORDS = /(AUTHOR|TITLE|BASENAME|STATUS|COMMENTS|CATEGORY|DATE|TAGS|CF50_AMAZONURL|CF50_CF_4|CF50_URL|CF50_CF|CF50_CF_5): (.*)/

def attr_and_value(line)
  line =~ KEYWORDS
  ["#{$1.downcase}=", $2]
end

puts " # Reading from data '#{ARGV[0]}' ..."

puts " # Generating Markdown ..."

comment_section = false
keyword_section = false
excerpt_section = false
ping_section = false
blockquote_section = false
blockquote = {body: []}

export.each do |line|
  if line.strip == "--------"
    posts << Post.new
    print '.'
    comment_section = false
    keyword_section = false
    blockquote_section = false
    ping_section = false
    blockquote = {body: []}
    next
  end

  if line.strip == '-----'
    comment_section = false
    keyword_section = false
    excerpt_section = false
    ping_section = false
    blockquote_section = false
    blockquote = {body: []}
  end

  next if comment_section
  next if excerpt_section
  next if ping_section

  case line.strip
  when KEYWORDS
    posts.last.send(*attr_and_value(line))
  when /EXTENDED BODY:/
    posts.last.body << '<!--more-->'
    next
  when /BODY/
    next
  when /CF50_CF_5/, /CF50_AMAZONURL/, /CF50_CF_3/, /CF50_CF/, /CF50_AMAZONURL/, /CF50_URL/
    next
  when /EXCERPT:/
    excerpt_section = true
    next
  when /CONVERT BREAKS/
    next
  when /ALLOW PINGS/
    next
  when "-----"
    next
  when /KEYWORDS:/
    keyword_section = true
    next
  when /COMMENT:/
    comment_section = true
    next
  when /PING:/
    ping_section = true
    next
  else
    if keyword_section
      posts.last.keyword = line.strip
    else
      if line =~ /<blockquote>/
        blockquote_section = true
      elsif line =~ /<\/blockquote>/
        blockquote_section = false
        blockquote[:body].unshift "{% blockquote #{blockquote[:cite]} %}"
        blockquote[:body].push '{% endblockquote %}'
        posts.last.body << blockquote[:body].join('')
        posts.last.body << "\n"
        blockquote = {body: []}
      else
        if blockquote_section
          if line =~ /<cite>(.*?)<\/cite>/
            blockquote[:cite] = $1
          else
            blockquote[:body] << line
          end
        else
          posts.last.body << line
        end
      end
    end
  end
end

puts
puts " # Writing files to #{target_dir} ..."
`mkdir -p #{target_dir}`
`cd #{target_dir} && rm -rf _posts && mkdir _posts`

ok, fails = 0, 0

posts.each do |post|
  begin
    File.open(File.join(target_dir, "_posts", post.file_name), "w+") { |f| f.puts post.render }
    puts " -> #{post.file_name}"
    ok += 1
  rescue => e
    puts " # Could not write file for '#{post.title}' #{e}"
    fails += 1
  end
end

puts
puts " # All done! (#{ok}/#{fails})"