package com.bsdclinic.subscriber;

import com.bsdclinic.request.DatatablePagination;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubscriberFilter extends DatatablePagination {
    private String keyword;
}
