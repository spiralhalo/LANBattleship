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
            case S_ROOSTER:
            case S_START:
                return header + separator + args[0];
            case META_NAME:
                return META_NAME + separator + args[0];
            case C_MASTER:
            case C_READY:
            case C_UNREADY:
                return header;
        }
        return null;
    }
    
    public static String[] decode(String message){
        return message.split(""+separator);
    }
}
