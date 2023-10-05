package com.jvavateam.carsharingapp.notification;

import com.jvavateam.carsharingapp.model.Rental;
import com.jvavateam.carsharingapp.model.User;
import com.jvavateam.carsharingapp.service.RentalService;
import com.jvavateam.carsharingapp.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ScheduledNotification {
    private static final String MANAGER_NO_OVERDUE_MESSAGE = "No rentals overdue today!";
    private static final String RENTAL_INFO_TEMPLATE = """
            📋 **Rental ID:** %d
            🚗 **Car:** %s
            📆 **Rental Date:** %s
            🔙 **Expected Return Date:** %s
            """;
    private static final String MANAGER_OVERDUE_MESSAGE_TEMPLATE = """
            🚨 *Action Required - Overdue Rental* 🚨
                    
            %s
                    
            This rental is overdue. Please contact the user and resolve this issue.
                    
            Cheers,
            JvavaCarSharing
            """;
    private static final String CLIENT_OVERDUE_MESSAGE_TEMPLATE = """
            📢 *Important Notice - Overdue Rental* 📢
                           
            Hello %s! 👋
                        
            %s
                        
            Your rental is overdue. Please return the car to avoid additional charges.
                        
            Best regards,
            JvavaCarSharing
            """;
    private final RentalService rentalService;
    private final UserService userService;
    private final NotificationService notificationService;

    //@Scheduled(cron = "0 0 16 * * *")
    @Scheduled(fixedRate = 50000)
    public void notifyUsersWithOutDateRentals() {
        List<User> managers = userService.findAllManagers();
        List<Rental> rentals = rentalService.getAllOverdueRentals();
        if (rentals.isEmpty()) {
            notificationService.notifyAll(managers, MANAGER_NO_OVERDUE_MESSAGE);
            return;
        }
        rentals.forEach(rental -> {
                    String rentalInfo = getRentalInfo(rental);
                    String managerMessage = String.format(
                            MANAGER_OVERDUE_MESSAGE_TEMPLATE,
                            rentalInfo
                    );
                    String clientOverdueMessage = String.format(
                            CLIENT_OVERDUE_MESSAGE_TEMPLATE,
                            rental.getUser().getFirstName(),
                            rentalInfo
                    );
                    notificationService.notifyAll(managers, managerMessage);
                    notificationService.sendMessage(rental.getUser(), clientOverdueMessage);
                }
        );
    }

    private String getRentalInfo(Rental rental) {
        String carInfo = rental.getCar().getModel() + " " + rental.getCar().getBrand();
        return String.format(
                RENTAL_INFO_TEMPLATE,
                rental.getId(),
                carInfo,
                rental.getRentalDate().toString(),
                rental.getReturnDate()
        );
    }
}
