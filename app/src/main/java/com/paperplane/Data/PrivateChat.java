package com.paperplane.Data;

import android.util.Log;

import com.paperplane.Data.ChatWindowMessage;
import com.paperplane.Data.UserAccount;
import com.paperplane.R;

import java.util.ArrayList;

import static android.support.constraint.Constraints.TAG;

public class PrivateChat {
    private boolean isGetMessage;

    private UserAccount targetUser;

    private ArrayList<ChatWindowMessage> messages;

    public PrivateChat(){
        isGetMessage = false;
        messages = new ArrayList<ChatWindowMessage>();
        targetUser = new UserAccount("123", "456");
    }

    public PrivateChat(UserAccount targetUser){
        this.targetUser = targetUser;
        isGetMessage = false;
        messages = new ArrayList<ChatWindowMessage>();
    }

    public UserAccount getTargetUser() {
        return targetUser;
    }

    public ArrayList<ChatWindowMessage> getMessages() {
        return messages;
    }

    public void AddMessage(ChatWindowMessage message){
        messages.add(message);
    }

    public void SendTextMessage(String content){
        ChatWindowMessage msg = new ChatWindowMessage(targetUser.getIcon(), content, "", ChatWindowMessage.SEND);
        this.AddMessage(msg);
    }

    public void ReceiveTextMessage(String content){
        ChatWindowMessage msg = new ChatWindowMessage(R.mipmap.ic_launcher, content, "", ChatWindowMessage.RECEIVE); //之后改为本地用户的头像
        this.AddMessage(msg);
        Log.d(TAG, "ReceiveTextMessage: "+messages.size());
    }

    public int getMessageSize(){
        return messages.size();
    }
}
