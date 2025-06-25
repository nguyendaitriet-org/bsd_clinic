import {CurrencyConverter} from "/common/js/currency_util.js";
import {DatatableAttribute} from "/common/js/app.js";

export const ServiceList = (function () {
    const module = {
        searchInputSelector: $('#search-input'),
        searchSubmitButtonSelector: $('#submit-btn'),
        cancelSearchButtonSelector: $('#cancel-btn'),
        serviceListTableSelector: $('#service-list-table'),
    };

    module.init = () => {
        // initDateRangePicker();
        module.renderMedicalServiceListTable();
        handleSearchSubmissionButton();
        handleCancelSearchButton();
    }

    const handleCancelSearchButton = () => {
        module.cancelSearchButtonSelector.on('click', function () {
            module.searchInputSelector.val('');
            module.renderMedicalServiceListTable();
        })
    }

    const handleSearchSubmissionButton = () => {
        module.searchSubmitButtonSelector.on('click', function () {
            module.renderMedicalServiceListTable();
        });
    }

    const getMedicalServiceListFilter = () => {
        return {
            keyword: module.searchInputSelector.val().trim(),
        }
    }

    module.renderMedicalServiceListTable = () => {
        const medicalServiceFilter = getMedicalServiceListFilter();
        const medicalServiceListDatatable = module.serviceListTableSelector.DataTable({
            ajax: {
                contentType: 'application/json',
                type: 'POST',
                url: API_ADMIN_MEDICAL_SERVICE_LIST,
                data: function (d) {
                    return JSON.stringify({...d, ...medicalServiceFilter});
                }
            },
            columns: [
                {data: null},
                {data: 'title'},
                {data: 'price'},
                {data: 'description'},
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
                    targets: [0, 2, 4],
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
                            <button class="btn btn-sm btn-warning show-updating-modal-btn">
                                <i class="fa fa-edit"></i>
                            </button>
                            <button class="btn btn-sm btn-danger show-deletion-confirmation-btn">
                                <i class="fa fa-trash"></i>
                            </button>
                    `;
                    }
                }
            ],
            language: DatatableAttribute.language
        });

        DatatableAttribute.renderOrdinalColumn(medicalServiceListDatatable, 0);
    }

    return module;
})();

(function () {
    ServiceList.init();
})();
