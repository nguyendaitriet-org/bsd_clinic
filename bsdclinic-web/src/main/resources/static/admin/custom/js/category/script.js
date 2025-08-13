import {App} from "/common/js/app.js";
import {RequestHeader} from "/common/js/constant.js";
import {CategoryComponent} from "/admin/custom/js/category/component.js";

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

export const CategoryCreation = (function () {
    const module = {
        addCategoryButtonSelector: $('#add-category-btn'),
        saveAllNewCategoriesButtonSelector: $('#save-all-new-categories-btn'),
        categoryListAreaSelector: $('#category-list-area')
    };

    module.init = () => {
        handleAddCategoryButton();
        handleRemoveCreateCategoryItem();
        handleSaveAllNewCategories();
    }

    const handleAddCategoryButton = () => {
        module.addCategoryButtonSelector.on('click', function () {
            module.saveAllNewCategoriesButtonSelector.prop('hidden', false);
            module.categoryListAreaSelector.prepend(CategoryComponent.categoryItemCreate);
        });
    }

    const handleRemoveCreateCategoryItem = () => {
        module.categoryListAreaSelector.on('click', '.cancel-category-btn', function () {
           $(this).closest('.category-item-create').remove();
        });
    }

    const handleSaveAllNewCategories = () => {
        module.saveAllNewCategoriesButtonSelector.on('click', function () {
            $(this).prop('hidden', true);
        });
    }

    return module;
})();