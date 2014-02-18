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
});
