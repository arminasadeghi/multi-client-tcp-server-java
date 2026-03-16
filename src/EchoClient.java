package com.company;
import java.net.*;
import java.io.*;
import java.util.zip.Adler32;
import java.util.zip.CRC32;
import java.util.zip.Checksum;
public class EchoClient {
    static Checksum checksum = new CRC32();
 public static void main(String[] args) throws Exception {
  try{
   Socket socket=new Socket("127.0.0.1",8888);
   DataInputStream inStream=new DataInputStream(socket.getInputStream());
   DataOutputStream outStream=new DataOutputStream(socket.getOutputStream());
   BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
   String clientMessage="",serverMessage="";
   clientMessage = "0";
   while(clientMessage.charAt(0)!='1') {
    System.out.println("Enter command :");
    clientMessage = br.readLine();
    if(clientMessage.charAt(0)=='1'){
        break;
    }
    outStream.writeUTF(clientMessage);
    outStream.flush();
   if (clientMessage.charAt(2)=='0') {
       System.out.println("file");
       while (clientMessage.charAt(1)!='0'){
       byte[] mybytearray = new byte[1024];
       FileOutputStream fos = new FileOutputStream("copy_of_"+clientMessage.substring(3));
       BufferedOutputStream bos = new BufferedOutputStream(fos);
       int bytesRead = inStream.read(mybytearray, 0, mybytearray.length);
       bos.write(mybytearray, 0, bytesRead);
       checksum.update(mybytearray, 0, mybytearray.length);
       int checksumValue = (int) checksum.getValue();
       //System.out.println(checksumValue);
       bos.close();
       fos.close();
       int recievedchecksum=-1;
       if(recievedchecksum==checksumValue){
           clientMessage=clientMessage.substring(0,1)+"1"+clientMessage.substring(2);
           System.out.println(clientMessage);
           outStream.writeUTF(clientMessage);
           outStream.flush();
       }
       else{
           clientMessage=clientMessage.substring(0,1)+"0"+clientMessage.substring(2);
           outStream.writeUTF(clientMessage);
           outStream.flush();
           System.out.println(clientMessage);
       }
       }
     serverMessage = inStream.readUTF();
     System.out.println(serverMessage);
    } else if (clientMessage.charAt(2)=='1'){
       System.out.println("square");
    serverMessage = inStream.readUTF();
    System.out.println(serverMessage);
   }
   }
   inStream.close();
   outStream.close();
   socket.close();
   System.out.println("ends");
  }catch(Exception e){
   System.out.println(e);
  }
  return;
 }
}
