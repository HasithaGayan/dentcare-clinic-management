
package com.dentcare.backend.service;

import com.dentcare.backend.dto.PrescriptionCreateRequest;
import com.dentcare.backend.dto.PrescriptionItemRequest;
import com.dentcare.backend.dto.PrescriptionItemResponse;
import com.dentcare.backend.dto.PrescriptionResponse;
import com.dentcare.backend.entity.Patient;
import com.dentcare.backend.entity.Prescription;
import com.dentcare.backend.entity.PrescriptionItem;
import com.dentcare.backend.repository.PatientRepository;
import com.dentcare.backend.repository.PrescriptionRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;
    private final PatientRepository patientRepository;

    public PrescriptionService(
            PrescriptionRepository prescriptionRepository,
            PatientRepository patientRepository) {

        this.prescriptionRepository = prescriptionRepository;
        this.patientRepository = patientRepository;
    }

    // Existing method - Get all prescriptions
    public List<Prescription> getAllPrescriptions() {
        return prescriptionRepository.findAll();
    }

    // Existing method - To be removed after final cleanup
    public Prescription createPrescription(Prescription prescription) {
        return prescriptionRepository.save(prescription);
    }

    // Get all prescriptions as safe DTO responses
    @Transactional(readOnly = true)
    public List<PrescriptionResponse> getAllPrescriptionResponses() {

        return prescriptionRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // Create a prescription with multiple medicine items
    @Transactional
    public PrescriptionResponse createPrescription(
            PrescriptionCreateRequest request) {

        // Validate patient ID
        if (request == null || request.patientId() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Patient ID is required"
            );
        }

        // Validate medicine items
        if (request.items() == null || request.items().isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "At least one medicine item is required"
            );
        }

        // Find the existing patient
        Patient patient = patientRepository
                .findById(request.patientId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Patient not found"
                ));

        // Create a new prescription
        Prescription prescription = new Prescription();
        prescription.setPatient(patient);
        prescription.setInstructions(request.instructions());

        // Add medicine items
        for (PrescriptionItemRequest itemRequest : request.items()) {

            if (itemRequest == null ||
                    itemRequest.medicineName() == null ||
                    itemRequest.medicineName().isBlank()) {

                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Medicine name is required"
                );
            }

            if (itemRequest.quantity() != null &&
                    itemRequest.quantity() <= 0) {

                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Medicine quantity must be positive"
                );
            }

            PrescriptionItem item = new PrescriptionItem();

            item.setMedicineName(itemRequest.medicineName());
            item.setStrength(itemRequest.strength());
            item.setDosage(itemRequest.dosage());
            item.setFrequency(itemRequest.frequency());
            item.setDuration(itemRequest.duration());
            item.setQuantity(itemRequest.quantity());
            item.setInstructions(itemRequest.instructions());

            // Link medicine item to its prescription
            prescription.addItem(item);
        }

        // Save prescription and linked medicine items
        Prescription savedPrescription =
                prescriptionRepository.save(prescription);

        return toResponse(savedPrescription);
    }

    // Convert Prescription entity into a safe response DTO
    private PrescriptionResponse toResponse(Prescription prescription) {

        // Convert medicine items
        List<PrescriptionItemResponse> itemResponses =
                prescription.getItems()
                        .stream()
                        .map(item -> new PrescriptionItemResponse(
                                item.getId(),
                                item.getMedicineName(),
                                item.getStrength(),
                                item.getDosage(),
                                item.getFrequency(),
                                item.getDuration(),
                                item.getQuantity(),
                                item.getInstructions()
                        ))
                        .toList();

        // Get linked patient ID
        Long patientId = prescription.getPatient() != null
                ? prescription.getPatient().getId()
                : null;

        // Return prescription including timestamps
        return new PrescriptionResponse(
                prescription.getId(),
                patientId,
                prescription.getMedicineName(),
                prescription.getDosage(),
                prescription.getInstructions(),
                prescription.getStatus(),
                itemResponses,
                prescription.getCreatedAt(),
                prescription.getUpdatedAt()
        );
    }
}
