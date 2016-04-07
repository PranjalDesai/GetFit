package com.pranjal.getfit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

public class UserMainScreen extends AppCompatActivity implements ChildEventListener {

    private static final String FIREBASE_URL = "https://fitnesshack.firebaseIO.com";
    private static final String FIREBASE_ROOT_NODE_TWO = "Workout";
    private Firebase mFirebase;
    String email;
    String pID, goal, cEmail;
    Button workout;
    TextView challengeName, challengeRep, challengeEmail, duration, challengeDescription, challengeGoal, bet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main_screen);


        Firebase.setAndroidContext(this);
        mFirebase = new Firebase(FIREBASE_URL);
        mFirebase.child(FIREBASE_ROOT_NODE_TWO).addChildEventListener(this);

        SharedPreferences myPref= getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor= myPref.edit();
        editor.putString("logCheck", "1");
        editor.apply();

        workout= (Button) findViewById(R.id.button);
        challengeName= (TextView) findViewById(R.id.name);
        challengeRep= (TextView) findViewById(R.id.reps);
        challengeEmail= (TextView) findViewById(R.id.email);
        duration= (TextView) findViewById(R.id.duration);
        challengeDescription= (TextView) findViewById(R.id.description);
        challengeGoal= (TextView) findViewById(R.id.goal);
        bet= (TextView) findViewById(R.id.bets);
        workout.setEnabled(false);
    }

    @Override
    public void onChildRemoved(DataSnapshot snapshot)
    {

    }
    @Override
    public void onChildChanged(DataSnapshot snapshot, String previousChildName)
    {

    }

    @Override
    public void onCancelled(FirebaseError error)
    {

    }

    @Override
    public void onChildMoved(DataSnapshot snapshot, String previousChildName)
    {

    }
    @Override
    public void onChildAdded(final DataSnapshot dataSnapshot, String s) {
        final String placeId = dataSnapshot.getKey();
        pID= placeId;

        SharedPreferences sp = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        email= sp.getString("Email", email);
        Boolean k=false;

        for(DataSnapshot child: dataSnapshot.getChildren())
        {
            if(child.getKey().equals("AlphaEmail"))
            {
                if(child.getValue().toString().equals(email))
                {
                    k=true;
                }
            }
            if(k)
            {
                if(child.getKey().equals("Bet"))
                {
                    bet.setText("Bet: $"+child.getValue().toString());
                }
                if(child.getKey().equals("CName"))
                {
                    challengeName.setText("Challenger Name: "+child.getValue().toString());
                }
                if(child.getKey().equals("Description"))
                {
                    challengeDescription.setText("Challenger Description: "+child.getValue().toString());
                }
                if(child.getKey().equals("Duration"))
                {
                    duration.setText("Duration: "+child.getValue().toString());
                }
                if(child.getKey().equals("Goal"))
                {
                    challengeGoal.setText("Goal: "+child.getValue().toString());
                    goal=child.getValue().toString();
                }
                if(child.getKey().equals("Reps"))
                {
                    challengeRep.setText("Reps Completed: "+child.getValue().toString());
                }
                if(child.getKey().equals("SEmail"))
                {
                    challengeEmail.setText("Challenger Email: "+child.getValue().toString());
                    cEmail=child.getValue().toString();
                    k=false;
                }
                workout.setEnabled(true);
            }

        }
    }

    public void workoutActivity(View view)
    {
        Intent activity = new Intent(UserMainScreen.this,WorkoutActivity.class);
        activity.putExtra("nodeId",pID);
        activity.putExtra("goals", goal);
        activity.putExtra("cEmail", cEmail);
        startActivity(activity);
    }

    public void challenge(View view)
    {
        Intent challenge = new Intent(UserMainScreen.this, ChallengeRequest.class);
        challenge.putExtra("SEMAIL", "");
        startActivity(challenge);
    }
}
