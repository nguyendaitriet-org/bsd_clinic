import {App, SweetAlert} from "/common/js/app.js";
import {FormHandler} from "/common/js/form.js";
import {ServiceList} from "./service-list.js";

export const ServiceUpdating = (function () {
    const module = {
        medicalServiceUpdatingModalSelector: $('#update-medical-service-modal'),
        updateMedicalServiceFormSelector: $('#update-medical-service-form'),
    };

    module.init = () => {
        handleShowUpdatingModal();
        handleUpdateMedicalServiceFormSubmission();
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
                    window.location.href = ADMIN_MEDICAL_SERVICE_INDEX;
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
        handleShowMedicalServiceDeletionConfirmation();
    }

    const handleShowMedicalServiceDeletionConfirmation = () => {
        ServiceList.serviceListTableSelector.on('click', '.show-deletion-confirmation-btn', function () {
            SweetAlert.showConfirmation('error', confirmApplyTitle, cannotRedoAfterDeleting).then((result) => {
               if(result.isConfirmed){
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
                window.location.href = ADMIN_MEDICAL_SERVICE_INDEX;
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

