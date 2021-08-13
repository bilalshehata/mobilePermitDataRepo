package tests;

import mobilefoodpermit.geotool.ProjectedCoordinateFactory;
import org.junit.Assert;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;

public class ProjectedCoordinateFactoryTest {

    @Test
    public void createProjectedCoordinate_knownXProjection_shouldMatch() {
        Coordinate knownProjection = new Coordinate(111319.49, 111325.14);
        Coordinate testCoordinate = ProjectedCoordinateFactory.createProjectedCoordinate(1, 1);
        Assert.assertEquals(knownProjection.getX(), testCoordinate.getX(), 0.1);

    }

    @Test
    public void createProjectedCoordinate_knownYProjection_shouldMatch() {
        Coordinate knownProjection = new Coordinate(111319.49, 111325.14);
        Coordinate testCoordinate = ProjectedCoordinateFactory.createProjectedCoordinate(1, 1);
        Assert.assertEquals(knownProjection.getY(), testCoordinate.getY(), 0.1);

    }

    @Test
    public void createProjectedCoordinate_ZeroCoordinate_shouldBeZero() {
        Coordinate knownProjection = new Coordinate(0, 0);
        Coordinate testCoordinate = ProjectedCoordinateFactory.createProjectedCoordinate(0, 0);
        Assert.assertEquals(knownProjection.getY(), testCoordinate.getY(), 0.1);

    }


}