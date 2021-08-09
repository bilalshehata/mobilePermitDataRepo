package mobileFoodPermit.models;

import mobileFoodPermit.gps.GPSLocation;

import javax.validation.constraints.NotNull;

public class MobileFoodPermit {
    @NotNull
    String locationId;

    String applicant;

    String facilityType;
    String status;
    double latitude;
    double longitude;


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

    public GPSLocation getGpsLocation(){
        return new GPSLocation(this.latitude,this.longitude);
    }

}
