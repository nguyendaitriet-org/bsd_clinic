export const FormHandler = (function () {
    const module = {};

    module.handleServerValidationError = (formSelector, jqXHR) => {
        const {errors, message, redirect} = jqXHR.responseJSON;

        if (errors) {
            for (const key in errors) {
                const input = formSelector.find('[name="' + makeInputName(key) + '"]').first();
                const error = makeErrorMessage(errors[key]);
                const group = input.closest('.with-validation');

                if (group.length) {
                    group.append(error);
                    addHidingEventToErrorText(group);
                }
            }
        }
    }

    module.clearAllInputs = (formSelector) => {
        formSelector.find('input')
            .not('[type="hidden"]')
            .val('')
            .prop('checked', false)
            .prop('selected', false);
        formSelector.find('.selectpicker').selectpicker('deselectAll');
        formSelector.find('select').val('');
        formSelector.find('textarea').val('');
    }

    module.clearAllErrors = (formSelector) => {
        formSelector.find('.error-text').remove();
    }

    const addHidingEventToErrorText = (validationGroupSelector) => {
        validationGroupSelector.find('input').on( "focus", function() {
            const errorTextSelector = validationGroupSelector.find('.error-text');
            errorTextSelector.css('visibility', 'hidden');
        });
    }

    /**
     * Make div element for showing error message
     *
     * @param error
     * @returns {HTMLDivElement}
     */
    const makeErrorMessage = (error) => {
        const errorSpan = document.createElement("span");
        errorSpan.classList.add('error-text');
        errorSpan.innerText = Array.isArray(error) ? error.join("\n") : error;

        return errorSpan;
    }

    /**
     * Convert validation attribute name to HTML input name
     *
     * @param validationName string, eg: 'supplier.0.supplier_id'
     * @returns {*}
     */
    const makeInputName = (validationName) => {
        const attributes = validationName.split(".");
        return attributes.map((el, i) => (i ? "[" + el + "]" : el)).join("");
    }

    return module;
})();




