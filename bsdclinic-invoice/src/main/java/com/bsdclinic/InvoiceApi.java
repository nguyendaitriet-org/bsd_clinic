package com.bsdclinic;

import com.bsdclinic.dto.request.CreateInvoiceRequest;
import com.bsdclinic.dto.response.InvoiceResponse;
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
    public InvoiceResponse createInvoice(@RequestBody @Valid CreateInvoiceRequest createInvoiceRequest) {
        return invoiceService.createInvoice(createInvoiceRequest);
    }

    @RoleAuthorization.AdminAndDoctorAuthorization
    @GetMapping(WebUrl.API_ADMIN_INVOICE_WITH_ID)
    public InvoiceResponse getInvoice(@PathVariable String invoiceId) {
        return invoiceService.getInvoice(invoiceId);
    }

    @RoleAuthorization.AdminAndDoctorAuthorization
    @DeleteMapping(WebUrl.API_ADMIN_INVOICE_WITH_ID)
    public void deleteInvoice(@PathVariable String invoiceId) {
        invoiceService.deleteInvoice(invoiceId);
    }
}
