package mobilefoodpermit;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import mobilefoodpermit.gps.GPSLocation;
import mobilefoodpermit.models.MobileFoodPermit;

import org.geotools.data.shapefile.index.quadtree.QuadTree;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.index.kdtree.KdTree;
import org.locationtech.jts.index.quadtree.Quadtree;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MobileFoodPermitStorage  {
    final static Integer AUTOFILL_OPTIONS_LENGTH = 5;
    //concurrent hash table to support multiple writes, in memory data storage until database is implemented.
    //locationID uniquely identifies each row therefore it will be used as a key.
    private static ConcurrentHashMap<String, MobileFoodPermit> storage;
    public static KdTree tree;
    //location of CSV to load in data
    MobileFoodPermitStorage() throws IOException {
        tree = new KdTree();
        this.initialiseStorage();



        StorageInitialiser.readInCSV(storage);
    }

    public static ConcurrentHashMap<String, MobileFoodPermit> getStorage() {
        return storage;
    }

    public static MobileFoodPermit getByLocationId(String locationId) {
        return storage.get(locationId);
    }

    public static MobileFoodPermit addMobileFoodPermit(MobileFoodPermit mobileFoodPermit) {
        return storage.put(mobileFoodPermit.getLocationId(), mobileFoodPermit);
    }


    public static List<MobileFoodPermit> getByName(String applicantName) {
        return storage.values().stream().filter(x -> x.getApplicant().equals(applicantName)).collect(Collectors.toList());
    }

    public static List<MobileFoodPermit> getAllPaged(int pageNumber, int pageSize) {
        AtomicInteger index = new AtomicInteger(0);
        List<List<MobileFoodPermit>> chunkedMobileFoodPermits =  storage.values().stream().collect(Collectors.groupingBy(x -> index.getAndIncrement() / pageSize))
                .entrySet().stream()
                .map(Map.Entry::getValue).collect(Collectors.toList());
        return chunkedMobileFoodPermits.get(pageNumber);
    }

    public static List<MobileFoodPermit> getByRadius(double latitude, double longitude, double radius) {
        GPSLocation origin = new GPSLocation(latitude, longitude);
        return storage.values().stream().filter(x -> GPSLocation.isPointsWithinRadius(origin, x.createGPSLocation(), radius)).collect(Collectors.toList());
    }

    public static List<MobileFoodPermit> getByNameStartingWith(String name) {

        List<MobileFoodPermit> autoFillOptions = new ArrayList<>();

        for (MobileFoodPermit permit : storage.values()) {
            if (permit.getApplicant().startsWith(name)) {
                autoFillOptions.add(permit);
            }
            if (autoFillOptions.size() == AUTOFILL_OPTIONS_LENGTH) {
                return autoFillOptions;
            }
        }
        return autoFillOptions;
    }


    private void initialiseStorage(){
        storage  = new ConcurrentHashMap<>();
    }

}

