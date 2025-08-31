import {App, SweetAlert} from "/common/js/app.js";
import {FormHandler} from "/common/js/form.js";
import {ServiceList} from "/admin/custom/js/service/service-list.js";
import {CurrencyConverter} from "/common/js/currency_util.js";

export const ServiceUpdating = (function () {
    const module = {
        medicalServiceUpdatingModalSelector: $('#update-medical-service-modal'),
        updateMedicalServiceFormSelector: $('#update-medical-service-form'),
        priceSelector: $('#update-medical-service-modal .price-input'),
        serviceCategorySelector: $('#update-medical-service-modal .category-select'),

    };

    module.init = () => {
        handleShowUpdatingModal();
        handleUpdateMedicalServiceFormSubmission();
        CurrencyConverter.setupPriceFormatter(module.priceSelector);
    }

    const handleShowUpdatingModal = () => {
        ServiceList.serviceListTableSelector.on('click', '.show-updating-modal-btn', function () {
            module.medicalServiceUpdatingModalSelector.modal('show');
            const rowData = ServiceList.serviceListTableSelector.DataTable().row($(this).closest('tr')).data();
            module.currentMedicalServiceId = rowData.medicalServiceId;
            renderMedicalServiceData(rowData);
        });
    }

    const renderMedicalServiceData = (medicalServiceData) => {
        for (const key in medicalServiceData) {
            if (key === 'price') {
                const vndPrice = CurrencyConverter.formatCurrencyVndWithoutSuffix(medicalServiceData[key]);
                module.medicalServiceUpdatingModalSelector.find(`input[name="price"]`).val(vndPrice);
                continue;
            }
            if (key === 'serviceCategories') {
                const categoryIds = medicalServiceData[key] ? medicalServiceData[key].map(item => item.categoryId) : [];
                module.serviceCategorySelector.selectpicker('val', categoryIds);
                continue;
            }
            module.medicalServiceUpdatingModalSelector.find(`input[name="${key}"]`).val(medicalServiceData[key]);
            module.medicalServiceUpdatingModalSelector.find(`textarea[name="${key}"]`).val(medicalServiceData[key]);
        }
    }

    const handleUpdateMedicalServiceFormSubmission = () => {
        module.updateMedicalServiceFormSelector.on('submit', function (e) {
            e.preventDefault();
            const formData = new FormData(this);
            const medicalServiceUpdatingParams = Object.fromEntries(
                Array.from(formData.entries()).map(([key, value]) => [key, value === '' ? null : value])
            );
            medicalServiceUpdatingParams.price = CurrencyConverter.getNumericValue(module.priceSelector.val());
            medicalServiceUpdatingParams.categoryIds = module.serviceCategorySelector.selectpicker('val');

            $.ajax({
                headers: {
                    "accept": "application/json",
                    "content-type": "application/json"
                },
                type: 'PUT',
                url: API_ADMIN_MEDICAL_SERVICE_WITH_ID.replace('{medicalServiceId}', module.currentMedicalServiceId),
                data: JSON.stringify(medicalServiceUpdatingParams),
            })
                .done(() => {
                    SweetAlert.showAlert('success', operationSuccess, '');
                    module.medicalServiceUpdatingModalSelector.modal('hide');
                    ServiceList.serviceListTableSelector.DataTable().draw('full-hold');
                })
                .fail((jqXHR) => {
                    App.handleResponseMessageByStatusCode(jqXHR);
                    FormHandler.handleServerValidationError(module.updateMedicalServiceFormSelector, jqXHR)
                })
        })
    }

    return module;
})();

export const MedicalServiceDeletion = (function () {
    const module = {};

    module.init = () => {
        handleMedicalServiceDeletion();
    }

    const handleMedicalServiceDeletion = () => {
        ServiceList.serviceListTableSelector.on('click', '.show-deletion-confirmation-btn', function () {
            SweetAlert.showConfirmation('error', confirmApplyTitle, cannotRedoAfterDeleting).then((result) => {
                if (result.isConfirmed) {
                    const rowData = ServiceList.serviceListTableSelector.DataTable().row($(this).closest('tr')).data();
                    deleteMedicalService(rowData.medicalServiceId);
                }
            });
        });
    }

    const deleteMedicalService = (medicalServiceId) => {
        $.ajax({
            type: 'DELETE',
            url: API_ADMIN_MEDICAL_SERVICE_WITH_ID.replace('{medicalServiceId}', medicalServiceId)
        })
            .done(() => {
                SweetAlert.showAlert('success', operationSuccess, '');
                ServiceList.serviceListTableSelector.DataTable().draw('page');
            })
            .fail((jqXHR) => {
                App.handleResponseMessageByStatusCode(jqXHR);
            })
    }

    return module;
})();

(function () {
    ServiceUpdating.init();
    MedicalServiceDeletion.init();
})();

