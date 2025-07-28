package com.bsdclinic.controller;

import com.bsdclinic.RoleAuthorization;
import com.bsdclinic.message.MessageProvider;
import com.bsdclinic.url.WebUrl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class MedicineController {
    private final MessageProvider messageProvider;

    @ModelAttribute("dosageUnits")
    public Map<String, String> getDosageUnits() {
        return messageProvider.getMessageMap("medicine.dosage_unit", "constants");
    }

    @RoleAuthorization.AdminAndDoctorAuthorization
    @GetMapping(WebUrl.ADMIN_MEDICINE_INDEX)
    public String toIndex() {
        return "admin/medicine/index";
    }
}
