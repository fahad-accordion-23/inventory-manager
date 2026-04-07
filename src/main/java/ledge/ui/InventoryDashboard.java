package ledge.ui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import ledge.application.InventoryEventBroker;
import ledge.application.dto.ProductDTO;
import ledge.application.event.InventoryRefreshRequestedEvent;
import ledge.application.event.ProductRemovedEvent;
import ledge.application.event.ProductUpdatedEvent;
import ledge.application.event.ProductsUpdatedEvent;
import ledge.domain.Role;
import ledge.security.SecurityContext;
import ledge.util.event.Subscribe;

import java.util.Optional;
import java.util.function.Consumer;

public class InventoryDashboard {

    @FXML
    private TableView<ProductDTO> inventoryTable;

    @FXML
    private TableColumn<ProductDTO, Void> actionsColumn;

    private final InventoryEventBroker eventBroker;
    private final Consumer<ProductDTO> onEditRequested;

    public InventoryDashboard(InventoryEventBroker eventBroker, Consumer<ProductDTO> onEditRequested) {
        this.eventBroker = eventBroker;
        this.onEditRequested = onEditRequested;
        this.eventBroker.register(this);
    }

    @FXML
    public void initialize() {
        setupActionsColumn();
        eventBroker.publish(new InventoryRefreshRequestedEvent());
    }

    @FXML
    public void handleRefreshAction() {
        eventBroker.publish(new InventoryRefreshRequestedEvent());
    }

    private void setupActionsColumn() {
        Role role = SecurityContext.getCurrentUser().getRole();
        boolean canEdit = role.hasPermission(ProductUpdatedEvent.REQUIRED);
        boolean canDelete = role.hasPermission(ProductRemovedEvent.REQUIRED);

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

                if (canEdit) buttons.getChildren().add(editBtn);
                if (canDelete) buttons.getChildren().add(deleteBtn);
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
            eventBroker.publish(new ProductRemovedEvent(product.getId()));
        }
    }

    @Subscribe
    private void handleProductsUpdated(ProductsUpdatedEvent event) {
        Platform.runLater(() -> {
            inventoryTable.getItems().setAll(event.getProducts());
        });
    }
}
