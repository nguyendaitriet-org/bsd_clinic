import {App} from "/common/js/app.js";
import {FormHandler} from "/common/js/form.js";
import {DatatableAttribute} from "/common/js/app.js";

export const Subscriber = (function () {
    const module = {
        subscriberListTableSelector: $('#subscriber-list-table'),
        searchSubscriberInputSelector: $('#search-subscriber-input'),
        searchSubscriberButtonSelector: $('#search-subscriber-btn'),
        resetSubscriberButtonSelector: $('#reset-subscriber-btn'),

        datatableInitConfig: {
            data: [],
            bJQueryUI: true,
            destroy: true,
            paging: true,
            searching: false,
            lengthChange: true,
            info: false,
            ordering: false,
            pagingType: 'simple_numbers',
            language: DatatableAttribute.language
        }
    };

    module.init = () => {
        initDatatable();
        handleSearchSubscriberButton();
        handleResetSubscriberButton();
    }

    const initDatatable = () => {
        module.subscriberListTable = module.subscriberListTableSelector.DataTable(module.datatableInitConfig);
    }

    const handleResetSubscriberButton = () => {
        module.resetSubscriberButtonSelector.on('click', function () {
            module.subscriberListTable.destroy();
            initDatatable();
        })
    }

    const handleSearchSubscriberButton = () => {
        module.searchSubscriberButtonSelector.on('click', function () {
            const keyword = module.searchSubscriberInputSelector.val().trim();
            module.subscriberListTable.destroy();
            module.subscriberListTable = module.subscriberListTableSelector.DataTable({
                ajax: {
                    contentType: 'application/json',
                    type: 'POST',
                    url: API_ADMIN_SUBSCRIBER_LIST,
                    data: function (d) {
                        d.keyword = keyword;
                        return JSON.stringify(d);
                    }
                },
                initComplete: function (settings, json) {
                    const tooltipTrigger = document.querySelectorAll('[data-bs-toggle="tooltip"]');
                    [...tooltipTrigger].map(tooltipTriggerEl => new bootstrap.Tooltip(tooltipTriggerEl));
                },
                columns: [
                    {data: null},
                    {data: 'subscriberEmail'},
                    {data: 'subscriberName'},
                    {data: 'subscriberPhone'},
                    {data: null}
                ],
                columnDefs: [
                    {
                        targets: [0, 3, 4],
                        className: "text-center"
                    },
                    {
                        targets: -1,
                        render: (data, type, row) =>
                            `<div class="btn-group btn-group-sm">
                                <button class="btn btn-outline-info border-0 self-filling-btn" data-bs-toggle="tooltip" type="button"
                                        data-bs-placement="bottom" data-bs-title="${selfTitle}">
                                    <i class="fas fa-arrow-left"></i>
                                </button>
                                <button class="btn btn-outline-warning border-0 appointment-list-btn" data-bs-toggle="tooltip" type="button"
                                        data-bs-placement="bottom" data-bs-title="${detailTitle}" data-subscriber-id="${row.subscriberId}">
                                    <i class="fas fa-users"></i>
                                </button>
                            </div>`
                    },
                ],
                serverSide: true,
                ...module.datatableInitConfig
            });

            DatatableAttribute.renderOrdinalColumn(module.subscriberListTable, 0);
        });
    }

    module.getSubscriberById = (subscriberId) => {
        return $.ajax({
            type: 'GET',
            url: API_ADMIN_SUBSCRIBER_DETAIL.replace('{subscriberId}', subscriberId)
        })
            .fail((jqXHR) => {
                App.handleResponseMessageByStatusCode(jqXHR);
            })
    }

    return module;
})();

(function () {
    Subscriber.init();
})();
