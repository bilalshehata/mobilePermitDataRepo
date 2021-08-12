package mobilefoodpermit.storage;


import mobilefoodpermit.geotool.ProjectedCoordinateFactory;
import mobilefoodpermit.models.MobileFoodPermit;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/***
 * Class assists API controller in handling the storage system so it does not need to interact with it directly
 * This class handles Pagination, autofill logic and error handling
 */
public class MobileFoodPermitStorageHandler {

    //pointing to the storage system.
    private final MobileFoodPermitStorage storage;

    //autofill option size set in applications.properties
    private final Integer autofillOptionsSize;


    //location of CSV to load in data
    public MobileFoodPermitStorageHandler(MobileFoodPermitStorage storage,
                                          Integer autofillOptionsSize) {
        this.storage = storage;
        this.autofillOptionsSize = autofillOptionsSize;
    }

    /***
     *  insert a permit into memory
     * @param mobileFoodPermit to be stored in memory
     * @return the stored permit
     */
    public MobileFoodPermit addMobileFoodPermit(MobileFoodPermit mobileFoodPermit) {
        return storage.insert(mobileFoodPermit);
    }

    /**
     * Get all permits in memory
     *
     * @return
     */
    public List<MobileFoodPermit> getAllPermits() {
        return storage.getAllPermits();
    }

    /**
     * get a permit by its location id
     *
     * @param locationId
     * @return permit with the location id specified
     */
    public MobileFoodPermit getMobileFoodPermitByLocationId(String locationId) {
        return storage.getByLocationId(locationId);
    }

    /***
     *
     * @param applicantName
     * @return
     */
    public List<MobileFoodPermit> getByName(String applicantName) {
        return storage.getAllPermits().stream()
                .filter(x -> x.getApplicant().equals(applicantName))
                .collect(Collectors.toList());
    }

    /**
     * Takes page size and page, then returns the page of data
     *
     * @param pageNumber
     * @param pageSize
     * @return
     */
    public List<MobileFoodPermit> getPage(int pageNumber, int pageSize) {
        int startIndex = pageNumber * pageSize;

        // to do  array index out of bounds check
        try {
            return storage.getAllPermits().subList(startIndex, startIndex + pageSize);
        } catch (Exception e) {
            //to return a proper error messag in APIController
            return null;
        }
    }

    public List<MobileFoodPermit> getByRadius(double latitude, double longitude, double radius) {
        return storage.getByRadius(ProjectedCoordinateFactory.createProjectedCoordinate(latitude, longitude), radius);
    }

    /***
     * function to be used with autofill functionality
     * @param name
     * @return a list of permits starting with the name
     */
    public List<MobileFoodPermit> getByNameStartingWith(String name) {

        List<MobileFoodPermit> autoFillOptions = new ArrayList<>();

        for (MobileFoodPermit permit : storage.getAllPermits()) {
            if (permit.getApplicant() != null) {
                if (permit.getApplicant().startsWith(name)) {
                    autoFillOptions.add(permit);
                }
                if (autoFillOptions.size() == this.autofillOptionsSize) {
                    return autoFillOptions;
                }
            }
        }
        return autoFillOptions;
    }


}

