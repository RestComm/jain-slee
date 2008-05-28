package org.mobicents.slee.services.sip.balancer;

import gov.nist.javax.sip.header.CallID;
import gov.nist.javax.sip.stack.DefaultRouter;
import gov.nist.javax.sip.stack.HopImpl;

import java.util.LinkedList;
import java.util.ListIterator;

import javax.sip.SipException;
import javax.sip.SipStack;
import javax.sip.address.Hop;
import javax.sip.address.Router;
import javax.sip.message.Request;

import org.mobicents.slee.services.sip.balancer.mbean.SIPNode;

public class RouterImpl implements Router {

	private static NodeRegister register = null;

	private String myHost;

	private int myPort;

	

	public RouterImpl(SipStack sipStack, String proxyPort)
			throws Exception {
		try {
			this.myHost = sipStack.getIPAddress();
			this.myPort = Integer.parseInt(proxyPort);
		

		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	static void setRegister(NodeRegister register) {
		RouterImpl.register = register;
	}

	public Hop getNextHop(Request request) throws SipException {

		
		//System.out.println(this.getClass().getName()+".getNextHop() for:\n"+request );
		String callID = ((CallID) request.getHeader(CallID.NAME)).getCallId();

		SIPNode node = null;
		Hop hop = null;

		String method = request.getMethod();

		if (method.equals(Request.INVITE) || method.equals(Request.SUBSCRIBE)) {
			node = register.stickSessionToNode(callID);
		} else if (method.equals(Request.BYE) || method.equals(Request.CANCEL)) {
			// We have to clean, other side wants to go BYE ;/
			node = register.getGluedNode(callID);
			if (node == null) {
				for (int i = 0; i < 5 && node == null; i++) {
					try {
						node = register.getNextNode();
					} catch (IndexOutOfBoundsException ioobe) {
					}
				}
			}
		} else {

			node = register.getGluedNode(callID);

			if (node == null)
				for (int i = 0; i < 5 && node == null; i++) {
					try {
						node = register.getNextNode();
					} catch (IndexOutOfBoundsException ioobe) {
					}
				}
		}

		hop = new HopImpl(node.getIp(), node.getPort(), node.getTransports()[0]);

		System.out.println(this.getClass().getName()+".getNextHop() returning hop:"+hop );
		return hop;
	}

	public ListIterator getNextHops(Request request) {

		SIPNode node = null;
		for (int i = 0; i < 5 && node == null; i++) {
			try {
				node = register.getNextNode();
			} catch (IndexOutOfBoundsException ioobe) {
			}
		}

		if (node == null) {
			return null;
		} else {
			LinkedList retval = new LinkedList();
			retval.add(new HopImpl(node.getIp(), node.getPort(), node
					.getTransports()[0]));
			return retval.listIterator();
		}
	}

	public Hop getOutboundProxy() {
		// TODO Auto-generated method stub
		return null;
	}

}
