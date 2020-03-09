package com.paperplane.Manager;

import com.alibaba.fastjson.JSONObject;
import com.paperplane.Network.SimpleClient;
import com.paperplane.Data.UserAccount;

/**
 * @Author scudrt
 * @Description
 * be used in client
 */
public class UserAccountClientManager {
    private UserAccountClientManager(){}

    /**
     * PUBLIC
     */

    public static UserAccountClientManager getInstance(){
        return instance;
    }

    /**
     * @Description
     * post a signup request to the server
     */
    public JSONObject signup(JSONObject json){
        System.out.println(1);
        json.put("MSGType", "SIGN_UP");
        SimpleClient client = new SimpleClient();
        if (client.isConnected()){
            System.out.println(2);
            client.send(json.toJSONString());
            System.out.println(3);
            json = JSONObject.parseObject(client.get());
            System.out.println(4);
            client.close();
            return json;
        }
        client.close();
        return null;
    }

    /**
     * @Description
     * try to post a login request to the server
     */
    public boolean login(JSONObject json) {
        json.put("MSGType", "LOGIN");

        SimpleClient client = new SimpleClient();
        if (client.isConnected()) {
            client.send(json.toJSONString());
            json = JSONObject.parseObject(client.get());
            if (json.getString("userID") != null) {
                this.user = new UserAccount(json);
                client.close();
                return true;
            }
        }
        client.close();
        return false;
    }

    public UserAccount getCurrentUser(){
        return this.user;
    }


    /**
     * PRIVATE
     */
    // use singleton mode
    private static UserAccountClientManager instance = new UserAccountClientManager();

    //user on client
    private UserAccount user;
}
