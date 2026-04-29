package lt.viko.eif.kskrebe.carclient.service;

import lt.viko.eif.kskrebe.carclient.dto.CarView;
import lt.viko.eif.kskrebe.carclient.soap.CarSoapGateway;
import lt.viko.eif.kskrebe.carclient.ws.Car;
import lt.viko.eif.kskrebe.carclient.ws.CarPart;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Vienetinis testas, tikrinantis SOAP modelio konvertavimą į kliento DTO.
 */
class CarServiceTest {

    /**
     * Patikrina, kad automobilių ir detalių laukai teisingai perkelti į {@link CarView}.
     */
    @Test
    void mapsSoapObjectsToViewObjects() {
        CarSoapGateway gateway = Mockito.mock(CarSoapGateway.class);
        CarService service = new CarService(gateway);

        CarPart part = new CarPart();
        part.setId(11L);
        part.setName("Brake Pad");
        part.setPartNumber("BP-1");
        part.setPrice(20.5f);
        part.setQuantity(2);
        part.setAvailable(true);

        Car.Parts parts = new Car.Parts();
        parts.getPart().add(part);

        Car car = new Car();
        car.setId(1L);
        car.setMake("Audi");
        car.setModel("A4");
        car.setYear(2020);
        car.setPrice(25000.0f);
        car.setAvailable(true);
        car.setColorCode('A');
        car.setParts(parts);

        Mockito.when(gateway.getAllCars()).thenReturn(List.of(car));

        List<CarView> cars = service.getAllCars();

        assertEquals(1, cars.size());
        assertEquals("Audi", cars.getFirst().make());
        assertEquals(1, cars.getFirst().parts().size());
        assertEquals("Brake Pad", cars.getFirst().parts().getFirst().name());
    }
}
