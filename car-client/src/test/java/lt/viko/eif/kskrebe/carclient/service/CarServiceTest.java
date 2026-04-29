package lt.viko.eif.kskrebe.carclient.service;

import lt.viko.eif.kskrebe.carclient.dto.CarForm;
import lt.viko.eif.kskrebe.carclient.dto.CarPartForm;
import lt.viko.eif.kskrebe.carclient.dto.CarView;
import lt.viko.eif.kskrebe.carclient.soap.CarSoapGateway;
import lt.viko.eif.kskrebe.carclient.ws.Car;
import lt.viko.eif.kskrebe.carclient.ws.CarPart;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
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

    @Test
    void upsertMapsFormToSoapModel() {
        CarSoapGateway gateway = Mockito.mock(CarSoapGateway.class);
        CarService service = new CarService(gateway);

        CarForm form = new CarForm();
        form.setId(5L);
        form.setMake("BMW");
        form.setModel("M3");
        form.setYear(2022);
        form.setPrice(45000f);
        form.setAvailable(true);
        form.setColorCode("B");

        CarPartForm partForm = new CarPartForm();
        partForm.setName("Mirror");
        partForm.setPartNumber("M-1");
        partForm.setPrice(100f);
        partForm.setQuantity(1);
        partForm.setAvailable(true);
        form.setParts(List.of(partForm));

        Car returned = new Car();
        returned.setId(5L);
        returned.setMake("BMW");
        returned.setModel("M3");
        returned.setYear(2022);
        returned.setPrice(45000f);
        returned.setAvailable(true);
        returned.setColorCode('B');
        returned.setParts(new Car.Parts());

        Mockito.when(gateway.upsertCar(Mockito.any(Car.class))).thenReturn(returned);

        service.upsertCar(form);

        ArgumentCaptor<Car> captor = ArgumentCaptor.forClass(Car.class);
        Mockito.verify(gateway).upsertCar(captor.capture());

        Car sent = captor.getValue();
        assertEquals("BMW", sent.getMake());
        assertEquals("M3", sent.getModel());
        assertEquals(1, sent.getParts().getPart().size());
        assertEquals("Mirror", sent.getParts().getPart().getFirst().getName());
    }
}
