import {CurrencyConverter} from "/common/js/currency_util.js";
import {DatatableAttribute, DebounceUtil} from "/common/js/app.js";

export const ServiceList = (function () {
    const module = {
        searchInputSelector: $('#search-input'),
        serviceListTableSelector: $('#service-list-table'),
        categorySelectSelector: $('#category-select'),
    };

    module.init = () => {
        renderMedicalServiceListTable();
        handleSearchInputChange();
        handleCategorySelectChange();
    }

    const handleSearchInputChange = () => {
        module.searchInputSelector.on('input', function() {
            DebounceUtil.debounce(
                renderMedicalServiceListTable,
                DebounceUtil.delayTime,
                'serviceSearch'
            )();
        });
    }

    const handleCategorySelectChange = () => {
        module.categorySelectSelector.on('changed.bs.select', function () {
            DebounceUtil.debounce(
                renderMedicalServiceListTable,
                DebounceUtil.delayTime,
                'serviceSearchCategory'
            )();
        });
    }

    const getMedicalServiceListFilter = () => {
        return {
            keyword: module.searchInputSelector.val().trim(),
            categoryIds: module.categorySelectSelector.selectpicker('val')
        }
    }

    const renderMedicalServiceListTable = () => {
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
                {data: 'serviceCategories'},
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
                    targets: [0, 3, 5],
                    className: "text-center"
                },
                {
                    targets: 2,
                    render: (data) => {
                        return data && data.map(item =>`<button class="btn btn-sm btn-secondary mt-2">${item.title}</button>` );
                    }
                },
                {
                    targets: 3,
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
