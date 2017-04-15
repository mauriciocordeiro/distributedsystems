package br.com.mac.ds.kmutexfd;

public class CrashHandler extends Thread {

	private Process process;
	private Message message;
	
	public CrashHandler() {}

	public CrashHandler(Process process, Message message) {
		super();
		this.process = process;
		this.message = message;
	}
	
	@Override
	public void run() {
		
		try {
			Process pOrigin = KMain.network[message.getProcessId()];
			if(!process.getCrashed().contains(pOrigin)) {
				process.addCrashed(KMain.network[Integer.parseInt(message.getPayload())]);
				if(process.getProcessState()==Process.REQUESTING && process.getReplyCount()[pOrigin.getProcessId()]==0) {
					process.setPermCount(process.getPermCount()-1);
				}
				process.setN(process.getN()-1);
			}
			
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}
		
	}
	
}
