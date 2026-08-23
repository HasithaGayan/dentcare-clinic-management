# Day 1 Architecture Notes

## DentCare Request Flow

```text
React Frontend
      ↓
REST API
      ↓
Controller
      ↓
Service
      ↓
Repository
      ↓
MySQL Database


## What Each Layer Does

### React Frontend
Displays the user interface and sends HTTP requests to the backend.

### REST API
Provides URLs such as `/api/health` that the frontend can call.

### Controller
Receives HTTP requests and returns HTTP responses.

### Service
Contains the main business logic of the application.

### Repository
Communicates with the database using Spring Data JPA.

### MySQL Database
Stores DentCare data permanently.

## Current Working Flow

```text
Browser
   ↓
React - localhost:5173
   ↓
GET http://localhost:8080/api/health
   ↓
HealthController
   ↓
JSON response
   ↓
React displays backend status