package com.jvavateam.carsharingapp.notification.telegram;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface TelegramUpdateHandler {
    SendMessage handle(Update update);

    boolean isSupport(String text);
}
