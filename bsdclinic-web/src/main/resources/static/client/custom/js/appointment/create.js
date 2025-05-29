import {App} from "/common/js/app.js";
import {FormHandler} from "/common/js/form.js";
import AdminAppointmentCreation from "/admin/custom/js/appointment/appointment-create.js";
import {DateTimePattern} from "/common/js/constant.js";

export const AppointmentCreation = (function () {
    const module = {
        patientBirthdaySelector: $('#patient-birthday'),
        registerDateSelector: $('#register-date'),
        registerTimeSelector: $('#register-time')
    };

    module.init = () => {
        initDatePicker();
        toggleSelectMutedClass();
        toggleSelectMutedClass();
        AdminAppointmentCreation.toggleSelfRegisterCheckbox();
    }

    const initDatePicker = () => {
        module.registerDate = new Lightpick({
            field: module.registerDateSelector[0],
            lang: 'vi',
            minDate: moment(),
            onSelect: handleRegisterDateChange
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

    const getAvailableRegisterTime = (registerDate) => {
        return $.ajax({
            url: API_CLIENT_APPOINTMENT_AVAILABLE_SLOTS,
            data: {registerDate}
        })
            .fail((jqXHR) => {
                App.handleResponseMessageByStatusCode(jqXHR);
            })
    }

    const handleRegisterDateChange = (dateMoment) => {
        const formattedDate = dateMoment.format(DateTimePattern.API_DATE_FORMAT);
        getAvailableRegisterTime(formattedDate).then((response) => {
            /* Re-select the first option */
            module.registerTimeSelector.children().first().prop('selected', true);
            module.registerTimeSelector.addClass('text-muted');
            /* Remove all options except the first */
            module.registerTimeSelector.children().slice(1).remove();
            /* Render new options from the response */
            const availableSlots = response.availableSlots;
            const options = availableSlots.map(slot => new Option(slot, slot));
            module.registerTimeSelector.append(options);
        })
    }

    return module;
})();

(function () {
    AppointmentCreation.init();
})();

