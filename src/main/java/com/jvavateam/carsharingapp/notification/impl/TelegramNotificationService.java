package com.jvavateam.carsharingapp.notification.impl;

import com.jvavateam.carsharingapp.model.User;
import com.jvavateam.carsharingapp.notification.NotificationService;
import com.jvavateam.carsharingapp.notification.TelegramBot;
import com.jvavateam.carsharingapp.repository.telegramsubsription.TelegramSubscriptionRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TelegramNotificationService implements NotificationService {
    private final TelegramBot bot;
    private final TelegramSubscriptionRepository subscriptionRepository;

    @Override
    public boolean sendMessage(User user, String message) {
        return subscriptionRepository.findById(user.getId())
                .map(subscription -> {
                    bot.sendMessage(subscription.getChatId(), message);
                    return true;
                })
                .orElse(false);
    }

    @Override
    public boolean notifyAll(List<User> users, String message) {
        List<Long> userIds = users.stream().map(User::getId).toList();
        return subscriptionRepository.findAllById(userIds)
                .stream()
                .peek(subscription -> bot.sendMessage(subscription.getChatId(), message))
                .map(obj -> true)
                .findFirst()
                .orElse(false);
    }
}
