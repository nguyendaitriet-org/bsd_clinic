package com.bsdclinic.subscriber;

import com.bsdclinic.response.DatatableResponse;

public interface SubscriberService {
    DatatableResponse getSubscribersByFilter(SubscriberFilter subscriberFilter);
    void checkDuplicateSubscriberEmail(String email);
    SubscriberDto getSubscriberById(String id);
}
