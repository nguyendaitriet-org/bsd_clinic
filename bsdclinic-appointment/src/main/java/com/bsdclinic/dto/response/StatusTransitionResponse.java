package com.bsdclinic.dto.response;

import com.bsdclinic.appointment.ActionStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class StatusTransitionResponse {
    private ActionStatus currentStatus;
    private Set<ActionStatus> nextStatuses;
}