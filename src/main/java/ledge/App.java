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
import ledge.security.application.services.AuthenticationService;
import ledge.security.application.services.AuthorizationService;
import ledge.security.application.services.IAuthenticationService;
import ledge.security.application.services.IAuthorizationService;
import ledge.security.domain.ISessionService;
import ledge.security.domain.SessionService;
import ledge.ui.core.SessionManager;
import ledge.ui.core.UIController;
import ledge.ui.messaging.UIEventBroker;
import ledge.users.application.commands.handlers.*;
import ledge.users.application.query.handlers.*;
import ledge.users.application.services.UserService;
import ledge.users.infrastructure.UserRepository;
import ledge.users.infrastructure.messaging.UserCommandBus;
import ledge.users.infrastructure.messaging.UserEventBroker;
import ledge.users.infrastructure.messaging.UserQueryBus;

/**
 * Main application class.
 * Initializes the core components (CQRS buses, security services, messaging)
 * and hands off UI control to the UIController.
 */
public class App extends Application {

    private InventoryEventBroker inventoryEventBroker;
    private UserEventBroker userEventBroker;
    private UIEventBroker uiEventBroker;

    private InventoryCommandBus inventoryCommandBus;
    private InventoryQueryBus inventoryQueryBus;
    private UserCommandBus userCommandBus;
    private UserQueryBus userQueryBus;

    private SessionManager sessionManager;
    private UIController uiController;

    @Override
    public void init() throws Exception {
        // User Domain
        UserRepository userRepository = new UserRepository();
        UserService userService = new UserService(userRepository);

        // Security Infrastructure
        ISessionService sessionService = new SessionService();
        IAuthenticationService authService = new AuthenticationService(userService, sessionService);
        IAuthorizationService authorizationService = new AuthorizationService(sessionService);

        // Messaging & CQRS
        inventoryEventBroker = new InventoryEventBroker();
        userEventBroker = new UserEventBroker();
        uiEventBroker = new UIEventBroker();

        inventoryCommandBus = new InventoryCommandBus(authorizationService);
        inventoryQueryBus = new InventoryQueryBus(authorizationService);
        userCommandBus = new UserCommandBus(authorizationService);
        userQueryBus = new UserQueryBus(authorizationService);

        // Inventory Domain
        ProductRepository productRepository = new ProductRepository();
        ProductService productService = new ProductService(productRepository);

        // Register Handlers - Inventory
        inventoryCommandBus.register(new AddProductCommandHandler(productService, inventoryEventBroker));
        inventoryCommandBus.register(new RemoveProductCommandHandler(productService, inventoryEventBroker));
        inventoryCommandBus.register(new UpdateProductCommandHandler(productService, inventoryEventBroker));
        inventoryQueryBus.register(new GetAllProductsQueryHandler(productService));

        // Register Handlers - Users
        userCommandBus.register(new AddUserCommandHandler(userService, userEventBroker));
        userCommandBus.register(new RemoveUserCommandHandler(userService, userEventBroker));
        userCommandBus.register(new ChangeUserPasswordCommandHandler(userService, userEventBroker));
        userCommandBus.register(new ChangeUserRoleCommandHandler(userService, userEventBroker));
        userCommandBus.register(new ChangeUsernameCommandHandler(userService, userEventBroker));

        userQueryBus.register(new GetAllUsersQueryHandler(userService));
        userQueryBus.register(new GetUserQueryHandler(userService));
        userQueryBus.register(new GetUserByIdQueryHandler(userService));

        // Client Session
        sessionManager = new SessionManager(authService, userQueryBus);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Ledge Inventory Manager");

        // Initialize and start UI coordination
        uiController = new UIController(
                primaryStage,
                uiEventBroker,
                inventoryEventBroker,
                inventoryCommandBus,
                inventoryQueryBus,
                userEventBroker,
                userCommandBus,
                userQueryBus,
                sessionManager);

        uiController.showLoginScene();
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}