import {DatatableAttribute} from "/common/js/app.js";

export const UserList = (function () {
    const module = {
        findAllUsersByFilterUrl: '/api/users',

        searchInputSelector: $('#search-input'),
        roleSelectSelector: $('#role-select'),
        statusSelectSelector: $('#status-select'),
        creationDaterangeInputSelector: $('#creation-daterange-input'),
        searchSubmitSelector: $('#submit-btn'),

        userListTableSelector: $('#user-list-table'),

    };

    module.init = () => {
        renderUserListTable();
        handleSearchSubmissionButton();
    }

    const handleSearchSubmissionButton = () => {
        module.searchSubmitSelector.on('click', function () {
            renderUserListTable();
        });
    }

    const getUserListFilter = () => {
        return {
            keyword: module.searchInputSelector.val().trim(),
            roles: module.roleSelectSelector.val(),
            status: module.statusSelectSelector.val(),
            creationStartDate: module.creationDaterangeInputSelector.data('daterangepicker').startDate,
            creationEndDate: module.creationDaterangeInputSelector.data('daterangepicker').endDate
        }
    }

    const renderUserListTable = () => {
        const userFilter = getUserListFilter();
        const userListDatatable = module.userListTableSelector.DataTable({
            ajax: {
                contentType: 'application/json',
                type: 'POST',
                url: module.findAllUsersByFilterUrl,
                data: function (d) {
                    d.keyword = userFilter.keyword;
                    d.roles = userFilter.roles;
                    d.status = userFilter.status;
                    d.creationStartDate = userFilter.creationStartDate;
                    d.creationEndDate = userFilter.creationEndDate;
                    return JSON.stringify(d);
                }
            },
            columns: [
                {data: null},
                {data: 'username'},
                {data: 'fullName'},
                {data: 'role'},
                {data: 'createdAt'},
                {data: 'isDisabled'},
                {data: null},
            ],
            serverSide: true,
            bJQueryUI: true,
            destroy: true,
            paging: true,
            searching: false,
            lengthChange: false,
            info: false,
            ordering: false,
            pageLength: 10,
            pagingType: 'simple_numbers',
            columnDefs: [
                {
                    targets: [0, 3, 4],
                    className: "text-center"
                },
                {
                    targets: -2,
                    className: "text-center",
                    render: (data, type, row) => {
                        return data ?
                            `<span class="text-danger">${disabled}</span>` :
                            `<span class="text-success">${active}</span>`;
                    }
                },
                {
                    targets: -1,
                    className: "text-center",
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

