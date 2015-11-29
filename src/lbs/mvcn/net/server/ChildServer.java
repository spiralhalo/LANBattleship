/*
 *  LAN Battleship
 *  Copyright (c) 2015, spiralhalo
 *  You are free to modify and reuse this program code for commercial and non-commercial purpose
 *  with the condition that you removed this license header or replaced it with your own.
 */
package lbs.mvcn.net.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import lbs.mvcn.controller.IServerController;
import lbs.mvcn.net.protocol.Codec;

import static lbs.mvcn.net.protocol.ClientHeaders.*;
import static lbs.mvcn.net.protocol.ServerHeaders.*;
import static lbs.mvcn.net.protocol.Codec.*;
/**
 *
 * @author spiralhalo
 */
public class ChildServer extends Thread {
    
    public int id;
    public IServerController controller;
    
    private BufferedReader in;
    private PrintWriter out;
    private Socket sock;
    
    private String clientName, serverName;
    
    private boolean connected = true;
    
    public ChildServer(String name, int serverId, Socket clientSocket, IServerController controller){
        this.controller = controller;
        id = serverId;
        serverName = name;
        try{
            sock = clientSocket;
            out = new PrintWriter(sock.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
        } catch (IOException ex) {
            Logger.getLogger(ChildServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private static final int maxAttempt = 3;
    @Override
    public void run(){
        String read;
        //look for client name
        int attempt = 0;
        System.out.println("child "+id+ " : client "+getIpAddress()+" is attempting to connect...");
        while(connected) {
            try {
                read = in.readLine();
                if (read != null) {
                    System.out.println("child "+id+ " : client "+getIpAddress()+" attempt "+attempt);
                    String[] decoded = Codec.decode(read);
                    if(decoded[0].equals(META_NAME) && controller.isJoiningAllowed()){
                        clientName = decoded[1];
                        String message = encode(S_CONNECT_OK, ""+id, serverName);
                        System.out.println("child "+id+" <send> :"+message);
                        out.println(message);
                        controller.onClientJoined(getIpAddress(), clientName, id);
                        break;
                    } else attempt++;
                    if (attempt >= maxAttempt){
                        System.out.println("child "+id+ " : client "+getIpAddress()+" attempt unsuccessful");
                        connected = false; // client not recognized as player... close connection
                    }
                } else {
                    connected = false;
                }
            } catch (IOException ex) {
                Logger.getLogger(ChildServer.class.getName()).log(Level.SEVERE, null, ex);
                connected = false;
            }
        }
        while (connected) {
            try {
                read = in.readLine();
                if (read != null) {
                    controller.onReceiveFromClient(clientName, id, read);
                } else {
                    connected = false;
                }
            } catch (IOException ex) {
                Logger.getLogger(ChildServer.class.getName()).log(Level.SEVERE, null, ex);
                connected = false;
            }
        }
        cleanup();
        controller.onClientLeft(clientName, id);
    }
    
    public String getIpAddress() {
        return sock.getLocalAddress().getHostAddress();
    }

    public String getClientName() {
        return clientName;
    }
    
    public void sendMessage(String message) {
        if (!connected) return;
        System.out.println("child "+id+" <send> :"+message);
        out.println(message);
    }
    
    public boolean isConnected() {
        return connected;
    }

    void cancel() {
        cleanup();
    }
    
    private void cleanup(){
        try{
            if(sock!=null && sock.isConnected())
                sock.close();
            if(in!=null)
                in.close();
            if(out!=null)
                out.close();
        } catch (IOException ex) {
            Logger.getLogger(ChildServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
