# `carclient` naudotojo vadovas

Šis projektas yra minimali kliento pusės demonstracija užduočiai apie JAX-WS web servisus ir XSL transformacijas.

## 1. Projekto tikslas

Programa:

1. Prisijungia prie SOAP serverio (`carserver`) pagal WSDL.
2. Parodo automobilių duomenis `Thymeleaf` puslapyje.
3. Sugeneruoja XML failą iš gautų duomenų.
4. Transformuoja XML į HTML su XSL.
5. Transformuoja XML į PDF su XSL-FO.

## 2. Reikalavimai paleidimui

- Java 21
- Maven (arba projekto `mvnw` skriptas)
- Paleistas serverio projektas (`carserver`)

Numatytieji serverio adresai:

- WSDL: `http://localhost:8080/services/cars?wsdl`
- SOAP endpoint: `http://localhost:8080/services/cars`

Kliento aplikacijos portas:

- `http://localhost:8081/`

## 3. Konfigūracija

Failas: `src/main/resources/application.properties`

Pagrindinės savybės:

- `carclient.soap.wsdl-url` - WSDL adresas
- `carclient.soap.namespace` - SOAP namespace
- `carclient.soap.service-name` - paslaugos pavadinimas
- `carclient.soap.port-name` - porto pavadinimas
- `carclient.output-dir` - katalogas sugeneruotiems failams

## 4. Paleidimas

1) Įsitikinkite, kad `carserver` jau paleistas.

2) Iš `carclient` katalogo vykdykite:

```powershell
./mvnw test
./mvnw spring-boot:run
```

3) Naršyklėje atidarykite:

- `http://localhost:8081/`

## 5. Naudojimas

Pagrindiniame puslapyje matysite automobilių sąrašą.

Mygtukai:

- `Sukurti XML` - sukuria `cars.xml`
- `Sukurti HTML` - sukuria `cars.xml` ir `cars.html`
- `Sukurti PDF` - sukuria `cars.xml` ir `cars.pdf`

Po kiekvieno veiksmo puslapyje rodoma žinutė apie sukurtus failus.

Sugeneruoti failai saugomi kataloge, nurodytame `carclient.output-dir` (numatytai `output/`).

## 6. Architektūra

### 6.1. Sluoksniai

- `web` - HTTP užklausos ir Thymeleaf puslapis (`CarController`)
- `service` - verslo logika ir eksporto valdymas (`CarService`, `ExportService`, `XmlExportService`, `TransformService`)
- `soap` - ryšys su SOAP paslauga (`CarSoapGateway`, `WsimportCarSoapGateway`)
- `dto` - duomenų objektai vaizdavimui (`CarView`, `CarPartView`)
- `config` - konfigūracijos nuskaitymas (`SoapProperties`)

### 6.2. Duomenų srautas

1. Vartotojas atidaro `/`.
2. `CarController` kviečia `CarService`.
3. `CarService` per `CarSoapGateway` paima duomenis iš SOAP serverio.
4. Duomenys konvertuojami į `DTO` ir atvaizduojami `cars.html` (Thymeleaf šablonas).
5. Vartotojui paspaudus mygtuką:
   - `Sukurti XML`: `ExportService` sukuria `cars.xml`.
   - `Sukurti HTML`: `ExportService` sukuria `cars.xml`, `TransformService` sukuria `cars.html`.
   - `Sukurti PDF`: `ExportService` sukuria `cars.xml`, `TransformService` sukuria `cars.pdf`.
6. Po peradresavimo pagrindiniame puslapyje parodoma sėkmės arba klaidos žinutė.

## 7. Testai

Projektas turi minimalius testus:

- Spring konteksto testas
- Serviso mapinimo testas
- Transformacijų testas (HTML/PDF failų generavimas)
- MVC valdiklio testas

Paleidimas:

```powershell
./mvnw test
```
