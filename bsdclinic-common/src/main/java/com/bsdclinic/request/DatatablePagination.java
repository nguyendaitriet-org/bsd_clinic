package com.bsdclinic.request;

import lombok.*;

@Getter
@Setter
public class DatatablePagination {
    private Integer draw;              // current page
    private Integer start;             // start from
    private Integer length;            // number of records each page
}