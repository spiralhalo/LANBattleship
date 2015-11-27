/*
 * LAN Battleship
 * Copyright (c) 2015, spiralhalo
 * You are free to modify and reuse this program code for commercial and non-commercial purpose
 * with the condition that you removed this license header or replaced it with your own.
 */
package lbs.mvcn.net;

import lbs.mvcn.controller.INetController;

/**
 *
 * @author spiralhalo
 */
public interface INetworker {
    
    public void setController(INetController controller);
    public boolean connectToServer(String hostname, int port);
    public void closeConnection();
    public boolean isConnected();
    public void sendMessage(String message);
    
}
