package com.jvavateam.carsharingapp.notification.telegram.handlers;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class AboutUsHandler extends AbstractHandler {
    private static final String SUPPORT = "About us";
    private static final String CONTACT_INFO = """
            🚗 JVAVA TEAM CAR SHARING SERVICE
            📧 Email: jvavateam@gmail.com

            Members:

            👨‍💼 Oleksandr Parkhomovskyi
            👩‍💼 Sofiya Kostashchuk
            👨‍💼 Artem Reizer
            👨‍💼 Ivan Fadieiev
            👨‍💼 Dmytro Martyshchuk

            Feel free to reach out to any of our team members! 📩🤝🚗
            """;

    @Override
    public SendMessage handle(Message message) {
        return SendMessage.builder()
                .chatId(message.getChatId())
                .text(CONTACT_INFO)
                .replyMarkup(getMainMenu(message))
                .build();
    }

    @Override
    public boolean isSupport(String text) {
        return text.equals(SUPPORT);
    }
}
