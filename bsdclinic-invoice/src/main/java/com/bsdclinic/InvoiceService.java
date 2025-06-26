package com.bsdclinic;

import com.bsdclinic.dto.request.CreateInvoiceRequest;
import com.bsdclinic.dto.response.CreateInvoiceResponse;

public interface InvoiceService {
    CreateInvoiceResponse createInvoice(CreateInvoiceRequest request);
}
