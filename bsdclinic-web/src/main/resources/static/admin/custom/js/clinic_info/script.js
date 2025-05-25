import {App} from "/common/js/app.js";
import {FormHandler} from "/common/js/form.js";

export const ClinicInfo = (function () {
    const module = {
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

        startTimeSelector: $('#start-time-picker'),
        endTimeSelector: $('#end-time-picker'),
        selectTimeRangeSelector: $('#select-time-range'),
        timeRangeOutputSelector: $('#time-range-output'),
        addTimeButtonSelector: $('#btn-add-time'),
        clearTimeButtonSelector: $('#btn-clear-time'),
        resetTimeButtonSelector: $('#btn-reset-time'),

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
                format: 'HH:mm'
            }
        },

        workingHours: null,
        workingHoursRequest: null
    };

    module.init = () => {
        initTimeSelect();
        getClinicInfo();
        handleOnChangeDayOption();
        handleEndTimeLimit();
        handleAddTimeButton();
        handleClearTimeButton();
        handleResetTimeButton();
    }

    const initTimeSelect = () => {
        module.startTimePicker = new tempusDominus.TempusDominus(module.startTimeSelector[0], module.initTimeSelectConfig);
        module.endTimePicker = new tempusDominus.TempusDominus(module.endTimeSelector[0], module.initTimeSelectConfig);
    }

    const getClinicInfo = () => {
        $.ajax({
            type: 'GET',
            url: API_CLIENT_CLINIC_INFO,
        })
            .done((clinicInfo) => {
                console.log(clinicInfo)
                renderClinicInfo(clinicInfo);
                module.workingHours = {...clinicInfo.workingHours};
                module.workingHoursRequest = {...clinicInfo.workingHours};
                renderWorkingHours(clinicInfo.workingHours);
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

            if (!startTime || !endTime) return;

            /* Convert picked datetime to pattern 'HH:mm' */
            const formatTime = dateTimeParam => {
                const date = new Date(dateTimeParam);
                return date.toLocaleTimeString([], {
                    hour: '2-digit',
                    minute: '2-digit',
                    hour12: false
                });
            }

            const timeRange =`${formatTime(startTime)}-${formatTime(endTime)}`;
            const oldTimeRangeOutput = module.timeRangeOutputSelector.val();
            const outputResult = oldTimeRangeOutput +
                (oldTimeRangeOutput ? `,${timeRange}` : timeRange);

            module.timeRangeOutputSelector.val(outputResult);
        });
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
        });
    }

    return module;
})();

(function () {
    ClinicInfo.init();
})();
