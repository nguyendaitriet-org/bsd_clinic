export const DateTimePattern = (function () {
    return {
        API_DATE_FORMAT: "YYYY-MM-DD",
        DISPLAY_DATE_FORMAT: "DD/MM/YYYY",
        HOUR_MINUTE_FORMAT: "HH:mm"
    };
})();

export const RegexPattern = (function () {
    return {
        PATH_VARIABLE_REGEX: /\\{.+\\}/g,
    };
})();

export const RequestHeader = (function () {
    return {
        JSON_TYPE: {
            "accept": "application/json",
            "content-type": "application/json"
        }
    };
})();

export const Status = (function () {
    return {
        APPOINTMENT: {
            PENDING: "PENDING",
            REJECTED: "REJECTED",
            ACCEPTED: "ACCEPTED",
            CHECKED_IN: "CHECKED_IN",
            EXAMINING: "EXAMINING",
            ADVANCED: "ADVANCED",
            FINISHED: "FINISHED",
            FINISHED_NO_PAY: "FINISHED_NO_PAY",
            PAID: "PAID",
            UNPAID: "UNPAID"
        }
    };
})();