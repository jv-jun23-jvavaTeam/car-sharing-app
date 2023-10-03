package com.jvavateam.carsharingapp.notification;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class TelegramBot extends TelegramLongPollingBot {
    private final String botUserName;

    public TelegramBot(String botUserName, String token) {
        super(token);
        this.botUserName = botUserName;
    }

    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        if (update.hasMessage() && message.hasText()) {
            Long chatId = message.getChatId();
            sendMessage(chatId, "Hello!");
        }
    }

    @Override
    public String getBotUsername() {
        return botUserName;
    }

    public void sendMessage(Long chatId, String message) {
        try {
            execute(
                    SendMessage.builder()
                            .chatId(chatId)
                            .text(message)
                            .build());
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
