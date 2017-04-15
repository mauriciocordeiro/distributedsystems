package br.com.mac.ds.node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

import br.org.mac.midgard.util.List;

/**
 * 
 * @author Maurício
 *
 */
public class Node extends Thread {
	
	private String ip;
	private int port;
	private ArrayList<Integer> data;
	private HashMap<String, ArrayList<Integer>> buffer;
	
	public Node() {
		initData();
		this.buffer = new HashMap<>();
	}
		
	public Node(String ip, int sendPort) {
		this.ip = ip;
		this.port = sendPort;
		initData();
		this.buffer = new HashMap<>();
		
	}
	
	public Node(String ip, int sendPort, ArrayList<Integer> data) {
		this.ip = ip;
		this.port = sendPort;
		this.data = data;
		buffer = new HashMap<>();
	}
	
	public Node(String ip, int sendPort, ArrayList<Integer> data, HashMap<String, 
			ArrayList<Integer>> buffer) {
		this.ip = ip;
		this.port = sendPort;
		this.data = data;
		this.buffer = buffer;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int sendPort) {
		this.port = sendPort;
	}

	public ArrayList<Integer> getData() {
		return data;
	}

	public void setData(ArrayList<Integer> data) {
		this.data = data;
	}

	public HashMap<String, ArrayList<Integer>> getBuffer() {
		return buffer;
	}

	public void setBuffer(HashMap<String, ArrayList<Integer>> buffer) {
		this.buffer = buffer;
	}

	@Override
	public void run() {
		
		new Sender(this).start();
		new Receiver(this).start();
		
	}
	
	/**
	 * Put on buffer the received data
	 * @param senderIp
	 * @param data
	 */
	public void toBuffer(String senderIp, Object data) {
		buffer.put(senderIp, (ArrayList<Integer>)data);//List.stringToArrayList((String)data));		
	}
	
	public void printBuffer() {
		System.out.println("received");
		Iterator iterator = buffer.entrySet().iterator();
		while(iterator.hasNext()) {
			HashMap.Entry pair = (HashMap.Entry)iterator.next();
			System.out.println("\t"+"Node "+pair.getKey());
			ArrayList<Integer> data = buffer.get(pair.getKey());
			data.sort(null);
			System.out.print("\t\t");
			for (Integer integer : data) {
				System.out.print(integer+" ");
			}
			System.out.println();
			iterator.remove();
		}
	}
	
	/**
	 * Initialize data with 10 random integers between 0 and 100
	 */
	private void initData() {
		Random gen = new Random();
		data = new ArrayList<>();
		for(int i=0; i<10; i++)
			this.data.add(gen.nextInt(100));
	}

}
