package com.jvavateam.carsharingapp.mapper.payment;

import com.jvavateam.carsharingapp.config.MapperConfiguration;
import com.jvavateam.carsharingapp.dto.payment.CreatePaymentRequestDto;
import com.jvavateam.carsharingapp.dto.payment.PaymentResponseDto;
import com.jvavateam.carsharingapp.model.Payment;
import com.jvavateam.carsharingapp.model.Rental;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfiguration.class)
public interface PaymentMapper {
    @Mapping(target = "type", source = "paymentType")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "rental", ignore = true)
    @Mapping(target = "sessionUrl", ignore = true)
    @Mapping(target = "sessionId", ignore = true)
    @Mapping(target = "amountToPay", ignore = true)
    Payment toEntity(CreatePaymentRequestDto requestDto);

    @Mapping(target = "rentalId", source = "payment.rental.id")
    PaymentResponseDto toDto(Payment payment);

    @AfterMapping
    default void setRentalById(@MappingTarget Payment payment,
                               CreatePaymentRequestDto requestDto) {
        Rental rental = new Rental();
        rental.setId(requestDto.rentalId());
        payment.setRental(rental);
    }
}
