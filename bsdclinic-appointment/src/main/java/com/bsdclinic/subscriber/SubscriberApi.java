package com.bsdclinic.subscriber;

import com.bsdclinic.RoleAuthorization;
import com.bsdclinic.response.DatatableResponse;
import com.bsdclinic.url.WebUrl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class SubscriberApi {
    private final SubscriberService subscriberService;

    @RoleAuthorization.AuthenticatedUser
    @PostMapping(WebUrl.API_ADMIN_SUBSCRIBER_LIST)
    public DatatableResponse getSubscribersByFilter(@RequestBody SubscriberFilter subscriberFilter) {
        return subscriberService.getSubscribersByFilter(subscriberFilter);
    }

    @RoleAuthorization.AuthenticatedUser
    @GetMapping(WebUrl.API_ADMIN_SUBSCRIBER_DETAIL)
    public SubscriberDto getSubscribersId(@PathVariable String subscriberId) {
        return subscriberService.getSubscriberById(subscriberId);
    }
}
