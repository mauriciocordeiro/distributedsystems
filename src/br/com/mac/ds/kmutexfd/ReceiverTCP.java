package br.com.mac.ds.kmutexfd;

import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ReceiverTCP extends Thread {
	
	private Process process;
	
	public ReceiverTCP() {}
	
	public ReceiverTCP(Process process) {
		this.process = process;
	}

	@Override
	public void run() {
		try {
			
			ServerSocket listener = new ServerSocket(process.getPort());
			
			while(true) {
				Socket s = listener.accept();
				ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
				Message msg = (Message)ois.readObject();
				
				switch(msg.getType()) {
					case Message.REQUEST:
						new RequestHandler(process, msg).start();
						break;
					case Message.REPLY:
						new ReplyHandler(process, msg).start();
						break;
					case Message.INIT:
						new InitHandler(process, msg).start();
						break;
					case Message.ACK:
						new AckHandler(process, msg).start();
						break;
					case Message.CRASH:
						new CrashHandler(process, msg).start();
						break;
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}
	}
}
