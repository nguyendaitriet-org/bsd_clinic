package com.bsdclinic.subscriber;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.bsdclinic.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "subscribers")
public class Subscriber{
    @Id
    @Column(name = "subscriber_id")
    private String subscriberId;

    @Column(name = "name")
    private String name;

    @Column(name = "phone")
    private String phone;

    @Column(name = "email")
    private String email;

    @PrePersist
    public void prePersist() {
        if (subscriberId == null) {
            subscriberId = NanoIdUtils.randomNanoId();
        }
    }
}
