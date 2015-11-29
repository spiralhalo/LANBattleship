/*
 * LAN Battleship
 * Copyright (c) 2015, spiralhalo
 * You are free to modify and reuse this program code for commercial and non-commercial purpose
 * with the condition that you removed this license header or replaced it with your own.
 */
package lbs.mvcn.view;

import lbs.mvcn.controller.IViewController;
import lbs.mvcn.model.IModel;
import lbs.mvcn.model.PlayerId;

/**
 *
 * @author lenovo z40
 */
public interface IView {
    
    public void setController(IViewController controller);
    public void diplay();
    public void setModel(IModel model);
    public void showLoadingScreen();
    public void showInputNameScreen();
    public void showMainMenuScreen(String playerName);
    public String getNameInput();
    public void showCreateServerScreen(String playerName);
    public String getServerNameInput();
    public int getMaxPlayer();
    public void showJoinScreen();
    public String getDestHostNameInput();
    public void showLobbyScreen(String serverName, String ipAddress);
    public void updateRoosterTable();
    public void updateStandingsTable(boolean passive);
    public void showClientButtonsOnly();
    public void showServerButtonsOnly();
    public void showGameScreen(PlayerId[] pidArrangement);
    public void modePlacing();
    public void modeBegin();
    public void modeWaiting();
    public void modeExecute();
    public void modeDead();
    public void cleanGameScreen();
    public void showResultScreen();
    public void displayTurn(boolean myTurn);
    
}
