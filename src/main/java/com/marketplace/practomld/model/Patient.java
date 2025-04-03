package com.marketplace.practomld.model;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;

@Getter
@Setter
public class Patient {
    int patientId;
    String patientName;

    public Patient(int patientId, String patientName) {
        this.patientId = patientId;
        this.patientName = patientName;
    }
    HashMap<Doctor, List<TimeSlot>> bookedSlots;
}
