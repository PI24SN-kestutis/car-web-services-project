package lt.viko.eif.kskrebe.carclient.dto;

import java.util.List;

/**
 * DTO objektas automobiliui atvaizduoti kliento pusėje.
 *
 * @param id automobilio identifikatorius
 * @param make gamintojas
 * @param model modelis
 * @param year pagaminimo metai
 * @param price kaina
 * @param available ar automobilis prieinamas
 * @param colorCode simbolinis spalvos kodas
 * @param parts susijusių dalių sąrašas
 */
public record CarView(
        Long id,
        String make,
        String model,
        int year,
        float price,
        boolean available,
        char colorCode,
        List<CarPartView> parts
) {
}
