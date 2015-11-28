/*
 *  LAN Battleship
 *  Copyright (c) 2015, spiralhalo
 *  You are free to modify and reuse this program code for commercial and non-commercial purpose
 *  with the condition that you removed this license header or replaced it with your own.
 */
package lbs.mvcn.net.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import lbs.mvcn.controller.IServerController;

/**
 *
 * @author spiralhalo
 */
public class MainServer extends Thread implements IServer{

    private IServerController controller;
    private ServerSocket ssock;
    private boolean alive = false;
    private HashMap<Integer, ChildServer> childServer;
    private int nextServerId;
    private String serverName;
    
    public MainServer(String serverName){
        this.serverName = serverName;
        childServer = new HashMap<>();
    }
    
    @Override
    public void setController(IServerController controller) {
        this.controller = controller;
    }

    @Override
    public void startListening(int port) {
        try {
            ssock = new ServerSocket(port);
            alive = true;
            nextServerId = 0;
            controller.onServerStartListening();
            this.start();
        } catch (IOException ex) {
            Logger.getLogger(MainServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void run(){
        System.out.println("server : listening...");
        while(alive){
            try {
                ChildServer temp = new ChildServer(serverName, nextServerId, ssock.accept(), controller);
                if(!controller.isJoiningAllowed()){
                    temp.cancel();
                    continue;
                }
                childServer.put(nextServerId, temp);
                temp.start();
                nextServerId ++;
            } catch (IOException ex) {
                Logger.getLogger(MainServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        controller.onServerThreadClosed();
    }
    
    @Override
    public String getIpAddress() {
        return ssock.getInetAddress().getHostAddress();
    }

    @Override
    public void closeAll() {
        //TODO Implement this.
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void sendMessage(int ServerId, String message) {
        if( childServer.get(ServerId).isConnected() ){
            System.out.println("server <send> :"+message);
            childServer.get(ServerId).sendMessage(message);
        }
    }

    @Override
    public void broadcast(String message) {
        System.out.println("server : broadcasting : " + message);
        Integer[] sids = childServer.keySet().toArray(new Integer[]{});
        for( int sid:sids )
            sendMessage( sid , message );
    }
    
    
}
