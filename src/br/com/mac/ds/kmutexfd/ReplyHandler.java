package br.com.mac.ds.kmutexfd;

public class ReplyHandler extends Thread {
	
	private Process process;
	private Message message;
	
	public ReplyHandler() {}

	public ReplyHandler(Process process, Message message) {
		super();
		this.process = process;
		this.message = message;
	}
	
	@Override
	public void run() {
		try {
			
			Process pOrigin = KMain.network[message.getProcessId()];
			System.out.println(process.getProcessId()+" receive REPLY from "+pOrigin.getProcessId());
			if(!process.getCrashed().contains(pOrigin)) {
				int[] rc = process.getReplyCount();
				
				rc[pOrigin.getProcessId()] = rc[pOrigin.getProcessId()] - 1;//(Integer.parseInt(message.getPayload()));
				
				if(process.getProcessState()==Process.REQUESTING && rc[pOrigin.getProcessId()]==0) { //FAIL
					process.setPermCount(process.getPermCount()+1);
					System.out.println(process.getProcessId()+" permCount: "+process.getPermCount());
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}
		
		
	}

}
