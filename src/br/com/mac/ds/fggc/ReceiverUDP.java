package br.com.mac.ds.fggc;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class ReceiverUDP extends Thread {
	
private Process process;
	
	public ReceiverUDP() { }
	
	public ReceiverUDP(Process process) {
		this.process = process;
	}
	
	@Override
	public void run() {
		DatagramSocket socket = null;
		try {
			socket = new DatagramSocket(process.getPort());
			
			while(true) {
				byte buffer[] = new byte[1024];
				DatagramPacket pack = new DatagramPacket(buffer, buffer.length);
				socket.receive(pack);
				
				new DatagramHandler(process, pack).start();
			}
		}
		catch(Exception e) {
			e.printStackTrace(System.err);
		}
		finally {
			socket.close();
		}
	}

}
