require 'open-uri'

open('source/_redirects.htaccess', 'r') do |f|
    lines = f.readlines

    lines.each do |line|
        _, _, src_path, dest_path = line.split(' ')
        if src_path
            status, msg = open("http://localhost:4000#{src_path}").status
            puts status
        end
    end
end