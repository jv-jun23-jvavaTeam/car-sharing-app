package com.jvavateam.carsharingapp.notification;

import com.jvavateam.carsharingapp.model.User;
import java.util.List;

public interface NotificationService {
    boolean sendMessage(User user, String message);

    boolean notifyAll(List<User> users, String message);
}
