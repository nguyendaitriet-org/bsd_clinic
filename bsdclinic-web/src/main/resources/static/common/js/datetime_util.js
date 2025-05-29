export const DateTimeConverter = (function () {
    const module = {};

    module.convertMomentToDateString = (moment, pattern) => {
        return moment ? moment.format(pattern) : null;
    }

    /* Convert 'YYYY-MM-DD' to 'DD/MM/YYYY' */
    module.convertToDisplayPattern = (inputDate) => {
        const dayOffDate = new Date(inputDate);
        return new Intl.DateTimeFormat('en-GB').format(dayOffDate);
    }

    return module;
})();