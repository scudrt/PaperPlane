package com.paperplane.Manager;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.paperplane.Logger;
import com.paperplane.Data.UserAccount;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

//import javax.swing.text.html.parser.Entity;

/**
* @Author scudrt
* @Description
* be used in the server
* */
public class UserAccountServerManager {
    /**
     * CONSTRUCTORS
     */
    private UserAccountServerManager(){
        this.users = new HashMap<String, UserAccount>();
        this.loadUserAccountFiles(); // if we have it
    }

    /**
     * PUBLIC
     */

    public static UserAccountServerManager getInstance(){
        return instance;
    }

    /**
     * @Description
     * Sign up for the new user, return sign up result true if succeed
     */
    public String signup(JSONObject json){
        JSONObject res = new JSONObject();
        res.put("result", false);
        String id = json.getString("userID");
        if (id != null) {
            if (id.length() >= 4 && id.length() <= 16 && !id.contains(" ")){ //legal id
                if (this.getUserByID(id) == null) { //user not exists
                    String pwd = json.getString("password");
                    if (pwd != null && pwd.length() >= 6 && pwd.length() <= 16 && !pwd.contains(" ")) { //ok
                        UserAccount newUser = new UserAccount(id, pwd);
                        this.users.put(id, newUser);
                        this.saveUsers();
                        res.put("result", true);
                    }else{
                        res.put("response", "illegal password");
                    }
                }else{
                    res.put("response", "user already exists");
                }
            }else{
                res.put("response", "illegal id");
            }
        }else{
            res.put("response", "no user id input");
        }
        return res.toJSONString();
    }

    /**
     * @Description
     * return login result true if succeed
     */
    public String login(JSONObject json){
        JSONObject res = new JSONObject(); //response
        String id = json.getString("userID");

        if (id != null){ //legal request
            UserAccount user = this.getUserByID(id);
            if (user != null){ //user exists
                if (user.getPassword().equals(json.getString("password"))){
                    user.setOnlineIP(json.getString("onlineIP"));
                    // return user's message to the client
                    res.put("userID", user.getUserID());
                    res.put("birthday", user.getBirthday());
                    res.put("nickname", user.getNickname());
                    res.put("signupTime", user.getSignupTime());
                }
            }
        }
        return res.toJSONString();
    }

    public String getOnlineUsers(){
        JSONObject res = new JSONObject();

        int size = 0;
        String userStr;
        UserAccount user;
        JSONObject userJSON;
        Iterator<Map.Entry<String, UserAccount>> it = this.users.entrySet().iterator();
        while (it.hasNext()){
            user = it.next().getValue();
            if (user.isOnline()){
                userStr = JSON.toJSONString(user);
                userJSON = JSONObject.parseObject(userStr);
                //don't send important information
                userJSON.remove("password");
                userJSON.remove("onlineIP");
                res.put("user" + size, userJSON);
                ++size;
            }
        }
        res.put("size", size);
        return res.toJSONString();
    }

    public String getUserStringByID(JSONObject json){
        String id = json.getString("userID");
        UserAccount user = this.getUserByID(id);
        if (user != null){
            JSONObject res = (JSONObject) JSONObject.toJSON(user);
            res.remove("onlineIP");
            res.remove("password");
            return res.toJSONString();
        }else{
            return "{}";
        }
    }

    public UserAccount getUserByID(String id){
        return this.users.get(id);
    }

    /**
     * PRIVATE
     */

    /**
     * load local files contain users' information
     */
    private boolean loadUserAccountFiles(){
        try{
            //get file content
            FileReader f = new FileReader("./UserAccounts.xml");
            UserAccount user;
            String str = "";
            int n;
            while ((n = f.read()) != -1){
                str += (char)n;
            }
            //reconstructing user-map
            JSONObject json = JSONObject.parseObject(str);
            n = json.getInteger("size"); //get users' count
            for (int i=0;i<n;++i){
                str = "user" + i;
                user = new UserAccount(json.getJSONObject(str));
                this.users.put(user.getUserID(), user);
            }
            //log
            Logger.log("file loaded, " + n + " users found.");
            return true;
        }catch(IOException e){
            Logger.log("user file no found");
            return false;
        }
    }

    //save all users into the file
    private boolean saveUsers(){
        try{
            FileWriter f = new FileWriter("./UserAccounts.xml");
            JSONObject json = new JSONObject();
            int n = this.users.size();
            json.put("size", n);
            Iterator<Map.Entry<String, UserAccount>> entries = this.users.entrySet().iterator();
            for (int i=0;i<n;++i){
                Map.Entry<String, UserAccount> it = entries.next();
                json.put("user" + i, it.getValue());
            }
            f.write(json.toJSONString());
            f.flush();
            f.close();
            Logger.log(n + " users were saved.");
            return true;
        }catch(IOException e){
            e.printStackTrace();
            return false;
        }
    }

    //singleton
    private static UserAccountServerManager instance = new UserAccountServerManager();

    //store the Users' list
    private HashMap<String, UserAccount> users;
}
