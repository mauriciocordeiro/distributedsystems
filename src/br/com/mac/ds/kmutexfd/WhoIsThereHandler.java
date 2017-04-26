package br.com.mac.ds.kmutexfd;

public class WhoIsThereHandler extends Thread {
	
	private Process process;
	private Message message;
	
	public WhoIsThereHandler() {}

	public WhoIsThereHandler(Process process, Message message) {
		super();
		this.process = process;
		this.message = message;
	}
	
	@Override
	public void run() {
		
		try {
			
			process.getFd().getKnockCount()[message.getProcessId()] = 0;
			
			
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}
		
	}

}
