package com.jvavateam.carsharingapp.config;

import com.jvavateam.carsharingapp.exception.TelegramBotException;
import com.jvavateam.carsharingapp.notification.telegram.TelegramBotService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.BotSession;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@RequiredArgsConstructor
@Configuration
public class TelegramBotConfiguration {
    private final TelegramBotService telegramBotService;

    @Bean
    public BotSession telegramBotSession() {
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            return telegramBotsApi.registerBot(telegramBotService);
        } catch (TelegramApiException e) {
            throw new TelegramBotException(
                    "Telegram bot API registration failed",
                    e);
        }
    }
}
