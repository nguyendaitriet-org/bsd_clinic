package com.bsdclinic.subscriber;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SubscriberRepository extends JpaRepository<Subscriber, String>, JpaSpecificationExecutor<Subscriber> {
    Subscriber findByPhone(String phone);
    boolean existsBySubscriberId(String subscriberId);
}
