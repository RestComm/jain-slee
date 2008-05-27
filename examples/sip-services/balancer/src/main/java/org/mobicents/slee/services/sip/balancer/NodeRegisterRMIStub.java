package org.mobicents.slee.services.sip.balancer;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

import org.mobicents.slee.services.sip.balancer.mbean.SIPNode;

public interface NodeRegisterRMIStub extends Remote {

	public void handlePing(ArrayList<SIPNode> ping) throws RemoteException;
	
}
