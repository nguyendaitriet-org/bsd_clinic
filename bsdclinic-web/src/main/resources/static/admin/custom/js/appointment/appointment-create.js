import {App} from "/common/js/app.js";
import {FormHandler} from "/common/js/form.js";

const AppointmentCreation = (function () {
    const module = {
        subscriberNameSelector: $('#subscriber-name'),
        subscriberPhoneSelector: $('#subscriber-phone'),
        subscriberEmailSelector: $('#subscriber-email'),
        patientNameSelector: $('#patient-name'),
        patientPhoneSelector: $('#patient-phone'),
        patientEmailSelector: $('#patient-email'),
        relationWithSubscriberSelector: $('#relation-with-subscriber'),
        patientBirthdayInputSelector: $('#patient-birthday'),
        selfRegisterSelector: $('#self-register'),

    };

    module.init = () => {
        initDatePicker();
        module.toggleSelfRegisterCheckbox();
    }

    const initDatePicker = () => {
        module.patientBirthdayPicker = new Lightpick({
            field: module.patientBirthdayInputSelector[0],
            lang: 'vi'
        });
    }

    module.toggleSelfRegisterCheckbox = () => {
        module.selfRegisterSelector.on('click', function () {
            if (this.checked) {
                module.patientNameSelector.val(module.subscriberNameSelector.val().trim());
                module.patientPhoneSelector.val(module.subscriberPhoneSelector.val().trim());
                module.patientEmailSelector.val(module.subscriberEmailSelector.val().trim());
                module.relationWithSubscriberSelector.val(selfRegister);
            } else {
                module.patientNameSelector.val('');
                module.patientPhoneSelector.val('');
                module.patientEmailSelector.val('');
                module.relationWithSubscriberSelector.val('');
            }
        })
    }

    return module;
})();

(function () {
    AppointmentCreation.init();
})();

export default AppointmentCreation;
