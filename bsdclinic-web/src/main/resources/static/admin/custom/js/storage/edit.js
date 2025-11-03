import {App, SweetAlert} from "/common/js/app.js";

export const ResourceDeletion = (function () {
    const module = {
        resourceListTableSelector: $('#resource-list-table')
    };

    module.init = () => {
        handleResourceDeletion();
    }

    const handleResourceDeletion = () => {
        module.resourceListTableSelector.on('click', '.delete-resource-btn', function () {
            SweetAlert.showConfirmation('error', confirmApplyTitle, cannotRedoAfterDeleting).then((result) => {
                if (result.isConfirmed) {
                    const rowData = module.resourceListTableSelector.DataTable().row($(this).closest('tr')).data();
                    deleteResource(rowData.resourceId);
                }
            });
        });
    }

    const deleteResource = (resourceId) => {
        $.ajax({
            type: 'DELETE',
            url: API_ADMIN_RESOURCE_WITH_ID.replace('{resourceId}', resourceId)
        })
            .done(() => {
                SweetAlert.showAlert('success', operationSuccess, '');
                module.resourceListTableSelector.DataTable().draw('page');
            })
            .fail((jqXHR) => {
                App.handleResponseMessageByStatusCode(jqXHR);
            })
    }

    return module;
})();
