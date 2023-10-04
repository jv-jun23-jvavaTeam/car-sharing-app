package com.jvavateam.carsharingapp.notification.telegram;

import com.jvavateam.carsharingapp.exception.TelegramBotException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class TelegramBotService extends TelegramLongPollingBot {
    private final TelegramHandlerManager handlerManager;
    private final String botUserName;

    public TelegramBotService(
            @Value(value = "${telegram.bot.name}") String botUserName,
            @Value(value = "${telegram.bot.token}") String token,
            @Autowired TelegramHandlerManager handlerManager
    ) {
        super(token);
        this.botUserName = botUserName;
        this.handlerManager = handlerManager;
    }

    @Override
    public void onUpdateReceived(Update update) {
        Message request = update.getMessage();
        if (update.hasMessage() && request.hasText()) {
            SendMessage response = handlerManager.handleUpdate(update);
            sendMessage(response);
        }
    }

    @Override
    public String getBotUsername() {
        return botUserName;
    }

    public void sendMessage(Long chatId, String message) {
        sendMessage(
                SendMessage.builder()
                        .chatId(chatId)
                        .text(message)
                        .build()
        );
    }

    private void sendMessage(SendMessage sendMessage) {
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new TelegramBotException(
                    "Can't send message to chatId "
                            + sendMessage.getChatId(),
                    e
            );
        }
    }
}
