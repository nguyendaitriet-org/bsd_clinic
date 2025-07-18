import {Subscriber} from "/admin/custom/js/appointment/subscriber.js";
import AppointmentCreation from "/admin/custom/js/appointment/appointment-create.js";
import {App, DatatableAttribute, Image} from "/common/js/app.js";
import {FormHandler} from "/common/js/form.js";
import {DateTimeConverter} from "/common/js/datetime_util.js";
import {RequestHeader, DateTimePattern} from "/common/js/constant.js";

export const AppointmentListForCreation = (function () {
    const module = {
        appointmentListModalSelector: $('#appointment-list-modal'),
        appointmentsForCreateTableSelector: $('#appointments-for-create-table'),
        confirmSelectAppointmentButton: $('.btn-confirm-select-appointment'),
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
        AppointmentCreation.subscriberNameSelector.val(appointmentDataForCreation.subscriberName);
        AppointmentCreation.subscriberPhoneSelector.val(appointmentDataForCreation.subscriberPhone);
        AppointmentCreation.subscriberEmailSelector.val(appointmentDataForCreation.subscriberEmail);

        if (appointmentDataForCreation.subscriberPhone === appointmentDataForCreation.patientPhone) {
            AppointmentCreation.selfRegisterSelector.prop('checked', true);
        } else {
            AppointmentCreation.selfRegisterSelector.prop('checked', false);
        }

        /* Use the patient name to determine the subscriber has self-registered yet */
        if (appointmentDataForCreation.patientName) {
            AppointmentCreation.patientNameSelector.val(appointmentDataForCreation.patientName);
            AppointmentCreation.patientPhoneSelector.val(appointmentDataForCreation.patientPhone);
            AppointmentCreation.patientEmailSelector.val(appointmentDataForCreation.patientEmail);
            AppointmentCreation.patientGenderSelector
                .filter(`[value="${appointmentDataForCreation.patientGender}"]`)
                .prop('checked', true);
            AppointmentCreation.patientBirthdayPicker.setDate(DateTimeConverter.convertToDisplayPattern(appointmentDataForCreation.patientBirthday));
            AppointmentCreation.patientAddressSelector.val(appointmentDataForCreation.patientAddress);
            AppointmentCreation.relationWithSubscriberSelector.val(appointmentDataForCreation.relationWithSubscriber);
        } else {
            AppointmentCreation.patientGenderSelector.prop('checked', false);
            AppointmentCreation.patientBirthdayPicker.setDate(null);
            AppointmentCreation.patientAddressSelector.val('');
            AppointmentCreation.relationWithSubscriberSelector.val('');
            AppointmentCreation.fillPatientInfoBySubscriber();
        }
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
                module.appointmentListModalSelector.modal('hide');
                App.showSweetAlert('success', operationSuccess, '');
            }
        });
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

export const AppointmentList = (function () {
    const module = {
        searchInputSelector: $('#search-input'),
        doctorInputSelector: $('#doctor-select'),
        statusAreaSelector: $('#status-select'),
        statusInputSelector: $('input[name="action-status"]'),
        registerDateInputSelector: $('#register-date-input'),
        appointmentListTableSelector: $('#appointment-list-table'),

        submitFilterButtonSelector: $('#submit-btn'),
        cancelFilterButtonSelector: $('#cancel-btn'),
        refreshTableButtonSelector: $('#refresh-btn'),

        appointmentDetailModalSelector: $('#appointment-detail-modal')
    }

    module.init = () => {
        initDateRangePicker();
        renderAppointmentsTable();
        handleSubmitFilterButton();
        handleRefreshTableButton();
        handleCancelFilterButton();
        handleChangeStatusFilter();
    }

    const initDateRangePicker = () => {
        module.registerDatePicker = new Lightpick({
            field: module.registerDateInputSelector[0],
            singleDate: false,
            lang: 'vi'
        });
    }

    const getAppointmentFilter = () => {
        const selectedStatus = module.statusInputSelector.filter(':checked').map(function() {
            return $(this).val();
        }).get();
        return ({
            keyword: module.searchInputSelector.val().trim(),
            doctorIds: module.doctorInputSelector.val(),
            actionStatus: selectedStatus,
            registerDateFrom: DateTimeConverter.convertMomentToDateString(module.registerDatePicker.getStartDate(), DateTimePattern.API_DATE_FORMAT),
            registerDateTo: DateTimeConverter.convertMomentToDateString(module.registerDatePicker.getEndDate(), DateTimePattern.API_DATE_FORMAT),
        });
    }

    const handleCancelFilterButton = () => {
        module.cancelFilterButtonSelector.on('click', function () {
            module.searchInputSelector.val('');
            module.doctorInputSelector.selectpicker('deselectAll');
            module.registerDatePicker.setDateRange(null, null);
            module.statusInputSelector.prop('checked', false);
            renderAppointmentsTable();
        });
    }

    const handleSubmitFilterButton = () => {
        module.submitFilterButtonSelector.on('click', function () {
            renderAppointmentsTable();
        });
    }

    const handleRefreshTableButton = () => {
        module.refreshTableButtonSelector.on('click', function () {
            renderAppointmentsTable();
        });
    }

    const handleChangeStatusFilter = () => {
        module.statusInputSelector.on('change', function () {
            renderAppointmentsTable();
        });
    }

    const renderAppointmentsTable = () => {
        const appointmentFilter = getAppointmentFilter();
        const appointmentsDatatable = module.appointmentListTableSelector.DataTable({
            ajax: {
                contentType: 'application/json',
                type: 'POST',
                url: API_ADMIN_APPOINTMENT_LIST,
                data: function (d) {
                    return JSON.stringify({...d, ...appointmentFilter});
                }
            },
            initComplete: () => handleAppointmentDetailButton(),
            columns: [
                {data: null},
                {data: 'patientName'},
                {data: 'patientEmail'},
                {data: 'patientPhone'},
                {data: 'registerDate'},
                {data: 'actionStatus'},
                {data: 'doctorId'},
                {data: null}
            ],
            serverSide: true,
            bJQueryUI: true,
            destroy: true,
            paging: true,
            searching: false,
            lengthChange: true,
            info: false,
            ordering: false,
            pagingType: 'simple_numbers',
            columnDefs: [
                {
                    targets: [0, 4, 5, 6, 7],
                    className: "text-center"
                },
                {
                    targets: 4,
                    createdCell: (td, cellData) => $(td).html(DateTimeConverter.convertToDisplayPattern(cellData))
                },
                {
                    targets: 5,
                    createdCell: (td, cellData) => {
                        const statusTitle = appointmentStatusMap[cellData];
                        const statusElement = `<span class="action-status-badge action-status-${cellData}">${statusTitle}</span>`;
                        $(td).html(statusElement);
                    }
                },
                {
                    targets: 6,
                    createdCell: (td, cellData) => $(td).html(doctorMap[cellData])
                },
                {
                    targets: -1,
                    createdCell: (td, cellData) => {
                        const detailButton = `<button class="btn-appointment-detail btn btn-sm btn-outline-dark">${detailTitle}</button>`;
                        $(td).html(detailButton);
                    }
                },
            ],
            language: DatatableAttribute.language
        });

        DatatableAttribute.renderOrdinalColumn(appointmentsDatatable, 0);
    }

    const handleAppointmentDetailButton = () => {
        module.appointmentListTableSelector.on('click', '.btn-appointment-detail', function () {
            module.appointmentDetailModalSelector.modal('show');
            let currentAppointmentData = module.appointmentListTableSelector.DataTable()
                .row($(this).closest('tr')).data();
            Subscriber.getSubscriberById(currentAppointmentData.subscriberId).then((response) => {
                currentAppointmentData = Object.assign(currentAppointmentData, response);
                AppointmentDetail.renderAppointmentDetail(currentAppointmentData);
            });
        });
    }

    return module;
})();

export const AppointmentDetail = (function () {
    const module = {
        appointmentDetailModalSelector: $('#appointment-detail-modal'),

        appointmentDetailTextSelector: $('.appointment-detail'),
        doctorSelector: $('#doctor-detail-select'),
        appointmentDetailStatusSelector: $('#appointment-detail-status'),
        appointmentActionStatusSelector: $('#appointment-action-status'),
        saveAppointmentButtonSelector: $('#btn-save-appointment'),
        appointmentIdInputSelector: $('#appointment-id-input'),
        appointmentStatusInputSelector: $('#appointment-status-input'),
        appointmentStatusFlowModalSelector: $('#appointment-status-flow-modal')
    }

    module.init = () => {
        handleSaveAppointmentButton();
        handleAppointmentStatusFlowDisplay();
    }

    const renderNextAppointmentStatus = (appointmentId) => {
        console.log('renderNextAppointmentStatus')
        $.ajax({
            type: 'GET',
            url: API_ADMIN_APPOINTMENT_NEXT_STATUS.replace('{appointmentId}', appointmentId),
        })
            .done((response) => {
                module.appointmentActionStatusSelector.children('option:not(:first)').remove();
                for (const nextStatus of response.nextStatuses) {
                    const option = new Option(appointmentStatusMap[nextStatus], nextStatus);
                    module.appointmentActionStatusSelector.append(option);
                }
            })
            .fail((jqXHR) => {
                App.handleResponseMessageByStatusCode(jqXHR);
            })
    }

    module.renderAppointmentDetail = ({actionStatus, doctorId, appointmentId, ...appointmentData}) => {
        module.appointmentDetailTextSelector.each((index, element) => {
            const attribute = $(element).data('attribute');
            $(element).text(appointmentData[attribute]);
            if (attribute === 'patientGender') {
                $(element).text(genderMap[appointmentData[attribute]]);
            }
            if (attribute === 'patientBirthday') {
                const date = DateTimeConverter.convertToDisplayPattern(appointmentData[attribute]);
                $(element).text(date);
            }
        });

        module.doctorSelector.val(doctorId);

        module.appointmentDetailStatusSelector.html(
            `<span class="action-status-badge action-status-${actionStatus} w-100">${appointmentStatusMap[actionStatus]}</span>`
        );

        module.appointmentActionStatusSelector.find(`option[value='${actionStatus}']`).remove();

        module.appointmentIdInputSelector.val(appointmentId);
        module.appointmentStatusInputSelector.val(actionStatus);

        renderNextAppointmentStatus(appointmentId);
    }

    const handleSaveAppointmentButton = () => {
        module.saveAppointmentButtonSelector.on('click', function () {
            const appointmentUpdateParams = {
                actionStatus: module.appointmentActionStatusSelector.val() || module.appointmentStatusInputSelector.val(),
                doctorId: module.doctorSelector.val()
            }
            const appointmentId = module.appointmentIdInputSelector.val();

            module.updateAppointment(appointmentId, appointmentUpdateParams)
                .then(() => {
                    App.showSweetAlert('success', operationSuccess, '');
                    location.reload();
                })
                .catch((jqXHR) => {
                    App.handleResponseMessageByStatusCode(jqXHR);
                    FormHandler.handleServerValidationError(module.appointmentDetailModalSelector, jqXHR);
                })
        });
    }

    module.updateAppointment = (appointmentId, appointmentUpdateParams) => {
        return $.ajax({
            headers: RequestHeader.JSON_TYPE,
            type: 'PATCH',
            url: API_ADMIN_APPOINTMENT_WITH_ID.replace('{appointmentId}', appointmentId),
            data: JSON.stringify(appointmentUpdateParams),
        })
    }

    const handleAppointmentStatusFlowDisplay = () => {
        const publicImageUrl = Image.getPublicImageApi('examination_register_flow.png', 'appointment');
        const imgSelector = module.appointmentStatusFlowModalSelector.find('img');
        imgSelector.prop('src', publicImageUrl);
    }

    return module;
})();