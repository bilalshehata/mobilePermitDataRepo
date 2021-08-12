package mobilefoodpermit.geotool;

import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.locationtech.jts.geom.Coordinate;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;

/***
 * This class is functions to create Projected coordinates from long  latitude values to projected X Y coordinates
 * for ease of use
 */
public class ProjectedCoordinateFactory {
    private static CoordinateReferenceSystem crsSource;
    private static CoordinateReferenceSystem crsTarget;

    static {
        try {
            // World Geodetic System  (latitude longitude in degrees)
            crsSource = CRS.decode("EPSG:4326");
            // WGS 84 / Pseudo-Mercator coordinate system
            crsTarget = CRS.decode("EPSG:3857");
        } catch (FactoryException e) {
            e.printStackTrace();
        }
    }

    /**
     * function takes a latitude and longitude value and return a projected UTM coordinate.
     *
     * @param latitude
     * @param longitude
     * @return X, Y value for a projected coordinate
     */
    public static Coordinate createProjectedCoordinate(double latitude, double longitude) {
        Coordinate coordinateToProject = new Coordinate(latitude, longitude);

        try {
            MathTransform transform = CRS.findMathTransform(crsSource, crsTarget, false);
            //function to perform an in-place transformation of the coordinate.
            JTS.transform(coordinateToProject, coordinateToProject, transform);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return coordinateToProject;
    }


}
