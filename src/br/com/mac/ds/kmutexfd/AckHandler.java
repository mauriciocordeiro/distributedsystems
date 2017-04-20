package br.com.mac.ds.kmutexfd;

public class AckHandler extends Thread {
	
	private Process process;
	private Message message;
	
	public AckHandler() {}

	public AckHandler(Process process, Message message) {
		super();
		this.process = process;
		this.message = message;
	}
	
	@Override
	public void run() {
		
		process.ackCount--;
		process.getFd().getFaultyS().remove(KMain.network[Integer.parseInt(message.getPayload())]);
		
	}
}
