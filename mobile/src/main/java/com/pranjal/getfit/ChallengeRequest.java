package com.pranjal.getfit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.HashMap;
import java.util.Map;

public class ChallengeRequest extends AppCompatActivity implements ChildEventListener {

    private static final String FIREBASE_URL = "https://fitnesshack.firebaseIO.com";
    private static final String FIREBASE_ROOT_NODE_TWO = "Workout";
    private static final String FIREBASE_ROOT_NODE_THREE = "Request";
    private Firebase mFirebaseCheck;
    private Firebase mFirebaseSend;
    Map<String, Object> requestData;
    String email,SEmail,temp;
    EditText challengeName, challengee, challenger, duration, challengeDescription, challengeGoal, bet;
    Boolean inWorkout=false;
    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge_request);

        SEmail= getIntent().getExtras().getString("SEMAIL");
        Firebase.setAndroidContext(this);
        mFirebaseCheck = new Firebase(FIREBASE_URL);
        mFirebaseCheck.child(FIREBASE_ROOT_NODE_TWO).addChildEventListener(this);
        mFirebaseSend= new Firebase(FIREBASE_URL);

        SharedPreferences sp = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        email= sp.getString("Email", email);

        requestData= new HashMap<>();

        temp="0";
        challengeName= (EditText) findViewById(R.id.Cname);
        challengee= (EditText) findViewById(R.id.challengee);
        challenger= (EditText) findViewById(R.id.challenger);
        duration= (EditText) findViewById(R.id.duration);
        challengeDescription= (EditText) findViewById(R.id.description);
        challengeGoal= (EditText) findViewById(R.id.goal);
        title= (TextView) findViewById(R.id.challengeTitle);
        bet= (EditText) findViewById(R.id.bet);

        challengee.setText(email);
        challenger.setText(SEmail);
        if(SEmail != null) {
            if (SEmail.equals("")) {
                title.setText("Challenge Request");
            } else {
                title.setText("Counter Request");
            }
        }
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
        //final String placeId = dataSnapshot.getKey();

        for(DataSnapshot child: dataSnapshot.getChildren())
        {
            if(child.getKey().equals("AlphaEmail"))
            {
                if(child.getValue().toString().equals(email))
                {
                    inWorkout=true;
                }
            }
            if(child.getKey().equals("zAsked")){
                if(child.getValue().toString().equals("1"))
                {
                    Log.i("bitch", "here");
                    temp="1";
                }
            }

        }
    }

    public void requestDone(View view) {


        if(challenger.getText().toString().equals(""))
        {
            Snackbar.make(findViewById(android.R.id.content), "Please enter Challenger Email", Snackbar.LENGTH_LONG).show();
            return;
        }
        if(duration.getText().toString().equals(""))
        {
            Snackbar.make(findViewById(android.R.id.content), "Please enter Duration", Snackbar.LENGTH_LONG).show();
            return;
        }
        if(challengeName.getText().toString().equals(""))
        {
            Snackbar.make(findViewById(android.R.id.content), "Please enter Challenge Name", Snackbar.LENGTH_LONG).show();
            return;
        }
        if(challengeDescription.getText().toString().equals(""))
        {
            Snackbar.make(findViewById(android.R.id.content), "Please enter Challenge Description", Snackbar.LENGTH_LONG).show();
            return;
        }
        if(challengeGoal.getText().toString().equals(""))
        {
            Snackbar.make(findViewById(android.R.id.content), "Please enter Challenge Goal", Snackbar.LENGTH_LONG).show();
            return;
        }
        if(bet.getText().toString().equals(""))
        {
            Snackbar.make(findViewById(android.R.id.content), "Please enter a Bet", Snackbar.LENGTH_LONG).show();
            return;
        }
        requestData.put("AlphaEmail", email);
        requestData.put("CName", challengeName.getText().toString());
        requestData.put("AbEmail", challenger.getText().toString());
        requestData.put("Reps", "0");
        requestData.put("Description", challengeDescription.getText().toString());
        requestData.put("Goal", challengeGoal.getText().toString());
        requestData.put("Duration", duration.getText().toString());
        requestData.put("Bet",bet.getText().toString());
        requestData.put("zAsked",temp);
        mFirebaseSend.child(FIREBASE_ROOT_NODE_THREE).push().setValue(requestData);


        Intent mainScreen= new Intent(ChallengeRequest.this, UserMainScreen.class);
        startActivity(mainScreen);
    }
}
