/*
 * LAN Battleship
 * Copyright (c) 2015, spiralhalo
 * You are free to modify and reuse this program code for commercial and non-commercial purpose
 * with the condition that you removed this license header or replaced it with your own.
 */
package lbs.mvcn.view;

import lbs.mvcn.controller.IViewController;
import lbs.mvcn.model.IModel;

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
    public void showCreateServerScreen();
    public String serverNameInput();
    public void showJoinScreen();
    public String destHostNameInput();
    public void showLobbyScreen(String serverName, String ipAddress);
    public void refreshReadyList();
    public void showClientButtonsOnly();
    public void showServerButtonsOnly();
    public void prepareGameScreen();
    public void showGameScreen();
    public void prepareGameScreenForShipArrangement();
    public void updateGameScreenForStartingRound();
    public void updateGameScreenForWaiting();
    public void updateGameScreenForExecution();
    public void cleanGameScreen();
    public void showResultScreen();
    
}
