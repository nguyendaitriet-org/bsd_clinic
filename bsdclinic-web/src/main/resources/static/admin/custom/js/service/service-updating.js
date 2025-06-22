import {App} from "/common/js/app.js";
import {FormHandler} from "/common/js/form.js";
import {ServiceList} from "./service-list.js";

export const ServiceUpdating = (function () {
    const module = {
        userUpdatingModalSelector: $('#update-medical-service-modal'),
        titleSelector: $('#update-medical-service-modal .service-title'),
        priceSelector: $('#update-user-modal .full-name-input'),
        descriptionSelector: $('#update-user-modal .phone-input'),
        roleSelectSelector: $('#update-user-modal .role-select'),
        statusSelectSelector: $('#update-user-modal .status-select'),
        saveButtonSelector: $('#update-user-modal .btn-save')
    };

    module.init = () => {
        openEditUserModalButton();
        handleSaveButton();
    }

    const openEditUserModalButton = () => {
        ServiceList.userListTableSelector.on('click', '.open-edit-user-modal-btn', function () {
            const rowData = ServiceList.userListTableSelector.DataTable().row($(this).closest('tr')).data();
            fillUserData(rowData);
            module.userUpdatingModalSelector.modal('show');
        })
    }

    const fillUserData = (data) => {
        module.userIdSelector.val(data.userId);
        module.emailSelector.val(data.email);
        module.fullNameSelector.val(data.fullName);
        module.phoneSelector.val(data.phone);
        module.roleSelectSelector.val(data.roleId);
        module.statusSelectSelector.val(data.status);
    }

    const handleSaveButton = () => {
        module.saveButtonSelector.on('click', function () {
            const userUpdatingData = getUserUpdatingData();
            updateUser(userUpdatingData);
        })
    }

    const getUserUpdatingData = () => {
        return (
            {
                userId: module.userIdSelector.val(),
                roleId: module.roleSelectSelector.val(),
                status: module.statusSelectSelector.val()
            }
        );
    }

    const updateUser = (userUpdatingData) => {
        $.ajax({
            headers: {
                "accept": "application/json",
                "content-type": "application/json"
            },
            type: 'PATCH',
            url: API_ADMIN_USER_ENDPOINT,
            data: JSON.stringify(userUpdatingData),
        })
            .done(() => {
                App.showSuccessMessage(operationSuccess);
                setTimeout(() => ServiceList.renderUserListTable(), 1000);
            })
            .fail((jqXHR) => {
                App.handleResponseMessageByStatusCode(jqXHR);
                FormHandler.handleServerValidationError(module.userCreationModalSelector, jqXHR)
            })
    }

    return module;
})();

(function () {
    ServiceUpdating.init();
})();

