import {App} from "/common/js/app.js";

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

