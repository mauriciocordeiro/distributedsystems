package br.com.mac.ds.node;

import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

import br.com.mac.ds.main.Main;

public class Receiver extends Thread {
	
	private Node node;
	
	public Receiver(Node node) {
		this.node = node;
	}
	
	@Override
	public void run() {
		try {
			ServerSocket listener = new ServerSocket(node.getPort());
			
			for(int i=0; i<Main.NODE_AMOUNT-1; i++) {
				
				Socket s = listener.accept();
				ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
				Object data = ois.readObject();
				node.toBuffer(Integer.toString(s.getPort()), data);
			}

			listener.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
