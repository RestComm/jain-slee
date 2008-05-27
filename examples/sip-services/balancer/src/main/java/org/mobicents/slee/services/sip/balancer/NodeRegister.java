package org.mobicents.slee.services.sip.balancer;

import java.net.InetAddress;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.mobicents.slee.services.sip.balancer.mbean.SIPNode;

public interface NodeRegister extends Remote{

	
	
	

	public SIPNode getNextNode() throws IndexOutOfBoundsException;

	public SIPNode stickSessionToNode(String callID);
	
	public SIPNode getGluedNode(String callID);

	public void unStickSessionFromNode(String callID);
	
	public void handlePingInRegister(ArrayList<SIPNode> ping);
	
}
