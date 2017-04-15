package br.com.mac.ds.kmutexfd;

import java.io.ObjectOutputStream;
import java.net.Socket;

public class SenderTCP extends Thread {
	
	private Process process;
	private Message message;
	
	public SenderTCP() { }
	
	public SenderTCP(Process process, Message message) {
		this.process = process;
		this.message = message;
	}
	
	@Override
	public void run() {
		try {
			
			Socket socket = new Socket(process.getIp(), message.getDestinationPort());
			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
			oos.writeObject(message);
			oos.flush();
			
		}
		catch(Exception e) {
			e.printStackTrace(System.err);
		}
		
	}

}
