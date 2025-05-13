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
        console.log('handleEditIcon')
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
        $(document).on('click', '.cancel-btn', function () {
            $('.edit-input').prop('hidden', true);
            $('.input-text').prop('hidden', false);
            $('.edit-input').each(function () {
                const input = $(this).find('input');
                const originalValue = $(this).siblings('.input-text').text().trim();
                input.val(originalValue);
            });
        });
    };

    module.editField  = (fieldId) => {
        const field = document.getElementById(fieldId);
        const value = field.textContent.trim();
        const input = document.createElement('input');
        input.type = 'text';
        input.value = value;
        input.className = 'form-control form-control-sm';
        input.onblur = function () {
            field.innerText = this.value;
            field.setAttribute("data-updated", "true");
        };
        field.innerText = '';
        field.appendChild(input);
        input.focus();
    }

    return module;
})();

(function () {
    UserProfile.init();
})();