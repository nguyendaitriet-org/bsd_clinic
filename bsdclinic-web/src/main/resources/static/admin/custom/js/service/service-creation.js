import {App} from "/common/js/app.js";
import {FormHandler} from "/common/js/form.js";
import {ServiceList} from "./service-list.js";

export const ServiceCreation = (function () {
    const module = {
        emailSelector: $('#create-user-modal .mail-input'),
        fullNameSelector: $('#create-user-modal .full-name-input'),
        phoneSelector: $('#create-user-modal .phone-input'),
        passwordSelector: $('#create-user-modal .password-input'),
        passwordConfirmationSelector: $('#create-user-modal .password-confirmation-input'),
        roleSelectSelector: $('#create-user-modal .role-select'),
        saveButtonSelector: $('#create-user-modal .btn-save'),
        userCreationModalSelector: $('#create-user-modal')
    };

    module.init = () => {
        handleSaveButton();
    }

    const handleSaveButton = () => {
        module.saveButtonSelector.on('click', function () {
            const userCreationData = getUserCreationData();
            createNewUser(userCreationData);
        });
    }

    const getUserCreationData = () => {
        return (
            {
                email: module.emailSelector.val().trim(),
                fullName: module.fullNameSelector.val().trim(),
                phone:  module.phoneSelector.val().trim(),
                password:  module.passwordSelector.val(),
                passwordConfirmation:  module.passwordConfirmationSelector.val(),
                roleId:  module.roleSelectSelector.val()
            }
        );
    }

    const createNewUser = (userCreationData) => {
        $.ajax({
            headers: {
                "accept": "application/json",
                "content-type": "application/json"
            },
            type: 'POST',
            url: API_ADMIN_USER_ENDPOINT,
            data: JSON.stringify(userCreationData),
        })
            .done(() => {
                App.showSuccessMessage(createSuccess);
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
    ServiceCreation.init();
})();

