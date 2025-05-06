import {App} from "/common/js/app.js";
import {FormHandler} from "/common/js/form.js";

export const UserCreation = (function () {
    const module = {
        userCreationUrl: '/api/users',

        emailSelector: $('.mail-input'),
        fullNameSelector: $('.full-name-input'),
        phoneSelector: $('.phone-input'),
        passwordSelector: $('.password-input'),
        passwordConfirmationSelector: $('.password-confirmation-input'),
        roleSelectSelector: $('.role-select'),
        saveButtonSelector: $('.btn-save'),
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
                password:  module.passwordSelector.val().trim(),
                passwordConfirmation:  module.passwordConfirmationSelector.val().trim(),
                roleId:  module.roleSelectSelector.val().trim()
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
            url: module.userCreationUrl,
            data: JSON.stringify(userCreationData),
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
    UserCreation.init();
})();

