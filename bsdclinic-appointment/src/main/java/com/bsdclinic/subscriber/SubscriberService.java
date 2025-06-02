package com.bsdclinic.subscriber;

import com.bsdclinic.response.DatatableResponse;

public interface SubscriberService {
    DatatableResponse getSubscribersByFilter(SubscriberFilter subscriberFilter);
}
