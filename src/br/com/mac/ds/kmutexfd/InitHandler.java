package br.com.mac.ds.kmutexfd;

public class InitHandler extends Thread {
	
	private Process process;
	private Message message;
	
	public InitHandler() {}

	public InitHandler(Process process, Message message) {
		super();
		this.process = process;
		this.message = message;
	}
	
	@Override
	public void run() {
		try {
			Process pOrigin = KMain.network[message.getProcessId()];
			
			while(process.getFd().isFaulty(pOrigin));
			if(process.getFd().isFaulty(pOrigin))
				process.getFd().remove(pOrigin);
			process.addTrusted(pOrigin);
			
			Message ack = new Message(Message.ACK, process.getProcessId(), process.getLast(), process.getPort(), pOrigin.getPort(), process.getProcessId()+"");
			new SenderUDP(process, ack).start();
			
		}
		catch (Exception e) {
			e.printStackTrace(System.err);
		}
	}

}
