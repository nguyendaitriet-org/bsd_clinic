import {App} from "/common/js/app.js";
import {FormHandler} from "/common/js/form.js";

export const ClinicInfo = (function () {
    const module = {
        startTimeSelector: $('#start-time-picker'),
        endTimeSelector: $('#end-time-picker'),
        selectTimeRangeSelector: $('#select-time-range'),
        timeRangeOutputSelector: $('#time-range-output'),
        addTimeButtonSelector: $('#btn-add-time'),
        clearTimeButtonSelector: $('#btn-clean-time'),

        initTimeSelectConfig: {
            display: {
                components: {
                    calendar: false,
                    date: false,
                    hours: true,
                    minutes: true,
                    seconds: false
                }
            },
            localization: {
                format: 'HH:mm'
            }
        }
    };

    module.init = () => {
        initTimeSelect();
    }

    const initTimeSelect = () => {
        module.startTimePicker = new tempusDominus.TempusDominus(module.startTimeSelector[0], module.initTimeSelectConfig);
        module.endTimePicker = new tempusDominus.TempusDominus(module.endTimeSelector[0], module.initTimeSelectConfig);
    }

    return module;
})();

(function () {
    ClinicInfo.init();
})();
