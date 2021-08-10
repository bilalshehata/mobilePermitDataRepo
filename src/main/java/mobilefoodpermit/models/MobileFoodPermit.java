package mobilefoodpermit.models;

import mobilefoodpermit.gps.GPSLocation;
import mobilefoodpermit.gps.Locatable;

import javax.validation.constraints.NotNull;

public class MobileFoodPermit implements Locatable {
    @NotNull
    private String locationId;

    private String applicant;

    private String facilityType;
    private String status;
    private double latitude;
    private double longitude;


    public String getFacilityType() {
        return facilityType;
    }

    public String getStatus() {
        return status;
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

    public GPSLocation createGPSLocation() {
        return new GPSLocation(this.latitude, this.longitude);
    }

}
