package com.company;
import java.net.*;

public class EchoServer {
    public static void main(String[] args) throws Exception {
        try{
            ServerSocket server=new ServerSocket(8888);
            int counter=0;
            System.out.println("Server Started ....");
            while(true){
                counter++;
                Socket serverClient=server.accept();
                System.out.println(" >> " + "Client No:" + counter + " started!");
                Main sct = new Main(serverClient,counter);
                sct.start();
            }
        }catch(Exception e){
            System.out.println(e);
        }
    }
}
