package com.paperplane.Manager;

import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.paperplane.Data.ChatMessage;
import com.paperplane.Network.NetworkService;
import com.paperplane.Data.PrivateChat;
import com.paperplane.Data.UserAccount;

import java.util.ArrayList;

public class ChatClientManager {
    private ArrayList<PrivateChat> chatList;
    private PrivateChat currentChat;

    private NetworkService.NetworkBinder networkBinder;

    static private ChatClientManager instance;

    private ChatListener chatWindowListener;
    private ChatListener chatListListener;

    public void setChatWindowListener(ChatListener chatWindowListener) {
        this.chatWindowListener = chatWindowListener;
    }

    public void setChatListListener(ChatListener chatListListener) {
        this.chatListListener = chatListListener;
    }

    private ChatClientManager(){
        chatList = new ArrayList<PrivateChat>();
        currentChat = null;
    }

    static public ChatClientManager getInstance(){
        if(instance == null){
            instance = new ChatClientManager();
        }
        return instance;
    }

    public void setCurrentChat(PrivateChat currentChat) {
        this.currentChat = currentChat;
    }

    public PrivateChat getCurrentChat() {
        return currentChat;
    }

    public void startChat(UserAccount targetUser){
        PrivateChat privateChat = new PrivateChat(targetUser);
        if(targetUser != null && getChatByUserId(targetUser.getUserID()) == null) {
            chatList.add(privateChat);
        }
        if(chatWindowListener!=null) {
            chatWindowListener.OnRefresh();
        }
        if(chatListListener!=null) {
            chatListListener.OnRefresh();
        }
    }

    public void delChat(PrivateChat privateChat){
        chatList.remove(privateChat);
    }

    public ArrayList<PrivateChat> getChatList() {
        return chatList;
    }

    public void setNetworkBinder(NetworkService.NetworkBinder networkBinder) {
        this.networkBinder = networkBinder;
    }

    public PrivateChat getChatByUser(UserAccount user){
        for(PrivateChat chat: chatList){
            if(chat.getTargetUser() == user){
                return chat;
            }
        }
        return null;
    }

    public PrivateChat getChatByUserId(String id){
        for(PrivateChat chat: chatList){
            if(chat.getTargetUser().getUserID() == (id));
                return chat;
        }
        return null;
    }

    public int getChatSize(){
        return chatList.size();
    }

    public PrivateChat getChatByPosition(int position){
        if(position >= getChatSize() || position < 0){
            return null;
        }
        return chatList.get(position);
    }

    public void sendTextMessageInChat(PrivateChat privateChat, String msg){
        privateChat.SendTextMessage(msg);

        Log.d("ChatManager", (new Boolean(networkBinder == null)).toString());

        JSONObject json = new JSONObject();
        try {
            json.put("MSGType", "SEND_TO");
            ChatMessage chatMessage = new ChatMessage(msg);
            chatMessage.setReceiverID(privateChat.getTargetUser().getUserID());
            chatMessage.setSenderID(UserAccountClientManager.getInstance().getCurrentUser().getUserID());
            json.put("message",chatMessage);
        }catch (Exception e){
            e.printStackTrace();
        }
        if(networkBinder != null)
            networkBinder.SendMessage(json.toJSONString());
    }

    public void receiveTextMessage(PrivateChat privateChat, String msg){
        privateChat.ReceiveTextMessage(msg);
        //如果聊天窗口或列表界面处于活动状态，刷新
        if(chatListListener!=null){
            chatListListener.OnRefresh();
        }
        if(chatWindowListener!=null){
            chatWindowListener.OnRefresh();
        }
    }
}
