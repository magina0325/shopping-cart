# E-Commerce REST API Backend

A simple backend system for an e-commerce platform built with Java Spring Boot. This system handles product management, categorized inventory, user authentication, and a dynamic shopping cart-to-order workflow.

# 🚀 Key Features
**Secure Authentication**: Integrated JWT (JSON Web Tokens) for secure stateless authentication via the AuthController.

**Dynamic Cart System**: Real-time cart management that automatically initializes carts for authenticated users and tracks items, quantities, and totals.

**Role-Based Access Control (RBAC)**: Administrative protection on sensitive endpoints (Add/Update/Delete products) using @PreAuthorize.

**Image Management**: Multi-file upload and download capabilities for product images with automatic metadata tracking.

**Automated Order Fulfillment**: Converts shopping carts into finalized orders while verifying inventory levels and calculating totals.

# 🛠️ Tech Stack
**Language**: Java 17+

**Framework**: Spring Boot 3.x (Spring Security, Spring Data JPA)

**Security**: JWT, BCrypt

**Database**: MySQL

