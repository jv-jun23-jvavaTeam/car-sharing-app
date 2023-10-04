package com.jvavateam.carsharingapp.notification.telegram.handlers;

import com.jvavateam.carsharingapp.notification.telegram.TelegramUpdateHandler;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class SubscribeHandler implements TelegramUpdateHandler {
    private static final String ENTER_YOUR_EMAIL = "Please, enter your email: ";
    private static final String SUPPORT = "Subscribe";

    @Override
    public SendMessage handle(Message message) {
        return SendMessage.builder()
                .chatId(message.getChatId())
                .text(ENTER_YOUR_EMAIL)
                .build();
    }

    @Override
    public boolean isSupport(String text) {
        return text.equals(SUPPORT);
    }
}
