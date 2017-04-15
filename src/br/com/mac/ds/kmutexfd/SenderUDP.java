package br.com.mac.ds.kmutexfd;

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
			byte[] buffer = message.getBytes();
			
//			System.out.println(process.getProcessId()+" sending "+Message.messageTypes[message.getType()]);
			
			DatagramPacket pckt = new DatagramPacket(buffer, buffer.length, InetAddress.getByName("127.0.0.1"), message.getDestinationPort());
			DatagramSocket socket = new DatagramSocket();
			socket.send(pckt);
			
		}
		catch(Exception e) {
			e.printStackTrace(System.err);
		}
		
	}

}
