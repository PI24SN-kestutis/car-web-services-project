package lt.viko.eif.kskrebe.carserver.ws;

import jakarta.jws.WebService;
import lt.viko.eif.kskrebe.carserver.model.Car;
import lt.viko.eif.kskrebe.carserver.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * JAX-WS SOAP tinklo paslaugos implementacija.
 *
 * <p>Realizuoja {@link CarWebService} sąsają. Kiekvienas metodo iškvietimas
 * per SOAP yra perduodamas į {@link CarService} verslo logikos sluoksnį.</p>
 *
 * <p>Anotacijos:</p>
 * <ul>
 *   <li>{@code @Component} – Spring sukuria šios klasės egzempliorių</li>
 *   <li>{@code @WebService} – Apache CXF registruoja kaip SOAP paslaugą</li>
 * </ul>
 *
 * <p>SOAP žinučių formatą ir WSDL automatiškai generuoja Apache CXF pagal
 * {@link CarWebService} sąsają ir JAXB anotacijas modelyje.</p>
 *
 * @author kskrebe
 * @version 1.0
 */
@Component
@WebService(
        endpointInterface = "lt.viko.eif.kskrebe.carserver.ws.CarWebService",
        serviceName = "CarService",
        portName = "CarPort",
        targetNamespace = "http://ws.carserver.kskrebe.eif.viko.lt/"
)
public class CarWebServiceImpl implements CarWebService {

    /** Verslo logikos komponentas. Įterpiamas per konstruktorių. */
    private final CarService carService;

    /**
     * Konstruktorius su priklausomybių įterpimu.
     *
     * @param carService automobilių verslo logikos komponentas
     */
    @Autowired
    public CarWebServiceImpl(CarService carService) {
        this.carService = carService;
    }

    /**
     * {@inheritDoc}
     *
     * <p>SOAP užklausa: operacija {@code getAllCars} be parametrų.</p>
     */
    @Override
    public List<Car> getAllCars() {
        return carService.getAllCars();
    }

    /**
     * {@inheritDoc}
     *
     * <p>Jei automobilis su nurodytu ID nerastas, grąžinamas {@code null},
     * kuris SOAP atsakyme atvaizduojamas kaip tuščias elementas.</p>
     */
    @Override
    public Car getCarById(Long id) {
        return carService.getCarById(id).orElse(null);
    }

    /**
     * {@inheritDoc}
     *
     * <p>Duomenys perduodami SOAP žinutėje kaip XML struktūra.
     * JAXB automatiškai konvertuoja XML į {@link Car} objektą.</p>
     */
    @Override
    public Car addCar(Car car) {
        return carService.saveCar(car);
    }

    /** {@inheritDoc} */
    @Override
    public void deleteCar(Long id) {
        carService.deleteCar(id);
    }
}

