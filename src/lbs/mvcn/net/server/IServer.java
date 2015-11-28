/*
 *  LAN Battleship
 *  Copyright (c) 2015, spiralhalo
 *  You are free to modify and reuse this program code for commercial and non-commercial purpose
 *  with the condition that you removed this license header or replaced it with your own.
 */
package lbs.mvcn.net.server;

import lbs.mvcn.controller.IServerController;

/**
 *
 * @author spiralhalo
 */
public interface IServer {
    
    public void setController(IServerController controller);
    public void startListening(int port);
    public void sendMessage(int ServerId, String message);
    public void broadcast(String message);
    public String getIpAddress();
    public void closeAll();
    
}
