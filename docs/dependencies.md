# Ledge Server Internal Dependencies

This document details the cross-package dependencies exclusively within the `ledge-server` module. It illustrates how different bounded contexts and architectural layers interact via formal OHS and Events.

## 1. High-Level Context Map

This diagram provides a bird's-eye view of how the primary packages within `ledge-server/src/main/java/ledge` interact with one another.

```mermaid
graph TD
    %% Define Contexts
    API[ledge.api <br/> REST Controllers]
    Security[ledge.security <br/> Auth & RBAC]
    Users[ledge.users <br/> User Management]
    Inventory[ledge.inventory <br/> Product Catalog]
    Shared[ledge.shared <br/> Infrastructure]
    Util[ledge.util <br/> Utilities]
    Boot[ledge.boot <br/> App Entrypoint]

    %% High Level Dependencies
    Boot -->|Component Scans| API
    Boot -->|Component Scans| Security
    Boot -->|Component Scans| Users
    Boot -->|Component Scans| Inventory
    Boot -->|Component Scans| Shared

    API -->|Validates/Routes via| Security
    API -->|Dispatches via| Shared
    API -->|Consumes OHS/DTOs from| Users
    API -->|Consumes DTOs from| Inventory

    Security -->|Resolves Identites via OHS| Users
    Security -.->|Listens for Events from| Users
    Security -->|Verifies Hashes with| Util
    Security -->|Secures| Shared

    Users -.->|Syncs Read Model via| Shared
    Inventory -.->|Syncs Read Model via| Shared
```

<br/>

## 2. API Layer Internal Dependencies

The `ledge.api` package is responsible for accepting HTTP traffic and delegating work. Within the server, it depends strictly on the CQRS buses, the security OHS, and the domain OHS/DTOs of the respective modules.

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
        RoleService[IUserRoleService]
    end
    
    subgraph ledge.users
        UsersOHS[IUserService]
        UserDTOs[UserDTO]
    end

    subgraph ledge.inventory
        InvDTOs[ProductDTO]
    end

    AuthController -->|Logs in via| AuthService
    AuthController -->|Resolves Profile via| UsersOHS
    AuthController -->|Hydrates Roles via| RoleService

    UserController -->|Dispatches| CommandBus
    UserController -->|Dispatches| QueryBus

    InventoryController -->|Dispatches| CommandBus
    InventoryController -->|Dispatches| QueryBus
```

<br/>

## 3. Security Subsystem Dependencies

The `ledge.security` package acts as the bodyguard for the application. It relies on the Users OHS for identity verification and reacts to User events for role management.

```mermaid
graph TD
    subgraph ledge.security
        AuthenticationService
        AuthorizationService
        SecurityEventListener
    end

    subgraph ledge.users.api
        IUserService[IUserService OHS]
        UserDTO[UserDTO]
    end
    
    subgraph ledge.users.events
        UserEvents[UserRegisteredIntegrationEvent]
    end

    subgraph ledge.util
        PasswordHasher[ledge.util.PasswordHasher]
    end

    AuthenticationService -->|Fetches identity from| IUserService
    AuthenticationService -->|Receives| UserDTO
    AuthenticationService -->|Verifies password using| PasswordHasher

    SecurityEventListener -.->|Reacts to| UserEvents
    SecurityEventListener -->|Updates internal roles| SecurityRepositories
```

<br/>

## 4. Bounded Context Isolation (Events)

Both core bounded contexts (`users` and `inventory`) use Spring Events to decouple their Write side (Domain) from their Read side (Persistence).

```mermaid
graph TD
    subgraph Domain Contexts
        WHandlers[Write Command Handlers]
        SyncListeners[Read Model Listeners]
    end

    subgraph Spring Framework
        EventBus[ApplicationEventPublisher]
    end

    WHandlers -->|Publishes Domain Events| EventBus
    EventBus -.->|Reactive Trigger| SyncListeners
    SyncListeners -->|Updates| ReadRepositories
```
