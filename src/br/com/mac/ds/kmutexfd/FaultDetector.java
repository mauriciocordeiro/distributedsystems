package br.com.mac.ds.kmutexfd;

import java.util.ArrayList;

public class FaultDetector extends Thread {
	
	private static final long serialVersionUID = 1L;
	
	private Process process;
	private ArrayList<Process> faultyS;
	private ArrayList<Process> faultyT;
	
	public FaultDetector(Process process) {
		super();
		this.process = process;
		this.faultyS = new ArrayList<>();
		this.faultyT = new ArrayList<>();
		
		for(Process p : KMain.network) {
			faultyS.add(p);
		}
	}
	
	public FaultDetector() {}
	
	public ArrayList<Process> getFaultyS() {
		return faultyS;
	}

	public void setFaultyS(ArrayList<Process> faultyS) {
		this.faultyS = faultyS;
	}

	public boolean isFaulty(Process p) {
		return faultyT.contains(p);
	}
	
	public void add(Process p) {
		faultyT.add(p);
	}
	
	public void remove(Process p) {
		faultyT.remove(p);
	}
	
	@Override
	public void run() {
		try {
			
			while(true) {
				//Thread.sleep(1000);
				
				for(Process p : KMain.network) {
					if(p.getState()==Thread.State.WAITING)
						add(p);
					
					if(process!=p && process.getTrusted().contains(p) && isFaulty(p)) {
						process.removeTrusted(p);
						
						sendCrash(p);
					}
				}
			}
			
			
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}
	}
	
	private void sendCrash(Process crashed) {
		
		Message crash = new Message(Message.CRASH, process.getProcessId(), process.getH(), process.getPort(), 0, Integer.toString(crashed.getProcessId()));
		System.out.println(process.getProcessId()+" sending "+crashed.getProcessId()+" CRASH...");
		for(Process pDestination : KMain.network) {
			if(!process.getCrashed().contains(pDestination) && process!=pDestination) {
				crash.setDestinationPort(pDestination.getPort());
				new SenderUDP(process, crash).start();
			}
		}
		
	}

}
