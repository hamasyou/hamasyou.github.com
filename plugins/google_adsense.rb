module Jekyll
  module Filters
    def inject_adsense(input)
      ads =<<-EOS
<script async src="//pagead2.googlesyndication.com/pagead/js/adsbygoogle.js"></script>
<ins class="adsbygoogle"
     style="display:inline-block;width:728px;height:90px"
     data-ad-client="ca-pub-0904452411138796"
     data-ad-slot="0907364863"></ins>
<script>
(adsbygoogle = window.adsbygoogle || []).push({});
</script>
      EOS

      input.sub(/<!--\s*more\s*-->/, ads)
    end
  end
end
