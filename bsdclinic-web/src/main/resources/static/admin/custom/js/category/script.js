import {App, SweetAlert} from "/common/js/app.js";
import {RequestHeader} from "/common/js/constant.js";
import {CategoryComponent} from "/admin/custom/js/category/component.js";
import {FormHandler} from "/common/js/form.js";

export const Element = (function () {
    return {
        saveCategoryButton: '.save-category-btn',
        cancelCategoryButton: '.cancel-category-btn',
        categoryTitleInput: '.category-title-input',
        categoryItemCreateArea: '.category-item-create',

        categoryTypeItemSelector: $('.category-type-item'),
        addCategoryButtonSelector: $('#add-category-btn'),
        categoryListAreaSelector: $('#category-list-area'),
        categoryTypeInputsSelector: $('input[name="categoryType"]')
    };
})();

export const CategoryInitiation = (function () {
    const module = {};

    module.init = () => {
        handleCategoryTypeChange();
        handleCategoryTypeLabelTrigger();
    }

    const handleCategoryTypeChange = () => {
        Element.categoryTypeInputsSelector.on('change', function () {
            Element.categoryTypeItemSelector.removeClass('active');

            if ($(this).is(':checked')) {
                $(this).parent().addClass('active');
            }
        });
    }

    const handleCategoryTypeLabelTrigger = () => {
        Element.categoryTypeItemSelector.on('click', function () {
            const radio = $(this).find('input[type="radio"]');
            if (radio.length) {
                radio.prop('checked', true).trigger('change');
            }
        });
    }

    return module;
})();

export const CategoryCreation = (function () {
    const module = {};

    module.init = () => {
        handleAddCategoryButton();
        handleSaveCategoryButton();
        handleRemoveCreateCategoryItem();
    }

    const handleAddCategoryButton = () => {
        Element.addCategoryButtonSelector.on('click', function () {
            Element.categoryListAreaSelector.prepend(CategoryComponent.categoryItemCreate);
        });
    }

    const handleSaveCategoryButton = () => {
        Element.categoryListAreaSelector.on('click', Element.saveCategoryButton, function () {
            const categoryParams = {
                title: $(this).parent().siblings(Element.categoryTitleInput).val().trim(),
                categoryType: Element.categoryTypeInputsSelector.filter(':checked').val()
            }

            $.ajax({
                headers: RequestHeader.JSON_TYPE,
                type: 'POST',
                url: API_ADMIN_CATEGORY,
                data: JSON.stringify(categoryParams),
            })
                .done(() => {
                    SweetAlert.showAlert('success', createSuccess, '');
                    $(this).closest(Element.categoryItemCreateArea).remove();
                })
                .fail((jqXHR) => {
                    App.handleResponseMessageByStatusCode(jqXHR);
                    const currentCategoryItem = $(this).closest(Element.categoryItemCreateArea);
                    FormHandler.handleServerValidationError(currentCategoryItem, jqXHR)
                })
        });
    }

    const handleRemoveCreateCategoryItem = () => {
        Element.categoryListAreaSelector.on('click', Element.cancelCategoryButton, function () {
           $(this).closest(Element.categoryItemCreateArea).remove();
        });
    }

    return module;
})();