-- DentCare Clinic Management System
-- Day 2 schema baseline, based on the 2026-10-16 schema-only MySQL 8.4 dump.
--
-- IMPORTANT:
--   * Use this ONLY for a NEW, EMPTY, separately selected test/deployment database.
--   * Do NOT execute this script against the existing `dentcare` database.
--   * This is a snapshot baseline, NOT an upgrade migration for older schemas.
--   * CREATE TABLE IF NOT EXISTS will NOT add missing columns to existing tables.
--   * This script does not create/select a database, drop tables, or insert data.
--   * No password, patient records, or other secrets are stored here.
--
-- Dependencies: patients, users -> prescriptions -> prescription_items.
-- MySQL: 8.4; InnoDB; utf8mb4_0900_ai_ci.

CREATE TABLE IF NOT EXISTS `patients` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(100) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `allergy_summary` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS `users` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `full_name` varchar(255) NOT NULL,
  `username` varchar(100) NOT NULL,
  `password_hash` varchar(255) NOT NULL,
  `role` varchar(30) NOT NULL,
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_users_username` (`username`),
  CONSTRAINT `chk_users_role` CHECK (
    `role` IN ('ADMIN', 'RECEPTIONIST', 'DENTIST', 'ASSISTANT', 'PATIENT')
  )
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS `prescriptions` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `medicine_name` varchar(255) DEFAULT NULL,
  `dosage` varchar(255) DEFAULT NULL,
  `instructions` varchar(255) DEFAULT NULL,
  `examination_id` bigint DEFAULT NULL,
  `patient_id` bigint DEFAULT NULL,
  `status` varchar(20) NOT NULL DEFAULT 'DRAFT',
  `created_at` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  `updated_at` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
  `created_by_user_id` bigint DEFAULT NULL,
  `dentist_id` bigint DEFAULT NULL,
  `finalized_at` datetime(6) DEFAULT NULL,
  `cancelled_at` datetime(6) DEFAULT NULL,
  `cancellation_reason` varchar(500) DEFAULT NULL,
  `replaces_prescription_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_prescriptions_examination_id` (`examination_id`),
  KEY `fk_prescriptions_patient` (`patient_id`),
  KEY `fk_prescriptions_created_by` (`created_by_user_id`),
  KEY `fk_prescriptions_dentist` (`dentist_id`),
  KEY `idx_prescriptions_replaces` (`replaces_prescription_id`),
  CONSTRAINT `fk_prescriptions_created_by` FOREIGN KEY (`created_by_user_id`)
    REFERENCES `users` (`id`) ON DELETE RESTRICT,
  CONSTRAINT `fk_prescriptions_dentist` FOREIGN KEY (`dentist_id`)
    REFERENCES `users` (`id`) ON DELETE RESTRICT,
  CONSTRAINT `fk_prescriptions_patient` FOREIGN KEY (`patient_id`)
    REFERENCES `patients` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_prescriptions_replaces` FOREIGN KEY (`replaces_prescription_id`)
    REFERENCES `prescriptions` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS `prescription_items` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `prescription_id` bigint NOT NULL,
  `medicine_name` varchar(255) NOT NULL,
  `strength` varchar(255) DEFAULT NULL,
  `dosage` varchar(255) DEFAULT NULL,
  `frequency` varchar(255) DEFAULT NULL,
  `duration` varchar(255) DEFAULT NULL,
  `quantity` int DEFAULT NULL,
  `instructions` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_prescription_items_prescription_id` (`prescription_id`),
  CONSTRAINT `fk_prescription_items_prescription` FOREIGN KEY (`prescription_id`)
    REFERENCES `prescriptions` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
