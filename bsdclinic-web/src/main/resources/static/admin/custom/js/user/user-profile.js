import {App} from "/common/js/app.js";
import {FormHandler} from "/common/js/form.js";

export const UserProfile = (function () {
    const module = {
        editIconSelector: $('.edit-icon'),
        cancelButtonSelector: $('.cancel-btn'),

        userAvatarAreaSelector: $('.avatar-wrapper'),
        userAvatarImageSelector: $('.profile-pic'),
        userAvatarInputSelector: $('.user-avatar'),
        updateAvatarButtonSelector: $('#update-avatar-btn'),
        fileUploadInputSelector: $('.file-upload'),
        uploadButtonSelector: $('.upload-button'),

        navbarProfileAvatarSelector: $('img.avatar-img'),
    };

    module.init = () => {
        handleEditIcon();
        handleCancelButton();
        handleAvatarUpload();
        handleUpdateAvatarButton();
    }

    const handleEditIcon = () => {
        module.editIconSelector.on('click', function () {
            $(this).closest('div').siblings('.input-text').prop('hidden', true);
            $(this).closest('div').siblings('.edit-input').prop('hidden', false);
        });
    }

    /*
    * Hiện lại tất cả các ô input text có giá trị cũ
    * Ẩn tất cả các ô edit input và thay lại bằng giá trị cũ
    * */
    const handleCancelButton = () => {
        module.cancelButtonSelector.on('click', function () {
            $('.edit-input').prop('hidden', true);
            $('.input-text').prop('hidden', false);
            $('.edit-input').each(function () {
                const input = $(this).find('input');
                const originalValue = $(this).siblings('.input-text').text();
                input.val(originalValue);
            });
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

    return module;
})();

(function () {
    UserProfile.init();
})();