package com.paperplane.Network;

import android.os.AsyncTask;
import android.util.Log;

import com.paperplane.Manager.ChatClientManager;

import static android.support.constraint.Constraints.TAG;

public class NetworkSendTask extends AsyncTask<String, Void, String> {
    private NetworkListener listener;

    private SimpleClient client;

    private ChatClientManager chatManager;

    public NetworkSendTask(NetworkListener listener){
        this.listener = listener;
        chatManager = ChatClientManager.getInstance();
    }

    @Override
    protected void onPreExecute(){
        Log.d("NetworkSend","preExecute");
    }

    @Override
    protected String doInBackground(String... params){
        Log.d("NetworkSend","Sending");
        String sendMessage = params[0];
        client = new SimpleClient();
        client.send(sendMessage);
        String res = client.get();
        return res;
    }

    @Override
    protected void onProgressUpdate(Void... values){

    }

    @Override
    protected void onPostExecute(String result){
        Log.d(TAG, "onPostExecute: end");
        listener.onReceived(result);
    }
}
