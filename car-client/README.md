# `carclient` naudotojo vadovas

Šis projektas yra kliento pusės demonstracija užduočiai apie JAX-WS web servisus ir XSL transformacijas.

## 1. Projekto tikslas

Programa:

1. Prisijungia prie SOAP serverio (`carserver`) pagal WSDL.
2. Leidžia valdyti automobilius (CRUD) kartu su jų detalėmis.
3. Turi paiešką ir filtrus automobilių sąrašui.
4. Sugeneruoja XML failą iš gautų duomenų.
5. Transformuoja XML į HTML su XSL.
6. Transformuoja XML į PDF su XSL-FO.

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

Pagrindiniame puslapyje:

- matysite automobilių sąrašą,
- galėsite filtruoti pagal paiešką, prieinamumą ir kainą,
- galėsite kurti naują automobilį ir redaguoti esamą,
- detalės redaguojamos kartu su automobiliu vienoje formoje.

CRUD veiksmai:

- `+ Naujas automobilis` - atidaro kūrimo formą
- `Redaguoti` - atidaro redagavimo formą
- `Šalinti` - pašalina automobilį
- Išsaugojimas vyksta per **upsert** logiką (`addCar` SOAP operacija)

Eksporto mygtukai:

- `Sukurti XML` - sukuria `cars.xml`
- `Sukurti HTML` - sukuria `cars.xml` ir `cars.html`
- `Sukurti PDF` - sukuria `cars.xml` ir `cars.pdf`

Po kiekvieno veiksmo puslapyje rodoma žinutė apie rezultatą.

Sugeneruoti failai saugomi kataloge, nurodytame `carclient.output-dir` (numatytai `output/`).

## 6. Architektūra

### 6.1. Sluoksniai

- `web` - HTTP maršrutai, CRUD, filtrai, eksportas (`CarController`)
- `service` - verslo logika, mapinimas, upsert, filtravimas, eksportas (`CarService`, `ExportService`, `XmlExportService`, `TransformService`)
- `soap` - ryšys su SOAP paslauga (`CarSoapGateway`, `WsimportCarSoapGateway`)
- `dto` - vaizdavimo ir formų objektai (`CarView`, `CarPartView`, `CarForm`, `CarPartForm`, `CarFilterForm`)
- `config` - konfigūracijos nuskaitymas (`SoapProperties`)

### 6.2. Duomenų srautas

1. Vartotojas atidaro `/`.
2. `CarController` kviečia `CarService` ir pritaiko filtrus.
3. `CarService` per `CarSoapGateway` paima duomenis iš SOAP serverio.
4. Vartotojas kuria/redaguoja automobilį formoje `car-form.html`.
5. `CarController` kviečia `CarService.upsertCar(...)`, kuris SOAP pusėje kviečia `addCar` (upsert).
6. Eksporto metu `ExportService` sukuria XML, o `TransformService` sukuria HTML/PDF.

## 7. Testai

Projektas turi minimalius testus:

- Spring konteksto testas
- Serviso mapinimo/upsert testai
- Transformacijų testas (HTML/PDF failų generavimas)
- MVC valdiklio testai

Paleidimas:

```powershell
./mvnw test
```
