package com.bsdclinic;

import com.bsdclinic.dto.request.CreateInvoiceRequest;
import com.bsdclinic.dto.response.InvoiceResponse;

public interface InvoiceService {
    InvoiceResponse createInvoice(CreateInvoiceRequest request);
    InvoiceResponse getInvoice(String invoiceId);
    void deleteInvoice(String invoiceId);
}
