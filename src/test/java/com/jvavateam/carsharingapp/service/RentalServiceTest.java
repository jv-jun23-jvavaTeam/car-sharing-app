package com.jvavateam.carsharingapp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import com.jvavateam.carsharingapp.dto.rental.CreateRentalByManagerDto;
import com.jvavateam.carsharingapp.dto.rental.CreateRentalDto;
import com.jvavateam.carsharingapp.dto.rental.RentalResponseDto;
import com.jvavateam.carsharingapp.mapper.rental.RentalMapper;
import com.jvavateam.carsharingapp.model.Car;
import com.jvavateam.carsharingapp.model.Rental;
import com.jvavateam.carsharingapp.model.User;
import com.jvavateam.carsharingapp.repository.rental.RentalRepository;
import com.jvavateam.carsharingapp.service.impl.RentalServiceImpl;
import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

@ExtendWith(MockitoExtension.class)
class RentalServiceTest {

    private static final LocalDate RENTAL_DATE = LocalDate.of(2023, 10, 3);
    private static final LocalDate RETURN_DATE = LocalDate.of(2023, 10, 18);
    private static final Long CAR_ID = 1L;
    private static final Long USER_ID = 1L;
    private static final Long RENTAL_ID = 1L;

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

    static final CreateRentalByManagerDto REQUEST_CREATE_RENTAL_BY_MANAGER_DTO = new CreateRentalByManagerDto(
            RENTAL_DATE,
            RETURN_DATE,
            CAR_ID,
            USER_ID

    );

    private static final Rental CREATED_RENTAL = new Rental()
            .setId(RENTAL_ID)
            .setRentalDate(RENTAL_DATE)
            .setReturnDate(RETURN_DATE)
            .setCar(FOREIGN_KEY_CAR)
            .setUser(FOREIGN_KEY_USER)
            .setActive(true);

    private static final RentalResponseDto RESPONSE_CREATED_RENTAL_DTO = new RentalResponseDto(
            CREATED_RENTAL.getId(),
            RENTAL_DATE,
            RETURN_DATE,
            CAR_ID,
            USER_ID,
            CREATED_RENTAL.isActive()
    );

    @Mock
    private RentalMapper rentalMapper;
    @Mock
    private CarService carService;
    @Mock
    private RentalRepository rentalRepository;
    @InjectMocks
    private RentalServiceImpl rentalService;

    @Test
    @DisplayName("Verify successful creating rental with a valid input")
    void create_validRentalParameters_shouldReturnRentalDto() {
        when(rentalMapper.toModel(REQUEST_CREATE_RENTAL_DTO)).thenReturn(CREATED_RENTAL);
        when(rentalRepository.save(CREATED_RENTAL)).thenReturn(CREATED_RENTAL);
        when(rentalMapper.toDto(CREATED_RENTAL)).thenReturn(RESPONSE_CREATED_RENTAL_DTO);
        when(carService.findById(CAR_ID)).thenReturn(CAR);

        RentalResponseDto actual = rentalService.create(REQUEST_CREATE_RENTAL_DTO);
        assertEquals(RESPONSE_CREATED_RENTAL_DTO, actual);
    }

    @Test
    @DisplayName("Verify successful creating rental with a valid input by Manager")
    void createByManager_validRentalParameters_shouldReturnRentalDto() {
        when(rentalMapper.toModel(REQUEST_CREATE_RENTAL_BY_MANAGER_DTO)).thenReturn(CREATED_RENTAL);
        when(rentalRepository.save(CREATED_RENTAL)).thenReturn(CREATED_RENTAL);
        when(rentalMapper.toDto(CREATED_RENTAL)).thenReturn(RESPONSE_CREATED_RENTAL_DTO);
        when(carService.findById(CAR_ID)).thenReturn(CAR);

        RentalResponseDto actual = rentalService.createByManager(REQUEST_CREATE_RENTAL_BY_MANAGER_DTO);
        assertEquals(RESPONSE_CREATED_RENTAL_DTO, actual);
    }

}
