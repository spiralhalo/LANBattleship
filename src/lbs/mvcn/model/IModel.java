/*
 * LAN Battleship
 * Copyright (c) 2015, spiralhalo
 * You are free to modify and reuse this program code for commercial and non-commercial purpose
 * with the condition that you removed this license header or replaced it with your own.
 */
package lbs.mvcn.model;

/**
 *
 * @author spiralhalo
 */
public interface IModel {
    
    public PlayerId add(int playerId, String playerName); 
    public int getPlayerCount();    
    public void remove(PlayerId id);    
    public byte getGrid(PlayerId id, int row, int col);    
    public void setGrid(PlayerId id, int serverId, int row, int col);    
    public int getRemaining(PlayerId id);    
//    public PlayerId[] getListOfPlayers();
    
}
