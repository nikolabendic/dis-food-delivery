package rs.ftn.dis.notificationservice.controller;

import rs.ftn.dis.notificationservice.model.Notification;
import rs.ftn.dis.notificationservice.store.NotificationStore;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NotificationControllerTest {

    @Mock
    private NotificationStore notificationStore;

    @InjectMocks
    private NotificationController notificationController;

    @Test
    void getAllNotifications_shouldReturnList() {
        Notification notification = new Notification(1L, 1L, "Porudžbina kreirana");
        when(notificationStore.getAll()).thenReturn(List.of(notification));

        ResponseEntity<List<Notification>> response = notificationController.getAllNotifications();

        assertEquals(200, response.getStatusCode().value());
        assertEquals(1, response.getBody().size());
        assertEquals("Porudžbina kreirana", response.getBody().get(0).getMessage());
    }

    @Test
    void getAllNotifications_whenEmpty_shouldReturnEmptyList() {
        when(notificationStore.getAll()).thenReturn(List.of());

        ResponseEntity<List<Notification>> response = notificationController.getAllNotifications();

        assertEquals(200, response.getStatusCode().value());
        assertTrue(response.getBody().isEmpty());
    }
}