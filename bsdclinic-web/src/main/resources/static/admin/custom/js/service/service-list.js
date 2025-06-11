import {DatatableAttribute} from "/common/js/app.js";
import {DateTimePattern} from "/common/js/constant.js";
import {DateTimeConverter} from "/common/js/datetime_util.js";

export const ServiceList = (function () {
    const module = {
        findAllUsersByFilterUrl: '/api/users/list',

        searchInputSelector: $('#search-input'),
        roleSelectSelector: $('#role-select'),
        statusSelectSelector: $('#status-select'),
        creationDateRangeInputSelector: $('#creation-daterange-input'),
        searchSubmitButtonSelector: $('#submit-btn'),
        cancelSearchButtonSelector: $('#cancel-btn'),

        userListTableSelector: $('#user-list-table'),

    };

    module.init = () => {
        initDateRangePicker();
        module.renderUserListTable();
        handleSearchSubmissionButton();
        handleCancelSearchButton();
    }

    const initDateRangePicker = () => {
        module.userCreatedAtRangePicker = new Lightpick({
            field: module.creationDateRangeInputSelector[0],
            singleDate: false,
            lang: 'vi'
        });
    }

    const handleCancelSearchButton = () => {
        module.cancelSearchButtonSelector.on('click', function () {
            module.searchInputSelector.val('');
            module.roleSelectSelector.selectpicker('deselectAll');
            module.statusSelectSelector.selectpicker('deselectAll');
            module.userCreatedAtRangePicker.setDateRange(null, null)
            module.renderUserListTable();
        })
    }

    const handleSearchSubmissionButton = () => {
        module.searchSubmitButtonSelector.on('click', function () {
            module.renderUserListTable();
        });
    }

    const getUserListFilter = () => {
        return {
            keyword: module.searchInputSelector.val().trim(),
            roleIds: module.roleSelectSelector.val(),
            status: module.statusSelectSelector.val(),
            createdFrom: DateTimeConverter.convertMomentToDateString(module.userCreatedAtRangePicker.getStartDate(), DateTimePattern.API_DATE_FORMAT),
            createdTo: DateTimeConverter.convertMomentToDateString(module.userCreatedAtRangePicker.getEndDate(), DateTimePattern.API_DATE_FORMAT),
        }
    }

    const toRoleMap = (userRoles) => {
        return userRoles.reduce((acc, role) => {
            acc[role.roleId] = role.code;
            return acc;
        }, {});
    }

    module.renderUserListTable = () => {
        const userFilter = getUserListFilter();
        const roleMap = toRoleMap(userRoles);
        const userListDatatable = module.userListTableSelector.DataTable({
            ajax: {
                contentType: 'application/json',
                type: 'POST',
                url: API_ADMIN_USER_LIST,
                data: function (d) {
                    return JSON.stringify({...d, ...userFilter});
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
                    targets: 2, // Cột giá
                    render: (data) => {
                        return data.toLocaleString('vi-VN') + '₫';
                    }
                },
                {
                    targets: -1, // Cột thao tác
                    render: (data, type, row) => {
                        return `
                        <button class="btn btn-sm btn-primary edit-service-btn" data-id="${row.id}">
                            <i class="fa fa-edit"></i> Chỉnh sửa
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
