
# DentCare — Day 2 Database Documentation

## Project Information

Project: DentCare Clinic Management System

Database: dentcare

Backend: Java 21 + Spring Boot 4.1.1

Database Server: MySQL 8.4

## 1. Database Tables

### patients

Stores patient information.

Main fields:
- id (Primary Key)
- name
- email
- phone
- allergy_summary

### users

Stores future staff and patient login identities.

Main fields:
- id (Primary Key)
- full_name
- username (Unique)
- password_hash
- role
- is_active

Supported roles:
- ADMIN
- RECEPTIONIST
- DENTIST
- ASSISTANT
- PATIENT

Authentication and authorization are pending.

### prescriptions

Stores prescription information and history.

Main fields:
- id (Primary Key)
- patient_id (Foreign Key)
- created_by_user_id (Foreign Key)
- dentist_id (Foreign Key)
- replaces_prescription_id (Foreign Key)
- examination_id (Future integration reference)
- medicine_name (Legacy field)
- dosage (Legacy field)
- instructions
- status
- created_at
- updated_at
- finalized_at
- cancelled_at
- cancellation_reason

### prescription_items

Stores medicines belonging to prescriptions.

Main fields:
- id (Primary Key)
- prescription_id (Foreign Key)
- medicine_name
- strength
- dosage
- frequency
- duration
- quantity
- instructions

## 2. Relationships

- Patient 1 -> Many Prescriptions
- Prescription 1 -> Many PrescriptionItems
- User 1 -> Many created Prescriptions
- User 1 -> Many assigned Prescriptions
- Prescription -> Optional previous Prescription

## 3. Prescription Statuses

DRAFT
FINALIZED
CANCELLED

A new prescription starts as DRAFT.

Full status-transition business rules and
authorization are not yet implemented.

## 4. Verified Functionality

- MySQL connection works.
- Patient-Prescription foreign key works.
- Prescription-PrescriptionItem foreign key works.
- User reference foreign keys work.
- Prescription creation through REST API works.
- Multiple medicine items can be saved.
- Prescription GET API works.
- Creation and update timestamps are retrieved.
- Eight Java domain tests passed.
- Two MySQL persistence tests passed.
- Draft item removal preserves the patient
  in the integration test.
- Prescription status values persist in MySQL.

## 5. Test Data

All test data must be fictional.

Test prescription:
- Prescription ID: 1
- Patient ID: 1
- Status: DRAFT
- Two fictional medicine items

Synthetic allergy patient:
- Name: TEST_ONLY_ALLERGY_PATIENT
- Email: dentcare-allergy-test@example.invalid
- Allergy summary: Simulated penicillin allergy

These records must never be treated as
real clinical information.

## 6. Database Change History

The development schema was modified through
MySQL Workbench.

Changes include:
- Added patient_id and its foreign key.
- Added prescription status.
- Created prescription_items.
- Added prescription timestamps.
- Created users and user foreign keys.
- Added patient allergy_summary.
- Added prescription finalization history.
- Added cancellation history.
- Added replacement prescription reference.

These are documentation notes, not
an executable or repeatable migration script.

## 7. Backup

Backup filename:
dentcare_backup_20261016_latest.sql

Location:
D:/Projects/dentcare_backup_20261016_latest.sql

Initial verification:
- mysqldump exit code: 0
- Four table definitions found
- Allergy field found
- Finalization history field found

Restore testing: Pending

## 8. Remaining Work

- Real login and role-based access control
- Authorized dentist identity assignment
- Secure prescription finalization
- Controlled cancellation
- Allergy warning implementation
- Complete backend validation
- Database migration scripts
- Additional security and integration tests

## 9. Security Notes

Never store plaintext user passwords.

Never commit database passwords to GitHub.

Do not use real patient information before
authentication and access controls are secure.
