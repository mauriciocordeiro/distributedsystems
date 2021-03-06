package br.com.mac.ds.fggc;

public class NewBallotHandler extends Thread {
	
	private Process process;
	private Message message;
	
	public NewBallotHandler() {}

	public NewBallotHandler(Process process, Message message) {
		super();
		this.process = process;
		this.message = message;
	}
	
	@Override
	public void run() {
		try {
			Integer balnum = Integer.parseInt(message.getPayload());
			
			System.out.println(process.getBalnum()+"-> "+process.getPid()+" receive ballot "+balnum+" from "+message.getProcessId());
			
			if(process.getBalnum()>balnum) {
				System.err.println(process.getBalnum()+"-> "+process.getPid()+" refuse ballot "+balnum+" from "+message.getProcessId());
				return;
			}
			else if(!mustAccept()) {
				return;
			}
			
			Message msg = new Message(Message.NEW_BALLOT_ACK, process.getPid(), 
					process.getPort(), message.getOriginPort(), Integer.toString(process.getBalnum()));

			process.setBalnum(balnum);
			
			new SenderUDP(process, msg).start();
			
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}
		
	}

	private boolean mustAccept() {
		for (int pid : FGGCMain.idFault) {
			if(pid==process.getPid())
				return false;
		}
		return true;
	}

}
