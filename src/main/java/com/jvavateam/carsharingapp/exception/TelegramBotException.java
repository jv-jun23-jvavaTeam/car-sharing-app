package com.jvavateam.carsharingapp.exception;

public class TelegramBotException extends RuntimeException {
    public TelegramBotException(String message, Throwable cause) {
        super(message, cause);
    }

    public TelegramBotException(String message) {
        super(message);
    }
}
