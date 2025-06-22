package com.bsdclinic;

import com.bsdclinic.dto.request.CreateMedicineRequest;
import com.bsdclinic.url.WebUrl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MedicineApi {
    private final MedicineService medicineService;

    @RoleAuthorization.AdminAuthorization
    @PostMapping(WebUrl.API_ADMIN_MEDICINE)
    public void createMedicine(@RequestBody @Valid CreateMedicineRequest request) {
        medicineService.createMedicine(request);
    }
}
