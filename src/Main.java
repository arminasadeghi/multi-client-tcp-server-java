package com.company;

import java.io.*;
import java.net.Socket;
import java.util.zip.Adler32;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

class Main extends Thread {
    Socket serverClient;
    int clientNo;
    int squre;
    static Checksum checksum = new CRC32();
    Main(Socket inSocket,int counter){
        serverClient = inSocket;
        clientNo=counter;
    }

    public void run(){
        try{
            DataInputStream inStream = new DataInputStream(serverClient.getInputStream());
            DataOutputStream outStream = new DataOutputStream(serverClient.getOutputStream());
            String clientMessage="", serverMessage="";
            clientMessage=inStream.readUTF();
            while(clientMessage.charAt(0)!='1') {
                System.out.println("From Client-" + clientNo + ": command is :" + clientMessage);
               if (clientMessage.charAt(2)=='0') {
                    System.out.println("client " + clientNo + " wants a file");
                   while (clientMessage.charAt(1)!='0') {
                       File myFile = new File(clientMessage.substring(3));
                       serverMessage = "From Server to Client-" + clientNo + " your file name is: " + clientMessage.substring(3);
                       BufferedInputStream bis = new BufferedInputStream(new FileInputStream(myFile));
                       byte[] mybytearray = new byte[(int) myFile.length()];
                       while (bis.read(mybytearray, 0, mybytearray.length) != -1) {
                           outStream.write(mybytearray, 0, mybytearray.length);
                           checksum.update(mybytearray, 0, mybytearray.length);
                           int checksumValue = (int) checksum.getValue();
                           outStream.flush();
                       }
                       System.out.println("did you recieve ?");
                       clientMessage = inStream.readUTF();
                   }
                   outStream.writeUTF(serverMessage);
                   System.out.println(clientMessage);
                   System.out.println(serverMessage);
                   outStream.flush();
                   clientMessage = inStream.readUTF();
                } else if (clientMessage.charAt(2)=='1'){
                System.out.println("client " + clientNo + "wants a square");
                //clientMessage = inStream.readUTF();
                squre = Integer.parseInt(clientMessage.substring(3)) * Integer.parseInt(clientMessage.substring(3));
                serverMessage = "From Server to Client-" + clientNo + " Square of " + clientMessage.substring(3) + " is " + squre;
                outStream.writeUTF(serverMessage);
                System.out.println(serverMessage);
                outStream.flush();
                clientMessage = inStream.readUTF();
            }
            }
            serverMessage = "bye bye";
            outStream.writeUTF(serverMessage);
            outStream.flush();
            inStream.close();
            outStream.close();
            serverClient.close();
        }catch(Exception ex){
            System.out.println(ex);
        }finally{
            System.out.println("Client -" + clientNo + " exit!! ");
        }
    }
}

