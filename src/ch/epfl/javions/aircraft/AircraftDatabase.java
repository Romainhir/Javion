package ch.epfl.javions.aircraft;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.zip.ZipFile;

/**
 * Class used to interact with the "mictronics" database.
 *
 * @author Romain Hirschi
 * @author Moussab Tasnim Ibrahim
 */
public final class AircraftDatabase {

    private String fileName;

    /**
     * Constructor of the database. Need the name of the resource of the database
     *
     * @param fileName (String) : the name of the resource of the database
     */
    public AircraftDatabase(String fileName) {
        this.fileName = Objects.requireNonNull(fileName);
    }

    /**
     * Get the information of a specific aircraft in the database that match the ICAO adress given in parameter.
     * The information are stored in an "AircraftData" object.
     * Throw an Exception if an error occur while reading the database files.
     *
     * @param address (IcaoAddress) : the ICAO address to match
     * @return (AircraftData) : the information on the aircraft
     * @throws IOException if an error occur while reading the database files
     */
    public AircraftData get(IcaoAddress address) throws IOException {
        AircraftData aircraftData = null;
        String file = URLDecoder.decode(fileName, StandardCharsets.UTF_8);
        try (ZipFile zip = new ZipFile(file); InputStream in = zip.getInputStream(zip.getEntry(address.string().substring(4) + ".csv"));
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
