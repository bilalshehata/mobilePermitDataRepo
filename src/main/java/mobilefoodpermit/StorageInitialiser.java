package mobilefoodpermit;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import mobilefoodpermit.models.MobileFoodPermit;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.index.kdtree.KdNode;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class StorageInitialiser {
    final private static String dataLocation = "/Applications/IntelliJ IDEA.app/Contents/bin/https/github.com/bshehata/Rokt-interview/resources/Mobile_Food_Permit_Map.csv";

    public static void readInCSV(MobileFoodPermitStorage storage) throws IOException {
        Reader reader = new BufferedReader(new FileReader(dataLocation));

        CsvToBean csvReader = new CsvToBeanBuilder(reader)
                .withType(MobileFoodPermit.class)
                .withSeparator(',')
                .withIgnoreLeadingWhiteSpace(true)
                .withIgnoreEmptyLine(true)
                .build();
        List<MobileFoodPermit> results = csvReader.parse();

        results.forEach(x -> {
            MobileFoodPermitStorage.insert(x);
        });

    }
}
