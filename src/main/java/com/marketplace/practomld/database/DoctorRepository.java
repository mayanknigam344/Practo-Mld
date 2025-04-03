package com.marketplace.practomld.database;

import com.marketplace.practomld.exception.DoctorAlreadyPresentException;
import com.marketplace.practomld.exception.DoctorNotFoundException;
import com.marketplace.practomld.exception.NoSpecializationFoundException;
import com.marketplace.practomld.exception.SlotNotFoundException;
import com.marketplace.practomld.model.AvailableDoctor;
import com.marketplace.practomld.model.Doctor;
import com.marketplace.practomld.model.Specialization;
import com.marketplace.practomld.model.TimeSlot;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class DoctorRepository {
    HashMap<Integer, Doctor> doctors = new HashMap<>();
    HashMap<Specialization, List<Doctor>> doctorsForSpecialization = new HashMap<>();

    public void registerDoctor(Doctor doctor) {
        if(doctors.containsKey(doctor.getDoctorId())) {
            throw new DoctorAlreadyPresentException();
        }
        doctors.put(doctor.getDoctorId(), doctor);

        if(!doctorsForSpecialization.containsKey(doctor.getSpecialization())) {
            doctorsForSpecialization.put(doctor.getSpecialization(), new ArrayList<>());
        }
        doctorsForSpecialization.get(doctor.getSpecialization()).add(doctor);

        log.info("Registered Doctor: {}", doctor.getDoctorId());
    }

    public void addAvailability(int doctorId, TimeSlot timeSlot) {
        if(!doctors.containsKey(doctorId)) {
            throw new DoctorNotFoundException();
        }
        Doctor doctor = doctors.get(doctorId);
        HashMap<TimeSlot,Boolean> slots = doctor.getSlots();
        slots.put(timeSlot,true);
        doctor.setSlots(slots);
        doctors.put(doctorId, doctor);
    }

    public List<Doctor> getDoctorsBySpecialization(Specialization specialization) {
        if (!doctorsForSpecialization.containsKey(specialization)) {
            throw new NoSpecializationFoundException();
        }
        return doctorsForSpecialization.get(specialization);
    }

    public List<AvailableDoctor> getAvailableTimeSlotsForAllDoctorsForSpecialization(List<Doctor> specializedDoctors) throws CloneNotSupportedException {
        List<AvailableDoctor> doctorsWithAvailableSlots = new ArrayList<>();
        for(Doctor doctor : specializedDoctors) {
            AvailableDoctor availableDoctor = new AvailableDoctor();
            availableDoctor.setDoctor(doctor.clone());
            List<TimeSlot> atimeSlots = new ArrayList<>();
            HashMap<TimeSlot,Boolean> timeSlots = doctor.getSlots();

            for(Map.Entry<TimeSlot,Boolean> entry : timeSlots.entrySet()){
                if(entry.getValue()) {
                    atimeSlots.add(entry.getKey());
                }
            }
            availableDoctor.setSlotList(atimeSlots);
            doctorsWithAvailableSlots.add(availableDoctor);
        }
        return doctorsWithAvailableSlots;
    }

    public boolean isDoctorRegistered(Doctor doctor){
       return doctors.containsKey(doctor.getDoctorId());
    }

    public Doctor getDoctorDetails(int doctorId) {
        return doctors.get(doctorId);
    }

    public void freeSlot(int doctorId, TimeSlot timeSlot) {
        Boolean put = doctors.get(doctorId).getSlots().put(timeSlot,true);
        if(put == null){
            doctors.get(doctorId).getSlots().remove(timeSlot);
            throw new SlotNotFoundException("Slot not Found");
        }
    }
}
