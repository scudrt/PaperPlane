//powered by SCUDRT
package com.paperplane.Data;
import com.alibaba.fastjson.JSONObject;
import com.paperplane.R;

import java.util.Date;

//TODO: convert this class to JSONObject
public class UserAccount {
    public UserAccount(String _id, String _pwd){
        this.signupTime = new Date().toString();
        this.birthday = this.signupTime;
        this.userID = _id;
        this.password = _pwd;
        this.icon = R.mipmap.ic_launcher;
    }
    public UserAccount(JSONObject userJSON){
        this.signupTime = userJSON.getString("signupTime");
        this.birthday = userJSON.getString("birthday");
        this.userID = userJSON.getString("userID");
        this.nickname = userJSON.getString("nickname");
        this.password = userJSON.getString("password");
        this.icon = userJSON.getIntValue("icon");
    }
    
    /** PUBLIC */
    public String getUserID(){
        return this.userID;
    }
    public String getPassword(){
        return this.password;
    }
    public String getNickname(){
        return this.nickname;
    }
    public String getBirthday(){
        return this.birthday;
    }
    public String getSignupTime(){
        return this.signupTime;
    }
    public boolean isOnline(){
        return this.onlineIP != null && this.onlineIP != "";
    }
    public String getOnlineIP(){
        return this.onlineIP;
    }
    public int getIcon() {
        return icon;
    }

    public void setUserID(String _userID){
        this.userID = _userID;
    }
    public void setPassword(String _password){
        this.password = _password;
    }
    public void setNickname(String _nickname){
        this.nickname = _nickname;
    }
    public void setBirthday(String _birthday){
        this.birthday = _birthday;
    }
    public void setSignupTime(String _signupTime){
        this.signupTime = _signupTime;
    }
    public void setOnlineIP(String _onlineIP){
        this.onlineIP = _onlineIP;
    }
    public void setIcon(int icon) {
        this.icon = icon;
    }

    /** PRIVATE */
    private String userID, password;

    private String nickname;

    private String birthday;

    private String signupTime = new Date().toString();

    //format: "{"address": "xxx", "port": xx}"
    private String onlineIP;

    private int icon;
}
