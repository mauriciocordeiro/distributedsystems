package br.com.mac.ds.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import br.com.mac.ds.node.Node;
import br.org.mac.midgard.util.List;

/**
 * 10 Threads, which works like nodes on a distributed system.
 * Each node will send a list to the other nodes and will receive the other
 * lists.
 * 
 * @author Maurício
 */
public class Main {

	public static int PORT_PREFIX = 20000;
	public static int NODE_AMOUNT = 10;
	
	public static void main(String[] args) {
		/*
		 * Create an start threads
		 */
		Node[] nodes = new Node[10];
		for(int i=0; i<10; i++) {
			nodes[i] = new Node();
			nodes[i].setIp("127.0.0.1");
			nodes[i].setPort(PORT_PREFIX + i);
			
			nodes[i].start();
		}
		
		//time to all threads finish
		try{Thread.sleep(1000);}catch(Exception e){}
		
		for(int i=0; i<10; i++) {
			System.out.println(i+"==========");
			ArrayList<Integer> data = nodes[i].getData();
			data.sort(null);
			System.out.println(List.toString(data));
			System.out.println("----------");
			HashMap<String, ArrayList<Integer>> buffer = nodes[i].getBuffer();
			Iterator iterator = buffer.entrySet().iterator();
			while(iterator.hasNext()) {
				HashMap.Entry pair = (HashMap.Entry)iterator.next();
				data = buffer.get(pair.getKey());
				data.sort(null);
				System.out.println(List.toString(data));
				iterator.remove();
			}
			System.out.println("=========="+i);
		}
	}
}
