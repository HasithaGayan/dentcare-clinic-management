package com.dentcare.backend.dto;

import java.util.List;

public record PrescriptionCreateRequest(
        Long patientId,
        String instructions,
        List<PrescriptionItemRequest> items
) {
}
