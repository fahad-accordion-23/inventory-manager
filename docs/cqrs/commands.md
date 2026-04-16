# Commands

In the CQRS architecture of Ledge Inventory Manager, **Commands** represent an intent to change the state of the system. 
They are typically named with verbs in the imperative mood and are dispatched via the `CommandBus` to exactly one `@CommandHandler`.

## List of Commands

| Command | Purpose | Required Permission | Handler |
|---------|---------|---------------------|---------|
| `LoginCommand` | Submits user credentials to authenticate against the system. | *None* | `AuthController` |
| `LogoutCommand` | Ends the current session and clears the security context. | *None* | `AuthController` |
| `AddProductCommand` | Requests to add a new product to the inventory. | `ADD_PRODUCT` | `ProductController`|
| `UpdateProductCommand` | Requests to update an existing product. | `UPDATE_PRODUCT` | `ProductController`|
| `RemoveProductCommand` | Requests to delete a product from the inventory. | `DELETE_PRODUCT`| `ProductController`|

## Workflow
1. UI initiates action by creating a `<Action>Command`.
2. UI dispatches it to the `CommandBus`.
3. `CommandBus` verifies `Required Permission` using `AccessPolicy`.
4. `CommandBus` routes the command to its respective handler.
5. The handler modifies the domain entity.
6. Real **Events** are published (e.g. `ProductAddedEvent`) via `EventBroker` upon success.
