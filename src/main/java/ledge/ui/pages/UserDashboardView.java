package ledge.ui.pages;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import ledge.shared.types.Role;
import ledge.ui.core.Capability;
import ledge.ui.core.SessionManager;
import ledge.ui.viewmodels.UserViewModel;
import ledge.users.application.commands.RemoveUserCommand;
import ledge.users.application.dtos.UserDTO;
import ledge.users.application.events.UsersUpdatedEvent;
import ledge.users.application.query.GetAllUsersQuery;
import ledge.users.infrastructure.messaging.UserCommandBus;
import ledge.users.infrastructure.messaging.UserEventBroker;
import ledge.users.infrastructure.messaging.UserQueryBus;
import ledge.util.event.Subscribe;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class UserDashboardView {

    @FXML
    private TableView<UserViewModel> usersTable;

    @FXML
    private TableColumn<UserViewModel, Void> actionsColumn;

    @FXML
    private Button addUserBtn;

    private final UserEventBroker eventBroker;
    private final UserCommandBus commandBus;
    private final UserQueryBus queryBus;
    private final SessionManager sessionManager;
    private final Runnable onAddRequested;
    private final Consumer<UserDTO> onEditRequested;

    private final ObservableList<UserViewModel> allUsers = FXCollections.observableArrayList();

    public UserDashboardView(UserEventBroker eventBroker, UserCommandBus commandBus,
            UserQueryBus queryBus, SessionManager sessionManager,
            Runnable onAddRequested, Consumer<UserDTO> onEditRequested) {
        this.eventBroker = eventBroker;
        this.commandBus = commandBus;
        this.queryBus = queryBus;
        this.sessionManager = sessionManager;
        this.onAddRequested = onAddRequested;
        this.onEditRequested = onEditRequested;
        this.eventBroker.register(this);
    }

    @FXML
    public void initialize() {
        usersTable.setItems(allUsers);
        
        // Setup table columns (Username, Role)
        // ... handled in FXML mostly but actions column needs code
        
        setupActionsColumn();
        loadAllUsers();
        
        // Only admins or those with CREATE_USER capability can see Add User
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
        try {
            String token = sessionManager.getAuthToken().orElse("");
            List<UserViewModel> viewModels = queryBus.dispatch(new GetAllUsersQuery(), token)
                    .stream()
                    .map(UserViewModel::new)
                    .toList();
            allUsers.setAll(viewModels);
        } catch (Exception e) {
            e.printStackTrace();
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

    private void confirmAndDelete(UserViewModel user) {
        // Don't allow deleting self? (Good practice)
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
            try {
                String token = sessionManager.getAuthToken().orElse("");
                commandBus.dispatch(new RemoveUserCommand(user.getId()), token);
            } catch (Exception e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage()).showAndWait();
            }
        }
    }

    @Subscribe
    private void handleUsersUpdated(UsersUpdatedEvent event) {
        Platform.runLater(this::loadAllUsers);
    }
}
