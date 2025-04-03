package com.marketplace.practomld.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AvailableDoctor {
    Doctor doctor;
    List<TimeSlot> slotList;
}
