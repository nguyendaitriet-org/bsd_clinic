import {App} from "/common/js/app.js";
import {FormHandler} from "/common/js/form.js";

export const AppointmentCreation = (function () {
    const module = {
        patientBirthdayInputSelector: $('#patient-birthday')
    };

    module.init = () => {
        initDatePicker();
    }

    const initDatePicker = () => {
        module.patientBirthdayPicker = new Lightpick({
            field: module.patientBirthdayInputSelector[0],
            lang: 'vi'
        });
    }

    return module;
})();

(function () {
    AppointmentCreation.init();
})();

