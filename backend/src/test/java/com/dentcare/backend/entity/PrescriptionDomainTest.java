
package com.dentcare.backend.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PrescriptionDomainTest {

    @Test
    void newPrescriptionStartsAsDraft() {
        Prescription prescription = new Prescription();

        assertEquals(
                PrescriptionStatus.DRAFT,
                prescription.getStatus()
        );
    }

    @Test
    void draftPrescriptionAcceptsMultipleItems() {
        Prescription prescription = new Prescription();

        PrescriptionItem item1 = new PrescriptionItem();
        item1.setMedicineName("TEST_MEDICINE_A");

        PrescriptionItem item2 = new PrescriptionItem();
        item2.setMedicineName("TEST_MEDICINE_B");

        prescription.addItem(item1);
        prescription.addItem(item2);

        assertEquals(2, prescription.getItems().size());
        assertSame(prescription, item1.getPrescription());
        assertSame(prescription, item2.getPrescription());
    }

    @Test
    void cannotAddNullMedicineItem() {
        Prescription prescription = new Prescription();

        assertThrows(
                IllegalArgumentException.class,
                () -> prescription.addItem(null)
        );
    }

    @Test
    void cannotAddItemsToFinalizedPrescription() {
        Prescription prescription = new Prescription();
        prescription.setStatus(PrescriptionStatus.FINALIZED);

        PrescriptionItem item = new PrescriptionItem();

        assertThrows(
                IllegalStateException.class,
                () -> prescription.addItem(item)
        );
    }

    @Test
    void cannotAddItemsToCancelledPrescription() {
        Prescription prescription = new Prescription();
        prescription.setStatus(PrescriptionStatus.CANCELLED);

        PrescriptionItem item = new PrescriptionItem();

        assertThrows(
                IllegalStateException.class,
                () -> prescription.addItem(item)
        );
    }

    @Test
    void removingDraftItemPreservesPatient() {
        Patient patient = new Patient();
        patient.setName("Test Patient");

        Prescription prescription = new Prescription();
        prescription.setPatient(patient);

        PrescriptionItem item1 = new PrescriptionItem();
        item1.setMedicineName("TEST_MEDICINE_A");

        PrescriptionItem item2 = new PrescriptionItem();
        item2.setMedicineName("TEST_MEDICINE_B");

        prescription.addItem(item1);
        prescription.addItem(item2);

        boolean removed = prescription.removeItem(item1);

        assertTrue(removed);
        assertEquals(1, prescription.getItems().size());
        assertNull(item1.getPrescription());
        assertSame(prescription, item2.getPrescription());
        assertSame(patient, prescription.getPatient());
    }

    @Test
    void cannotRemoveItemsFromFinalizedPrescription() {
        Prescription prescription = new Prescription();

        PrescriptionItem item = new PrescriptionItem();
        prescription.addItem(item);

        prescription.setStatus(PrescriptionStatus.FINALIZED);

        assertThrows(
                IllegalStateException.class,
                () -> prescription.removeItem(item)
        );
    }

    @Test
    void cannotRemoveItemsFromCancelledPrescription() {
        Prescription prescription = new Prescription();

        PrescriptionItem item = new PrescriptionItem();
        prescription.addItem(item);

        prescription.setStatus(PrescriptionStatus.CANCELLED);

        assertThrows(
                IllegalStateException.class,
                () -> prescription.removeItem(item)
        );
    }
}
