package com.bsdclinic.client.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class RegisterDateRequest {
    private LocalDate registerDate;
    private String registerTime;
}
