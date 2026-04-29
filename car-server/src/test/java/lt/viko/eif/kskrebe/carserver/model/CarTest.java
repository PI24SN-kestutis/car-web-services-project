package lt.viko.eif.kskrebe.carserver.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Vienetų testai {@link Car} modelio klasei.
 *
 * <p>Tikrinami:</p>
 * <ul>
 *   <li>Konstruktoriaus veikimas</li>
 *   <li>Getter ir Setter metodai</li>
 *   <li>Dalių sąrašo valdymas</li>
 *   <li>{@code toString} metodas</li>
 * </ul>
 *
 * @author kskrebe
 * @version 1.0
 */
@DisplayName("Car modelio testai")
class CarTest {

    /** Testuojamas automobilio objektas. */
    private Car car;

    /**
     * Parengiamas automobilio objektas prieš kiekvieną testą.
     * {@code @BeforeEach} užtikrina, kad kiekvienas testas prasideda švaria būsena.
     */
    @BeforeEach
    void setUp() {
        car = new Car("Toyota", "Corolla", 2022, 18500.0f, true, 'R');
    }

    /** Tikrina, ar tuščias konstruktorius sukuria objektą be klaidų. */
    @Test
    @DisplayName("Tuščias konstruktorius veikia")
    void testDefaultConstructor() {
        Car emptyCar = new Car();
        assertNotNull(emptyCar);
    }

    /** Tikrina, ar pilnas konstruktorius teisingai priskiria visas reikšmes. */
    @Test
    @DisplayName("Pilnas konstruktorius priskiria laukus")
    void testFullConstructor() {
        assertEquals("Toyota", car.getMake());
        assertEquals("Corolla", car.getModel());
        assertEquals(2022, car.getYear());
        assertEquals(18500.0f, car.getPrice(), 0.001f);
        assertTrue(car.isAvailable());
        assertEquals('R', car.getColorCode());
    }

    /** Tikrina ID getter ir setter. */
    @Test
    @DisplayName("ID getter/setter veikia")
    void testIdGetterSetter() {
        car.setId(10L);
        assertEquals(10L, car.getId());
    }

    /** Tikrina gamintojo getter ir setter. */
    @Test
    @DisplayName("Make (gamintojas) getter/setter veikia")
    void testMakeGetterSetter() {
        car.setMake("BMW");
        assertEquals("BMW", car.getMake());
    }

    /** Tikrina modelio getter ir setter. */
    @Test
    @DisplayName("Model getter/setter veikia")
    void testModelGetterSetter() {
        car.setModel("X5");
        assertEquals("X5", car.getModel());
    }

    /** Tikrina metų getter ir setter. */
    @Test
    @DisplayName("Year (metai) getter/setter veikia")
    void testYearGetterSetter() {
        car.setYear(2025);
        assertEquals(2025, car.getYear());
    }

    /** Tikrina kainos getter ir setter. */
    @Test
    @DisplayName("Price (kaina) getter/setter veikia")
    void testPriceGetterSetter() {
        car.setPrice(25000.0f);
        assertEquals(25000.0f, car.getPrice(), 0.001f);
    }

    /** Tikrina prieinamumo getter ir setter. */
    @Test
    @DisplayName("Available (prieinamumas) getter/setter veikia")
    void testAvailableGetterSetter() {
        car.setAvailable(false);
        assertFalse(car.isAvailable());
    }

    /** Tikrina spalvos kodo getter ir setter (char tipas). */
    @Test
    @DisplayName("ColorCode (char tipo) getter/setter veikia")
    void testColorCodeGetterSetter() {
        car.setColorCode('G');
        assertEquals('G', car.getColorCode());
    }

    /** Tikrina, ar dalių sąrašas yra tuščias po sukūrimo. */
    @Test
    @DisplayName("Dalių sąrašas inicijuojamas tuščias")
    void testPartsListInitiallyEmpty() {
        assertNotNull(car.getParts(), "Dalių sąrašas negali būti null");
        assertTrue(car.getParts().isEmpty(), "Naujas automobilis neturi dalių");
    }

    /** Tikrina dalių sąrašo setter. */
    @Test
    @DisplayName("Parts (dalys) setter veikia")
    void testPartsSetterAndGetter() {
        List<CarPart> parts = new ArrayList<>();
        parts.add(new CarPart("Variklis", "ENG-001", 3200.0f, 1, true));
        car.setParts(parts);

        assertEquals(1, car.getParts().size());
        assertEquals("Variklis", car.getParts().get(0).getName());
    }

    /** Tikrina, ar toString grąžina svarbius laukus. */
    @Test
    @DisplayName("toString() apima pagrindinius laukus")
    void testToString() {
        String result = car.toString();
        assertTrue(result.contains("Toyota"), "toString turi turėti gamintoją");
        assertTrue(result.contains("Corolla"), "toString turi turėti modelį");
        assertTrue(result.contains("2022"),    "toString turi turėti metus");
    }
}

