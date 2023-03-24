package ch.epfl.javions.p5;

import ch.epfl.javions.adsb.AircraftIdentificationMessage;
import ch.epfl.javions.adsb.RawMessage;
import ch.epfl.javions.demodulation.AdsbDemodulator;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HexFormat;

import static org.junit.jupiter.api.Assertions.*;


public class aircraftIdentificationMessageTestPerso{
    private static final HexFormat hf = HexFormat.of().withUpperCase();

    @Test
    public void main() throws IOException {
        try (InputStream s = new FileInputStream("resources/samples_20230304_1442.bin")) {
            AdsbDemodulator d = new AdsbDemodulator(s);
            RawMessage m;
            AircraftIdentificationMessage im;
            int i = 0;

            while ((m = d.nextMessage()) != null) {
                im = AircraftIdentificationMessage.of(m);
                if (im != null && m.typeCode() <= 4 && m.typeCode() >= 1){
                    System.out.printf("msg %d : ",++i);
                    System.out.println(im);
                }
            }
        }
    }

    @Test
    void testMethodOfShouldNotValidateTypeCode() throws IOException{
        String correctOutput = "AircraftIdentificationMessage[timeStampNs=316898700, icaoAddress=IcaoAddress[string=4241A9], category=-79, callSign=CallSign[string=BF4M4HPL]]AircraftIdentificationMessage[timeStampNs=1447383100, icaoAddress=IcaoAddress[string=4D0221], category=-79, callSign=CallSign[string=DH4QNEPM]]AircraftIdentificationMessage[timeStampNs=1472930400, icaoAddress=IcaoAddress[string=4241A9], category=-79, callSign=CallSign[string=BF4M4HPK]]AircraftIdentificationMessage[timeStampNs=1499146900, icaoAddress=IcaoAddress[string=4D2228], category=163, callSign=CallSign[string=RYR7JD]]AircraftIdentificationMessage[timeStampNs=2012822900, icaoAddress=IcaoAddress[string=4D0221], category=-79, callSign=CallSign[string=DH4QNEPM]]AircraftIdentificationMessage[timeStampNs=2186271000, icaoAddress=IcaoAddress[string=4B2964], category=49, callSign=CallSign[string=RZZNU84E]]AircraftIdentificationMessage[timeStampNs=2240535600, icaoAddress=IcaoAddress[string=01024C], category=163, callSign=CallSign[string=MSC3361]]AircraftIdentificationMessage[timeStampNs=2516450900, icaoAddress=IcaoAddress[string=4241A9], category=-79, callSign=CallSign[string=BF4M4HPK]]AircraftIdentificationMessage[timeStampNs=2597642100, icaoAddress=IcaoAddress[string=4D0221], category=-79, callSign=CallSign[string=DH0QFE M]]AircraftIdentificationMessage[timeStampNs=2698727800, icaoAddress=IcaoAddress[string=495299], category=163, callSign=CallSign[string=TAP931]]AircraftIdentificationMessage[timeStampNs=2911496600, icaoAddress=IcaoAddress[string=4D0221], category=48, callSign=CallSign[string=D4YE25ZT]]AircraftIdentificationMessage[timeStampNs=3113140100, icaoAddress=IcaoAddress[string=4D0221], category=-79, callSign=CallSign[string=DH0QFE0M]]AircraftIdentificationMessage[timeStampNs=3215880100, icaoAddress=IcaoAddress[string=A4F239], category=165, callSign=CallSign[string=DAL153]]AircraftIdentificationMessage[timeStampNs=3358467600, icaoAddress=IcaoAddress[string=4B17E1], category=48, callSign=CallSign[string=U1LOY4FC]]AircraftIdentificationMessage[timeStampNs=3532024100, icaoAddress=IcaoAddress[string=4241A9], category=-79, callSign=CallSign[string=BF4M4G0K]]AircraftIdentificationMessage[timeStampNs=4103219900, icaoAddress=IcaoAddress[string=4B2964], category=161, callSign=CallSign[string=HBPRO]]AircraftIdentificationMessage[timeStampNs=4619251500, icaoAddress=IcaoAddress[string=4B1A00], category=162, callSign=CallSign[string=SAZ54]]AircraftIdentificationMessage[timeStampNs=4750361000, icaoAddress=IcaoAddress[string=4D0221], category=162, callSign=CallSign[string=SVW29VM]]AircraftIdentificationMessage[timeStampNs=5240059000, icaoAddress=IcaoAddress[string=4CA2BF], category=163, callSign=CallSign[string=RYR5907]]AircraftIdentificationMessage[timeStampNs=5444719600, icaoAddress=IcaoAddress[string=4BB279], category=48, callSign=CallSign[string=ZZJZ7YOP]]AircraftIdentificationMessage[timeStampNs=5715627900, icaoAddress=IcaoAddress[string=4B2964], category=-79, callSign=CallSign[string=DG LDD H]]AircraftIdentificationMessage[timeStampNs=6532434900, icaoAddress=IcaoAddress[string=4D2228], category=163, callSign=CallSign[string=RYR7JD]]AircraftIdentificationMessage[timeStampNs=6739638900, icaoAddress=IcaoAddress[string=4B2964], category=-79, callSign=CallSign[string=DG LDD H]]AircraftIdentificationMessage[timeStampNs=7009169300, icaoAddress=IcaoAddress[string=4D029F], category=161, callSign=CallSign[string=JFA17S]]AircraftIdentificationMessage[timeStampNs=7157583600, icaoAddress=IcaoAddress[string=39CEAA], category=160, callSign=CallSign[string=TVF7307]]AircraftIdentificationMessage[timeStampNs=7193198000, icaoAddress=IcaoAddress[string=4B17E5], category=163, callSign=CallSign[string=SWR6LH]]AircraftIdentificationMessage[timeStampNs=7203089700, icaoAddress=IcaoAddress[string=4BCDE9], category=163, callSign=CallSign[string=SXS5GH]]AircraftIdentificationMessage[timeStampNs=7208226400, icaoAddress=IcaoAddress[string=4B2964], category=-79, callSign=CallSign[string=DG LLD H]]AircraftIdentificationMessage[timeStampNs=7306755600, icaoAddress=IcaoAddress[string=440237], category=163, callSign=CallSign[string=EJU63HA]]";

        try (InputStream s = new FileInputStream("resources/samples_20230304_1442.bin")) {
            AdsbDemodulator d = new AdsbDemodulator(s);
            RawMessage m;
            AircraftIdentificationMessage im;

            StringBuilder output = new StringBuilder();
            while ((m = d.nextMessage()) != null) {
                im = AircraftIdentificationMessage.of(m);
                if (im != null){
                    output.append(im);
                }
            }

            /*assertEquals(correctOutput, output.toString());*/
        }
    }
}
