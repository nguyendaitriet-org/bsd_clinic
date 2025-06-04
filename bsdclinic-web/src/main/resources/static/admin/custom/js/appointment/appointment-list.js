import {App} from "/common/js/app.js";
import {FormHandler} from "/common/js/form.js";
import {Subscriber} from "/admin/custom/js/appointment/subscriber.js";
import {DatatableAttribute} from "/common/js/app.js";

export const AppointmentList = (function () {
    const module = {
        showAppointmentListButtonSelector: $('.appointment-list-btn'),
        appointmentListModalSelector: $('#appointment-list-modal'),
        appointmentsForCreateTableSelector: $('#appointments-for-create-table'),

    };

    module.init = () => {
        handleShowAppointmentListButton()
    }

    const handleShowAppointmentListButton = () => {
        Subscriber.subscriberListTableSelector.on('click', '.appointment-list-btn', function () {
            module.appointmentListModalSelector.modal('show');
            const subscriberId = $(this).data('subscriber-id');
            initAppointmentTableForCreating(subscriberId)
        })
    }

    const initAppointmentTableForCreating = (subscriberId) => {
        const appointmentsDatatable = module.appointmentsForCreateTableSelector.DataTable({
            ajax: {
                contentType: 'application/json',
                type: 'POST',
                url: API_ADMIN_APPOINTMENT_LIST,
                data: function (d) {
                    d.subscriberId = subscriberId;
                    return JSON.stringify(d);
                }
            },
            columns: [
                {data: null},
                {data: null},
                {data: 'patientName'},
                {data: 'patientGender'},
                {data: 'patientPhone'},
                {data: 'patientAddress'},
                {data: 'registerDate'}
            ],
            serverSide: true,
            bJQueryUI: true,
            destroy: true,
            paging: true,
            search: {
                boundary: true
            },
            lengthChange: true,
            info: false,
            ordering: false,
            pagingType: 'simple_numbers',
            columnDefs: [
                {
                    targets: [0, 1, 3, 4, 6],
                    className: "text-center"
                },
                {
                    targets: 0,
                    render: () => `<label><input type="radio" class="option-input radio" name="example"/></label>`
                },
                {
                    targets: 3,
                    render: (data, type, row) => genderMap[data]
                }
            ],
            language: DatatableAttribute.language
        });

        DatatableAttribute.renderOrdinalColumn(appointmentsDatatable, 1);

    }

    return module;
})();

(function () {
    AppointmentList.init();
})();