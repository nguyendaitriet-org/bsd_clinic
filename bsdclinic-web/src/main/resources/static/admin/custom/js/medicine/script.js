import {App} from "/common/js/app.js";
import {CurrencyConverter} from "/common/js/currency_util.js";
import {FormHandler} from "/common/js/form.js";
import {DatatableAttribute} from "/common/js/app.js";

export const MedicineCreation = (function () {
    const module = {
        createMedicineFormSelector: $('#create-medicine-form'),
        saveMedicineButtonSelector: $('.btn-save')
    };

    module.init = () => {
        handleCreateMedicineFormSubmission();
    }

    const handleCreateMedicineFormSubmission = () => {
        module.createMedicineFormSelector.on('submit', function (e) {
            e.preventDefault();
            const formData = new FormData(this);
            const medicineCreationParams = Object.fromEntries(
                Array.from(formData.entries()).map(([key, value]) => [key, value === '' ? null : value])
            );

            $.ajax({
                headers: {
                    "accept": "application/json",
                    "content-type": "application/json"
                },
                type: 'POST',
                url: API_ADMIN_MEDICINE,
                data: JSON.stringify(medicineCreationParams),
            })
                .done(() => {
                    App.showSweetAlert('success', createSuccess, '');
                    setTimeout(() => window.location.href = ADMIN_MEDICINE_INDEX, 1000);
                })
                .fail((jqXHR) => {
                    App.handleResponseMessageByStatusCode(jqXHR);
                    FormHandler.handleServerValidationError(module.createMedicineFormSelector, jqXHR)
                })
        })
    }

    return module;
})();

export const MedicineList = (function () {
    const module = {
        searchInputSelector: $('#search-input'),
        searchSubmitButtonSelector: $('#submit-btn'),
        cancelSearchButtonSelector: $('#cancel-btn'),

        userListTableSelector: $('#medicine-list-table'),
    };

    module.init = () => {
        module.renderMedicineListTable();
        handleSearchSubmissionButton();
        handleCancelSearchButton();
    }

    const handleCancelSearchButton = () => {
        module.cancelSearchButtonSelector.on('click', function () {
            module.searchInputSelector.val('');
            module.renderMedicineListTable();
        })
    }

    const handleSearchSubmissionButton = () => {
        module.searchSubmitButtonSelector.on('click', function () {
            module.renderMedicineListTable();
        });
    }

    const getMedicineListFilter = () => {
        return {
            keyword: module.searchInputSelector.val().trim(),
        }
    }

    module.renderMedicineListTable = () => {
        const medicineFilter = getMedicineListFilter();
        const userListDatatable = module.userListTableSelector.DataTable({
            ajax: {
                contentType: 'application/json',
                type: 'POST',
                url: API_ADMIN_MEDICINE_LIST,
                data: function (d) {
                    return JSON.stringify({...d, ...medicineFilter});
                }
            },
            columns: [
                {data: null},
                {data: 'title'},
                {data: 'unitPrice'},
                {data: 'unit'},
                {data: null},
            ],
            serverSide: true,
            bJQueryUI: true,
            destroy: true,
            paging: true,
            searching: false,
            lengthChange: true,
            info: false,
            ordering: false,
            pagingType: 'simple_numbers',
            columnDefs: [
                {
                    targets: [0, 2, 3, 4],
                    className: "text-center"
                },
                {
                    targets: 2,
                    render: (data) => {
                        return CurrencyConverter.formatCurrencyVND(data);
                    }
                },
                {
                    targets: -1,
                    render: (data, type, row) => {
                        return `
                            <button class="btn btn-sm btn-primary update-medicine-btn">
                                <i class="fa fa-edit"></i>
                            </button>
                            <button class="btn btn-sm btn-danger delete-service-btn">
                                <i class="fa fa-trash"></i>
                            </button>
                        `;
                    }
                }
            ],
            language: DatatableAttribute.language
        });

        DatatableAttribute.renderOrdinalColumn(userListDatatable, 0);
    }

    return module;
})();


(function () {
    MedicineCreation.init();
    MedicineList.init();
})();