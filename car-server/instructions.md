# Client dalies instrukcija (brief)

Trumpa instrukcija, kaip sukurti `car-client` projektą, kuris:
1) kviečia SOAP servisą iš `car-server`,
2) gauna XML duomenis,
3) transformuoja juos į HTML ir PDF.

---

## 1. Tikslas

`car-client` turi atlikti šiuos veiksmus:
- Prisijungti prie WSDL: `http://localhost:8080/services/cars?wsdl`
- Sugeneruoti Java klientą (`wsimport`)
- Iškviesti bent metodus: `getAllCars`, `getCarById`
- Iš atsakymo suformuoti XML failą
- Pritaikyti `cars.xsl` (XML -> HTML)
- Pritaikyti `cars-fo.xsl` + Apache FOP (XML -> PDF)

---

## 2. Minimali projekto struktūra

```text
car-client/
  pom.xml
  src/main/java/.../client/
    CarSoapClientApplication.java
    SoapClientService.java
    XmlExportService.java
    TransformService.java
  src/main/resources/
    output/   (generuojami failai)
```

---

## 3. Priklausomybės (Maven)

`car-client/pom.xml` pridėk bent:
- `jakarta.xml.ws-api` (jei reikia)
- `org.apache.cxf:cxf-rt-frontend-jaxws` (arba naudok tik `wsimport` sugeneruotą klientą)
- `org.apache.xmlgraphics:fop` (PDF generavimui)
- `jaxb-runtime` (jei prireikia XML marshalling)

> Pastaba: jeigu klientą sugeneruoji su `wsimport`, dažnai pakanka JAXB/JAX-WS runtime bibliotekų.

---

## 4. Paleidimo seka

### 4.1 Paleisti serverį (`car-server`)

```powershell
cd car-server
.\mvnw.cmd spring-boot:run
```

### 4.2 Sugeneruoti SOAP klientą (`car-client`)

```powershell
cd ..\car-client
wsimport -keep -verbose -p lt.viko.eif.kskrebe.carclient.ws http://localhost:8080/services/cars?wsdl
```

Tai sugeneruos klases iš WSDL, kurias naudos klientas kvietimams.

### 4.3 Parašyti „runner“ klasę

`CarSoapClientApplication` (main) turėtų:
1. Sukurti serviso proxy iš sugeneruotų klasių
2. Iškviesti `getAllCars()`
3. Rezultatą išsaugoti kaip `cars.xml`
4. Transformuoti į `cars.html`
5. Transformuoti į `cars.pdf`

---

## 5. Transformacijos logika

### XML -> HTML
- XSL adresas: `http://localhost:8080/xsl/cars.xsl`
- Naudok `TransformerFactory` + `Transformer`
- Input: `cars.xml`, output: `cars.html`

### XML -> PDF
- XSL-FO adresas: `http://localhost:8080/xsl/cars-fo.xsl`
- Pirmiausia XML transformuoji į FO stream
- Tuomet Apache FOP sukuria PDF (`cars.pdf`)

---

## 6. Minimalus „definition of done"

`car-client` dalis laikoma padaryta, kai:
- [ ] Sėkmingai sugeneruotas SOAP klientas (`wsimport`)
- [ ] Veikia bent 2 SOAP kvietimai
- [ ] Sukuriamas `cars.xml`
- [ ] Sukuriamas `cars.html`
- [ ] Sukuriamas `cars.pdf`
- [ ] Yra trumpas `README.md` su paleidimo komandomis

---

## 7. Dažnos problemos

1. **`wsimport` nerastas**
   - Naudok JDK (ne tik JRE), arba IntelliJ „Terminal“ su teisingu `JAVA_HOME`.

2. **404 ant WSDL**
   - Patikrink, ar serveris paleistas: `http://localhost:8080/services/cars?wsdl`

3. **Tuščias PDF/HTML**
   - Patikrink ar XML failas turi duomenų (`getAllCars` negrąžina tuščio sąrašo).

4. **`colorCode` reikšmė skaičiumi**
   - Tai normalu (`char` JAXB serializuojamas kaip skaičius).

---

## 8. Ką daryti toliau

1. Sukurti `car-client` skeleton (`pom.xml`, `main` klasė, servisai)
2. Įkelti pirmą veikiančią versiją su `getAllCars -> cars.xml`
3. Pridėti XSL transformacijas (HTML/PDF)
4. Papildyti `car-client/README.md` su testavimo pavyzdžiais

