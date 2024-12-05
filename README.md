# 🌱🚜👨‍🌾🛠️ Crop Management System - Green Shadow 🌱🚜👨‍🌾🛠️

Green Shadow is an innovative digital solution crafted to revolutionize agricultural operations. By offering an integrated suite of tools for managing fields, crops, personnel, equipment, vehicles, and observation logs, it simplifies daily farm activities. With role-specific access controls and a user-friendly interface, Green Shadow fosters smarter decision-making and boosts productivity.

---

## 📘 API Overview
Access the complete API guide for Green Shadow:
[API Documentation for Green Shadow](#)

---

## 💻 Technology Stack
### Frontend
- **Languages**: HTML5, CSS3, JavaScript.
- **Frameworks**: Bootstrap for dynamic and responsive layouts.
- **Data Handling**: AJAX and Fetch API for smooth client-server communication.

### Backend
- **Frameworks**: Spring Boot for RESTful APIs, Hibernate for database interaction.
- **Database**: MySQL for structured data management.
- **Security**: JSON Web Tokens (JWT) for robust authentication.

---

## ✨ Key Features
### Core Modules
- **Field Management**: Manage detailed field information, including crop assignments and images.
- **Crop Tracking**: Monitor crop lifecycle stages, types, and associated field data.
- **Personnel Management**: Allocate tasks, define roles, and oversee staff activities.
- **Equipment Tracking**: Manage allocation and operational status of farming tools.
- **Vehicle Management**: Keep detailed records of vehicles and their assignments.
- **Observation Logs**: Log and track field and crop observations comprehensively.
- **Role-Based Permissions**: Customized access levels for Managers, Administrators, and Scientists.

### Additional Highlights
- Modern Single Page Application (SPA) design for seamless user experience.
- Backed by Spring Boot for scalable performance.
- Secure authentication with JWT-based authorization.
- Efficient relational database modeling using MySQL.

---

## 📋 Setup Instructions
### Requirements
- JDK 17 or newer.
- MySQL Server.
- Postman (optional) for API testing.

### Steps to Deploy
1. Clone the repositories:
    ```bash
    git clone https://github.com/ayomahansani/AAD-Crop_Monitoring_System_Backend_SpringBoot.git
    git clone https://github.com/ayomahansani/AAD-Crop_Monitoring_System_Frontend_JS_With_AJAX.git
    ```
2. Configure the database settings in the `application-dev.properties` file:
    ```properties
    spring.datasource.url=jdbc:mysql://localhost:3306/green_shadow_farm?createDatabaseIfNotExist=true
    spring.datasource.username=your-username
    spring.datasource.password=your-password
    ```
3. Launch the application and access it at:
    ```
    http://localhost:3000
    ```

---

## 🔑 Role Permissions
| Role            | Access Rights                                                            |
|-----------------|--------------------------------------------------------------------------|
| **MANAGER**     | Complete CRUD access to all system entities.                            |
| **ADMINISTRATOR** | Limited to read-only access for crop, field, and log-related data.        |
| **SCIENTIST**   | Restricted from modifying staff, vehicle, and equipment records.        |

---

## 🗄️ Database Design
The Green Shadow system ensures data integrity and efficiency with its relational database model:
- **Fields and Crops**: Many-to-Many linkage for optimal field-crop mapping.
- **Staff and Vehicles**: One-to-Many assignment relationships.
- **Fields and Equipment**: One-to-Many relationships for resource allocation.
- **Logs**: Centralized tracking of observations across entities.

---

## 📌 API Highlights
### Base URL
```
http://localhost:5052/cropMonitoringSystem/api/v1/
```

### Sample Endpoints
- **Register User**: `POST /auth/signup`
- **Get Fields**: `GET /fields`
- **Add Crop**: `POST /crops`
- **Delete Equipment**: `DELETE /equipments/{equipmentId}`
- **Modify Vehicle**: `PUT /vehicles/{vehicleCode}`

---

## 📜 License
This project is distributed under the **MIT License**. See the LICENSE file for additional information.

---

## 🙌 Acknowledgments
Special thanks to our mentors and peers for their guidance and contributions in shaping Green Shadow.

**Green Shadow - Driving Innovation in Farm Management.**

