package com.example.muralic.circlesmay22;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;


public class Settings extends Activity {
    TextView Person, CurrentAddress, WorkAddress, Education, School, College;
    LinearLayout PersonDetails, CurrentAddressDetails, WorkAddressDetails, EducationDetails, SchoolDetails, CollegeDetails;
    EditText textFullName, textHouseNo, textAptStreetName, textArea, textCity, textPinCode;
    SharedPreferences mSharedPreferences;
    private static final String MyPreferences = "CirclesPreferences";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mSharedPreferences = getSharedPreferences(MyPreferences, Context.MODE_PRIVATE);
        Button settingsSave = (Button) findViewById(R.id.btnSettingsSave);

        textFullName = (EditText) findViewById(R.id.Settings_Person_FullName);
        textHouseNo = (EditText) findViewById(R.id.Settings_CurrentAddress_HouseNo);
        textAptStreetName = (EditText) findViewById(R.id.Settings_CurrentAddress_AptStreetName);
        textArea = (EditText) findViewById(R.id.Settings_CurrentAddress_AreaName);
        textCity = (EditText) findViewById(R.id.Settings_CurrentAddress_CityName);
        textPinCode = (EditText) findViewById(R.id.Settings_CurrentAddress_PinCode);

        Person = (TextView) findViewById(R.id.Settings_Person);
        WorkAddress = (TextView) findViewById(R.id.Settings_Work_Address);
        CurrentAddress = (TextView) findViewById(R.id.Settings_CurrentAddress);
        Education = (TextView) findViewById(R.id.Settings_Education);
        School = (TextView) findViewById(R.id.Settings_Education_School);
        College = (TextView) findViewById(R.id.Settings_Education_College);

        PersonDetails = (LinearLayout) findViewById(R.id.Settings_Person_Details);
        WorkAddressDetails = (LinearLayout) findViewById(R.id.Settings_Work_Address_Details);
        CurrentAddressDetails = (LinearLayout) findViewById(R.id.Settings_CurrentAddress_Details);
        EducationDetails = (LinearLayout) findViewById(R.id.Settings_Education_Details);
        SchoolDetails = (LinearLayout) findViewById(R.id.Settings_Education_School_Details);
        CollegeDetails = (LinearLayout) findViewById(R.id.Settings_Education_College_Details);

        College.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CollegeDetails.setVisibility(CollegeDetails.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
            }
        });

        School.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SchoolDetails.setVisibility(SchoolDetails.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
            }
        });

        Education.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EducationDetails.setVisibility(EducationDetails.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
            }
        });

        Person.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PersonDetails.setVisibility(PersonDetails.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
            }
        });

        WorkAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WorkAddressDetails.setVisibility(WorkAddressDetails.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
            }
        });

        CurrentAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CurrentAddressDetails.setVisibility(CurrentAddressDetails.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
            }
        });


        settingsSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Entered Here:", "OnClick");
                Log.d("FullName:", textFullName.getText().toString());
                Log.d("HouseNo:", textHouseNo.getText().toString());
                Log.d("AptStreetName:", textAptStreetName.getText().toString());
                Log.d("Area:", textArea.getText().toString());
                Log.d("City:", textCity.getText().toString());
                Log.d("PinCode:", textPinCode.getText().toString());

                SharedPreferences.Editor editor = mSharedPreferences.edit();
                editor.putString("FullName", textFullName.getText().toString());
                editor.putString("HouseNo", textHouseNo.getText().toString());
                editor.putString("AptStreetName", textAptStreetName.getText().toString());
                editor.putString("Area", textArea.getText().toString());
                editor.putString("City", textCity.getText().toString());
                editor.putString("PinCode", textPinCode.getText().toString());

                editor.commit();
                // Note: Change this to logically go back to the previous activity which started it
                onBackPressed();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
