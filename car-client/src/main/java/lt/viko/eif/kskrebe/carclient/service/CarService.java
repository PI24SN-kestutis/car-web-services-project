package lt.viko.eif.kskrebe.carclient.service;

import lt.viko.eif.kskrebe.carclient.dto.CarPartView;
import lt.viko.eif.kskrebe.carclient.dto.CarView;
import lt.viko.eif.kskrebe.carclient.soap.CarSoapGateway;
import lt.viko.eif.kskrebe.carclient.ws.Car;
import lt.viko.eif.kskrebe.carclient.ws.CarPart;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Servisas, kuris paima duomenis iš SOAP ir pritaiko juos vaizdavimui UI.
 */
@Service
public class CarService {

    private final CarSoapGateway carSoapGateway;

    /**
     * @param carSoapGateway priklausomybė ryšiui su SOAP paslauga
     */
    public CarService(CarSoapGateway carSoapGateway) {
        this.carSoapGateway = carSoapGateway;
    }

    /**
     * Grąžina visų automobilių sąrašą, konvertuotą į kliento DTO.
     *
     * @return automobilių sąrašas vaizdavimui
     */
    public List<CarView> getAllCars() {
        return carSoapGateway.getAllCars().stream()
                .map(this::toView)
                .toList();
    }

    private CarView toView(Car car) {
        return new CarView(
                car.getId(),
                car.getMake(),
                car.getModel(),
                car.getYear(),
                car.getPrice(),
                car.isAvailable(),
                (char) car.getColorCode(),
                toPartViews(car.getParts() == null ? null : car.getParts().getPart())
        );
    }

    private List<CarPartView> toPartViews(List<CarPart> parts) {
        if (parts == null) {
            return List.of();
        }
        return parts.stream()
                .map(part -> new CarPartView(
                        part.getId(),
                        part.getName(),
                        part.getPartNumber(),
                        part.getPrice(),
                        part.getQuantity(),
                        part.isAvailable()
                ))
                .toList();
    }
}
