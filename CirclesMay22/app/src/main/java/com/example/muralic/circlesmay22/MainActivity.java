package com.example.muralic.circlesmay22;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.IntentService;
import android.app.LoaderManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.Loader;
import android.media.browse.MediaBrowser;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.app.ActionBar;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.Drive;

import java.util.List;

/**
 * Created by Muralic on 5/22/15.
 */
public class MainActivity extends Activity {


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_settings:
                openSettings();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void openSettings () {
        Intent i = new Intent(getApplicationContext(), Settings.class);
        startActivity(i);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //outState.putBoolean(this.STATE_RESOLVING_ERROR, mResolvingError);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("mainActivity:", "Creating intent Service");
        /* Create the service for getting the location information */
        Intent mServiceIntent = new Intent(this, UserLocationService.class);
        mServiceIntent.setAction("com.example.muralic.circlesmay22.action.ACTION_GET_LOCATION");
        this.startService(mServiceIntent);

        // The filter's action is BROADCAST_ACTION
        IntentFilter mStatusIntentFilter = new IntentFilter(
                UserLocationService.Constants.BROADCAST_ACTION);

        // Adds a data filter for the HTTP scheme
        //mStatusIntentFilter.addDataScheme();


        // Instantiates a new DownloadStateReceiver
        ResponseReceiver mResponseReceiver =
                new ResponseReceiver();

        // Registers the DownloadStateReceiver and its intent filters
        LocalBroadcastManager.getInstance(this).registerReceiver(
                mResponseReceiver,
                mStatusIntentFilter);

        /* Set actions for all the four buttons circle's has */
        Button family = (Button) findViewById(R.id.btnFamily);
        Button Friends = (Button) findViewById(R.id.btnFriends);
        Button Neighbors = (Button) findViewById(R.id.btnNeighbors);
        Button Social = (Button) findViewById(R.id.btnSocial);

        /* Set the onClickView Handler for family */
        family.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                /* Send a http request through http service */
                Intent i = new Intent(getApplicationContext(), Family.class);
                startActivity(i);
            }
        });

        // Listening to register new account link
        Friends.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Switching to Register screen
                Intent i = new Intent(getApplicationContext(), Friends.class);
                startActivity(i);
            }
        });

        // Listening to register new account link
        Neighbors.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Switching to Neighbors screen
                Intent i = new Intent(getApplicationContext(), Neighbors.class);
                startActivity(i);
            }
        });

        // Listening to register new account link
        Social.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Switching to Social screen
                Intent i = new Intent(getApplicationContext(), Social.class);
                startActivity(i);
            }
        });

        LoaderManager.LoaderCallbacks<UserAccountUtils.UserAccount> callbacksUserAccount =
                new LoaderManager.LoaderCallbacks<UserAccountUtils.UserAccount>() {
                    public Loader<UserAccountUtils.UserAccount> onCreateLoader(int id, Bundle _) {
                        switch(id) {
                            case 0:
                                return new UserAccountLoader(getApplicationContext());

                            default:
                                Log.d("Error:", "Unknown id to onCreate Loader Account");
                        }
                        return null;
                    }

                    public void onLoaderReset(Loader<UserAccountUtils.UserAccount> loader) {
                        // Nothing to be done for now
                    }

                    public void onLoadFinished(Loader<UserAccountUtils.UserAccount> loader,
                                                     UserAccountUtils.UserAccount user_account) {
                        final List<String> possible_emails = user_account.getemails();
                        final List<String> possible_names = user_account.getnames();
                        final List<String> possible_phone_numbers = user_account.getphonenumbers();
                        final String primary_email = user_account.getemail();
                        final String primary_phone_number = user_account.getphonenumber();
                    }
        };

        // Start the load manager  with Loader for getting UserAccounts.
        getLoaderManager().initLoader(0, Bundle.EMPTY, callbacksUserAccount);
    }

    // Broadcast receiver for receiving status updates from the IntentService
    private class ResponseReceiver extends BroadcastReceiver
    {
        // Prevents instantiation
        private ResponseReceiver() {
        }

        // Called when the BroadcastReceiver gets an Intent it's registered to receive
        public void onReceive(Context context, Intent intent) {
            /*
             * Handle Intents here.
             */
            String Action = intent.getAction();
            if (UserLocationService.Constants.BROADCAST_ACTION.equals(Action)) {
                Double Longitude = intent.getDoubleExtra(UserLocationService.Constants.EXTENDED_DATA_LONGITUDE, 0.0);
                Double Latitude = intent.getDoubleExtra(UserLocationService.Constants.EXTENDED_DATA_LATITUDE, 0.0);
                Log.d("MainARR:Long:", Longitude.toString());
                Log.d("MainARR:Lat:", Latitude.toString());
                Log.d("MainActivity:RR:", "GotResponse");
            } else if (HttpClientService.Constants.HTTP_RESP.equals(Action)) {
                // Http Response for the request that is sent from here, act on the same.
                String Response = intent.getStringExtra(HttpClientService.Constants.EXTENDED_DATA1);
                Log.d("HttpResponse:", Response);
            }
        }
    }
}
