import {App} from "/common/js/app.js";
import {FormHandler} from "/common/js/form.js";

/* Init global events */
(function () {
    $.ajaxSetup({
        headers: {'Accept-Language': 'vi'},
        beforeSend: function () {
            $('form').find('button').prop('disabled', true);
            $('.modal').find('button').prop('disabled', true);
        },
        complete: function () {
            $('form').find("button").prop('disabled', false);
            $('.modal').find('button').prop('disabled', false);
            $('.modal').modal('hide');
        }
    });

    $('.selectpicker').selectpicker('setStyle', 'btn-outline-secondary');

    App.keepSidebarDropdownOpen();
    App.keepSidebarTabActive();

    $('.modal').on('hidden.bs.modal', function () {
        FormHandler.clearAllInputs($(this));
        FormHandler.clearAllErrors($(this));
    })

    const tooltipTriggerList = document.querySelectorAll('[data-bs-toggle="tooltip"]');
    const tooltipList = [...tooltipTriggerList].map(tooltipTriggerEl => new bootstrap.Tooltip(tooltipTriggerEl));
})();