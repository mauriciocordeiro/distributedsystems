package br.com.mac.ds.fggc;

import java.util.HashMap;
import java.util.Random;

public class FGGCMain {
	
	public static final int TOTAL_PROCESS = 5;
	public static HashMap<Integer, Integer> portTable = new HashMap<>();
	
	public static Process[] network; // all process
	public static int[] idFault = {}; // faulty ids

	public static void main(String[] args) {
		
		network = new Process[TOTAL_PROCESS];
		
		for (int i=0; i<TOTAL_PROCESS; i++) {
			network[i] = new Process(i);
			portTable.put(i, (7891+i));
			network[i].setPort(portTable.get(i));
			network[i].setIp("127.0.0.1");
		}
		
		network[new Random().nextInt(TOTAL_PROCESS)].setType(Process.PROPOSER);
		
		for(Process p : network)
			p.start();

//		try {
//			while(true) {
//				
//				
//				Thread.sleep(2000);
//			}
//		} catch (Exception e) { }

	}

}
