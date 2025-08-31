import {App, SweetAlert, DebounceUtil, DatatableAttribute} from "/common/js/app.js";
import {CurrencyConverter} from "/common/js/currency_util.js";
import {FormHandler} from "/common/js/form.js";
import {RequestHeader} from "/common/js/constant.js";

export const MedicineCreation = (function () {
    const module = {
        createMedicineModalSelector: $('#create-medicine-modal'),
        createMedicineFormSelector: $('#create-medicine-form'),
        saveMedicineButtonSelector: $('.btn-save'),
        unitPriceSelector: $('#create-medicine-modal .price-input'),
        medicineCategorySelector: $('#create-medicine-modal .category-select')
    };

    module.init = () => {
        handleCreateMedicineFormSubmission();
        CurrencyConverter.setupPriceFormatter(module.unitPriceSelector);
    }

    const handleCreateMedicineFormSubmission = () => {
        module.createMedicineFormSelector.on('submit', function (e) {
            e.preventDefault();
            const formData = new FormData(this);

            const medicineCreationParams = Object.fromEntries(
                Array.from(formData.entries()).map(([key, value]) => [key, value === '' ? null : value])
            );
            medicineCreationParams.unitPrice = CurrencyConverter.getNumericValue(module.unitPriceSelector.val());
            medicineCreationParams.categoryIds = module.medicineCategorySelector.selectpicker('val');

            $.ajax({
                headers: RequestHeader.JSON_TYPE,
                type: 'POST',
                url: API_ADMIN_MEDICINE,
                data: JSON.stringify(medicineCreationParams),
            })
                .done(() => {
                    SweetAlert.showAlert('success', createSuccess, '');
                    module.createMedicineModalSelector.modal('hide');
                    MedicineList.medicineListTableSelector.DataTable().draw('full-hold');
                })
                .fail((jqXHR) => {
                    App.handleResponseMessageByStatusCode(jqXHR);
                    FormHandler.handleServerValidationError(module.createMedicineFormSelector, jqXHR)
                })
        })
    }

    return module;
})();

export const MedicineList = (function () {
    const module = {
        searchInputSelector: $('#search-input'),
        categorySelectSelector: $('#category-select'),
        medicineListTableSelector: $('#medicine-list-table'),
    };

    module.init = () => {
        renderMedicineListTable();
        handleSearchInputChange();
        handleCategorySelectChange();
    }

    const handleSearchInputChange = () => {
        module.searchInputSelector.on('input', function() {
            DebounceUtil.debounce(
                renderMedicineListTable,
                DebounceUtil.delayTime,
                'medicineSearchInput'
            )();
        });
    }

    const handleCategorySelectChange = () => {
        module.categorySelectSelector.on('changed.bs.select', function () {
            DebounceUtil.debounce(
                renderMedicineListTable,
                DebounceUtil.delayTime,
                'medicineSearchCategory'
            )();
        });
    }

    const getMedicineListFilter = () => {
        return {
            keyword: module.searchInputSelector.val().trim(),
            categoryIds: module.categorySelectSelector.selectpicker('val')
        }
    }

    const renderMedicineListTable = () => {
        const medicineFilter = getMedicineListFilter();
        const medicineListDatatable = module.medicineListTableSelector.DataTable({
            ajax: {
                contentType: 'application/json',
                type: 'POST',
                url: API_ADMIN_MEDICINE_LIST,
                data: function (d) {
                    return JSON.stringify({...d, ...medicineFilter});
                }
            },
            columns: [
                {data: null},
                {data: 'title'},
                {data: 'medicineCategories'},
                {data: 'unitPrice'},
                {data: 'unit'},
                {data: null},
            ],
            serverSide: true,
            bJQueryUI: true,
            destroy: true,
            paging: true,
            searching: false,
            lengthChange: true,
            info: false,
            ordering: false,
            pagingType: 'simple_numbers',
            columnDefs: [
                {
                    targets: [0, 3, 4, 5],
                    className: "text-center"
                },
                {
                    targets: 2,
                    render: (data) => {
                        return data && data.map(item =>`<button class="btn btn-sm btn-secondary mt-2">${item.title}</button>` );
                    }
                },
                {
                    targets: 3,
                    render: (data) => {
                        return CurrencyConverter.formatCurrencyVND(data);
                    }
                },
                {
                    targets: 4,
                    render: (data) => {
                        return dosageUnitMap[data];
                    }
                },
                {
                    targets: -1,
                    render: (data, type, row) => {
                        return `
                            <button class="btn btn-sm btn-warning show-updating-modal-btn">
                                <i class="fa fa-edit"></i>
                            </button>
                            <button class="btn btn-sm btn-danger show-deletion-confirmation-btn">
                                <i class="fa fa-trash"></i>
                            </button>
                        `;
                    }
                }
            ],
            language: DatatableAttribute.language
        });

        DatatableAttribute.renderOrdinalColumn(medicineListDatatable, 0);
    }

    return module;
})();

export const MedicineUpdating = (function () {
    const module = {
        medicineUpdatingModalSelector: $('#update-medicine-modal'),
        updateMedicineFormSelector: $('#update-medicine-form'),
        unitPriceSelector: $('#update-medicine-modal .price-input'),
        unitSelector: $('#update-medicine-modal .dosage-unit'),
        medicineCategorySelector: $('#update-medicine-modal .category-select'),
    };

    module.init = () => {
        handleShowUpdatingModal();
        handleUpdateMedicineFormSubmission();
        CurrencyConverter.setupPriceFormatter(module.unitPriceSelector);
    }

    const handleShowUpdatingModal = () => {
        MedicineList.medicineListTableSelector.on('click', '.show-updating-modal-btn', function () {
            module.medicineUpdatingModalSelector.modal('show');
            const rowData = MedicineList.medicineListTableSelector.DataTable().row($(this).closest('tr')).data();
            module.currentMedicineId = rowData.medicineId;
            renderMedicineData(rowData);
        });
    }

    const renderMedicineData = (medicineData) => {
        for (const key in medicineData) {
            if (key === 'unitPrice') {
                const vndPrice = CurrencyConverter.formatCurrencyVndWithoutSuffix(medicineData[key]);
                module.medicineUpdatingModalSelector.find(`input[name="unitPrice"]`).val(vndPrice);
                continue;
            }
            if (key === 'unit') {
                module.unitSelector.selectpicker('val', medicineData[key]);
                continue;
            }
            if (key === 'medicineCategories') {
                const categoryIds = medicineData[key] ? medicineData[key].map(item => item.categoryId) : [];
                module.medicineCategorySelector.selectpicker('val', categoryIds);
                continue;
            }
            module.medicineUpdatingModalSelector.find(`input[name="${key}"]`).val(medicineData[key]);
            module.medicineUpdatingModalSelector.find(`textarea[name="${key}"]`).val(medicineData[key]);
        }
    }

    const handleUpdateMedicineFormSubmission = () => {
        module.updateMedicineFormSelector.on('submit', function (e) {
            e.preventDefault();
            const formData = new FormData(this);
            const medicineUpdatingParams = Object.fromEntries(
                Array.from(formData.entries()).map(([key, value]) => [key, value === '' ? null : value])
            );
            medicineUpdatingParams.unitPrice = CurrencyConverter.getNumericValue(module.unitPriceSelector.val());
            medicineUpdatingParams.categoryIds = module.medicineCategorySelector.selectpicker('val');

            $.ajax({
                headers: {
                    "accept": "application/json",
                    "content-type": "application/json"
                },
                type: 'PUT',
                url: API_ADMIN_MEDICINE_WITH_ID.replace('{medicineId}', module.currentMedicineId),
                data: JSON.stringify(medicineUpdatingParams),
            })
                .done(() => {
                    SweetAlert.showAlert('success', operationSuccess, '');
                    module.medicineUpdatingModalSelector.modal('hide');
                    MedicineList.medicineListTableSelector.DataTable().draw('full-hold');
                })
                .fail((jqXHR) => {
                    App.handleResponseMessageByStatusCode(jqXHR);
                    FormHandler.handleServerValidationError(module.updateMedicineFormSelector, jqXHR)
                })
        })
    }

    return module;
})();

export const MedicineDeletion = (function () {
    const module = {};

    module.init = () => {
        handleMedicineDeletion();
    }

    const handleMedicineDeletion = () => {
        MedicineList.medicineListTableSelector.on('click', '.show-deletion-confirmation-btn', function () {
            SweetAlert.showConfirmation('error', confirmApplyTitle, cannotRedoAfterDeleting).then((result) => {
                if (result.isConfirmed) {
                    const rowData = MedicineList.medicineListTableSelector.DataTable().row($(this).closest('tr')).data();
                    deleteMedicine(rowData.medicineId);
                }
            });
        });
    }

    const deleteMedicine = (medicineId) => {
        $.ajax({
            type: 'DELETE',
            url: API_ADMIN_MEDICINE_WITH_ID.replace('{medicineId}', medicineId)
        })
            .done(() => {
                SweetAlert.showAlert('success', operationSuccess, '');
                MedicineList.medicineListTableSelector.DataTable().draw('page');
            })
            .fail((jqXHR) => {
                App.handleResponseMessageByStatusCode(jqXHR);
            })
    }

    return module;
})();

(function () {
    MedicineCreation.init();
    MedicineList.init();
    MedicineUpdating.init();
    MedicineDeletion.init();
})();