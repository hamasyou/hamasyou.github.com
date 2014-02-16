require './misc/open-uri-post.rb'
# require 'rest_client'

open('http://localhost:9292', {'postdata' => 'name=hamasyou'}).read
# RestClient.post('http://localhost:9292', name: 'hamasyou')
