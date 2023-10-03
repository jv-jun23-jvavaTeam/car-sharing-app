package com.jvavateam.carsharingapp.notification.impl;

import com.jvavateam.carsharingapp.model.User;
import com.jvavateam.carsharingapp.notification.NotificationService;
import com.jvavateam.carsharingapp.notification.TelegramBot;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TelegramNotificationService implements NotificationService {
    private final TelegramBot telegramBot;

    @Override
    public boolean sendMessage(User user, String message) {
        return false;
    }

    @Override
    public boolean notifyAll(List<User> users, String message) {
        return false;
    }
}
