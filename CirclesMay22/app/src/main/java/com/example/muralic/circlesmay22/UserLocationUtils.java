package com.example.muralic.circlesmay22;

import android.location.Location;

import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Created by Muralic on 5/26/15.
 */
public class UserLocationUtils {

    /* Add a member which will hold the actual address
     * Add a member which can parse the address and store them into multiple other address fields.
     * In the database everything can be stored based on Longitude and Latitude.
     */
    public static class UserLocation {
        /* Longitude  */
        private Double Longitude;

        /* Latitude */
        private Double Latitude;

        private GoogleApiClient mLocationClient;
        private Location mLastLocation;

        public void setLogitude(Double aLongitude)
        {
            Longitude = aLongitude;
        }

        public void setLatitude(Double aLatitude)
        {
            Latitude = aLatitude;
        }

        public Double getLongitude()
        {
            return Longitude;
        }

        public Double getLatitude () {
            return Latitude;
        }
    }
}
