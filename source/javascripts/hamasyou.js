jQuery(function($) {
    FastClick.attach(document.body);
    $('a[rel~="external"]').attr('target', '_blank');

    $('#scroll-to-top').on('click', function() {
        $('body,html').animate({
            scrollTop: 0
        }, 400);
    });
});
