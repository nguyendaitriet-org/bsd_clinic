import {App} from "/common/js/app.js";
import {FormHandler} from "/common/js/form.js";

export const UserProfile = (function () {
    const module = {
        editIconSelector: $('.edit-icon'),
        cancelButtonSelector: $('.cancel-btn'),
        saveProfileButtonSelector: $('.save-profile-btn'),

        userAvatarAreaSelector: $('.avatar-wrapper'),
        userAvatarImageSelector: $('.profile-pic'),
        userAvatarInputSelector: $('.user-avatar'),
        updateAvatarButtonSelector: $('#update-avatar-btn'),
        fileUploadInputSelector: $('.file-upload'),
        uploadButtonSelector: $('.upload-button'),

        userProfileAreaSelector: $('.profile-area'),
        userFullNameInputSelector: $('#user-full-name'),
        userEmailInputSelector: $('#user-email'),
        userPhoneInputSelector: $('#user-phone'),

        navbarProfileAvatarSelector: $('img.avatar-img'),

        changePasswordModalSelector: $('#change-password-modal'),
        oldPasswordSelector: $('input[name="oldPassword"]'),
        newPasswordSelector: $('input[name="newPassword"]'),
        passwordConfirmationSelector: $('input[name="newPasswordConfirmation"]'),
        savePasswordButtonSelector: $('.btn-save-password')
    };

    module.init = () => {
        handleEditIcon();
        handleCancelButton();
        handleAvatarUpload();
        handleUpdateAvatarButton();
        handleSaveProfileButton();
        toggleSaveButtonState();
        handleSavePasswordBtn();
    }

    const toggleSaveButtonState = () => {
        const anyEditVisible = $('.edit-input').filter(function () {
            return !$(this).prop('hidden');
        }).length > 0;

        module.saveProfileButtonSelector.prop('disabled', !anyEditVisible);
    };

    const handleSaveProfileButton = () => {
        module.saveProfileButtonSelector.on('click', function () {
            const params = {
                email: module.userEmailInputSelector.val().trim(),
                fullName: module.userFullNameInputSelector.val().trim(),
                phone: module.userPhoneInputSelector.val().trim()
            };

            $.ajax({
                url: API_ADMIN_USER_PROFILE,
                headers: {
                    "accept": "application/json",
                    "content-type": "application/json"
                },
                type: 'PATCH',
                data: JSON.stringify(params),
            })
                .done(() => {
                    App.showSuccessMessage(operationSuccess);
                    location.reload();
                })
                .fail((jqXHR) => {
                    FormHandler.handleServerValidationError(module.userProfileAreaSelector, jqXHR)
                    App.handleResponseMessageByStatusCode(jqXHR);
                })
        })
    }

    const handleEditIcon = () => {
        module.editIconSelector.on('click', function () {
            $(this).closest('div').siblings('.input-text').prop('hidden', true);
            $(this).closest('div').siblings('.edit-input').prop('hidden', false);
            toggleSaveButtonState();
        });
    }

    const handleCancelButton = () => {
        module.cancelButtonSelector.on('click', function () {
            $('.edit-input').prop('hidden', true);
            $('.input-text').prop('hidden', false);
            $('.edit-input').each(function () {
                const input = $(this).find('input');
                const originalValue = $(this).siblings('.input-text').text();
                input.val(originalValue);
            });
            toggleSaveButtonState();
        });
    };

    const handleAvatarUpload = () => {
        let readURL = function (input) {
            if (input.files && input.files[0]) {
                let reader = new FileReader();
                reader.onload = function (e) {
                    module.userAvatarImageSelector.attr('src', e.target.result);
                }
                reader.readAsDataURL(input.files[0]);
            }
        }

        module.fileUploadInputSelector.on('change', function () {
            readURL(this);
            module.updateAvatarButtonSelector.prop('hidden', false);
        });

        module.uploadButtonSelector.on('click', function () {
            module.fileUploadInputSelector.click();
        });
    }

    const handleUpdateAvatarButton = () => {
        module.updateAvatarButtonSelector.on('click', function () {
            let formData = new FormData();
            const avatar = module.userAvatarInputSelector.prop('files')[0];
            formData.append('avatar', avatar);
            updateAvatar(formData);
        })
    }

    const updateAvatar = (avatarFormData) => {
        $.ajax({
            url: API_ADMIN_USER_AVATAR,
            data: avatarFormData,
            type: 'POST',
            processData: false,
            mimeType: 'multipart/form-data',
            contentType: false,
            dataType: 'json',
        })
            .done(() => {
                App.showSuccessMessage(operationSuccess);
                module.updateAvatarButtonSelector.prop('hidden', true);
                module.navbarProfileAvatarSelector.prop('src', API_ADMIN_USER_AVATAR);
            })
            .fail((jqXHR) => {
                FormHandler.handleServerValidationError(module.userAvatarAreaSelector, jqXHR)
                App.handleResponseMessageByStatusCode(jqXHR);
            })
    }

    /* Handle password change */
    const handleSavePasswordBtn = () => {
        module.savePasswordButtonSelector.on('click', function () {
            const params = {
                oldPassword: module.oldPasswordSelector.val(),
                newPassword: module.newPasswordSelector.val(),
                newPasswordConfirmation: module.passwordConfirmationSelector.val(),
            }
            $.ajax({
                url: API_ADMIN_USER_CHANGE_PASSWORD,
                headers: {
                    "accept": "application/json",
                    "content-type": "application/json"
                },
                type: 'PUT',
                data: JSON.stringify(params),
            })
                .done(() => {
                    App.showSuccessMessage(operationSuccess);
                    module.changePasswordModalSelector.modal('hide');
                    FormHandler.clearAllInputs(module.changePasswordModalSelector);
                })
                .fail((jqXHR) => {
                    FormHandler.handleServerValidationError(module.changePasswordModalSelector, jqXHR)
                    App.handleResponseMessageByStatusCode(jqXHR);
                })
        })

    }


    return module;
})();

(function () {
    UserProfile.init();
})();