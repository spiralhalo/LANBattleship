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
    HashMap<PlayerId, Boolean> binary;
    
    public MainModel(){
        grid = new HashMap<>();
    }

    @Override
    public PlayerId add(int playerId, String ipAddress, String playerName) {
        PlayerId temp = new PlayerId(playerName, ipAddress, playerId);
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
    public void setReady(boolean value) {
        //TODO Implement this.
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setAttacked(boolean value) {
        //TODO Implement this.
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private Object[][] rooster;
    @Override
    public void createOrUpdateRooster() {
        if(rooster == null){
            rooster = new Object[grid.size()][3];
        }
        
        PlayerId[] temp = (PlayerId[])grid.keySet().toArray();
        temp = temp.clone();
        
        for (int i = 0; i<grid.size(); i++) {
            standings[i][0] = temp[i].ip;
            standings[i][1] = temp[i].name;
            standings[i][2] = binary.get(temp[i]);
        }
    }

    private Object[][] standings;
    @Override
    public void createOrUpdateStandings() {
        if(standings == null){
            standings = new Object[grid.size()][3];
        }
        
        PlayerId[] temp = (PlayerId[])grid.keySet().toArray();
        temp = temp.clone();
        
        for (int i = 0; i<grid.size(); i++) {
            standings[i][0] = temp[i].name;
            standings[i][1] = binary.get(temp[i]);
            standings[i][2] = getRemaining(temp[i]);
        }
    }

    private Object[][] result;
    @Override
    public void createResult() {
        //create a sorted list
        result = new Object[grid.size()][3];
        
        PlayerId[] temp = (PlayerId[])grid.keySet().toArray();
        temp = temp.clone();
        
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
    }

    @Override
    public void destroyStandingsData() {
        standings = null;
    }

    @Override
    public void destroyResultData() {
        result = null;
    }
    
}
