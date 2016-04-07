package com.pranjal.getfit;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

/**
 * Created by Harsh on 2/20/2016.
 * Listener for the data.
 */
public class ListenerService extends WearableListenerService {

    @Override
    public void onMessageReceived(MessageEvent event)
    {

        String data [] = new String[4];             // The data


        // When the message is recieved
        if (event.getPath().equals("/name_path")) {
            data[0] = new String(event.getData());

            Log.v("tag", "name recieved: " + data[0]);
        }
        else if (event.getPath().equals("/goal_path"))
        {
            data[1] = new String(event.getData());
            Log.v("tag", "goal recieved: " + data[1]);

        }
        else if(event.getPath().equals("/reps_path"))
        {
            data[2] = new String(event.getData());
            Log.v("tag", "reps recieved: " + data[2]);
        }
        else if(event.getPath().equals("/pid_path"))
        {
            data[3] = new String(event.getData());
            Log.v("tag", "reps recieved: " + data[3]);
        }
        else {
            super.onMessageReceived(event);
        }

        // Broadcast message to wearable activity for display
        Intent messageIntent = new Intent();
        messageIntent.setAction(Intent.ACTION_SEND);
        messageIntent.putExtra("data", data);
        LocalBroadcastManager.getInstance(this).sendBroadcast(messageIntent);
    }

}
