package ledge;

import javafx.application.Application;
import javafx.stage.Stage;
import ledge.inventory.application.commands.handlers.AddProductCommandHandler;
import ledge.inventory.application.commands.handlers.RemoveProductCommandHandler;
import ledge.inventory.application.commands.handlers.UpdateProductCommandHandler;
import ledge.inventory.application.query.handlers.GetAllProductsQueryHandler;
import ledge.inventory.application.services.ProductService;
import ledge.inventory.infrastructure.ProductRepository;
import ledge.inventory.infrastructure.messaging.InventoryCommandBus;
import ledge.inventory.infrastructure.messaging.InventoryEventBroker;
import ledge.inventory.infrastructure.messaging.InventoryQueryBus;
import ledge.security.application.AuthenticationService;
import ledge.security.application.AuthorizationService;
import ledge.security.application.SessionService;
import ledge.security.infrastructure.UserRepository;
import ledge.ui.SessionManager;
import ledge.ui.UIController;
import ledge.ui.messaging.UIEventBroker;

/**
 * Main application class.
 * Initializes the core components (CQRS buses, security services, messaging)
 * and hands off UI control to the UIController.
 */
public class App extends Application {

    private InventoryEventBroker inventoryEventBroker;
    private UIEventBroker uiEventBroker;
    private InventoryCommandBus commandBus;
    private InventoryQueryBus queryBus;
    private SessionManager sessionManager;
    private UIController uiController;

    @Override
    public void init() throws Exception {
        // Security Infrastructure
        SessionService sessionService = new SessionService();
        UserRepository userRepository = new UserRepository();
        AuthenticationService authService = new AuthenticationService(userRepository, sessionService);
        AuthorizationService authorizationService = new AuthorizationService(sessionService);

        // Messaging & CQRS
        inventoryEventBroker = new InventoryEventBroker();
        uiEventBroker = new UIEventBroker();
        commandBus = new InventoryCommandBus(authorizationService);
        queryBus = new InventoryQueryBus(authorizationService);

        // Inventory Domain
        ProductRepository productRepository = new ProductRepository();
        ProductService productService = new ProductService(productRepository);
        
        // Register Handlers
        commandBus.register(new AddProductCommandHandler(productService, inventoryEventBroker));
        commandBus.register(new RemoveProductCommandHandler(productService, inventoryEventBroker));
        commandBus.register(new UpdateProductCommandHandler(productService, inventoryEventBroker));
        queryBus.register(new GetAllProductsQueryHandler(productService));

        // Client Session
        sessionManager = new SessionManager(authService, sessionService);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Ledge Inventory Manager");

        // Initialize and start UI coordination
        uiController = new UIController(
                primaryStage,
                uiEventBroker,
                inventoryEventBroker,
                commandBus,
                queryBus,
                sessionManager
        );

        uiController.showLoginScene();
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}