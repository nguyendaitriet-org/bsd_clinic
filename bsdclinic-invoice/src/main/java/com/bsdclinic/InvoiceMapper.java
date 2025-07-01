package com.bsdclinic;

import com.bsdclinic.dto.request.CreateInvoiceRequest;
import com.bsdclinic.dto.response.InvoiceResponse;
import com.bsdclinic.invoice.Invoice;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface InvoiceMapper {
    Invoice toEntity(CreateInvoiceRequest request);
    InvoiceResponse toDto(Invoice invoice);
}
