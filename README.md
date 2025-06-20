# 🏥 Hospital Management System (HMS) – Doctor Slot Booking Backend

This is a **Spring Boot 3.5 + PostgreSQL**-based backend application for managing a hospital’s doctor appointment scheduling, patient queue, and real-time consultation flow.

---

## 🔧 Features Implemented

### ✅ Doctor Slot Management
- Create/edit/delete individual or bulk slots
- Define consultation slot duration (e.g., 15 min)
- Prevent overlapping or conflicting slots
- Recurring slot templates supported (optional)

### ✅ Patient Booking System
- Book available time slots per doctor/day
- Assign queue number automatically
- Support for missed and late arrivals

### ✅ Queue Handling
- Mark patient as arrived, missed, or completed
- Auto-reorder missed patients to next slot

### ✅ Bulk Slot Operations
- Bulk update slot duration by date range
- Bulk delete for doctor unavailability (e.g., leave)
- Import slots from Excel/CSV (Google Calendar style)

### ✅ Upcoming (optional)
- WebSocket integration for real-time queue updates
- Admin dashboard with analytics & heatmaps
- Rule engine for slot configuration

---

## 🗂 Tech Stack

| Layer        | Tech Stack                           |
|--------------|--------------------------------------|
| Language     | Java 17                              |
| Framework    | Spring Boot 3.5, Spring Web, JPA     |
| Database     | PostgreSQL                           |
| Frontend     | ReactJS (separate repo - optional)   |
| Tools        | Lombok, MapStruct, Swagger/OpenAPI   |

---

## 🚀 Getting Started

### Prerequisites
- Java 17+ installed
- PostgreSQL database running
- Maven installed

### Clone the Repository

```bash
git clone https://github.com/your-username/hms-slot-booking.git
cd hms-slot-booking
```

### Configure DB

In `application.yml` or `application.properties`, set:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/hms_db
    username: postgres
    password: yourpassword
```

### Run the Application

```bash
mvn spring-boot:run
```

App will start at: `http://localhost:8080`

---

## 🧪 Testing with Postman / Curl

All API endpoints with working `curl` examples are available in this file:  
👉 [`hms-backend-api-doc.md`](./hms-backend-api-doc.md)

---

## 📁 Folder Structure

```
src/main/java/com/hms/
├── config/              # CORS, Swagger, etc.
├── controller/          # REST controllers
├── dto/                 # Request/response models
├── enums/               # SlotStatus, etc.
├── model/               # JPA entities
├── repository/          # JPA repositories
├── service/             # Business logic layer
└── utils/               # Slot generator, helpers
```

---

## 👨‍💻 Contributors

- **@Atabur Rahaman Mollah (Rehman)** – Backend Developer  
- Open to community contributors for frontend, analytics, and reporting layers

---

## 📜 License

MIT License. Feel free to fork and extend for your clinic, hospital, or startup.

---

## ❤️ Acknowledgement

Built with passion to make healthcare scheduling efficient, reliable, and developer-friendly.

---
