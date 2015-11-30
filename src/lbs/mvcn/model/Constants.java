/*
 * LAN Battleship
 * Copyright (c) 2015, spiralhalo
 * You are free to modify and reuse this program code for commercial and non-commercial purpose
 * with the condition that you removed this license header or replaced it with your own.
 */
package lbs.mvcn.model;

/**
 *
 * @author spiralhalo
 */
public class Constants {
    
    public static byte NULL                = 0;     //[   ]
    public static byte SHIPBODY_H          = 1;     //[===]
    public static byte SHIPBODY_V          = 2;     //[| |]
    public static byte SHIPHEAD_H          = 3;     //[<==]
    public static byte SHIPHEAD_V          = 4;     //[/ |]
    public static byte SHIPTAIL_H          = 5;     //[==>]
    public static byte SHIPTAIL_V          = 6;     //[|_/]
    public static byte NULL_ATTACKED       = 7;     //[xxx]
    public static byte SHIPBODY_H_ATTACKED = 8;     //[=x=]
    public static byte SHIPBODY_V_ATTACKED = 9;     //[|x|]
    public static byte SHIPHEAD_H_ATTACKED = 10;    //[<x=]
    public static byte SHIPHEAD_V_ATTACKED = 11;    //[/x|]
    public static byte SHIPTAIL_H_ATTACKED = 12;    //[=x>]
    public static byte SHIPTAIL_V_ATTACKED = 13;    //[|x/]
    public static byte DISABLE_GRID        = 99;    //put on first tile to flag grid as disabled
    
}
