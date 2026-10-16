
package com.dentcare.backend.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "prescriptions")
public class Prescription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Existing prescription fields
    private String medicineName;

    private String dosage;

    private String instructions;

    // Prescription status
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private PrescriptionStatus status = PrescriptionStatus.DRAFT;

    // Database-managed timestamps
    @Column(
            name = "created_at",
            nullable = false,
            insertable = false,
            updatable = false
    )
    private LocalDateTime createdAt;

    @Column(
            name = "updated_at",
            nullable = false,
            insertable = false,
            updatable = false
    )
    private LocalDateTime updatedAt;

    // Prescription history
    @Column(name = "finalized_at")
    private LocalDateTime finalizedAt;

    @Column(name = "cancelled_at")
    private LocalDateTime cancelledAt;

    @Column(name = "cancellation_reason", length = 500)
    private String cancellationReason;

    // Patient relationship
    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    // Creator relationship
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_user_id")
    private User createdBy;

    // Dentist relationship
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dentist_id")
    private User dentist;

    // Replacement relationship
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "replaces_prescription_id")
    private Prescription replacesPrescription;

    // Multiple medicine items
    @OneToMany(
            mappedBy = "prescription",
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            },
            orphanRemoval = true
    )
    private List<PrescriptionItem> items = new ArrayList<>();

    public Prescription() {
    }

    public Long getId() {
        return id;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public PrescriptionStatus getStatus() {
        return status;
    }

    public void setStatus(PrescriptionStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public LocalDateTime getFinalizedAt() {
        return finalizedAt;
    }

    public void setFinalizedAt(LocalDateTime finalizedAt) {
        this.finalizedAt = finalizedAt;
    }

    public LocalDateTime getCancelledAt() {
        return cancelledAt;
    }

    public void setCancelledAt(LocalDateTime cancelledAt) {
        this.cancelledAt = cancelledAt;
    }

    public String getCancellationReason() {
        return cancellationReason;
    }

    public void setCancellationReason(String cancellationReason) {
        this.cancellationReason = cancellationReason;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public User getDentist() {
        return dentist;
    }

    public void setDentist(User dentist) {
        this.dentist = dentist;
    }

    public Prescription getReplacesPrescription() {
        return replacesPrescription;
    }

    public void setReplacesPrescription(
            Prescription replacesPrescription) {
        this.replacesPrescription = replacesPrescription;
    }

    public List<PrescriptionItem> getItems() {
        return items;
    }

    public void setItems(List<PrescriptionItem> items) {
        this.items = items;
    }

    // Add medicine item to DRAFT prescription
    public void addItem(PrescriptionItem item) {

        if (status != PrescriptionStatus.DRAFT) {
            throw new IllegalStateException(
                    "Only DRAFT prescriptions can be edited"
            );
        }

        if (item == null) {
            throw new IllegalArgumentException(
                    "Medicine item cannot be null"
            );
        }

        if (!items.contains(item)) {
            items.add(item);
        }

        item.setPrescription(this);
    }

    // Remove medicine item from DRAFT prescription
    public boolean removeItem(PrescriptionItem item) {

        if (status != PrescriptionStatus.DRAFT) {
            throw new IllegalStateException(
                    "Only DRAFT prescriptions can be edited"
            );
        }

        if (item == null) {
            throw new IllegalArgumentException(
                    "Medicine item cannot be null"
            );
        }

        boolean removed = items.remove(item);

        if (removed) {
            item.setPrescription(null);
        }

        return removed;
    }
}
