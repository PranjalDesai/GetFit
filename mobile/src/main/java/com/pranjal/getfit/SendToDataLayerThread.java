package com.pranjal.getfit;

import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

/**
 * Created by Pranjal on 2/21/16.
 */
public class SendToDataLayerThread extends Thread
{
    private String path;                            // The path to the message.
    private String data;                            // The data of the message.
    private GoogleApiClient client;                 // The Google APi client


    // A constructor for the the data to be sent..
    SendToDataLayerThread(String path, String info, GoogleApiClient client)
    {
        this.path = path;
        this.client = client;
        this.data = info;
    }

    public void run()
    {
        NodeApi.GetConnectedNodesResult nodes = Wearable.NodeApi.getConnectedNodes(client).await();
        for (Node node : nodes.getNodes()) {
            // Sending the message.
            MessageApi.SendMessageResult result = Wearable.MessageApi.sendMessage(client, node.getId(),
                    path, data.getBytes()).await();
            // if success, else
            if (result.getStatus().isSuccess()) {
                Log.v("myTag", "Data: {" + data + "} sent to: " + node.getDisplayName());
            }
            else {
                // Log an error
                Log.v("myTag", "ERROR: failed to send Message");
            }
        }
    }
}
