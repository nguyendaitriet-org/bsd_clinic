import {App} from "/common/js/app.js";
import {RequestHeader} from "/common/js/constant.js";

export const CategoryInitiation = (function () {
    const module = {};

    module.init = () => {
        handleCategoryTypeChange();
        handleCategoryTypeLabelTrigger();
    }

    const handleCategoryTypeChange = () => {
        $('input[name="categoryType"]').on('change', function() {
            $('.category-type-item').removeClass('active');

            if ($(this).is(':checked')) {
                $(this).parent().addClass('active');
            }
        });
    }

    const handleCategoryTypeLabelTrigger = () => {
        $('.category-type-item').on('click', function() {
            const radio = $(this).find('input[type="radio"]');
            if (radio.length) {
                radio.prop('checked', true).trigger('change');
            }
        });
    }

    return module;
})();