package com.marketplace.practomld.database;

import com.marketplace.practomld.exception.PatientAlreadyRegisteredException;
import com.marketplace.practomld.model.Patient;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;

@Slf4j
public class PatientRepository {
    HashMap<Integer, Patient> patients = new HashMap<>();

    public void registerPatient(Patient patient){
        if(patients.containsKey(patient.getPatientId())){
            throw new PatientAlreadyRegisteredException();
        }
        patients.put(patient.getPatientId(), patient);
        log.info("Patient registered: {}", patient.getPatientId());
    }

    public boolean isRegisteredPatient(Patient patient){
        return patients.containsKey(patient.getPatientId());
    }
}
