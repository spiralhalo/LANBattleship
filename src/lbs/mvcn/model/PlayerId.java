/*
 * LAN Battleship
 * Copyright (c) 2015, spiralhalo
 * You are free to modify and reuse this program code for commercial and non-commercial purpose
 * with the condition that you removed this license header or replaced it with your own.
 */
package lbs.mvcn.model;

import lbs.net.ServerThread;

/**
 *
 * @author spiralhalo
 */
public class PlayerId {
    
    public final int id;
    public final String name;
    
    public PlayerId(String playerName, int serverId) {
        name = playerName;
        id = serverId;
    }
    
    public boolean equals(String playerName, int serverId){
        
        return id == serverId && name.equals(playerName);
        
    }
}
