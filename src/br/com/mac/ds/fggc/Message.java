package br.com.mac.ds.fggc;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Message implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public static final int NEW_BALLOT = 1;
	public static final int NEW_BALLOT_ACK = 2;
	public static final int PROPOSE = 3;
	public static final int PROPOSE_ACK = 4;
	public static final int LEARN = 5;	
	
	public static final String[] messageTypes = {"", "NEW_BALLOT", "NEW_BALLOT_ACK", "PROPOSE", "PROPOSE_ACK", "LEARN"};
	
	private Integer type;
	private Integer processId;
	private Integer originPort;
	private Integer destinationPort;
	private String payload;
	
	public Message() {}
	
	public Message(Integer type, Integer processId, Integer originPort, Integer destinationPort, String payload) {
		super();
		this.type = type;
		this.processId = processId;
		this.originPort = originPort;
		this.destinationPort = destinationPort;
		this.payload = payload;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getProcessId() {
		return processId;
	}

	public void setProcessId(Integer processId) {
		this.processId = processId;
	}

	public Integer getDestinationPort() {
		return destinationPort;
	}

	public void setDestinationPort(Integer port) {
		this.destinationPort = port;
	}

	public Integer getOriginPort() {
		return originPort;
	}

	public void setOriginPort(Integer originPort) {
		this.originPort = originPort;
	}

	public String getPayload() {
		return payload;
	}

	public void setPayload(String payload) {
		this.payload = payload;
	}

	public byte[] getBytes() {
		ByteArrayOutputStream baos = null;
		ObjectOutput out = null;
		try {
			baos = new ByteArrayOutputStream();
			out = new ObjectOutputStream(baos);
			out.writeObject(this);
			out.flush();
			
			return baos.toByteArray();
		} catch (Exception e) {
			return null;
		} finally {
			try {baos.close();} catch(Exception e){}
		}
	}
	
	public Message setBytes(byte[] bytes) {
		try {
			ObjectInputStream bais = new ObjectInputStream(new ByteArrayInputStream(bytes));
						
			return (Message)bais.readObject();
		}
		catch(Exception e) {
			return null;
		}
	}
	

}
