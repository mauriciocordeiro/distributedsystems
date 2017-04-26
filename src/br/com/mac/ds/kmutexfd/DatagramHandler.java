package br.com.mac.ds.kmutexfd;

import java.net.DatagramPacket;

public class DatagramHandler extends Thread {
	
	private Process process;
	private DatagramPacket dtgm;
	
	public DatagramHandler() { }
	
	public DatagramHandler(Process process, DatagramPacket dtgm) {
		this.process = process;
		this.dtgm = dtgm;
	}
	
	@Override
	public void run() {
		try {
			
			Message msg = new Message().setBytes(dtgm.getData());
			
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
				case Message.KNOCK_KNOCK:
					new KnockKnockHandler(process, msg).start();
					break;
				case Message.WHO_IS_THERE:
					new WhoIsThereHandler(process, msg).start();
					break;
			}
			
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}
	}
}
