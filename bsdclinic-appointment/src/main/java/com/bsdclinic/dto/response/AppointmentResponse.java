package com.bsdclinic.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class AppointmentResponse {
    private String appointmentId;
    private String patientName;
    private String patientPhone;
    private String patientEmail;
    private String patientGender;
    private LocalDate patientBirthday;
    private String patientAddress;
    private String visitReason;
    private String relationWithSubscriber;
    private String actionStatus;
    private LocalDate registerDate;
    private String registerTime;
    private String doctorId;
    private String subscriberId;
    private String medicalRecordId;
}