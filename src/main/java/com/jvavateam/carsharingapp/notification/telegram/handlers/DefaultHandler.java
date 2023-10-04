package com.jvavateam.carsharingapp.notification.telegram.handlers;

import com.jvavateam.carsharingapp.model.Subscription;
import com.jvavateam.carsharingapp.notification.telegram.TelegramUpdateHandler;
import com.jvavateam.carsharingapp.service.SubscriptionService;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

@RequiredArgsConstructor
@Component
public class DefaultHandler implements TelegramUpdateHandler {
    private static final String START = "/start";
    private static final String UNSUBSCRIBE_LABEL = "Unsubscribe";
    private static final String SUBSCRIBE_LABEL = "Subscribe";
    private static final String CONTACT_INFO_LABEL = "Contact info";
    private final SubscriptionService subscriptionService;

    @Override
    public SendMessage handle(Message message) {
        Optional<Subscription> subscription = subscriptionService
                .findByChatId(message.getChatId());
        String label = subscription.isPresent() ? UNSUBSCRIBE_LABEL : SUBSCRIBE_LABEL;
        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        KeyboardRow row = new KeyboardRow();
        row.add(label);
        row.add(CONTACT_INFO_LABEL);
        markup.setKeyboard(List.of(row));
        markup.setResizeKeyboard(true);
        return SendMessage.builder()
                .chatId(message.getChatId())
                .text("Hello, " + message.getFrom().getFirstName())
                .replyMarkup(markup)
                .build();
    }

    @Override
    public boolean isSupport(String text) {
        return text.equals(START);
    }

}
