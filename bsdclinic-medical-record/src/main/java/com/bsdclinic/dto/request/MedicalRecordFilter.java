package com.bsdclinic.dto.request;

import com.bsdclinic.request.DatatablePagination;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class MedicalRecordFilter extends DatatablePagination {
    private String keyword;
    private String createdAtFrom;
    private LocalDate createdAtFromDate;
    private LocalDateTime createdAtFromDateTime;
    private String createdAtTo;
    private LocalDate createdAtToDate;
    private LocalDateTime createdAtToDateTime;
    private List<String> doctorIds;
}
