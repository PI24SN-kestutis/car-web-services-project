package lt.viko.eif.kskrebe.carclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

/**
 * Pagrindinis kliento programos paleidimo taškas.
 * <p>
 * Klasė paleidžia Spring Boot aplikaciją ir įjungia konfigūracijos savybių
 * nuskaitymą iš {@code application.properties}.
 */
@SpringBootApplication
@ConfigurationPropertiesScan
public class CarclientApplication {

    /**
     * Paleidžia programą.
     *
     * @param args paleidimo argumentai
     */
    public static void main(String[] args) {
        SpringApplication.run(CarclientApplication.class, args);
    }

}
