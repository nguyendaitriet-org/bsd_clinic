export const UserProfile = (function () {
    const module = {
        editIconSelector: $('.edit-icon'),
        cancelButtonSelector: $('.cancel-btn')
    };

    module.init = () => {
        handleEditIcon();
        handleCancelButton();
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

    return module;
})();

(function () {
    UserProfile.init();
})();