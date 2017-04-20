package br.com.mac.ds.kmutexfd;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Random;
import java.util.Stack;

import br.com.mac.ds.node.Node;

public class Process extends Node {
	
	private int id;
	private int state;
	private int last;
	private int H;
	private int n;
	private ArrayList<Process> trusted;
	private ArrayList<Process> crashed;
	
	private int permCount;
	private int[] replyCount;
	private int[] deferCount;
	
	private FaultDetector fd;
	
	public int ackCount = 0;
	
	public static final int NOT_REQUESTING = 0;
	public static final int REQUESTING = 1;
	public static final int CS = 2;
	
	public Process() {
		super();
	}

	public Process(int id) {
		super();
		this.id = id;
		
		trusted = new ArrayList<>();
		crashed = new ArrayList<>();		
		state = NOT_REQUESTING;
		H = 0;
		last = 0;
		n = KMain.TOTAL_PROCESS;
		
		permCount = 0;
		replyCount = new int[KMain.TOTAL_PROCESS];
		deferCount = new int[KMain.TOTAL_PROCESS];
		for(int i=0; i<KMain.TOTAL_PROCESS; i++) {
			replyCount[i] = 0;
			deferCount[i] = 0;
		}
		
		fd = new FaultDetector(this);
		
		ackCount = KMain.TOTAL_PROCESS;
	}

	public int getProcessId() {
		return id;
	}

	public void setProcessId(int id) {
		this.id = id;
	}

	public int getProcessState() {
		return state;
	}

	public void setProcessState(int state) {
		this.state = state;
	}

	public int getLast() {
		return last;
	}

	public void setLast(int last) {
		this.last = last;
	}
	
	public int getH() {
		return H;
	}

	public void setH(int h) {
		H = h;
	}

	public ArrayList<Process> getTrusted() {
		return trusted;
	}

	public void setTrusted(ArrayList<Process> trusted) {
		this.trusted = trusted;
	}

	public ArrayList<Process> getCrashed() {
		return crashed;
	}

	public void setCrashed(ArrayList<Process> crashed) {
		this.crashed = crashed;
	}

	public int getPermCount() {
		return permCount;
	}

	public void setPermCount(int permCount) {
		this.permCount = permCount;
	}

	public int[] getReplyCount() {
		return replyCount;
	}

	public void setReplyCount(int[] replyCount) {
		this.replyCount = replyCount;
	}

	public int[] getDeferCount() {
		return deferCount;
	}

	public void setDeferCount(int[] deferCount) {
		this.deferCount = deferCount;
	}

	public int getN() {
		return n;
	}

	public void setN(int n) {
		this.n = n;
	}
	
	public FaultDetector getFd() {
		return fd;
	}

	public void setFd(FaultDetector fd) {
		this.fd = fd;
	}

	@Override
	public void run() {
		System.out.println(getProcessId()+" starts...");
		try {
			fd.start();
			new ReceiverUDP(this).start();
			
			sendInit();
			
			while(ackCount>0); 
			
			System.out.println(id+" is no longer waiting ACK");
			
			Thread.sleep((new Random().nextInt(5)+1)*1000);
			

			for(int idFault : KMain.idFault) {
				if(getProcessId()==idFault) {
//					while(true);
					this.wait();
				}
			}
			
			
			requestResource();
			
		} 
		catch (IllegalMonitorStateException imse) {
			System.err.println(id +" crashed.");
		}
		catch (Exception e) {
			e.printStackTrace(System.err);
		}
		
	}
	
	/**
	 * Process wishes to enter Critic Session
	 */
	public void requestResource() {
		state = REQUESTING;
		last = H + 1;
		permCount = 0;
		
		System.out.println(id+" requesting...");
		
		Message msg = new Message(Message.REQUEST, getProcessId(), getLast(), getPort(), 0, Integer.toString(last));
		
		for (Process pDestination : KMain.network) {
			if(!getCrashed().contains(pDestination) && this!=pDestination) {
				msg.setDestinationPort(pDestination.getPort());
				new SenderUDP(this, msg).start();
				getReplyCount()[pDestination.getProcessId()]+=1;
			}
		}
		
//		while(permCount<(n-KMain.TOTAL_RESOURCE)) { //FAIL
//			//System.out.println(id+" waiting perms...");
//		}
		
		while(getTotalPerms()<(n-KMain.TOTAL_RESOURCE)) {
			System.out.println(id+" waiting permission...");
		}
		
		criticSession();
		
	}
	
	public void releaseResource() {
		state = NOT_REQUESTING;
		
		Message msg = new Message(Message.REPLY, getProcessId(), getLast(), getPort(), 0, "");
		for (Process pDestination : KMain.network) {
			if(!getCrashed().contains(pDestination) && this!=pDestination && getDeferCount()[pDestination.getProcessId()]!=0) {
				msg.setDestinationPort(pDestination.getPort());
				msg.setPayload(Integer.toString(getDeferCount()[pDestination.getProcessId()]));
				new SenderUDP(this, msg).start();
				getDeferCount()[pDestination.getProcessId()] = 0;
			}
		}		
	}
	
	private void sendInit() {
		
		System.out.println(getProcessId()+" sending INIT...");
		
		Message init = new Message(Message.INIT, getProcessId(), getLast(), getPort(), 0, "");
		for(Process p : KMain.network) {
			init.setDestinationPort(p.getPort());
			new SenderUDP(this, init).start();
			try{Thread.sleep((id+1)*100);}catch(Exception e){}
		}
		
	}
	
	public void criticSession() {
		state = CS;
		try {
			long startTime = new GregorianCalendar().getTimeInMillis();
			long now = 0;
			do {
				System.out.println("\t\t"+id+". critic session!");
				now = new GregorianCalendar().getTimeInMillis();
			} while((now-startTime)<(new Random().nextInt(5)*1000));
			
			releaseResource();
		}
		catch(Exception e) {
			System.err.println("Process.criticSession:");
			e.printStackTrace(System.err);
		}
	}
	
	public void addTrusted(Process p) {
		System.out.println(p.getProcessId()+" add in trusted("+id+")");
		trusted.add(p);
	}
	
	public void addCrashed(Process p) {
		crashed.add(p);
	}
	
	public void removeTrusted(Process p) {
		trusted.remove(p);
	}
	
	public void removeCrashed(Process p) {
		crashed.remove(p);
	}
	
	private int getTotalPerms() {
		int p = 0;
		
		for(int i : replyCount) {
			if(i!=0)
				p+=i;
		}
		
		return p;
	}

}
