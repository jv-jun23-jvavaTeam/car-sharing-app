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
    private final RentalService rentalService;
    private final UserService userService;
    private final NotificationService notificationService;

    @Scheduled(fixedRate = 100)
    public void notifyUsersWithOutDateRentals() {
        List<User> managers = userService.findAllManagers();
        List<Rental> rentals = rentalService.getAllOverdueRentals();
    }
}
