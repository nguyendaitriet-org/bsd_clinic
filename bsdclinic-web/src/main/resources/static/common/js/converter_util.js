export const CurrencyConverter = (function () {
    const module = {};

    module.formatCurrencyVND = (amount) => {
        return amount.toLocaleString('vi-VN') + ' ₫';
    }

    module.formatCurrencyVndWithoutSuffix = (amount) => {
        return amount.toLocaleString('vi-VN');
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
    module.getNumericValue = (inputValue) => {
        let raw = inputValue.replace(/\./g, '');
        return parseInt(raw, 10) || null;
    }

    return module;
})();

export const NumberConverter = (function () {
    const module = {};

    // Converts bytes to MB if >= 1 MB, otherwise to KB
    module.formatBytes = (bytes) => {
        const mb = bytes / (1024 * 1024);

        if (mb >= 1) {
            return `${Math.round(mb)} MB`;
        }

        const kb = bytes / 1024;
        return `${Math.round(kb)} KB`;
    }

    return module;
})();