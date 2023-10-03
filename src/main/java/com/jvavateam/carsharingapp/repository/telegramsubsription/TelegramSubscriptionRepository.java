package com.jvavateam.carsharingapp.repository.telegramsubsription;

import com.jvavateam.carsharingapp.model.TelegramSubscription;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TelegramSubscriptionRepository
        extends JpaRepository<TelegramSubscription, Long> {

}
