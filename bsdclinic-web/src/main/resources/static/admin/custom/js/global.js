import {App, ProgressingBar} from "/common/js/app.js";
import {FormHandler} from "/common/js/form.js";

/* Init global events */
(function () {
    $.ajaxSetup({
        headers: {'Accept-Language': 'vi'}
    });

    App.disableElementsInAjaxProgress();

    ProgressingBar.init();

    $(document).ajaxStop(function () {
        setTimeout(() => {
            $('.error-text').remove();
        }, 7000)
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