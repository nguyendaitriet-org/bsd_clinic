package com.bsdclinic;

import com.bsdclinic.dto.request.CreateInvoiceRequest;
import com.bsdclinic.dto.response.CreateInvoiceResponse;
import com.bsdclinic.exception_handler.exception.NotFoundException;
import com.bsdclinic.invoice.Invoice;
import com.bsdclinic.invoice.PurchasedService;
import com.bsdclinic.medical_record.MedicalRecord;
import com.bsdclinic.message.MessageProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {
    private final InvoiceRepository invoiceRepository;
    private final MedicalRecordRepository medicalRecordRepository;
    private final MessageProvider messageProvider;
    private final InvoiceMapper invoiceMapper;
    private final MedicalServiceMapper medicalServiceMapper;
    private final ServiceMedicalService serviceMedicalService;

    @Override
    public CreateInvoiceResponse createInvoice(CreateInvoiceRequest request) {
        String medicalRecordId = request.getMedicalRecordId();
        MedicalRecord medicalRecord = medicalRecordRepository.findByMedicalRecordId(medicalRecordId);
        if (medicalRecord == null) {
            throw new NotFoundException(messageProvider.getMessage("validation.no_exist.medical_record"));
        }
        Invoice invoice = invoiceRepository.findByMedicalRecordId(medicalRecordId);

        if (invoice == null) {
            invoice = invoiceMapper.toEntity(request);

            List<PurchasedService> purchasedServices = medicalServiceMapper
                    .toPurchasedServiceList(serviceMedicalService.getMedicalServicesByMedicalRecordId(medicalRecordId));
            invoice.setPurchasedServices(purchasedServices);

            BigDecimal servicesTotalPrice = purchasedServices.stream()
                    .map(PurchasedService::getPrice)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            invoice.setServicesTotalPrice(servicesTotalPrice);

            BigDecimal grandTotalPrice = request.getMedicinesTotalPrice().add(servicesTotalPrice);
            invoice.setGrandTotalPrice(grandTotalPrice);

            BigDecimal advance = medicalRecord.getAdvance();
            invoice.setAdvance(advance);

            BigDecimal remainingPrice = grandTotalPrice.subtract(advance);
            invoice.setRemainingPrice(remainingPrice);

            invoiceRepository.save(invoice);
        }

        return invoiceMapper.toDto(invoice);
    }
}
