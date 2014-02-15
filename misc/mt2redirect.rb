#!/usr/bin/env ruby

require 'date'
require 'mustache'
require 'stringex'
require 'cgi'

export     = File.open(ARGV[0])

class Post < Mustache
  self.template = <<-MARKDOWN
Redirect 301 {{src_path}} /blog/{{dest_name}}
MARKDOWN

  attr_accessor :src_path, :status, :dest_path, :basename, :cf50_amazonurl

  def initialize(i)
    @src_path = "/archives/#{"%06d" % i}"
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
    body
  end

  def dest_name
    file_name = if self.cf50_amazonurl != '' && (self.cf50_amazonurl =~ /product\/(.*?)[\?\/]/ || self.cf50_amazonurl =~ /ASIN\/(.*?)\//)
       $1
    else
      self.basename.to_url
    end

    "#{date.strftime("%Y/%m/%d")}/#{file_name}/"
  end
end

posts = [Post.new(357)]

KEYWORDS = /(BASENAME|STATUS|CF50_AMAZONURL|DATE): (.*)/

def attr_and_value(line)
  line =~ KEYWORDS
  ["#{$1.downcase}=", $2]
end

puts " # Reading from data '#{ARGV[0]}' ..."

puts " # Generating Redirect ..."

i = 360
export.each do |line|
  if line.strip == "--------"
    i = 363 if i == 361
    i = 371 if i == 370
    i = 379 if i == 378
    i = 390 if i == 389
    i = 396 if i == 395
    i = 401 if i == 399
    i = 404 if i == 403
    i = 406 if i == 405
    last_post = posts.last
    if posts.last.status != 'Publish'
      posts.pop
      i = i - 1
    end
    posts << Post.new(i)
    print '.'
    i = i + 1
    next
  end

  case line.strip
  when KEYWORDS
    posts.last.send(*attr_and_value(line))
  end
end

puts
puts " # Writing files to _redirects.htaccess ..."
ok, fails = 0, 0

posts.each do |post|
  File.open('_redirects.htaccess', "a+") { |f| f.write post.render }
  begin
    puts " -> #{post.dest_name}"
    ok += 1
  rescue => e
    puts " # Could not write file for '#{post}' #{e}"
    fails += 1
  end
end

puts
puts " # All done! (#{ok}/#{fails})"