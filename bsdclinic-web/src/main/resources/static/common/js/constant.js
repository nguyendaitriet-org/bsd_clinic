export const DateTimePattern = (function () {
    return {
        API_DATE_FORMAT : "YYYY-MM-DD",
        DISPLAY_DATE_FORMAT : "DD/MM/YYYY",
        HOUR_MINUTE_FORMAT: "HH:mm"
    };
})();

export const RegexPattern = (function () {
    return {
        PATH_VARIABLE_REGEX  : /\\{.+\\}/g,
    };
})();