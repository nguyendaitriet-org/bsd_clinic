/* Init global events */
(function () {
    $.ajaxSetup({
        headers: {'Accept-Language': 'vi'},
        beforeSend: function () {
            $('form').find('.error-text').remove();
            $('form').find('button').prop('disabled', true);
            $('.modal').find('button').prop('disabled', true);
        },
        complete: function () {
            $('form').find("button").prop('disabled', false);
            $('.modal').find('button').prop('disabled', false);
        }
    });

    $('.selectpicker').selectpicker('setStyle', 'btn-outline-secondary');
})();