package br.com.mac.ds.fggc;

public class NewBallotAckHandler extends Thread {
	
	private Process process;
	private Message message;
	
	public NewBallotAckHandler() {}

	public NewBallotAckHandler(Process process, Message message) {
		super();
		this.process = process;
		this.message = message;
	}
	
	@Override
	public void run() {
		
		try {
			
			Process p = FGGCMain.network[message.getProcessId()];
			
			//process.getLearners().remove(p);
			p.setType(Process.ACCEPTOR);
			process.getAcceptors().add(p);
			
			System.out.println(process.getBalnum()+"-> "+p.getPid()+" joins ballot "+process.getBalnum()+" from "+process.getPid());
			
//			process.propose();
			
		} catch (Exception e) { }
		
	}

}
