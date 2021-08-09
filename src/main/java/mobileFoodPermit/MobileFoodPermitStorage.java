package mobileFoodPermit;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import mobileFoodPermit.gps.GPSLocation;
import mobileFoodPermit.models.MobileFoodPermit;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class MobileFoodPermitStorage {
    //concurrent hash table to support multiple writes, in memory data storage until database is implemented.
    //locationID uniquely identifies each row therefore it will be used as a key.
    public static ConcurrentHashMap<String, MobileFoodPermit> storage;
    //location of CSV to load in data
    final String dataLocation = "/Applications/IntelliJ IDEA.app/Contents/bin/https/github.com/bshehata/Rokt-interview/src/main/java/mobileFoodPermit/Mobile_Food_Permit_Map.csv";
    final static Integer AUTOFILL_OPTIONS_LENGTH = 5;
    MobileFoodPermitStorage() throws IOException {
        storage = new ConcurrentHashMap<>();
        this.readInCSV();
    }

    public static ConcurrentHashMap<String, MobileFoodPermit> getStorage() {
        return storage;
    }

    public static MobileFoodPermit getByLocationId(String locationId) {
        return storage.get(locationId);
    }

    public static MobileFoodPermit addMobileFoodPermit(MobileFoodPermit mobileFoodPermit) {
        System.out.println("Saving id = " + mobileFoodPermit.getLocationId());
       return storage.put(mobileFoodPermit.getLocationId(), mobileFoodPermit);
    }


    public static List<MobileFoodPermit> getByName(String applicantName) {
        return storage.values().stream().filter(x -> x.getApplicant().equals(applicantName)).collect(Collectors.toList());
    }

    public static List<MobileFoodPermit> getByRadius(double latitude,double longitude, double radius) {
        GPSLocation origin = new GPSLocation(latitude,longitude);
        return storage.values().stream().filter(x -> GPSLocation.isPointsWithinRadius(origin,x.getGpsLocation(),radius)).collect(Collectors.toList());
    }

    public static List<MobileFoodPermit>  getByNameStartingWith(String name) {
       //
        List<MobileFoodPermit> autoFillOptions = new ArrayList<>();

        for(MobileFoodPermit permit : storage.values()){
            if(permit.getApplicant().startsWith(name)) {
                autoFillOptions.add(permit);
            }
            if(autoFillOptions.size() == 5) {
                return autoFillOptions;
            }
        }
        return autoFillOptions;
    }

    public void readInCSV() throws IOException {
        Reader reader = new BufferedReader(new FileReader(dataLocation));

        CsvToBean csvReader = new CsvToBeanBuilder(reader)
                .withType(MobileFoodPermit.class)
                .withSeparator(',')
                .withIgnoreLeadingWhiteSpace(true)
                .withIgnoreEmptyLine(true)
                .build();
        List<MobileFoodPermit> results = csvReader.parse();

        results.stream().forEach(x -> {
            storage.put(x.getLocationId(), x);
            System.out.println(storage.get(x.getLocationId()));

        });

    }
}

