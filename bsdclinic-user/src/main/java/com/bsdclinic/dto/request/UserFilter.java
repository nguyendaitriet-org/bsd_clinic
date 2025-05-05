package com.bsdclinic.dto.request;

import com.bsdclinic.request.DatatablePagination;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class UserFilter extends DatatablePagination {
    private String currentUserId;
    private String keyword;
    private List<String> roleIds;
    private List<String> status;
    private LocalDate createdFrom;
    private LocalDate createdTo;
}