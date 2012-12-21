(function ($) {
    $(function () {
        if ($.bwin === undefined) {
            $.bwin = {};
        }

        // Helper for toggling menus.
        var _toggle = function (iconSpan) {
            iconSpan.toggleClass('ui-icon-triangle-1-e')
                    .toggleClass('ui-icon-triangle-1-s');
            $(this).children().last().toggle();
        };

        // Helper for making toggle icon spans.
        var _makeToggleIconSpan = function () {
            return $('<span>')
                    .addClass('ui-icon ui-icon-triangle-1-s');
        };

        // Can be used to add toggles.
        $.fn.bwin_toggle = function () {
            this.each(function () {
                var isActive = $(this).find('a.activeItem').length > 0;
                var toggleIconSpan = _makeToggleIconSpan();

                if (!isActive) {
                    _toggle.call(this, toggleIconSpan);
                }

                $(this).addClass('toggle').prepend(toggleIconSpan);

                // Stop event bubbling if clicking a link.
                $(this).find('a[href]').click(function (e) {
                    e.stopPropagation();
                });

                // Handle toggle clicks.
                $(this).click(function () {
                    _toggle.call(this, toggleIconSpan);
                });
            });

            return this;
        };
    });
})(jQuery);
