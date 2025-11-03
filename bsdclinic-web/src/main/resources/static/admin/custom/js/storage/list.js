import {DatatableAttribute, DebounceUtil} from "/common/js/app.js";
import {NumberConverter} from "/common/js/converter_util.js";
import {DateTimeConverter} from "/common/js/datetime_util.js";

export const ResourceList = (function () {
    const module = {
        keywordInputSelector: $('#search-input'),
        resourceListTableSelector: $('#resource-list-table'),
        resourceTypeInputsSelector: $('input[name="resourceType"]'),
        resourceTypeItemSelector: $('.resource-type-item'),
    };

    module.init = () => {
        renderResourceListTable();
        handleResourceTypeChange();
        handleSearchInputChange();
    }

    const getResourceListFilter = () => {
        return {
            keyword: module.keywordInputSelector.val().trim(),
            resourceType: $('input[name="resourceType"]:checked').val()
        }
    }

    const renderResourceListTable = () => {
        const resourceFilter = getResourceListFilter();

        const resourceListDatatable = module.resourceListTableSelector.DataTable({
            ajax: {
                contentType: 'application/json',
                type: 'POST',
                url: API_ADMIN_RESOURCE_LIST,
                data: function (d) {
                    return JSON.stringify({...d, ...resourceFilter});
                }
            },
            columns: [
                {data: null},
                {data: 'title'},
                {data: 'fileSize'},
                {data: 'uploadedBy'},
                {data: 'createdAt'},
                {data: 'resourceCategories'},
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
                    targets: [0, 2, 3, 4, 6],
                    className: "text-center"
                },
                {
                    targets: 2,
                    render: (data) => {
                        return NumberConverter.formatBytes(data);
                    }
                },
                {
                    targets: 4,
                    createdCell: (td, cellData) => {
                        const formattedDate = DateTimeConverter.convertToDisplayPattern(cellData)
                        $(td).text(formattedDate);
                    }
                },
                {
                    targets: 5,
                    render: (data) => {
                        return data && data.map(item => `<button class="btn btn-sm btn-secondary mt-2">${item.title}</button>`);
                    }
                },
                {
                    targets: -1,
                    render: (data, type, row) => {
                        return `
                            <div class="btn-group">
                                <button class="btn btn-sm btn-warning" type="button">
                                    <i class="fa fa-eye"></i>
                                </button>
                                <button class="btn btn-sm btn-primary" type="button">
                                    <i class="fa fa-pen"></i>
                                </button>
                                <button class="btn btn-sm btn-success" type="button">
                                    <i class="fa fa-arrow-down"></i>
                                </button>
                                <button class="btn btn-sm btn-danger delete-resource-btn" type="button">
                                    <i class="fa fa-trash"></i>
                                </button>
                            </div>`;
                    }
                }
            ],
            language: DatatableAttribute.language
        });

        DatatableAttribute.renderOrdinalColumn(resourceListDatatable, 0);
    }

    const handleResourceTypeChange = () => {
        module.resourceTypeInputsSelector.on('change', function () {
            module.resourceTypeItemSelector.removeClass('active');
            if ($(this).is(':checked')) {
                $(this).parent().addClass('active');
                renderResourceListTable();
            }
        });
    }

    const handleSearchInputChange = () => {
        module.keywordInputSelector.on('input', function() {
            DebounceUtil.debounce(
                () => renderResourceListTable(),
                DebounceUtil.delayTime,
                'resourceSearch'
            )();
        });
    }

    return module;
})();
