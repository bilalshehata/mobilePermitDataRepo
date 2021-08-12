package tests;

import mobilefoodpermit.models.MobileFoodPermit;
import mobilefoodpermit.storage.MobileFoodPermitStorage;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;


public class MobileFoodPermitStorageTest {

    private MobileFoodPermitStorage setupTestStorage() {
        MobileFoodPermitStorage testStorage = new MobileFoodPermitStorage();
        MobileFoodPermit testPermit = new MobileFoodPermit();
        testPermit.setLocationId("location1");
        testPermit.setLatitude(1.0);
        testPermit.setLongitude(1.0);
        testStorage.insert(testPermit);
        return testStorage;
    }

    @Test
    public void getByLocationId_permitIsAvailable_returnPermit() {
        MobileFoodPermitStorage testStorage = new MobileFoodPermitStorage();
        MobileFoodPermit testPermit = new MobileFoodPermit();
        testPermit.setLocationId("location1");
        testPermit.setLatitude(1.0);
        testPermit.setLongitude(1.0);
        testStorage.insert(testPermit);
        Assert.assertNotNull(testStorage.getByLocationId(testPermit.getLocationId()));
    }
    @Test
    public void getByLocationId_permitIsNotAvailable_returnNull() {
        MobileFoodPermitStorage testStorage = new MobileFoodPermitStorage();
        Assert.assertNull(testStorage.getByLocationId("Unavailable"));
    }

    @Test
    public void getByRadius_permitInsideBoundingBox_returnPermit() {
        Assert.assertTrue(setupTestStorage().getByRadius(new Coordinate(10, 10), 1).size() == 0);
    }

    @Test
    public void getByRadius_permitOutsideBoundingBox_null() {
        Assert.assertTrue(setupTestStorage().getByRadius(new Coordinate(10, 10), 10).size() == 1);
    }

    @Test
    public void dataFromNodes() {
    }
}