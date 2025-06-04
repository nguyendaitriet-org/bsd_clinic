import {Subscriber} from "/admin/custom/js/appointment/subscriber.js";
import {DatatableAttribute} from "/common/js/app.js";
import {App} from "/common/js/app.js";

export const AppointmentList = (function () {
    const module = {
        appointmentListModalSelector: $('#appointment-list-modal'),
        appointmentsForCreateTableSelector: $('#appointments-for-create-table'),
        confirmSelectAppointmentButton: $('.btn-confirm-select-appointment')
    };

    module.init = () => {
        handleShowAppointmentListButton();
        handleSelfFillingButton();
        handleConfirmSelectAppointmentButton();
    }

    const handleShowAppointmentListButton = () => {
        Subscriber.subscriberListTableSelector.on('click', '.appointment-list-btn', function () {
            module.appointmentListModalSelector.modal('show');
            module.confirmSelectAppointmentButton.prop('disabled', true);
            module.subscriberId = $(this).data('subscriber-id');
            initAppointmentTableForCreating(module.subscriberId);
        });
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
            initComplete: () => {
                toggleConfirmSelectAppointmentButtonState();
            },
            columns: [
                {data: null},
                {data: null},
                {data: 'patientName'},
                {data: 'patientGender'},
                {data: 'patientPhone'},
                {data: 'patientAddress'},
                {data: 'registerDate'},
                {data: 'actionStatus'}
            ],
            serverSide: true,
            bJQueryUI: true,
            destroy: true,
            paging: true,
            search: {
                boundary: true
            },
            searchDelay: 1000,
            lengthChange: true,
            info: false,
            ordering: false,
            pagingType: 'simple_numbers',
            columnDefs: [
                {
                    targets: [0, 1, 3, 4, 6, 7],
                    className: "text-center"
                },
                {
                    targets: 0,
                    render: () => `<label><input type="radio" class="option-input radio" name="example"/></label>`
                },
                {
                    targets: 3,
                    render: (data, type, row) => genderMap[data]
                },
                {
                    targets: 7,
                    render: (data, type, row) => {
                        const statusTitle = appointmentStatusMap[data];
                        return `<span class="action-status-badge action-status-${data}">${statusTitle}</span>`
                    }
                },
            ],
            language: DatatableAttribute.language
        });

        DatatableAttribute.renderOrdinalColumn(appointmentsDatatable, 1);
    }

    const handleSelfFillingButton = () => {
        Subscriber.subscriberListTableSelector.on('click', '.self-filling-btn', function () {
            const subscriberData = Subscriber.subscriberListTableSelector.DataTable().row($(this).closest('tr')).data();
            const subscriberId = subscriberData.subscriberId;
            const subscriberPhone = subscriberData.subscriberPhone;
            $.ajax({
                headers: {
                    "accept": "application/json",
                    "content-type": "application/json"
                },
                type: 'POST',
                url: API_ADMIN_APPOINTMENT_LIST,
                data: JSON.stringify({
                    subscriberId,
                    patientPhone: subscriberPhone
                }),
            })
                .done((appointmentResponse) => {
                    const appointmentDataForCreation = {
                        ...subscriberData,
                        ...appointmentResponse.data[0]
                    }
                    renderAppointmentDataToCreateForm(appointmentDataForCreation);
                })
                .fail((jqXHR) => {
                    App.handleResponseMessageByStatusCode(jqXHR);
                })
        });
    }

    const renderAppointmentDataToCreateForm = (appointmentDataForCreation) => {
        // TODO: Add logic
        console.log('appointmentData', appointmentDataForCreation);
    }

    const handleConfirmSelectAppointmentButton = () => {
        module.confirmSelectAppointmentButton.on('click', function () {
            const currentSubscriberData = Subscriber.subscriberListTableSelector.DataTable()
                .row($(`button[data-subscriber-id="${module.subscriberId}"]`)
                    .closest('tr')).data();
            const radioInputs = module.appointmentsForCreateTableSelector.find('input[type="radio"]')
            const checkedInput = radioInputs.filter(':checked');
            if (checkedInput.length > 0) {
                const currentAppointmentData = module.appointmentsForCreateTableSelector.DataTable()
                    .row(checkedInput.closest('tr')).data();
                const appointmentDataForCreation = {
                    ...currentSubscriberData,
                    ...currentAppointmentData
                }
                renderAppointmentDataToCreateForm(appointmentDataForCreation);
            }
        })
    }

    const toggleConfirmSelectAppointmentButtonState = () => {
        module.appointmentsForCreateTableSelector
            .find('input[type="radio"]')
            .on('change', function () {
                const isAnyChecked = $(this).filter(':checked').length > 0;
                if (isAnyChecked) {
                    module.confirmSelectAppointmentButton.prop('disabled', false)
                } else {
                    module.confirmSelectAppointmentButton.prop('disabled', true)
                }
            });
    }

    return module;
})();

(function () {
    AppointmentList.init();
})();