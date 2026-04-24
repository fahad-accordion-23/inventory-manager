package ledge.boot;

import javafx.application.Application;
import javafx.stage.Stage;

import ledge.ui.clients.HttpAuthClient;
import ledge.ui.clients.HttpInventoryClient;
import ledge.ui.clients.HttpUserClient;
import ledge.ui.core.SessionManager;
import ledge.ui.core.UIController;
import ledge.ui.messaging.UIEventBroker;

/**
 * Main application class.
 * Initializes the HTTP proxy clients for communicating with Ledge Server
 * and hands off UI control to the UIController.
 */
public class App extends Application {

    private UIEventBroker uiEventBroker;
    private UIController uiController;
    private SessionManager sessionManager;
    private HttpAuthClient authController;
    private HttpInventoryClient inventoryController;
    private HttpUserClient userController;

    @Override
    public void init() throws Exception {
        // Initialize HTTP proxy clients
        authController = new HttpAuthClient();
        inventoryController = new HttpInventoryClient();
        userController = new HttpUserClient();

        // Initialize UI infrastructure
        uiEventBroker = new UIEventBroker();
        sessionManager = new SessionManager(authController);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Ledge Inventory Manager");

        // Initialize and start UI coordination
        uiController = new UIController(
                primaryStage,
                uiEventBroker,
                sessionManager,
                inventoryController,
                userController);

        uiController.showLoginScene();
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}