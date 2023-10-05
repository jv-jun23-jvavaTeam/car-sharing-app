package com.jvavateam.carsharingapp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.jvavateam.carsharingapp.dto.rental.CreateRentalByManagerDto;
import com.jvavateam.carsharingapp.dto.rental.CreateRentalDto;
import com.jvavateam.carsharingapp.dto.rental.RentalResponseDto;
import com.jvavateam.carsharingapp.dto.rental.RentalReturnResponseDto;
import com.jvavateam.carsharingapp.dto.rental.RentalSearchParameters;
import com.jvavateam.carsharingapp.exception.EntityNotFoundException;
import com.jvavateam.carsharingapp.mapper.rental.RentalMapper;
import com.jvavateam.carsharingapp.model.Car;
import com.jvavateam.carsharingapp.model.Rental;
import com.jvavateam.carsharingapp.model.User;
import com.jvavateam.carsharingapp.repository.rental.RentalRepository;
import com.jvavateam.carsharingapp.repository.rental.RentalSpecificationBuilder;
import com.jvavateam.carsharingapp.service.impl.RentalServiceImpl;
import jakarta.persistence.criteria.Predicate;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
class RentalServiceTest {

    private static final LocalDate RENTAL_DATE = LocalDate.of(2023, 10, 3);
    private static final LocalDate RETURN_DATE = LocalDate.of(2023, 10, 18);
    private static final LocalDate ACTUAL_RETURN_DATE = LocalDate.of(2023, 10, 18);
    private static final Long CAR_ID = 1L;
    private static final Long USER_ID = 1L;
    private static final Long RENTAL_ID = 1L;
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

    private static final Rental CREATED_RENTAL = new Rental()
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
            List.of(CREATED_RENTAL, SECOND_RENTAL);

    private static final RentalReturnResponseDto RENTAL_RETURN_RESPONSE_DTO =
            new RentalReturnResponseDto(
                    CREATED_RENTAL.getId(),
                    RENTAL_DATE,
                    RETURN_DATE,
                    ACTUAL_RETURN_DATE,
                    CAR_ID,
                    USER_ID,
                    false
            );

    private static final RentalResponseDto RESPONSE_CREATED_RENTAL_DTO = new RentalResponseDto(
            CREATED_RENTAL.getId(),
            RENTAL_DATE,
            RETURN_DATE,
            CAR_ID,
            USER_ID,
            CREATED_RENTAL.isActive()
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
    private static final Specification<Rental> SEARCH_SPECIFICATION =
            (root, query, criteriaBuilder) -> {
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
    private static final User USER = new User()
                .setId(100L)
                .setEmail("wylo@ua.com")
                .setPassword("$2a$12$2gWx8fCmINQ1EZ9cNrMG0.uNl7d63gmb/zTwj6yCdgsPXn5WD4tcW")
                .setFirstName("Oleh")
                .setLastName("Lyashko");
    @Mock
    private RentalSpecificationBuilder rentalSpecificationBuilder;
    @Mock
    private RentalMapper rentalMapper;
    @Mock
    private CarService carService;
    @Mock
    private UserService userService;
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
        when(userService.getAuthentificatedUser()).thenReturn(USER);
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

        RentalResponseDto actual =
                rentalService.createByManager(REQUEST_CREATE_RENTAL_BY_MANAGER_DTO);
        assertEquals(RESPONSE_CREATED_RENTAL_DTO, actual);
    }

    @Test
    @DisplayName("Verify get all rentals for specific user by Manager")
    void getAllByManager_validSearchSpecification_shouldReturnRentalDtos() {
        when(rentalSpecificationBuilder.build(SEARCH_PARAMS)).thenReturn(SEARCH_SPECIFICATION);
        when(rentalRepository.findAll(SEARCH_SPECIFICATION, DEFAULT_PAGEABLE))
                .thenReturn(REPOSITORY_PAGE);
        when(rentalMapper.toDto(CREATED_RENTAL)).thenReturn(RESPONSE_CREATED_RENTAL_DTO);
        when(rentalMapper.toDto(SECOND_RENTAL)).thenReturn(SECOND_RENTAL_DTO);

        List<RentalResponseDto> actual =
                rentalService.getAllByManager(SEARCH_PARAMS, DEFAULT_PAGEABLE);
        assertEquals(REPOSITORY_RENTALS_DTO, actual);
    }

    @Test
    @DisplayName("Verify get all rentals for specific user")
    void getAll_validUser_shouldReturnRentalDtos() {
        when(rentalRepository.findAll(DEFAULT_PAGEABLE)).thenReturn(REPOSITORY_PAGE);
        when(rentalMapper.toDto(CREATED_RENTAL)).thenReturn(RESPONSE_CREATED_RENTAL_DTO);
        when(rentalMapper.toDto(SECOND_RENTAL)).thenReturn(SECOND_RENTAL_DTO);

        List<RentalResponseDto> actual = rentalService.getAll(DEFAULT_PAGEABLE);
        assertEquals(REPOSITORY_RENTALS_DTO, actual);
    }

    @Test
    @DisplayName("Verify get rental by id")
    void getById_validRentalId_shouldReturnRentalDto() {
        when(rentalRepository.getByIdForCurrentUser(SECOND_RENTAL_ID))
                .thenReturn(Optional.ofNullable(SECOND_RENTAL));
        when(rentalMapper.toDto(SECOND_RENTAL)).thenReturn(SECOND_RENTAL_DTO);

        RentalResponseDto actual = rentalService.getById(SECOND_RENTAL_ID);
        assertEquals(SECOND_RENTAL_DTO, actual);
    }

    @Test
    @DisplayName("Verify complete rental with Valid Id")
    void completeRental_validRentalId_shouldReturnRentalDto() {
        when(rentalRepository.getByIdForCurrentUser(SECOND_RENTAL_ID))
                .thenReturn(Optional.ofNullable(SECOND_RENTAL));
        when(rentalRepository.save(SECOND_RENTAL))
                .thenReturn(SAVED_RETURN_SECOND_RENTAL);
        when(rentalMapper.toReturnDto(SAVED_RETURN_SECOND_RENTAL))
                .thenReturn(RENTAL_RETURN_RESPONSE_DTO);
        when(carService.findById(CAR_ID)).thenReturn(CAR);

        RentalReturnResponseDto actual = rentalService.completeRental(SECOND_RENTAL_ID);
        assertEquals(RENTAL_RETURN_RESPONSE_DTO, actual);
    }

    @Test
    @DisplayName("Verify creating rental with invalid car ID throws EntityNotFoundException")
    void create_invalidCarId_shouldThrowEntityNotFoundException() {
        when(carService.findById(CAR_ID)).thenThrow(new EntityNotFoundException("Car not found"));

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> rentalService.create(REQUEST_CREATE_RENTAL_DTO)
        );

        assertEquals("Car not found", exception.getMessage());
    }

    @Test
    @DisplayName("Verify complete rental with invalid ID throws EntityNotFoundException")
    void completeRental_invalidRentalId_shouldThrowEntityNotFoundException() {
        when(rentalRepository.getByIdForCurrentUser(SECOND_RENTAL_ID)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> rentalService.completeRental(SECOND_RENTAL_ID)
        );

        assertEquals("Can`t find rental with id: " + SECOND_RENTAL_ID, exception.getMessage());
    }
}
