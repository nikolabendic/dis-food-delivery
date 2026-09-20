package rs.ftn.dis.notificationservice.listener;

import rs.ftn.dis.notificationservice.dto.OrderCreatedEvent;
import rs.ftn.dis.notificationservice.store.NotificationStore;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OrderEventListenerTest {

    @Mock
    private NotificationStore notificationStore;

    @InjectMocks
    private OrderEventListener orderEventListener;

    @Test
    void handleOrderCreated_shouldSaveNotification() {
        OrderCreatedEvent event = new OrderCreatedEvent(5L, 2L, 1L);

        orderEventListener.handleOrderCreated(event);

        verify(notificationStore).add(any());
    }
}