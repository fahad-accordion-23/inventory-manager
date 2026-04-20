package ledge.ui.pages;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import ledge.ui.core.Capability;
import ledge.ui.core.SessionManager;
import ledge.inventory.application.commands.RemoveProductCommand;
import ledge.inventory.application.commands.UpdateProductCommand;
import ledge.inventory.application.dtos.ProductDTO;
import ledge.inventory.application.events.ProductsUpdatedEvent;
import ledge.inventory.application.query.GetAllProductsQuery;
import ledge.inventory.infrastructure.messaging.InventoryCommandBus;
import ledge.inventory.infrastructure.messaging.InventoryEventBroker;
import ledge.inventory.infrastructure.messaging.InventoryQueryBus;
import ledge.shared.types.Role;
import ledge.util.event.Subscribe;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Controller for the inventory table view.
 * Displays products with search, low-stock highlighting, and authorized
 * actions.
 */
public class InventoryDashboard {

    private static final int LOW_STOCK_THRESHOLD = 10;

    @FXML
    private TableView<ProductDTO> inventoryTable;

    @FXML
    private TableColumn<ProductDTO, Void> actionsColumn;

    @FXML
    private TextField searchField;

    private final InventoryEventBroker eventBroker;
    private final InventoryCommandBus commandBus;
    private final InventoryQueryBus queryBus;
    private final SessionManager sessionManager;
    private final Consumer<ProductDTO> onEditRequested;

    private final ObservableList<ProductDTO> allProducts = FXCollections.observableArrayList();
    private final FilteredList<ProductDTO> filteredProducts = new FilteredList<>(allProducts, _ -> true);

    public InventoryDashboard(InventoryEventBroker eventBroker, InventoryCommandBus commandBus,
            InventoryQueryBus queryBus, SessionManager sessionManager, Consumer<ProductDTO> onEditRequested) {
        this.eventBroker = eventBroker;
        this.commandBus = commandBus;
        this.queryBus = queryBus;
        this.sessionManager = sessionManager;
        this.onEditRequested = onEditRequested;
        this.eventBroker.register(this);
    }

    @FXML
    public void initialize() {
        inventoryTable.setItems(filteredProducts);

        searchField.textProperty().addListener((_, _, newValue) -> {
            filteredProducts.setPredicate(product -> {
                if (newValue == null || newValue.isBlank())
                    return true;
                return product.name().toLowerCase().contains(newValue.toLowerCase());
            });
        });

        inventoryTable.setRowFactory(_ -> new TableRow<>() {
            @Override
            protected void updateItem(ProductDTO item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setStyle("");
                } else if (item.stockQuantity() < LOW_STOCK_THRESHOLD) {
                    setStyle("-fx-background-color: #ffcccc;");
                } else {
                    setStyle("");
                }
            }
        });

        setupActionsColumn();
        loadAllProducts();
    }

    @FXML
    public void handleRefreshAction() {
        loadAllProducts();
    }

    private void loadAllProducts() {
        try {
            String token = sessionManager.getAuthToken().orElse("");
            allProducts.setAll(queryBus.dispatch(new GetAllProductsQuery(), token));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupActionsColumn() {
        Role role = sessionManager.getCurrentUser().get().role();
        if (role == null) {
            actionsColumn.setVisible(false);
            return;
        }

        boolean canEdit = sessionManager.isAllowed(Capability.EDIT_PRODUCT);
        boolean canDelete = sessionManager.isAllowed(Capability.DELETE_PRODUCT);

        if (!canEdit && !canDelete) {
            actionsColumn.setVisible(false);
            return;
        }

        actionsColumn.setCellFactory(_ -> new TableCell<>() {
            private final HBox buttons = new HBox(5);
            private final Button editBtn = new Button("Edit");
            private final Button deleteBtn = new Button("Delete");

            {
                buttons.setAlignment(Pos.CENTER);
                editBtn.setOnAction(_ -> {
                    ProductDTO product = getTableView().getItems().get(getIndex());
                    onEditRequested.accept(product);
                });
                deleteBtn.setOnAction(_ -> {
                    ProductDTO product = getTableView().getItems().get(getIndex());
                    confirmAndDelete(product);
                });

                if (canEdit)
                    buttons.getChildren().add(editBtn);
                if (canDelete)
                    buttons.getChildren().add(deleteBtn);
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : buttons);
            }
        });
    }

    private void confirmAndDelete(ProductDTO product) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Deletion");
        confirm.setHeaderText("Delete \"" + product.name() + "\"?");
        confirm.setContentText("This action cannot be undone.");

        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                String token = sessionManager.getAuthToken().orElse("");
                commandBus.dispatch(new RemoveProductCommand(product.id()), token);
            } catch (Exception e) {
                Alert err = new Alert(Alert.AlertType.ERROR);
                err.setContentText(e.getMessage());
                err.showAndWait();
            }
        }
    }

    @Subscribe
    private void handleProductsUpdated(ProductsUpdatedEvent event) {
        Platform.runLater(() -> {
            String token = sessionManager.getAuthToken().orElse("");
            List<ProductDTO> freshInventory = queryBus.dispatch(new GetAllProductsQuery(), token);
            allProducts.setAll(freshInventory);
        });
    }
}
