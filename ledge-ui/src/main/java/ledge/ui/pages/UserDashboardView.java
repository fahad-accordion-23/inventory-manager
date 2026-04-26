package ledge.ui.pages;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import ledge.api.shared.ApiResponse;
import ledge.api.shared.AuthContext;
import ledge.ui.clients.HttpUserClient;
import ledge.api.users.dto.response.GetAllUsersResponseDTO;
import ledge.api.users.dto.UserResponseDTO;
import ledge.ui.core.Capability;
import ledge.ui.core.SessionManager;
import ledge.ui.viewmodels.UserViewModel;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * Controller for the user management table view via the API layer.
 */
public class UserDashboardView {

    @FXML
    private TableView<UserViewModel> usersTable;

    @FXML
    private TableColumn<UserViewModel, Void> actionsColumn;

    @FXML
    private Button addUserBtn;

    private final HttpUserClient userController;
    private final SessionManager sessionManager;
    private final Runnable onAddRequested;
    private final Consumer<UserResponseDTO> onEditRequested;

    private final ObservableList<UserViewModel> allUsers = FXCollections.observableArrayList();

    public UserDashboardView(HttpUserClient userController, SessionManager sessionManager,
            Runnable onAddRequested, Consumer<UserResponseDTO> onEditRequested) {
        this.userController = userController;
        this.sessionManager = sessionManager;
        this.onAddRequested = onAddRequested;
        this.onEditRequested = onEditRequested;
    }

    @FXML
    public void initialize() {
        usersTable.setItems(allUsers);

        setupActionsColumn();
        loadAllUsers();

        addUserBtn.setVisible(sessionManager.isAllowed(Capability.CREATE_USER));
    }

    @FXML
    public void handleAddUser() {
        onAddRequested.run();
    }

    @FXML
    public void handleRefresh() {
        loadAllUsers();
    }

    private void loadAllUsers() {
        Optional<AuthContext> authContext = sessionManager.getAuthContext();
        if (authContext.isEmpty())
            return;

        ApiResponse<GetAllUsersResponseDTO> response = userController.getAllUsers(authContext.get());
        if (response.success()) {
            allUsers.setAll(response.data().users().stream().map(UserViewModel::new).toList());
        }
    }

    private void setupActionsColumn() {
        boolean canEdit = sessionManager.isAllowed(Capability.EDIT_USER);
        boolean canDelete = sessionManager.isAllowed(Capability.DELETE_USER);

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
                    UserViewModel user = getTableView().getItems().get(getIndex());
                    onEditRequested.accept(user.toDTO());
                });
                deleteBtn.setOnAction(_ -> {
                    UserViewModel user = getTableView().getItems().get(getIndex());
                    confirmAndDelete(user);
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

    private void confirmAndDelete(UserViewModel user) {
        if (sessionManager.getCurrentUser().isPresent() &&
                sessionManager.getCurrentUser().get().id().equals(user.getId())) {
            new Alert(Alert.AlertType.WARNING, "You cannot delete your own account.").showAndWait();
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Deletion");
        confirm.setHeaderText("Delete user \"" + user.getUsername() + "\"?");
        confirm.setContentText("This action cannot be undone.");

        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Optional<AuthContext> authContext = sessionManager.getAuthContext();
            if (authContext.isEmpty())
                return;

            ApiResponse<Void> response = userController.deleteUser(authContext.get(), user.getId());

            if (response.success()) {
                loadAllUsers();
            } else {
                new Alert(Alert.AlertType.ERROR, response.error().message()).showAndWait();
            }
        }
    }
}
