package com.bsdclinic.appointment;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.bsdclinic.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDate;

@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "appointments")
public class Appointment extends BaseEntity {
    @Id
    @Column(name = "appointment_id")
    private String appointmentId;

    @Column(name = "subscriber_id")
    private String subscriberId;

    @Column(name = "patient_name")
    private String patientName;

    @Column(name = "patient_phone")
    private String patientPhone;

    @Column(name = "patient_email")
    private String patientEmail;

    @Column(name = "patient_gender")
    private String patientGender;

    @Column(name = "patient_birthday")
    private LocalDate patientBirthday;

    @Column(name = "patient_address")
    private String patientAddress;

    @Column(name = "visit_reason")
    private String visitReason;

    @Column(name = "relation_with_subscriber")
    private String relationWithSubscriber;

    @Column(name = "doctor_id")
    private String doctorId;

    @Column(name = "action_status")
    private String actionStatus;

    @PrePersist
    public void prePersist() {
        if (appointmentId == null) {
            appointmentId = NanoIdUtils.randomNanoId();
        }
    }
}
