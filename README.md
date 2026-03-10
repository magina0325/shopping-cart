# E-Commerce REST API Backend

A simple backend system for an e-commerce platform built with Java Spring Boot. This system handles product management, categorized inventory, user authentication, and a dynamic shopping cart-to-order workflow.

# 🚀 Key Features
* **Secure Authentication**: Integrated JWT (JSON Web Tokens) for secure stateless authentication via the AuthController.

* **Dynamic Cart System**: Real-time cart management that automatically initializes carts for authenticated users and tracks items, quantities, and totals.

* **Role-Based Access Control (RBAC)**: Administrative protection on sensitive endpoints (Add/Update/Delete products) using @PreAuthorize.

* **Image Management**: Multi-file upload and download capabilities for product images with automatic metadata tracking.

* **Automated Order Fulfillment**: Converts shopping carts into finalized orders while verifying inventory levels and calculating totals.

# 🛠️ Tech Stack
* **Language**: Java 17+

* **Framework**: Spring Boot 3.x (Spring Security, Spring Data JPA)

* **Security**: JWT, BCrypt

* **Database**: MySQL

# 📖 API Documentation
1. **Authentication(`/auth`)** <br>
**Base URL:** `localhost:8080/api/v1/auth` <br>

| Endpoint | Method | Description | Access |
| --- | --- | --- | --- |
| /login | POST | Authenticates user and returns a JWT token. | Public |

2. **Products(`/products`)** <br>
   **Base URL:** `localhost:8080/api/v1/products` <br>

| Endpoint | Method | Description | Access |
| --- | --- | --- | --- |
| /all | GET | Retrieves all products as DTOs. | Public |
| /get/{productId} | GET | Fetches a single product by its ID. | Public |
| /add | POST | Adds a new product to the catalog. | Admin Only |
| /update/{productId} | PUT | Updates existing product details. | Admin Only |
| /delete/{productId} | DELETE | Removes a product from the database. | Admin Only |


3. **Shopping Cart & Items (`/carts`)** <br>
   **Base URL:** `localhost:8080/api/v1/carts` <br>

| Endpoint | Method | Description | Access |
| --- | --- | --- | --- |
| /get/{cartId} | GET | Retrieves the cart and its associated items. | User |
| /add-item | POST | Adds a product to the cart (auto-initializes cart if none exists). | User |
| /update-item | PUT | Updates the quantity of a specific item in the cart. | User |
| /delete-item | DELETE | Removes a specific product from the cart. | User |
| /clear/{cartId} | DELETE | Removes all items from the specified cart. | User |

4. **Orders(`/orders`)** <br>
   **Base URL:** `localhost:8080/api/v1/orders` <br>

| Endpoint | Method | Description | Access |
| --- | --- | --- | --- |
| /order | POST | Places an order for a user based on their current cart. | User |
| /order/{orderId} | GET | Retrieves details for a specific order. | User |
| /user/{userId} | GET | Retrieves all orders associated with a specific user. | User |

5. **Category Management(`/categories`)** <br>
   **Base URL:** `localhost:8080/api/v1/categories` <br>

| Endpoint | Method | Description | Access |
| --- | --- | --- | --- |
| /all | GET | Lists all available product categories. | Public |
| /add | POST | Creates a new category. | Admin Only |

6. **Image Management(`/images`)** <br>
   **Base URL:** `localhost:8080/api/v1/images` <br>

| Endpoint | Method | Description | Access |
| --- | --- | --- | --- |
| /upload | POST | Uploads multiple images for a specific product. | Admin Only |
| /image/download/{imageId} | GET | Downloads the image file directly. | Public |


# 🏗️ Architectural Highlights
**Data Transfer Objects (DTOs)**
The project utilizes a strict DTO pattern (e.g., ProductDto, UserDto, OrderDto) to ensure that internal database entities are never exposed directly to the client. This enhances security and reduces payload size.

**Error Handling**
A centralized approach is used for exception handling, including custom exceptions like:

* ResourceNotFoundException: Returned when entities are missing.

* AlreadyExistException: Prevents duplicate records for users or categories.

* InsufficientInventoryException: Triggered during the order process if stock is low.

# 🚦 Getting Started
1. **Clone the repository:** `git clone https://github.com/magina0325/shopping-cart.git`
2. **Configure Environment:** Update the file `src/main/resources/application.yaml` with your MySQL database credentials.
3. **Run the application:** `mvnw spring-boot:run`
4. **Test the API:** Import the provided `Postman_Collection.json` found in the root directory.

