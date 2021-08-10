package mobilefoodpermit.gps;


import org.geotools.referencing.GeodeticCalculator;

public class GPSLocation {
    private double latitude;
    private double longitude;

    public GPSLocation(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * calculate distance between two long,lat values in meters
     *
     * @param locationA
     * @param locationB
     * @return
     */
    public static double distanceBetweenTwoPoints(GPSLocation locationA, GPSLocation locationB) {
        GeodeticCalculator geodeticCalculator = new GeodeticCalculator();
        geodeticCalculator.setStartingGeographicPoint(locationA.longitude, locationA.latitude);
        geodeticCalculator.setDestinationGeographicPoint(locationB.longitude, locationB.latitude);
        return geodeticCalculator.getOrthodromicDistance();
    }

    public static Boolean isPointsWithinRadius(GPSLocation locationA, GPSLocation locationB, double radius) {
        return distanceBetweenTwoPoints(locationA, locationB) < radius;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

}
