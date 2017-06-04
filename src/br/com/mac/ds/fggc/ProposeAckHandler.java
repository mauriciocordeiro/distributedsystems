package br.com.mac.ds.fggc;

public class ProposeAckHandler extends Thread {
	
	private Process process;
	private Message message;
	
	public ProposeAckHandler() {}

	public ProposeAckHandler(Process process, Message message) {
		super();
		this.process = process;
		this.message = message;
	}
	
	@Override
	public void run() {
		
		try {
			process.getProposeCount()[message.getProcessId()] = 0;
			
			System.out.println("\t"+message.getProcessId()+" accepts value "+process.getValue()+" from ballot "+process.getBalnum());
					
			
			if(process.hasPendingPropose())
				return;
			
			Message msg = new Message(Message.LEARN, process.getPid(), process.getPort(), null, Integer.toString(process.getValue()));
			for (Process learner : process.getLearners()) {
				msg.setDestinationPort(learner.getPort());
				new SenderUDP(process, msg).start();
			}
			
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}
		
	}

}
