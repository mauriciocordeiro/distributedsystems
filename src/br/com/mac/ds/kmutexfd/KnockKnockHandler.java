package br.com.mac.ds.kmutexfd;

public class KnockKnockHandler extends Thread {
	
	private Process process;
	private Message message;
	
	public KnockKnockHandler() {}

	public KnockKnockHandler(Process process, Message message) {
		super();
		this.process = process;
		this.message = message;
	}
	
	@Override
	public void run() {
		
		try {
			
			boolean mustReply = true;
			for (Integer idFaulty : KMain.idFault) {
				if(process.getProcessId()==idFaulty) {
					mustReply = false;
					break;
				}
			}
			
			if(mustReply) {
				Message whoIsThere = new Message(Message.WHO_IS_THERE, process.getProcessId(), process.getLast(), process.getPort(), message.getOriginPort(), "");
				new SenderUDP(process, whoIsThere).start();
			}
			else {
				process.wait();
			}
			
			
		} catch (Exception e) {
			//e.printStackTrace(System.err);
		}
		
	}

}
