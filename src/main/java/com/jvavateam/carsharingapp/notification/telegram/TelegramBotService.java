package com.jvavateam.carsharingapp.notification.telegram;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Component
public class TelegramBotService extends TelegramLongPollingBot {
    private static final Logger logger = LoggerFactory.getLogger(TelegramBotService.class);
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

    @PostConstruct
    public void init() {
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(this);
        } catch (TelegramApiException e) {
            logger.error("Telegram bot API registration failed", e);
        }
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
            logger.error(
                    "Can't send message to chatId "
                    + sendMessage.getChatId(),
                    e);
        }
    }
}
