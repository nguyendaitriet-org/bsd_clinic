package com.bsdclinic.dto.request;

import com.bsdclinic.request.DatatablePagination;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MedicineFilter extends DatatablePagination {
    private String keyword;
}