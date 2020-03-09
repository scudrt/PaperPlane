package com.paperplane.Network;

import android.os.AsyncTask;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.paperplane.Manager.ChatClientManager;
import com.paperplane.Manager.UserAccountClientManager;

import static android.support.constraint.Constraints.TAG;

public class NetworkReceiveTask extends AsyncTask<Void, String, Boolean> {

    private NetworkListener listener;

    private boolean isStop = false;
    private SimpleClient client;

    private ChatClientManager chatClientManager;


    public NetworkReceiveTask(NetworkListener listener){
        super();
        this.listener = listener;
        chatClientManager = ChatClientManager.getInstance();
    }

    @Override
    protected void onPreExecute(){

    }

    @Override
    protected Boolean doInBackground(Void... params){
        if (isStop){
            return true;
        }
        new Thread(new Runnable(){
            @Override
            public void run() {
                while (true) {
                    Log.d(TAG, "run: looping");
                    if (isStop) {
                        break;
                    }
                    if (isCancelled()){
                        break;
                    }
                    //接收代码
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        if (client != null){
                            client.close();
                        }
                        e.printStackTrace();
                        break;
                    }
                    client = new SimpleClient();
                    try {
                        JSONObject json = new JSONObject();
                        json.put("MSGType", "ASK_MESSAGE");
                        json.put("userID", UserAccountClientManager.getInstance().getCurrentUser().getUserID());
                        client.send(json.toJSONString());
                        SimpleClient.currentAskingClient = client;
                        Log.d(TAG, "run: ask sent");
                        String msg = client.get();
                        Log.d(TAG, "run: ask get");
                        SimpleClient.currentAskingClient = null;
                        Log.d(TAG, "run: "+new Boolean(msg==null));
                        if (msg != null) {
                            Log.d(TAG, "run: getMSG");
                            listener.onReceived(msg);
                        }
                    } catch (Exception e) {
                        if (client != null){
                            client.close();
                        }
                        e.printStackTrace();
                        break;
                    }
                }
            }
        }).run();
        return true;
    }

    @Override
    protected void onProgressUpdate(String... values){
        if (this.isStop){
            return;
        }
        Log.d(TAG, "onProgressUpdate: message received");
        String content = values[0];
        listener.onReceived(content);
    }

    @Override
    protected void onPostExecute(Boolean result){

    }

    public void StopNetwork(){
        isStop = true;
    }
}
