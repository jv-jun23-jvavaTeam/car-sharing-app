package com.jvavateam.carsharingapp.service.impl;

import com.jvavateam.carsharingapp.dto.subscription.SubscriptionDto;
import com.jvavateam.carsharingapp.model.Subscription;
import com.jvavateam.carsharingapp.model.User;
import com.jvavateam.carsharingapp.repository.subsription.SubscriptionRepository;
import com.jvavateam.carsharingapp.repository.user.UserRepository;
import com.jvavateam.carsharingapp.service.SubscriptionService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class SubscriptionServiceImpl implements SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public Optional<Subscription> create(SubscriptionDto subscriptionDto) {
        Optional<User> user = userRepository
                .findByEmail(subscriptionDto.userEmail());
        return user.flatMap(value -> subscriptionRepository.findByChatId(subscriptionDto.chatId())
                .or(() -> createNew(subscriptionDto, value)));

    }

    @Override
    public Optional<Subscription> findByChatId(Long chatId) {
        return subscriptionRepository.findByChatId(chatId);
    }

    @Override
    @Transactional
    public void unsubscribeByChatId(Long chatId) {
        subscriptionRepository.deleteByChatId(chatId);
    }

    private Optional<Subscription> createNew(SubscriptionDto subscriptionDto, User value) {
        Subscription subscription = new Subscription(
                value,
                subscriptionDto.chatId()
        );
        return Optional.of(subscriptionRepository.save(subscription));
    }
}
