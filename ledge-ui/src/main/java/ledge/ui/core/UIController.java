package ledge.ui.core;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import ledge.api.inventory.InventoryController;
import ledge.api.users.UserController;
import ledge.ui.events.LoginSucceededEvent;
import ledge.ui.events.NavigateToLoginEvent;
import ledge.ui.events.NavigateToMainEvent;
import ledge.ui.events.UserLoggedOutEvent;
import ledge.ui.messaging.UIEventBroker;
import ledge.ui.pages.LoginView;
import ledge.ui.components.MainLayout;
import ledge.ui.components.Sidebar;
import ledge.shared.infrastructure.events.Subscribe;

/**
 * Event-driven controller for coordinating UI states and transitions.
 * Listens for navigation and session events to manage scene changes.
 */
public class UIController {
    private final Stage primaryStage;
    private final UIEventBroker uiEventBroker;
    private final SessionManager sessionManager;
    private final InventoryController inventoryController;
    private final UserController userController;

    public UIController(Stage primaryStage, UIEventBroker uiEventBroker, SessionManager sessionManager,
            InventoryController inventoryController, UserController userController) {
        this.primaryStage = primaryStage;
        this.uiEventBroker = uiEventBroker;
        this.sessionManager = sessionManager;
        this.inventoryController = inventoryController;
        this.userController = userController;

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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ledge/ui/pages/LoginView.fxml"));
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ledge/ui/components/MainLayout.fxml"));
            loader.setControllerFactory(param -> {
                if (param == MainLayout.class) {
                    return new MainLayout(inventoryController, userController, sessionManager, uiEventBroker);
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
