package com.bsdclinic.specification;

import com.bsdclinic.request.DatatablePagination;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class EntityFilter extends DatatablePagination {
    private String keyword;
    private List<String> categoryIds;
}
