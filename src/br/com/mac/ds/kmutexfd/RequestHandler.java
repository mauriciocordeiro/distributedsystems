package br.com.mac.ds.kmutexfd;

public class RequestHandler extends Thread {
	
	private Process process;
	private Message message;
	
	public RequestHandler() {}

	public RequestHandler(Process process, Message message) {
		super();
		this.process = process;
		this.message = message;
	}
	
	@Override
	public void run() {
		try {
			Process pOrigin = KMain.network[message.getProcessId()];
			process.setH(KMain.max(process.getH(), pOrigin.getH()));
			if(!process.getCrashed().contains(pOrigin)) {
				if((process.getProcessState()==Process.CS) || 
					(process.getProcessState()==Process.REQUESTING && process.getLast()<pOrigin.getLast())) {
					process.getDeferCount()[pOrigin.getProcessId()]+=1;
				}
				else {
					Message reply = new Message(Message.REPLY, process.getProcessId(), process.getLast(), process.getPort(), pOrigin.getPort(), Integer.toString(1));
					Thread.sleep(process.getProcessId()*100);
					new SenderUDP(process, reply).start();
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
	}

}
