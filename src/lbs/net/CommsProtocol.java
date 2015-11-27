/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lbs.net;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author lenovo z40
 */
public class CommsProtocol {
    
    private static InetAddress address;
    
    public static String getAddress(){
        try {
            if(address==null)
                address = InetAddress.getLocalHost();
        } catch (UnknownHostException ex) {
            Logger.getLogger(CommsProtocol.class.getName()).log(Level.SEVERE, null, ex);
        }
        return address.getHostAddress();
    }
    
    public static final String H_NAME = "n";
    public static final String H_SERVER_NAME = "sn";
    
    public static final int T_NAME = 0;
    public static final int T_SERVER_NAME = 1;
    
    public static final int PORT = 47474;
    
    public static boolean processMessage(String msg, CommsResult resultBuffer){
        
        String header;
        String[] split;
        
        split = msg.split("[|]");
        header = split[0];
        
        switch(header){
            case(H_NAME):
                resultBuffer.set(T_NAME, split[1]);
                return true;
            case(H_SERVER_NAME):
                resultBuffer.set(T_SERVER_NAME, split[1]);
                return true;
        }
        
        return false;
    }
    
    public static String message(String header, String message){
        return header + "|" + message;
    }
}
