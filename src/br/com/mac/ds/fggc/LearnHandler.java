package br.com.mac.ds.fggc;

public class LearnHandler extends Thread {
	
	private Process process;
	private Message message;
	
	public LearnHandler() {}

	public LearnHandler(Process process, Message message) {
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
			
			System.err.println("\t"+process.getBalnum()+"-> "+process.getPid()+" learns "+value+" from "+message.getProcessId());
			
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}
		
	}

}
