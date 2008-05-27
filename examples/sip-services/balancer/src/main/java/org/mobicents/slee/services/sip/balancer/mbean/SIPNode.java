package org.mobicents.slee.services.sip.balancer.mbean;

import java.awt.SystemColor;
import java.io.Serializable;
import java.net.Socket;
import java.util.Arrays;

public class SIPNode implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4959114432342926569L;
	private String hostName = null;
	private String ip = null;
	private int port = -1;
	private String[] transports = null;
	private long timeStamp=System.currentTimeMillis();
	
	//private transient Socket s=null;
	
	public SIPNode(String hostName, String ip, int port, String[] transports) {
		super();
		this.hostName = hostName;
		this.ip = ip;
		this.port = port;
		this.transports = transports;
	}

	public String getHostName() {
		return hostName;
	}

	public String getIp() {
		return ip;
	}

	public int getPort() {
		return port;
	}

	public String[] getTransports() {
		return transports;
	}

	
	public long getTimeStamp()
	{
		return this.timeStamp;
	}
	
	public void updateTimerStamp()
	{
		this.timeStamp=System.currentTimeMillis();
	}
	
	
	
	//public Socket getSocket() {
	//	return s;
	//}

	//public void setSocket(Socket s) {
	//	this.s = s;
	//}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((hostName == null) ? 0 : hostName.hashCode());
		result = prime * result + ((ip == null) ? 0 : ip.hashCode());
		result = prime * result + port;
		result = prime * result + Arrays.hashCode(transports);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final SIPNode other = (SIPNode) obj;
		if (hostName == null) {
			if (other.hostName != null)
				return false;
		} else if (!hostName.equals(other.hostName))
			return false;
		if (ip == null) {
			if (other.ip != null)
				return false;
		} else if (!ip.equals(other.ip))
			return false;
		if (port != other.port)
			return false;
		if (!Arrays.equals(transports, other.transports))
			return false;
		return true;
	}

	public String toString()
	{
		
		return "SIPNode hostname["+this.hostName+"] ip["+this.ip+"] port["+this.port+"] transport["+Arrays.toString(this.transports)+"]";
	}
	
	
}
