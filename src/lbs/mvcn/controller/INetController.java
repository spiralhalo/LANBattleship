/*
 * LAN Battleship
 * Copyright (c) 2015, spiralhalo
 * You are free to modify and reuse this program code for commercial and non-commercial purpose.
 * Attribution is okay but not required.
 */
package lbs.mvcn.controller;

/**
 *
 * @author spiralhalo
 */
public interface INetController {
    
    public void onReceiveFromServer(String message);
    public void onThreadClosed();
    
}
