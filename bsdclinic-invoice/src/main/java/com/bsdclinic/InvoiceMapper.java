package com.bsdclinic;

import com.bsdclinic.dto.request.CreateInvoiceRequest;
import com.bsdclinic.dto.response.CreateInvoiceResponse;
import com.bsdclinic.invoice.Invoice;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface InvoiceMapper {
    Invoice toEntity(CreateInvoiceRequest request);
    CreateInvoiceResponse toDto(Invoice invoice);
}
