package br.com.mac.ds.fggc;

import java.util.HashMap;
import java.util.Random;

public class FGGCMain {
	
	public static final int TOTAL_PROCESS = 10;
	public static HashMap<Integer, Integer> portTable = new HashMap<>();
	
	public static Process[] network; // all process
	public static int[] idFault = {0,5,7}; // faulty ids

	public static void main(String[] args) {
		
		network = new Process[TOTAL_PROCESS];
		
		for (int i=0; i<TOTAL_PROCESS; i++) {
			network[i] = new Process(i);
			portTable.put(i, (7891+i));
			network[i].setPort(portTable.get(i));
			network[i].setIp("127.0.0.1");
			network[i].setType(Process.PROPOSER);
		}
		
//		network[new Random().nextInt(TOTAL_PROCESS)].setType(Process.PROPOSER);
		
		for(Process p : network)
			p.start();

	}

}
