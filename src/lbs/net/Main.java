/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lbs.net;

import static lbs.net.StandardMessage.*;
import static lbs.net.CommsProtocol.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author lenovo z40
 */
public class Main {

    private static String nickname;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println("alamat ip anda: "+CommsProtocol.getAddress());
        try(BufferedReader stdIn = new BufferedReader(
            new InputStreamReader(System.in))){
            System.out.print("nickname: ");
            nickname = stdIn.readLine();
            System.out.println("menu:");
            System.out.println("1. buat server");
            System.out.println("2. join server");
            int choice = Integer.parseInt(stdIn.readLine());
            switch(choice){
                case 1:
                    serverRoutine();
                    break;
                case 2:
                    clientRoutine();
                    break;
            }
        } catch(IOException e){
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    
    public static void serverRoutine(){
        String serverName;
        int numOfPlayer;
        int numOfConnections;
        int maxConnections;
        ServerThread[] thread;
        
        try(BufferedReader stdIn = new BufferedReader(
            new InputStreamReader(System.in))){
            System.out.print("nama server: ");
            serverName = stdIn.readLine();
            System.out.print("jumlah pemain (2-4): ");
            numOfPlayer = Integer.parseInt(stdIn.readLine());
            if(numOfPlayer < 2 || numOfPlayer > 4)
                System.exit(1);
            maxConnections = numOfPlayer - 1;
            thread = new ServerThread[maxConnections];
            numOfConnections = 0;
            try(ServerSocket sock = new ServerSocket(PORT)){
                while(true){
                    while(numOfConnections < maxConnections){
                        try{
                            System.out.println(String.format(WAITING__OF__, numOfConnections, maxConnections));
                            int index = numOfConnections;
                            thread[index] = new ServerThread(serverName,  sock.accept());
                            thread[index].start();
                            numOfConnections++;

                            //check if a player has disconnected
                            int i = 0;
                            while(i<maxConnections){
                                boolean connected = (thread[i]!=null && thread[i].isAlive());
                                if(!connected && numOfConnections > i){
                                    for(int j=i;j<maxConnections-1;j++){
                                        thread[j]=thread[j+1];
                                    }
                                    thread[maxConnections-1]=null;
                                    --numOfConnections;
                                }
                                i++;
                            }
                        } catch (IOException e){
                            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, e);
                        }
                    }
                    System.out.println("Ready? y/N");
                    String temp = stdIn.readLine();
                    if("Y".equals(temp) || "y".equals(temp))
                        break;
                }
            }
        } catch(IOException e){
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, e);
        }
        
    }
    
    public static void clientRoutine(){
        String hostname;
        ClientThread thread;
        
        try(BufferedReader stdIn = new BufferedReader(
            new InputStreamReader(System.in))){
            System.out.print("hostname/alamat ip server: ");
            hostname = stdIn.readLine();
            thread = new ClientThread(nickname, hostname);
            thread.start();
        } catch (IOException e){
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, e);
        }
    }
}
