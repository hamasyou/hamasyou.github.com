jQuery(function($) {
    FastClick.attach(document.body);
    $('a[rel~="external"]').attr('target', '_blank');

    $('.scroll-to-top').on('click', function() {
        $('body,html').animate({
            scrollTop: 0
        }, 400);
        return false;
    });

    $('a[href="#disqus_thread"]').on('click', function() {
        var p = $('#disqus_thread').offset().top;
        $('body,html').animate({
            scrollTop: p
        }, 400);
        return false;
    });

    <!-- slide in/out -->
    $('a.excerpt-link').click(function() {
        var pass = $(this).attr('href');
        $('#container').transition({x: -$(window).width() + 'px', opacity: '0'}, 500, function() {
            location.href = pass;
            setTimeout(function() {
                $('#container').css({marginLeft: '0', opacity: '1'})
            }, 100);
        });
        return false;
    });
});
