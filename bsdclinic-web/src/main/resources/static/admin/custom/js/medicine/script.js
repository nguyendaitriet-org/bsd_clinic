import {App} from "/common/js/app.js";
import {CurrencyConverter} from "/common/js/currency_util.js";
import {FormHandler} from "/common/js/form.js";
import {DatatableAttribute} from "/common/js/app.js";

export const MedicineCreation = (function () {
    const module = {
        createMedicineFormSelector: $('#create-medicine-form'),
        saveMedicineButtonSelector: $('.btn-save')
    };

    module.init = () => {
        handleCreateMedicineFormSubmission();
    }

    const handleCreateMedicineFormSubmission = () => {
        module.createMedicineFormSelector.on('submit', function (e) {
            e.preventDefault();
            const formData = new FormData(this);
            const medicineCreationParams = Object.fromEntries(
                Array.from(formData.entries()).map(([key, value]) => [key, value === '' ? null : value])
            );

            $.ajax({
                headers: {
                    "accept": "application/json",
                    "content-type": "application/json"
                },
                type: 'POST',
                url: API_ADMIN_MEDICINE,
                data: JSON.stringify(medicineCreationParams),
            })
                .done(() => {
                    App.showSweetAlert('success', createSuccess, '');
                    setTimeout(() => window.location.href = ADMIN_MEDICINE_INDEX, 1000);
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
        searchSubmitButtonSelector: $('#submit-btn'),
        cancelSearchButtonSelector: $('#cancel-btn'),

        medicineListTableSelector: $('#medicine-list-table'),
    };

    module.init = () => {
        module.renderMedicineListTable();
        handleSearchSubmissionButton();
        handleCancelSearchButton();
    }

    const handleCancelSearchButton = () => {
        module.cancelSearchButtonSelector.on('click', function () {
            module.searchInputSelector.val('');
            module.renderMedicineListTable();
        })
    }

    const handleSearchSubmissionButton = () => {
        module.searchSubmitButtonSelector.on('click', function () {
            module.renderMedicineListTable();
        });
    }

    const getMedicineListFilter = () => {
        return {
            keyword: module.searchInputSelector.val().trim(),
        }
    }

    module.renderMedicineListTable = () => {
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
                    targets: [0, 2, 3, 4],
                    className: "text-center"
                },
                {
                    targets: 2,
                    render: (data) => {
                        return CurrencyConverter.formatCurrencyVND(data);
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
    };

    module.init = () => {
        handleShowUpdatingModal();
        handleUpdateMedicineFormSubmission();
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
                    App.showSweetAlert('success', operationSuccess, '');
                    setTimeout(() => window.location.href = ADMIN_MEDICINE_INDEX, 1000);
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
        handleShowMedicineDeletionConfirmation();
    }

    const handleShowMedicineDeletionConfirmation = () => {
        MedicineList.medicineListTableSelector.on('click', '.show-deletion-confirmation-btn', function () {
            App.showSweetAlertConfirmation('error', confirmApplyTitle, cannotRedoAfterDeleting).then(() => {
                const rowData = MedicineList.medicineListTableSelector.DataTable().row($(this).closest('tr')).data();
                deleteMedicine(rowData.medicineId);
            });
        });
    }

    const deleteMedicine = (medicineId) => {
            $.ajax({
                type: 'DELETE',
                url: API_ADMIN_MEDICINE_WITH_ID.replace('{medicineId}', medicineId)
            })
                .done(() => {
                    App.showSweetAlert('success', operationSuccess, '');
                    setTimeout(() => window.location.href = ADMIN_MEDICINE_INDEX, 1000);
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