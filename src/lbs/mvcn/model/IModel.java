/*
 * LAN Battleship
 * Copyright (c) 2015, spiralhalo
 * You are free to modify and reuse this program code for commercial and non-commercial purpose
 * with the condition that you removed this license header or replaced it with your own.
 */
package lbs.mvcn.model;

import java.util.HashMap;
import java.util.LinkedList;

/**
 *
 * @author spiralhalo
 */
public interface IModel {
    
    public void resetAll();
    public PlayerId addIfNotExist(int playerId, String ipAddress, String playerName);
    public int getAliveCount();
    public int getPlayerCount();
    public int getReadyCount();
    public void trimNonReady(); //remove non-ready players (for server)
    public PlayerId[] toArray();
    public PlayerId get(String playerName, int playerId);
    public void remove(PlayerId id);
    public void setReady(PlayerId id, boolean value);
    public void setAttacked(PlayerId id, boolean value);
    public void setAlive(PlayerId id, boolean value);
    public void resetAttacked();
    public void resetAlive();
    public String getPlacementMessage(PlayerId id);
    public void processPlacementData(String playerName, int playerId, String data);
    public void processAllGridData(String allGridData);
    public void processAttacksData(String attacks);
    public void processResultData(String deaths);
    public String getPlacementBroadcast();
    public String getRoosterBroadcast();
    public String getStartBroadcast();
    public String getAttackBroadcast(HashMap<PlayerId,LinkedList> attack);
//    public String getStandingsBroadcast();
    public String getGridBroadcast();
    public String getResultBroadcast();
    public void createOrUpdateRooster();
    public void createOrUpdateStandings(boolean passive);
    public void createResult();
    public Object[][] getRoosterData();
    public Object[][] getStandingsData();
    public Object[][] getResultData();
    public void destroyRoosterData();
    public void destroyStandingsData();
    public void destroyResultData();
    public byte getGrid(PlayerId id, int row, int col);
    public void setGrid(PlayerId id, int row, int col, byte value);
    public void attackGrid(PlayerId id, int row, int col);
    public int getHealth(PlayerId id);    
//    public PlayerId[] getListOfPlayers();

    
}
