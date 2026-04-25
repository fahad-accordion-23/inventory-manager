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
- **Response**: `ApiResponse<LoginResponseDTO>`
    - `token`: `String`
    - `user`: `UserResponseDTO`

### `POST /api/auth/logout`
Invalidates the current session token provided in the header.
- **Header**: `Authorization: Bearer <token>`
- **Response**: `ApiResponse<Void>`

### `GET /api/auth/me`
Retrieves details about the currently authenticated user and their effective permissions.
- **Header**: `Authorization: Bearer <token>`
- **Response**: `ApiResponse<UserResponseDTO>`

---

## 👥 User Management
Administrative actions for user profiles.

### `GET /api/users`
Lists all registered users. Requires `USER:READ` permission.
- **Response**: `ApiResponse<GetAllUsersResponseDTO>`
    - `users`: `Array<UserResponseDTO>`

### `POST /api/users`
Creates a new user account. Requires `USER:CREATE` permission.
- **Request Body**: `CreateUserRequestDTO`
    - `username`: `String`
    - `password`: `String`
- **Response**: `ApiResponse<UserResponseDTO>`

### `PATCH /api/users/{id}/username`
Updates the username of an existing user. Requires `USER:UPDATE` permission.
- **Request Body**: `ChangeUsernameRequestDTO`
    - `newUsername`: `String`
- **Response**: `ApiResponse<Void>`

### `PATCH /api/users/{id}/password`
Updates the password of an existing user. Requires `USER:UPDATE` permission.
- **Request Body**: `ChangePasswordRequestDTO`
    - `newPassword`: `String`
- **Response**: `ApiResponse<Void>`

### `DELETE /api/users/{id}`
Removes a user from the system. Requires `USER:DELETE` permission.
- **Response**: `ApiResponse<Void>`

---

## 🛡 Security & Access Control
Manage roles, permissions, and user assignments. **A user can only have one role at a time.**

### `GET /api/security/roles`
Lists all roles defined in the system.
- **Response**: `ApiResponse<GetAllRolesResponseDTO>`
    - `roles`: `Array<RoleResponseDTO>`

### `POST /api/security/roles`
Registers a new custom role.
- **Request Body**: `RegisterRoleRequestDTO`
    - `name`: `String`
    - `permissions`: `Array<PermissionDTO>`
- **Response**: `ApiResponse<Void>` (Implementation Pending)

### `GET /api/security/users/{userId}/role`
Retrieves the role ID assigned to a specific user.
- **Response**: `ApiResponse<GetUserRoleResponseDTO>`
    - `roleId`: `UUID`

### `PUT /api/security/users/{userId}/role`
Assigns a single role to a user. This overwrites any previously assigned role.
- **Request Body**: `AssignRoleRequestDTO`
    - `roleId`: `UUID`
- **Response**: `ApiResponse<Void>`

### `DELETE /api/security/users/{userId}`
Revokes the role assignment from the user, leaving them with no role.
- **Response**: `ApiResponse<Void>`

### `DELETE /api/security/roles/{roleId}`
Deletes a role from the system. Will fail if the role is assigned to any user.
- **Response**: `ApiResponse<Void>` (Implementation Pending)

---

## 📦 Inventory Catalog
Manage products and stock.

### `GET /api/products`
Lists all products. Requires `PRODUCT:READ` permission.
- **Response**: `ApiResponse<GetAllProductsResponseDTO>`
    - `products`: `Array<ProductResponseDTO>`

### `POST /api/products`
Adds a new product to the inventory. Requires `PRODUCT:CREATE` permission.
- **Request Body**: `CreateProductRequestDTO`
    - `name`: `String`
    - `purchasePrice`: `BigDecimal`
    - `sellingPrice`: `BigDecimal`
    - `stockQuantity`: `Integer`
    - `taxRate`: `BigDecimal`
- **Response**: `ApiResponse<ProductResponseDTO>`

### `PUT /api/products/{id}`
Updates an existing product. Requires `PRODUCT:UPDATE` permission.
- **Request Body**: `UpdateProductRequestDTO`
    - `name`: `String`
    - `purchasePrice`: `BigDecimal`
    - `sellingPrice`: `BigDecimal`
    - `stockQuantity`: `Integer`
    - `taxRate`: `BigDecimal`
- **Response**: `ApiResponse<ProductResponseDTO>`

### `DELETE /api/products/{id}`
Removes a product from the catalog. Requires `PRODUCT:DELETE` permission.
- **Response**: `ApiResponse<Void>`
