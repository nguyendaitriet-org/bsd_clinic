import {App} from "/common/js/app.js";
import {RequestHeader} from "/common/js/constant.js";
import {DateTimeConverter} from "/common/js/datetime_util.js";

export const PrescriptionCreation = (function () {
    const module = {
        medicalRecordIdSelector: $('#medical-record-id'),
        patientNameSelector: $('#patient-full-name'),
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
            patientName: module.patientNameSelector.data('value'),
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

export const PrescriptionDetail = (function () {
    const module = {
        invoiceDetailFormSelector: $('#prescription-detail-form'),
        tableInternalMedicineDetail: $('.table-internal-medicine'),
        tableExternalMedicineDetail: $('.table-external-medicine')

    };

    module.init = () => {}

    const getMedicineDetailRow = ({title, purchasedQuantity, usage}) =>
        `<tr>
            <td>${title}</td>
            <td>${purchasedQuantity}</td>
            <td>${usage}</td>
        </tr>`;

    module.renderPrescriptionDetail = (prescriptionDetail) => {
        for (const [key, value] of Object.entries(prescriptionDetail)) {
            if (key === 'externalMedicines') {
                let externalMedicineRows = '';
                for (const externalMedicine of value) {
                    externalMedicineRows += getMedicineDetailRow(externalMedicine);
                }
                module.tableExternalMedicineDetail.find('tbody').append(externalMedicineRows);
                continue;
            }

            if (key === 'takenMedicines') {
                let internalMedicineRows = '';
                for (const internalMedicine of value) {
                    internalMedicineRows += getMedicineDetailRow(internalMedicine);
                }
                module.tableInternalMedicineDetail.find('tbody').append(internalMedicineRows);
                continue;
            }

            if (key === 'createdAt') {
                const displayDate = DateTimeConverter.convertToDisplayPattern(value);
                module.invoiceDetailFormSelector.find(`span[data-attribute='${key}']`).text(displayDate);
                continue;
            }

            module.invoiceDetailFormSelector.find(`span[data-attribute='${key}']`).text(value);
        }
    }

    module.getPrescription = (prescriptionId) => {
        return $.ajax({
            url: API_ADMIN_PRESCRIPTION_WITH_ID.replace('{prescriptionId}', prescriptionId)
        })
            .fail((jqXHR) => {
                App.handleResponseMessageByStatusCode(jqXHR);
            })
    }

    return module;
})();