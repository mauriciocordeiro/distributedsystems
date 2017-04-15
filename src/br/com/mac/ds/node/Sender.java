package br.com.mac.ds.node;

import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

import javax.swing.plaf.ListUI;

import br.com.mac.ds.main.Main;
import br.org.mac.midgard.util.List;

public class Sender extends Thread {
	
	private Node node;
	private int sendPort;
	
	public Sender(Node node) {
		this.node = node;
		
		this.sendPort = Main.PORT_PREFIX;
	}

	@Override
	public void run() {
		try {
			//this.sleep(100);
			
			for(int i=0; i<Main.NODE_AMOUNT; i++) {
				if(node.getPort()!=(sendPort+i)) {
					Socket s = new Socket(node.getIp(), sendPort+i);
					ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
					oos.writeObject(node.getData());
					oos.flush();
					s.close();	
				}
			}
						
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
