package br.com.mac.ds.fggc;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class SenderUDP extends Thread {
	
	private Process process;
	private Message message;
	
	public SenderUDP() { }
	
	public SenderUDP(Process process, Message message) {
		this.process = process;
		this.message = message;
	}
	
	@Override
	public void run() {
		try {
			//Thread.sleep(1000);
			byte[] buffer = message.getBytes();
			
			InetAddress ip = null;
			if(process!=null)
				ip = InetAddress.getByName(process.getIp());
			
			
			DatagramPacket pckt = new DatagramPacket(buffer, buffer.length, ip, message.getDestinationPort());
			DatagramSocket socket = new DatagramSocket();
			socket.send(pckt);
			
//			System.out.println(process.getPid()+" sending "+Message.messageTypes[message.getType()]+" to "+message.getDestinationPort());
			
		}
		catch(Exception e) {
			e.printStackTrace(System.err);
		}
		
	}

}
