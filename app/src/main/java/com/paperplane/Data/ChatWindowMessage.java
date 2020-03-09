package com.paperplane.Data;

import com.alibaba.fastjson.*;
import com.paperplane.R;

import java.io.Serializable;

public class ChatWindowMessage {
    public static final int SEND = 0;
    public static final int RECEIVE = 1;

    private int icon = R.mipmap.ic_launcher;
    private String content = "";
    private String image = "";
    private int type = SEND;


    public ChatWindowMessage(String json){
        JSONObject resolver = JSONObject.parseObject(json);

        icon = Integer.parseInt(resolver.getString("icon"));
        content = resolver.getString("content");
        image = resolver.getString("image");
        type = resolver.getString("type")=="SEND" ? 0 : 1;
    }

    public ChatWindowMessage(int icon, String content, String image, int type){
        this.icon = icon;
        this.content = content;
        this.image = image;
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public int getIcon() {
        return icon;
    }

    public String getImage() {
        return image;
    }

    public int getType() {
        return type;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setType(int type) {
        this.type = type;
    }

}
