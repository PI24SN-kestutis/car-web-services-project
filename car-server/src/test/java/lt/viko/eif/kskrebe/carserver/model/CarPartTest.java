package lt.viko.eif.kskrebe.carserver.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Vienetų testai {@link CarPart} modelio klasei.
 *
 * <p>Tikrinami:</p>
 * <ul>
 *   <li>Konstruktoriaus veikimas</li>
 *   <li>Getter ir Setter metodai</li>
 *   <li>Ryšys su {@link Car} (ManyToOne)</li>
 *   <li>{@code toString} metodas</li>
 * </ul>
 *
 * @author kskrebe
 * @version 1.0
 */
@DisplayName("CarPart modelio testai")
class CarPartTest {

    /** Testuojama automobilio dalis. */
    private CarPart part;

    /**
     * Parengiamas dalies objektas prieš kiekvieną testą.
     */
    @BeforeEach
    void setUp() {
        part = new CarPart("Variklis 1.6", "ENG-TOY-001", 3200.0f, 1, true);
    }

    /** Tikrina, ar tuščias konstruktorius sukuria objektą. */
    @Test
    @DisplayName("Tuščias konstruktorius veikia")
    void testDefaultConstructor() {
        CarPart emptyPart = new CarPart();
        assertNotNull(emptyPart);
    }

    /** Tikrina, ar pilnas konstruktorius teisingai priskiria visas reikšmes. */
    @Test
    @DisplayName("Pilnas konstruktorius priskiria laukus")
    void testFullConstructor() {
        assertEquals("Variklis 1.6", part.getName());
        assertEquals("ENG-TOY-001", part.getPartNumber());
        assertEquals(3200.0f, part.getPrice(), 0.001f);
        assertEquals(1, part.getQuantity());
        assertTrue(part.isAvailable());
    }

    /** Tikrina ID getter ir setter. */
    @Test
    @DisplayName("ID getter/setter veikia")
    void testIdGetterSetter() {
        part.setId(5L);
        assertEquals(5L, part.getId());
    }

    /** Tikrina pavadinimo getter ir setter. */
    @Test
    @DisplayName("Name (pavadinimas) getter/setter veikia")
    void testNameGetterSetter() {
        part.setName("Stabdžiai");
        assertEquals("Stabdžiai", part.getName());
    }

    /** Tikrina katalogo numerio getter ir setter. */
    @Test
    @DisplayName("PartNumber (katalogo nr.) getter/setter veikia")
    void testPartNumberGetterSetter() {
        part.setPartNumber("BRK-002");
        assertEquals("BRK-002", part.getPartNumber());
    }

    /** Tikrina kainos getter ir setter. */
    @Test
    @DisplayName("Price (kaina) getter/setter veikia")
    void testPriceGetterSetter() {
        part.setPrice(450.0f);
        assertEquals(450.0f, part.getPrice(), 0.001f);
    }

    /** Tikrina kiekio getter ir setter. */
    @Test
    @DisplayName("Quantity (kiekis) getter/setter veikia")
    void testQuantityGetterSetter() {
        part.setQuantity(5);
        assertEquals(5, part.getQuantity());
    }

    /** Tikrina prieinamumo getter ir setter. */
    @Test
    @DisplayName("Available (prieinamumas) getter/setter veikia")
    void testAvailableGetterSetter() {
        part.setAvailable(false);
        assertFalse(part.isAvailable());
    }

    /** Tikrina automobilio ryšio nustatymą ir gavimą. */
    @Test
    @DisplayName("Car ryšys (setCar/getCar) veikia")
    void testCarAssociation() {
        Car car = new Car("Toyota", "Corolla", 2022, 18500.0f, true, 'R');
        car.setId(1L);
        part.setCar(car);

        assertNotNull(part.getCar());
        assertEquals("Toyota", part.getCar().getMake());
        assertEquals(1L, part.getCar().getId());
    }

    /** Tikrina, kad iš pradžių automobilis nėra priskirtas. */
    @Test
    @DisplayName("Naujai sukurtai daliai automobilis nepriskirtas")
    void testCarInitiallyNull() {
        assertNull(part.getCar());
    }

    /** Tikrina, ar toString grąžina svarbius laukus. */
    @Test
    @DisplayName("toString() apima pagrindinius laukus")
    void testToString() {
        String result = part.toString();
        assertTrue(result.contains("Variklis 1.6"),  "toString turi turėti pavadinimą");
        assertTrue(result.contains("ENG-TOY-001"), "toString turi turėti katalogo numerį");
    }
}

