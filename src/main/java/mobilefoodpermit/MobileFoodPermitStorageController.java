package mobilefoodpermit;

import mobilefoodpermit.gps.GPSLocation;
import mobilefoodpermit.models.MobileFoodPermit;
import org.locationtech.jts.geom.Coordinate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class MobileFoodPermitStorageController {
    final static Integer AUTOFILL_OPTIONS_LENGTH = 5;
    //concurrent hash table to support multiple writes, in memory data storage until database is implemented.
    //locationID uniquely identifies each row therefore it will be used as a key.

    public static MobileFoodPermitStorage storage;

    //location of CSV to load in data
    MobileFoodPermitStorageController() throws IOException {
        storage = new MobileFoodPermitStorage();
        this.initialiseStorage();
        StorageInitialiser.readInCSV(storage);
    }


    public static MobileFoodPermit addMobileFoodPermit(MobileFoodPermit mobileFoodPermit) {

        return MobileFoodPermitStorage.insert(mobileFoodPermit);
    }


    public static List<MobileFoodPermit> getByName(String applicantName) {
        return MobileFoodPermitStorage.getAllPermits().stream().filter(x -> x.getApplicant().equals(applicantName)).collect(Collectors.toList());
    }

    /**
     * Takes page size and page, then returns the page of data
     * @param pageNumber
     * @param pageSize
     * @return
     */
    public static List<MobileFoodPermit> getAllPaged(int pageNumber, int pageSize) {
        AtomicInteger index = new AtomicInteger(0);
        List<List<MobileFoodPermit>> chunkedMobileFoodPermits = MobileFoodPermitStorage.getAllPermits().stream().collect(Collectors.groupingBy(x -> index.getAndIncrement() / pageSize))
                .entrySet().stream()
                .map(Map.Entry::getValue).collect(Collectors.toList());
        return chunkedMobileFoodPermits.get(pageNumber);
    }

    public static List<MobileFoodPermit> getByRadius(double latitude, double longitude, double radius) {
        Coordinate origin = new Coordinate(latitude, longitude);
        return MobileFoodPermitStorage.getByRadius(new Coordinate(latitude,longitude),radius) ;
    }

    public static List<MobileFoodPermit> getByNameStartingWith(String name) {

        List<MobileFoodPermit> autoFillOptions = new ArrayList<>();

        for (MobileFoodPermit permit : MobileFoodPermitStorage.getAllPermits()) {
            if (permit.getApplicant().startsWith(name)) {
                autoFillOptions.add(permit);
            }
            if (autoFillOptions.size() == AUTOFILL_OPTIONS_LENGTH) {
                return autoFillOptions;
            }
        }
        return autoFillOptions;
    }


    private void initialiseStorage() {

    }

}

