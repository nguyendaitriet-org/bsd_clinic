export const CurrencyConverter = (function () {
    const module = {};

    module.formatCurrencyVND = (amount) => {
        return amount.toLocaleString('vi-VN') + ' ₫';
    }

    // Format price input with thousands separators
    module.setupPriceFormatter = (inputSelector) => {
        inputSelector.on('input', function () {
            let value = $(this).val().replace(/\D/g, '');
            value = value.replace(/\B(?=(\d{3})+(?!\d))/g, '.');
            $(this).val(value);
        });
    }

    // Get numeric value from formatted input
    module.getNumericValue = (inputSelector) => {
        let raw = inputSelector.val().replace(/\./g, '');
        return parseInt(raw, 10) || 0;
    }

    return module;
})();