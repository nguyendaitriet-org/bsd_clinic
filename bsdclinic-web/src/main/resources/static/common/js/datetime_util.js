export const DateTimeConverter = (function () {
    const module = {};

    module.convertMomentToDateString = (moment, pattern) => {
        return moment ? moment.format(pattern) : null;
    }

    return module;
})();