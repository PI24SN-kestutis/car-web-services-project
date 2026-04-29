package lt.viko.eif.kskrebe.carserver.ws;

import lt.viko.eif.kskrebe.carserver.model.Car;
import lt.viko.eif.kskrebe.carserver.service.CarService;
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
 * Vienetų testai {@link CarWebServiceImpl} SOAP paslaugos implementacijai.
 *
 * <p>Naudojama Mockito biblioteka imitaciniam {@link CarService} objektui sukurti.
 * Testai tikrina tik SOAP sluoksnio logiką, ne duomenų bazę ar tinklą.</p>
 *
 * <p>Šie testai užtikrina, kad:</p>
 * <ul>
 *   <li>SOAP metodai teisingai deleguoja užklausas į servisą</li>
 *   <li>Null apdorojimas veikia teisingai (nerastas automobilis)</li>
 *   <li>Kiekvienas metodas iškviečia servisą teisingas kartų skaičius</li>
 * </ul>
 *
 * @author kskrebe
 * @version 1.0
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("CarWebServiceImpl SOAP paslaugos testai")
class CarWebServiceImplTest {

    /**
     * Imitacinis verslo logikos servisas – nenaudoja tikros logikos.
     */
    @Mock
    private CarService carService;

    /**
     * Testuojama SOAP implementacija su įterpta imitacine priklausomybe.
     */
    @InjectMocks
    private CarWebServiceImpl carWebServiceImpl;

    /** Bandomasis automobilis. */
    private Car testCar;

    /**
     * Parengiamas bandomasis objektas prieš kiekvieną testą.
     */
    @BeforeEach
    void setUp() {
        testCar = new Car("BMW", "X5", 2023, 67000.0f, true, 'B');
        testCar.setId(1L);
    }

    /** Tikrina, ar getAllCars() grąžina sąrašą iš serviso. */
    @Test
    @DisplayName("getAllCars() SOAP metodas grąžina sąrašą")
    void testGetAllCars() {
        when(carService.getAllCars()).thenReturn(Arrays.asList(testCar));

        List<Car> result = carWebServiceImpl.getAllCars();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("BMW", result.get(0).getMake());
        verify(carService, times(1)).getAllCars();
    }

    /** Tikrina, ar getCarById() grąžina automobilį, kai rastas. */
    @Test
    @DisplayName("getCarById() SOAP metodas grąžina automobilį, kai rastas")
    void testGetCarByIdFound() {
        when(carService.getCarById(1L)).thenReturn(Optional.of(testCar));

        Car result = carWebServiceImpl.getCarById(1L);

        assertNotNull(result);
        assertEquals("X5", result.getModel());
    }

    /** Tikrina, ar getCarById() grąžina null, kai automobilis nerastas. */
    @Test
    @DisplayName("getCarById() SOAP metodas grąžina null, kai nerastas")
    void testGetCarByIdNotFound() {
        when(carService.getCarById(99L)).thenReturn(Optional.empty());

        Car result = carWebServiceImpl.getCarById(99L);

        // SOAP atsakyme null atvaizdaujamas kaip tuščias elementas
        assertNull(result);
    }

    /** Tikrina automobilio pridėjimą per SOAP. */
    @Test
    @DisplayName("addCar() SOAP metodas išsaugo automobilį")
    void testAddCar() {
        when(carService.saveCar(testCar)).thenReturn(testCar);

        Car result = carWebServiceImpl.addCar(testCar);

        assertNotNull(result);
        assertEquals("BMW", result.getMake());
        assertEquals(67000.0f, result.getPrice(), 0.001f);
        verify(carService, times(1)).saveCar(testCar);
    }

    /** Tikrina automobilio ištrynimą per SOAP. */
    @Test
    @DisplayName("deleteCar() SOAP metodas iškviečia serviso deleteCar()")
    void testDeleteCar() {
        doNothing().when(carService).deleteCar(1L);

        carWebServiceImpl.deleteCar(1L);

        verify(carService, times(1)).deleteCar(1L);
    }
}

