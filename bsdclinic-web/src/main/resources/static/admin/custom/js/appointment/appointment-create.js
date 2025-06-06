import {App} from "/common/js/app.js";
import {FormHandler} from "/common/js/form.js";
import {DateTimeConverter} from "/common/js/datetime_util.js";
import {DateTimePattern} from "/common/js/constant.js";

export const AppointmentCreation = (function () {
    const module = {
        appointmentCreationAreaSelector: $('.appointment-creation-area'),

        subscriberNameSelector: $('#subscriber-name'),
        subscriberPhoneSelector: $('#subscriber-phone'),
        subscriberEmailSelector: $('#subscriber-email'),

        patientNameSelector: $('#patient-name'),
        patientPhoneSelector: $('#patient-phone'),
        patientEmailSelector: $('#patient-email'),
        patientGenderSelector: $('input[name="patientGender"]'),
        patientBirthdaySelector: $('#patient-birthday'),
        patientAddressSelector: $('#patient-address'),
        relationWithSubscriberSelector: $('#relation-with-subscriber'),
        visitReasonSelector: $('#visit-reason'),
        doctorSelector: $('select[name="doctorId"]'),

        appointmentCreateSelector: $('#create-appointment'),
        selfRegisterSelector: $('#self-register'),
        submitButtonSelector: $('#submit-btn'),
        cancelButtonSelector: $('#cancel-btn')
    };

    module.init = () => {
        initDatePicker();
        module.toggleSelfRegisterCheckbox();
        handleSubmitButton();
        handleCancelButton();
    };

    const initDatePicker = () => {
        module.patientBirthdayPicker = new Lightpick({
            field: module.patientBirthdaySelector[0],
            lang: 'vi'
        });
    };

    module.toggleSelfRegisterCheckbox = () => {
        module.selfRegisterSelector.on('change', function () {
            if (this.checked) {
                module.fillPatientInfoBySubscriber();
            } else {
                module.patientNameSelector.val('');
                module.patientPhoneSelector.val('');
                module.patientEmailSelector.val('');
                module.relationWithSubscriberSelector.val('');
            }
        });
    };

    module.fillPatientInfoBySubscriber = () => {
        module.patientNameSelector.val(module.subscriberNameSelector.val().trim());
        module.patientPhoneSelector.val(module.subscriberPhoneSelector.val().trim());
        module.patientEmailSelector.val(module.subscriberEmailSelector.val().trim());
        module.relationWithSubscriberSelector.val(selfRegister);
    }

    const handleSubmitButton = () => {
        module.submitButtonSelector.on('click', function () {
            const appointmentData = getAppointmentData();

            createAppointment(appointmentData);
        });
    };

    const getAppointmentData = () => {
        return {
            subscriberName: module.subscriberNameSelector.val().trim(),
            subscriberPhone: module.subscriberPhoneSelector.val().trim(),
            subscriberEmail: module.subscriberEmailSelector.val().trim(),

            patientName: module.patientNameSelector.val().trim(),
            patientPhone: module.patientPhoneSelector.val().trim(),
            patientEmail: module.patientEmailSelector.val().trim(),
            patientGender: module.patientGenderSelector.filter(':checked').val(),
            patientBirthday: DateTimeConverter.convertMomentToDateString(module.patientBirthdayPicker.getDate(), DateTimePattern.API_DATE_FORMAT),
            patientAddress: module.patientAddressSelector.val().trim(),

            relationWithSubscriber: module.relationWithSubscriberSelector.val().trim(),
            visitReason: module.visitReasonSelector.val().trim(),
            doctorId: module.doctorSelector.val()
        };
    };

    const createAppointment = (appointmentData) => {
        $.ajax({
            headers: {
                "accept": "application/json",
                "content-type": "application/json"
            },
            type: 'POST',
            url: API_ADMIN_APPOINTMENT,
            data: JSON.stringify(appointmentData)
        })
            .done(() => {
                App.showSweetAlert('success', operationSuccess, '');
                setTimeout(() => window.location.href = ADMIN_APPOINTMENT_INDEX, 700);
            })
            .fail((jqXHR) => {
                App.handleResponseMessageByStatusCode(jqXHR);
                FormHandler.handleServerValidationError(module.appointmentCreateSelector, jqXHR);
            });
    };

    const handleCancelButton = () => {
        module.cancelButtonSelector.on('click', function () {
            FormHandler.clearAllInputs(module.appointmentCreationAreaSelector);
            module.patientBirthdayPicker.setDate(null);
        })
    }

    return module;
})();

export default AppointmentCreation;
