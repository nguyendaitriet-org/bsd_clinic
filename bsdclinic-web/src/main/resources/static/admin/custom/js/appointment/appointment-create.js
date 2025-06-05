import {App} from "/common/js/app.js";
import {FormHandler} from "/common/js/form.js";
import {DateTimeConverter} from "/common/js/datetime_util.js";
import {DateTimePattern} from "/common/js/constant.js";

export const AppointmentCreation = (function () {
    const module = {
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
        doctorSelector: $('select[name="doctor"]'),

        appointmentCreateSelector: $('#create-appointment'),
        selfRegisterSelector: $('#self-register'),
        submitBtn: $('#submit-btn')
    };

    module.init = () => {
        initDatePicker();
        handleSelfRegisterCheckbox();
        handleSubmitButton();
    };

    const initDatePicker = () => {
        module.patientBirthdayPicker = new Lightpick({
            field: module.patientBirthdaySelector[0],
            lang: 'vi'
        });
    };

    const handleSelfRegisterCheckbox = () => {
        module.selfRegisterSelector.on('click', function () {
            if (this.checked) {
                module.patientNameSelector.val(module.subscriberNameSelector.val().trim());
                module.patientPhoneSelector.val(module.subscriberPhoneSelector.val().trim());
                module.patientEmailSelector.val(module.subscriberEmailSelector.val().trim());
                module.relationWithSubscriberSelector.val('Bản thân');
            } else {
                module.patientNameSelector.val('');
                module.patientPhoneSelector.val('');
                module.patientEmailSelector.val('');
                module.relationWithSubscriberSelector.val('');
            }
        });
    };

    const handleSubmitButton = () => {
        module.submitBtn.on('click', function () {
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
                App.showSuccessMessage(createSuccess);
            })
            .fail((jqXHR) => {
                App.handleResponseMessageByStatusCode(jqXHR);
                FormHandler.handleServerValidationError(module.appointmentCreateSelector, jqXHR);
            });
    };

    return module;
})();

(function () {
    AppointmentCreation.init();
})();

export default AppointmentCreation;
