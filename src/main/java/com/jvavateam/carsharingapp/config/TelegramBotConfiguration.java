package com.jvavateam.carsharingapp.config;

import com.jvavateam.carsharingapp.notification.telegram.TelegramBotService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.BotSession;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@RequiredArgsConstructor
@Configuration
public class TelegramBotConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(TelegramBotConfiguration.class);
    private final TelegramBotService telegramBotService;

    @Bean
    public BotSession telegramBotSession() {
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            return telegramBotsApi.registerBot(telegramBotService);
        } catch (TelegramApiException e) {
            logger.error("Telegram bot API registration failed", e);
        }
        return null;
    }
}
