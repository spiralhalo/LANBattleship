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
    
    public MainModel(){
        grid = new HashMap<>();
    }

    @Override
    public PlayerId add(int playerId, String playerName) {
        PlayerId temp = new PlayerId(playerName, playerId);
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
    
}
