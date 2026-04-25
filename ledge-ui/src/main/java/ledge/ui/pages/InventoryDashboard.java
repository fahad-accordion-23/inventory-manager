package ledge.ui.pages;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import ledge.ui.clients.HttpInventoryClient;
import ledge.api.inventory.dto.response.ProductResponseDTO;
import ledge.api.shared.ApiResponse;
import ledge.api.shared.AuthContext;
import ledge.ui.core.Capability;
import ledge.ui.core.SessionManager;
import ledge.ui.viewmodels.ProductViewModel;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Controller for the inventory table view via the API layer.
 * Displays products with search, low-stock highlighting, and authorized
 * actions.
 */
public class InventoryDashboard {

    private static final int LOW_STOCK_THRESHOLD = 10;

    @FXML
    private TableView<ProductViewModel> inventoryTable;

    @FXML
    private TableColumn<ProductViewModel, Void> actionsColumn;

    @FXML
    private TextField searchField;

    private final HttpInventoryClient inventoryController;
    private final SessionManager sessionManager;
    private final Consumer<ProductResponseDTO> onEditRequested;

    private final ObservableList<ProductViewModel> allProducts = FXCollections.observableArrayList();
    private final FilteredList<ProductViewModel> filteredProducts = new FilteredList<>(allProducts, _ -> true);

    public InventoryDashboard(HttpInventoryClient inventoryController, SessionManager sessionManager,
            Consumer<ProductResponseDTO> onEditRequested) {
        this.inventoryController = inventoryController;
        this.sessionManager = sessionManager;
        this.onEditRequested = onEditRequested;
    }

    @FXML
    public void initialize() {
        inventoryTable.setItems(filteredProducts);

        searchField.textProperty().addListener((_, _, newValue) -> {
            filteredProducts.setPredicate(product -> {
                if (newValue == null || newValue.isBlank())
                    return true;
                return product.getName().toLowerCase().contains(newValue.toLowerCase());
            });
        });

        inventoryTable.setRowFactory(_ -> new TableRow<>() {
            @Override
            protected void updateItem(ProductViewModel item, boolean empty) {
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
        Optional<AuthContext> authContext = sessionManager.getAuthContext();
        if (authContext.isEmpty())
            return;

        ApiResponse<List<ProductResponseDTO>> response = inventoryController.getAllProducts(authContext.get());
        if (response.success()) {
            List<ProductViewModel> viewModels = response.data()
                    .stream()
                    .map(ProductViewModel::new)
                    .toList();
            allProducts.setAll(viewModels);
        }
    }

    private void setupActionsColumn() {
        if (sessionManager.getCurrentUser().isEmpty())
            return;

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
                    ProductViewModel product = getTableView().getItems().get(getIndex());
                    onEditRequested.accept(product.toDTO());
                });
                deleteBtn.setOnAction(_ -> {
                    ProductViewModel product = getTableView().getItems().get(getIndex());
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

    private void confirmAndDelete(ProductViewModel product) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Deletion");
        confirm.setHeaderText("Delete \"" + product.getName() + "\"?");
        confirm.setContentText("This action cannot be undone.");

        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Optional<AuthContext> authContext = sessionManager.getAuthContext();
            if (authContext.isEmpty())
                return;

            ApiResponse<Void> response = inventoryController.deleteProduct(authContext.get(), product.getId());
            if (response.success()) {
                loadAllProducts();
            } else {
                new Alert(Alert.AlertType.ERROR, response.error().message()).showAndWait();
            }
        }
    }
}
