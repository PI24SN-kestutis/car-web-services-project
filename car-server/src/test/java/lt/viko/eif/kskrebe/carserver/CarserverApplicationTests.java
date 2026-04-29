package lt.viko.eif.kskrebe.carserver;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Integracinis testas – tikrina, ar Spring kontekstas sėkmingai paleidžiamas.
 *
 * <p>{@code @SpringBootTest} paleidžia pilną Spring ApplicationContext,
 * įkelia visus komponentus ir tikrina, kad nėra konfigūracijos klaidų.</p>
 *
 * <p>{@code @ActiveProfiles("test")} aktyvuoja testinį profilį, kuris
 * išjungia {@code DataInitializer} duomenų įkėlimą – testai turi veikti
 * nepriklausomai nuo pradinių duomenų.</p>
 *
 * @author kskrebe
 * @version 1.0
 */
@SpringBootTest
@ActiveProfiles("test")
@DisplayName("Spring konteksto integracinis testas")
class CarserverApplicationTests {

    /**
     * Tikrina, ar Spring ApplicationContext sėkmingai inicializuojamas.
     * Jei konfigūracijoje yra klaidų – šis testas nepraeis.
     */
    @Test
    @DisplayName("Spring kontekstas paleidžiamas be klaidų")
    void contextLoads() {
        // Jei pasiekiama čia – kontekstas įkeltas sėkmingai
    }
}
