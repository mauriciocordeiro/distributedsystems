package br.com.mac.ds.kmutexfd;

import java.util.HashMap;

public class KMain {
	
	public static final int TOTAL_PROCESS = 5;
	public static final int TOTAL_RESOURCE = 2;
	
	@Deprecated
	public static final String MC_GROUP_IP = "224.2.2.2"; //ip do grupo de multicast
	@Deprecated
	public static final int DEFAULT_PORT = 7890; //porta padrão do grupo
	@Deprecated
	public static final int TTL = 1;
	
	public static HashMap<Integer, Integer> portTable = new HashMap<>();
	
	public static Process[] network; // all process
	public static int[] idFault = {4}; // faulty ids

	public static void main(String[] args) {
		network = new Process[TOTAL_PROCESS];
		
		for (int i=0; i<TOTAL_PROCESS; i++) {
			network[i] = new Process(i);
			portTable.put(i, (7891+i));
			network[i].setPort(portTable.get(i));
			network[i].setIp("127.0.0.1");
//			network[i].start();
			
		}
		
		for(Process p : network)
			p.start();
	}
	
	public static int max(int a, int b) {
		return (a>b ? a : b);
	}

}
