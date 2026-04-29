# Carserver â€“ JAX-WS SOAP tinklo paslauga

> **UÅ¾duotis:** Web Services â€“ Assessment Task 2  
> **Technologijos:** Spring Boot 3.3 Â· JAX-WS (Apache CXF) Â· H2 Â· JPA Â· JAXB Â· XSL Â· XSL-FO  
> **Java:** 21 Â· **Maven:** 3.9

---

## ðŸ“‹ Turinys

1. [Projekto apraÅ¡as](#projekto-apraÅ¡as)
2. [ArchitektÅ«ra](#architektÅ«ra)
3. [Paketo struktÅ«ra](#paketo-struktÅ«ra)
4. [DuomenÅ³ modelis](#duomenÅ³-modelis)
5. [Kaip paleisti](#kaip-paleisti)
6. [SOAP naudojimas su SoapUI](#soap-naudojimas-su-soapui)
7. [H2 duomenÅ³ bazÄ—s konsolÄ—](#h2-duomenÅ³-bazÄ—s-konsolÄ—)
8. [XSL transformacijos (kliento dalis)](#xsl-transformacijos-kliento-dalis)
9. [Testai](#testai)
10. [SOLID principai projekte](#solid-principai-projekte)

---

## Projekto apraÅ¡as

**Carserver** yra serverio pusÄ—s Spring Boot programa, kuri teikia duomenis apie automobilius ir jÅ³ dalis per **SOAP tinklo paslaugÄ…** (JAX-WS protokolas).

Programa skirta studentams mokytis:
- **JAX-WS** â€“ SOAP paslaugos kÅ«rimas su Apache CXF
- **JAXB** â€“ Java objektÅ³ serializacija Ä¯ XML
- **JPA + H2** â€“ duomenÅ³ saugojimas atminties duomenÅ³ bazÄ—je
- **XSL / XSL-FO** â€“ XML duomenÅ³ transformacija Ä¯ HTML ir PDF

> **Serveris â‰  Klientas.** Å is projektas yra tik **serverio dalis**.  
> Kliento projektas (wsimport, XSL transformacijos, PDF) kuriamas atskirai.

---

## ArchitektÅ«ra

```
â”Œâ”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”
â”‚                   SOAP Klientas                      â”‚
â”‚         (SoapUI / wsimport / atskiras projektas)     â”‚
â””â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”¬â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”˜
                       â”‚ SOAP/HTTP uÅ¾klausa (XML)
                       â–¼
â”Œâ”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”
â”‚               carserver (Å¡is projektas)              â”‚
â”‚                                                      â”‚
â”‚  â”Œâ”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”    â”‚
â”‚  â”‚          ws/ â€“ SOAP sluoksnis                â”‚    â”‚
â”‚  â”‚   CarWebService (interface + @WebService)    â”‚    â”‚
â”‚  â”‚   CarWebServiceImpl (@Component + @WebServiceâ”‚    â”‚
â”‚  â”‚   Apache CXF â†’ generuoja WSDL automatiÅ¡kai  â”‚    â”‚
â”‚  â””â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”¬â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”˜    â”‚
â”‚                     â”‚ deleguoja                      â”‚
â”‚  â”Œâ”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â–¼â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”    â”‚
â”‚  â”‚        service/ â€“ verslo logika              â”‚    â”‚
â”‚  â”‚   CarService (interface)                     â”‚    â”‚
â”‚  â”‚   CarServiceImpl (@Service)                  â”‚    â”‚
â”‚  â””â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”¬â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”˜    â”‚
â”‚                     â”‚ deleguoja                      â”‚
â”‚  â”Œâ”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â–¼â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”    â”‚
â”‚  â”‚       repository/ â€“ duomenÅ³ prieiga          â”‚    â”‚
â”‚  â”‚   CarRepository (JpaRepository<Car, Long>)   â”‚    â”‚
â”‚  â””â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”¬â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”˜    â”‚
â”‚                     â”‚ SQL                            â”‚
â”‚  â”Œâ”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â–¼â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”    â”‚
â”‚  â”‚          H2 in-memory duomenÅ³ bazÄ—           â”‚    â”‚
â”‚  â”‚   LentelÄ—s: CARS, CAR_PARTS                  â”‚    â”‚
â”‚  â””â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”˜    â”‚
â”‚                                                      â”‚
â”‚  â”Œâ”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”    â”‚
â”‚  â”‚   static/xsl/ â€“ XSL Å¡ablonai klientui       â”‚    â”‚
â”‚  â”‚   cars.xsl     â†’ XML Ä¯ HTML                  â”‚    â”‚
â”‚  â”‚   cars-fo.xsl  â†’ XML Ä¯ PDF (XSL-FO + FOP)   â”‚    â”‚
â”‚  â””â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”˜    â”‚
â””â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”˜
```

### SluoksniÅ³ atsakomybÄ—s (SOLID â€“ Single Responsibility)

| Sluoksnis | Paketas | AtsakomybÄ— |
|-----------|---------|------------|
| Modelis | `model/` | DuomenÅ³ struktÅ«ra (JPA + JAXB) |
| Repozitorija | `repository/` | Tik duomenÅ³ bazÄ—s operacijos |
| Servisas | `service/` | Tik verslo logika |
| SOAP | `ws/` | Tik SOAP protokolo apdorojimas |
| KonfigÅ«racija | `config/` | Tik infrastruktÅ«ros nustatymai |

---

## Paketo struktÅ«ra

```
src/
â”œâ”€â”€ main/
â”‚   â”œâ”€â”€ java/lt/viko/eif/kskrebe/carserver/
â”‚   â”‚   â”œâ”€â”€ CarserverApplication.java       â† Spring Boot paleidimo taÅ¡kas
â”‚   â”‚   â”œâ”€â”€ model/
â”‚   â”‚   â”‚   â”œâ”€â”€ Car.java                    â† Automobilio entitija (JPA + JAXB)
â”‚   â”‚   â”‚   â””â”€â”€ CarPart.java                â† Dalies entitija (JPA + JAXB)
â”‚   â”‚   â”œâ”€â”€ repository/
â”‚   â”‚   â”‚   â””â”€â”€ CarRepository.java          â† Spring Data JPA repozitorija
â”‚   â”‚   â”œâ”€â”€ service/
â”‚   â”‚   â”‚   â”œâ”€â”€ CarService.java             â† Verslo logikos sÄ…saja
â”‚   â”‚   â”‚   â””â”€â”€ CarServiceImpl.java         â† Verslo logikos implementacija
â”‚   â”‚   â”œâ”€â”€ ws/
â”‚   â”‚   â”‚   â”œâ”€â”€ CarWebService.java          â† JAX-WS SOAP sÄ…saja
â”‚   â”‚   â”‚   â””â”€â”€ CarWebServiceImpl.java      â† JAX-WS SOAP implementacija
â”‚   â”‚   â””â”€â”€ config/
â”‚   â”‚       â”œâ”€â”€ WebServiceConfig.java       â† CXF endpoint registracija
â”‚   â”‚       â””â”€â”€ DataInitializer.java        â† Pradiniai bandomieji duomenys
â”‚   â””â”€â”€ resources/
â”‚       â”œâ”€â”€ application.properties          â† H2, JPA, CXF konfigÅ«racija
â”‚       â””â”€â”€ static/xsl/
â”‚           â”œâ”€â”€ cars.xsl                    â† XML â†’ HTML Å¡ablonas
â”‚           â””â”€â”€ cars-fo.xsl                 â† XML â†’ PDF (XSL-FO) Å¡ablonas
â””â”€â”€ test/
    â””â”€â”€ java/lt/viko/eif/kskrebe/carserver/
        â”œâ”€â”€ CarserverApplicationTests.java  â† Integracinis testas
        â”œâ”€â”€ model/
        â”‚   â”œâ”€â”€ CarTest.java                â† Car modelio testai
        â”‚   â””â”€â”€ CarPartTest.java            â† CarPart modelio testai
        â”œâ”€â”€ service/
        â”‚   â””â”€â”€ CarServiceImplTest.java     â† Serviso testai (Mockito)
        â””â”€â”€ ws/
            â””â”€â”€ CarWebServiceImplTest.java  â† SOAP testai (Mockito)
```

---

## DuomenÅ³ modelis

### `Car` â€“ Automobilis

| Laukas | Tipas | ApraÅ¡as |
|--------|-------|---------|
| `id` | `Long` | AutomatiÅ¡kai generuojamas ID |
| `make` | `String` | Gamintojas, pvz., "Toyota" |
| `model` | `String` | Modelis, pvz., "Corolla" |
| `year` | `int` | Pagaminimo metai |
| `price` | `float` | Kaina eurais |
| `available` | `boolean` | Ar galima pirkti |
| `colorCode` | `char` | Spalvos kodas: `R`, `B`, `G`, `S` |
| `parts` | `List<CarPart>` | DaliÅ³ sÄ…raÅ¡as (One-to-Many) |

### `CarPart` â€“ Automobilio dalis

| Laukas | Tipas | ApraÅ¡as |
|--------|-------|---------|
| `id` | `Long` | AutomatiÅ¡kai generuojamas ID |
| `name` | `String` | Pavadinimas, pvz., "Variklis" |
| `partNumber` | `String` | Katalogo numeris, pvz., "ENG-001" |
| `price` | `float` | Kaina eurais |
| `quantity` | `int` | Kiekis sandÄ—lyje |
| `available` | `boolean` | Ar galima uÅ¾sakyti |
| `car` | `Car` | SusijÄ™s automobilis (`@XmlTransient`) |

> **Pastaba dÄ—l `char` tipo su JAXB:** JAXB serializuoja `char` kaip `xs:unsignedShort` (skaiÄiÅ³).  
> Pvz., `'R'` XML atsakyme bus rodomas kaip `82` (Unicode kodo taÅ¡kas).  
> Klientas konvertuoja atgal: `char c = (char) 82; // â†’ 'R'`

### ER diagrama

```
â”Œâ”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”         â”Œâ”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”
â”‚      CARS        â”‚         â”‚     CAR_PARTS       â”‚
â”œâ”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”¤    1    â”œâ”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”¤
â”‚ id (PK)          â”‚â—„â”€â”€â”€â”€â”€â”€â”€â”€â”‚ id (PK)            â”‚
â”‚ make             â”‚    âˆž    â”‚ name               â”‚
â”‚ model            â”‚         â”‚ part_number        â”‚
â”‚ year             â”‚         â”‚ price              â”‚
â”‚ price            â”‚         â”‚ quantity           â”‚
â”‚ available        â”‚         â”‚ available          â”‚
â”‚ color_code       â”‚         â”‚ car_id (FK)        â”‚
â””â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”˜         â””â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”˜
```

---

## Kaip paleisti

### Reikalavimai

- **Java 21** â€“ patikrinti: `java -version`
- **Maven** â€“ naudojame `mvnw.cmd` (Ä¯traukta Ä¯ projektÄ…, atsisiÅ³sti nereikia)

### 1. Projekto kompiliavimas

```powershell
cd car-server
.\mvnw.cmd compile
```

### 2. TestÅ³ paleidimas

```powershell
.\mvnw.cmd test
```

Laukiamas rezultatas:
```
Tests run: 35, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

### 3. Serverio paleidimas

```powershell
.\mvnw.cmd spring-boot:run
```

SÄ—kmingo paleidimo Å¾inutÄ—s konsolÄ—je:
```
>>> Pradiniai duomenys Ä¯kelti: 3 automobiliai, 5 dalys.
>>> WSDL: http://localhost:8080/services/cars?wsdl
>>> H2 konsolÄ—: http://localhost:8080/h2-console
```

### 4. Tikrinimas narÅ¡yklÄ—je

| Adresas | Tikslas |
|---------|---------|
| `http://localhost:8080/services/cars?wsdl` | WSDL dokumentas |
| `http://localhost:8080/services` | VisÅ³ CXF endpointÅ³ sÄ…raÅ¡as |
| `http://localhost:8080/h2-console` | H2 duomenÅ³ bazÄ—s konsolÄ— |
| `http://localhost:8080/xsl/cars.xsl` | HTML transformacijos Å¡ablonas |
| `http://localhost:8080/xsl/cars-fo.xsl` | PDF (XSL-FO) Å¡ablonas |

---

## SOAP naudojimas su SoapUI

### Å½ingsniai SoapUI programoje

1. **Atidaryti SoapUI** â†’ `File` â†’ `New SOAP Project`
2. **Project Name:** `CarService`
3. **Initial WSDL:** `http://localhost:8080/services/cars?wsdl`
4. Spausti **OK** â€“ SoapUI automatiÅ¡kai sugeneruos visas operacijas

### SOAP operacijos

#### `getAllCars` â€“ gauti visus automobilius

UÅ¾klausa:
```xml
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                  xmlns:ws="http://ws.carserver.kskrebe.eif.viko.lt/">
   <soapenv:Header/>
   <soapenv:Body>
      <ws:getAllCars/>
   </soapenv:Body>
</soapenv:Envelope>
```

#### `getCarById` â€“ gauti automobilÄ¯ pagal ID

UÅ¾klausa:
```xml
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                  xmlns:ws="http://ws.carserver.kskrebe.eif.viko.lt/">
   <soapenv:Header/>
   <soapenv:Body>
      <ws:getCarById>
         <id>1</id>
      </ws:getCarById>
   </soapenv:Body>
</soapenv:Envelope>
```

#### `addCar` â€“ pridÄ—ti naujÄ… automobilÄ¯

UÅ¾klausa:
```xml
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                  xmlns:ws="http://ws.carserver.kskrebe.eif.viko.lt/">
   <soapenv:Header/>
   <soapenv:Body>
      <ws:addCar>
         <car>
            <make>Audi</make>
            <model>A4</model>
            <year>2024</year>
            <price>42000.0</price>
            <available>true</available>
            <colorCode>83</colorCode>
         </car>
      </ws:addCar>
   </soapenv:Body>
</soapenv:Envelope>
```
> `colorCode` reikÅ¡mÄ— â€“ Unicode skaiÄius: `82`='R', `66`='B', `71`='G', `83`='S'

#### `deleteCar` â€“ iÅ¡trinti automobilÄ¯

UÅ¾klausa:
```xml
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                  xmlns:ws="http://ws.carserver.kskrebe.eif.viko.lt/">
   <soapenv:Header/>
   <soapenv:Body>
      <ws:deleteCar>
         <id>1</id>
      </ws:deleteCar>
   </soapenv:Body>
</soapenv:Envelope>
```

### `wsimport` â€“ kliento generavimas

Kliento projekte SOAP klientÄ… galima sugeneruoti automatiÅ¡kai:

```powershell
wsimport -keep -verbose http://localhost:8080/services/cars?wsdl
```

Generuojami Java failai, kuriuos naudoja kliento projektas SOAP uÅ¾klausoms siÅ³sti.

---

## H2 duomenÅ³ bazÄ—s konsolÄ—

Serveris veikiant pasiekiama adresu: `http://localhost:8080/h2-console`

| Laukas | ReikÅ¡mÄ— |
|--------|---------|
| **Driver Class** | `org.h2.Driver` |
| **JDBC URL** | `jdbc:h2:mem:cardb` |
| **User Name** | `sa` |
| **Password** | *(tuÅ¡Äia)* |

Naudingos SQL uÅ¾klausos tikrinimui:
```sql
-- Visi automobiliai
SELECT * FROM CARS;

-- Visos dalys su automobilio pavadinimu
SELECT cp.*, c.MAKE, c.MODEL
FROM CAR_PARTS cp
JOIN CARS c ON cp.CAR_ID = c.ID;
```

> H2 duomenÅ³ bazÄ— veikia **atmintyje** â€“ paleidus serverÄ¯ iÅ¡ naujo, duomenys prarandami.

---

## XSL transformacijos (kliento dalis)

Serveris teikia du XSL Å¡ablonus kaip statinius failus. Klientas juos parsiunÄia ir naudoja XML transformacijai.

### `cars.xsl` â€“ XML Ä¯ HTML

```
http://localhost:8080/xsl/cars.xsl
```

Naudojimas kliento Java kode:
```java
// 1. Gauti XML iÅ¡ SOAP atsakymo
// 2. ParsisiÅ³sti XSL Å¡ablonÄ…
// 3. Transformuoti

TransformerFactory factory = TransformerFactory.newInstance();
Source xsl = new StreamSource(new URL("http://localhost:8080/xsl/cars.xsl").openStream());
Transformer transformer = factory.newTransformer(xsl);
transformer.transform(new StreamSource(xmlInput), new StreamResult(htmlOutput));
```

### `cars-fo.xsl` â€“ XML Ä¯ PDF (XSL-FO + Apache FOP)

```
http://localhost:8080/xsl/cars-fo.xsl
```

Naudojimas kliento Java kode su Apache FOP:
```java
// 1. Transformuoti XML â†’ FO su cars-fo.xsl (JAXP)
// 2. Konvertuoti FO â†’ PDF su Apache FOP

FopFactory fopFactory = FopFactory.newInstance(new File(".").toURI());
FOUserAgent userAgent = fopFactory.newFOUserAgent();
Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, userAgent, pdfOutputStream);
Transformer transformer = /* ... */;
transformer.transform(xmlSource, new SAXResult(fop.getDefaultHandler()));
```

---

## Testai

### TestÅ³ apÅ¾valga

| Testas | Tipas | TestÅ³ sk. | ApraÅ¡as |
|--------|-------|-----------|---------|
| `CarTest` | VienetÅ³ | 12 | `Car` modelio getter/setter, konstruktoriaus testai |
| `CarPartTest` | VienetÅ³ | 11 | `CarPart` modelio testai, ryÅ¡io su `Car` tikrinimas |
| `CarServiceImplTest` | VienetÅ³ (Mockito) | 6 | Serviso logika su imitacine repozitorija |
| `CarWebServiceImplTest` | VienetÅ³ (Mockito) | 5 | SOAP sluoksnis su imitaciniu servisu |
| `CarserverApplicationTests` | Integracinis | 1 | Spring konteksto paleidimas |
| **Viso** | | **35** | **Visi praeina âœ…** |

### Paleidimas

```powershell
# Visi testai
.\mvnw.cmd test

# Tik konkretus testas
.\mvnw.cmd test -Dtest=CarServiceImplTest

# Testai su ataskaitÄ…
.\mvnw.cmd test surefire-report:report
```

### Mockito Å¡ablonas (kaip veikia)

```
â”Œâ”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”
â”‚   CarServiceImplTestâ”‚
â”‚                     â”‚
â”‚   @Mock             â”‚
â”‚   CarRepository     â”‚  â† Imitacija (fake DB)
â”‚   â†• when/verify     â”‚
â”‚   @InjectMocks      â”‚
â”‚   CarServiceImpl    â”‚  â† Tikras testuojamas kodas
â””â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”˜
```

---

## SOLID principai projekte

| Principas | AngliÅ¡kai | Kaip pritaikytas |
|-----------|-----------|-----------------|
| **S** | Single Responsibility | Kiekviena klasÄ— atsakinga uÅ¾ vienÄ… dalykÄ…: `CarRepository` â€“ tik DB, `CarService` â€“ tik logika, `CarWebServiceImpl` â€“ tik SOAP |
| **O** | Open/Closed | `CarService` â€“ sÄ…saja. Norint pridÄ—ti naujÄ… elgsenÄ… â€“ kuriama nauja implementacija, ne keiÄiama esama |
| **L** | Liskov Substitution | `CarServiceImpl` visiÅ¡kai realizuoja `CarService` sutartÄ¯ â€“ galima pakeisti be pasekmiÅ³ |
| **I** | Interface Segregation | `CarService` ir `CarWebService` â€“ atskiros, maÅ¾os sÄ…sajos |
| **D** | Dependency Inversion | Visos priklausomybÄ—s Ä¯terpiamos per konstruktoriÅ³ (`@Autowired`), ne sukuriamos `new` raktaÅ¾odÅ¾iu |

---

## PriklausomybÄ—s (`pom.xml`)

| Biblioteka | Versija | Paskirtis |
|-----------|---------|-----------|
| `spring-boot-starter-web` | 3.3.5 | HTTP serveris (Tomcat) |
| `spring-boot-starter-data-jpa` | 3.3.5 | JPA / Hibernate |
| `h2` | 3.3.5 | In-memory duomenÅ³ bazÄ— |
| `cxf-spring-boot-starter-jaxws` | 4.0.5 | JAX-WS SOAP su Apache CXF |
| `fop` | 2.9 | XSL-FO â†’ PDF (Apache FOP) |
| `spring-boot-starter-test` | 3.3.5 | JUnit 5 + Mockito + AssertJ |

---

*Projektas sukurtas mokymosi tikslais, VIKO EIF, 2026.*


