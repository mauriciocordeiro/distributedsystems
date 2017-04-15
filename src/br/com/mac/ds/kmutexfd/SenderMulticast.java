package br.com.mac.ds.kmutexfd;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

@Deprecated
public class SenderMulticast extends Thread {
	
	private Process process;
	private Message message;
	
	public SenderMulticast() { }
	
	public SenderMulticast(Process process, Message message) {
		this.process = process;
		this.message = message;
	}
	
	@Override
	public void run() {
		try {
			byte[] buffer = message.getBytes();
			DatagramPacket pckt = new DatagramPacket(buffer, buffer.length, InetAddress.getByName(KMain.MC_GROUP_IP), KMain.DEFAULT_PORT);
			
			MulticastSocket socket = new MulticastSocket();
			socket.setTimeToLive(KMain.TTL);
			socket.send(pckt);
			
			socket.close();
			
		}
		catch(Exception e) {
			e.printStackTrace(System.err);
		}
		
	}

}
