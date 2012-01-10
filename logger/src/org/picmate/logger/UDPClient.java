package org.picmate.logger;
import java.net.*;
import java.util.Random;

class UDPClient
{
   static final String HOST = "localhost";
   static final int PORT = 9876;
   static final int MAX_LINES = 1000;

   public static void main(String args[]) throws Exception
   {
      Random generator = new Random(12415352);      
      InetAddress IPAddress = InetAddress.getByName(HOST);
      DatagramSocket clientSocket = new DatagramSocket();

      for(int i = 0; i < MAX_LINES; i += 1) {
          float x = generator.nextInt()/10000f;
          float y = generator.nextInt()/10000f;
          float z = generator.nextInt()/10000f;

          byte[] sendData = String.format("%f, %f, %f", x, y, z).getBytes();
          DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, PORT);
          clientSocket.send(sendPacket);
      }

      clientSocket.close();
   }


}
