# Carserver - JAX-WS SOAP tinklo paslauga

> **Užduotis:** Web Services - Assessment Task 2  
> **Technologijos:** Spring Boot 3.3, JAX-WS (Apache CXF), H2, JPA, JAXB, XSL, XSL-FO  
> **Java:** 21, **Maven:** 3.9

---

## Turinys

1. [Projekto aprašas](#projekto-aprašas)
2. [Architektūra](#architektūra)
3. [Paketo struktūra](#paketo-struktūra)
4. [Duomenų modelis](#duomenų-modelis)
5. [Kaip paleisti](#kaip-paleisti)
6. [SOAP naudojimas su SoapUI](#soap-naudojimas-su-soapui)
7. [H2 duomenų bazės konsolė](#h2-duomenų-bazės-konsolė)
8. [XSL transformacijos (kliento dalis)](#xsl-transformacijos-kliento-dalis)
9. [Testai](#testai)
10. [SOLID principai projekte](#solid-principai-projekte)

---

## Projekto aprašas

**Carserver** yra serverio pusės Spring Boot programa, kuri teikia duomenis apie automobilius ir jų dalis per **SOAP tinklo paslaugą** (JAX-WS protokolas).

Programa skirta studentams mokytis:
- **JAX-WS** - SOAP paslaugos kūrimas su Apache CXF
- **JAXB** - Java objektų serializacija į XML
- **JPA + H2** - duomenų saugojimas atminties duomenų bazėje
- **XSL / XSL-FO** - XML duomenų transformacija į HTML ir PDF

> **Serveris != klientas.** Šis projektas yra tik **serverio dalis**.  
> Kliento projektas (wsimport, XSL transformacijos, PDF) kuriamas atskirai.

---

## Architektūra

```text
SOAP klientas (SoapUI / wsimport / atskiras projektas)
    |
    | SOAP/HTTP (XML)
    v
carserver (Spring Boot)
    |
    +-- ws/          -> SOAP endpointai (JAX-WS)
    +-- service/     -> verslo logika
    +-- repository/  -> JPA prieiga prie H2
    +-- model/       -> Car ir CarPart
    +-- static/xsl/  -> XSL šablonai klientui
```

### Sluoksnių atsakomybės

| Sluoksnis | Paketas | Atsakomybė |
|-----------|---------|------------|
| Modelis | `model/` | Duomenų struktūra (JPA + JAXB) |
| Repozitorija | `repository/` | Duomenų bazės operacijos |
| Servisas | `service/` | Verslo logika |
| SOAP | `ws/` | SOAP protokolo apdorojimas |
| Konfigūracija | `config/` | Infrastruktūros nustatymai |

---

## Paketo struktūra

```text
src/
  main/
    java/lt/viko/eif/kskrebe/carserver/
      CarserverApplication.java
      model/
        Car.java
        CarPart.java
      repository/
        CarRepository.java
      service/
        CarService.java
        CarServiceImpl.java
      ws/
        CarWebService.java
        CarWebServiceImpl.java
      config/
        WebServiceConfig.java
        DataInitializer.java
    resources/
      application.properties
      static/xsl/
        cars.xsl
        cars-fo.xsl
  test/
    java/lt/viko/eif/kskrebe/carserver/
      CarserverApplicationTests.java
      model/
        CarTest.java
        CarPartTest.java
      service/
        CarServiceImplTest.java
      ws/
        CarWebServiceImplTest.java
```

---

## Duomenų modelis

### `Car` - automobilis

| Laukas | Tipas | Aprašas |
|--------|-------|---------|
| `id` | `Long` | Automatiškai generuojamas ID |
| `make` | `String` | Gamintojas, pvz., `Toyota` |
| `model` | `String` | Modelis, pvz., `Corolla` |
| `year` | `int` | Pagaminimo metai |
| `price` | `float` | Kaina eurais |
| `available` | `boolean` | Ar galima pirkti |
| `colorCode` | `char` | Spalvos kodas: `R`, `B`, `G`, `S` |
| `parts` | `List<CarPart>` | Dalių sąrašas (One-to-Many) |

### `CarPart` - automobilio dalis

| Laukas | Tipas | Aprašas |
|--------|-------|---------|
| `id` | `Long` | Automatiškai generuojamas ID |
| `name` | `String` | Pavadinimas, pvz., `Variklis` |
| `partNumber` | `String` | Katalogo numeris, pvz., `ENG-001` |
| `price` | `float` | Kaina eurais |
| `quantity` | `int` | Kiekis sandėlyje |
| `available` | `boolean` | Ar galima užsakyti |
| `car` | `Car` | Susijęs automobilis (`@XmlTransient`) |

> **Pastaba apie `char` su JAXB:** `char` serializuojamas kaip skaičius (`xs:unsignedShort`).  
> Pvz., `'R'` atsakyme gali būti `82`.

---

## Kaip paleisti

### Reikalavimai

- Java 21
- Maven Wrapper (`mvnw.cmd`) jau yra projekte

### 1) Kompiliuoti

```powershell
cd "C:\Users\kestu\Documents\VIKO\2026-pavasaris\saitynas_Dmitrijev\2-as_pp\carserver"
.\mvnw.cmd compile
```

### 2) Paleisti testus

```powershell
.\mvnw.cmd test
```

### 3) Paleisti serverį

```powershell
.\mvnw.cmd spring-boot:run
```

### 4) Patikrinti naršyklėje

| Adresas | Tikslas |
|---------|---------|
| `http://localhost:8080/services/cars?wsdl` | WSDL dokumentas |
| `http://localhost:8080/services` | CXF endpointų sąrašas |
| `http://localhost:8080/h2-console` | H2 konsolė |
| `http://localhost:8080/xsl/cars.xsl` | XML -> HTML XSL |
| `http://localhost:8080/xsl/cars-fo.xsl` | XML -> PDF (XSL-FO) |

---

## SOAP naudojimas su SoapUI

### Žingsniai

1. Atidaryti `SoapUI` -> `File` -> `New SOAP Project`
2. Įvesti projekto pavadinimą, pvz., `CarService`
3. `Initial WSDL`: `http://localhost:8080/services/cars?wsdl`
4. Spausti `OK`

### Pagrindinės operacijos

- `getAllCars`
- `getCarById`
- `addCar`
- `deleteCar`

### `wsimport` (kliento generavimas)

```powershell
wsimport -keep -verbose http://localhost:8080/services/cars?wsdl
```

---

## H2 duomenų bazės konsolė

Adresas: `http://localhost:8080/h2-console`

| Laukas | Reikšmė |
|--------|---------|
| Driver Class | `org.h2.Driver` |
| JDBC URL | `jdbc:h2:mem:cardb` |
| User Name | `sa` |
| Password | *(tuščia)* |

Naudingos užklausos:

```sql
SELECT * FROM CARS;

SELECT cp.*, c.MAKE, c.MODEL
FROM CAR_PARTS cp
JOIN CARS c ON cp.CAR_ID = c.ID;
```

---

## XSL transformacijos (kliento dalis)

Serveris teikia du XSL šablonus:
- `http://localhost:8080/xsl/cars.xsl` (XML -> HTML)
- `http://localhost:8080/xsl/cars-fo.xsl` (XML -> XSL-FO -> PDF)

Klientas:
1. gauna XML iš SOAP atsakymo,
2. parsisiunčia XSL šabloną,
3. atlieka transformaciją kliento pusėje.

---

## Testai

### Testų klasės

- `CarTest`
- `CarPartTest`
- `CarServiceImplTest`
- `CarWebServiceImplTest`
- `CarserverApplicationTests`

Paleidimas:

```powershell
.\mvnw.cmd test
```

---

## SOLID principai projekte

| Principas | Kaip pritaikytas |
|-----------|------------------|
| Single Responsibility | Kiekvienas sluoksnis turi aiškią atsakomybę |
| Open/Closed | Naudojamos sąsajos (`CarService`, `CarWebService`) |
| Liskov Substitution | `CarServiceImpl` laikosi `CarService` kontrakto |
| Interface Segregation | Mažos, aiškios sąsajos |
| Dependency Inversion | Priklausomybės injektuojamos per Spring |

---

Projektas sukurtas mokymosi tikslais, VIKO EIF, 2026.
