package br.com.mac.ds.kmutexfd;

import java.util.ArrayList;

public class FaultDetector extends Thread {
	
	private static final long serialVersionUID = 1L;
	
	private Process process;
	private ArrayList<Process> faultyS;
	private ArrayList<Process> faultyT;
	
	private int[] knockCount;
	
	public FaultDetector(Process process) {
		super();
		this.process = process;
		this.faultyS = new ArrayList<>();
		this.faultyT = new ArrayList<>();
		
		for(Process p : KMain.network) {
			faultyS.add(p);
		}
		
		knockCount = new int[KMain.TOTAL_PROCESS];
	}
	
	public FaultDetector() {}
	
	public ArrayList<Process> getFaultyS() {
		return faultyS;
	}

	public void setFaultyS(ArrayList<Process> faultyS) {
		this.faultyS = faultyS;
	}

	public int[] getKnockCount() {
		return knockCount;
	}

	public void setKnockCount(int[] knockCount) {
		this.knockCount = knockCount;
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
				Thread.sleep(500);
				
				for(int i=0; i<knockCount.length; i++) {
					if(knockCount[i]==1) {
						Process p = KMain.network[i];
						add(p);
						
						if(process.getTrusted().contains(p) && isFaulty(p)) {
							process.removeTrusted(p);
							
							sendCrash(p);
						}
						
					}
				}
				
				for(Process p : KMain.network) {
					if(process!=p && !process.getCrashed().contains(p)) {
						Message knock = new Message(Message.KNOCK_KNOCK, process.getProcessId(), process.getLast(), process.getPort(), p.getPort(), "");
						new SenderUDP(process, knock).start();
						
						knockCount[p.getProcessId()] = 1;
					}
				}
			}
			
			
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}
	}
	
	private void sendCrash(Process crashed) {
		
		Message crash = new Message(Message.CRASH, process.getProcessId(), process.getH(), process.getPort(), 0, Integer.toString(crashed.getProcessId()));
		System.err.println(process.getProcessId()+" sending "+crashed.getProcessId()+" CRASH...");
		for(Process pDestination : KMain.network) {
			if(!process.getCrashed().contains(pDestination) && process!=pDestination) {
				crash.setDestinationPort(pDestination.getPort());
				new SenderUDP(process, crash).start();
			}
		}
		
	}

}
