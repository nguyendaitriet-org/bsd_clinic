package com.bsdclinic.subscriber;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriberRepository extends JpaRepository<Subscriber, String> {
    Subscriber findByPhone(String phone);
}
