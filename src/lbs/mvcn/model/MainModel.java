/*
 * LAN Battleship
 * Copyright (c) 2015, spiralhalo
 * You are free to modify and reuse this program code for commercial and non-commercial purpose
 * with the condition that you removed this license header or replaced it with your own.
 */
package lbs.mvcn.model;

import java.util.Arrays;
import java.util.HashMap;
import static lbs.mvcn.model.Constants.*;

/**
 *
 * @author spiralhalo
 */
public class MainModel implements IModel{

    HashMap<PlayerId, byte[]> grid;
    HashMap<PlayerId, Boolean> isReady;
    HashMap<PlayerId, Boolean> hasAttacked;
    
    @Override
    public void resetAll(){
        grid = new HashMap<>();
        isReady = null;
        hasAttacked = null;
    }

    private PlayerId exists(int playerId, String playerName) {
        PlayerId[] temp = grid.keySet().toArray(new PlayerId[]{});
        for(PlayerId pid : temp)
            if(pid.equals(playerName, playerId))
                return pid;
        return null;
    }
    
    @Override
    public PlayerId addIfNotExist(int playerId, String ipAddress, String playerName) {
        PlayerId temp = exists(playerId, playerName);
        if(temp != null) return temp;
        temp = new PlayerId(playerName, ipAddress, playerId);
        grid.put(temp, new byte[100]);
        return temp;
    }
    
    @Override
    public int getPlayerCount(){
        return grid.size();
    }

    @Override
    public void remove(PlayerId id) {
        grid.remove(id);
        if(isReady!=null && isReady.containsKey(id))
            isReady.remove(id);
        if(hasAttacked!=null && hasAttacked.containsKey(id))
            hasAttacked.remove(id);
    }

    @Override
    public byte getGrid(PlayerId id, int row, int col) {
        return grid.get(id)[row * 10 + col];
    }

    @Override
    //This method is used only for attacking. there is no manual set
    public void setGrid(PlayerId id, int serverId, int row, int col) {
        if(grid.get(id)[row * 10 + col] < NULL_ATTACKED)
            //change grid state to "whatever_ATTACKED" by adding a constant offset
            grid.get(id)[row * 10 + col] += NULL_ATTACKED - NULL;
    }

    @Override
    public int getRemaining(PlayerId id) {
        
        byte[] temp = grid.get(id).clone();
        Arrays.sort(temp);                      //sort so that all attacked ship grid will gather at the end of the array
        int i = 0;
        while (i<temp.length)
            if (temp[i++]==SHIPBODY_H_ATTACKED) //continues until it found an attacked ship grid
                return --i;                     //return the amount of non-attacked ship grid + all null grid (attacked or not)
        return 0;
        
    }

    @Override
    public void setReady(PlayerId id, boolean value) {
        if(isReady==null){
            isReady = new HashMap<>();
            for(PlayerId key : grid.keySet()){
                isReady.put(key, false);
            }
        }
        isReady.put(id, value);
    }

    @Override
    public void setAttacked(PlayerId id, boolean value) {
        if(hasAttacked==null){
            hasAttacked = new HashMap<>();
            for(PlayerId key : grid.keySet()){
                hasAttacked.put(key, false);
            }
        }
        hasAttacked.put(id, value);
    }
    
    public static final String player_separator = ""+(char)30;
    public static final String unit_separator = ";";
    public static final String grid_separator = ",";

    @Override
    public String getRoosterBroadcast(){
        
        String broadcastData = "";
        createOrUpdateRooster();
        for (int i = 0; i<grid.size(); i++) {
            broadcastData += rooster[i][0] + unit_separator + rooster[i][1] + unit_separator + rooster[i][2] + unit_separator + rooster[i][3];
            if(i<grid.size()-1)
                broadcastData += player_separator;
        }
        return broadcastData;
        
    }
    

    @Override
    public String getStartBroadcast() {
        
        String broadcastData = "";
        
        PlayerId[] temp = grid.keySet().toArray(new PlayerId[]{});
        
        for (int i = 0; i<grid.size(); i++) {
            broadcastData += temp[i].id + unit_separator + temp[i].ip + unit_separator + temp[i].name;
            if(i<grid.size()-1)
                broadcastData += player_separator;
        }
        
        return broadcastData;
        
    }
    
    @Override
    public String getStandingsBroadcast(){
        
        String broadcastData = "";
        createOrUpdateStandings();
        for (int i = 0; i<grid.size(); i++) {
            broadcastData += standings[i][0] + unit_separator + standings[i][1] + unit_separator + standings[i][2] + unit_separator + standings[i][3];
            if(i<grid.size()-1)
                broadcastData += player_separator;
        }
        return broadcastData;
    }
    
    @Override
    public String getGridBroadcast(){
        //TODO implement this
        return null;
    }
    
    private Object[][] rooster;
    @Override
    public void createOrUpdateRooster() {
        if(isReady==null){
            isReady = new HashMap<>();
            for(PlayerId key : grid.keySet()){
                isReady.put(key, false);
            }
        }
        if(rooster == null){
            rooster = new Object[4][4];
        }
        
        PlayerId[] temp = grid.keySet().toArray(new PlayerId[]{});
        
        for (int i = 0; i<grid.size(); i++) {
            rooster[i][0] = temp[i].ip;
            rooster[i][1] = temp[i].name;
            rooster[i][2] = isReady.get(temp[i]);
            rooster[i][3] = temp[i].id;
        }
        
        if(grid.size() < 4){
            for (int i = grid.size(); i<4; i++) {
                rooster[i][0] = null;
                rooster[i][1] = null;
                rooster[i][2] = null;
                rooster[i][3] = null;
            }
        }
    }

    private Object[][] standings;
    @Override
    public void createOrUpdateStandings() {
        if(hasAttacked==null){
            hasAttacked = new HashMap<>();
            for(PlayerId key : grid.keySet()){
                hasAttacked.put(key, false);
            }
        }
        if(standings == null){
            standings = new Object[4][4];
        }
        
        PlayerId[] temp = grid.keySet().toArray(new PlayerId[]{});
        
        for (int i = 0; i<grid.size(); i++) {
            standings[i][0] = temp[i].name;
            standings[i][1] = hasAttacked.get(temp[i]);
            standings[i][2] = getRemaining(temp[i]);
            standings[i][3] = temp[i].id;
        }
        
        if(grid.size() < 4){
            for (int i = grid.size(); i<4; i++) {
                standings[i][0] = null;
                standings[i][1] = null;
                standings[i][2] = null;
                standings[i][3] = null;
            }
        }
    }

    private Object[][] result;
    @Override
    public void createResult() {
        //create a sorted list
        result = new Object[4][3];
        
        PlayerId[] temp = grid.keySet().toArray(new PlayerId[]{});
        
        for (int i = 0; i<grid.size(); i++) {
            result[i][0] = 0;
            result[i][1] = temp[i].name;
            result[i][2] = getRemaining(temp[i]);
        }
        
        //selSort
        Object temp2;
        int max = 0; 
        for (int i = 0; i<grid.size()-1; i++) {
            max = i;
            for (int j = i; j<grid.size(); j++)
                if ((int)result[j][2] > (int)result[max][2])
                    max = j;
            //swap
            temp2 = result[max];
            result[max] = result[i];
            result[i] = result[max];
        }
        
        for (int i = 0; i<grid.size(); i++) 
            result[i][2] = ""+result[i][2]+" tiles";
    }

    @Override
    public Object[][] getRoosterData() {
        return rooster;
    }

    @Override
    public Object[][] getStandingsData() {
        return standings;
    }

    @Override
    public Object[][] getResultData() {
        return result;
    }

    @Override
    public void destroyRoosterData() {
        rooster = null;
        isReady = null;
    }

    @Override
    public void destroyStandingsData() {
        standings = null;
        hasAttacked = null;
    }

    @Override
    public void destroyResultData() {
        result = null;
    }

//    private PlayerId[] ready;
//    
//    private void updateReadyList() {
//        if(ready == null)
//            ready = new PlayerId[4];
//        
//        ready[0] = ready[1] = ready[2] = ready[3] = null;
//        
//        if(binary == null) return;
//        
//        PlayerId[] temp = grid.keySet().toArray(new PlayerId[]{});
//        temp = temp.clone();
//        
//        int i = 0;
//        for(PlayerId pid : temp)
//            if(binary.containsKey(pid) && binary.get(pid))
//                ready[i++] = pid;
//    }
    
    @Override
    public int getReadyCount() {
        PlayerId[] temp = grid.keySet().toArray(new PlayerId[]{});
        temp = temp.clone();
        
        int i = 0;
        for(PlayerId pid : temp)
            if(isReady.containsKey(pid) && isReady.get(pid))
                i++;
        
        return i;
    }

//    @Override
//    public PlayerId[] getReady() {
//        PlayerId[] temp = new PlayerId[getReadyCount()];
//        System.arraycopy(ready, 0, temp, 0, temp.length);
//        return temp;
//    }

    @Override
    public void trimNonReady() {
        
        if(isReady == null) return;
        
        PlayerId[] temp = grid.keySet().toArray(new PlayerId[]{});
        
        int i = 0;
        for(PlayerId pid : temp)
            if(!isReady.containsKey(pid) || !isReady.get(pid))
                grid.remove(pid);
        isReady = null;
        
    }
    
}
