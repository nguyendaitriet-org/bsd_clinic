$(document).ready(function () {
    $('#open-change-password').on('click', function () {
        $('#change-password-modal').show();
    });

    $('#cancel-password-btn').on('click', function () {
        $('#change-password-form')[0].reset();
        $('#save-password-btn').prop('disabled', true);
        $('#change-password-modal').hide();
    });

    $('#change-password-form input').on('input', function () {
        const filled = $('#old-password').val().trim() &&
            $('#new-password').val().trim() &&
            $('#confirm-password').val().trim();
        $('#save-password-btn').prop('disabled', !filled);
    });

    $('#change-password-form').on('submit', function (e) {
        e.preventDefault();

        const oldPass = $('#old-password').val().trim();
        const newPass = $('#new-password').val().trim();
        const confirmPass = $('#confirm-password').val().trim();

        if (newPass !== confirmPass) {
            alert("Mật khẩu mới không khớp!");
            return;
        }

        const data = {
            oldPassword: oldPass,
            newPassword: newPass
        };

        $.ajax({
            url: '/api/admin/user/change-password', // Thay URL thực tế
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(data),
            success: function () {
                alert('Đổi mật khẩu thành công!');
                $('#cancel-password-btn').click();
            },
            error: function (jqXHR) {
                alert('Lỗi đổi mật khẩu!');
            }
        });
    });
});