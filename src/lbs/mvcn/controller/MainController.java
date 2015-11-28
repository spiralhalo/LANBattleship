/*
 * LAN Battleship
 * Copyright (c) 2015, spiralhalo
 * You are free to modify and reuse this program code for commercial and non-commercial purpose
 * with the condition that you removed this license header or replaced it with your own.
 */
package lbs.mvcn.controller;

import java.util.ArrayList;
import javax.swing.JOptionPane;
import lbs.mvcn.model.IModel;
import lbs.mvcn.model.MainModel;
import lbs.mvcn.model.PlayerId;
import lbs.mvcn.net.INetworker;
import lbs.mvcn.net.MainNetworker;
import lbs.mvcn.view.IView;
import lbs.mvcn.net.server.IServer;
import lbs.mvcn.net.server.MainServer;
import lbs.mvcn.view.MainView;

import static lbs.mvcn.net.protocol.ClientHeaders.*;
import static lbs.mvcn.net.protocol.ServerHeaders.*;
import static lbs.mvcn.net.protocol.Constants.*;
import static lbs.mvcn.net.protocol.Codec.*;
/**
 *
 * @author spiralhalo
 */

public class MainController implements IViewController, INetController, IServerController{
    
    private IView view;                     //handles gui
    private IServer server;                 //server handles server-side communication. only used when player is acting as server
    private INetworker net;                 //networker handles client-side communication
    private IModel model;                   //model handles players' data
    private String playerName;              //the player's name
    private String serverName;              //the server name of created/joined server
    private PlayerId serverMaster;          //the master of current server
    private ArrayList<PlayerId> playerList; //list of connected players of current created/joined server
    private StateEnum currentState;         //
    private int serverMaxPlayer;            //max player of current server. only used when player is acting as server
    private int playerCount;
    private boolean isMaster;               //whether the player is acting as server
    private boolean isReady = false;
            
    public MainController(){
        view = new MainView();
        model = new MainModel();

        view.setController(this);
        view.diplay();
        view.showLoadingScreen();
    }

    private void prepareJoinNewGame(boolean master) {
        model.resetAll();
        view.setModel(model);
        isMaster = master;
        isReady = false;
        playerList = new ArrayList<>();
        createNewNetworker();
    }
   
    private void createNewNetworker(){
       this.net = new MainNetworker();
       net.setController(this);
    }
   
    private void createNewServer(String serverName){
       this.server = new MainServer(serverName);
       server.setController(this);
    }

    private boolean isJoining = false; //bad..
    @Override
    public void onButtonClick(ButtonEnum buttonId) {
        switch(buttonId){
            case NAME_OK:
                playerName = view.getNameInput();
                view.showMainMenuScreen(playerName);
                break;
            case CHANGE_NAME:
                playerName = view.getNameInput();
                view.showMainMenuScreen(playerName);
                break;
            case CREATE_SERVER:
                view.showCreateServerScreen(playerName);
                break;
            case SERVERNAME_OK:
                serverName = view.getServerNameInput();
                serverMaxPlayer = view.getMaxPlayer();
                playerCount = 0;
                prepareJoinNewGame(true);
                createNewServer(serverName);
                server.startListening(PORT);
                break;
            case SERVER_START:
                model.trimNonReady();
                server.broadcast(encode(S_START, model.getStartBroadcast()));
                view.showGameScreen();
                view.modePlacing();
                break;
            case JOIN_SERVER:
                view.showJoinScreen();
                break;
            case CLIENT_READY:
                isReady = true;
                net.sendMessage(encode(C_READY));
                break;
            case HOSTNAME_OK:
                isJoining = true;
                prepareJoinNewGame(false);
                int status = net.connectToServer(playerName, view.getDestHostNameInput(), PORT);
                switch (status){
                    case 1:
                        JOptionPane.showMessageDialog(null, "Unknown error occured.", "Can't connect to server", JOptionPane.WARNING_MESSAGE);
                        break;
                    case 2: //host not found
                    case 3: //host found, but no server running on port 16273
                        JOptionPane.showMessageDialog(null, "Server not found.", "Can't connect to server", JOptionPane.WARNING_MESSAGE);
                        break;
                }
                if(status!=0){
                    isJoining = false;
                    view.showJoinScreen();
                }
                break;
            case CLIENT_LEAVE:
                net.closeConnection();
                model.resetAll();
                view.setModel(model);
                view.showMainMenuScreen(playerName);
                break;
            case CLIENT_UNREADY:
                isReady = false;
                net.sendMessage(encode(C_UNREADY));
                break;
            case TO_TITLE:
                //safe "back to title screen" without need to reset anything
                isJoining = false;
                view.showMainMenuScreen(playerName);
                break;
            default:
                throw new AssertionError(buttonId.name());
        }
    }

    @Override
    public void onLoadingEnd() {
        view.showInputNameScreen();
    }

    @Override
    public void onGridButtonClick(int index) {
        //TODO Implement this.
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    synchronized public void onReceiveFromServer(String message) {
        System.out.println("controller <server> "+message);
        String[] decoded = decode(message);
        String header = decoded[0];
        PlayerId temp;
        switch(header){
            case S_CONNECT_OK:
                serverName = decoded[1];
                if(isMaster){
                    net.sendMessage(encode(C_MASTER));
                    view.showServerButtonsOnly();
                } else view.showClientButtonsOnly();
                isJoining = false;
                view.showLobbyScreen(serverName, net.getIpAddress());
                break;
            case S_ROOSTER:
                if(!isMaster){
                    String[] playerData = decoded[1].split(MainModel.player_separator);
                    model.resetAll();
                    view.setModel(model);
                    for(String pdata:playerData){
                        String[] unitData = pdata.split(MainModel.unit_separator);
                        temp = model.addIfNotExist(Integer.parseInt(unitData[3]), unitData[0], unitData[1]);
                        model.setReady(temp, Boolean.parseBoolean(unitData[2]));
                    }
                }
                view.updateRoosterTable();
                break;
            case S_START:
                if(!isMaster && isReady){
                    String[] playerData = decoded[1].split(MainModel.player_separator);
                    model.resetAll();
                    view.setModel(model);
                    for(String pdata:playerData){
                        String[] unitData = pdata.split(MainModel.unit_separator);
                        model.addIfNotExist(Integer.parseInt(unitData[0]), unitData[1], unitData[2]);
                    }
                    view.showGameScreen();
                    view.modePlacing();
                } else if(!isMaster){
                    //not ready -> autokick
                    net.closeConnection();
                    view.showMainMenuScreen(playerName);
                    JOptionPane.showMessageDialog(null, "The game has started.", "Left server", JOptionPane.INFORMATION_MESSAGE);
                    break;
                }
                break;
        }
    }

    @Override
    public void onThreadClosed() {
        //TODO Implement this further.
        net = null;
        if(isJoining){
            JOptionPane.showMessageDialog(null, "Server is full.", "Can't connect to server", JOptionPane.WARNING_MESSAGE);
            view.showJoinScreen();
        }
    }

    @Override
    public void onServerStartListening() {
        System.out.println("controller : server listening confirmed");
        net.connectToServer(playerName, server.getIpAddress(), PORT);
    }

    @Override
    synchronized public void onClientJoined(String clientIpAddress, String clientName, int serverId) {
        addPlayer(serverId, clientIpAddress, clientName);
        PlayerId temp = findPid(playerName, serverId);
        model.setReady(temp, false);
        broadcastRooster();
        view.updateRoosterTable();
    }

    @Override
    synchronized public void onReceiveFromClient(String clientName, int serverId, String message) {
        System.out.println("controller <client "+clientName+", "+serverId+"> : "+message);
        String[] decoded = decode(message);
        String header = decoded[0];
        PlayerId temp;
        switch(header){
            case C_MASTER:
                temp = findPid(clientName, serverId);
                serverMaster = temp;
                model.setReady(temp, true);
                broadcastRooster();
                break;
            case C_READY:
            case C_UNREADY:
                temp = findPid(clientName, serverId);
                model.setReady(temp, header.equals(C_READY));
                broadcastRooster();
                break;
        }
    }
    
    @Override
    synchronized public void onClientLeft(String clientName, int serverId) {
        removePlayer(clientName, serverId);
        broadcastRooster();
    }

    @Override
    public void onServerThreadClosed() {
        System.out.println("controller : server terminated.");
    }
    
    @Override
    synchronized public boolean isJoiningAllowed() {
        return playerCount < serverMaxPlayer;
    }
    
    //for server
    private void addPlayer(int serverId, String ipAddress, String playerName){
        playerCount++;
        playerList.add(model.addIfNotExist(serverId, ipAddress, playerName));
    }
    
    //for server
    private void removePlayer(String playerName, int serverId){
        playerCount--;
        PlayerId temp = findPid(playerName, serverId);
        model.remove(temp);
        playerList.remove(temp);
    }
    
    private void broadcastRooster() {
        server.broadcast(encode(S_ROOSTER, model.getRoosterBroadcast()));
        view.updateRoosterTable();
    }
    
    private PlayerId findPid(String playerName, int serverId) {
        for( PlayerId pid : playerList )
            if( pid.equals(playerName, serverId) )
                return pid;
        return null;
    }
}
