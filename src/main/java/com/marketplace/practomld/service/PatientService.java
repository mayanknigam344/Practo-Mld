package com.marketplace.practomld.service;


import com.marketplace.practomld.database.PatientRepository;
import com.marketplace.practomld.model.Patient;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;

    public void registerPatient(Patient patient) {
        patientRepository.registerPatient(patient);
    }
}
