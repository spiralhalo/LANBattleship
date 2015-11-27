/*
 * LAN Battleship
 * Copyright (c) 2015, spiralhalo
 * You are free to modify and reuse this program code for commercial and non-commercial purpose
 * with the condition that you removed this license header or replaced it with your own.
 */
package lbs.mvcn.controller;

import lbs.mvcn.net.INetworker;
import lbs.mvcn.net.MainNetworker;
import lbs.mvcn.view.IView;

/**
 *
 * @author lenovo z40
 */
enum ButtonEnum {
    NAME_OK,
    CHANGE_NAME,
    CREATE_SERVER,
    SERVERNAME_OK,
    SERVER_START,
    JOIN_SERVER,
    CLIENT_READY
}

enum StateEnum {
    NOT_INGAME,
    PREGAME,
    ARRANGING_SHIPS,
    WAITING_FOR_PLAYER_DECISION,
    DECISION_MADE_WAITING_FOR_SERVER,
    SERVER_REPLIED_EXECUTING_BEGIN_NEXT_ROUND,
    SERVER_DECLARED_GAME_END;
}

public class MainController implements IViewController, INetController{
    
    private IView view;
    private INetworker net;
    private String playerName;
    private StateEnum currentState;
            
   public MainController(IView view){
       this.view = view;
       
       view.setController(this);
   }
   
    public void createNewNetworker(){
       this.net = new MainNetworker();
       net.setController(this);
    }
    
    public void startNetworker(){
        
    }
    
    //set current state as ns and do whatever must be done in that state
    void nextState(StateEnum ns){
        currentState = ns;
        switch(currentState){
            
        }
    }

    @Override
    public void onButtonClick(ButtonEnum buttonId) {
        switch(buttonId){
            case NAME_OK:
                break;
            case CHANGE_NAME:
                break;
            case CREATE_SERVER:
                break;
            case SERVERNAME_OK:
                break;
            case SERVER_START:
                break;
            case JOIN_SERVER:
                break;
            case CLIENT_READY:
                break;
            default:
                throw new AssertionError(buttonId.name());
        }
    }

    @Override
    public void onGridButtonClick(int index) {
        //TODO Implement this.
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onReceiveFromServer(String message) {
        //TODO Implement this.
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onThreadClosed() {
        //TODO Implement this.
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
