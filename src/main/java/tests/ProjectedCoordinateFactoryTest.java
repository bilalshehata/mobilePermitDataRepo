package tests;

import com.fasterxml.jackson.core.JsonProcessingException;
import mobilefoodpermit.api.MobileFoodPermitAPIController;
import mobilefoodpermit.geotool.ProjectedCoordinateFactory;
import mobilefoodpermit.storage.MobileFoodPermitStorageHandler;
import org.junit.Assert;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.mockito.Mockito;

import static org.junit.Assert.*;

public class ProjectedCoordinateFactoryTest {

    @Test
    public void createProjectedCoordinate_knownXProjection_shouldMatch() {
        Coordinate knownProjection = new Coordinate(111319.49,111325.14);
        Coordinate testCoordinate = ProjectedCoordinateFactory.createProjectedCoordinate(1,1);
        Assert.assertEquals(knownProjection.getX(),testCoordinate.getX(),0.1);

    }

    @Test
    public void createProjectedCoordinate_knownYProjection_shouldMatch() {
        Coordinate knownProjection = new Coordinate(111319.49,111325.14);
        Coordinate testCoordinate = ProjectedCoordinateFactory.createProjectedCoordinate(1,1);
        Assert.assertEquals(knownProjection.getY(),testCoordinate.getY(),0.1);

    }


}