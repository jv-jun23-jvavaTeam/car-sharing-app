package com.jvavateam.carsharingapp.mapper.rental;

import com.jvavateam.carsharingapp.config.MapperConfiguration;
import com.jvavateam.carsharingapp.dto.rental.CreateRentalByManagerDto;
import com.jvavateam.carsharingapp.dto.rental.CreateRentalDto;
import com.jvavateam.carsharingapp.dto.rental.RentalResponseDto;
import com.jvavateam.carsharingapp.dto.rental.RentalReturnResponseDto;
import com.jvavateam.carsharingapp.model.Car;
import com.jvavateam.carsharingapp.model.Rental;
import com.jvavateam.carsharingapp.model.User;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfiguration.class)
public interface RentalMapper {
    @Mapping(target = "carId", source = "car.id")
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "isActive", source = "active")
    RentalResponseDto toDto(Rental rental);

    @Mapping(target = "carId", source = "car.id")
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "isActive", source = "active")
    RentalReturnResponseDto toReturnDto(Rental rental);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "returnDate", ignore = true)
    @Mapping(target = "actualReturnDate", ignore = true)
    @Mapping(target = "car", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "active", ignore = true)
    Rental toModel(CreateRentalByManagerDto rentalDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "returnDate", ignore = true)
    @Mapping(target = "actualReturnDate", ignore = true)
    @Mapping(target = "car", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "active", ignore = true)
    Rental toModel(CreateRentalDto rentalDto);

    @AfterMapping
    default void setUpModel(@MappingTarget Rental rental, CreateRentalByManagerDto createRentalByManagerDto) {
        Car car = new Car();
        car.setId(createRentalByManagerDto.carId());
        User user = new User();
        user.setId(createRentalByManagerDto.userId());
        rental.setActive(true);
        rental.setCar(car);
        rental.setUser(user);
    }
}
