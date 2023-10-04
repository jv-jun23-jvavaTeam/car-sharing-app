package com.jvavateam.carsharingapp.controller;

import com.jvavateam.carsharingapp.dto.rental.CreateRentalByManagerDto;
import com.jvavateam.carsharingapp.dto.rental.CreateRentalDto;
import com.jvavateam.carsharingapp.dto.rental.RentalResponseDto;
import com.jvavateam.carsharingapp.dto.rental.RentalReturnResponseDto;
import com.jvavateam.carsharingapp.dto.rental.RentalSearchParameters;
import com.jvavateam.carsharingapp.model.Car;
import com.jvavateam.carsharingapp.model.Rental;
import com.jvavateam.carsharingapp.model.User;
import jakarta.persistence.criteria.Predicate;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public class RentalControllerTest {
    private static final LocalDate RENTAL_DATE = LocalDate.of(2023, 10, 3);
    private static final LocalDate RETURN_DATE = LocalDate.of(2023, 10, 18);
    private static final LocalDate ACTUAL_RETURN_DATE = LocalDate.of(2023, 10, 18);
    private static final Long CAR_ID = 100L;
    private static final Long USER_ID = 100L;
    private static final Long RENTAL_ID = 100L;
    private static final Long SECOND_RENTAL_ID = 2L;

    private static final Car CAR = new Car()
            .setId(1L)
            .setModel("Toyota Camry")
            .setBrand("Toyota")
            .setInventory(11)
            .setDailyFee(new BigDecimal("80.00"))
            .setType(Car.Type.SEDAN)
            .setDeleted(false);
    private static final Car FOREIGN_KEY_CAR = new Car()
            .setId(CAR_ID)
            .setDeleted(false);
    private static final User FOREIGN_KEY_USER = new User()
            .setId(USER_ID)
            .setDeleted(false);

    static final CreateRentalDto REQUEST_CREATE_RENTAL_DTO = new CreateRentalDto(
            RENTAL_DATE,
            RETURN_DATE,
            CAR_ID
    );

    static final CreateRentalByManagerDto REQUEST_CREATE_RENTAL_BY_MANAGER_DTO =
            new CreateRentalByManagerDto(
                    RENTAL_DATE,
                    RETURN_DATE,
                    CAR_ID,
                    USER_ID
            );

    private static final Rental TOYOTA_RENTAL = new Rental()
            .setId(RENTAL_ID)
            .setRentalDate(RENTAL_DATE)
            .setReturnDate(RETURN_DATE)
            .setCar(FOREIGN_KEY_CAR)
            .setUser(FOREIGN_KEY_USER)
            .setActive(true);
    private static final Rental SECOND_RENTAL = new Rental()
            .setId(SECOND_RENTAL_ID)
            .setRentalDate(RENTAL_DATE)
            .setReturnDate(RETURN_DATE)
            .setCar(FOREIGN_KEY_CAR)
            .setUser(FOREIGN_KEY_USER)
            .setActive(true);
    private static final List<Rental> REPOSITORY_RENTALS =
            List.of(TOYOTA_RENTAL, SECOND_RENTAL);

    private static final RentalReturnResponseDto RENTAL_RETURN_RESPONSE_DTO =
            new RentalReturnResponseDto(
                    TOYOTA_RENTAL.getId(),
                    RENTAL_DATE,
                    RETURN_DATE,
                    ACTUAL_RETURN_DATE,
                    CAR_ID,
                    USER_ID,
                    false
            );

    private static final RentalResponseDto RESPONSE_CREATED_RENTAL_DTO = new RentalResponseDto(
            TOYOTA_RENTAL.getId(),
            RENTAL_DATE,
            RETURN_DATE,
            CAR_ID,
            USER_ID,
            TOYOTA_RENTAL.isActive()
    );

    private static final RentalResponseDto SECOND_RENTAL_DTO = new RentalResponseDto(
            SECOND_RENTAL.getId(),
            RENTAL_DATE,
            RETURN_DATE,
            CAR_ID,
            USER_ID,
            SECOND_RENTAL.isActive()
    );

    private static final List<RentalResponseDto> REPOSITORY_RENTALS_DTO =
            List.of(RESPONSE_CREATED_RENTAL_DTO, SECOND_RENTAL_DTO);
    private static final RentalSearchParameters SEARCH_PARAMS = new RentalSearchParameters(
            USER_ID,
            true
    );

    private static final Pageable DEFAULT_PAGEABLE = Pageable.ofSize(20);
    private static final Page<Rental> REPOSITORY_PAGE =
            new PageImpl<>(REPOSITORY_RENTALS, DEFAULT_PAGEABLE, REPOSITORY_RENTALS.size());
    private final Specification<Rental> SEARCH_SPECIFICATION = (root, query, criteriaBuilder) -> {
        Long userId = SEARCH_PARAMS.userId();
        boolean isActive = SEARCH_PARAMS.isActive();
        List<Predicate> predicates = new ArrayList<>();
        if (userId != null) {
            predicates.add(criteriaBuilder.equal(root.get("user").get("id"), userId));
        }
        if (isActive) {
            predicates.add(criteriaBuilder.isTrue(root.get("active")));
        }
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    };

    private static final Rental SAVED_RETURN_SECOND_RENTAL = new Rental()
            .setId(SECOND_RENTAL_ID)
            .setRentalDate(RENTAL_DATE)
            .setReturnDate(RETURN_DATE)
            .setActualReturnDate(LocalDate.now())
            .setCar(FOREIGN_KEY_CAR)
            .setUser(FOREIGN_KEY_USER)
            .setActive(false);
    private static final String ADD_TOYOTA_CAR =
            "classpath:database/car/add-100th-car-to-cars-table.sql";
    private static final String ADD_USER =
            "classpath:database/user/add-100th-user-to-users-table.sql";
    private static final String ADD_RENTAL =
            "classpath:database/rental/add-100th-rental-to-rentals-table.sql";
    private static final String CLEAR_RENTAL_TABLE =
            "classpath:database/rental/remove-100th-rental-from-rentals-table.sql.sql";
    private static final String CLEAR_USER_TABLE =
            "classpath:database/user/remove-100th-user-from-users-table.sql";
    private static final String CLEAR_CAR_TABLE =
            "classpath:database/car/remove-100th-car-from-cars-table.sql";
}
