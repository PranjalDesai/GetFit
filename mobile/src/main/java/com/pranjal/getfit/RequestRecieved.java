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

import java.util.HashMap;
import java.util.Map;

public class RequestRecieved extends AppCompatActivity implements ChildEventListener{

    private static final String FIREBASE_URL = "https://fitnesshack.firebaseIO.com";
    private static final String FIREBASE_ROOT_NODE_THREE = "Request";
    private static final String FIREBASE_ROOT_NODE_TWO = "Workout";
    private Firebase mFirebase;
    String email, sssemail;
    Map<String, Object> workoutData;
    TextView name, semail, goal, durration, description, title, bet;
    Button decline, accept, continues;
    Boolean k=false, done=false;
    String pID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_recieved);

        Firebase.setAndroidContext(this);
        mFirebase = new Firebase(FIREBASE_URL);
        mFirebase.child(FIREBASE_ROOT_NODE_THREE).addChildEventListener(this);

        workoutData= new HashMap<>();

        name= (TextView) findViewById(R.id.challengeName);
        semail= (TextView) findViewById(R.id.challengeEmail);
        goal= (TextView) findViewById(R.id.challengeGoal);
        durration= (TextView) findViewById(R.id.durrrr);
        description= (TextView) findViewById(R.id.challengeDescription);
        title= (TextView) findViewById(R.id.textView3);
        accept= (Button) findViewById(R.id.accept);
        decline= (Button) findViewById(R.id.decline);
        continues= (Button) findViewById(R.id.next);
        bet= (TextView) findViewById(R.id.bet);

        title.setText("No Request Recieved!");
        accept.setVisibility(View.GONE);
        decline.setVisibility(View.GONE);
        continues.setVisibility(View.VISIBLE);
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
        pID=placeId;

        SharedPreferences sp = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        email= sp.getString("Email", email);


        for(DataSnapshot child: dataSnapshot.getChildren())
        {
            if(child.getKey().equals("AbEmail"))
            {
                if(child.getValue().toString().equals(email))
                {
                    workoutData.put("AlphaEmail",email);
                    k=true;
                }
            }
            if(k)
            {
                if(child.getKey().equals("Bet"))
                {
                    bet.setText("Bet: $"+child.getValue().toString());
                    workoutData.put("Bet",child.getValue().toString());
                }
                if(child.getKey().equals("CName"))
                {
                    name.setText("Challenger Name: "+child.getValue().toString());
                    workoutData.put("CName",child.getValue().toString());
                }
                if(child.getKey().equals("Description"))
                {
                    description.setText("Challenger Description: " + child.getValue().toString());
                    workoutData.put("Description", child.getValue().toString());
                }
                if(child.getKey().equals("Duration"))
                {
                    durration.setText("Duration: " + child.getValue().toString());
                    workoutData.put("Duration", child.getValue().toString());
                }
                if(child.getKey().equals("Goal"))
                {
                    goal.setText("Goal: "+child.getValue().toString());
                    workoutData.put("Goal", child.getValue().toString());
                }

                if(child.getKey().equals("AlphaEmail"))
                {
                    semail.setText("Challenger Email: " + child.getValue().toString());
                    workoutData.put("SEmail", child.getValue().toString());
                    sssemail= child.getValue().toString();

                }

                if(child.getKey().equals("zAsked"))
                {
                    if(child.getValue().toString().equals("1"))
                    {
                        done=true;
                    }
                    workoutData.put("zAsked", "1");
                    workoutData.put("Reps", "0");
                    k=false;
                }

                title.setText("Request Recieved!");
                accept.setVisibility(View.VISIBLE);
                decline.setVisibility(View.VISIBLE);
                continues.setVisibility(View.GONE);
            }

        }

    }

    public void accept(View view)
    {

        mFirebase.child(FIREBASE_ROOT_NODE_TWO).push().setValue(workoutData);
        mFirebase.child(FIREBASE_ROOT_NODE_THREE).child(pID).removeValue();
        if(done)
        {
            Intent challenge= new Intent(RequestRecieved.this, UserMainScreen.class);
            startActivity(challenge);
        }
        else{
            Intent challenge= new Intent(RequestRecieved.this, ChallengeRequest.class);
            challenge.putExtra("SEMAIL",sssemail);
            startActivity(challenge);
        }
    }

    public void decline(View view)
    {
        Intent challenge= new Intent(RequestRecieved.this, UserMainScreen.class);
        startActivity(challenge);
    }
}
