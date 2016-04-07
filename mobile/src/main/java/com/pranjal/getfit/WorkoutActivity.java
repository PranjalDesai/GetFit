package com.pranjal.getfit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Wearable;

public class WorkoutActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener  {


    private GoogleApiClient googleClient;               // The client for sending messages.
    private static final String FIREBASE_URL = "https://fitnesshack.firebaseIO.com";
    private static final String FIREBASE_ROOT_NODE_TWO = "Workout";
    private Firebase mFirebase;
    String pID, reps, goals, cEmail;
    Button Reps;
    int i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);

        //creating a firebase object
        Firebase.setAndroidContext(this);
        mFirebase = new Firebase(FIREBASE_URL);

        pID= getIntent().getExtras().getString("nodeId");
        goals= getIntent().getExtras().getString("goals");
        cEmail= getIntent().getExtras().getString("cEmail");

        Reps= (Button) findViewById(R.id.PBUTTON);

        // Initializing the Google cilent.
        googleClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        displayWork();
    }

    //display current reps
    public void displayWork(){

        mFirebase.child("Workout/"+pID+"/Reps").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                reps = snapshot.getValue().toString();
                Reps.setText(reps);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });

    }

    //checks if the current task is done
    public void work(View view)
    {
        int x= Integer.parseInt(Reps.getText().toString());
        int y= Integer.parseInt(goals);
        x++;
        mFirebase.child("Workout/"+pID+"/Reps").setValue(x);
        if(x>= y)
        {
            mFirebase.child("Workout/"+pID).removeValue();
            Intent done= new Intent(WorkoutActivity.this, TaskDone.class);
            done.putExtra("email", cEmail);
            startActivity(done);
        }
        else {
            new SendToDataLayerThread("/reps_path", "" + i, googleClient).start();
            displayWork();
        }
    }

    @Override
    public void onStart()
    {
        // Connect to google on start.
        super.onStart();
        googleClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        String activityName = "Push Ups";
        String activityGoal = goals;
        String activitypID = pID;

        Toast.makeText(this, "Connected", Toast.LENGTH_LONG);
        // We need to send through another thread so that we dont block the UI
        new SendToDataLayerThread("/name_path", activityName,googleClient).start();
        new SendToDataLayerThread("/goal_path",activityGoal, googleClient).start();
        new SendToDataLayerThread("/pid_path",activitypID, googleClient).start();

    }

    @Override
    public void onStop()
    {
        // When we are done, disconnect from the client.
        if (null != googleClient && googleClient.isConnected()) {
            googleClient.disconnect();
        }
        super.onStop();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
