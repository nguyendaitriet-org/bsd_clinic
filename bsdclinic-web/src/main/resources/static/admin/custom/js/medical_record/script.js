import {App} from "/common/js/app.js";
import {DateTimeConverter} from "/common/js/datetime_util.js";

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
        patientBirthdayInputSelector: $('#patient-birthday')
    };

    module.init = () => {
        initBirthdayPicker()
    }

    const initBirthdayPicker = () => {
        const displayBirthday = DateTimeConverter.convertToDisplayPattern(module.patientBirthdayInputSelector.val())
        module.patientBirthdayPicker = new Lightpick({
            field: module.patientBirthdayInputSelector[0],
            lang: 'vi',
            startDate: displayBirthday
        });
    }

    return module;
})();

