import {DatatableAttribute} from "/common/js/app.js";
import {DateTimeConverter} from "/common/js/datetime_util.js";
import {DateTimePattern} from "/common/js/constant.js";
import {AppointmentListForDoctor} from "/admin/custom/js/appointment/appointment-list-doctor.js";

export const MedicalRecordList = (function () {
    const module = {
        searchInputSelector: $('#search-input'),
        doctorInputSelector: $('#doctor-select'),
        createdAtInputSelector: $('#created-at-input'),
        medicalRecordListTableSelector: $('#medical-record-list-table'),

        submitFilterButtonSelector: $('#submit-btn'),
        cancelFilterButtonSelector: $('#cancel-btn'),
    }

    module.init = () => {
        initDateRangePicker();
        renderMedicalRecordsTable();
        handleSubmitFilterButton();
        handleCancelFilterButton();
    }

    const initDateRangePicker = () => {
        module.createdAtPicker = new Lightpick({
            field: module.createdAtInputSelector[0],
            singleDate: false,
            lang: 'vi'
        });
    }

    const getMedicalRecordFilter = () => {
        return ({
            keyword: module.searchInputSelector.val().trim(),
            doctorIds: module.doctorInputSelector.val(),
            createdAtFrom: DateTimeConverter.convertMomentToDateString(module.createdAtPicker.getStartDate(), DateTimePattern.API_DATE_FORMAT),
            createdAtTo: DateTimeConverter.convertMomentToDateString(module.createdAtPicker.getEndDate(), DateTimePattern.API_DATE_FORMAT),
        });
    }

    const handleCancelFilterButton = () => {
        module.cancelFilterButtonSelector.on('click', function () {
            module.searchInputSelector.val('');
            module.doctorInputSelector.selectpicker('deselectAll');
            module.createdAtPicker.setDateRange(null, null)
            renderMedicalRecordsTable();
        });
    }

    const handleSubmitFilterButton = () => {
        module.submitFilterButtonSelector.on('click', function () {
            renderMedicalRecordsTable();
        });
    }

    const renderMedicalRecordsTable = () => {
        const medicalRecordFilter = getMedicalRecordFilter();
        const medicalRecordsDatatable = module.medicalRecordListTableSelector.DataTable({
            ajax: {
                contentType: 'application/json',
                type: 'POST',
                url: API_ADMIN_MEDICAL_RECORD_LIST,
                data: function (d) {
                    return JSON.stringify({...d, ...medicalRecordFilter});
                }
            },
            initComplete: () => handleAppointmentDetailButton(),
            columns: [
                {data: null},
                {data: 'patientName'},
                {data: 'patientPhone'},
                {data: 'createdAt'},
                {data: 'doctorName'},
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
                    targets: [0, 2, 3, 4, 5],
                    className: "text-center"
                },
                {
                    targets: 3,
                    createdCell: (td, cellData) => {
                        const formattedDate = DateTimeConverter.convertToDisplayPattern(cellData)
                        $(td).text(formattedDate);
                    }
                },
                {
                    targets: -1,
                    createdCell: (td) => {
                        const detailButton = `<button class="btn-see-record btn btn-sm btn-outline-dark">${seeRecordTitle}</button>`;
                        $(td).html(detailButton);
                    }
                },
            ],
            language: DatatableAttribute.language
        });

        DatatableAttribute.renderOrdinalColumn(medicalRecordsDatatable, 0);
    }

    const handleAppointmentDetailButton = () => {
        module.medicalRecordListTableSelector.on('click', '.btn-see-record', function () {
            let currentMedicalRecordData = module.medicalRecordListTableSelector.DataTable()
                .row($(this).closest('tr')).data();
            AppointmentListForDoctor.redirectToMedicalRecordDetail(
                currentMedicalRecordData.medicalRecordId,
                currentMedicalRecordData.appointmentId
            )
        });
    }

    return module;
})();
