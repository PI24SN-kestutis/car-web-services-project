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

- `Generate HTML` - sukuria `cars.html`
- `Generate PDF` - sukuria `cars.pdf`

Abiem atvejais prieš transformaciją automatiškai sukuriamas `cars.xml`.

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
5. Vartotojui paspaudus eksporto mygtuką:
   - `ExportService` sukuria `cars.xml`.
   - `TransformService` atlieka XSL arba XSL-FO transformaciją.
   - Gaunamas `cars.html` arba `cars.pdf`.

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

## 8. Koduotė ir lietuviški simboliai

- Rekomenduojama visus failus laikyti `UTF-8` koduote.
- Jei redaktoriuje matosi neteisingi simboliai (`ąčęėįšųūž`), patikrinkite failo koduotę ir nustatykite `UTF-8`.
- Šiame projekte dokumentacija ir komentarai pateikti lietuvių kalba, o kodo identifikatoriai palikti anglų kalba.
