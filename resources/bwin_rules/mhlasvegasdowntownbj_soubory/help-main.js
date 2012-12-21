(function ($) {
    $(function () {
        var menusToToggle = $('#main').find('.menu.ui-widget-content-body ul > li > ul')
                                                .parent();
        $('#main').find('.menu .ui-widget-content-body > ul > li > ul').show();
        // Add toggles in left nav menus.
        menusToToggle.bwin_toggle();

        $('.backlink').click(function (e) {
            e.preventDefault();
            history.back();
            return false;
        });
        $('.glossary').miniTip();

        if (window.opener != null) {
            var openParentLinks = $('a.open-parent');

            openParentLinks.each(function () {
                $(this).click(function (e) {
                    if (!window.opener.closed) {
                        e.preventDefault();
                        window.opener.location = this.href;
                        window.opener.focus();
                        return false;
                    }
                });
            });
        }
        //add spans with js for faq items arrow in ie7
        if ($('.ie7').length == 1) {
            $('.ie7 #help-content').find('.help-link-section a').append('&nbsp;<span>&nbsp;</span>');
        }


        //add toplinks in front of all h2 if there is no toplink already present 
        if ($(window).height() < $('.container').height()) {
            //get all "top-level" h2
            var topLinkHeaders = $('#help-content').find('h2').not('li h2');
            //check if there is a toplink present on the page 
            //(at least one has to come from sitecore for translations)
            if ($('#help-content').find('.toplink-hidden').length) {
                topLinkHeaders.each(function (i) {
                    var toplinkThere = $(this).nextUntil('h2').is('.toplink-box');
                    //dont do it for the last header, because it has no next h2, and check if toplinks are already present
                    if (i != topLinkHeaders.length-1 && !toplinkThere) {
                        //clone the present toplink and add before next h2
                        $('#help-content').find('.toplink-box:last').clone().removeClass('toplink-hidden').insertBefore($(topLinkHeaders[i+1]));
                    }
                });
            }

            //show the last toplink that comes from the template
            var lastlink = $('#help-content').find('.toplink-hidden');
            var lastHTwo = $('#help-content').find('h2:last').not('li h2');
            if ($(lastlink).is(":hidden") && !$(lastHTwo).nextAll().is('.toplink-box') && topLinkHeaders.length > 1) {
                $(lastlink).removeClass('toplink-hidden');
            }
        }
    });
})(jQuery);

