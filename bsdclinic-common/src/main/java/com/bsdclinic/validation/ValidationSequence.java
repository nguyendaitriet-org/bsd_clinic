package com.bsdclinic.validation;

import jakarta.validation.GroupSequence;

@GroupSequence({
        GroupOrder.First.class,
        GroupOrder.Second.class,
        GroupOrder.Third.class,
        GroupOrder.Fourth.class,
        GroupOrder.Fifth.class
})
public interface ValidationSequence {}