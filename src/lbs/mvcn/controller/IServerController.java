/*
 *  LAN Battleship
 *  Copyright (c) 2015, spiralhalo
 *  You are free to modify and reuse this program code for commercial and non-commercial purpose
 *  with the condition that you removed this license header or replaced it with your own.
 */
package lbs.mvcn.controller;

/**
 *
 * @author spiralhalo
 */
public interface IServerController {
    
    public void onServerStartListening();
    public void onClientJoined(String clientIpAddress, String clientName, int serverId);
    public void onClientLeft(String clientName, int serverId);
    public void onReceiveFromClient(String clientName, int serverId, String message);
    public void onServerThreadClosed();
    public boolean isJoiningAllowed();
    
}
