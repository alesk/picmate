package org.picmate.logger;
import java.io.IOException;
import java.net.*;

public class UDPSender {
	   private InetAddress ipAddress;
	   private int port;
	   private DatagramSocket clientSocket;

	   public UDPSender(String host, int port)
	   {
		  try { 
		  this.ipAddress = InetAddress.getByName(host);
		  this.port = port;
		  this.clientSocket = new DatagramSocket();
		  } catch(IOException e) {
			  // eat that exception;
		  }
		  	      	     
	      clientSocket.close();
	   }
	   
	   protected void finalize() {
		   this.clientSocket.close();		   
	   }
	   
	   public void send(String text) {
		   try {
		   	  byte[] bytes = text.getBytes();
	          DatagramPacket sendPacket = new DatagramPacket(bytes, bytes.length, this.ipAddress, this.port);
	          this.clientSocket.send(sendPacket);
		   } catch (IOException e) {
			   // eat that exception
		   }
	   }	   

}

