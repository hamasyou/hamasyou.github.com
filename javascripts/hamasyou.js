jQuery(function($) {
    FastClick.attach(document.body);
    $('a[rel~="external"]').attr('target', '_blank');

    $('.scroll-to-top').on('click', function() {
        $('body,html').animate({
            scrollTop: 0
        }, 400);
        return false;
    });
//
//    $('a[href="#disqus_thread"]').on('click', function() {
//        var p = $('#disqus_thread').offset().top;
//        $('body,html').animate({
//            scrollTop: p
//        }, 400);
//        return false;
//    });

    // toc
    if ($('#toc')[0]) {
        (function() {
            $("#toc").append('<p class="h2">もくじ</p>');
            var list = $('<ul class="table-of-contents"></ul>');
            $('.entry-content h2, .entry-content h3').each(function(i) {
                var current = $(this);
                current.attr("id", "header" + i);
                if (current[0].tagName === 'H2') {
                    list.append('<li class="toc2"><a href="#header' + i + '" title="' + current.text() + '">' + current.text() + '</a></li>');
                } else {
                    list.append('<li class="toc3"><a href="#header' + i + '" title="' + current.text() + '">' + current.text() + '</a></li>');
                }
            });
            $("#toc").append(list);
        })();
    }

    $("a[href^=#]").click(function() {
        var loc = $(this.hash);
        var offset = $(loc).offset().top;
        $("body,html").animate({
            scrollTop: offset
        }, 400);
        return false;
    });
});
