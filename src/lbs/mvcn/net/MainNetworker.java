/*
 * LAN Battleship
 * Copyright (c) 2015, spiralhalo
 * You are free to modify and reuse this program code for commercial and non-commercial purpose
 * with the condition that you removed this license header or replaced it with your own.
 */
package lbs.mvcn.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import lbs.mvcn.controller.INetController;
import java.lang.Thread.State;

/**
 *
 * @author spiralhalo
 */
public class MainNetworker extends Thread implements INetworker{

    private INetController controller;
    private Socket sock;
    private PrintWriter out;
    private BufferedReader in;
    private boolean connected;
    
    @Override
    public void setController(INetController controller) {
        this.controller = controller;
        connected = false;
    }

    @Override
    public void sendMessage(String message) {
        if (!connected) return;
        out.println(message);
    }

    @Override
    public boolean connectToServer(String hostname, int port) {
        if (getState()!=State.NEW || !connected) return false;
        try{
            sock = new Socket(hostname, port);
            out = new PrintWriter(sock.getOutputStream());
            in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            connected = true;
            this.start();
        } catch (IOException ex) {
            Logger.getLogger(MainNetworker.class.getName()).log(Level.SEVERE, null, ex);
        }
        return connected;
    }
    
    @Override
    public void closeConnection() {
        if (!connected) return;
        try{
            if(sock!=null)
                sock.close();
            if(in!=null)
                in.close();
            if(out!=null)
                out.close();
        } catch (IOException ex) {
            Logger.getLogger(MainNetworker.class.getName()).log(Level.SEVERE, null, ex);
        }
        connected = false;
    }
    
    @Override
    public void run() {
        String read;
        while (connected) {
            try {
                read = in.readLine();
                if (read != null) {
                    controller.onReceiveFromServer(read);
                } else {
                    connected = false;
                }
            } catch (IOException ex) {
                Logger.getLogger(MainNetworker.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public boolean isConnected() {
        return connected;
    }
    
}
