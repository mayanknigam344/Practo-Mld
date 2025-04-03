package com.marketplace.practomld.service;

import com.marketplace.practomld.Utils.LocalTimeUtil;
import com.marketplace.practomld.database.DoctorRepository;
import com.marketplace.practomld.model.AvailableDoctor;
import com.marketplace.practomld.model.Doctor;
import com.marketplace.practomld.model.Specialization;
import com.marketplace.practomld.model.TimeSlot;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@AllArgsConstructor
@Slf4j
public class DoctorService {
    private final DoctorRepository doctorRepository;

    public void registerDoctor(Doctor doctor) {
        doctorRepository.registerDoctor(doctor);
    }

    public void addAvailability(int doctorId, TimeSlot timeSlot) {
        // check for 30 mins slots
        LocalTime start = LocalTimeUtil.convertStringToTime(timeSlot.getStart());
        LocalTime end = LocalTimeUtil.convertStringToTime(timeSlot.getEnd());

        if(ChronoUnit.MINUTES.between(start, end)%60  == 30) {
            doctorRepository.addAvailability(doctorId, timeSlot);
            log.info("Added the availability of doctor {}", doctorId);
        }else{
           log.warn("Its exceeding time limit i.e 30 mins");
           // TODO: throw error in this case
        }
    }

    public void showAvailableSlotsBySpeciality(Specialization specialization) throws CloneNotSupportedException {
        List<Doctor> specializedDoctors = doctorRepository.getDoctorsBySpecialization(specialization);
        List<AvailableDoctor> availableSpecializedDoctors = doctorRepository.getAvailableTimeSlotsForAllDoctorsForSpecialization(specializedDoctors);

        if(!availableSpecializedDoctors.isEmpty()) {
            for(AvailableDoctor availableDoctor : availableSpecializedDoctors) {
                for(TimeSlot slot : availableDoctor.getSlotList()){
                    log.info("Available slots with doctor id");
                    System.out.println(availableDoctor.getDoctor().getDoctorId() +" " +slot.getStart() +":" + slot.getEnd());
                }
            }
        }else{
            log.warn("No available doctors found for specialization {}", specialization);
        }
    }
}
