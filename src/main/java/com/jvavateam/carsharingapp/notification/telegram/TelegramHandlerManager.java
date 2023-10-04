package com.jvavateam.carsharingapp.notification.telegram;

import com.jvavateam.carsharingapp.notification.telegram.handlers.DefaultHandler;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@RequiredArgsConstructor
@Component
public class TelegramHandlerManager {
    private final DefaultHandler defaultHandler;
    private final List<TelegramUpdateHandler> handlers;

    public SendMessage handleUpdate(Update update) {
        Message message = update.getMessage();
        return handlers.stream()
                .filter(handler -> handler.isSupport(message.getText()))
                .map(handler -> handler.handle(message))
                .findFirst()
                .orElseGet(() -> defaultHandler.handle(message));
    }
}
