# Ledge Server Internal Dependencies

This document details the cross-package dependencies exclusively within the `ledge-server` module. It illustrates how different bounded contexts and architectural layers interact.

## 1. High-Level Context Map

This diagram provides a bird's-eye view of how the primary packages within `ledge-server/src/main/java/ledge` interact with one another.

```mermaid
graph TD
    %% Define Contexts
    API[ledge.api <br/> REST Controllers]
    Security[ledge.security <br/> Auth & Sessions]
    Users[ledge.users <br/> User Management]
    Inventory[ledge.inventory <br/> Product Catalog]
    Shared[ledge.shared <br/> CQRS Buses & Shared Types]
    Util[ledge.util <br/> Utilities like PasswordHasher]
    Boot[ledge.boot <br/> App Entrypoint]

    %% High Level Dependencies
    Boot -->|Component Scans| API
    Boot -->|Component Scans| Security
    Boot -->|Component Scans| Users
    Boot -->|Component Scans| Inventory
    Boot -->|Component Scans| Shared

    API -->|Validates/Routes via| Security
    API -->|Dispatches via| Shared
    API -->|Reads DTOs from| Users
    API -->|Reads DTOs from| Inventory

    Security -->|Fetches User Data| Users
    Security -->|Verifies Hashes with| Util
    Security -->|Secures| Shared

    Users -->|Implements & Uses| Shared
    Inventory -->|Implements & Uses| Shared
```

<br/>

## 2. API Layer Internal Dependencies

The `ledge.api` package is responsible for accepting HTTP traffic and delegating work. Within the server, it depends strictly on the CQRS buses, the security context, and the read-model DTOs/Contracts of the respective domain modules.

```mermaid
graph LR
    %% API Components
    subgraph ledge.api
        AuthController
        UserController
        InventoryController
    end

    %% Targets
    subgraph ledge.shared
        CommandBus[ledge.shared.infrastructure.commands.CommandBus]
        QueryBus[ledge.shared.infrastructure.queries.QueryBus]
    end

    subgraph ledge.security
        AuthService[IAuthenticationService]
        SessionService[ISessionService]
    end
    
    subgraph ledge.users
        UserDTOs[ledge.users.readmodel.dtos.*]
        UserQueries[ledge.users.readmodel.contracts.*]
        UserCommands[ledge.users.writemodel.commands.*]
    end

    subgraph ledge.inventory
        InvDTOs[ledge.inventory.readmodel.dtos.*]
        InvQueries[ledge.inventory.readmodel.contracts.*]
        InvCommands[ledge.inventory.writemodel.contracts.*]
    end

    AuthController -->|Logs in via| AuthService
    AuthController -->|Reads session via| SessionService
    AuthController -->|Returns| UserDTOs

    UserController -->|Dispatches| CommandBus
    UserController -->|Dispatches| QueryBus
    UserController -->|Constructs| UserCommands
    UserController -->|Constructs| UserQueries
    UserController -->|Returns| UserDTOs

    InventoryController -->|Dispatches| CommandBus
    InventoryController -->|Dispatches| QueryBus
    InventoryController -->|Constructs| InvCommands
    InventoryController -->|Constructs| InvQueries
    InventoryController -->|Returns| InvDTOs
```

<br/>

## 3. Security Subsystem Dependencies

The `ledge.security` package acts as the bodyguard for the application. It heavily relies on the `ledge.users` package to verify identities, highlighting an important dependency where security is a consumer of the user read-model.

```mermaid
graph TD
    subgraph ledge.security.application.services
        AuthenticationService
        AuthorizationService
    end

    subgraph ledge.security.domain
        SessionService
    end

    subgraph ledge.users.readmodel
        IUserReadRepository[ledge.users.readmodel.infrastructure.IUserReadRepository]
        UserDTO[ledge.users.readmodel.dtos.UserDTO]
    end

    subgraph ledge.util
        PasswordHasher[ledge.util.PasswordHasher]
    end

    subgraph ledge.shared
        Permission[ledge.shared...Permission]
    end

    AuthenticationService -->|Fetches user by username from| IUserReadRepository
    AuthenticationService -->|Receives| UserDTO
    AuthenticationService -->|Verifies password using| PasswordHasher
    AuthenticationService -->|Creates token in| SessionService

    AuthorizationService -->|Validates token via| SessionService
    AuthorizationService -->|Enforces| Permission
```

<br/>

## 4. Bounded Contexts -> Shared Infrastructure

Both core bounded contexts (`users` and `inventory`) rely heavily on the `ledge.shared` infrastructure packages to map handlers and enforce type safety.

```mermaid
graph TD
    subgraph Domain Contexts
        UserHandlers[ledge.users.***.handlers.*]
        InvHandlers[ledge.inventory.***.handlers.*]
    end

    subgraph ledge.shared.infrastructure
        CommandHandler[CommandHandler Interface]
        QueryHandler[QueryHandler Interface]
    end

    UserHandlers -.->|Implements| CommandHandler
    UserHandlers -.->|Implements| QueryHandler
    
    InvHandlers -.->|Implements| CommandHandler
    InvHandlers -.->|Implements| QueryHandler
```
