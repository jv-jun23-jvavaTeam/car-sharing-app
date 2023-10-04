package com.jvavateam.carsharingapp.notification.telegram;

import com.jvavateam.carsharingapp.exception.TelegramBotException;
import com.jvavateam.carsharingapp.model.Subscription;
import com.jvavateam.carsharingapp.model.User;
import com.jvavateam.carsharingapp.repository.subsription.SubscriptionRepository;
import com.jvavateam.carsharingapp.repository.user.UserRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class TelegramBotService extends TelegramLongPollingBot {
    private static final String EMAIL_REGEX = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
    public static final String UNSUBSCRIBED_RESPONCE = "Yor unsubscribed from Car Sharing notification";
    public static final String ENTER_YOUR_EMAIL = "Please, enter your email: ";
    private final String botUserName;
    private final UserRepository userRepository;
    private final SubscriptionRepository subscriptionRepository;


    public TelegramBotService(
            @Value(value = "${telegram.bot.name}") String botUserName,
            @Value(value = "${telegram.bot.token}") String token,
            @Autowired UserRepository userRepository,
            @Autowired SubscriptionRepository subscriptionRepository
    ) {
        super(token);
        this.botUserName = botUserName;
        this.userRepository = userRepository;
        this.subscriptionRepository = subscriptionRepository;
    }

    @Override
    public void onUpdateReceived(Update update) {
        Message request = update.getMessage();
        if (update.hasMessage() && request.hasText()) {
            switch (request.getText()) {
                case "Subscribe" -> sendMessage(request.getChatId(), ENTER_YOUR_EMAIL);
                case "Unsubscribe" -> deleteSubscription(request);
                default -> {
                    if (request.getText().matches(EMAIL_REGEX)) {
                        createSubscription(request);
                    } else {
                        sendMessage(getMainMenu(request));
                    }

                }
            }
        }
    }

    @Override
    public String getBotUsername() {
        return botUserName;
    }

    public void sendMessage(Long chatId, String message) {
        sendMessage(SendMessage.builder()
                .chatId(chatId)
                .text(message)
                .build());
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

    private SendMessage getMainMenu(Message message) {
        Optional<Subscription> subscription = subscriptionRepository
                .findByChatId(message.getChatId());
        String label = subscription.isPresent() ? "Unsubscribe" : "Subscribe";
        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        KeyboardRow row = new KeyboardRow();
        row.add(label);
        row.add("Contact info");
        markup.setKeyboard(List.of(row));
        markup.setResizeKeyboard(true);
        return SendMessage.builder()
                .chatId(message.getChatId())
                .text("Hello, " + message.getFrom().getFirstName())
                .replyMarkup(markup)
                .build();
    }

    private void deleteSubscription(Message message) {
        subscriptionRepository.deleteByChatId(message.getChatId());
        sendMessage(message.getChatId(), UNSUBSCRIBED_RESPONCE);
    }

    private void createSubscription(Message message) {
        Optional<Subscription> subscription = subscriptionRepository.findByChatId(message.getChatId());
        if (subscription.isPresent()) {
            sendMessage(
                    message.getChatId(),
                    "You have already subscribed for notification"
            );
            return;
        }
        Optional<User> user = userRepository.findByEmail(message.getText());
        if (user.isPresent()) {
            Subscription telegramSubscription = new Subscription(
                    user.get(),
                    message.getChatId()
            );
            subscriptionRepository.save(telegramSubscription);
            sendMessage(
                    message.getChatId(),
                    "You successfully subscribed to Car Sharing notification"
            );
        } else {
            sendMessage(message.getChatId(),
                    "We can't find User with email: " + message.getText()
            );
        }
    }
}
