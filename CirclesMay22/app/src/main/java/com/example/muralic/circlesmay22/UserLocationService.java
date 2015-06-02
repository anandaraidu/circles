package com.example.muralic.circlesmay22;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class UserLocationService extends IntentService implements LocationListener,
              GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, android.location.LocationListener {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_GET_LOCATION =
            "com.example.muralic.circlesmay22.action.ACTION_GET_LOCATION";
    private static Location mLocation;
    private static LocationManager mLocationManager;
    GoogleApiClient mLocationClient;
    LocationRequest mLocationRequest;
    String mBestProvider;
    Geocoder mGeoCoder;

    /**
     * Starts this service to perform action GetLocation with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startGetLocation(Context context) {
        Intent intent = new Intent(context, UserLocationService.class);
        intent.setAction(ACTION_GET_LOCATION);
        context.startService(intent);
    }

    @Override
    public void onProviderEnabled(String Provider) {
        // Nothing to be done
    }

    @Override
    public void onProviderDisabled(String Provider) {
        if (Provider.equals(mBestProvider)) {
            Criteria criteria = new Criteria();
            mBestProvider = mLocationManager.getBestProvider(criteria, false);
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    public UserLocationService() {
        super("UserLocationService");
        Log.d("UserLocationService:", "Initialized");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            Log.d("UserLocationService", "onHandleIntent");
            final String action = intent.getAction();
            if (ACTION_GET_LOCATION.equals(action)) {
                handleActionGetLocation();
            }
        }
    }

    /**
     * Handle action GetLocation in the provided background thread with the provided
     * parameters.
     */
    private void handleActionGetLocation() {
        // TODO: Handle action GetLocation
        Log.d("UserLocationService:", "Entered");
        /*
         * Creates a new Intent containing a Uri object
         * BROADCAST_ACTION is a custom Intent action
         */

        mGeoCoder = new Geocoder(this, Locale.getDefault());

        if (utils.isGooglePlayServicesAvailable(this.getApplicationContext())) {
            Log.d("UserLocationService:", "RegisteredGoogleApiClient");
            mLocationClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();

            if (mLocationClient == null) {
                Log.d("UserLocationService:", "mLocationClient null");
            }

            mLocationClient.connect();

        } else {
            Log.d("UserLocationService:", "GooglePlayServices not available");
            if (mLocationManager == null) {
                Log.d("UserLocationService:", "mLocationManager inited");
                mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            } else {
                Log.d("UserLocationService:", "mLocationManager null");
            }

            Criteria criteria = new Criteria();
            mBestProvider = mLocationManager.getBestProvider(criteria, false);

            Log.d("UserLocationService:Best", mBestProvider);

            startLocationUpdates();

            mLocation = mLocationManager.getLastKnownLocation(mBestProvider);

            if (mLocation != null) {
                Log.d("UserLocationService:", "Getting LL");
                this.onLocationChanged(mLocation);
            }
        }
        return;
    }

    @Override
    public void onConnected(Bundle aConnectionHint) {
        Log.d("UserLocationService: ", "onConnected");
        if (utils.isGooglePlayServicesAvailable(getApplicationContext())) {
            mLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mLocationClient);
            if (mLocation != null) {
                Log.d("UserLocationService:", "mLocation != null");
                Double lLongitude = mLocation.getLongitude();
                Double lLatitude = mLocation.getLatitude();
                Log.d("UserLocationService:", lLongitude.toString());
                Log.d("UserLocationService:", lLatitude.toString());

                startLocationUpdates();

                sendBroadCasttoActivity();

                Log.d("UserLocationService:", "SentBroadCast");
            } else {
                Log.d("UserLocationService:", "No Location");
            }
        }
    }

    @Override
    public void onConnectionSuspended(int flag) {
        // Nothing to be done, to be implemented
        Log.d("UserLocationService:", "Connection Suspended");
    }

    @Override
    public void onConnectionFailed(ConnectionResult aResult) {
        // Nothing to be done for now, to be implemented.
        Log.d("UserLocationService:", "Failed in connection");
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(Constants.MIN_TIME_BETWEEN_UPDATES);
        mLocationRequest.setFastestInterval((long)Constants.MIN_DISTANCE_BETWEEN_UPDATES);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    protected void startLocationUpdates() {
        Log.d("UserLocationService:", "startLocationUpdates");
        if (utils.isGooglePlayServicesAvailable(this.getApplicationContext())) {
            createLocationRequest();
            Log.d("UserLocationUpdate:", "StartLocationUpdates");
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mLocationClient, mLocationRequest, this);
        } else {
            mLocationManager.requestLocationUpdates(mBestProvider,
                    Constants.MIN_TIME_BETWEEN_UPDATES,
                    Constants.MIN_DISTANCE_BETWEEN_UPDATES, this);
        }
    }

    @Override
    public void onLocationChanged(Location aLocation) {

        Log.d("UserLocationService:", "onLocationChanged");
        mLocation = aLocation;
        sendBroadCasttoActivity();
    }


    public void sendBroadCasttoActivity() {

        String errorMessage = "";
        // Get the location passed to this service through an extra.

        List<Address> addresses = null;
        Double lLongitude = mLocation.getLongitude();
        Double lLatitude = mLocation.getLatitude();

        Log.d("UserLocationService:", lLongitude.toString());
        Log.d("UserLocationService:", lLatitude.toString());

        if (mGeoCoder.isPresent()) {
            try {
                addresses = mGeoCoder.getFromLocation(
                        mLocation.getLatitude(),
                        mLocation.getLongitude(),
                        // In this sample, get just a single address.
                        1);
            } catch (IOException ioException) {
                // Catch network or other I/O problems.
                errorMessage = "Service Not Available";
                Log.e("UserLocationService", errorMessage, ioException);
            } catch (IllegalArgumentException illegalArgumentException) {
                // Catch invalid latitude or longitude values.
                errorMessage = "Invalid Language";
                Log.e("UserLocationService:", errorMessage + ". " +
                        "Latitude = " + mLocation.getLatitude() +
                        ", Longitude = " +
                        mLocation.getLongitude(), illegalArgumentException);
            }
        }

        // Handle case where no address was found.
        if (addresses == null || addresses.size()  == 0) {
            if (errorMessage.isEmpty() || errorMessage.equals("")) {
                errorMessage = "No Address found for the long,lat";
                Log.e("UserLocationService", errorMessage);
            }

        } else {
            Address address = addresses.get(0);
            ArrayList<String> addressFragments = new ArrayList<String>();

            // Fetch the address lines using getAddressLine,
            // join them, and send them to the thread.
            for(int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                addressFragments.add(address.getAddressLine(i));
                Log.d("UserLocationService:", address.getAddressLine(i));
            }
            Log.i("UserLocationService", "Address Found");
        }

        Intent localIntent =
                new Intent(Constants.BROADCAST_ACTION)
                        // Puts the status into the Intent
                        .putExtra(Constants.EXTENDED_DATA_LONGITUDE, mLocation.getLongitude())
                        .putExtra(Constants.EXTENDED_DATA_LATITUDE, mLocation.getLatitude());
        // Broadcasts the Intent to receivers in this app.
        LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);

        Log.d("UserLocationService:", "SentBroadCast");

    }

    public final class Constants {
        // Defines a custom Intent action
        public static final String BROADCAST_ACTION =
                "com.example.muralic.circlesmay22.BROADCAST";

        // Defines the key for the status "extra" in an Intent
        public static final String EXTENDED_DATA_STATUS =
                "com.example.muralic.circlesmay22.STATUS";

        // Defines the key for the status "extra" in an Intent
        public static final String EXTENDED_DATA_LONGITUDE =
                "com.example.muralic.circlesmay22.Longitude";

        // Defines the key for the status "extra" in an Intent
        public static final String EXTENDED_DATA_LATITUDE =
                "com.example.muralic.circlesmay22.Latitude";

        public static final long MIN_TIME_BETWEEN_UPDATES =
                10000;

        public static final float MIN_DISTANCE_BETWEEN_UPDATES =
                5000;
    }
}
