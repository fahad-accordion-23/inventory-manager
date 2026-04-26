package ledge.users.writemodel.application.handlers;

import ledge.users.events.domain.UserCreatedDomainEvent;
import ledge.users.events.integration.UserRegisteredIntegrationEvent;
import ledge.users.writemodel.commands.AddUserCommand;
import ledge.users.writemodel.domain.User;
import ledge.users.writemodel.infrastructure.IUserWriteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.context.ApplicationEventPublisher;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AddUserCommandHandlerTest {

    private AddUserCommandHandler handler;
    private IUserWriteRepository writeRepository;
    private ApplicationEventPublisher eventPublisher;

    @BeforeEach
    void setUp() {
        writeRepository = mock(IUserWriteRepository.class);
        eventPublisher = mock(ApplicationEventPublisher.class);
        handler = new AddUserCommandHandler(writeRepository, eventPublisher);
    }

    @Test
    void testHandleAddUser() {
        AddUserCommand command = new AddUserCommand("newuser", "password123");

        handler.handle(command);

        // Verify write repository interaction
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(writeRepository).save(userCaptor.capture());
        
        User savedUser = userCaptor.getValue();
        assertEquals("newuser", savedUser.getUsername());

        // Verify domain event publication
        verify(eventPublisher).publishEvent(any(UserCreatedDomainEvent.class));

        // Verify integration event publication
        verify(eventPublisher).publishEvent(any(UserRegisteredIntegrationEvent.class));
    }
}
