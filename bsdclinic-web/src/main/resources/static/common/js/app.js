import {FormHandler} from "./form.js";

export const App = (function () {
    const module = {
        errorToastSelector: $('#error-toast'),
        successToastSelector: $('#success-toast'),
    };

    module.handleResponseMessageByStatusCode = (jqXHR) => {
        switch (jqXHR.status) {
            case 400:
                module.showErrorMessage(ERROR_400);
                break;
            case 401:
                module.showErrorMessage(jqXHR.responseJSON.message);
                break;
            case 403:
                module.showErrorMessage(ERROR_403);
                break;
            case 404:
                module.showErrorMessage(ERROR_404);
                break;
            case 200:
                module.showSuccessMessage(operationSuccess);
                break;
            default:
                module.showErrorMessage(ERROR_500);
        }
    }

    module.showSuccessMessage = (message) => {
        module.successToastSelector.find('.toast-body').text('').text(message);
        module.successToastSelector.toast('show');
    }

    module.showErrorMessage = (message) => {
        module.errorToastSelector.find('.toast-body').text('').text(message);
        module.errorToastSelector.toast('show');
    }

    module.showSweetAlert = (type, title, content) => {
        Swal.fire({
            icon: type,
            title: title,
            text: content,
            timer: 1000
        });
    }

    module.showSweetAlertConfirmation = (type, title, content) => {
        return Swal.fire({
            icon: type,
            title: title,
            text: content,
            showCancelButton: true,
            confirmButtonText: acceptTitle,
            denyButtonText: closeTitle
        });
    }

    module.clearAllModalInputsAfterClosing = () => {
        $('.modal').on('hidden.bs.modal', function () {
            FormHandler.clearAllInputs($(this));
            FormHandler.clearAllErrors($(this));
        })
    }

    module.enableEnterKeyboard = (formSelector, buttonSelector) => {
        formSelector.keypress(function (event) {
            let keycode = (event.keyCode ? event.keyCode : event.which);
            if (keycode == '13') {
                buttonSelector.click();
                return false;
            }
        });
    }

    module.keepSidebarTabActive = () => {
        const url = window.location;
        const targetLinkSelector = $('.sidebar-content .nav-item a').filter(function () {
            return this.href == url;
        });
        targetLinkSelector.closest('li').addClass('active');
    }

    module.keepSidebarDropdownOpen = () => {
        const url = window.location;
        const targetLinkSelector = $('.sidebar-content .nav-item .collapse a').filter(function () {
            return this.href == url;
        });
        targetLinkSelector.closest('li').addClass('active');
        targetLinkSelector.closest('.nav-item').addClass('active');
        targetLinkSelector.closest('.collapse').addClass('show');
    }

    module.disableElementsInAjaxProgress = () => {
        $(document)
            .ajaxStart(function () {
                $('button').prop('disabled', true).css('cursor', 'wait');
                $('a').css('pointer-events', 'none').css('cursor', 'wait');
            })
            .ajaxStop(function () {
                $('button').prop('disabled', false).css('cursor', 'pointer');
                $('a').css('pointer-events', 'auto').css('cursor', 'pointer');
            });
    }

    module.hideModalAfterAjax = () => {
        $(document).on("ajaxSuccess", function () {
            $('.modal').modal('hide');
        });
    }

    return module;
})();

export const DatatableAttribute = (function () {
    const module = {};

    module.language = {
        info: resultCount,
        infoEmpty: noResult,
        lengthMenu: menuLength,
        paginate: {
            previous: previousPage,
            next: nextPage
        },
        aria: {
            paginate: {
                previous: 'Previous',
                next: 'Next'
            }
        },
        emptyTable: emptyTable,
        search: searchTitle
    }

    /* Create increment number for ordinal column of datatable */
    module.renderOrdinalColumn = (datatableSelector, columnIndex) => {
        datatableSelector.on('draw.dt', function () {
            const PageInfo = datatableSelector.page.info();
            datatableSelector.column(columnIndex, {page: 'current'}).nodes().each(function (cell, i) {
                cell.innerHTML = i + 1 + PageInfo.start;
            });
        });
    }

    return module;
})();

// export const ProgressingBar = (function () {
//     const module = {};
//
//     const configMode = () => {
//         topbar.config({
//             autoRun: true,
//             barThickness: 6,
//             barColors: {
//                 "0": "rgba(26,  188, 156, .9)",
//                 ".25": "rgba(52,  152, 219, .9)",
//                 ".50": "rgba(241, 196, 15,  .9)",
//                 ".75": "rgba(230, 126, 34,  .9)",
//                 "1.0": "rgba(211, 84,  0,   .9)",
//             },
//             shadowBlur: 10,
//             shadowColor: "rgba(0,   0,   0,   .6)",
//             className: null,
//         });
//     }
//
//     module.init = () => {
//         configMode();
//         $(document)
//             .ajaxStart(function () {
//                 topbar.show();
//             })
//             .ajaxStop(function () {
//                 topbar.hide();
//             });
//     }
//     return module;
// })();

export const LocalStorage = (function () {
    const module = {};

    module.setJsonItem = (name, value) => {
        localStorage.setItem(name, JSON.stringify(value));
    }

    module.getJsonItem = (name) => {
        return JSON.parse(localStorage.getItem(name));
    }

    module.removeItem = (name) => {
        localStorage.removeItem(name);
    }

    module.removeAll = () => {
        localStorage.clear();
    }

    return module;
})();