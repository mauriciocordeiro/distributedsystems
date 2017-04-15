package br.com.mac.ds.kmutexfd;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

@Deprecated
public class ReceiverMulticast extends Thread {

	private Process process;
	
	public ReceiverMulticast() { }
	
	public ReceiverMulticast(Process process) {
		this.process = process;
	}
	
	@Override
	public void run() {
		MulticastSocket socketMulticast = null;
		try {
			socketMulticast = new MulticastSocket(KMain.DEFAULT_PORT);
			socketMulticast.joinGroup(InetAddress.getByName(KMain.MC_GROUP_IP));
			
			while(true) {
				byte buf[] = new byte[1024];
				DatagramPacket pack = new DatagramPacket(buf, buf.length);
				socketMulticast.receive(pack);
				
				Message msg = new Message().setBytes(pack.getData());
				switch(msg.getType()) {
					case Message.REQUEST:
						new RequestHandler(process, msg).start();
						break;
					case Message.INIT:
						new InitHandler(process, msg).start();
						break;
					case Message.CRASH:
						new CrashHandler(process, msg).start();
						break;
				}
			
			}
		}
		catch(Exception e) {
			e.printStackTrace(System.err);
		}
		finally {
			try {
				socketMulticast.leaveGroup(InetAddress.getByName(KMain.MC_GROUP_IP));
				socketMulticast.close();
			} catch(Exception e) {}
		}
	}
}
