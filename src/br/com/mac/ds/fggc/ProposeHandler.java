package br.com.mac.ds.fggc;

public class ProposeHandler extends Thread {
	
	private Process process;
	private Message message;
	
	public ProposeHandler() {}

	public ProposeHandler(Process process, Message message) {
		super();
		this.process = process;
		this.message = message;
	}
	
	@Override
	public void run() {
		try {
			
			String[] payload = message.getPayload().split(",");
			Integer balnum = Integer.parseInt(payload[0]);
			Integer value = Integer.parseInt(payload[1]);
			
			if(process.getBalnum()>balnum)
				return;
			
			process.setValue(value);
			
			Message msg = new Message(Message.PROPOSE_ACK, process.getPid(), process.getPort(), message.getOriginPort(), null);
			new SenderUDP(process, msg).start();
			
			System.out.println("\t"+process.getBalnum()+"-> "+process.getPid()+" accepts value "+value+" from ballot "+balnum);
			
			
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}
		
	}

}
