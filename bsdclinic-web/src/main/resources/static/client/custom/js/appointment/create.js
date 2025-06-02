import {App} from "/common/js/app.js";
import {FormHandler} from "/common/js/form.js";
import AdminAppointmentCreation from "/admin/custom/js/appointment/appointment-create.js";
import {DateTimePattern} from "/common/js/constant.js";
import {DateTimeConverter} from "/common/js/datetime_util.js";

export const AppointmentCreation = (function () {
    const module = {
        appointmentRegisterFormSelector: $('#appointment-register-form'),
        patientBirthdaySelector: $('#patient-birthday'),
        registerDateSelector: $('#register-date'),
        registerTimeSelector: $('#register-time')
    };

    module.init = () => {
        disableEnterSubmit();
        initDatePicker();
        toggleSelectMutedClass();
        toggleSelectMutedClass();
        AdminAppointmentCreation.toggleSelfRegisterCheckbox();
        handleFormSubmission();
    }

    const disableEnterSubmit = () => {
        module.appointmentRegisterFormSelector.on('keydown', function (e) {
            if (e.key === 'Enter') {
                e.preventDefault();
            }
        });
    }

    const initDatePicker = () => {
        /* Get clinic day-offs then disable them on date picker */
        getClinicDayOffs().then((clinicInfo) => {
            const formattedDayOffs = clinicInfo.dayOffs.map(dayOff => DateTimeConverter.convertToDisplayPattern(dayOff));
            module.registerDatePicker = new Lightpick({
                field: module.registerDateSelector[0],
                lang: 'vi',
                minDate: moment(),
                startDate: new Date,
                disableDates: formattedDayOffs,
                onSelect: handleRegisterDateChange,
            });
            handleRegisterDateChange(moment());
        })

        module.patientBirthdayPicker = new Lightpick({
            field: module.patientBirthdaySelector[0],
            lang: 'vi',
            startDate: new Date(1900, 1, 1),
        });
    }

    const getClinicDayOffs = () => {
        return $.ajax({
            type: 'GET',
            url: API_CLIENT_CLINIC_INFO,
        })
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

    const handleFormSubmission = () => {
        module.appointmentRegisterFormSelector.on('submit', function (e) {
            e.preventDefault();
            App.showSweetAlertConfirmation('warning', confirmApplyTitle, '').then((result) => {
                if (result.isConfirmed) {
                    const formData = new FormData(this);
                    const appointmentCreationParams = Object.fromEntries(
                        Array.from(formData.entries()).map(([key, value]) => [key, value === '' ? null : value])
                    );
                    appointmentCreationParams.registerDate = module.registerDatePicker.getDate().format(DateTimePattern.API_DATE_FORMAT);
                    appointmentCreationParams.patientBirthday = module.patientBirthdayPicker.getDate().format(DateTimePattern.API_DATE_FORMAT);

                    $.ajax({
                        headers: {
                            "accept": "application/json",
                            "content-type": "application/json"
                        },
                        type: 'POST',
                        url: API_CLIENT_APPOINTMENT,
                        data: JSON.stringify(appointmentCreationParams),
                    })
                        .done(() => {
                            App.showSweetAlert('success', operationSuccess, '');
                            setTimeout(() => window.location.href = CLIENT_HOME, 1000);
                        })
                        .fail((jqXHR) => {
                            App.handleResponseMessageByStatusCode(jqXHR);
                            FormHandler.handleServerValidationError(module.appointmentRegisterFormSelector, jqXHR)
                        })
                }
            });
        })
    }

    return module;
})();

(function () {
    AppointmentCreation.init();
})();

