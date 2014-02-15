require 'nokogiri'
require 'open-uri'

open('source/_redirects.htaccess', 'r') do |f|
    lines = f.readlines

    lines.each do |line|
        _, _, src_path, dest_path = line.split(' ')
        if src_path
            doc = Nokogiri::HTML(open("http://localhost:4000#{src_path}").read)
            url = "http://localhost:4000#{doc.css('a')[0].attr('href')}"
            begin
                status, msg = open(url).status
            rescue => e
                puts "NotFound #{url}"
            end
        end
    end
end