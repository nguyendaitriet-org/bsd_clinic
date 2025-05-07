import {App} from "/common/js/app.js";
import {FormHandler} from "/common/js/form.js";
import {UserList} from "./user-list.js";

export const UserUpdating = (function () {
    const module = {
        userUpdatingUrl: '/api/users',

        userUpdatingModalSelector: $('#update-user-modal'),
        emailSelector: $('#update-user-modal .mail-input'),
        fullNameSelector: $('#update-user-modal .full-name-input'),
        phoneSelector: $('#update-user-modal .phone-input'),
        roleSelectSelector: $('#update-user-modal .role-select'),
        statusSelectSelector: $('#update-user-modal .status-select'),
        saveButtonSelector: $('#update-user-modal .btn-save')
    };

    module.init = () => {
        openEditUserModalButton();
    }

    const openEditUserModalButton = () => {
        UserList.userListTableSelector.on('click', '.open-edit-user-modal-btn', function () {
            const rowData = UserList.userListTableSelector.DataTable().row($(this).closest('tr')).data();
            fillUserData(rowData);
            module.userUpdatingModalSelector.modal('show');
        })
    }

    const fillUserData = (data) => {
        module.emailSelector.val(data.email);
        module.fullNameSelector.val(data.fullName);
        module.phoneSelector.val(data.phone);
        module.roleSelectSelector.val(data.roleId);
        module.statusSelectSelector.val(data.status);
    }

    const updateUser = (userUpdatingData) => {
        $.ajax({
            headers: {
                "accept": "application/json",
                "content-type": "application/json"
            },
            type: 'PATCH',
            url: module.userUpdatingUrl,
            data: JSON.stringify(userUpdatingData),
        })
            .done(() => {
                App.showSuccessMessage(createSuccess);
                setTimeout(() => location.reload(), 1000);
            })
            .fail((jqXHR) => {
                App.handleResponseMessageByStatusCode(jqXHR);
                FormHandler.handleServerValidationError(module.userCreationModalSelector, jqXHR)
            })
    }

    return module;
})();

(function () {
    UserUpdating.init();
})();

