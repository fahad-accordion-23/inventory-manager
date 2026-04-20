package ledge.ui;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ledge.inventory.infrastructure.messaging.InventoryCommandBus;
import ledge.inventory.infrastructure.messaging.InventoryEventBroker;
import ledge.inventory.infrastructure.messaging.InventoryQueryBus;
import ledge.users.infrastructure.messaging.UserCommandBus;
import ledge.users.infrastructure.messaging.UserQueryBus;
import ledge.ui.events.LoginSucceededEvent;
import ledge.ui.events.NavigateToLoginEvent;
import ledge.ui.events.NavigateToMainEvent;
import ledge.ui.events.UserLoggedOutEvent;
import ledge.ui.messaging.UIEventBroker;
import ledge.ui.views.LoginView;
import ledge.ui.views.MainLayout;
import ledge.ui.views.Sidebar;
import ledge.util.event.Subscribe;

/**
 * Event-driven controller for coordinating UI states and transitions.
 * Listens for navigation and session events to manage scene changes.
 */
public class UIController {
    private final Stage primaryStage;
    private final UIEventBroker uiEventBroker;
    private final InventoryEventBroker inventoryEventBroker;
    private final InventoryCommandBus commandBus;
    private final InventoryQueryBus queryBus;
    private final UserCommandBus userCommandBus;
    private final UserQueryBus userQueryBus;
    private final SessionManager sessionManager;

    public UIController(Stage primaryStage, UIEventBroker uiEventBroker,
            InventoryEventBroker inventoryEventBroker,
            InventoryCommandBus commandBus,
            InventoryQueryBus queryBus,
            UserCommandBus userCommandBus,
            UserQueryBus userQueryBus,
            SessionManager sessionManager) {
        this.primaryStage = primaryStage;
        this.uiEventBroker = uiEventBroker;
        this.inventoryEventBroker = inventoryEventBroker;
        this.commandBus = commandBus;
        this.queryBus = queryBus;
        this.userCommandBus = userCommandBus;
        this.userQueryBus = userQueryBus;
        this.sessionManager = sessionManager;

        this.uiEventBroker.register(this);
    }

    @Subscribe
    public void onNavigateToLogin(NavigateToLoginEvent event) {
        Platform.runLater(this::showLoginScene);
    }

    @Subscribe
    public void onNavigateToMain(NavigateToMainEvent event) {
        Platform.runLater(this::showMainScene);
    }

    @Subscribe
    public void onLoginSucceeded(LoginSucceededEvent event) {
        Platform.runLater(this::showMainScene);
    }

    @Subscribe
    public void onUserLoggedOut(UserLoggedOutEvent event) {
        Platform.runLater(this::showLoginScene);
    }

    /**
     * Loads and displays the login scene.
     */
    public void showLoginScene() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ledge/ui/LoginView.fxml"));
            loader.setControllerFactory(param -> {
                if (param == LoginView.class) {
                    return new LoginView(sessionManager, uiEventBroker);
                }
                try {
                    return param.getDeclaredConstructor().newInstance();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
            Parent root = loader.load();
            primaryStage.setScene(new Scene(root, 800, 600));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads and displays the main dashboard scene.
     */
    public void showMainScene() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ledge/ui/MainLayout.fxml"));
            loader.setControllerFactory(param -> {
                if (param == MainLayout.class) {
                    return new MainLayout(inventoryEventBroker, commandBus, queryBus, sessionManager, uiEventBroker);
                }
                if (param == Sidebar.class) {
                    return new Sidebar(sessionManager, uiEventBroker);
                }
                try {
                    return param.getDeclaredConstructor().newInstance();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
            Parent root = loader.load();
            primaryStage.setScene(new Scene(root, 800, 600));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
