
package com.dentcare.backend.entity;

import com.dentcare.backend.repository.PatientRepository;
import com.dentcare.backend.repository.PrescriptionItemRepository;
import com.dentcare.backend.repository.PrescriptionRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback
class PrescriptionPersistenceTest {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    @Autowired
    private PrescriptionItemRepository itemRepository;

    @PersistenceContext
    private EntityManager entityManager;

    // Create a fictional patient for testing
    private Patient createTestPatient() {

        Patient patient = new Patient();
        patient.setName("TEST_ONLY_PATIENT");

        return patientRepository.saveAndFlush(patient);
    }

    // Create a DRAFT with two fictional medicine items
    private Prescription createTestPrescription(Patient patient) {

        Prescription prescription = new Prescription();
        prescription.setPatient(patient);
        prescription.setInstructions("TEST ONLY");

        PrescriptionItem first = new PrescriptionItem();
        first.setMedicineName("TEST_MEDICINE_A");

        PrescriptionItem second = new PrescriptionItem();
        second.setMedicineName("TEST_MEDICINE_B");

        prescription.addItem(first);
        prescription.addItem(second);

        return prescriptionRepository.saveAndFlush(prescription);
    }

    @Test
    void prescriptionStatusPersistsInDatabase() {

        Patient patient = createTestPatient();

        Prescription prescription =
                createTestPrescription(patient);

        Long prescriptionId = prescription.getId();

        entityManager.clear();

        // Verify DRAFT status
        Prescription draft = prescriptionRepository
                .findById(prescriptionId)
                .orElseThrow();

        assertEquals(
                PrescriptionStatus.DRAFT,
                draft.getStatus()
        );

        // Verify FINALIZED status persists
        draft.setStatus(PrescriptionStatus.FINALIZED);

        prescriptionRepository.saveAndFlush(draft);
        entityManager.clear();

        Prescription finalized = prescriptionRepository
                .findById(prescriptionId)
                .orElseThrow();

        assertEquals(
                PrescriptionStatus.FINALIZED,
                finalized.getStatus()
        );

        // Verify CANCELLED status persists
        finalized.setStatus(PrescriptionStatus.CANCELLED);

        prescriptionRepository.saveAndFlush(finalized);
        entityManager.clear();

        Prescription cancelled = prescriptionRepository
                .findById(prescriptionId)
                .orElseThrow();

        assertEquals(
                PrescriptionStatus.CANCELLED,
                cancelled.getStatus()
        );
    }

    @Test
    void removingDraftItemPreservesPatientInDatabase() {

        Patient patient = createTestPatient();
        Long patientId = patient.getId();

        Prescription prescription =
                createTestPrescription(patient);

        Long prescriptionId = prescription.getId();

        Long firstItemId =
                prescription.getItems().get(0).getId();

        Long secondItemId =
                prescription.getItems().get(1).getId();

        entityManager.clear();

        // Reload the prescription from MySQL
        Prescription saved = prescriptionRepository
                .findById(prescriptionId)
                .orElseThrow();

        PrescriptionItem firstItem = saved.getItems()
                .stream()
                .filter(item -> firstItemId.equals(item.getId()))
                .findFirst()
                .orElseThrow();

        // Remove the first medicine item
        assertTrue(saved.removeItem(firstItem));

        entityManager.flush();
        entityManager.clear();

        // Removed item must no longer exist
        assertFalse(
                itemRepository.existsById(firstItemId)
        );

        // Second medicine item must remain
        assertTrue(
                itemRepository.existsById(secondItemId)
        );

        // Patient must remain
        assertTrue(
                patientRepository.existsById(patientId)
        );
    }
}
