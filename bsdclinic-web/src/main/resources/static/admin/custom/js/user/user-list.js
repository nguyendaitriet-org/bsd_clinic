import {DatatableAttribute} from "/common/js/app.js";
import {DateTimePattern} from "/common/js/constant.js";
import {DateTimeConverter} from "/common/js/datetime_util.js";

export const UserList = (function () {
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
        renderUserListTable();
        handleSearchSubmissionButton();
        handleCancelSearchButton();
    }

    const initDateRangePicker = () => {
        module.userCreatedAtRangePicker = new Lightpick({
            field: module.creationDateRangeInputSelector[0],
            singleDate: false
        });
    }

    const handleCancelSearchButton = () => {
        module.cancelSearchButtonSelector.on('click', function () {
            module.searchInputSelector.val('');
            module.roleSelectSelector.selectpicker('deselectAll');
            module.statusSelectSelector.selectpicker('deselectAll');
            module.userCreatedAtRangePicker.setDateRange(null, null)
            renderUserListTable();
        })
    }

    const handleSearchSubmissionButton = () => {
        module.searchSubmitButtonSelector.on('click', function () {
            renderUserListTable();
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

    const renderUserListTable = () => {
        const userFilter = getUserListFilter();
        const roleMap = toRoleMap(userRoles);
        const userListDatatable = module.userListTableSelector.DataTable({
            ajax: {
                contentType: 'application/json',
                type: 'POST',
                url: module.findAllUsersByFilterUrl,
                data: function (d) {
                    d.keyword = userFilter.keyword;
                    d.roleIds = userFilter.roleIds;
                    d.status = userFilter.status;
                    d.createdFrom = userFilter.createdFrom;
                    d.createdTo = userFilter.createdTo;
                    return JSON.stringify(d);
                }
            },
            columns: [
                {data: null},
                {data: 'email'},
                {data: 'fullName'},
                {data: 'phone'},
                {data: 'createdAt'},
                {data: 'roleId'},
                {data: 'status'},
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
                    targets: [0, 3, 4, 5, 6, 7],
                    className: "text-center"
                },
                {
                    targets: 5,
                    render: (data, type, row) => {
                        const roleCode = roleMap[data];
                        return roleTitleMap[roleCode];
                    }
                },
                {
                    targets: 6,
                    render: (data, type, row) => {
                        if (data == 'BLOCKED')
                            return `<span class="btn btn-sm btn-round btn-danger">${userStatusMap[data]}</span>`
                        if (data == 'ACTIVE')
                            return `<span class="btn btn-sm btn-round btn-success">${userStatusMap[data]}</span>`
                        return `<span class="btn btn-sm btn-round btn-danger">${userStatusMap[data]}</span>`
                    }
                },
                {
                    targets: -1,
                    render: () =>
                        `<button class="open-edit-user-modal-btn btn btn-sm btn-outline-primary border-0">
                            <i class="fa fa-edit"></i>
                        </button>`
                },
            ],
            language: DatatableAttribute.language
        });

        DatatableAttribute.renderOrdinalColumn(userListDatatable);
    }

    return module;
})();

(function () {
    UserList.init();
})();
