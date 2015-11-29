/*
 * LAN Battleship
 * Copyright (c) 2015, spiralhalo
 * You are free to modify and reuse this program code for commercial and non-commercial purpose
 * with the condition that you removed this license header or replaced it with your own.
 */
package lbs.mvcn.model;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import static lbs.mvcn.model.Constants.*;

/**
 *
 * @author spiralhalo
 */
public class MainModel implements IModel{

    HashMap<PlayerId, byte[]> grid;
    HashMap<PlayerId, Boolean> isReady;
    HashMap<PlayerId, Boolean> hasAttacked;
    HashMap<PlayerId, Boolean> alive;
    
    @Override
    public void resetAll(){
        grid = new HashMap<>();
        isReady = null;
        hasAttacked = null;
    }

    private PlayerId exists(int playerId, String playerName) {
        for(PlayerId pid : grid.keySet())
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
    public void attackGrid(PlayerId id, int row, int col) {
        if(grid.get(id)[row * 10 + col] < NULL_ATTACKED)
            //change grid state to "whatever_ATTACKED" by adding a constant offset
            grid.get(id)[row * 10 + col] += NULL_ATTACKED - NULL;
    }

    @Override
    public int getHealth(PlayerId id) {
        
        byte[] temp = grid.get(id).clone();
        Arrays.sort(temp);              //sort so that all attacked grid will gather at the end of the array
        int i = 0;
        while (i<temp.length){
            if (temp[i]>NULL)           //set i = first healthy ship body index
                break;            
            i++;
            }
        int j = i;
        while (j<temp.length){
            if (temp[j]>=NULL_ATTACKED) //set j = last healthy ship body index            
                break;
            j++;
            }
        return j-i;                     //return the amount of healthy ship body
        
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
    
    public static final String player_separator = "."+(char)30;
    public static final String unit_separator = "_`.";
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
        
        int i = 0;
        for (PlayerId p:grid.keySet()) {
            broadcastData += p.id + unit_separator + p.ip + unit_separator + p.name;
            if(i<grid.size()-1)
                broadcastData += player_separator;
            i++;
        }
        
        return broadcastData;
        
    }
    
//    @Override
//    public String getStandingsBroadcast(){
//        
//        String broadcastData = "";
//        createOrUpdateStandings(false);
//        for (int i = 0; i<grid.size(); i++) {
//            broadcastData += standings[i][0] + unit_separator + standings[i][1] + unit_separator + standings[i][2] + unit_separator + standings[i][3];
//            if(i<grid.size()-1)
//                broadcastData += player_separator;
//        }
//        return broadcastData;
//    }
    
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
        
        int i = 0;
        for (PlayerId p:grid.keySet()) {
            rooster[i][0] = p.ip;
            rooster[i][1] = p.name;
            rooster[i][2] = isReady.get(p);
            rooster[i][3] = p.id;
            i++;
        }
        
        if(grid.size() < 4){
            for (i = grid.size(); i<4; i++) {
                rooster[i][0] = null;
                rooster[i][1] = null;
                rooster[i][2] = null;
                rooster[i][3] = null;
            }
        }
    }

    private Object[][] standings;
    @Override
    public void createOrUpdateStandings(boolean passive) {
        if(hasAttacked==null){
            hasAttacked = new HashMap<>();
            for(PlayerId key : grid.keySet()){
                hasAttacked.put(key, false);
            }
        }
        if(standings == null){
            standings = new Object[4][4];
        }
        
        int i = 0;
        for (PlayerId p:grid.keySet()) {
            standings[i][0] = p.name;
            standings[i][1] = hasAttacked.get(p);
            if(!passive)standings[i][2] = getHealth(p);
            standings[i][3] = p.id;
            i++;
        }
        
        if(grid.size() < 4){
            for (i = grid.size(); i<4; i++) {
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
        result = new Object[4][2];
        
        int i = 0;
        for (PlayerId p:grid.keySet()) {
            if(death.containsKey(p))
                result[i][0] = getPlayerCount()-death.get(p);
            else result[i][0] = 1;
            result[i][1] = p.name;
            i++;
        }
        
        //selSort
        int min;
        for (i = 0; i<grid.size()-1; i++) {
            min = i;
            for (int j = i; j<grid.size(); j++)
                if ((int)result[j][0] < (int)result[min][0])
                    min = j;
            //swap
            Object[] temp2 = result[min];
            result[min] = result[i];
            result[i] = temp2;
        }
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
    
    @Override
    public int getReadyCount() {
        int i = 0;
        for(PlayerId pid : grid.keySet())
            if(isReady.containsKey(pid) && isReady.get(pid))
                i++;
        
        return i;
    }

    @Override
    public void trimNonReady() {
        
        if(isReady == null) return;
        
        PlayerId[] temp = toArray();
        for(PlayerId pid : temp)
            if(!isReady.containsKey(pid) || !isReady.get(pid))
                grid.remove(pid);
        
        isReady = null;
        
    }

    @Override
    public PlayerId[] toArray() {
        return grid.keySet().toArray(new PlayerId[]{}).clone();
    }

    @Override
    public void setGrid(PlayerId id, int row, int col, byte value) {
        if(row * 10 + col < 100)
            grid.get(id)[row * 10 + col] = value;
    }

    @Override
    public PlayerId get(String playerName, int playerId) {
        for(PlayerId pid : grid.keySet())
            if(pid.equals(playerName, playerId))
                return pid;
        return null;
    }

    @Override
    public String getPlacementMessage(PlayerId id) {
        String toSend = "";
        for(int i=0; i<100; i++)
            toSend += ""+grid.get(id)[i]+(i<99?grid_separator:"");
        return toSend;
    }

    @Override
    public String getPlacementBroadcast() {
        String broadcast = "";
        int i = 0;
        for(PlayerId id:grid.keySet()){
            broadcast += id.id + unit_separator + id.name + unit_separator + getPlacementMessage(id);
            if(i<grid.size()-1)
                broadcast += player_separator;
            i++;
        }
        return broadcast;
    }

    @Override
    public void processPlacementData(String playerName, int playerId, String data) {
        PlayerId temp = get(playerName, playerId);
        String[] gridData = data.split(grid_separator);
        for(int i =0;i<100;i++)
            grid.get(temp)[i] = Byte.parseByte(gridData[i]);
    }

    @Override
    public void processAllGridData(String allGridData) {
        String[] pData = allGridData.split(player_separator);
        for(String pdata:pData){
            String[] uData = pdata.split(unit_separator);
            processPlacementData(uData[1], Integer.parseInt(uData[0]), uData[2]);
        }
    }

    @Override
    public String getAttackBroadcast(HashMap<PlayerId,LinkedList> attack) {
        String broadcast = "";
        int i = 0;
        for(PlayerId p:attack.keySet()){
            broadcast += p.id + unit_separator + p.name + unit_separator;
            int j = 0;
            for(Object index:attack.get(p)){
                broadcast += index;
                if(j<attack.get(p).size()-1)
                    broadcast += grid_separator;
                j++;
            }
            if(i<attack.size()-1)
                broadcast += player_separator;
            i++;
        }
        return broadcast;
    }

    @Override
    public void processAttacksData(String attacks) {
        String[] pData = attacks.split(player_separator);
        for(String pdata:pData){
            String[] uData = pdata.split(unit_separator);
            String[] indexes = uData[2].split(grid_separator);
            for(String si:indexes){
                int index = Integer.parseInt(si);
                attackGrid(get(uData[1], Integer.parseInt(uData[0])), index/10, index%10);
            }
        }
    }
    
    @Override
    public void resetAlive() {
        death = new HashMap<>();
        deathCount = 0;
        alive=new HashMap<>();
        for(PlayerId key : grid.keySet())
            alive.put(key, true);
    }

    @Override
    public void resetAttacked() {
        if(hasAttacked==null) return;
        for(PlayerId key : grid.keySet())
            hasAttacked.put(key, false);
    }

    @Override
    public int getAliveCount() {
        int i = 0;
        System.out.println(alive.toString());
        for(PlayerId key : alive.keySet())
            if(alive.get(key)) i++;
        System.out.println("alive:"+i);
        return i;
    }

    private int deathCount = 0;
    private HashMap<PlayerId, Integer> death;
    @Override
    public void setAlive(PlayerId id, boolean value) {
        if(!value){
            if(!death.containsKey(id))
                death.put(id, deathCount++);
        }
        alive.put(id, value);
    }

    @Override
    public String getResultBroadcast() {
        String broadcast = "";
        int i = 0;
        for(PlayerId id:grid.keySet()){
            if(death.containsKey(id))
                broadcast += id.id + unit_separator + id.name + unit_separator + death.get(id);
            else
                broadcast += id.id + unit_separator + id.name + unit_separator + (getPlayerCount()-1);
            if(i<grid.size()-1)
                broadcast += player_separator;
            i++;
        }
        return broadcast;
    }

    @Override
    public void processResultData(String deaths) {
        String[] pData = deaths.split(player_separator);
        resetAlive();
        for(String pdata:pData){
            String[] uData = pdata.split(unit_separator);
            int deathOrder = Integer.parseInt(uData[2]);
            death.put(get(uData[1], Integer.parseInt(uData[0])), deathOrder);
        }
    }
    
}
