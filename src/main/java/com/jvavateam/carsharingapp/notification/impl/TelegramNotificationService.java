package com.jvavateam.carsharingapp.notification.impl;

import com.jvavateam.carsharingapp.model.Subscription;
import com.jvavateam.carsharingapp.model.User;
import com.jvavateam.carsharingapp.notification.NotificationService;
import com.jvavateam.carsharingapp.notification.telegram.TelegramBotService;
import com.jvavateam.carsharingapp.repository.subsription.SubscriptionRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TelegramNotificationService implements NotificationService {
    private final TelegramBotService bot;
    private final SubscriptionRepository subscriptionRepository;

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
        List<Subscription> subscriptions = subscriptionRepository.findAllById(userIds);
        subscriptions.forEach(
                subscription -> bot.sendMessage(subscription.getChatId(), message)
        );
        return !subscriptions.isEmpty();
    }
}
