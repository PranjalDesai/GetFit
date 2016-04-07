package com.pranjal.getfit;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.wearable.view.WatchViewStub;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

        private TextView activityName;
        private TextView goal;
        private TextView reps;
        private TextView label;
        private String nameData = "";
        private String goalData = "";
        private String repsData = "";
        private Button my_activity;
        private static final float CURL_THRESHOLD = 1.1f;
        private static final int CURL_WAIT_TIME_MS = 250;
    String pid="";

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
            stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
                @Override
                public void onLayoutInflated(WatchViewStub stub) {
                    activityName = (TextView) stub.findViewById(R.id.Name);
                    goal = (TextView) stub.findViewById(R.id.Goal);
                    reps = (TextView) stub.findViewById(R.id.Reps);
                    label = (TextView) stub.findViewById(R.id.RepsLabel);
                    my_activity = (Button) stub.findViewById(R.id.myActivity);
                    my_activity.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Intent nextActivity = new Intent(MainActivity.this, MyActivity.class);
                            nextActivity.putExtra("challenge", nameData);
                            nextActivity.putExtra("goal",goalData);
                            nextActivity.putExtra("pid", pid);
                            startActivity(nextActivity);
                        }
                    });
                }
            });


            IntentFilter messageFilter = new IntentFilter(Intent.ACTION_SEND);
            MessageReceiver messageReceiver = new MessageReceiver();
            LocalBroadcastManager.getInstance(this).registerReceiver(messageReceiver, messageFilter);
        }


        public class MessageReceiver extends BroadcastReceiver {
            @Override
            public void onReceive(Context context, Intent intent) {
                String data [] = intent.getStringArrayExtra("data");
                if(data[0] != null)
                    nameData = data[0];
                if(data[1] != null)
                    goalData = data[1];
                if(data[2] != null)
                    repsData = data[2];
                if(data[3] != null)
                    pid= data[3];
                if(!repsData.equals("") && !goalData.equals("") && repsData.equals(goalData))
                {
                    goalData = "";
                    repsData = "";
                    pid= "";
                    label.setText("Goal Reached!");
                }
                activityName.setText(nameData);
                goal.setText(goalData);
                reps.setText(repsData);

            }
        }
    }


