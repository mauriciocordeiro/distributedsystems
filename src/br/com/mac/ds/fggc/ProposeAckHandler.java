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
			
			if(process.hasPendingPropose())
				return;
			
			//if(process.getType()==Process.PROPOSER) {
				String payload = process.getBalnum()+","+process.getValue();
				for (Process learner : process.getLearners()) {
					Message msg = new Message(Message.LEARN, process.getPid(), process.getPort(), null, payload);
					msg.setDestinationPort(learner.getPort());
					new SenderUDP(process, msg).start();
//					System.out.println(process.getPid()+" sends LEARN to "+learner.getPid());
				}
			//}
			
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}
		
	}

}
