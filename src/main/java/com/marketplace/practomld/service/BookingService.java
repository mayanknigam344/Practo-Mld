package com.marketplace.practomld.service;


import com.marketplace.practomld.database.DoctorRepository;
import com.marketplace.practomld.database.PatientRepository;
import com.marketplace.practomld.exception.BookingNotFoundException;
import com.marketplace.practomld.exception.DoctorNotRegisteredException;
import com.marketplace.practomld.exception.PatientAlreadyOccupiedException;
import com.marketplace.practomld.exception.PatientNotRegisteredException;
import com.marketplace.practomld.model.Booking;
import com.marketplace.practomld.model.Doctor;
import com.marketplace.practomld.model.Patient;
import com.marketplace.practomld.model.TimeSlot;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@RequiredArgsConstructor
@Slf4j
public class BookingService {

    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;

    HashMap<Integer, Booking> bookings = new HashMap<>();
    HashMap<Integer, List<TimeSlot>> patientsSlots = new HashMap<>();
    Queue<Booking> waitListQueue = new LinkedList<>();

    static int index = 1;

    public void bookAppointment(Patient patient,Doctor doctor, String fromSlot) {
        if(!patientRepository.isRegisteredPatient(patient)){
            throw new PatientNotRegisteredException("Patient is not registered");
        }

        if(!doctorRepository.isDoctorRegistered(doctor)){
            throw new DoctorNotRegisteredException("Doctor is not registered");
        }

        if(patientsSlots.containsKey(patient.getPatientId())){
            for(TimeSlot timeSlot : patientsSlots.get(patient.getPatientId())){
                if(timeSlot.getStart().equals(fromSlot)){
                    throw new PatientAlreadyOccupiedException();
                }
            }
        }else{
            patientsSlots.put(patient.getPatientId(), new ArrayList<>());
        }

        Doctor doctorDetails = doctorRepository.getDoctorDetails(doctor.getDoctorId());
        HashMap<TimeSlot,Boolean> slots =  doctorDetails.getSlots();

        for(Map.Entry<TimeSlot,Boolean> slot : slots.entrySet()){
            if(slot.getKey().getStart().equals(fromSlot) && slot.getValue()){
                slots.put(slot.getKey(),false);
                patientsSlots.get(patient.getPatientId()).add(slot.getKey());
                Booking booking = new Booking(index++,doctor,patient,slot.getKey());
                bookings.put(booking.getBookingId(),booking);
                log.info("Booking created {}",booking.getBookingId());
                return;
            }
        }

        log.info("No available slots");
        Booking booking = new Booking(index++,doctor,patient,new TimeSlot(fromSlot,fromSlot));
        booking.setWaitList(true);
        log.info("Added to the waitlist. Booking id: {}", booking.getBookingId());
        waitListQueue.add(booking);
    }

    public void cancelBooking(int bookingId){
        if(!bookings.containsKey(bookingId)){
            throw new BookingNotFoundException("Booking not found");
        }

        Booking booking = bookings.get(bookingId);
        doctorRepository.freeSlot(booking.getDoctor().getDoctorId(),booking.getTimeSlot());
        bookings.remove(bookingId);
        log.info("Booking cancelled. Booking id: {}", booking.getBookingId());
        checkForFreeSlot(booking);
    }

    private void checkForFreeSlot(Booking booking) {
        for(Booking waitListBooking : waitListQueue) {
            if (waitListBooking.getTimeSlot().getStart().equals(booking.getTimeSlot().getStart())) {
                waitListBooking.getTimeSlot().setEnd(booking.getTimeSlot().getEnd());
                waitListBooking.setWaitList(false);

                Doctor doctorDetails = doctorRepository.getDoctorDetails(waitListBooking.getDoctor().getDoctorId());
                HashMap<TimeSlot, Boolean> slots = doctorDetails.getSlots();
                for (Map.Entry<TimeSlot, Boolean> slot : slots.entrySet()) {
                    if (slot.getKey().getStart().equals(booking.getTimeSlot().getStart())) {
                        slots.put(slot.getKey(), false);
                        break;
                    }
                }
                bookings.put(waitListBooking.getBookingId(), waitListBooking);
                waitListQueue.remove(waitListBooking);
            }
        }
    }

    public void showBookedAppointments(){
        for(Map.Entry<Integer,Booking> bookingEntry : bookings.entrySet()){
            Booking booking = bookingEntry.getValue();
            System.out.println(booking.getBookingId() +" "+booking.getDoctor().getDoctorId() +" "+booking.getDoctor().getDoctorName() +" "+ booking.getTimeSlot().getStart() +" "+booking.getTimeSlot().getEnd());
        }
    }
}
