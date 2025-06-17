export const CurrencyConverter = (function () {
    const module = {};

    module.formatCurrencyVND = (amount) => {
        return new Intl.NumberFormat('vi-VN').format(amount) + ' đ';
    }

    return module;
})();