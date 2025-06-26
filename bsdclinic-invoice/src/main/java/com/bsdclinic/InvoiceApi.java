package com.bsdclinic;

import com.bsdclinic.dto.request.CreateInvoiceRequest;
import com.bsdclinic.dto.response.CreateInvoiceResponse;
import com.bsdclinic.url.WebUrl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class InvoiceApi {
    private final InvoiceService invoiceService;

    @RoleAuthorization.AdminAndDoctorAuthorization
    @PostMapping(WebUrl.API_ADMIN_INVOICE)
    public CreateInvoiceResponse createInvoice(@RequestBody @Valid CreateInvoiceRequest createInvoiceRequest) {
        return invoiceService.createInvoice(createInvoiceRequest);
    }
}
