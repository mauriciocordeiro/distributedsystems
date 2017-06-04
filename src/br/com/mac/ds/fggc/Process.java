package br.com.mac.ds.fggc;

import java.util.ArrayList;
import java.util.Random;

import br.com.mac.ds.node.Node;
import br.org.mac.midgard.util.List;

public class Process extends Node {
	
	public static final Integer COORDINATOR = 0;
	public static final Integer ACCEPTOR = 1;
	public static final Integer PROPOSER = 2;
	public static final Integer LEARNER = 3;

	private ArrayList<Process> coordinators;
	private ArrayList<Process> acceptors;
	private ArrayList<Process> proposers;
	private ArrayList<Process> learners;
	
	private int[] proposeCount;
	
	private Integer pid;
	private Integer type;
	
	private Integer balnum;
	private Integer value;
	
	public Process() {
		super();
	}

	public Process(Integer pid) {
		super();
		this.pid = pid;
		
		this.balnum = 0;
		
		coordinators = new ArrayList<>();
		acceptors = new ArrayList<>();
		proposers = new ArrayList<>();
		learners = new ArrayList<>();
		
		proposeCount = new int[FGGCMain.TOTAL_PROCESS];
		cleanProposeCount();
		
		
	}

	public Integer getPid() {
		return pid;
	}

	public void setPid(Integer pid) {
		this.pid = pid;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getBalnum() {
		return balnum;
	}

	public void setBalnum(Integer balnum) {
		this.balnum = balnum;
	}	
	
	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	public ArrayList<Process> getCoordinators() {
		return coordinators;
	}

	public void setCoordinators(ArrayList<Process> coordinators) {
		this.coordinators = coordinators;
	}

	public ArrayList<Process> getAcceptors() {
		return acceptors;
	}

	public void setAcceptors(ArrayList<Process> acceptors) {
		this.acceptors = acceptors;
	}

	public ArrayList<Process> getProposers() {
		return proposers;
	}

	public void setProposers(ArrayList<Process> proposers) {
		this.proposers = proposers;
	}

	public ArrayList<Process> getLearners() {
		return learners;
	}

	public void setLearners(ArrayList<Process> learners) {
		this.learners = learners;
	}

	public int[] getProposeCount() {
		return proposeCount;
	}

	public void setProposeCount(int[] acceptCount) {
		this.proposeCount = acceptCount;
	}

	@Override
	public void run() {
		new ReceiverUDP(this).start();
		try {
			
			if(type==PROPOSER) {
				System.out.println("..:: "+pid+" is the PROPOSER ::..");
				newBalNum();
			}
			else if(type==ACCEPTOR) {
				
			}
			else if(type==LEARNER) {
				
			}
			
		}
		catch(Exception e) { }
	}
	
	
	
	public void newBalNum() throws Exception {
		cleanProposeCount();
		balnum++;
		
		String payload = Integer.toString(balnum);
		
		Message msgPropose = new Message(Message.NEW_BALLOT, pid, getPort(), null, payload);
		
		for (Process process : FGGCMain.network) {
			if(process!=this) {
				System.out.println(pid+" sending NEW_BALLOT to "+process.getPid());
				msgPropose.setDestinationPort(process.getPort());
				new SenderUDP(this, msgPropose).start();
				
				process.setType(Process.LEARNER);
				learners.add(process);
				
				Thread.sleep(1000);
			}
		}
		
//		System.out.print("Leaners: |");
//		for (Process p : getLearners())
//			System.out.print(p.getPid()+"|");
//		System.out.println();
		
	}
	
	public synchronized void propose() throws Exception {
		if(!hasMajority())
			return;
		
		value = new Random().nextInt(10);
		String payload = balnum+","+value;
		
		Message msgPropose = new Message(Message.PROPOSE, pid, getPort(), null, payload);
		for (Process acceptor : acceptors) {
			msgPropose.setDestinationPort(acceptor.getPort());
			new SenderUDP(this, msgPropose).start();
			proposeCount[acceptor.getPid()] = 1;
		}
	}
	
	public boolean hasPendingPropose() {
		for (int p : proposeCount) {
			if(p==1)
				return true;
		}
		return false;
	}
	
	private boolean hasMajority() {
		return (acceptors.size() >= (FGGCMain.TOTAL_PROCESS/2)+1);
	}
	
	private void cleanProposeCount() {
		for (int i : proposeCount)
			i = 0;
	}

}
