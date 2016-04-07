package com.pranjal.getfit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.client.Firebase;


/**
 * Created by Harsh on 2/21/2016.
 *
 * **Note: This class was inspired by the class found on this URL.
 * URL: https://github.com/drejkim/AndroidWearMotionSensors/blob/master/wear/src/main/java/com/drejkim/androidwearmotionsensors/SensorFragment.java
 * Date: 2/21/2016
 * By: Harsh Patel
 */
public class MyActivity extends Activity implements SensorEventListener
{
    private TextView exercize;
    private TextView reps;
    private TextView goalText;
    private TextView slash;
    private Button back;
    private SensorManager sensorManager;
    private Sensor sensor;
    private int sensorType;
    private String goal;
    private float x,y;                        // The data

    private static final String FIREBASE_URL = "https://fitnesshack.firebaseIO.com";
    private Firebase mFirebase;

    private boolean hitsXMin, hitsYMin;
    private boolean hitsXMax, hitsYMax;
    private boolean isPushUp;
    private int rep;
    String pID;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_activity);               // Make it look like my activity
        Bundle b = new Bundle();

        rep = 0;

        x = 0.0f; y = 0.0f;


        Firebase.setAndroidContext(this);
        mFirebase = new Firebase(FIREBASE_URL);


        String exe;
        reps = (TextView) findViewById(R.id.my_reps);
        sensorType = 1;                                     // 1 = acceleromoeter.
        Intent info = getIntent();
        back = (Button) findViewById(R.id.back_button);

        exe = info.getExtras().getString(("challenge"));
        goal = info.getExtras().getString("goal");
        pID= info.getExtras().getString("pid");
        exercize = (TextView) findViewById(R.id.my_excersize);
        goalText = (TextView) findViewById(R.id.goalText);
        slash = (TextView) findViewById(R.id.shash);
        goalText.setText(goal);
        exercize.setText(exe);      // Displaying the exercise.

        if(exe.equals("Push Ups"))
            isPushUp = true;
        else
            isPushUp = false;
        sensorManager = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);

        sensor = sensorManager.getDefaultSensor(sensorType);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent prevActivity = new Intent(v.getContext(), MainActivity.class);
                startActivity(prevActivity);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        sensorManager.registerListener(this, sensor, 1000000);
    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        // If sensor is unreliable, then just return
        if (event.accuracy == SensorManager.SENSOR_STATUS_UNRELIABLE)
        {
            return;
        }

        // Detecting the x,y,z.
        x =  event.values[0];
        y =  event.values[1];
        //reps.setText("x: " + x + "\n" + "y: " + y);

        // Detect a rep.

        // Checking for the mins and max.
        if(x > 7)
        {
            hitsXMax = true;
            //Log.v("tag", "hits x max");
        }
        if(y > 1)
        {
            hitsYMax = true;
            // Log.v("tag", "hits y max");
        }

        if(x < 1)
        {
            hitsXMin = true;
            //Log.v("tag", "hits x min");
        }
        if(y < -4)
        {
            hitsYMin = true;
            // Log.v("tag", "hits y min");
        }

        if((hitsXMax && hitsXMin) && !isPushUp)
        {
            hitsXMax = false;
            hitsXMin = false;
            Log.v("tag", "Curl");
            rep++;
        }

        if((hitsYMax && hitsYMin) && isPushUp)
        {
            hitsYMax = false;
            hitsYMin = false;
            Log.v("tag","push up");
            rep++;
        }


        if(Integer.parseInt(goal) <= rep/2)
        {
            goalText.setText("Reached!");
            slash.setText("");
            reps.setText("Goal");
            mFirebase.child("Workout/"+pID+"/Reps").setValue(Integer.parseInt(goal));

        }
        else {
            reps.setText("" + rep / 2);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}

