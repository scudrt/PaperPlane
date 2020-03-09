package com.paperplane.Network;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.paperplane.Manager.ChatClientManager;
import com.paperplane.Data.ChatMessage;
import com.paperplane.Data.PrivateChat;
import com.paperplane.Data.UserAccount;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static android.support.constraint.Constraints.TAG;

public class NetworkService extends Service {

    private NetworkReceiveTask networkReceiveTask;
    private ChatClientManager chatClientManager;

    private NetworkBinder nBinder = new NetworkBinder();

    private PrivateChat privateChat;

    ExecutorService executors = Executors.newCachedThreadPool();

    final int START_NEW_CHAT = 1;
    final int EXIST_CHAT_RECEIVE = 2;

    private Handler handler = new Handler(){

        public void startChat(UserAccount user, ChatMessage chatMessage){
            Log.d(TAG, "startChat: Create Window");
            chatClientManager.startChat(user);
            PrivateChat privateChat = chatClientManager.getChatByUserId(user.getUserID());
            chatClientManager.receiveTextMessage(privateChat, chatMessage.getMessage());
        }

        public void handleMessage(Message message){
            Bundle bundle;
            String msgStr;
            ChatMessage chatMessage;
            switch (message.what){
                case START_NEW_CHAT:
                    bundle = message.getData();
                    String usrStr = bundle.getString("user");
                    msgStr = bundle.getString("chatMessage");
                    UserAccount user = new UserAccount(JSONObject.parseObject(usrStr));
                    Log.d(TAG, "handleMessage: " + JSONObject.toJSONString(user));
                    chatMessage = new ChatMessage(JSONObject.parseObject(msgStr));
                    startChat(user, chatMessage);
                    break;
                case EXIST_CHAT_RECEIVE:
                    Log.d(TAG, "handleMessage: send message in existing window");
                    bundle = message.getData();
                    msgStr = bundle.getString("chatMessage");
                    chatMessage = new ChatMessage(JSONObject.parseObject(msgStr));
                    PrivateChat privateChat = chatClientManager.getChatByUserId(chatMessage.getSenderID());
                    chatClientManager.receiveTextMessage(privateChat, chatMessage.getMessage());
                    break;
                    default:
                        break;
            }
        }
    };

    private NetworkListener receiveListener = new NetworkListener() {
        @Override
        public void onReceived(String msg) {
            Log.d(TAG, "onReceived: Receive message");
            final String message = msg;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG, "run: Thread running");
                    JSONObject loader = JSONObject.parseObject(message);
                    String userStr;
                    Bundle bundle;
                    Message hMessage;
                    for (int i = 0; i < loader.getIntValue("size"); i++) {
                        Log.d(TAG, message);
                        ChatMessage chatMessage = new ChatMessage(loader.getJSONObject("message" + i));
                        Log.d(TAG, "run: "+ JSONObject.toJSONString(chatMessage));
                        privateChat = chatClientManager.getChatByUserId(chatMessage.getSenderID());
                        if (privateChat == null) {//当前聊天列表未创建与目标用户的聊天创建聊天窗口
                            SimpleClient client = new SimpleClient();
                            JSONObject json = new JSONObject();
                            json.put("MSGType", "GET_USER");
                            json.put("userID", chatMessage.getSenderID());
                            client.send(json.toJSONString());
                            userStr = client.get();
                            bundle = new Bundle();
                            bundle.putString("user", userStr);
                            bundle.putString("chatMessage", JSONObject.toJSONString(chatMessage));
                            hMessage = new Message();
                            hMessage.setData(bundle);
                            hMessage.what = START_NEW_CHAT;
                            handler.sendMessage(hMessage);
                        }
                        else{
                            Log.d(TAG, "run: chat window found");
                            bundle = new Bundle();
                            hMessage = new Message();
                            bundle.putString("chatMessage", JSONObject.toJSONString(chatMessage));
                            hMessage.setData(bundle);
                            hMessage.what = EXIST_CHAT_RECEIVE;
                            handler.sendMessage(hMessage);
                        }
                    }
                }
            }).start();
        }
    };

    public NetworkService() {
    }

    @Override
    public void onCreate(){
        super.onCreate();

        chatClientManager = ChatClientManager.getInstance();
        networkReceiveTask = new NetworkReceiveTask(receiveListener);
        networkReceiveTask.executeOnExecutor(executors);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        return super.onStartCommand(intent, flags, startId);
    }

    public void stopReceiveService(){
        if (networkReceiveTask != null){
            networkReceiveTask.StopNetwork();
            networkReceiveTask.cancel(true);
        }
    }

    @Override
    public void onDestroy(){
        System.out.println("stopping receiving");
        stopReceiveService();
        super.onDestroy();
    }



    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return nBinder;
    }

    public class NetworkBinder extends Binder{

        private String res = null;

        public void SendMessage(String msg, NetworkListener listener){
            Log.d(TAG, "SendMessage: "+res);
            NetworkSendTask networkSendTask = new NetworkSendTask(listener);
            networkSendTask.execute(msg);
        }

        public void SendMessage(String msg){
            NetworkListener listener = new NetworkListener() {
                @Override
                public void onReceived(String content) {

                }
            };
            NetworkSendTask networkSendTask = new NetworkSendTask(listener);
            networkSendTask.execute(msg);
        }

        public String getRes(){
            return res;
        }
    }
}
