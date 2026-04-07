package ledge.ui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import ledge.application.InventoryEventBroker;
import ledge.application.event.InventoryRefreshRequestedEvent;
import ledge.application.event.ProductsUpdatedEvent;
import ledge.application.dto.ProductDTO;
import ledge.util.event.Subscribe;

public class InventoryDashboard {

    @FXML
    private TableView<ProductDTO> inventoryTable;

    private final InventoryEventBroker eventBroker;

    public InventoryDashboard(InventoryEventBroker eventBroker) {
        this.eventBroker = eventBroker;
        this.eventBroker.register(this);
    }

    @FXML
    public void initialize() {
        eventBroker.publish(new InventoryRefreshRequestedEvent());
    }
    
    @Subscribe
    private void handleProductsUpdated(ProductsUpdatedEvent event) {
        Platform.runLater(() -> {
            inventoryTable.getItems().setAll(event.getProducts());
        });
    }
}
