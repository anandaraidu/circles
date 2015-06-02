package com.example.muralic.circlesmay22;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

/**
 * Created by Muralic on 6/2/15.
 */
public class utils {
    public utils() {
        // Nothing to be done for now.
    }

    // Return true if network is connected otherwise false.
    public static boolean isConnected(Context context){
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }

    // Return true if GooglePlayServices are available, otherwise false.
    public static boolean isGooglePlayServicesAvailable(Context context) {
        if (ConnectionResult.SUCCESS == GooglePlayServicesUtil.isGooglePlayServicesAvailable(context)) {
            return true;
        } else {
            // Send a broadcast saying location is not got.
            Log.d("UserLocationService:", "No GooglePlayServices");
            return false;
        }
    }
}
