import {App} from "/common/js/app.js";
import {FormHandler} from "/common/js/form.js";
import {CurrencyConverter} from "/common/js/currency_util.js";
import {ServiceList} from "/admin/custom/js/service/service-list.js";

export const ServiceCreation = (function () {
    const module = {
        titleSelector: $('#create-medical-service-modal .title-input'),
        priceSelector: $('#create-medical-service-modal .price-input'),
        descriptionSelector: $('#create-medical-service-modal .description-input'),
        saveButtonSelector: $('#create-medical-service-modal .btn-save'),
        medicalServiceCreationModalSelector: $('#create-medical-service-modal')
    };

    module.init = () => {
        handleSaveButton();
        CurrencyConverter.setupPriceFormatter(module.priceSelector);
    }

    const handleSaveButton = () => {
        module.saveButtonSelector.on('click', function () {
            const medicalServiceCreationData = getMedicalServiceCreationData();
            createNewMedicalService(medicalServiceCreationData);
        });
    }

    const getMedicalServiceCreationData = () => {
        return (
            {
                title: module.titleSelector.val().trim(),
                price: CurrencyConverter.getNumericValue(module.priceSelector.val()),
                description:  module.descriptionSelector.val().trim(),
            }
        );
    }

    const createNewMedicalService = (medicalServiceCreationData) => {
        $.ajax({
            headers: {
                "accept": "application/json",
                "content-type": "application/json"
            },
            type: 'POST',
            url: API_ADMIN_MEDICAL_SERVICE,
            data: JSON.stringify(medicalServiceCreationData),
        })
            .done(() => {
                module.medicalServiceCreationModalSelector.modal('hide');
                App.showSuccessMessage(createSuccess);
                ServiceList.serviceListTableSelector.DataTable().row.add(medicalServiceCreationData).draw('full-hold');
            })
            .fail((jqXHR) => {
                App.handleResponseMessageByStatusCode(jqXHR);
                FormHandler.handleServerValidationError(module.medicalServiceCreationModalSelector, jqXHR)
            })
    }

    return module;
})();

(function () {
    ServiceCreation.init();
})();

