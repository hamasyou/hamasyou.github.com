require 'rest_client'
require 'json'
require 'pp'

response = RestClient.get 'http://www.kimonolabs.com/api/4b5q146s?apikey=81c2b7add1263b7e459f5ed58b5f6504'
json = JSON.parse(response.to_str)
pp json