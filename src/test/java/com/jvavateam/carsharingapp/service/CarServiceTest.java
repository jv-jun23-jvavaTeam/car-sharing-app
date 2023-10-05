package com.jvavateam.carsharingapp.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.jvavateam.carsharingapp.dto.car.CarDtoRequest;
import com.jvavateam.carsharingapp.dto.car.CarDtoResponse;
import com.jvavateam.carsharingapp.exception.EntityNotFoundException;
import com.jvavateam.carsharingapp.mapper.car.CarMapper;
import com.jvavateam.carsharingapp.model.Car;
import com.jvavateam.carsharingapp.repository.car.CarRepository;
import com.jvavateam.carsharingapp.service.impl.CarServiceImpl;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CarServiceTest {
    private static final Long CAR_ID = 100L;
    private static final Long NON_EXISTING_CAR_ID = 1L;
    private static final String BRAND = "Toyota";
    private static final String UPDATED_BRAND = "BMW";
    private static final String MODEL = "Toyota Camry";
    private static final String UPDATED_MODEL = "X5";
    private static final int INVENTORY = 15;
    private static final int UPDATED_INVENTORY = 20;
    private static final BigDecimal DAILY_FEE = BigDecimal.valueOf(80.00);
    private static final BigDecimal UPDATED_DAILY_FEE = BigDecimal.valueOf(99.99);
    private static final Car.Type TYPE = Car.Type.SEDAN;
    private static final Car.Type UPDATED_TYPE = Car.Type.SUV;
    private static final String EXCEPTION_MESSAGE = "Can't find car by id: ";
    private static final int AVAILABLE_CARS_QUANTITY = 2;
    private static final CarDtoRequest CAR_DTO_REQUEST = new CarDtoRequest(
            BRAND, MODEL, INVENTORY, DAILY_FEE, TYPE);
    private static final CarDtoResponse CAR_DTO_RESPONSE = new CarDtoResponse(
            CAR_ID, BRAND, MODEL, INVENTORY, DAILY_FEE, TYPE);
    private static final Car EXISTING_CAR = new Car()
            .setId(CAR_ID)
            .setBrand(BRAND)
            .setModel(MODEL)
            .setInventory(INVENTORY)
            .setDailyFee(DAILY_FEE)
            .setType(TYPE);
    private static final Car UPDATED_CAR = new Car()
            .setId(CAR_ID)
            .setBrand(UPDATED_BRAND)
            .setModel(UPDATED_MODEL)
            .setInventory(UPDATED_INVENTORY)
            .setDailyFee(UPDATED_DAILY_FEE)
            .setType(UPDATED_TYPE);

    @Mock
    private CarRepository carRepository;
    @Mock
    private CarMapper carMapper;
    @InjectMocks
    private CarServiceImpl carService;

    @Test
    @DisplayName("Creates a car with valid data and returns it")
    public void create_ValidData_ReturnCar() {
        when(carMapper.toEntity(CAR_DTO_REQUEST)).thenReturn(EXISTING_CAR);
        when(carRepository.save(EXISTING_CAR)).thenReturn(EXISTING_CAR);
        when(carMapper.toDto(EXISTING_CAR)).thenReturn(CAR_DTO_RESPONSE);

        CarDtoResponse savedCarDto = carService.create(CAR_DTO_REQUEST);
        assertThat(savedCarDto).isEqualTo(CAR_DTO_RESPONSE);
    }

    @Test
    @DisplayName("Retrieves a car by its id and returns it when exists")
    public void getById_WhenCarExists_ReturnCar() {
        Car car = new Car();
        when(carRepository.findById(CAR_ID)).thenReturn(Optional.of(car));
        when(carMapper.toDto(car)).thenReturn(CAR_DTO_RESPONSE);
        CarDtoResponse carDtoResponse = carService.getById(CAR_ID);
        assertNotNull(carDtoResponse);
    }

    @Test
    @DisplayName("Retrieves a car by its id and throws exception is doesn't exist")
    public void getById_WhenCarDoesntExists_ThrowException() {
        when(carRepository.findById(CAR_ID)).thenReturn(Optional.empty());
        Exception exception = assertThrows(
                EntityNotFoundException.class,
                () -> carService.findById(CAR_ID));
        assertEquals(EXCEPTION_MESSAGE + CAR_ID, exception.getMessage());
    }

    @Test
    @DisplayName("Retrieves list of cars and returns it when exists")
    public void getALl_WhenCarsExist_ReturnListOfCars() {
        List<Car> cars = List.of(new Car(), new Car());
        when(carRepository.findAll()).thenReturn(cars);
        List<CarDtoResponse> carDtoResponses = carService.getAll();
        assertEquals(AVAILABLE_CARS_QUANTITY, carDtoResponses.size());
    }

    @Test
    @DisplayName("Retrieves list of cars and returns empty list if it doesn't exist")
    public void getAll_WhenNoneExist_ThrowsException() {
        when(carRepository.findAll()).thenReturn(Collections.emptyList());
        List<CarDtoResponse> carDtoResponses = carService.getAll();
        assertEquals(0, carDtoResponses.size());
    }

    @Test
    @DisplayName("Updates car by id and returns it when exists")
    public void update_WhenCarExists_ReturnCar() {
        CarDtoResponse expectedResponse = new CarDtoResponse(
                CAR_ID, UPDATED_MODEL, UPDATED_BRAND,
                UPDATED_INVENTORY, UPDATED_DAILY_FEE, UPDATED_TYPE);

        when(carMapper.toEntity(CAR_DTO_REQUEST)).thenReturn(UPDATED_CAR);
        when(carRepository.save(UPDATED_CAR)).thenReturn(UPDATED_CAR);
        when(carMapper.toDto(UPDATED_CAR)).thenReturn(expectedResponse);

        CarDtoResponse updatedCarDtoResponse = carService.update(CAR_DTO_REQUEST, CAR_ID);

        assertNotNull(updatedCarDtoResponse);
        assertEquals(expectedResponse, updatedCarDtoResponse);
    }

    @Test
    @DisplayName("Update the car in database and return it")
    public void updateByCar_WhenCarExists_ReturnCar() {
        when(carRepository.save(EXISTING_CAR)).thenReturn(UPDATED_CAR);

        Car returnedCar = carService.update(EXISTING_CAR);

        assertNotNull(returnedCar);
        assertEquals(UPDATED_CAR.getId(), returnedCar.getId());
        assertEquals(UPDATED_CAR.getBrand(), returnedCar.getBrand());
        assertEquals(UPDATED_CAR.getModel(), returnedCar.getModel());
        assertEquals(UPDATED_CAR.getType(), returnedCar.getType());

        verify(carRepository, times(1)).save(EXISTING_CAR);
    }

    @Test
    @DisplayName("Update car inventory and return the car")
    public void updateInventory_WhenCarExists_ReturnCar() {
        when(carRepository.findById(CAR_ID)).thenReturn(Optional.of(EXISTING_CAR));
        when(carRepository.save(EXISTING_CAR)).thenReturn(UPDATED_CAR);

        carService.updateInventory(EXISTING_CAR);

        verify(carRepository).findById(CAR_ID);
        verify(carRepository).save(EXISTING_CAR);

        assertEquals(UPDATED_INVENTORY, UPDATED_CAR.getInventory());
    }

    @Test
    @DisplayName("Update car inventory and throw exception if car doesn't exists")
    public void updateInventory_WhenCarDoesntExist_ThrowException() {
        when(carRepository.findById(CAR_ID)).thenReturn(Optional.empty());
        Exception exception = assertThrows(
                EntityNotFoundException.class,
                () -> carService.updateInventory(EXISTING_CAR));
        assertEquals(EXCEPTION_MESSAGE + CAR_ID, exception.getMessage());
    }

    @Test
    @DisplayName("Delete car by id and should remove it")
    public void deleteById_ValidId_DeleteTheCar() {
        carService.deleteById(CAR_ID);
        Mockito.verify(carRepository).deleteById(CAR_ID);
    }

    @Test
    @DisplayName("Retrieve a car by id and return the car")
    public void findById_ValidId_ReturnCar() {
        when(carRepository.findById(CAR_ID)).thenReturn(Optional.of(EXISTING_CAR));
        Car actualCar = carService.findById(CAR_ID);
        assertNotNull(actualCar);
        assertEquals(EXISTING_CAR, actualCar);
    }

    @Test
    @DisplayName("Retrieve a car by id and throws exception if car doesn't exist")
    public void findById_InvalidId_ThrowsException() {
        when(carRepository.findById(NON_EXISTING_CAR_ID)).thenReturn(Optional.empty());
        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> carService.findById(NON_EXISTING_CAR_ID)
        );
        assertEquals(EXCEPTION_MESSAGE + NON_EXISTING_CAR_ID, exception.getMessage());
    }

}
