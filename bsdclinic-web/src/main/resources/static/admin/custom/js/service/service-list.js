import {DatatableAttribute} from "/common/js/app.js";

export const ServiceList = (function () {
    const module = {
        findAllMedicalServicesByFilterUrl: '/api/services/list',

        searchInputSelector: $('#search-input'),
        searchSubmitButtonSelector: $('#submit-btn'),
        cancelSearchButtonSelector: $('#cancel-btn'),

        userListTableSelector: $('#appointment-list-table'),

    };

    module.init = () => {
        // initDateRangePicker();
        module.renderMedicalServiceListTable();
        handleSearchSubmissionButton();
        handleCancelSearchButton();
    }

    // const initDateRangePicker = () => {
    //     module.userCreatedAtRangePicker = new Lightpick({
    //         field: module.creationDateRangeInputSelector[0],
    //         singleDate: false,
    //         lang: 'vi'
    //     });
    // }

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

    const toRoleMap = (userRoles) => {
        return userRoles.reduce((acc, role) => {
            acc[role.roleId] = role.code;
            return acc;
        }, {});
    }

    module.renderMedicalServiceListTable = () => {
        const medicalServiceFilter = getMedicalServiceListFilter();
        const userListDatatable = module.userListTableSelector.DataTable({
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
                        return data.toLocaleString('vi-VN') + '₫';
                    }
                },
                {
                    targets: -1,
                    render: (data, type, row) => {
                        return `
                        <button class="btn btn-sm btn-primary"
                            data-bs-toggle="modal" data-bs-target="#update-medical-service-modal">
                        <i class="fa fa-edit"></i>
                        </button>
                        <button class="btn btn-sm btn-danger delete-service-btn" data-id="${row.id}">
                            <i class="fa fa-trash"></i> Xóa
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
    ServiceList.init();
})();
