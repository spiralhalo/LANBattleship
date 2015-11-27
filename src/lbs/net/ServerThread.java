/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lbs.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;
import static lbs.net.CommsProtocol.*;
import static lbs.net.StandardMessage.*;

/**
 *
 * @author lenovo z40
 */
public class ServerThread extends Thread{
    
    public int id;
    
    private BufferedReader in;
    private PrintWriter out;
    private Socket sock;
    
    private String clientName, serverName;
    
    private boolean ready = false;
    
    public ServerThread(String name, int threadId, Socket clientSocket){
        id = threadId;
        serverName = name;
        try{
            sock = clientSocket;
            out = new PrintWriter(sock.getOutputStream());
            in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
        } catch (IOException ex) {
            Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public boolean ready(){
        return ready;
    }
    
    @Override
    public void run(){
        try{
            String read;
            ready = true;
            CommsResult comms = new CommsResult();
            sendMessage(CommsProtocol.message(H_SERVER_NAME, serverName));
            while((read = in.readLine())!=null){
                if(CommsProtocol.processMessage(read, comms)){
                    switch(comms.consumeType()){
                        case T_NAME:
                            clientName = comms.consumeResult();
                            System.out.println(String.format(PLAYER___JOINED, clientName));
                            break;
                    }
                }
            }
        } catch (SocketException ex){
            System.out.println(String.format(PLAYER___HASLEFT, clientName));
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
        out.flush();
    }
    
}
