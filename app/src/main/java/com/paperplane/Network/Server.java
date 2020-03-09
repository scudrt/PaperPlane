package com.paperplane.Network;

import com.paperplane.Network.SimpleServer;

//powered by SCUDRT
public class Server{
    public static void main(String[] args) {
        Thread t = new Thread(new SimpleServer(3000));
        t.start();
    }
}
