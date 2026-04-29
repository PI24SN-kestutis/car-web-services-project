# car-web-services-project
Monorepo struktura `Car Web Services` darbui.
## Projektai
- `car-server/` - Spring Boot + JAX-WS + H2 serverio dalis (SOAP paslauga)
- `car-client/` - (SOAP klientas, wsimport, XSL transformacijos)
## Greitas startas (serveriui)
```powershell
cd car-server
.\mvnw.cmd test
.\mvnw.cmd spring-boot:run
```
## Naudingi adresai
- WSDL: `http://localhost:8080/services/cars?wsdl`
- H2: `http://localhost:8080/h2-console`
