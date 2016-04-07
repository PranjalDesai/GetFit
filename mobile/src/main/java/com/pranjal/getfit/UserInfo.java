package com.pranjal.getfit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.util.HashMap;
import java.util.Map;

public class UserInfo extends AppCompatActivity {

    private static final String FIREBASE_URL = "https://fitnesshack.firebaseIO.com";
    private static final String FIREBASE_ROOT_NODE_ONE = "Users";
    private Firebase mFirebase;
    Map<String, Object> userData;
    Map<String, Object> tempWorkoutData;
    private static final int REQUEST_PLACE_PICKER = 1;
    Place place;
    String email, name, logCheck;
    EditText height, weight;
    Boolean placeCheck=false;
    Boolean nextscreen=false;
    Button userD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        Firebase.setAndroidContext(this);
        userData = new HashMap<>();
        tempWorkoutData= new HashMap<>();
        height = (EditText) findViewById(R.id.editText2);
        weight = (EditText) findViewById(R.id.editText);
        mFirebase = new Firebase(FIREBASE_URL);

        userD= (Button) findViewById(R.id.slocation);

        SharedPreferences sp = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        email= sp.getString("Email", email);
        name= sp.getString("Name", name);
        logCheck= sp.getString("logCheck", logCheck);
//        Log.i("logCheck", logCheck);
        if(logCheck!=null)
        {
            if(logCheck.equals("1")) {
                Intent mainScreen = new Intent(UserInfo.this, RequestRecieved.class);
                startActivity(mainScreen);
            }
        }
    }

    @Override
    public void onStart(){
        super.onStart();
        if(!nextscreen)
        {
            nextscreen=true;
        }
        else
        {
            SharedPreferences myPref= getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor= myPref.edit();
            editor.putString("logCheck", "1");
            editor.apply();
        }
    }

    public void infoDone(View view) {
        if (!placeCheck) {
            Snackbar.make(findViewById(android.R.id.content), "Select a Place", Snackbar.LENGTH_LONG).show();
            return;
        }
        if(height.getText().toString().equals(""))
        {
            Snackbar.make(findViewById(android.R.id.content), "Please enter Height", Snackbar.LENGTH_LONG).show();
            return;
        }
        if(weight.getText().toString().equals(""))
        {
            Snackbar.make(findViewById(android.R.id.content), "Please enter Weight", Snackbar.LENGTH_LONG).show();
            return;
        }
        userData.put("Email", email);
        userData.put("Name", name);
        userData.put("Weight", weight.getText().toString());
        userData.put("Height", height.getText().toString());
        mFirebase.child(FIREBASE_ROOT_NODE_ONE).child(place.getId()).setValue(userData);
        Intent mainScreen= new Intent(UserInfo.this, RequestRecieved.class);
        startActivity(mainScreen);
    }

    public void placePicker(View view) {
        try {
            PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();
            Intent intent = intentBuilder.build(this);
            startActivityForResult(intent, REQUEST_PLACE_PICKER);
        } catch (GooglePlayServicesRepairableException e) {
            GoogleApiAvailability.getInstance().getErrorDialog(this, e.getConnectionStatusCode(),
                    REQUEST_PLACE_PICKER);
        } catch (GooglePlayServicesNotAvailableException e) {
            Toast.makeText(this, "Please install Google Play Services!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_PLACE_PICKER) {
            if (resultCode == Activity.RESULT_OK) {
                place = PlacePicker.getPlace(data, this);
                //userData.put("time", ServerValue.TIMESTAMP);
                userData.put("Address", place.getAddress());
                userD.setText(place.getAddress());
                placeCheck=true;

                //userData.put("placeName", place.getName());


            } else if (resultCode == PlacePicker.RESULT_ERROR) {
                Toast.makeText(this, "Places API failure! Check the API is enabled for your key",
                        Toast.LENGTH_LONG).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
