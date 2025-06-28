import {App} from "/common/js/app.js";
import {RequestHeader} from "/common/js/constant.js";

export const PrescriptionCreation = (function () {
    const module = {
        medicalRecordIdSelector: $('#medical-record-id'),
        instructionSelector: $('#instruction'),
        reExaminationSelector: $('#re-examination'),
        selectedInternalMedicinesTableSelector: $('#selected-internal-medicines-table'),
        externalMedicinesTableSelector: $('#external-medicines-table')
    };

    module.init = () => {}

    module.getCreatePrescriptionParams = () => {
        const takenMedicines = [];
        module.selectedInternalMedicinesTableSelector.find('tbody tr').each((index, item) => {
            const takenMedicine = {
                medicineId: $(item).find('.medicine-id').val(),
                purchasedQuantity: $(item).find('.medicine-quantity-input').val(),
                purchasedTotalPrice: $(item).find('.medicine-purchased-total-price-text').data('price'),
                usage: $(item).find('.medicine-usage-input').val()
            };
            takenMedicines.push(takenMedicine);
        });

        const externalMedicines = [];
        module.externalMedicinesTableSelector.find('tbody tr').each((index, item) => {
            /* Check the empty 'title' to ignore the invalid record */
            const title = $(item).find('.medicine-title').val().trim();
            if (!title) return;

            const takenMedicine = {
                title: title,
                purchasedQuantity: $(item).find('.medicine-quantity').val().trim(),
                usage: $(item).find('.medicine-usage').val().trim()
            };
            externalMedicines.push(takenMedicine);
        });

        return {
            medicalRecordId: module.medicalRecordIdSelector.val(),
            instruction: module.instructionSelector.val().trim(),
            reExamination: module.reExaminationSelector.val().trim(),
            takenMedicines,
            externalMedicines
        }
    }

    module.createPrescription = (prescriptionCreationParams) => {
        return $.ajax({
            headers: RequestHeader.JSON_TYPE,
            type: 'POST',
            url: API_ADMIN_PRESCRIPTION,
            data: JSON.stringify(prescriptionCreationParams),
        })
            .fail((jqXHR) => {
                App.handleResponseMessageByStatusCode(jqXHR);
            })
    }

    return module;
})();