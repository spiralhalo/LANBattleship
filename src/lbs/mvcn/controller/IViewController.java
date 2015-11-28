/*
 * LAN Battleship
 * Copyright (c) 2015, spiralhalo
 * You are free to modify and reuse this program code for commercial and non-commercial purpose
 * with the condition that you removed this license header or replaced it with your own.
 */
package lbs.mvcn.controller;

/**
 *
 * @author lenovo z40
 */
public interface IViewController {
    
    public void onLoadingEnd();
    public void onButtonClick(ButtonEnum buttonId);
    public void onGridButtonClick(int index);
    
}
