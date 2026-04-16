package ledge.ui;

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
import ledge.inventory.app.InventoryCommandBus;
import ledge.inventory.app.InventoryEventBroker;
import ledge.inventory.app.InventoryQueryBus;
import ledge.inventory.app.command.RemoveProductCommand;
import ledge.inventory.app.command.UpdateProductCommand;
import ledge.inventory.app.dto.ProductDTO;
import ledge.inventory.app.event.ProductsUpdatedEvent;
import ledge.inventory.app.query.GetAllProductsQuery;
import ledge.security.domain.Role;
import ledge.security.app.SecurityContext;
import ledge.util.event.Subscribe;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

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
    private final Consumer<ProductDTO> onEditRequested;

    private final ObservableList<ProductDTO> allProducts = FXCollections.observableArrayList();
    private final FilteredList<ProductDTO> filteredProducts = new FilteredList<>(allProducts, _ -> true);

    public InventoryDashboard(InventoryEventBroker eventBroker, InventoryCommandBus commandBus,
            InventoryQueryBus queryBus, Consumer<ProductDTO> onEditRequested) {
        this.eventBroker = eventBroker;
        this.commandBus = commandBus;
        this.queryBus = queryBus;
        this.onEditRequested = onEditRequested;
        this.eventBroker.register(this);
    }

    @FXML
    public void initialize() {
        inventoryTable.setItems(filteredProducts);

        // US-4.3: Search by product name
        searchField.textProperty().addListener((_, _, newValue) -> {
            filteredProducts.setPredicate(product -> {
                if (newValue == null || newValue.isBlank())
                    return true;
                return product.getName().toLowerCase().contains(newValue.toLowerCase());
            });
        });

        // US-4.4: Low-stock highlighting
        inventoryTable.setRowFactory(_ -> new TableRow<>() {
            @Override
            protected void updateItem(ProductDTO item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setStyle("");
                } else if (item.getStockQuantity() < LOW_STOCK_THRESHOLD) {
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
            allProducts.setAll(queryBus.dispatch(new GetAllProductsQuery()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupActionsColumn() {
        Role role = SecurityContext.getCurrentUser().getRole();
        boolean canEdit = role.hasPermission(UpdateProductCommand.REQUIRED);
        boolean canDelete = role.hasPermission(RemoveProductCommand.REQUIRED);

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
        confirm.setHeaderText("Delete \"" + product.getName() + "\"?");
        confirm.setContentText("This action cannot be undone.");

        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                commandBus.dispatch(new RemoveProductCommand(product.getId()));
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
            List<ProductDTO> freshInventory = queryBus.dispatch(new GetAllProductsQuery());
            allProducts.setAll(freshInventory);
        });
    }
}
