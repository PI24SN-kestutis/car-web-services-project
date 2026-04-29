package lt.viko.eif.kskrebe.carserver.ws;

import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;
import lt.viko.eif.kskrebe.carserver.model.Car;

import java.util.List;

/**
 * JAX-WS SOAP tinklo paslaugos sutartis (Contract/Interface).
 *
 * <p>Ši sąsaja apibrėžia, kokias operacijas klientai gali iškviesti per SOAP protokolą.
 * Apache CXF automatiškai sugeneruoja WSDL dokumentą pagal šią sąsają.</p>
 *
 * <p>Naudojamas <b>Document/Literal</b> stilius – tai rekomenduojamas JAX-WS standartas,
 * kuris generuoja aiškesnį ir WS-I Basic Profile suderinamą WSDL.</p>
 *
 * <p>SOAP endpoint adresas: {@code http://localhost:8080/services/cars}<br>
 * WSDL dokumentas: {@code http://localhost:8080/services/cars?wsdl}</p>
 *
 * @author kskrebe
 * @version 1.0
 */
@WebService(
        name = "CarWebService",
        targetNamespace = "http://ws.carserver.kskrebe.eif.viko.lt/"
)
public interface CarWebService {

    /**
     * Grąžina visų automobilių sąrašą su jų dalimis.
     *
     * @return automobilių sąrašas
     */
    @WebMethod(operationName = "getAllCars")
    List<Car> getAllCars();

    /**
     * Randa automobilį pagal jo unikalų identifikatorių.
     *
     * @param id automobilio identifikatorius
     * @return automobilis su dalimis arba {@code null}, jei nerastas
     */
    @WebMethod(operationName = "getCarById")
    Car getCarById(@WebParam(name = "id") Long id);

    /**
     * Prideda naują automobilį su dalimis į sistemą.
     *
     * @param car automobilio objektas (ID nustatomas automatiškai)
     * @return išsaugotas automobilis su sugeneruotu ID
     */
    @WebMethod(operationName = "addCar")
    Car addCar(@WebParam(name = "car") Car car);

    /**
     * Ištrina automobilį ir visas jo dalis pagal ID.
     *
     * @param id automobilio identifikatorius
     */
    @WebMethod(operationName = "deleteCar")
    void deleteCar(@WebParam(name = "id") Long id);
}

