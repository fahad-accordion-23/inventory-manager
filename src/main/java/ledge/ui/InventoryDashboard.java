package ledge.ui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import ledge.application.InventoryEventBroker;
import ledge.application.event.InventoryRefreshRequestedEvent;
import ledge.application.event.ProductsUpdatedEvent;
import ledge.application.dto.ProductDTO;

public class InventoryDashboard {

    @FXML
    private TableView<ProductDTO> inventoryTable;

    private final InventoryEventBroker eventBroker;

    public InventoryDashboard(InventoryEventBroker eventBroker) {
        this.eventBroker = eventBroker;
    }

    @FXML
    public void initialize() {
        eventBroker.subscribe(ProductsUpdatedEvent.class, this::handleProductsUpdated);
        eventBroker.publish(new InventoryRefreshRequestedEvent());
    }
    
    private void handleProductsUpdated(ProductsUpdatedEvent event) {
        Platform.runLater(() -> {
            inventoryTable.getItems().setAll(event.getProducts());
        });
    }
}
