import {App} from "/common/js/app.js";
import {RequestHeader} from "/common/js/constant.js";

export const InvoiceCreation = (function () {
    const module = {};

    module.init = () => {}

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