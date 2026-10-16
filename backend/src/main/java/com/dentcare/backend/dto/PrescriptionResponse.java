
package com.dentcare.backend.dto;

import com.dentcare.backend.entity.PrescriptionStatus;
import java.time.LocalDateTime;
import java.util.List;

public record PrescriptionResponse(
        Long id,
        Long patientId,
        String medicineName,
        String dosage,
        String instructions,
        PrescriptionStatus status,
        List<PrescriptionItemResponse> items,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    // Keep the existing service constructor working
    public PrescriptionResponse(
            Long id,
            Long patientId,
            String medicineName,
            String dosage,
            String instructions,
            PrescriptionStatus status,
            List<PrescriptionItemResponse> items
    ) {
        this(
                id,
                patientId,
                medicineName,
                dosage,
                instructions,
                status,
                items,
                null,
                null
        );
    }
}
