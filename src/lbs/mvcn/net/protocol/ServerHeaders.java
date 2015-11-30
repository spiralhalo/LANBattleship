/*
 *  LAN Battleship
 *  Copyright (c) 2015, spiralhalo
 *  You are free to modify and reuse this program code for commercial and non-commercial purpose
 *  with the condition that you removed this license header or replaced it with your own.
 */
package lbs.mvcn.net.protocol;

/**
 *
 * @author spiralhalo
 */
public class ServerHeaders {
    
    public static final String S_CONNECT_OK = "connect ok"      ;
    public static final String S_ROOSTER    = "player list"     ;
    public static final String S_START      = "start"           ;
    public static final String S_PLACEMENT  = "all grid data"   ;
    public static final String S_ATTACK     = "player attacks"  ;
    public static final String S_ATTACKS    = "attakcs"         ;
    public static final String S_FINISH     = "finish"          ;
    public static final String S_TURN       = "player turn"     ;
    public static final String S_DEAD       = "player dead"     ;
    public static final String S_DISCONNECT = "disconnected"    ;
    
}
