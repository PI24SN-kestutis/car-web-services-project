package lt.viko.eif.kskrebe.carclient.service;

import lt.viko.eif.kskrebe.carclient.dto.CarFilterForm;
import lt.viko.eif.kskrebe.carclient.dto.CarForm;
import lt.viko.eif.kskrebe.carclient.dto.CarPartForm;
import lt.viko.eif.kskrebe.carclient.dto.CarPartView;
import lt.viko.eif.kskrebe.carclient.dto.CarView;
import lt.viko.eif.kskrebe.carclient.soap.CarSoapGateway;
import lt.viko.eif.kskrebe.carclient.ws.Car;
import lt.viko.eif.kskrebe.carclient.ws.CarPart;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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

    /**
     * Grąžina automobilių sąrašą su pritaikytais paieškos filtrais.
     *
     * @param filter filtro forma
     * @return filtruotas automobilių sąrašas
     */
    public List<CarView> getAllCars(CarFilterForm filter) {
        List<CarView> cars = getAllCars();
        if (filter == null) {
            return cars;
        }

        return cars.stream()
                .filter(car -> matchesQuery(car, filter.getQ()))
                .filter(car -> matchesAvailability(car, filter.getAvailable()))
                .filter(car -> matchesMinPrice(car, filter.getMinPrice()))
                .filter(car -> matchesMaxPrice(car, filter.getMaxPrice()))
                .toList();
    }

    /**
     * Grąžina automobilio formą redagavimui.
     *
     * @param id automobilio identifikatorius
     * @return forma su užpildytais duomenimis
     */
    public CarForm getCarFormById(Long id) {
        Car car = carSoapGateway.getCarById(id);
        if (car == null) {
            throw new IllegalStateException("Automobilis nerastas pagal nurodytą ID.");
        }
        return toForm(car);
    }

    /**
     * Sukuria tuščią formą naujam automobiliui.
     *
     * @return pradinė forma
     */
    public CarForm createEmptyForm() {
        CarForm form = new CarForm();
        form.setAvailable(Boolean.TRUE);
        form.setColorCode("A");
        form.getParts().add(new CarPartForm());
        return form;
    }

    /**
     * Sukuria arba atnaujina automobilį su detalėmis.
     *
     * @param form forma iš UI
     * @return išsaugotas automobilis vaizdavimui
     */
    public CarView upsertCar(CarForm form) {
        validateCarForm(form);
        Car saved = carSoapGateway.upsertCar(toSoapCar(form));
        return toView(saved);
    }

    /**
     * Pašalina automobilį pagal ID.
     *
     * @param id automobilio identifikatorius
     */
    public void deleteCar(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Nenurodytas automobilio ID.");
        }
        carSoapGateway.deleteCar(id);
    }

    private boolean matchesQuery(CarView car, String query) {
        if (query == null || query.isBlank()) {
            return true;
        }
        String q = query.toLowerCase(Locale.ROOT);
        return safe(car.make()).toLowerCase(Locale.ROOT).contains(q)
                || safe(car.model()).toLowerCase(Locale.ROOT).contains(q);
    }

    private boolean matchesAvailability(CarView car, Boolean availability) {
        return availability == null || car.available() == availability;
    }

    private boolean matchesMinPrice(CarView car, Float minPrice) {
        return minPrice == null || car.price() >= minPrice;
    }

    private boolean matchesMaxPrice(CarView car, Float maxPrice) {
        return maxPrice == null || car.price() <= maxPrice;
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

    private CarForm toForm(Car car) {
        CarForm form = new CarForm();
        form.setId(car.getId());
        form.setMake(car.getMake());
        form.setModel(car.getModel());
        form.setYear(car.getYear());
        form.setPrice(car.getPrice());
        form.setAvailable(car.isAvailable());
        form.setColorCode(String.valueOf((char) car.getColorCode()));
        form.setParts(toPartForms(car.getParts() == null ? null : car.getParts().getPart()));
        if (form.getParts().isEmpty()) {
            form.getParts().add(new CarPartForm());
        }
        return form;
    }

    private Car toSoapCar(CarForm form) {
        Car car = new Car();
        car.setId(form.getId());
        car.setMake(trimToNull(form.getMake()));
        car.setModel(trimToNull(form.getModel()));
        car.setYear(form.getYear() == null ? 0 : form.getYear());
        car.setPrice(form.getPrice() == null ? 0 : form.getPrice());
        car.setAvailable(Boolean.TRUE.equals(form.getAvailable()));
        car.setColorCode(extractColorCode(form.getColorCode()));

        Car.Parts parts = new Car.Parts();
        for (CarPartForm partForm : nonNullParts(form.getParts())) {
            if (isBlankPart(partForm)) {
                continue;
            }
            CarPart part = new CarPart();
            part.setId(partForm.getId());
            part.setName(trimToNull(partForm.getName()));
            part.setPartNumber(trimToNull(partForm.getPartNumber()));
            part.setPrice(partForm.getPrice() == null ? 0 : partForm.getPrice());
            part.setQuantity(partForm.getQuantity() == null ? 0 : partForm.getQuantity());
            part.setAvailable(Boolean.TRUE.equals(partForm.getAvailable()));
            parts.getPart().add(part);
        }
        car.setParts(parts);
        return car;
    }

    private int extractColorCode(String colorCode) {
        if (colorCode == null || colorCode.isBlank()) {
            return 'A';
        }
        return colorCode.trim().charAt(0);
    }

    private void validateCarForm(CarForm form) {
        if (form == null) {
            throw new IllegalArgumentException("Nėra automobiliui reikalingų duomenų.");
        }
        if (trimToNull(form.getMake()) == null || trimToNull(form.getModel()) == null) {
            throw new IllegalArgumentException("Gamintojas ir modelis yra privalomi.");
        }
        if (form.getYear() == null || form.getYear() < 1886 || form.getYear() > 2100) {
            throw new IllegalArgumentException("Neteisingi automobilio metai.");
        }
        if (form.getPrice() == null || form.getPrice() < 0) {
            throw new IllegalArgumentException("Automobilio kaina negali būti neigiama.");
        }
    }

    private List<CarPartForm> toPartForms(List<CarPart> parts) {
        List<CarPartForm> result = new ArrayList<>();
        if (parts == null) {
            return result;
        }
        for (CarPart part : parts) {
            CarPartForm form = new CarPartForm();
            form.setId(part.getId());
            form.setName(part.getName());
            form.setPartNumber(part.getPartNumber());
            form.setPrice(part.getPrice());
            form.setQuantity(part.getQuantity());
            form.setAvailable(part.isAvailable());
            result.add(form);
        }
        return result;
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

    private boolean isBlankPart(CarPartForm part) {
        if (part == null) {
            return true;
        }
        return trimToNull(part.getName()) == null
                && trimToNull(part.getPartNumber()) == null
                && part.getPrice() == null
                && part.getQuantity() == null;
    }

    private List<CarPartForm> nonNullParts(List<CarPartForm> parts) {
        return parts == null ? List.of() : parts;
    }

    private String trimToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private String safe(String value) {
        return value == null ? "" : value;
    }
}
