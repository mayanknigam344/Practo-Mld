package com.marketplace.practomld.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Booking {
    int bookingId;
    Doctor doctor;
    Patient patient;
    TimeSlot timeSlot;
    boolean waitList;

    public Booking(int bookingId, Doctor doctor, Patient patient, TimeSlot timeSlot) {
        this.bookingId = bookingId;
        this.doctor = doctor;
        this.patient = patient;
        this.timeSlot = timeSlot;
        this.waitList = false;
    }
}
