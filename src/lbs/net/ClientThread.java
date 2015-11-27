/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lbs.net;

import static lbs.net.CommsProtocol.*;
import static lbs.net.StandardMessage.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author lenovo z40
 */
public class ClientThread extends Thread {
    
    private String idname, hostname, serverName;
    
    private Socket sock;
    private PrintWriter out;
    private BufferedReader in;
    
    private boolean ready = false;
    
    public ClientThread(String clientName, String serverHostname){
        idname = clientName;
        hostname = serverHostname;
    }
    
    public boolean ready(){
        return ready;
    }
    
    @Override
    public void run(){
        try{
            sock = new Socket(hostname, PORT);
            out = new PrintWriter(sock.getOutputStream());
            in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            ready = true;
            String read;
            CommsResult comms = new CommsResult();
            while((read = in.readLine())!=null){
                if(CommsProtocol.processMessage(read, comms)){
                    switch(comms.consumeType()){
                        case T_SERVER_NAME:
                            serverName = comms.consumeResult();
                            System.out.println(String.format(JOINED___SERVER, serverName));
                            out.println(CommsProtocol.message(H_NAME, idname));
                            out.flush();
                            break;
                    }
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if(in!=null)
                    in.close();
                if(out!=null)
                    out.close();
            } catch (IOException ex) {
                Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void sendMessage(String msg){
        out.println(msg);
    }
}
