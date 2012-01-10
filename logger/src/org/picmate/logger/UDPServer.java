package org.picmate.logger;
import java.io.*;
import java.net.*;

class UDPServer
{
    private static final String OUT_FILE = "out.txt";
    private static final int PORT = 9876;
    public static final DatagramSocket serverSocket = null;

    public static void serveForever() throws Exception
    {        
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(OUT_FILE));
        DatagramSocket serverSocket = new DatagramSocket(PORT);
        byte[] receiveData = new byte[1024];

        try {
            DatagramPacket receivePacket = 
                new DatagramPacket(receiveData, 1024);

            while(true) {
                serverSocket.receive(receivePacket);
                bufferedWriter.write(new String(receivePacket.getData(), 0, receivePacket.getLength()));
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
        } catch(Exception e) {
           e.printStackTrace(); 
            
        } finally {
            if (bufferedWriter != null) {
                bufferedWriter.flush();
                bufferedWriter.close();
            }

            if (serverSocket != null) {
                serverSocket.close();
            }
        }

    }

    public static void main(String args[]) throws Exception {
        serveForever();
    }
   
}
