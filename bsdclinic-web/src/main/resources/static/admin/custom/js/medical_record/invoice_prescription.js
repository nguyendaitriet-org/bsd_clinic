import {App, Image} from "/common/js/app.js";
import {Printer} from "/common/js/print.js";
import {Status} from "/common/js/constant.js";
import {MedicalRecordUpdating, MedicalRecordPrescription} from "/admin/custom/js/medical_record/detail.js";
import {InvoiceCreation, InvoiceDetail, InvoiceDeletion} from "/admin/custom/js/invoice/script.js";
import {PrescriptionCreation, PrescriptionDetail, PrescriptionDeletion} from "/admin/custom/js/prescription/script.js";
import {AppointmentDetail} from "/admin/custom/js/appointment/appointment-list.js";

export const MedicalRecordInvoicePrescription = (function () {
    const module = {
        invoiceIdSelector: $('#invoice-id'),
        prescriptionIdSelector: $('#prescription-id'),

        createInvoiceAndPrescriptionButtonSelector: $('.create-invoice-prescription-btn'),
        seeInvoiceAndPrescriptionButtonSelector: $('.see-invoice-prescription-btn'),
        finishExaminationButtonSelector: $('.finish-examination-btn'),
        printInvoiceButtonSelector: $('.print-invoice-btn'),
        deleteInvoiceButtonSelector: $('.delete-invoice-btn'),
        printPrescriptionButtonSelector: $('.print-prescription-btn'),
        deletePrescriptionButtonSelector: $('.delete-prescription-btn'),

        clinicLogoSelector: $('.clinic-logo'),

        invoiceDetailFormSelector: $('#invoice-detail-form')
    }

    module.init = () => {
        handleCreateInvoiceAndPrescriptionButton();
        handleShowInvoiceAndPrescriptionDetail();
        handleDeleteInvoiceButton();
        handleDeletePrescriptionButton();
        handleFinishExaminationButton();
        renderClinicLogo();
        handlePrintInvoiceButton();
        handlePrintPrescriptionButton();
    }

    const renderClinicLogo = () => {
        const clinicLogoUrl = Image.getPublicImageApi('BSD_Clinic_Logo_circle.png', 'client_home');
        module.clinicLogoSelector.prop('src', clinicLogoUrl);
    }

    const handleCreateInvoiceAndPrescriptionButton = () => {
        module.createInvoiceAndPrescriptionButtonSelector.on('click', function () {
            /* Handle warning message before creating invoice and prescription */
            const createPrescriptionParams = PrescriptionCreation.getCreatePrescriptionParams();
            const warningMessage = handleCreateInvoicePrescriptionWarningMessage(
                MedicalRecordUpdating.selectedMedicalServiceIds.length,
                createPrescriptionParams.takenMedicines.length,
            );

            App.showSweetAlertConfirmation('warning', confirmApplyTitle, warningMessage).then((result) => {
                if (result.isConfirmed) {
                    /* Create prescription first */
                    PrescriptionCreation.createPrescription(createPrescriptionParams).then(prescriptionResponse => {
                        const createInvoiceParams = {
                            medicalRecordId: prescriptionResponse.medicalRecordId,
                            patientName: MedicalRecordUpdating.patientNameSelector.data('value'),
                            purchasedMedicines: prescriptionResponse.takenMedicines,
                            medicinesTotalPrice: MedicalRecordPrescription.getMedicineGrandTotalPrice()
                        }
                        /* Then create invoice with response from the created prescription */
                        InvoiceCreation.createInvoice(createInvoiceParams).then(() => {
                            const appointmentId = MedicalRecordUpdating.appointmentIdSelector.val();
                            AppointmentDetail.updateAppointment(appointmentId, {actionStatus: Status.APPOINTMENT.FINISHED});
                            App.showSweetAlert('success', createSuccess);
                            location.reload();
                        })
                    });
                }
            });
        })
    }

    const handleCreateInvoicePrescriptionWarningMessage = (selectedServicesCount, selectedMedicinesCount) => {
        if (selectedServicesCount === 0) {
            return noChosenMedicalService;
        }
        if (selectedMedicinesCount === 0) {
            return noChosenInternalMedicine;
        }
        return finishExaminationAfterCreatingInvoice;
    }

    const handleShowInvoiceAndPrescriptionDetail = () => {
        const invoiceId = module.invoiceIdSelector.val();
        const prescriptionId = module.prescriptionIdSelector.val();
        if (invoiceId) {
            InvoiceDetail.getInvoice(invoiceId).then((invoiceResponse) => {
                InvoiceDetail.renderInvoiceDetail(invoiceResponse, module.invoiceDetailFormSelector);
            });
        }
        if (prescriptionId) {
            PrescriptionDetail.getPrescription(prescriptionId).then((prescriptionResponse) => {
                PrescriptionDetail.renderPrescriptionDetail(prescriptionResponse);
            });
        }
        if (invoiceId && prescriptionId) {
            module.createInvoiceAndPrescriptionButtonSelector.prop('hidden', true);
            disableRelatedInputs();
        }
        if (!invoiceId && !prescriptionId) {
            module.seeInvoiceAndPrescriptionButtonSelector.prop('hidden', true);
        }
    }

    const disableRelatedInputs = () => {
        MedicalRecordUpdating.medicalHistorySelector.prop('disabled', true);
        MedicalRecordUpdating.diagnosisSelector.prop('disabled', true);
        MedicalRecordUpdating.advanceSelector.prop('disabled', true);
        MedicalRecordUpdating.medicalServiceSelector.prop('disabled', true);
        MedicalRecordUpdating.selectedServicesTableSelector.find('button').prop('hidden', true);
        MedicalRecordPrescription.internalMedicineSelector.prop('disabled', true);
        MedicalRecordPrescription.addExternalMedicineButtonSelector.prop('disabled', true);
        PrescriptionCreation.instructionSelector.prop('disabled', true);
        PrescriptionCreation.reExaminationSelector.prop('disabled', true);

        module.finishExaminationButtonSelector.prop('hidden', true);
    }

    const handleDeleteInvoiceButton = () => {
        module.deleteInvoiceButtonSelector.on('click', function () {
            App.showSweetAlertConfirmation('error', confirmApplyTitle, deleteInvoiceCaution).then((result) => {
                if (result.isConfirmed) {
                    const invoiceId = module.invoiceDetailFormSelector.find(InvoiceDetail.invoiceIdInput).val();
                    InvoiceDeletion.deleteInvoice(invoiceId)
                        .then(() => {
                            const appointmentId = MedicalRecordUpdating.appointmentIdSelector.val();
                            AppointmentDetail.updateAppointment(appointmentId, {actionStatus: Status.APPOINTMENT.EXAMINING});
                            App.showSweetAlert('success', operationSuccess);
                            location.reload();
                        })
                        .catch((jqXHR) => {
                            App.handleResponseMessageByStatusCode(jqXHR);
                        });
                }
            });
        });
    }

    const handleDeletePrescriptionButton = () => {
        module.deletePrescriptionButtonSelector.on('click', function () {
            App.showSweetAlertConfirmation('error', confirmApplyTitle, deletePrescriptionCaution).then((result) => {
                if (result.isConfirmed) {
                    const prescriptionId = PrescriptionDetail.prescriptionDetailFormSelector.find(PrescriptionDetail.prescriptionIdInput).val();
                    PrescriptionDeletion.deletePrescription(prescriptionId)
                        .then(() => {
                            const appointmentId = MedicalRecordUpdating.appointmentIdSelector.val();
                            AppointmentDetail.updateAppointment(appointmentId, {actionStatus: Status.APPOINTMENT.EXAMINING});
                            App.showSweetAlert('success', operationSuccess);
                            location.reload();
                        })
                        .catch((jqXHR) => {
                            App.handleResponseMessageByStatusCode(jqXHR);
                        });
                }
            });
        });
    }

    const handleFinishExaminationButton = () => {
        module.finishExaminationButtonSelector.on('click', function () {
            App.showSweetAlertConfirmation('error', confirmApplyTitle, finishExaminationCaution).then((result) => {
                if (result.isConfirmed) {
                    const appointmentId = MedicalRecordUpdating.appointmentIdSelector.val();
                    AppointmentDetail.updateAppointment(appointmentId, {actionStatus: Status.APPOINTMENT.FINISHED_NO_PAY})
                        .then(() => {
                            App.showSweetAlert('success', operationSuccess);
                            window.location.href = ADMIN_MEDICAL_RECORD_INDEX;
                        })
                        .catch((jqXHR) => {
                            App.handleResponseMessageByStatusCode(jqXHR);
                        });
                }
                });
            });
    }

    const handlePrintInvoiceButton = () => {
        module.printInvoiceButtonSelector.on('click', function () {
            const invoiceElement = module.invoiceDetailFormSelector.clone();
            invoiceElement.find('button').remove();
            const invoiceContent = invoiceElement.html();
            Printer.openPrintWindow(invoiceContent, invoiceTitle);
        });
    }

    const handlePrintPrescriptionButton = () => {
        module.printPrescriptionButtonSelector.on('click', function () {
            const prescriptionElement = PrescriptionDetail.prescriptionDetailFormSelector.clone();
            prescriptionElement.find('button').remove();
            const prescriptionContent = prescriptionElement.html();
            Printer.openPrintWindow(prescriptionContent, prescriptionTitle);
        });
    }

    return module;
})();
