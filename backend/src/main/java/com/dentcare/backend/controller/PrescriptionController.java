
package com.dentcare.backend.controller;

import com.dentcare.backend.dto.PrescriptionCreateRequest;
import com.dentcare.backend.dto.PrescriptionResponse;
import com.dentcare.backend.service.PrescriptionService;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prescriptions")
@CrossOrigin
public class PrescriptionController {

    private final PrescriptionService prescriptionService;

    public PrescriptionController(PrescriptionService prescriptionService) {
        this.prescriptionService = prescriptionService;
    }

    // GET ALL - Return safe DTO responses
    @GetMapping
    public List<PrescriptionResponse> getAllPrescriptions() {
        return prescriptionService.getAllPrescriptionResponses();
    }

    // CREATE - Save a prescription with multiple medicines
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PrescriptionResponse createPrescription(
            @RequestBody PrescriptionCreateRequest request) {

        return prescriptionService.createPrescription(request);
    }
}
