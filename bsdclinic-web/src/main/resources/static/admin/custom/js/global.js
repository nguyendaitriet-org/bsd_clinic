/* Init global events */
(function () {
    $.ajaxSetup({
        headers: {'Accept-Language': 'vi'}
    });

    $('.selectpicker').selectpicker('setStyle', 'btn-outline-secondary');

    $('.daterange').daterangepicker({
        locale: {
            format: 'DD/MM/YYYY'
        }
    });
})();