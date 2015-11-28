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
    
    public void resetAll();
    public PlayerId addIfNotExist(int playerId, String ipAddress, String playerName); 
    public int getPlayerCount();
    public int getReadyCount();
    public void trimNonReady(); //remove non-ready players
    public void remove(PlayerId id);
    public void setReady(PlayerId id, boolean value);
    public void setAttacked(PlayerId id, boolean value);
    public String getRoosterBroadcast();
    public String getStartBroadcast();
    public String getStandingsBroadcast();
    public String getGridBroadcast();
    public void createOrUpdateRooster();
    public void createOrUpdateStandings();
    public void createResult();
    public Object[][] getRoosterData();
    public Object[][] getStandingsData();
    public Object[][] getResultData();
    public void destroyRoosterData();
    public void destroyStandingsData();
    public void destroyResultData();
    public byte getGrid(PlayerId id, int row, int col);    
    public void setGrid(PlayerId id, int serverId, int row, int col);    
    public int getRemaining(PlayerId id);    
//    public PlayerId[] getListOfPlayers();
    
}
