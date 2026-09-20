package rs.ftn.dis.notificationservice.store;

import rs.ftn.dis.notificationservice.model.Notification;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class NotificationStore {

    private final List<Notification> notifications = new ArrayList<>();

    public void add(Notification notification) {
        notifications.add(notification);
    }

    public List<Notification> getAll() {
        return Collections.unmodifiableList(notifications);
    }
}