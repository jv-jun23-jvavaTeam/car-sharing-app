package com.jvavateam.carsharingapp.config;

import com.jvavateam.carsharingapp.notification.TelegramBot;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Configuration
public class TelegramBotConfiguration {

    @Bean
    TelegramBot telegramBot(@Value(value = "${telegram.bot.name}") String botUserName,
                            @Value(value = "${telegram.bot.token}") String token) {
        try {
            TelegramBot telegramBot = new TelegramBot(botUserName, token);
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(telegramBot);
            return telegramBot;
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
