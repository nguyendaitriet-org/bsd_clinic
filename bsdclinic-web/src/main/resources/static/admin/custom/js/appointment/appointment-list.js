import {App} from "/common/js/app.js";
import {FormHandler} from "/common/js/form.js";
import {Subscriber} from "/admin/custom/js/appointment/subscriber.js";

export const AppointmentList = (function () {
    const module = {
        showAppointmentListButtonSelector: $('.appointment-list-btn'),
        appointmentListModalSelector: $('#appointment-list-modal'),

    };

    module.init = () => {
        handleShowAppointmentListButton()
    }

    const handleShowAppointmentListButton = () => {
        Subscriber.subscriberListTableSelector.on('click', '.appointment-list-btn', function () {
            module.appointmentListModalSelector.modal('show');
        })
    }

    return module;
})();

(function () {
    AppointmentList.init();
})();