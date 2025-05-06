import {DatatableAttribute, Constant} from "/common/js/app.js";

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
        renderUserListTable({createdFrom: Constant.minDateFilter});
        handleSearchSubmissionButton();
        handleCancelSearchButton();
    }

    const handleCancelSearchButton = () => {
        module.cancelSearchButtonSelector.on('click', function () {
            renderUserListTable({createdFrom: Constant.minDateFilter});
            module.searchInputSelector.val('');
            module.roleSelectSelector.selectpicker('deselectAll');
            module.statusSelectSelector.selectpicker('deselectAll');
            module.creationDateRangeInputSelector.data('daterangepicker').setStartDate(new Date());
            module.creationDateRangeInputSelector.data('daterangepicker').setEndDate(new Date());
        })
    }

    const handleSearchSubmissionButton = () => {
        module.searchSubmitButtonSelector.on('click', function () {
            const userFilter = getUserListFilter();
            renderUserListTable(userFilter);
        });
    }

    const getUserListFilter = () => {
        return {
            keyword: module.searchInputSelector.val().trim(),
            roleIds: module.roleSelectSelector.val(),
            status: module.statusSelectSelector.val(),
            createdFrom: module.creationDateRangeInputSelector.data('daterangepicker').startDate,
            createdTo: module.creationDateRangeInputSelector.data('daterangepicker').endDate
        }
    }

    const toRoleMap = (userRoles) => {
        return userRoles.reduce((acc, role) => {
            acc[role.roleId] = role.code;
            return acc;
        }, {});
    }

    const renderUserListTable = (userFilter) => {
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
                    render: (data, type, row) => userStatusMap[data]
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
