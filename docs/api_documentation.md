# Ledge Inventory Manager API Documentation

This document outlines the REST API endpoints for the Ledge Inventory Manager system, consistent with the Open Host Service (OHS) architecture.

---

## 🔐 Authentication & Session
Manage user authentication and session state.

### `POST /api/auth/login`
Authenticates a user and provides a session token.
- **Request Body**: `LoginRequestDTO`
    - `username`: `String`
    - `password`: `String`
- **Response**: `LoginResponseDTO`
    - `token`: `String`
    - `user`: `UserResponseDTO`

### `POST /api/auth/logout`
Invalidates the current session token provided in the header.
- **Header**: `Authorization: Bearer <token>`
- **Response**: `204 No Content`

### `GET /api/auth/me`
Retrieves details about the currently authenticated user and their effective permissions.
- **Header**: `Authorization: Bearer <token>`
- **Response**: `UserResponseDTO`

---

## 👥 User Management
Administrative actions for user profiles.

### `GET /api/users`
Lists all registered users. Requires `USER:READ` permission.
- **Response**: `GetAllUsersResponseDTO`
    - `users`: `Array<UserResponseDTO>`

### `GET /api/users/{id}`
Retrieves a specific user by ID. Requires `USER:READ` permission.
- **Response**: `UserResponseDTO`

### `POST /api/users`
Creates a new user account. Requires `USER:CREATE` permission.
- **Request Body**: `CreateUserRequestDTO`
    - `username`: `String`
    - `password`: `String`
- **Response**: `UserResponseDTO`

### `PATCH /api/users/{id}/username`
Updates the username of an existing user. Requires `USER:UPDATE` permission.
- **Request Body**: `UpdateUsernameRequestDTO`
    - `newUsername`: `String`
- **Response**: `200 OK`

### `PATCH /api/users/{id}/password`
Updates the password of an existing user. Requires `USER:UPDATE` permission.
- **Request Body**: `UpdatePasswordRequestDTO`
    - `newPassword`: `String`
- **Response**: `200 OK`

### `DELETE /api/users/{id}`
Removes a user from the system. Requires `USER:DELETE` permission.
- **Response**: `204 No Content`

---

## 🛡 Security & Access Control
Manage roles, permissions, and user assignments. **A user can only have one role at a time.**

### `GET /api/security/roles`
Lists all roles defined in the system.
- **Response**: `GetAllRolesResponseDTO`
    - `roles`: `Array<RoleResponseDTO>`

### `POST /api/security/roles`
Registers a new custom role.
- **Request Body**: `CreateRoleRequestDTO`
    - `name`: `String`
    - `permissions`: `Array<PermissionDTO>`
    
- **Response**: `CreateRoleResponseDTO`
    - `role`: `RoleResponseDTO`

### `GET /api/security/users/{userId}/role`
Retrieves the role ID assigned to a specific user.
- **Response**: `GetUserRoleResponseDTO`
    - `roleId`: `UUID`

### `PUT /api/security/users/{userId}/role`
Assigns a single role to a user. This overwrites any previously assigned role.
- **Request Body**: `AssignRoleRequestDTO`
    - `roleId`: `UUID`
- **Response**: `200 OK`

### `DELETE /api/security/users/{userId}`
Revokes the role assignment from the user, leaving them with no role.
- **Response**: `204 No Content`

### `DELETE /api/security/roles/{roleId}`
Deletes a role from the system. Will fail if the role is assigned to any user.
- **Response**: `204 No Content`

---

## 📦 Inventory Catalog
Manage products and stock.

### `GET /api/products`
Lists all products. Requires `PRODUCT:READ` permission.
- **Response**: `GetAllProductsResponseDTO`
    - `products`: `Array<ProductResponseDTO>`

### `POST /api/products`
Adds a new product to the inventory. Requires `PRODUCT:CREATE` permission.
- **Request Body**: `CreateProductRequestDTO`
    - `name`: `String`
    - `description`: `String`
    - `purchasePrice`: `BigDecimal`
    - `sellingPrice`: `BigDecimal`
    - `stockQuantity`: `Integer`
    - `taxRate`: `BigDecimal`
- **Response**: `ProductResponseDTO`

### `PUT /api/products/{id}`
Updates an existing product. Requires `PRODUCT:UPDATE` permission.
- **Request Body**: `UpdateProductRequestDTO`
    - `name`: `String`
    - `description`: `String`
    - `purchasePrice`: `BigDecimal`
    - `sellingPrice`: `BigDecimal`
    - `stockQuantity`: `Integer`
    - `taxRate`: `BigDecimal`
- **Response**: `ProductResponseDTO`

### `DELETE /api/products/{id}`
Removes a product from the catalog. Requires `PRODUCT:DELETE` permission.
- **Response**: `204 No Content`
