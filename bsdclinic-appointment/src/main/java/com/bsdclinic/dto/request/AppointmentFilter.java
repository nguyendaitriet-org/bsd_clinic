package com.bsdclinic.dto.request;

import com.bsdclinic.request.DatatablePagination;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class AppointmentFilter extends DatatablePagination {
    private String keyword;
    private List<String> doctorIds;
    private List<String> actionStatus;
    private LocalDate registerDateFrom;
    private LocalDate registerDateTo;
    /* Filter for appointment creation */
    private String subscriberId;
    private SearchParam search;
    private String patientPhone;
    /* Filter for current doctor */
    private String doctorId;
    private boolean isAdminRole;

    @Getter
    @Setter
    public static class SearchParam {
        private String value;
        private boolean regex;
    }
}
