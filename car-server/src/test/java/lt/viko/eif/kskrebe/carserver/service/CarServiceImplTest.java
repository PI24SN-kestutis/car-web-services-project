package lt.viko.eif.kskrebe.carserver.service;

import lt.viko.eif.kskrebe.carserver.model.Car;
import lt.viko.eif.kskrebe.carserver.model.CarPart;
import lt.viko.eif.kskrebe.carserver.repository.CarRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Vienetų testai {@link CarServiceImpl} verslo logikos klasei.
 *
 * <p>Naudojama Mockito biblioteka, kuri imituoja ({@code mock})
 * {@link CarRepository} elgseną – testai nevykdo realių DB užklausų.
 * Tai daro testus greitais ir nepriklausomais nuo aplinkos.</p>
 *
 * <p>Anotacija {@code @ExtendWith(MockitoExtension.class)} automatiškai
 * inicializuoja {@code @Mock} ir {@code @InjectMocks} laukus.</p>
 *
 * @author kskrebe
 * @version 1.0
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("CarServiceImpl verslo logikos testai")
class CarServiceImplTest {

    /**
     * Imitacinė (mock) repozitorija.
     * Mockito ją sukuria automatiškai – nenaudoja tikros H2 DB.
     */
    @Mock
    private CarRepository carRepository;

    /**
     * Testuojamas servisas su įterpta imitacine repozitorija.
     * {@code @InjectMocks} automatiškai sukuria {@link CarServiceImpl}
     * ir įterpia {@code carRepository}.
     */
    @InjectMocks
    private CarServiceImpl carService;

    /** Bandomasis automobilis, naudojamas testuose. */
    private Car testCar;

    /**
     * Parengiamas bandomasis objektas prieš kiekvieną testą.
     */
    @BeforeEach
    void setUp() {
        testCar = new Car("Toyota", "Corolla", 2022, 18500.0f, true, 'R');
        testCar.setId(1L);
    }

    /** Tikrina, ar getAllCars grąžina sąrašą iš repozitorijos. */
    @Test
    @DisplayName("getAllCars() grąžina automobilių sąrašą")
    void testGetAllCars() {
        // Nustatome: kai iškviestas findAll(), grąžinamas sąrašas su vienu automobiliu
        when(carRepository.findAll()).thenReturn(Arrays.asList(testCar));

        List<Car> result = carService.getAllCars();

        assertEquals(1, result.size());
        assertEquals("Toyota", result.get(0).getMake());
        // Patikriname, kad findAll() buvo iškviestas lygiai vieną kartą
        verify(carRepository, times(1)).findAll();
    }

    /** Tikrina, ar getCarById grąžina teisingą automobilį, kai rastas. */
    @Test
    @DisplayName("getCarById() grąžina automobilį, kai rastas")
    void testGetCarByIdFound() {
        when(carRepository.findById(1L)).thenReturn(Optional.of(testCar));

        Optional<Car> result = carService.getCarById(1L);

        assertTrue(result.isPresent(), "Automobilis turėtų būti rastas");
        assertEquals("Toyota", result.get().getMake());
    }

    /** Tikrina, ar getCarById grąžina tuščią Optional, kai nerastas. */
    @Test
    @DisplayName("getCarById() grąžina tuščią Optional, kai nerastas")
    void testGetCarByIdNotFound() {
        when(carRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Car> result = carService.getCarById(99L);

        assertFalse(result.isPresent(), "Neegzistuojančio ID automobilis neturėtų būti rastas");
    }

    /** Tikrina automobilio išsaugojimą. */
    @Test
    @DisplayName("saveCar() išsaugo automobilį ir grąžina jį")
    void testSaveCar() {
        when(carRepository.save(testCar)).thenReturn(testCar);

        Car result = carService.saveCar(testCar);

        assertNotNull(result);
        assertEquals("Toyota", result.getMake());
        verify(carRepository, times(1)).save(testCar);
    }

    /** Tikrina, ar saveCar() teisingai susieja dalis su automobiliu prieš išsaugojimą. */
    @Test
    @DisplayName("saveCar() susieja dalis su automobiliu")
    void testSaveCarLinksParts() {
        CarPart part = new CarPart("Variklis", "ENG-001", 3200.0f, 1, true);
        testCar.getParts().add(part);

        when(carRepository.save(testCar)).thenReturn(testCar);
        carService.saveCar(testCar);

        // Patikriname, kad dalis buvo susieta su automobiliu
        assertEquals(testCar, part.getCar());
    }

    /** Tikrina automobilio ištrynimą. */
    @Test
    @DisplayName("deleteCar() iškviečia repozitorijos deleteById()")
    void testDeleteCar() {
        // Nustatome: deleteById() nieko negrąžina (void)
        doNothing().when(carRepository).deleteById(1L);

        carService.deleteCar(1L);

        verify(carRepository, times(1)).deleteById(1L);
    }
}

