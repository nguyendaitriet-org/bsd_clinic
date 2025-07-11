import {FormHandler} from "/common/js/form.js";
import {App} from "/common/js/app.js";

const LogIn = (function () {
    const module = {
        logInButtonSelector: $('#login-button'),
        emailInputSelector: $('#email'),
        passwordInputSelector: $('#password'),
        formLoginSelector: $('#login-form')
    };

    module.init = () => {
        handleLogInSubmitButton();
        App.enableEnterKeyboard(module.formLoginSelector, module.logInButtonSelector);
    }

    const handleLogInSubmitButton = () => {
        module.logInButtonSelector.on('click', function () {
            const loginParam = {
                email: module.emailInputSelector.val().trim(),
                password: module.passwordInputSelector.val().trim(),
            }

            logInByAjax(loginParam);
        })
    }

    const logInByAjax = (loginParam) => {
        $.ajax({
            headers: {
                "accept": "application/json",
                "content-type": "application/json"
            },
            type: 'POST',
            url: API_ADMIN_LOGIN,
            data: JSON.stringify(loginParam),
            beforeSend: function () {
                module.formLoginSelector.addClass("loading");
                module.formLoginSelector.find(".error-text").remove();
                module.formLoginSelector.find("button").prop('disabled', true);
            },
        })
            .done((response) => {
                App.showSuccessMessage(response.message);
                window.location.href = response.redirectUrl;
            })
            .fail((jqXHR) => {
                FormHandler.handleServerValidationError(module.formLoginSelector, jqXHR)
                App.handleResponseMessageByStatusCode(jqXHR);
            })
            .always(() => {
                module.formLoginSelector.find("button").prop('disabled', false);
                module.formLoginSelector.removeClass("loading");
            });
    }

    return module;
})();

(function () {
    LogIn.init();
})();
