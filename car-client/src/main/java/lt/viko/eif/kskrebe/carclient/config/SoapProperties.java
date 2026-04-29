package lt.viko.eif.kskrebe.carclient.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * SOAP kliento konfigūracijos reikšmės, nuskaitomos iš
 * {@code application.properties} su prefiksu {@code carclient.soap}.
 *
 * @param wsdlUrl WSDL URL adresas
 * @param namespace SOAP vardų sritis (namespace)
 * @param serviceName paslaugos pavadinimas iš WSDL
 * @param portName porto pavadinimas iš WSDL
 */
@ConfigurationProperties(prefix = "carclient.soap")
public record SoapProperties(
        String wsdlUrl,
        String namespace,
        String serviceName,
        String portName
) {
}
