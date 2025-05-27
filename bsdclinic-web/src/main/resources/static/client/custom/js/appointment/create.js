import {App} from "/common/js/app.js";
import {FormHandler} from "/common/js/form.js";

export const AppointmentCreation = (function () {
    const module = {
        registerDateSelector: $('#register-date'),
        patientBirthdaySelector: $('#patient-birthday'),
        registerTimeSelector: $('#register-time')
    };

    module.init = () => {
        initDatePicker();
        toggleSelectMutedClass();
        toggleSelectMutedClass();
    }

    const initDatePicker = () => {
        module.registerDate = new Lightpick({
            field: module.registerDateSelector[0],
            lang: 'vi',
            minDate: moment()
        });

        module.patientBirthday = new Lightpick({
            field: module.patientBirthdaySelector[0],
            lang: 'vi',
        });
    }

    const toggleSelectMutedClass = () => {
        module.registerTimeSelector.on('change', function () {
            const selectedOption = $(this).find('option:selected');

            if (selectedOption.hasClass('text-muted')) {
                $(this).addClass('text-muted');
            } else {
                $(this).removeClass('text-muted');
            }
        })
    }

    return module;
})();

(function () {
    AppointmentCreation.init();
})();

