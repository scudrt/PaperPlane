package com.paperplane.Network;

import com.alibaba.fastjson.JSONObject;
import com.paperplane.Logger;
import com.paperplane.Manager.ChatServerManager;

import java.io.EOFException;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

/**
 * @Author
 * scudrt
 * @Description
 * created to wait and push message for server
 */
public class PushMessageDelegate implements Runnable{
    public PushMessageDelegate(Socket server, JSONObject json){
        this.server = server;
        this.message = json;
        try{
            this.input = new DataInputStream(server.getInputStream());
            this.output = new DataOutputStream(server.getOutputStream());
        }catch(IOException ioe){
            ioe.printStackTrace();
        }
    }
    /**
     * PUBLIC
     */

    public void run(){
        String id = this.message.getString("userID");
        try{
            while (!ChatServerManager.getInstance().hasMessage(id)){
                //don't be too busy
                Thread.currentThread().sleep(500);
            }
            //send message
            try{
                if (!server.isClosed()){
                    //check if the user is still connecting
                    this.output.writeByte(0); //confirm byte
                    server.setSoTimeout(5000); //wait for response
                    this.input.readByte();

                    this.output.writeUTF(ChatServerManager.getInstance().getOfflineChatMessage(id));
                    Logger.log("offline messages sent to " + id);
                }else{
                    Logger.log("user " + id + "closed the connection");
                }
            }catch(SocketException se){
                Logger.log("SocketException: user \'" + id + "\' offline, stop sending messages");
            }catch(SocketTimeoutException ste){
                Logger.log("TimeoutException: user \'" + id + "\' offline, stop sending messages");
            }catch(EOFException eofe){
                Logger.log("EOFException: user \'" + id + "\' is offline, stop sending messages");
            }
            this.output.close();
            this.server.close();
            return;
        }catch(InterruptedException ie){
            ie.printStackTrace();
        }catch(IOException ioe){
            ioe.printStackTrace();
        }
    }

    /**
     * PRIVATE
     */
    private Socket server;
    private DataOutputStream output;
    private DataInputStream input;
    private JSONObject message;
}