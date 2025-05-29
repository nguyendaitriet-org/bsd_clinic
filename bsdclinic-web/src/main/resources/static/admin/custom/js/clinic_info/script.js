import {App} from "/common/js/app.js";
import {FormHandler} from "/common/js/form.js";
import {DateTimePattern} from "/common/js/constant.js";

export const ClinicInfo = (function () {
    const module = {
        clinicInfoForm: $('#clinic-info-form'),

        clinicNameSelector: $('#name'),
        clinicAddressSelector: $('#address'),
        clinicPhoneSelector: $('#phone'),
        clinicEmailSelector: $('#email'),
        clinicWebsiteSelector: $('#website'),
        clinicIntroductionSelector: $('#introduction'),
        clinicSloganSelector: $('#slogan'),
        clinicDescriptionSelector: $('#description'),
        clinicIsActiveSelector: $('#is-active'),
        clinicRegisterTimeRangeSelector: $('#register-time-range'),
        clinicDayOptionSelector: $('#day-option'),

        timeRangeSelectAreaSelector: $('#time-range-select-area'),
        startTimeSelector: $('#start-time-picker'),
        startTimeInputSelector: $('#start-time'),
        endTimeSelector: $('#end-time-picker'),
        endTimeInputSelector: $('#end-time'),
        selectTimeRangeSelector: $('#select-time-range'),
        timeRangeOutputSelector: $('#time-range-output'),
        addTimeButtonSelector: $('#btn-add-time'),
        clearTimeButtonSelector: $('#btn-clear-time'),
        resetTimeButtonSelector: $('#btn-reset-time'),

        dayOffListAreaSelector: $('#day-off-list-area'),
        dayOffInputSelector: $('#day-off'),
        addDayOffButtonSelector: $('#btn-add-day-off'),
        resetDayOffButtonSelector: $('#btn-reset-day-off'),

        initTimeSelectConfig: {
            display: {
                components: {
                    calendar: false,
                    date: false,
                    hours: true,
                    minutes: true,
                    seconds: false
                }
            },
            localization: {
                format: DateTimePattern.HOUR_MINUTE_FORMAT
            }
        },

        workingHours: null,
        workingHoursRequest: null,
        dayOffs: null,
        dayOffsRequest: null
    };

    module.init = () => {
        module.dropdownMenuSelector = module.dayOffListAreaSelector.find('.dropdown-menu');

        getClinicInfo();

        initTimeSelect();
        handleOnChangeDayOption();
        handleEndTimeLimit();
        handleAddTimeButton();
        handleClearTimeButton();
        handleResetTimeButton();

        initDayOffPicker();
        handleAddDayOffButton();
        handleRemoveDayOffButton();
        handleResetDayOffButton();

        handleSubmittingClinicInfoForm();
    }

    /*--- Clinic info rendering ---*/

    const getClinicInfo = () => {
        $.ajax({
            type: 'GET',
            url: API_CLIENT_CLINIC_INFO,
        })
            .done((clinicInfo) => {
                renderClinicInfo(clinicInfo);
                module.clinicInfoId = clinicInfo.clinicInfoId;

                module.workingHours = {...clinicInfo.workingHours};
                module.workingHoursRequest = {...clinicInfo.workingHours};
                renderWorkingHours(module.workingHours);

                // TODO: Mock data
                module.dayOffs = ['2025-04-30', '2025-05-01'];
                module.dayOffsRequest = ['2025-04-30', '2025-05-01', '2025-05-29', '2025-05-30'];
                renderDayOffs(module.dayOffs);
                handleEmptyDayOffDropdown();
            })
            .fail((jqXHR) => {
                App.handleResponseMessageByStatusCode(jqXHR);
            })
    }

    const renderClinicInfo = (clinicInfo) => {
        module.clinicNameSelector.val(clinicInfo.name);
        module.clinicAddressSelector.val(clinicInfo.address);
        module.clinicPhoneSelector.val(clinicInfo.phone);
        module.clinicEmailSelector.val(clinicInfo.email);
        module.clinicWebsiteSelector.val(clinicInfo.website);
        module.clinicIntroductionSelector.val(clinicInfo.introduction);
        module.clinicSloganSelector.val(clinicInfo.slogan);
        module.clinicDescriptionSelector.val(clinicInfo.description);
        module.clinicRegisterTimeRangeSelector.val(clinicInfo.registerTimeRange);
        if (clinicInfo) {
            module.clinicIsActiveSelector.prop('checked', true);
        }
    }

    const renderWorkingHours = (workingHours) => {
        const selectedDay = module.clinicDayOptionSelector.val();
        renderWorkingHoursBySelectedDay(selectedDay, workingHours)
    }

    const renderWorkingHoursBySelectedDay = (day, workingHours) => {
        const workingHoursOfDay = workingHours[day];
        let workingHoursArray = [];
        for (const workingHoursRange of workingHoursOfDay) {
            /* The result return the second format: 19:00:00 so that the second need to be removed */
            workingHoursArray.push(`${workingHoursRange.start.substring(0, 5)}-${workingHoursRange.end.substring(0, 5)}`)
        }
        module.timeRangeOutputSelector.val(workingHoursArray.join(','))
    }

    /*--- Working hours handling ---*/

    const initTimeSelect = () => {
        module.startTimePicker = new tempusDominus.TempusDominus(module.startTimeSelector[0], module.initTimeSelectConfig);
        module.endTimePicker = new tempusDominus.TempusDominus(module.endTimeSelector[0], module.initTimeSelectConfig);
    }

    const handleOnChangeDayOption = () => {
        module.clinicDayOptionSelector.on('change', function () {
            const selectedDayValue = $(this).val();
            renderWorkingHoursBySelectedDay(selectedDayValue, module.workingHoursRequest);
        })
    }

    const handleEndTimeLimit = () => {
        module.startTimeSelector.on('change.td', function () {
            const selectedStartTime = module.startTimePicker.dates.lastPicked;
            if (selectedStartTime) {
                module.endTimePicker.updateOptions({
                    restrictions: {
                        minDate: selectedStartTime
                    }
                });
            }
        });
    }

    const handleAddTimeButton = () => {
        module.addTimeButtonSelector.on('click', function () {
            const startTime = module.startTimePicker.dates.lastPicked;
            const endTime = module.endTimePicker.dates.lastPicked;

            if (!startTime || !endTime) {
                handleEmptyTimeRangeSelect(startTime, endTime);
                return;
            }

            /* Convert picked datetime to pattern 'HH:mm' */
            const formatTime = dateTimeParam => {
                const date = new Date(dateTimeParam);
                return date.toLocaleTimeString([], {
                    hour: '2-digit',
                    minute: '2-digit',
                    hour12: false
                });
            };

            const timeRange = `${formatTime(startTime)}-${formatTime(endTime)}`;
            const oldTimeRangeOutput = module.timeRangeOutputSelector.val();
            const outputResult = oldTimeRangeOutput +
                (oldTimeRangeOutput ? `,${timeRange}` : timeRange);

            module.timeRangeOutputSelector.val(outputResult);

            /* Update the working hours request for updating clinic info API */
            const selectedDay = module.clinicDayOptionSelector.val();
            module.workingHoursRequest[selectedDay] = convertToWorkingHoursRequest(outputResult);
        });
    }

    const convertToWorkingHoursRequest = (workingHoursString) => {
        return workingHoursString.split(',').map(range => {
            const [start, end] = range.split('-');
            return { start, end };
        });
    }

    const handleEmptyTimeRangeSelect = (startTime, endTime) => {
        const errors = {};

        if (!startTime) {
            errors.startTime = requiredStartTime;
        }
        if (!endTime) {
            errors.endTime = requiredEndTime;
        }

        if (Object.keys(errors).length > 0) {
            const errorResponse = {
                responseJSON: {
                    errors
                }
            };
            FormHandler.handleServerValidationError(module.timeRangeSelectAreaSelector, errorResponse);
        }
    }

    const handleClearTimeButton = () => {
        module.clearTimeButtonSelector.on('click', function () {
            module.timeRangeOutputSelector.val('');
        });
    }

    const handleResetTimeButton = () => {
        module.resetTimeButtonSelector.on('click', function () {
            /* Select the first option and get its value */
            const selectedDay = module.clinicDayOptionSelector.prop('selectedIndex', 0).val();
            renderWorkingHoursBySelectedDay(selectedDay, module.workingHours);

            module.workingHoursRequest = {...module.workingHours};

            module.startTimePicker.dates.clear();
            module.startTimeInputSelector.val('');
            module.endTimePicker.dates.clear();
            module.endTimeInputSelector.val('');
        });
    }

    /*--- Day off handling ---*/

    const initDayOffPicker = () => {
        module.dayOffPicker = new Lightpick({
            field: module.dayOffInputSelector[0],
            singleDate: false,
            lang: 'vi',
            minDate: moment()
        });
    }

    const DayOffDropdownItem = (text, value) => `
        <li data-value="${value}">
            <span class="dropdown-item-text">
                <span>${text}</span>
                <span class="btn btn-sm btn-outline-danger border-0 btn-remove-day-off">X</span>
            </span>
        </li>
    `

    const DayOffDropdownEmpty = (text) => `
        <li class="empty-dropdown">
            <span class="dropdown-item-text">
                <span>${text}</span>
            </span>
        </li>
    `

    const renderDayOffs = (dayOffs) => {
        const dropdownItems = dayOffs.map(dayOff => {
            const dayOffDate = new Date(dayOff);
            const displayFormatDate = new Intl.DateTimeFormat('en-GB').format(dayOffDate);
            return DayOffDropdownItem(displayFormatDate, dayOff);
        })

        module.dropdownMenuSelector.prepend(dropdownItems);
    }

    const handleAddDayOffButton = () => {
        module.addDayOffButtonSelector.on('click', function () {
            const startDate = module.dayOffPicker.getStartDate();
            const endDate = module.dayOffPicker.getEndDate();
            if (!startDate || !endDate) {
                // TODO: handle error message
                return;
            }

            const pickedDateRangeList = getDateRangeList(startDate, endDate);
            /* Use Set to merge arrays and remove duplicate values */
            module.dayOffsRequest = [...new Set([...pickedDateRangeList, ...module.dayOffsRequest])];
            module.dayOffsRequest = module.dayOffsRequest.sort((a, b) => new Date(a) - new Date(b))

            if (module.dayOffsRequest.length > 0) {
                module.dropdownMenuSelector.children().remove();
                renderDayOffs(module.dayOffsRequest);
            }

            clearDayOffPick();
            handleEmptyDayOffDropdown();
            blinkBorder(module.dayOffListAreaSelector)
        })
    }

    const blinkBorder = (elementSelector) => {
        void elementSelector[0].offsetWidth;
        elementSelector.addClass('blink-border');
        setTimeout(() => {
            elementSelector.removeClass('blink-border');
        }, 700)
    }

    const clearDayOffPick = () => {
        module.dayOffPicker.setDateRange(null, null);
        module.dayOffInputSelector.val('');
    }

    const getDateRangeList = (startMoment, endMoment) => {
        const dates = [];

        const current = startMoment.clone().startOf('day');
        const last = endMoment.clone().startOf('day');

        while (current.isSameOrBefore(last)) {
            dates.push(current.format(DateTimePattern.API_DATE_FORMAT));
            current.add(1, 'day');
        }

        return dates;
    }

    const handleRemoveDayOffButton = () => {
        module.dayOffListAreaSelector.on('click', '.btn-remove-day-off', function () {
            const parentSpan = $(this).closest('li');
            const removedValue = parentSpan.data('value');
            module.dayOffsRequest = module.dayOffsRequest.filter(dayOff => dayOff !== removedValue);
            parentSpan.remove();
            handleEmptyDayOffDropdown();
        });
    }

    const handleEmptyDayOffDropdown = () => {
        const dayOffItemCount = module.dropdownMenuSelector.children().length;
        if (dayOffItemCount === 0) {
            module.dropdownMenuSelector.append(DayOffDropdownEmpty(emptyDayOffList));
        } else {
            module.dropdownMenuSelector.find('.empty-dropdown').remove();
        }
    }

    const handleResetDayOffButton = () => {
        module.resetDayOffButtonSelector.on('click', function () {
            module.dayOffsRequest = [...module.dayOffs];
            module.dropdownMenuSelector.children().remove();
            renderDayOffs(module.dayOffs);
            clearDayOffPick();
        })
    }

    /*--- Form submission ---*/

    const handleSubmittingClinicInfoForm = () => {
        module.clinicInfoForm.on('submit', function (e) {
            e.preventDefault();
            const formData = new FormData(this);
            const clinicInfoParams = Object.fromEntries(formData);
            clinicInfoParams.workingHours = {...module.workingHoursRequest};
            clinicInfoParams.isActive = module.clinicIsActiveSelector.prop('checked');

            $.ajax({
                headers: {
                    "accept": "application/json",
                    "content-type": "application/json"
                },
                type: 'PUT',
                url: API_ADMIN_UPDATE_CLINIC_INFO.replace('{clinicInfoId}', module.clinicInfoId),
                data: JSON.stringify(clinicInfoParams),
            })
                .done(() => {
                    App.showSweetAlert('success', operationSuccess, '');
                    setTimeout(() => location.reload(), 1000);
                })
                .fail((jqXHR) => {
                    App.handleResponseMessageByStatusCode(jqXHR);
                    FormHandler.handleServerValidationError(module.clinicInfoForm, jqXHR)
                })
        })
    }

    return module;
})();

(function () {
    ClinicInfo.init();
})();
