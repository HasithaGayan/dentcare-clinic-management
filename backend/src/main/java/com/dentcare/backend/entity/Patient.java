
package com.dentcare.backend.entity;

import jakarta.persistence.*;

import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name = "patients")
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Patient basic information
    private String name;

    private String email;

    private String phone;

    // Patient allergy information
    @Column(name = "allergy_summary", length = 500)
    private String allergySummary;

    // One patient can have many prescriptions
    @OneToMany(mappedBy = "patient")
    private List<Prescription> prescriptions = new ArrayList<>();

    // Empty constructor required by JPA
    public Patient() {
    }

    // Patient ID
    public Long getId() {
        return id;
    }

    // Patient name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Patient email
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Patient phone
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    // Allergy summary
    public String getAllergySummary() {
        return allergySummary;
    }

    public void setAllergySummary(String allergySummary) {
        this.allergySummary = allergySummary;
    }

    // Patient prescriptions
    public List<Prescription> getPrescriptions() {
        return prescriptions;
    }

    public void setPrescriptions(
            List<Prescription> prescriptions) {
        this.prescriptions = prescriptions;
    }
}
