package com.paperplane.Manager;

import com.alibaba.fastjson.JSONObject;
import com.paperplane.Data.ChatMessage;
import com.paperplane.Logger;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @Description
 * manage and distribute users' chatting messages for server
 */
public class ChatServerManager{
    private ChatServerManager(){
        this.messageKeeper = new HashMap<String, ArrayList<ChatMessage>>();
    }

    /**
     * PUBLIC
     */

     public static ChatServerManager getInstance(){
         return instance;
     }

     /**
      * @Description
      * save offline chatting messages for users
      */
     public String addOfflineChatMessage(JSONObject chatJSON){
         chatJSON = chatJSON.getJSONObject("message"); //take message
         String receiverID = chatJSON.getString("receiverID");
         String senderID = chatJSON.getString("senderID");
         JSONObject res = new JSONObject();
         res.put("result", false);

         //check if sender and receiver exists
         UserAccountServerManager instance = UserAccountServerManager.getInstance();
         if (instance.getUserByID(receiverID) != null){ //legal id
             if (instance.getUserByID(senderID) != null){
                 //save message for the receiver
                 ArrayList<ChatMessage> messages = this.messageKeeper.get(receiverID);
                 if (messages == null){
                     messages = new ArrayList<ChatMessage>();
                     this.messageKeeper.put(receiverID, messages);
                 }
                 messages.add(new ChatMessage(chatJSON));
                 res.put("result", true);
             }
         }
         return res.toJSONString();
     }

     /**
      * @Description
      * return user's offline chatting message
      */
    public String getOfflineChatMessage(String id){
        //TODO: check if  the user is online
        JSONObject res = new JSONObject();
        res.put("size", 0);

        //take all user's messages out
        if (id != null){
            ArrayList<ChatMessage> messages = this.messageKeeper.get(id);
            if (messages != null && messages.size() > 0){
                int n = messages.size();
                ChatMessage temp;
                res.put("size", n);
                //put messages into response json
                for (int i=0;i<n;++i){
                    temp = messages.get(0);
                    res.put("message"+i, temp);
                    messages.remove(0);
                }
                //logging
                Logger.log("send offline messages: " + res.toJSONString());
            }
        }
        return res.toJSONString();
    }

    public boolean hasMessage(String userID){
        if (userID != null){
            ArrayList<ChatMessage> messages = this.messageKeeper.get(userID);
            return messages != null && messages.size() > 0;
        }else{
            return false;
        }
    }

    /**
     * PRIVATE
     */
    private static ChatServerManager instance = new ChatServerManager();

    //keep offline messages for users, the key is userID
    private HashMap<String, ArrayList<ChatMessage>> messageKeeper;
}
