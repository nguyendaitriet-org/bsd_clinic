package com.bsdclinic.dto.request;

import com.bsdclinic.validation.GroupOrder;
import com.bsdclinic.validation.RuleAnnotation;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserByAdminRequest {
    @NotBlank(message = "{validation.required.user_id}", groups = GroupOrder.First.class)
    @RuleAnnotation.NotExistedUserId(message = "{validation.no_exist.user_id}", groups = GroupOrder.Second.class)
    private String userId;

    @NotBlank(message = "{validation.required.role}", groups = GroupOrder.First.class)
    @RuleAnnotation.NotExistedRole(message = "{validation.no_exist.role}", groups = GroupOrder.Second.class)
    private String roleId;

    @NotBlank(message = "{validation.required.user_status}", groups = GroupOrder.First.class)
    @RuleAnnotation.NotExistedUserStatus(message = "{validation.invalid.user_status}", groups = GroupOrder.Second.class)
    private String status;
}
