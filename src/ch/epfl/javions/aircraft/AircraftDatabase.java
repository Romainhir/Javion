package ch.epfl.javions.aircraft;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.zip.ZipFile;

public final class AircraftDatabase {

    private String fileName;

    public AircraftDatabase(String fileName) {
        Objects.requireNonNull(fileName);
        this.fileName = fileName;
    }

    public AircraftData get(IcaoAddress address) throws IOException {
        AircraftData aircraftData = null;
        try (ZipFile zip = new ZipFile(fileName); InputStream in = zip.getInputStream(zip.getEntry(fileName));
             BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
            String value;
            while ((value = reader.readLine()) != null) {
                String[] tab = value.split(",", -1);
                if (tab[0].equals(address.string())) {
                    aircraftData = new AircraftData(new AircraftRegistration(tab[1]), new AircraftTypeDesignator(tab[2]),
                            tab[3], new AircraftDescription(tab[4]), WakeTurbulenceCategory.of(tab[5]));
                    break;
                }
            }
        }
        return aircraftData;
    }

}
