package br.com.mac.ds.fggc;

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
				case Message.NEW_BALLOT:
					new NewBallotHandler(process, msg).start();
					break;
				case Message.NEW_BALLOT_ACK:
					new NewBallotAckHandler(process, msg).start();
					break;
				case Message.PROPOSE:
					new ProposeHandler(process, msg).start();
					break;
				case Message.PROPOSE_ACK:
					new ProposeAckHandler(process, msg).start();
					break;
				case Message.LEARN:
					new LearnHandler(process, msg).start();
					break;
			}
			
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}
	}
}
