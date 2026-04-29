package lt.viko.eif.kskrebe.carserver.config;

import lt.viko.eif.kskrebe.carserver.model.Car;
import lt.viko.eif.kskrebe.carserver.model.CarPart;
import lt.viko.eif.kskrebe.carserver.service.CarService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Pradinių duomenų įkėlimo konfigūracija.
 *
 * <p>Paleidus serverį, automatiškai įkelia keletą bandomųjų automobilių
 * su dalimis į H2 duomenų bazę. Tai padeda iš karto testuoti SOAP paslaugą
 * SoapUI ar kitu įrankiu be rankinio duomenų įvedimo.</p>
 *
 * <p>{@link CommandLineRunner} paleidžiamas po to, kai Spring kontekstas
 * visiškai inicializuojamas – tai saugus laikas dirbti su duomenų baze.</p>
 *
 * <p>{@code @Profile("!test")} užtikrina, kad šis komponentas
 * NEPALEIDŽIAMAS testų metu – testai neturi rašyti į duomenų bazę.</p>
 *
 * @author kskrebe
 * @version 1.0
 */
@Configuration
@Profile("!test")
public class DataInitializer {

    /**
     * Sukuria ir išsaugo pradinius bandomuosius duomenis.
     *
     * <p>Šis metodas automatiškai paleidžiamas Spring Boot paleidimo metu.</p>
     *
     * @param carService automobilių verslo logikos komponentas
     * @return {@link CommandLineRunner} egzempliorius
     */
    @Bean
    public CommandLineRunner loadData(CarService carService) {
        return args -> {

            // ===== 1 automobilis: Toyota Corolla =====
            Car toyota = new Car("Toyota", "Corolla", 2022, 18500.0f, true, 'R');
            toyota.setParts(new ArrayList<>(Arrays.asList(
                    new CarPart("Variklis 1.6",               "ENG-TOY-001", 3200.0f, 1,  true),
                    new CarPart("Stabdžių diskas (priekyje)", "BRK-TOY-002",  145.0f, 4,  true)
            )));
            carService.saveCar(toyota);

            // ===== 2 automobilis: BMW X5 =====
            Car bmw = new Car("BMW", "X5", 2023, 67000.0f, true, 'B');
            bmw.setParts(new ArrayList<>(Arrays.asList(
                    new CarPart("Turbina",        "TRB-BMW-001", 2800.0f, 1, true),
                    new CarPart("Amortizatorius", "SHK-BMW-002",  520.0f, 2, true)
            )));
            carService.saveCar(bmw);

            // ===== 3 automobilis: Volkswagen Golf =====
            Car vw = new Car("Volkswagen", "Golf", 2021, 22000.0f, false, 'G');
            vw.setParts(new ArrayList<>(Arrays.asList(
                    new CarPart("Oro filtras", "FLT-VW-001", 35.0f, 10, true)
            )));
            carService.saveCar(vw);

            System.out.println(">>> Pradiniai duomenys įkelti: 3 automobiliai, 5 dalys.");
            System.out.println(">>> WSDL: http://localhost:8080/services/cars?wsdl");
            System.out.println(">>> H2 konsolė: http://localhost:8080/h2-console");
        };
    }
}

