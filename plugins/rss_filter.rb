module Jekyll
  module Filters
    def rfc822(input)
      time(input).rfc822
    end

    private

    # thanks jekyll
    def time(input)
      case input
      when Time
        input
      when String
        Time.parse(input)
      else
        Jekyll.logger.error "Invalid Date:", "'#{input}' is not a valid datetime."
        exit(1)
      end
    end

  end
end
