import {App, SweetAlert, DebounceUtil} from "/common/js/app.js";
import {RequestHeader} from "/common/js/constant.js";
import {CategoryComponent} from "/admin/custom/js/category/component.js";
import {FormHandler} from "/common/js/form.js";

export const Element = (function () {
    return {
        saveCategoryButton: '.save-category-btn',
        cancelCategoryButton: '.cancel-category-btn',
        categoryTitleInput: '.category-title-input',
        categoryIdInput: '.category-id-input',
        categoryTitleText: '.category-title-text',
        categoryItemCreateArea: '.category-item-create',
        categoryItemDetailArea: '.category-item-detail',
        editCategoryButton: '.edit-category-btn',
        deleteCategoryButton: '.delete-category-btn',
        updateCategoryButton: '.update-category-btn',
        cancelUpdateCategoryButton: '.cancel-update-category-btn',
        confirmUpdateArea: '.confirm-update-area',


        searchInputSelector: $('#search-input'),
        categoryTypeItemSelector: $('.category-type-item'),
        addCategoryButtonSelector: $('#add-category-btn'),
        categoryListAreaSelector: $('#category-list-area'),
        categoryTypeInputsSelector: $('input[name="categoryType"]'),
        categoryTitleSelector: $('#category-title-text'),
        categoryItemDetailSelector: $('.category-item-detail'),
        emptyCategoryAreaSelector: $('#empty-category-area')
    };
})();

export const CategoryInitiation = (function () {
    const module = {};

    module.init = () => {
        handleCategoryTypeChange();
        renderDefaultCategoryList();
    }

    const handleCategoryTypeChange = () => {
        Element.categoryTypeInputsSelector.on('change', function () {
            Element.categoryTypeItemSelector.removeClass('active');
            if ($(this).is(':checked')) {
                $(this).parent().addClass('active');
                // Change the category title
                Element.categoryTitleSelector.text($(this).data('text'));
                // Change the category list
                CategoryList.renderCategoryList({
                    keyword: Element.searchInputSelector.val().trim(),
                    categoryType: $(this).val()
                })
            }
        });
    }

    const renderDefaultCategoryList = () => {
        const categoryTitle = Element.categoryTypeInputsSelector.filter(':checked').data('text');
        Element.categoryTitleSelector.text(categoryTitle);
        CategoryList.renderCategoryList(CategoryList.getCategoryParam());
    }

    module.getCurrentCategoryType = () => {
        return Element.categoryTypeInputsSelector.filter(':checked').val();
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
                categoryType: CategoryInitiation.getCurrentCategoryType()
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
                    CategoryList.renderCategoryList(CategoryList.getCategoryParam());
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

export const CategoryList = (function () {
    const module = {};

    module.init = () => {
        handleSearchInputChange();
    }

    module.renderCategoryList = (categoryParams) => {
        getCategoryList(categoryParams).then((categoryList) => {
            Element.categoryListAreaSelector.find(Element.categoryItemDetailArea).remove();
            Element.categoryListAreaSelector.append(
                categoryList.map(categoryItem => CategoryComponent.categoryItemDetail(categoryItem))
            );
        });
    }

    const getCategoryList = (categoryParams) => {
        return $.ajax({
            url: API_ADMIN_CATEGORY,
            data: categoryParams,
        });
    }

    module.getCategoryParam = () => {
        return {
            keyword: Element.searchInputSelector.val().trim(),
            categoryType: CategoryInitiation.getCurrentCategoryType()
        }
    }

    const handleSearchInputChange = () => {
        Element.searchInputSelector.on('input', function() {
            DebounceUtil.debounce(
                () => module.renderCategoryList(module.getCategoryParam()),
                DebounceUtil.delayTime,
                'categorySearch'
            )();
        });
    }

    return module;
})();

export const CategoryUpdating = (function () {
    const module = {};

    module.init = () => {
        handleEditCategoryButton();
        handleCancelUpdateCategoryButton();
        handleUpdateCategoryButton();
    }

    const handleEditCategoryButton = () => {
        Element.categoryListAreaSelector.on('click', Element.editCategoryButton, function () {
            $(this).closest('.dropdown').prop('hidden', true);
            const categoryItemDetailAreaSelector = $(this).closest(Element.categoryItemDetailArea);
            categoryItemDetailAreaSelector.find(Element.confirmUpdateArea).prop('hidden', false);
            categoryItemDetailAreaSelector.find(Element.categoryTitleInput).prop('hidden', false);
            categoryItemDetailAreaSelector.find(Element.categoryTitleText).prop('hidden', true);
        });
    }

    const handleCancelUpdateCategoryButton = () => {
        Element.categoryListAreaSelector.on('click', Element.cancelUpdateCategoryButton, function () {
            const categoryItemDetailAreaSelector = $(this).closest(Element.categoryItemDetailArea);
            categoryItemDetailAreaSelector.find('.dropdown').prop('hidden', false);
            categoryItemDetailAreaSelector.find(Element.confirmUpdateArea).prop('hidden', true);
            categoryItemDetailAreaSelector.find(Element.categoryTitleInput).prop('hidden', true);
            categoryItemDetailAreaSelector.find(Element.categoryTitleText).prop('hidden', false);
        });
    }

    const handleUpdateCategoryButton = () => {
        Element.categoryListAreaSelector.on('click', Element.updateCategoryButton, function () {
            const categoryParams = {
                title: $(this).parent().siblings(Element.categoryTitleInput).val().trim()
            }

            const categoryId = $(this).parent().siblings(Element.categoryIdInput).val();

            $.ajax({
                headers: RequestHeader.JSON_TYPE,
                type: 'PATCH',
                url: API_ADMIN_CATEGORY_WITH_ID.replace('{categoryId}', categoryId),
                data: JSON.stringify(categoryParams),
            })
                .done(() => {
                    SweetAlert.showAlert('success', operationSuccess, '');
                    $(this).closest(Element.categoryItemDetailArea).remove();
                    // Re-render updated category
                    Element.categoryListAreaSelector.append(
                        CategoryComponent.categoryItemDetail({
                            ...categoryParams,
                            categoryId
                        })
                    );
                })
                .fail((jqXHR) => {
                    App.handleResponseMessageByStatusCode(jqXHR);
                    const currentCategoryItem = $(this).closest(Element.categoryItemDetailArea);
                    FormHandler.handleServerValidationError(currentCategoryItem, jqXHR)
                })
        });
    }

    return module;
})();