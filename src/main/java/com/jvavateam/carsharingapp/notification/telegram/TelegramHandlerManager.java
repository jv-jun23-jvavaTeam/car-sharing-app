package com.jvavateam.carsharingapp.notification.telegram;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@RequiredArgsConstructor
@Component
public class TelegramHandlerManager {
    public static final String DEFAULT_MESSAGE = "Have a nice day!";
    private final List<TelegramUpdateHandler> handlers;

    public SendMessage handleUpdate(Update update) {
        Message message = update.getMessage();
        return handlers.stream()
                .filter(handler -> handler.isSupport(message.getText()))
                .map(handler -> handler.handle(update))
                .findFirst()
                .orElseGet(
                        () -> SendMessage.builder()
                                .chatId(message.getChatId())
                                .text(DEFAULT_MESSAGE)
                                .build());
    }
}
