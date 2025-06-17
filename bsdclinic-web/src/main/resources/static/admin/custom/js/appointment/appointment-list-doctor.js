import {MedicalRecordCreation} from "/admin/custom/js/medical_record/script.js";
import {DatatableAttribute} from "/common/js/app.js";
import {App} from "/common/js/app.js";
import {DateTimeConverter} from "/common/js/datetime_util.js";
import {DateTimePattern} from "/common/js/constant.js";

export const AppointmentListForDoctor = (function () {
    const module = {
        searchInputSelector: $('#search-input'),
        statusInputSelector: $('#status-select'),
        registerDateInputSelector: $('#register-date-input'),
        appointmentListTableSelector: $('#appointment-list-table'),

        submitFilterButtonSelector: $('#submit-btn'),
        cancelFilterButtonSelector: $('#cancel-btn'),
        refreshTableButtonSelector: $('#refresh-btn'),
    }

    module.init = () => {
        initDateRangePicker();
        renderAppointmentsTable();
        handleSubmitFilterButton();
        handleRefreshTableButton();
        handleCancelFilterButton();
    }

    const initDateRangePicker = () => {
        module.registerDatePicker = new Lightpick({
            field: module.registerDateInputSelector[0],
            singleDate: false,
            lang: 'vi'
        });
    }

    const getAppointmentFilter = () => {
        return ({
            keyword: module.searchInputSelector.val().trim(),
            actionStatus: module.statusInputSelector.val(),
            registerDateFrom: DateTimeConverter.convertMomentToDateString(module.registerDatePicker.getStartDate(), DateTimePattern.API_DATE_FORMAT),
            registerDateTo: DateTimeConverter.convertMomentToDateString(module.registerDatePicker.getEndDate(), DateTimePattern.API_DATE_FORMAT),
        })
    }

    const handleCancelFilterButton = () => {
        module.cancelFilterButtonSelector.on('click', function () {
            module.searchInputSelector.val('');
            module.statusInputSelector.selectpicker('deselectAll');
            module.registerDatePicker.setDateRange(null, null)
            renderAppointmentsTable();
        })
    }

    const handleSubmitFilterButton = () => {
        module.submitFilterButtonSelector.on('click', function () {
            renderAppointmentsTable();
        })
    }

    const handleRefreshTableButton = () => {
        module.refreshTableButtonSelector.on('click', function () {
            renderAppointmentsTable();
        });
    }

    const renderAppointmentsTable = () => {
        const appointmentFilter = getAppointmentFilter();
        const appointmentsDatatable = module.appointmentListTableSelector.DataTable({
            ajax: {
                contentType: 'application/json',
                type: 'POST',
                url: API_ADMIN_APPOINTMENT_FOR_DOCTOR,
                data: function (d) {
                    return JSON.stringify({...d, ...appointmentFilter});
                }
            },
            initComplete: () => {
                handleCreateMedicalRecordButton();
                handleSeeMedicalRecordButton();
            },
            columns: [
                {data: null},
                {data: 'patientName'},
                {data: 'patientEmail'},
                {data: 'patientPhone'},
                {data: 'registerDate'},
                {data: 'actionStatus'},
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
                    targets: [0, 3, 4, 5, 6],
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
                    targets: -1,
                    createdCell: (td, cellData, rowData) => {
                        const status = rowData.actionStatus;
                        let actionButton;
                        if (status === appointmentStatusConstant.CHECKED_IN) {
                            actionButton = `<button class="btn-create-record btn btn-sm btn-outline-success">${createRecordTitle}</button>`;
                        } else {
                            actionButton = `<button class="btn-see-record btn btn-sm btn-outline-secondary">${seeRecordTitle}</button>`;
                        }
                        $(td).html(actionButton);
                    }
                },
            ],
            language: DatatableAttribute.language
        });

        DatatableAttribute.renderOrdinalColumn(appointmentsDatatable, 0);
    }

    const handleCreateMedicalRecordButton = () => {
        module.appointmentListTableSelector.on('click', '.btn-create-record', function () {
            App.showSweetAlertConfirmation('warning', confirmApplyTitle, '').then((result) => {
                if (result.isConfirmed) {
                    const rowData = module.appointmentListTableSelector.DataTable().row($(this).closest('tr')).data();
                    const appointmentId = rowData.appointmentId;
                    MedicalRecordCreation.createMedicalRecord(appointmentId).then((response) => {
                        App.showSweetAlert('success', createSuccess);
                        setTimeout(() => redirectToMedicalRecordDetail(response.medicalRecordId, appointmentId), 1000);
                    });
                }
            });
        });
    }

    const handleSeeMedicalRecordButton = () => {
        module.appointmentListTableSelector.on('click', '.btn-see-record', function () {
            const rowData = module.appointmentListTableSelector.DataTable().row($(this).closest('tr')).data();
            redirectToMedicalRecordDetail(rowData.medicalRecordId, rowData.appointmentId);
        });
    }

    const redirectToMedicalRecordDetail = (medicalRecordId, appointmentId) => {
        location.href = ADMIN_MEDICAL_RECORD_DETAIL
                .replace('{medicalRecordId}', medicalRecordId)
                .replace('{appointmentId}', appointmentId);
    }

    return module;
})();