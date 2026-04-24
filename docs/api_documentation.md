# Ledge Inventory Manager API Documentation

This document outlines the REST API endpoints for the Ledge Inventory Manager system, consistent with the Open Host Service (OHS) architecture.

---

## 🔐 Authentication & Session
Manage user authentication and session state.

### `POST /api/auth/login`
Authenticates a user and provides a session token.
- **Request Body**: `{ "username": "...", "password": "..." }`
- **Response**: `{ "token": "uuid-token", "expiresAt": "..." }`

### `POST /api/auth/logout`
Invalidates the current session token provided in the header.
- **Header**: `Authorization: Bearer <token>`
- **Response**: `204 No Content`

### `GET /api/auth/me`
Retrieves details about the currently authenticated user and their effective permissions.
- **Header**: `Authorization: Bearer <token>`
- **Response**: `{ "userId": "...", "username": "...", "role": "...", "permissions": [{ "resource": "...", "action": "..." }] }`

---

## 👥 User Management
Administrative actions for user profiles.

### `GET /api/users`
Lists all registered users. Requires `USER:READ` permission.
- **Response**: `Array<UserDTO>`

### `GET /api/users/{id}`
Retrieves a specific user by ID. Requires `USER:READ` permission.
- **Response**: `UserDTO`

### `POST /api/users`
Creates a new user account. Requires `USER:CREATE` permission.
- **Request Body**: `{ "username": "...", "password": "..." }`
- **Response**: `UserDTO`

### `PATCH /api/users/{id}/username`
Updates the username of an existing user. Requires `USER:UPDATE` permission.
- **Request Body**: `{ "newUsername": "..." }`
- **Response**: `200 OK`

### `PATCH /api/users/{id}/password`
Updates the password of an existing user. Requires `USER:UPDATE` permission or ownership.
- **Request Body**: `{ "newPassword": "..." }`
- **Response**: `200 OK`

### `DELETE /api/users/{id}`
Removes a user from the system. Requires `USER:DELETE` permission.
- **Response**: `204 No Content`

---

## 🛡 Security & Access Control
Manage roles, permissions, and user assignments. **A user can only have one role at a time.**

### `GET /api/security/roles`
Lists all roles defined in the system.
- **Response**: `Array<RoleDTO>`

### `POST /api/security/roles`
Registers a new custom role.
- **Request Body**: `{ "name": "...", "permissions": [{ "resource": "ResourceEnum", "action": "ActionEnum" }] }`
- **Response**: `{ "roleId": "..." }`

### `GET /api/security/assignments/{userId}`
Retrieves the role ID assigned to a specific user.
- **Response**: `{ "userId": "...", "roleId": "..." }`

### `PUT /api/security/assignments/{userId}`
Assigns a single role to a user. This overwrites any previously assigned role.
- **Request Body**: `{ "roleId": "..." }`
- **Response**: `200 OK`

### `DELETE /api/security/assignments/{userId}`
Revokes the role assignment from the user, leaving them with no role.
- **Response**: `204 No Content`

---

## 📦 Inventory Catalog
Manage products and stock.

### `GET /api/products`
Lists all products. Requires `PRODUCT:READ` permission.
- **Response**: `Array<ProductDTO>`

### `POST /api/products`
Adds a new product to the inventory. Requires `PRODUCT:CREATE` permission.
- **Request Body**: `{ "name": "...", "description": "...", "purchasePrice": 0.0, "sellingPrice": 0.0, "stockQuantity": 0, "taxRate": 0.1 }`
- **Response**: `ProductDTO`

### `PUT /api/products/{id}`
Updates an existing product. Requires `PRODUCT:UPDATE` permission.
- **Request Body**: `{ "name": "...", "purchasePrice": 0.0, ... }`
- **Response**: `ProductDTO`

### `DELETE /api/products/{id}`
Removes a product from the catalog. Requires `PRODUCT:DELETE` permission.
- **Response**: `204 No Content`
