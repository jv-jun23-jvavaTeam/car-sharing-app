package com.jvavateam.carsharingapp.mapper.payment;

import com.jvavateam.carsharingapp.config.MapperConfiguration;
import com.jvavateam.carsharingapp.dto.payment.CreatePaymentRequestDto;
import com.jvavateam.carsharingapp.dto.payment.PaymentResponseDto;
import com.jvavateam.carsharingapp.model.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfiguration.class)
public interface PaymentMapper {
    Payment toEntity(CreatePaymentRequestDto requestDto);

    @Mapping(target = "rentalId", source = "payment.rental.id")
    PaymentResponseDto toDto(Payment payment);
}
