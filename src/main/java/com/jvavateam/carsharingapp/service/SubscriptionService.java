package com.jvavateam.carsharingapp.service;

import com.jvavateam.carsharingapp.dto.subscription.SubscriptionDto;
import com.jvavateam.carsharingapp.model.Subscription;
import java.util.Optional;

public interface SubscriptionService {
    Optional<Subscription> create(SubscriptionDto subscriptionDto);

    Optional<Subscription> findByChatId(Long chatId);

    void unsubscribeByChatId(Long chatId);
}
