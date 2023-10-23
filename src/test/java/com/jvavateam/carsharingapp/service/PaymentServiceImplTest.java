package com.jvavateam.carsharingapp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.jvavateam.carsharingapp.dto.payment.CreatePaymentRequestDto;
import com.jvavateam.carsharingapp.dto.payment.PaymentResponseDto;
import com.jvavateam.carsharingapp.exception.EntityNotFoundException;
import com.jvavateam.carsharingapp.mapper.payment.PaymentMapper;
import com.jvavateam.carsharingapp.model.Car;
import com.jvavateam.carsharingapp.model.Payment;
import com.jvavateam.carsharingapp.model.Rental;
import com.jvavateam.carsharingapp.model.Role;
import com.jvavateam.carsharingapp.model.User;
import com.jvavateam.carsharingapp.payment.PaymentServiceImpl;
import com.jvavateam.carsharingapp.repository.payment.PaymentRepository;
import com.jvavateam.carsharingapp.repository.rental.RentalRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {
    private static final String VALID_SESSION_ID =
            "VALID_SESSION_ID";
    private static final String INVALID_SESSION_ID = "-";
    private static final String VALID_SESSION_URL =
            "VALID_SESSION_URL";

    private static final User VALID_USER_CUSTOMER = new User()
            .setId(100L)
            .setEmail("wylo@ua.com")
            .setPassword("Oleh")
            .setLastName("Lyashko")
            .setPassword("$2a$12$2gWx8fCmINQ1EZ9cNrMG0.uNl7d63gmb/zTwj6yCdgsPXn5WD4tcW")
            .setRoles(Set.of(new Role().setId(2L).setName(Role.RoleName.CUSTOMER)))
            .setDeleted(false);

    private static final Car VALID_CAR = new Car()
            .setId(100L)
            .setBrand("Toyota")
            .setModel("Camry")
            .setType(Car.Type.SEDAN)
            .setInventory(11)
            .setDailyFee(BigDecimal.valueOf(8000,2))
            .setDeleted(false);

    private static final Rental VALID_RENTAL = new Rental()
            .setId(100L)
            .setCar(VALID_CAR)
            .setUser(VALID_USER_CUSTOMER)
            .setRentalDate(LocalDate.of(2023,11,05))
            .setReturnDate(LocalDate.of(2023,11,20))
            .setActive(true)
            .setIsDeleted(false);
    private static final Rental VALID_RENTAL_TWO = new Rental()
            .setId(101L)
            .setCar(VALID_CAR)
            .setUser(VALID_USER_CUSTOMER)
            .setRentalDate(LocalDate.of(2023,11,05))
            .setReturnDate(LocalDate.of(2023,11,20))
            .setActive(true)
            .setIsDeleted(false);

    private static final Payment PAYMENT_UNPAID =
            new Payment()
                    .setId(1L)
                    .setStatus(Payment.Status.PENDING)
                    .setType(Payment.Type.PAYMENT)
                    .setSessionId(VALID_SESSION_ID)
                    .setSessionUrl(VALID_SESSION_URL)
                    .setRental(VALID_RENTAL)
                    .setAmountToPay(BigDecimal.valueOf(40000, 2))
                    .setDeleted(false);

    private static final Payment PAYMENT_PAID =
            new Payment()
                    .setId(2L)
                    .setStatus(Payment.Status.PAID)
                    .setType(Payment.Type.PAYMENT)
                    .setSessionId(VALID_SESSION_ID + "2")
                    .setSessionUrl(VALID_SESSION_URL + "2")
                    .setRental(VALID_RENTAL_TWO)
                    .setAmountToPay(BigDecimal.valueOf(40000,2))
                    .setDeleted(false);
    private static final CreatePaymentRequestDto CREATE_PAYMENT_DTO =
            new CreatePaymentRequestDto(
                    100L,
                    "PAYMENT"
            );
    private static final PaymentResponseDto PAYMENT_RESPONSE_DTO_UNPAID_PAYMENT =
            new PaymentResponseDto(
                    1L,
                    "PENDING",
                    "PAYMENT",
                    VALID_SESSION_URL,
                    VALID_SESSION_ID,
                    VALID_RENTAL.getId(),
                    BigDecimal.valueOf(40000,2)
            );

    @Mock
    private PaymentRepository paymentRepository;
    @Mock
    private RentalRepository rentalRepository;
    @Mock
    private PaymentMapper paymentMapper;
    @InjectMocks
    private PaymentServiceImpl paymentService;

    @Test
    @DisplayName("Verify the creation of Payment from CreatePaymentRequestDto "
            + "with not existing rental id")
    public void create_notExistingPaymentId_ThrowsException() {
        when(rentalRepository.findById(any(Long.class))).thenReturn(Optional.empty());
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> paymentService.create(CREATE_PAYMENT_DTO));
        assertEquals("Can't find rental by id: " + CREATE_PAYMENT_DTO.rentalId(),
                exception.getMessage());
        verifyNoMoreInteractions(rentalRepository);
    }

    @Test
    @DisplayName("Verify update of Payment status throws exception")
    public void update_nonExistingSessionId_ThrowsException() {
        when(paymentRepository.findBySessionId(INVALID_SESSION_ID)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> paymentService.updatePaymentStatus(INVALID_SESSION_ID));

        String expected = "Can't find payment by session id: " + INVALID_SESSION_ID;
        String actual = exception.getMessage();

        assertEquals(expected, actual);
        verify(paymentRepository, times(1)).findBySessionId(INVALID_SESSION_ID);
        verifyNoMoreInteractions(paymentRepository);
    }

    @Test
    @DisplayName("Verify retrieving all succesful payments for current user")
    public void getAllSuccesfull_shouldReturnAllPaidPayments() {
        when(paymentRepository.findAllByStatus(Payment.Status.PAID))
                .thenReturn(List.of(PAYMENT_PAID));

        List<PaymentResponseDto> actual = paymentService.getAllSuccessfulPayments();

        assertEquals(1, actual.size());
        verify(paymentRepository, times(1)).findAllByStatus(Payment.Status.PAID);
        verifyNoMoreInteractions(paymentRepository);
    }

    @Test
    @DisplayName("Verify retrieving all paused payments for current user")
    public void getAllPaused_shouldReturnAllPendingPayments() {
        when(paymentRepository.findAllByStatus(Payment.Status.PENDING))
                .thenReturn(List.of(PAYMENT_UNPAID));

        List<PaymentResponseDto> actual = paymentService.getAllPausedPayments();

        assertEquals(1, actual.size());
        verify(paymentRepository, times(1)).findAllByStatus(Payment.Status.PENDING);
        verifyNoMoreInteractions(paymentRepository);
    }

    @Test
    @DisplayName("Verify retrieving all payments for certain user by user id")
    public void getAllByUserId_shouldReturnAllPaymentsForCertainUser() {
        when(paymentRepository.findAllByRentalUserId(VALID_USER_CUSTOMER.getId()))
                .thenReturn(List.of(PAYMENT_UNPAID));
        when(paymentMapper.toDto(any(Payment.class)))
                .thenAnswer(invocation -> PAYMENT_RESPONSE_DTO_UNPAID_PAYMENT);
        List<PaymentResponseDto> actual = paymentService.getAllForUser(VALID_USER_CUSTOMER.getId());

        assertEquals(1, actual.size());
        verifyNoMoreInteractions(paymentRepository);
    }

    @Test
    @DisplayName("Verify retrieving all payments for current user")
    public void getAll_shouldReturnAllPaymentsForCurrentUser() {
        when(paymentRepository.findAllByRentalUserId(VALID_USER_CUSTOMER.getId()))
                .thenReturn(List.of(PAYMENT_UNPAID));

        List<PaymentResponseDto> actual = paymentService.getAllForUser(VALID_USER_CUSTOMER.getId());
        assertEquals(1, actual.size());
        verify(paymentRepository, times(1)).findAllByRentalUserId(VALID_USER_CUSTOMER.getId());
        verifyNoMoreInteractions(paymentRepository);
    }
}
