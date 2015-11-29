/*
 * LAN Battleship
 * Copyright (c) 2015, spiralhalo
 * You are free to modify and reuse this program code for commercial and non-commercial purpose
 * with the condition that you removed this license header or replaced it with your own.
 */
package lbs.mvcn.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
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
import static lbs.mvcn.model.Constants.*;
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
    private int serverId;
    private String serverName;              //the server name of created/joined server
//    private PlayerId serverMaster;        //the master of current server
//    private ArrayList<PlayerId> playerList; //list of connected players of current created/joined server
    private int serverMaxPlayer;            //max player of current server. only used when player is acting as server
    private int playerCount;
    private boolean isMaster;               //whether the player is acting as server
    private boolean isReady = false;
    private int serverSignalReceived = 0;   //signal received by server on current state. only used when player is acting as server
    private HashMap<PlayerId, LinkedList> tempAttackReceived; //used by server to record attacks given to a player in the current round
    private PlayerId[] turnLineup;          //used by server when activating "turn based" mode
    private int playerTurn;                 //used by server when "turn based" mode is active... which is when only 2 players are left

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
                model.resetAlive();
                turnLineup = null;
                playerTurn = 0;
                server.broadcast(encode(S_START,  model.getStartBroadcast()));
                PlayerId[] temp3 = model.toArray();
                PlayerId[] temp2 = new PlayerId[temp3.length];
                int i = 0;
                for(PlayerId temp:temp3){
                    if(temp.name.equals(playerName) && temp.id == serverId){
                        temp2[temp2.length-1] = temp;
                    } else {
                        temp2[i++] = temp;
                    }
                }
                if(model.getPlayerCount()==2){
                    //activate turn based mode
                    turnLineup = model.toArray();
                }
                serverSignalReceived = 0;
                tempAttackReceived = new HashMap<>();
                view.showGameScreen(temp2);
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
    synchronized public void onReceiveFromServer(String message) {
        System.out.println("controller <server> "+message);
        String[] decoded = decode(message);
        String header = decoded[0];
        PlayerId temp;
        switch(header){
            case S_CONNECT_OK:
                serverId = Integer.parseInt(decoded[1]);
                serverName = decoded[2];
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
                    PlayerId[] temp2 = new PlayerId[playerData.length];
                    int i = 0;
                    for(String pdata:playerData){
                        String[] unitData = pdata.split(MainModel.unit_separator);
                        temp = model.addIfNotExist(Integer.parseInt(unitData[0]), unitData[1], unitData[2]);
                        if(temp.name.equals(playerName) && temp.id == serverId){
                            temp2[temp2.length-1] = temp;
                        } else {
                            temp2[i++] = temp;
                        }
                    }
                    
                    view.setModel(model);
                    view.showGameScreen(temp2);
                    view.modePlacing();
                    
                } else if(!isMaster){
                    //not ready -> autokick
                    net.closeConnection();
                    view.showMainMenuScreen(playerName);
                    JOptionPane.showMessageDialog(null, "The game has started.", "Left server", JOptionPane.INFORMATION_MESSAGE);
                    break;
                }
                break;
            case S_PLACEMENT:
                if(!isMaster){
                    model.processAllGridData(decoded[1]);
                    view.updateStandingsTable(false);
                    view.modeBegin();
                }
                break;
            case S_ATTACK:
                if(!isMaster){
                    model.setAttacked(model.get(decoded[2], Integer.parseInt(decoded[1])), true);
                    view.updateStandingsTable(true);
                }
                break;
            case S_ATTACKS:
                if(!isMaster){
                    model.processAttacksData(decoded[1]);
                    view.updateStandingsTable(false);
                    if(model.getHealth(model.get(playerName, this.serverId)) <= 0){
                        net.sendMessage(encode(C_DEAD));
                        view.modeDead();
                    }
                    view.modeExecute();
                }
                break;
            case S_TURN:
                if(!isMaster){
                    temp = model.get(decoded[2], Integer.parseInt(decoded[1]));
                    if(temp != model.get(playerName, this.serverId)) {
                        view.modeWaiting();
                        view.displayTurn(false);
                    } else {
                        view.modeBegin();
                        view.displayTurn(true);
                    }
                }
                break;
            case S_FINISH:
                if(!isMaster){
                    model.processResultData(decoded[1]);
                    view.cleanGameScreen();
                    view.showResultScreen();
                    net.closeConnection();
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
        PlayerId temp = model.get(clientName, serverId);
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
                temp = model.get(clientName, serverId);
//                serverMaster = temp;
                model.setReady(temp, true);
                broadcastRooster();
                break;
            case C_READY:
            case C_UNREADY:
                temp = model.get(clientName, serverId);
                model.setReady(temp, header.equals(C_READY));
                broadcastRooster();
                break;
            case C_PLACEMENT:
                serverSignalReceived ++;
                model.processPlacementData(clientName, serverId, decoded[1]);
                if(serverSignalReceived==model.getPlayerCount()){
                    server.broadcast(encode(S_PLACEMENT,model.getPlacementBroadcast()));
                    serverSignalReceived = 0;
                    view.updateStandingsTable(false);
                    if(turnLineup == null){
                        view.modeBegin();
                    } else {
                        processTurn();
                    }
                }
                break;
            case C_ATTACK:
                temp = model.get(decoded[2], Integer.parseInt(decoded[1]));
                serverSignalReceived ++;
                int index = Integer.parseInt(decoded[3]);
//                model.attackGrid(temp, index/10, index%10);
                if(!tempAttackReceived.containsKey(temp))
                    tempAttackReceived.put(temp, new LinkedList());
                tempAttackReceived.get(temp).add(index);
                
                model.setAttacked(model.get(clientName, serverId), true);
                server.broadcast(encode(S_ATTACK,""+serverId,clientName));
                
                view.updateStandingsTable(true);
                if(turnLineup!= null || serverSignalReceived==model.getAliveCount()){
                    serverSignalReceived = 0;
                    server.broadcast(encode(S_ATTACKS, model.getAttackBroadcast(tempAttackReceived)));
                    Object[] attacker = tempAttackReceived.keySet().toArray();
                    for(Object p:attacker){
                        for(Object i:tempAttackReceived.get((PlayerId)p))
                            model.attackGrid((PlayerId)p, ((int)i)/10, ((int)i)%10);
                        tempAttackReceived.remove((PlayerId)p);
                    }
                    if(model.getHealth(model.get(playerName, this.serverId)) <= 0){
                        net.sendMessage(encode(C_DEAD));
                        view.modeDead();
                    }
                    if(turnLineup==null&&model.getAliveCount()==2){
                        turnLineup = new PlayerId[2];
                        int i = 0;
                        for(PlayerId p:model.toArray())
                            if(model.getHealth(p)>0)
                                turnLineup[i++] = p;
                    }
                    view.updateStandingsTable(false);
                    view.modeExecute();
                }
                break;
            case C_DEAD:
                model.setAlive(model.get(clientName, serverId), false);
                if(model.getAliveCount()<=1){
                    view.cleanGameScreen();
                    view.showResultScreen();
                    server.broadcast(encode(S_FINISH,model.getResultBroadcast()));
                    server.closeAll();
                    net.closeConnection();
                }
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
        model.addIfNotExist(serverId, ipAddress, playerName);
    }
    
    //for server
    private void removePlayer(String playerName, int serverId){
        playerCount--;
        PlayerId temp = model.get(playerName, serverId);
        model.remove(temp);
    }
    
    private void broadcastRooster() {
        server.broadcast(encode(S_ROOSTER, model.getRoosterBroadcast()));
        view.updateRoosterTable();
    }

    @Override
    public void onGridButtonClick(PlayerId target, int index) {
        view.modeWaiting();
        net.sendMessage(encode(C_ATTACK,""+target.id,target.name,""+index));
    }

    @Override
    public void onPlaceShip(int index, int size, boolean vertical) {
        for(int i=0; i<size; i++)
            if(vertical){
                if(i==0)
                    model.setGrid(model.get(playerName, serverId), index/10+i, index%10, SHIPHEAD_V);
                else if(i==size-1)
                    model.setGrid(model.get(playerName, serverId), index/10+i, index%10, SHIPTAIL_V);
                else model.setGrid(model.get(playerName, serverId), index/10+i, index%10, SHIPBODY_V);
            } else {
                if(i==0)
                    model.setGrid(model.get(playerName, serverId), index/10, index%10+i, SHIPHEAD_H);
                else if(i==size-1)
                    model.setGrid(model.get(playerName, serverId), index/10, index%10+i, SHIPTAIL_H);
                else model.setGrid(model.get(playerName, serverId), index/10, index%10+i, SHIPBODY_H);
            }
    }

    @Override
    public void onFinishPlacingShip() {
        net.sendMessage(encode(C_PLACEMENT,model.getPlacementMessage(model.get(playerName, serverId))));
    }

    @Override
    public void onFinishExecution() {
        model.resetAttacked();
        view.updateStandingsTable(false);
        if(isMaster && turnLineup != null)
            processTurn();
        else view.modeBegin();
    }
    
    //server
    private void processTurn() {
        PlayerId temp;
        temp = turnLineup[playerTurn++];
        playerTurn %= 2;
        server.broadcast(encode(S_TURN, ""+temp.id, temp.name));
        if(temp != model.get(playerName, serverId)){
            view.modeWaiting();
            view.displayTurn(false);
        } else {
            view.modeBegin();
            view.displayTurn(true);
        }
    }
}
