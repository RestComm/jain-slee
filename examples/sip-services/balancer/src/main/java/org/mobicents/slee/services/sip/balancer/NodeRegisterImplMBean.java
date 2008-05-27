package org.mobicents.slee.services.sip.balancer;

import java.net.InetAddress;
import java.util.List;

import org.mobicents.slee.services.sip.balancer.mbean.SIPNode;

public interface NodeRegisterImplMBean {

	
	
	/**
	 * Sets interval between runs of task that removes nodes that expired.
	 * @param value
	 */
	public void setNodeExpirationTaskInterval(long value);
	public long getNodeExpirationTaskInterval();
	
	/**
	 * Sets value which indicates when node has expired. if node.timeStamp+nodeExpiration<System.currentTimeMilis than node has expired and on next
	 * run of nodeExpirationTask will be removed.
	 * @param value
	 */
	public void setNodeExpiration(long value);
	public long getNodeExpiration();
	
	///**
	// * Sets new value of interval between fetching data sent via sockets to this balancer
	// * @param value
	// */
	//public void setPingFetchInterval(long value);
	//public long getPingFetchInterval();
	
	//public int getPort();
	public InetAddress getAddress();
	
	public boolean startServer();
	public boolean stopServer();
	
	public List<SIPNode> getGatheredInfo();
	
}
