# Events

In the CQRS architecture of Ledge Inventory Manager, **Events** represent an immutable fact that something has happened in the past. They are named in the past tense. 
Events are published by the `EventBroker` and can have zero to many `@Subscribe` listeners. They do *not* contain intents to change the state or read data.

## List of Events

| Event | Purpose | Published By | Typical Listeners |
|-------|---------|--------------|-------------------|
| `LoginSucceededEvent` | Fired when a user successfully authenticates. Contains the `User` object. | `AuthController` | UI Components (to switch views to Dashboard), MainLayout |
| `LoginFailedEvent` | Fired when authentication fails due to invalid credentials. | `AuthController` | `LoginView` (to show an error message) |
| `ProductAddedEvent` | Fired *after* a new product is successfully added. | `ProductController` | `InventoryDashboard` (or similar) to refresh UI |
| `ProductUpdatedEvent` | Fired *after* a product is modified. | `ProductController` | `InventoryDashboard` |
| `ProductRemovedEvent` | Fired *after* a product is successfully deleted. | `ProductController` | `InventoryDashboard` |
| `ProductsUpdatedEvent` | Aggregate event fired when the main product list has fundamentally changed. | `ProductController` | UI components to reload data directly |

## Workflow
1. A Domain Service or Controller completes a state-altering action.
2. The Service creates an Event describing what happened.
3. It publishes to the `EventBroker`.
4. `EventBroker` notifies all listening `@Subscribe` methods asynchronously or synchronously.
5. Dependent components update their status or reactive UI elements without needing direct couplings.
