package mobilefoodpermit.storage;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import mobilefoodpermit.models.MobileFoodPermit;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

public class StorageFactory {

    /***
     *  function initiates a storage system based on a CSV file
     *  see application.properties for csv file location
     * @param dataLocation
     * @return
     */
    public static MobileFoodPermitStorage fromCSV(String dataLocation) {
        MobileFoodPermitStorage storage = new MobileFoodPermitStorage();
        Reader reader = new BufferedReader(new InputStreamReader(StorageFactory.class.getResourceAsStream(dataLocation)));
        CsvToBean csvReader = new CsvToBeanBuilder(reader)
                .withType(MobileFoodPermit.class)
                .withSeparator(',')
                .withIgnoreLeadingWhiteSpace(true)
                .withIgnoreEmptyLine(true)
                .build();
        List<MobileFoodPermit> results = csvReader.parse();

        results.forEach(storage::insert);
        return storage;
    }
}
