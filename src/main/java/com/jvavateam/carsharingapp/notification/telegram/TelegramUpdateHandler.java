package com.jvavateam.carsharingapp.notification.telegram;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface TelegramUpdateHandler {
    SendMessage handle(Message message);

    boolean isSupport(String text);
}
