package com.dentcare.backend.dto;

public record PrescriptionItemRequest(
        String medicineName,
        String strength,
        String dosage,
        String frequency,
        String duration,
        Integer quantity,
        String instructions
) {
}
