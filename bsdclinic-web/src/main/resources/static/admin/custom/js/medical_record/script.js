import {App} from "/common/js/app.js";
import {DateTimeConverter} from "/common/js/datetime_util.js";
import {CurrencyConverter} from "/common/js/currency_util.js";

export const MedicalRecordCreation = (function () {
    const module = {};

    module.init = () => {}

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
        patientBirthdayInputSelector: $('#patient-birthday'),
        medicalServiceSelector: $('#medical-service'),
        backToListButtonSelector: $('#back-btn'),
        selectedServicesTableSelector: $('#selected-services-table'),
        serviceTotalPriceSelector: $('#service-total-price'),
        servicePriceTextSelector: $('.service-price-text'),

        selectedMedicalServiceIds: []
    };

    module.init = () => {
        reformatMedicalServicePrice();
        initBirthdayPicker();
        initMedicalServiceServerSelect();
        handleBackToListButton();
        handleSelectedMedicalService();
        handleRemoveSelectedServiceButton();
        renderMedicalServiceTotalPrice();
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
        })
    }

    const handleRemoveSelectedServiceButton = () => {
        module.selectedServicesTableSelector.on('click', '.btn-remove-service', function () {
            $(this).closest('tr').remove();
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
        module.backToListButtonSelector.on('click',  () => location.href = ADMIN_APPOINTMENT_FOR_DOCTOR);
    }

    return module;
})();
