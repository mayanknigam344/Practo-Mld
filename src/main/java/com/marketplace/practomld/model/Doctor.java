package com.marketplace.practomld.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

@Getter
@Setter
@AllArgsConstructor
public class Doctor implements Cloneable{
    int doctorId;
    String doctorName;
    HashMap<TimeSlot,Boolean> slots;
    Specialization specialization;

    @Override
    public Doctor clone() throws CloneNotSupportedException {
        return (Doctor) super.clone();
    }
}
