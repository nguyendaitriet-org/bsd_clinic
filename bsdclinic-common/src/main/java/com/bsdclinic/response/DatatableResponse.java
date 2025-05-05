package com.bsdclinic.response;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class DatatableResponse<T> {
    /*
        Set the default values like below to return a response object:
            {"data":[],"draw":1,"recordsFiltered":0,"recordsTotal":0}
        to datatables when the fetched list is empty
        so that we can avoid error from datatables
    */
    private Integer start;
    private Integer draw = 1;
    private Long recordsTotal = 0L;
    private Long recordsFiltered = 0L;
    private List<T> data = new ArrayList<>();
}