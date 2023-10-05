package com.jvavateam.carsharingapp.repository.subsription;

import com.jvavateam.carsharingapp.model.Subscription;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionRepository
        extends JpaRepository<Subscription, Long> {
    Optional<Subscription> findByChatId(Long chatId);

    void deleteByChatId(Long chatId);
}
