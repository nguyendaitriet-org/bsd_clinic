import {App} from "/common/js/app.js";
import {FormHandler} from "/common/js/form.js";

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

(function () {
    MedicineCreation.init();
})();