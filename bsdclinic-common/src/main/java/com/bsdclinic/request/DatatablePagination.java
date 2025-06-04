package com.bsdclinic.request;

import lombok.*;

@Getter
@Setter
public class DatatablePagination {
    private Integer draw = 1;              // current page
    private Integer start = 0;             // start from
    private Integer length = 1;            // number of records each page
}