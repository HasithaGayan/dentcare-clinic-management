
# DentCare Prescription ER Diagram

## Entity Relationship Diagram

```mermaid
erDiagram
    PATIENT ||--o{ PRESCRIPTION : has
    PRESCRIPTION ||--o{ PRESCRIPTION_ITEM : contains
    USER o|--o{ PRESCRIPTION : creates
    USER o|--o{ PRESCRIPTION : assigned_dentist
    PRESCRIPTION o|--o{ PRESCRIPTION : replaces

    PATIENT {
        bigint id PK
        string name
        string email
        string phone
        string allergy_summary
    }

    USER {
        bigint id PK
        string full_name
        string username UK
        string password_hash
        string role
        boolean is_active
    }

    PRESCRIPTION {
        bigint id PK
        bigint patient_id FK
        bigint created_by_user_id FK
        bigint dentist_id FK
        bigint replaces_prescription_id FK
        bigint examination_id
        string medicine_name
        string dosage
        string instructions
        string status
        datetime created_at
        datetime updated_at
        datetime finalized_at
        datetime cancelled_at
        string cancellation_reason
    }

    PRESCRIPTION_ITEM {
        bigint id PK
        bigint prescription_id FK
        string medicine_name
        string strength
        string dosage
        string frequency
        string duration
        int quantity
        string instructions
    }
```

## Database Information

Database name: dentcare

Tables:
1. patients
2. users
3. prescriptions
4. prescription_items

## Entity Relationships

1. One patient can have multiple prescriptions.
2. One prescription can contain multiple medicine items.
3. A prescription can reference the user who created it.
4. A prescription can reference its responsible dentist.
5. A replacement prescription can reference an earlier prescription.

## Important Notes

- The Patient relationship is protected by a foreign key.
- Medicine items are linked using prescription_id.
- Creator and dentist references are currently optional.
- User roles will be checked by backend authorization.
- Prescription history must be preserved.
- The examination_id column is retained for future integration.
- Legacy medicine_name and dosage fields remain in prescriptions.
- Password hashes must never be stored as plaintext passwords.

## Implementation Status

- Database tables: Created
- Patient-Prescription relationship: Verified
- PrescriptionItem relationship: Verified
- User relationships: Verified
- Prescription timestamps: Verified
- Complete role-based authorization: Pending
- Finalization and cancellation services: Pending
