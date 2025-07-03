import {App} from "/common/js/app.js";
import {RequestHeader} from "/common/js/constant.js";
import {CurrencyConverter} from "/common/js/currency_util.js";
import {DateTimeConverter} from "/common/js/datetime_util.js";

export const InvoiceCreation = (function () {
    const module = {};

    module.init = () => {
    }

    module.createInvoice = (invoiceCreationParams) => {
        return $.ajax({
            headers: RequestHeader.JSON_TYPE,
            type: 'POST',
            url: API_ADMIN_INVOICE,
            data: JSON.stringify(invoiceCreationParams),
        })
            .fail((jqXHR) => {
                App.handleResponseMessageByStatusCode(jqXHR);
            })
    }

    return module;
})();

export const InvoiceDetail = (function () {
    const module = {
        invoiceDetailFormSelector: $('#invoice-detail-form'),
        tableMedicalServicesDetail: $('.table-medical-services-detail'),
        tableTakenMedicinesDetail: $('.table-taken-medicines-detail'),

        invoiceIdInput: 'input[data-attribute="invoiceId"]'
    };

    module.init = () => {
    }

    const getPurchasedServiceRow = ({title, price}) =>
        `<tr>
            <td>${title}</td>
            <td>${CurrencyConverter.formatCurrencyVND(price)}</td>
        </tr>`;

    const getTakenMedicineRow = ({title, purchasedQuantity, unitPrice, purchasedTotalPrice}) =>
        `<tr>
            <td>${title}</td>
            <td>${purchasedQuantity}</td>
            <td>${CurrencyConverter.formatCurrencyVND(unitPrice)}</td>
            <td>${CurrencyConverter.formatCurrencyVND(purchasedTotalPrice)}</td>
        </tr>`;


    module.renderInvoiceDetail = (invoiceDetail) => {
        for (const [key, value] of Object.entries(invoiceDetail)) {
            if (key === 'invoiceId') {
                module.invoiceDetailFormSelector.find(module.invoiceIdInput).val(value);
            }

            if (key === 'purchasedServices') {
                let purchasedServiceRows = '';
                for (const purchasedService of value) {
                    purchasedServiceRows += getPurchasedServiceRow(purchasedService);
                }
                module.tableMedicalServicesDetail.find('tbody').append(purchasedServiceRows);
                continue;
            }

            if (key === 'purchasedMedicines') {
                let takenMedicineRows = '';
                for (const takenMedicine of value) {
                    takenMedicineRows += getTakenMedicineRow(takenMedicine);
                }
                module.tableTakenMedicinesDetail.find('tbody').append(takenMedicineRows);
                continue;
            }

            if (key === 'createdAt') {
                const displayDate = DateTimeConverter.convertToDisplayPattern(value);
                module.invoiceDetailFormSelector.find(`span[data-attribute='createdAt']`).text(displayDate);
                continue;
            }

            if (typeof value === 'number') {
                const displayPrice = CurrencyConverter.formatCurrencyVND(value);
                module.invoiceDetailFormSelector.find(`span[data-attribute='${key}']`).text(displayPrice);
                continue;
            }

            module.invoiceDetailFormSelector.find(`span[data-attribute='${key}']`).text(value);
        }
    }

    module.getInvoice = (invoiceId) => {
        return $.ajax({
            url: API_ADMIN_INVOICE_WITH_ID.replace('{invoiceId}', invoiceId)
        })
            .fail((jqXHR) => {
                App.handleResponseMessageByStatusCode(jqXHR);
            })
    }

    return module;
})();

export const InvoiceDeletion = (function () {
    const module = {};

    module.init = () => {}

    module.deleteInvoice = (invoiceId) => {
        return $.ajax({
            type: 'DELETE',
            url: API_ADMIN_INVOICE_WITH_ID.replace('{invoiceId}', invoiceId)
        })
            .fail((jqXHR) => {
                App.handleResponseMessageByStatusCode(jqXHR);
            })
    }

    return module;
})();