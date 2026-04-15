# Queries

In the CQRS architecture of Ledge Inventory Manager, **Queries** represent a request to read data without side effects.
They are typically named starting with `Get` or `Find` and return a result of type `R`. They are routed via the `QueryBus` to exactly one `@QueryHandler`.

## List of Queries

| Query | Returns | Purpose | Required Permission | Handler |
|-------|---------|---------|---------------------|---------|
| `GetAllProductsQuery` | `List<ProductDTO>` | Retrieves all existing products for display. Replaces `InventoryRefreshRequestedEvent`. | `VIEW_INVENTORY` | `ProductController` |
| `GetProductByIdQuery` | `Optional<ProductDTO>` | Submits a request to read details of a specific product by its ID. | `VIEW_INVENTORY` | `ProductController` |

## Workflow
1. UI requests data by creating a `<Action>Query`.
2. UI dispatches it to the `QueryBus`.
3. `QueryBus` verifies `Required Permission` using `AccessPolicy`.
4. `QueryBus` routes the query to its respective handler.
5. The handler reads from the data source and returns a result `R`.
6. UI consumes the result `R` and updates the view explicitly.
