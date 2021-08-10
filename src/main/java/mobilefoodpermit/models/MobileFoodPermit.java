package mobilefoodpermit.models;

import mobilefoodpermit.gps.GPSLocation;

import javax.validation.constraints.NotNull;

public class MobileFoodPermit {
    @NotNull
    private String locationId;
    private String applicant;
    private String facilityType;
    private String status;
    private double latitude;
    private double longitude;
    private double X;
    private double Y;


    public String getFacilityType() {
        return facilityType;
    }

    public String getStatus() {
        return status;
    }
    public double getX() {
        return X;
    }

    public double getY() {
        return Y;
    }


    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getLocationId() {
        return locationId;
    }

    public String getApplicant() {
        return applicant;
    }

  

}
