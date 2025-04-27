package com.bsdclinic.dto.request;

import com.bsdclinic.validation.RuleAnnotation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUserRequest {
    @RuleAnnotation.ExistedEmail( message = "Email đã tồn tại")
    @NotBlank(message = "Email không được để trống")
    @Size(max = 255, message = "Chỉ nhập tối đa 255 kí tự")
    @Pattern(regexp = "[\\w.%+-]+@[\\w.-]+\\.[A-Za-z]{2,}$", message = "Vui lòng nhập đúng định dạng email")
    private String email;

    @NotBlank(message = "Họ và tên không được để trống")
    @Size(max = 255, message = "Chỉ nhập tối đa 255 kí tự")
    private String fullName;

    @NotBlank(message = "Số điện thoại không được để trống")
    @Size(max = 20, message = "Chỉ nhập tối đa 20 kí tự")
    private String phone;

    @NotBlank(message = "Mật khẩu không được để trống")
    @Size(min = 6, max = 16, message = "Mật khẩu tối thiểu 6 kí tự và không quá 16 kí tự")
    private String password;

    @NotBlank(message = "Xác nhận mật khẩu không được để trống")
    private String passwordConfirmation;

    @NotBlank(message = "Vai trò không được để trống")
    private String roleId;
}
