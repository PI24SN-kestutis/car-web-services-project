package lt.viko.eif.kskrebe.carclient.dto;

/**
 * DTO objektas automobilio detalei atvaizduoti.
 *
 * @param id detalės identifikatorius
 * @param name detalės pavadinimas
 * @param partNumber detalės numeris
 * @param price detalės kaina
 * @param quantity kiekis
 * @param available ar detalė prieinama
 */
public record CarPartView(
        Long id,
        String name,
        String partNumber,
        float price,
        int quantity,
        boolean available
) {
}
