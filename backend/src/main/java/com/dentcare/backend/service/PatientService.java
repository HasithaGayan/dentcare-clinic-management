package com.dentcare.backend.service;

import com.dentcare.backend.entity.Patient;
import com.dentcare.backend.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientService {

    private final PatientRepository patientRepository;


    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }


    // GET ALL
    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }


    // GET BY ID
    public Patient getPatientById(Long id) {

        return patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
    }


    // CREATE
    public Patient createPatient(Patient patient) {

        return patientRepository.save(patient);
    }


    // UPDATE
    public Patient updatePatient(Long id, Patient updatedPatient) {

        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found"));


        patient.setName(updatedPatient.getName());
        patient.setEmail(updatedPatient.getEmail());
        patient.setPhone(updatedPatient.getPhone());


        return patientRepository.save(patient);
    }


    // DELETE
    public void deletePatient(Long id) {

        patientRepository.deleteById(id);
    }
}