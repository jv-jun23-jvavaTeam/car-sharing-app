package com.jvavateam.carsharingapp.mapper;

import com.jvavateam.carsharingapp.config.MapperConfiguration;
import com.jvavateam.carsharingapp.dto.payment.CreatePaymentRequestDto;
import com.jvavateam.carsharingapp.dto.payment.PaymentResponseDto;
import com.jvavateam.carsharingapp.model.Payment;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfiguration.class)
public interface PaymentMapper {
    Payment toEntity(CreatePaymentRequestDto requestDto);

    PaymentResponseDto toDto(Payment payment);
}
