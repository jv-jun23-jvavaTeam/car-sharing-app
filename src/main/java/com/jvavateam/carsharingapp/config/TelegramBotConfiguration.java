package com.jvavateam.carsharingapp.config;

import com.jvavateam.carsharingapp.notification.TelegramBot;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Configuration
public class TelegramBotConfiguration {
    void init(@Value(value = "${telegram.bot.name}") String botUserName,
              @Value(value = "${telegram.bot.token}") String token) {
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(new TelegramBot(botUserName, token));
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
