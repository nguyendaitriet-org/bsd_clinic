import {App} from "/common/js/app.js";
import {MedicalRecordUpdating, MedicalRecordPrescription} from "/admin/custom/js/medical_record/detail.js";
import {InvoiceCreation, InvoiceDetail} from "/admin/custom/js/invoice/script.js";
import {PrescriptionCreation, PrescriptionDetail} from "/admin/custom/js/prescription/script.js";

export const MedicalRecordInvoicePrescription = (function () {
    const module = {
        invoiceIdSelector: $('#invoice-id'),
        prescriptionIdSelector: $('#prescription-id'),

        createInvoiceAndPrescriptionButtonSelector: $('.create-invoice-prescription-btn'),
        seeInvoiceAndPrescriptionButtonSelector: $('.see-invoice-prescription-btn'),
        finishExaminationButtonSelector: $('.finish-examination-btn'),
    }

    module.init = () => {
        handleCreateInvoiceAndPrescriptionButton();
        handleShowInvoiceAndPrescriptionDetail();
    }

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
                            medicinesTotalPrice: MedicalRecordPrescription.getMedicineGrandTotalPrice()
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

    const handleShowInvoiceAndPrescriptionDetail = () => {
        const invoiceId = module.invoiceIdSelector.val();
        const prescriptionId = module.prescriptionIdSelector.val();
        if (invoiceId) {
            InvoiceDetail.getInvoice(invoiceId).then((invoiceResponse) => {
                InvoiceDetail.renderInvoiceDetail(invoiceResponse);
            });
        }
        if (prescriptionId) {
            PrescriptionDetail.getPrescription(prescriptionId).then((prescriptionResponse) => {
                PrescriptionDetail.renderPrescriptionDetail(prescriptionResponse);
            });
        }
        if (invoiceId && prescriptionId) {
            module.createInvoiceAndPrescriptionButtonSelector.prop('hidden', true)
            disableRelatedInputs();
        }
        if (!invoiceId && !prescriptionId) {
            module.seeInvoiceAndPrescriptionButtonSelector.prop('hidden', true)
        }
    }

    const disableRelatedInputs = () => {
        MedicalRecordUpdating.medicalHistorySelector.prop('disabled', true);
        MedicalRecordUpdating.diagnosisSelector.prop('disabled', true);
        MedicalRecordUpdating.advanceSelector.prop('disabled', true);
        MedicalRecordUpdating.medicalServiceSelector.prop('disabled', true);
        MedicalRecordPrescription.internalMedicineSelector.prop('disabled', true);
        MedicalRecordPrescription.addExternalMedicineButtonSelector.prop('disabled', true);
        PrescriptionCreation.instructionSelector.prop('disabled', true);
        PrescriptionCreation.reExaminationSelector.prop('disabled', true);

        module.finishExaminationButtonSelector.prop('hidden', true);
    }

    return module;
})();
