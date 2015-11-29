/*
 *  LAN Battleship
 *  Copyright (c) 2015, spiralhalo
 *  You are free to modify and reuse this program code for commercial and non-commercial purpose
 *  with the condition that you removed this license header or replaced it with your own.
 */
package lbs.mvcn.net.protocol;

import static lbs.mvcn.net.protocol.ClientHeaders.*;
import static lbs.mvcn.net.protocol.ServerHeaders.*;

/**
 *
 * @author spiralhalo
 */
public class Codec {
    
    public static final char separator = (char)31;
    
    public static String encode(String header, String... args) {
        switch(header){
            case S_CONNECT_OK:
            case S_ATTACK:
            case S_TURN:
                return header + separator + args[0] + separator + args[1];
            case S_ROOSTER:
            case S_START:
            case S_PLACEMENT:
            case S_ATTACKS:
            case S_FINISH:
                return header + separator + args[0];
            case META_NAME:
                return META_NAME + separator + args[0];
            case C_MASTER:
            case C_READY:
            case C_UNREADY:
            case C_DEAD:
                return header;
            case C_PLACEMENT:
                return header + separator + args[0];
            case C_ATTACK:
                return header + separator + args[0] + separator + args[1] + separator + args[2];
        }
        return null;
    }
    
    public static String[] decode(String message){
        return message.split(""+separator);
    }
}
