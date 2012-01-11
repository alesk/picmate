package org.picmate;

import java.io.IOException;
import java.net.*;
import android.util.Log;

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
			  Log.e("ERROR", e.getMessage());
		  }	      	     
	      
	   }
	   
	   protected void finalize() {
		   this.clientSocket.close();		   
	   }
	   
	    
	   public void send(String text) {
		   try {
		   	  byte[] bytes = text.getBytes();
	          DatagramPacket sendPacket = new DatagramPacket(bytes, bytes.length, this.ipAddress, this.port);		   	
	          this.clientSocket.send(sendPacket);
	          //Log.e("ERROR", text);
		   } catch (IOException e) {
			   Log.e("ERROR", e.getMessage());
			   // eat that exception
		   }
	   }	   



}
