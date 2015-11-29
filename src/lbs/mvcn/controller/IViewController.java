/*
 * LAN Battleship
 * Copyright (c) 2015, spiralhalo
 * You are free to modify and reuse this program code for commercial and non-commercial purpose
 * with the condition that you removed this license header or replaced it with your own.
 */
package lbs.mvcn.controller;

import lbs.mvcn.model.PlayerId;

/**
 *
 * @author lenovo z40
 */
public interface IViewController {
    
    public void onLoadingEnd();
    public void onButtonClick(ButtonEnum buttonId);
    public void onGridButtonClick(PlayerId target, int index);
    public void onPlaceShip(int index, int size, boolean vertical);
    public void onFinishPlacingShip();
    public void onFinishExecution();
    
}
