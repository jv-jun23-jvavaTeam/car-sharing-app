package com.jvavateam.carsharingapp.notification.telegram.handlers;

import com.jvavateam.carsharingapp.notification.telegram.TelegramUpdateHandler;
import com.jvavateam.carsharingapp.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@RequiredArgsConstructor
@Component
public class UnsubscribeHandler implements TelegramUpdateHandler {
    private static final String SUCCESS_RESPONSE =
            "You have successfully unsubscribed from notifications";
    private static final String SUPPORT = "Unsubscribe";
    private final SubscriptionService subscriptionService;

    @Override
    public SendMessage handle(Message message) {
        subscriptionService.unsubscribeByChatId(message.getChatId());
        return SendMessage.builder()
                .chatId(message.getChatId())
                .text(SUCCESS_RESPONSE)
                .build();
    }

    @Override
    public boolean isSupport(String text) {
        return text.equals(SUPPORT);
    }
}
