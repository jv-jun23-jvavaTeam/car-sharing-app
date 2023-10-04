package com.jvavateam.carsharingapp.notification.telegram.handlers;

import com.jvavateam.carsharingapp.dto.subscription.SubscriptionDto;
import com.jvavateam.carsharingapp.model.Subscription;
import com.jvavateam.carsharingapp.service.SubscriptionService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@RequiredArgsConstructor
@Component
public class CreateSubscriptionHandler extends AbstractHandler {
    private static final String SUPPORT = "^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
    private static final String SUCCESS_RESPONSE =
            "You have successfully subscribed to notifications";
    private static final String FAIL_RESPONSE = "Sorry, we couldn't sign up for notifications";
    private final SubscriptionService subscriptionService;

    @Override
    public SendMessage handle(Message message) {
        SubscriptionDto subscriptionDto = new SubscriptionDto(
                message.getText(),
                message.getChatId());
        Optional<Subscription> subscription = subscriptionService.create(subscriptionDto);
        String responseMessage = subscription.isPresent() ? SUCCESS_RESPONSE : FAIL_RESPONSE;
        return SendMessage.builder()
                .chatId(message.getChatId())
                .text(responseMessage)
                .replyMarkup(getMainMenu(message))
                .build();
    }

    @Override
    public boolean isSupport(String text) {
        return text.matches(SUPPORT);
    }
}
