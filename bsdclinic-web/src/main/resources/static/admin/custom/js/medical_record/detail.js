import {App} from "/common/js/app.js";
import {FormHandler} from "/common/js/form.js";
import {DateTimeConverter} from "/common/js/datetime_util.js";
import {DateTimePattern} from "/common/js/constant.js";
import {CurrencyConverter} from "/common/js/currency_util.js";
import {PrescriptionCreation, PrescriptionDetail} from "/admin/custom/js/prescription/script.js";
import {InvoiceCreation, InvoiceDetail} from "/admin/custom/js/invoice/script.js";

export const MedicalRecordCreation = (function () {
    const module = {};

    module.init = () => {
    }

    module.createMedicalRecord = (appointmentId) => {
        return $.ajax({
            headers: {
                "accept": "application/json",
                "content-type": "application/json"
            },
            type: 'POST',
            url: API_ADMIN_MEDICAL_RECORD,
            data: JSON.stringify({appointmentId}),
        })
            .fail((jqXHR) => {
                App.handleResponseMessageByStatusCode(jqXHR);
            })
    }

    return module;
})();

export const MedicalRecordUpdating = (function () {
    const module = {
        medicalRecordDetailArea: $('.medical-record-detail-area'),
        appointmentIdSelector: $('#appointment-id'),
        medicalRecordIdSelector: $('#medical-record-id'),
        patientNameSelector: $('#patient-full-name'),
        patientBirthdayInputSelector: $('#patient-birthday'),
        patientGenderSelector: $('input[name="patientGender"]'),
        patientPhoneSelector: $('#patient-phone'),
        patientEmailSelector: $('#patient-email'),
        patientAddressSelector: $('#patient-address'),
        visitReasonSelector: $('#visit-reason'),
        medicalHistorySelector: $('#medical-history'),
        diagnosisSelector: $('#diagnosis'),
        advanceSelector: $('#advance'),

        medicalServiceSelector: $('#medical-service'),
        backToListButtonSelector: $('#back-btn'),
        selectedServicesTableSelector: $('#selected-services-table'),
        serviceTotalPriceSelector: $('#service-total-price'),
        servicePriceTextSelector: $('.service-price-text'),
        saveMedicalRecordButton: $('.save-record-btn'),

        selectedMedicalServiceIds: []
    };

    module.init = () => {
        handleSelectedMedicalServiceIds();
        reformatMedicalServicePrice();
        initBirthdayPicker();
        initMedicalServiceServerSelect();
        handleBackToListButton();
        handleSelectedMedicalService();
        handleRemoveSelectedServiceButton();
        renderMedicalServiceTotalPrice();
        handleSaveMedicalRecordButton();
        CurrencyConverter.setupPriceFormatter(module.advanceSelector);
    }

    const handleSelectedMedicalServiceIds = () => {
        $('input[name="medicalServiceIds[]"]').each(function () {
            module.selectedMedicalServiceIds.push(this.value);
        });
    }

    const reformatMedicalServicePrice = () => {
        module.servicePriceTextSelector.each((index, element) => {
            const servicePrice = $(element).data('price');
            $(element).text(CurrencyConverter.formatCurrencyVND(servicePrice));
        })
    }

    const initBirthdayPicker = () => {
        const displayBirthday = DateTimeConverter.convertToDisplayPattern(module.patientBirthdayInputSelector.val())
        module.patientBirthdayPicker = new Lightpick({
            field: module.patientBirthdayInputSelector[0],
            lang: 'vi',
            startDate: displayBirthday
        });
    }

    const initMedicalServiceServerSelect = () => {
        module.medicalServiceSelector.bootstrapServerSelect({
            apiUrl: API_ADMIN_MEDICAL_SERVICE,
            paramName: 'keyword',
            minLength: 2,
            placeholder: medicalServiceFinding,
            noResultsText: emptyTable,
            loadingText: finding,
            debounceDelay: 300,
            template: (item, index) => {
                return `<div class="bs-server-select-item" data-value="${item.medicalServiceId}" data-text="${item.title}" data-price="${item.price}">
                            <div class="fw-bolder">${item.title}</div>
                            <small class="text-muted">${CurrencyConverter.formatCurrencyVND(item.price)}</small>
                        </div>`;
            }
        });
    }

    const getSelectedServiceRow = (medicalServiceId, title, price) => {
        return `
            <tr>
                <input type="hidden" name="medicalServiceIds[]" value="${medicalServiceId}">
                <input type="hidden" name="medicalServicePrice[]" value="${price}">
                <td>${title}</td>
                <td class="service-price-text" data-price="${price}">${CurrencyConverter.formatCurrencyVND(price)}</td>
                <td class="text-center">
                    <button type="button" class="btn btn-sm btn-outline-danger btn-remove-service">X</button>
                </td>
            </tr>
        `;
    }

    const handleSelectedMedicalService = () => {
        module.medicalServiceSelector.on('bss:select', function (e, value, text, item) {
            module.selectedMedicalServiceIds.push(value);

            const servicePrice = item.data('price');
            const serviceRow = getSelectedServiceRow(value, text, servicePrice);
            module.selectedServicesTableSelector.find('tbody').append(serviceRow);

            renderMedicalServiceTotalPrice();
        });
    }

    const handleRemoveSelectedServiceButton = () => {
        module.selectedServicesTableSelector.on('click', '.btn-remove-service', function () {
            const currentRowSelector = $(this).closest('tr');
            const removedServiceId = currentRowSelector.find('input[name="medicalServiceIds[]"]').val();
            module.selectedMedicalServiceIds = module.selectedMedicalServiceIds.filter(item => item !== removedServiceId);
            currentRowSelector.remove();
            renderMedicalServiceTotalPrice();
        });
    }

    const renderMedicalServiceTotalPrice = () => {
        const serviceTotalPrice = getMedicalServiceTotalPrice();
        module.serviceTotalPriceSelector.text(CurrencyConverter.formatCurrencyVND(serviceTotalPrice));
    }

    const getMedicalServiceTotalPrice = () => {
        let total = 0;
        $('input[name="medicalServicePrice[]"]').each(function () {
            total += parseFloat($(this).val()) || 0;
        });

        return total;
    }

    const handleBackToListButton = () => {
        module.backToListButtonSelector.on('click', () => location.href = ADMIN_MEDICAL_RECORD_INDEX);
    }

    /* Handle API integration */
    const getMedicalRecordData = () => {
        return ({
            patientName: module.patientNameSelector.val().trim(),
            patientBirthday: DateTimeConverter.convertMomentToDateString(module.patientBirthdayPicker.getDate(), DateTimePattern.API_DATE_FORMAT),
            patientGender: module.patientGenderSelector.filter(":checked").val(),
            patientPhone: module.patientPhoneSelector.val().trim(),
            patientEmail: module.patientEmailSelector.val().trim(),
            patientAddress: module.patientAddressSelector.val().trim(),
            visitReason: module.visitReasonSelector.val().trim(),
            medicalHistory: module.medicalHistorySelector.val().trim(),
            diagnosis: module.diagnosisSelector.val().trim(),
            advance: CurrencyConverter.getNumericValue(module.advanceSelector),
            medicalServiceIds: module.selectedMedicalServiceIds
        });
    }

    const handleSaveMedicalRecordButton = () => {
        module.saveMedicalRecordButton.on('click', function () {
            const medicalRecordData = getMedicalRecordData();
            App.showSweetAlertConfirmation('warning', confirmApplyTitle, '').then((result) => {
                if (result.isConfirmed) {
                    saveMedicalRecordData(medicalRecordData);
                }
            });
        });
    }

    const saveMedicalRecordData = (medicalRecordData) => {
        const appointmentId = module.appointmentIdSelector.val();
        const medicalRecordId = module.medicalRecordIdSelector.val();
        const requestUrl = API_ADMIN_MEDICAL_RECORD_APPOINTMENT
            .replace('{medicalRecordId}', medicalRecordId)
            .replace('{appointmentId}', appointmentId);

        $.ajax({
            headers: {
                "accept": "application/json",
                "content-type": "application/json"
            },
            type: 'PUT',
            url: requestUrl,
            data: JSON.stringify(medicalRecordData),
        })
            .done(() => {
                App.showSweetAlert('success', operationSuccess, '');
                setTimeout(() => location.reload(), 1000);
            })
            .fail((jqXHR) => {
                FormHandler.handleServerValidationError(module.medicalRecordDetailArea, jqXHR);
                App.handleResponseMessageByStatusCode(jqXHR);
            })
    }

    return module;
})();

export const MedicalRecordDeletion = (function () {
    const module = {
        deleteMedicalRecordButtonSelector: $('.delete-record-btn'),
    };

    module.init = () => {
        handleDeleteMedicalRecord();
    }

    const handleDeleteMedicalRecord = () => {
        module.deleteMedicalRecordButtonSelector.on('click', function () {
            App.showSweetAlertConfirmation('error', confirmApplyTitle, cannotRedoAfterDeleting).then((result) => {
                if (result.isConfirmed) {
                    const appointmentId = MedicalRecordUpdating.appointmentIdSelector.val();
                    const medicalRecordId = MedicalRecordUpdating.medicalRecordIdSelector.val();
                    deleteMedicalRecord(medicalRecordId, appointmentId);
                }
            });
        });
    }

    const deleteMedicalRecord = (medicalRecordId, appointmentId) => {
        const requestUrl = API_ADMIN_MEDICAL_RECORD_APPOINTMENT
            .replace('{medicalRecordId}', medicalRecordId)
            .replace('{appointmentId}', appointmentId);

        return $.ajax({
            type: 'DELETE',
            url: requestUrl,
        })
            .done(() => {
                App.showSweetAlert('success', operationSuccess, '');
                setTimeout(() => window.location.href = ADMIN_APPOINTMENT_FOR_DOCTOR, 700);
            })
            .fail((jqXHR) => {
                App.handleResponseMessageByStatusCode(jqXHR);
            })
    }

    return module;
})();

export const MedicalRecordPrescription = (function () {
    const module = {
        internalMedicineSelector: $('#internal-medicine'),
        selectedInternalMedicinesTableSelector: $('#selected-internal-medicines-table'),
        medicineTotalPriceSelector: $('#medicine-total-price'),
        medicineQuantitySelector: $('.medicine-total-price'),

        externalMedicinesTableSelector: $('#external-medicines-table'),
        addExternalMedicineButtonSelector: $('#btn-add-external-medicine'),

        createInvoiceAndPrescriptionButtonSelector: $('.create-invoice-prescription-btn'),
        seeInvoiceAndPrescriptionButtonSelector: $('.see-invoice-prescription-btn')
    };

    module.init = () => {
        initMedicineServerSelect();
        handleSelectedInternalMedicine();
        handleRemoveInternalMedicineButton();
        handleInternalMedicineQuantityChange();
        handleAddExternalMedicineButton();
        handleRemoveExternalMedicineButton();
        handleCreateInvoiceAndPrescriptionButton();
    }

    /* Handle internal medicine */

    const initMedicineServerSelect = () => {
        module.internalMedicineSelector.bootstrapServerSelect({
            apiUrl: API_ADMIN_MEDICINE,
            paramName: 'keyword',
            minLength: 2,
            placeholder: medicineFinding,
            noResultsText: emptyTable,
            loadingText: finding,
            debounceDelay: 300,
            template: item =>
                `<div class="bs-server-select-item" data-value="${item.medicineId}" data-usage="${item.dosage}"
                        data-text="${item.title}" data-price="${item.unitPrice}">
                    <div class="fw-bolder">${item.title}</div>
                    <small class="text-muted">${CurrencyConverter.formatCurrencyVND(item.unitPrice)}</small>
              </div>`
        });
    }

    const handleSelectedInternalMedicine = () => {
        module.internalMedicineSelector.on('bss:select', function (e, value, text, item) {
            const medicineUnitPrice = item.data('price');
            const medicineUsage = item.data('usage');
            const serviceRow = getSelectedInternalMedicineRow(value, text, medicineUnitPrice, medicineUsage);
            module.selectedInternalMedicinesTableSelector.find('tbody').append(serviceRow);
            renderMedicineTotalPrice();
        });
    }

    const getSelectedInternalMedicineRow = (medicineId, title, unitPrice, usage) => {
        return `
            <tr>
                <input class="medicine-id" type="hidden" value="${medicineId}">
                <td>${title}</td>
                <td>
                    <input class="medicine-quantity-input" min="0" class="form-control form-control-sm" type="number"
                            name="quantity" value="1" data-price="${unitPrice}">
                </td>
                <td class="medicine-unit-price-text" data-price="${unitPrice}">
                    ${CurrencyConverter.formatCurrencyVND(unitPrice)}
                </td>
                <td>
                    <input class="form-control form-control-sm medicine-usage-input" type="text" name="usage" value="${usage}">
                </td>
                <td class="medicine-purchased-total-price-text" data-price="${unitPrice}">
                    ${CurrencyConverter.formatCurrencyVND(unitPrice)}
                </td>
                <td class="text-center">
                    <button type="button" class="btn btn-sm btn-outline-danger btn-remove-internal-medicine">X</button>
                </td>
            </tr>
        `;
    }

    const handleInternalMedicineQuantityChange = () => {
        module.selectedInternalMedicinesTableSelector.on('change', '.medicine-quantity-input', function () {
            const unitPriceSelector = $(this).closest('tr').find('.medicine-unit-price-text');
            const unitPriceValue = unitPriceSelector.data('price');
            const purchasedTotalPrice = Number($(this).val()) * Number(unitPriceValue);
            const purchasedTotalPriceSelector = $(this).closest('tr').find('.medicine-purchased-total-price-text')
            purchasedTotalPriceSelector.data('price', purchasedTotalPrice);
            purchasedTotalPriceSelector.text(CurrencyConverter.formatCurrencyVND(purchasedTotalPrice))
            renderMedicineTotalPrice();
        })
    }

    const renderMedicineTotalPrice = () => {
        const serviceTotalPrice = getMedicineGrandTotalPrice();
        module.medicineTotalPriceSelector.text(CurrencyConverter.formatCurrencyVND(serviceTotalPrice));
    }

    const getMedicineGrandTotalPrice = () => {
        let grandTotal = 0;
        $('.medicine-purchased-total-price-text').each(function () {
            const purchasedTotalPrice = Number($(this).data('price') || 0);
            grandTotal += purchasedTotalPrice;
        });

        return grandTotal;
    }

    const handleRemoveInternalMedicineButton = () => {
        module.selectedInternalMedicinesTableSelector.on('click', '.btn-remove-internal-medicine', function () {
            const currentRowSelector = $(this).closest('tr');
            currentRowSelector.remove();
            renderMedicineTotalPrice();
        });
    }

    /* Handle external medicine */

    const handleAddExternalMedicineButton = () => {
        module.addExternalMedicineButtonSelector.on('click', function () {
            module.externalMedicinesTableSelector.find('tbody').append(
                `<tr>
                    <td>
                        <input class="form-control form-control-sm medicine-title" type="text">
                    </td>
                    <td>
                        <input class="form-control form-control-sm medicine-quantity" type="text">
                    </td>
                    <td>
                        <input class="form-control form-control-sm medicine-usage" type="text">
                    </td>
                    <td class="text-center">
                        <button type="button" class="btn btn-sm btn-outline-danger btn-remove-external-medicine">X</button>
                    </td>
                </tr>`
            );
        });
    }

    const handleRemoveExternalMedicineButton = () => {
        module.externalMedicinesTableSelector.on('click', '.btn-remove-external-medicine', function () {
            const currentRowSelector = $(this).closest('tr');
            currentRowSelector.remove();
        });
    }

    /* Handle invoice and prescription creation */

    const handleCreateInvoiceAndPrescriptionButton = () => {
        module.createInvoiceAndPrescriptionButtonSelector.on('click', function () {
            App.showSweetAlertConfirmation('warning', confirmApplyTitle, finishExaminationAfterCreatingInvoice).then((result) => {
                if (result.isConfirmed) {
                    const createPrescriptionParams = PrescriptionCreation.getCreatePrescriptionParams();
                    /* Create prescription first */
                    PrescriptionCreation.createPrescription(createPrescriptionParams).then(prescriptionResponse => {
                        const createInvoiceParams = {
                            medicalRecordId: prescriptionResponse.medicalRecordId,
                            patientName: MedicalRecordUpdating.patientNameSelector.data('value'),
                            purchasedMedicines: prescriptionResponse.takenMedicines,
                            medicinesTotalPrice: getMedicineGrandTotalPrice()
                        }
                        /* Then create invoice with response from the created prescription */
                        InvoiceCreation.createInvoice(createInvoiceParams).then(invoiceResponse => {
                            InvoiceDetail.renderInvoiceDetail(invoiceResponse);
                            PrescriptionDetail.renderPrescriptionDetail(prescriptionResponse);
                            App.showSweetAlert('success', createSuccess);
                        })
                    });
                }
            });
        })
    }

    return module;
})();
