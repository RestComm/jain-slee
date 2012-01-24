/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

/*
 * CallSbb.java
 *
 * The source code contained in this file is in in the public domain.
 * It can be used in any project or product without prior permission,
 * license or royalty payments. There is  NO WARRANTY OF ANY KIND,
 * EXPRESS, IMPLIED OR STATUTORY, INCLUDING, WITHOUT LIMITATION,
 * THE IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE,
 * AND DATA ACCURACY.  We do not warrant or make any representations
 * regarding the use of the software or the  results thereof, including
 * but not limited to the correctness, accuracy, reliability or
 * usefulness of the software.
 */
package org.mobicents.mgcp.demo;

import jain.protocol.ip.mgcp.JainMgcpEvent;
import jain.protocol.ip.mgcp.message.CreateConnection;
import jain.protocol.ip.mgcp.message.CreateConnectionResponse;
import jain.protocol.ip.mgcp.message.DeleteConnection;
import jain.protocol.ip.mgcp.message.parms.CallIdentifier;
import jain.protocol.ip.mgcp.message.parms.ConflictingParameterException;
import jain.protocol.ip.mgcp.message.parms.ConnectionDescriptor;
import jain.protocol.ip.mgcp.message.parms.ConnectionIdentifier;
import jain.protocol.ip.mgcp.message.parms.ConnectionMode;
import jain.protocol.ip.mgcp.message.parms.EndpointIdentifier;
import jain.protocol.ip.mgcp.message.parms.ReturnCode;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Iterator;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sip.Dialog;
import javax.sip.InvalidArgumentException;
import javax.sip.RequestEvent;
import javax.sip.ServerTransaction;
import javax.sip.SipException;
import javax.sip.address.Address;
import javax.sip.address.AddressFactory;
import javax.sip.header.ContactHeader;
import javax.sip.header.ContentTypeHeader;
import javax.sip.header.FromHeader;
import javax.sip.header.HeaderFactory;
import javax.sip.header.ToHeader;
import javax.sip.message.MessageFactory;
import javax.sip.message.Request;
import javax.sip.message.Response;
import javax.slee.ActivityContextInterface;
import javax.slee.CreateException;
import javax.slee.FactoryException;
import javax.slee.InitialEventSelector;
import javax.slee.RolledBackContext;
import javax.slee.Sbb;
import javax.slee.SbbContext;
import javax.slee.UnrecognizedActivityException;
import javax.slee.facilities.Tracer;

import net.java.slee.resource.mgcp.JainMgcpProvider;
import net.java.slee.resource.mgcp.MgcpActivityContextInterfaceFactory;
import net.java.slee.resource.mgcp.MgcpConnectionActivity;
import net.java.slee.resource.sip.DialogActivity;
import net.java.slee.resource.sip.SipActivityContextInterfaceFactory;
import net.java.slee.resource.sip.SleeSipProvider;

import org.mobicents.mgcp.demo.events.CustomEvent;

/**
 * 
 * @author amit bhayani
 */
public abstract class ConferenceSbb implements Sbb {

	public final static String JBOSS_BIND_ADDRESS = System.getProperty("jboss.bind.address", "127.0.0.1");

	public final static String ENDPOINT_NAME = "mobicents/cnf/$";

	public static final int MGCP_PEER_PORT = 2427;
	public static final int MGCP_PORT = 2727;

	public final static String CONFERENCE_DEMO = "2012";

	private SbbContext sbbContext;

	// SIP
	private SleeSipProvider provider;

	private AddressFactory addressFactory;
	private HeaderFactory headerFactory;
	private MessageFactory messageFactory;
	private SipActivityContextInterfaceFactory acif;

	// MGCP
	private JainMgcpProvider mgcpProvider;
	private MgcpActivityContextInterfaceFactory mgcpAcif;

	private Tracer logger;

	/** Creates a new instance of CallSbb */
	public ConferenceSbb() {
	}

	public void onCallCreated(RequestEvent evt, ActivityContextInterface aci) {
		Request request = evt.getRequest();

		FromHeader from = (FromHeader) request.getHeader(FromHeader.NAME);
		String fromURI = from.getAddress().getURI().toString();
		ToHeader to = (ToHeader) request.getHeader(ToHeader.NAME);

		logger.info("Incoming call " + from + " " + to);

		// create Dialog and attach SBB to the Dialog Activity
		ActivityContextInterface daci = null;
		try {
			Dialog dialog = provider.getNewDialog(evt.getServerTransaction());
			dialog.terminateOnBye(true);
			daci = acif.getActivityContextInterface((DialogActivity) dialog);
			daci.attach(sbbContext.getSbbLocalObject());
		} catch (Exception e) {
			logger.severe("Error during dialog creation", e);
			respond(evt, Response.SERVER_INTERNAL_ERROR);
			return;
		}

		// respond(evt, Response.RINGING);
		HashMap fromVsConnIdMap = this.getFromVsConnIdMap();
		HashMap<Integer, ServerTransaction> txIdVsServerTxMap = this.getTxIdVsServerTxMap();
		EndpointIdentifier endpointID = this.getEndpointIdentifier();
		CallIdentifier callID = this.getCallIdentifier();
		if (endpointID == null) {
			logger.info("This is new Conference");
			endpointID = new EndpointIdentifier(ENDPOINT_NAME, JBOSS_BIND_ADDRESS + ":" + MGCP_PEER_PORT);
			fromVsConnIdMap = new HashMap();

			txIdVsServerTxMap = new HashMap<Integer, ServerTransaction>();

			callID = this.mgcpProvider.getUniqueCallIdentifier();
			this.setCallIdentifier(callID);
		} else {
			logger.info("Conference has already begun at endpoint " + endpointID);
		}

		CreateConnection createConnection = new CreateConnection(this, callID, endpointID, ConnectionMode.Confrnce);

		try {
			String sdp = new String(evt.getRequest().getRawContent());
			createConnection.setRemoteConnectionDescriptor(new ConnectionDescriptor(sdp));
		} catch (ConflictingParameterException e) {
			logger.severe("Erro while setting SDP in CRCX", e);
		}

		int txID = mgcpProvider.getUniqueTransactionHandler();

		fromVsConnIdMap.put(fromURI, txID);
		txIdVsServerTxMap.put(txID, evt.getServerTransaction());
		this.setTxIdVsServerTxMap(txIdVsServerTxMap);
		this.setFromVsConnIdMap(fromVsConnIdMap);

		createConnection.setTransactionHandle(txID);

		MgcpConnectionActivity connectionActivity = null;
		try {
			connectionActivity = mgcpProvider.getConnectionActivity(txID, endpointID);
			ActivityContextInterface epnAci = mgcpAcif.getActivityContextInterface(connectionActivity);
			epnAci.attach(sbbContext.getSbbLocalObject());
		} catch (FactoryException ex) {
			ex.printStackTrace();
		} catch (NullPointerException ex) {
			ex.printStackTrace();
		} catch (UnrecognizedActivityException ex) {
			ex.printStackTrace();
		}

		mgcpProvider.sendMgcpEvents(new JainMgcpEvent[] { createConnection });
		logger.info("Sent CRCX:\n"+createConnection);
	}

	public void onCreateConnectionResponse(CreateConnectionResponse event, ActivityContextInterface aci) {
		logger.info("Receive CRCX response: \n" + event);

		ServerTransaction txn = null;
		HashMap fromVsConnIdMap = this.getFromVsConnIdMap();
		Iterator itr = fromVsConnIdMap.keySet().iterator();
		while (itr.hasNext()) {
			String fromUri = (String) itr.next();
			Object obj = fromVsConnIdMap.get(fromUri);

			if (obj instanceof Integer) {
				int txId = (Integer) obj;
				if (event.getTransactionHandle() == txId) {
					txn = this.getTxIdVsServerTxMap().remove(txId);
					fromVsConnIdMap.put(fromUri, event.getConnectionIdentifier());
					break;
				}
			}
		}

		Request request = txn.getRequest();

		ReturnCode status = event.getReturnCode();

		switch (status.getValue()) {
		case ReturnCode.TRANSACTION_EXECUTED_NORMALLY:

			this.setEndpointIdentifier(event.getSpecificEndpointIdentifier());

			String sdp = event.getLocalConnectionDescriptor().toString();

			ContentTypeHeader contentType = null;
			try {
				contentType = headerFactory.createContentTypeHeader("application", "sdp");
			} catch (ParseException ex) {
				ex.printStackTrace();
			}

			String localAddress = provider.getListeningPoints()[0].getIPAddress();
			int localPort = provider.getListeningPoints()[0].getPort();

			Address contactAddress = null;
			try {
				contactAddress = addressFactory.createAddress("sip:" + localAddress + ":" + localPort);
			} catch (ParseException ex) {
				ex.printStackTrace();
			}
			ContactHeader contact = headerFactory.createContactHeader(contactAddress);

			Response response = null;
			try {
				response = messageFactory.createResponse(Response.OK, request, contentType, sdp.getBytes());
			} catch (ParseException ex) {
				ex.printStackTrace();
			}

			response.setHeader(contact);
			try {
				txn.sendResponse(response);

				if (fromVsConnIdMap.size() == 1) {
					CustomEvent cutEvent = new CustomEvent(this.getEndpointIdentifier().getLocalEndpointName(), this
							.getCallIdentifier().toString());
					fireConferenceInitiate(cutEvent, aci, null);
				}
			} catch (InvalidArgumentException ex) {
				ex.printStackTrace();
			} catch (SipException ex) {
				ex.printStackTrace();
			}
			break;
		default:
			try {
				response = messageFactory.createResponse(Response.SERVER_INTERNAL_ERROR, request);
				txn.sendResponse(response);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	public void onCallTerminated(RequestEvent evt, ActivityContextInterface aci) {

		try {
			Request request = evt.getRequest();
			FromHeader from = (FromHeader) request.getHeader(FromHeader.NAME);
			String fromURI = from.getAddress().getURI().toString();

			ConnectionIdentifier connId = (ConnectionIdentifier) this.getFromVsConnIdMap().remove(fromURI);

			// EndpointIdentifier endpointID = new EndpointIdentifier(ENDPOINT_NAME, JBOSS_BIND_ADDRESS+":2729");
			EndpointIdentifier endpointID = this.getEndpointIdentifier();
			DeleteConnection deleteConnection = new DeleteConnection(this, this.getCallIdentifier(), endpointID/*, connId*/);

			deleteConnection.setTransactionHandle(this.mgcpProvider.getUniqueTransactionHandler());
			mgcpProvider.sendMgcpEvents(new JainMgcpEvent[] { deleteConnection });

			ServerTransaction tx = evt.getServerTransaction();

			Response response = messageFactory.createResponse(Response.OK, request);
			tx.sendResponse(response);

			if (this.getFromVsConnIdMap().size() == 0) {
				CustomEvent cutEvent = new CustomEvent(this.getEndpointIdentifier().getLocalEndpointName(), this
						.getCallIdentifier().toString());
				fireConferenceTerminate(cutEvent, getMgcpConnectionACI(), null);
			}

		} catch (Exception e) {
			logger.severe("Error while sending DLCX", e);
		}
	}

	public abstract void fireConferenceInitiate(CustomEvent event, ActivityContextInterface aci,
			javax.slee.Address address);

	public abstract void fireConferenceTerminate(CustomEvent event, ActivityContextInterface aci,
			javax.slee.Address address);

	public InitialEventSelector callIDSelect(InitialEventSelector ies) {
		Object event = ies.getEvent();

		if (event instanceof RequestEvent) {
			Request request = ((RequestEvent) event).getRequest();

			ToHeader to = (ToHeader) request.getHeader(ToHeader.NAME);
			String destination = to.toString();
			if (destination.indexOf(CONFERENCE_DEMO) > 0) {
				ies.setCustomName("2012");
				return ies;
			}
		}
		ies.setInitialEvent(false);
		return ies;
	}

	private void respond(RequestEvent evt, int cause) {
		Request request = evt.getRequest();
		ServerTransaction tx = evt.getServerTransaction();
		try {
			Response response = messageFactory.createResponse(cause, request);
			tx.sendResponse(response);
		} catch (Exception e) {
			logger.warning("Unexpected error: ", e);
		}
	}

	private ActivityContextInterface getMgcpConnectionACI() {
		ActivityContextInterface[] activities = sbbContext.getActivities();
		for (ActivityContextInterface activity : activities) {
			if (activity.getActivity() instanceof MgcpConnectionActivity) {
				return activity;
			}
		}
		return null;
	}

	public void setSbbContext(SbbContext sbbContext) {
		this.sbbContext = sbbContext;
		this.logger = sbbContext.getTracer(ConferenceSbb.class.getSimpleName());
		try {
			Context ctx = (Context) new InitialContext().lookup("java:comp/env");

			// initialize SIP API
			provider = (SleeSipProvider) ctx.lookup("slee/resources/jainsip/1.2/provider");
			addressFactory = provider.getAddressFactory();
			headerFactory = provider.getHeaderFactory();
			messageFactory = provider.getMessageFactory();
			acif = (SipActivityContextInterfaceFactory) ctx.lookup("slee/resources/jainsip/1.2/acifactory");

			// initialize media api

			mgcpProvider = (JainMgcpProvider) ctx.lookup("slee/resources/jainmgcp/2.0/provider/demo");
			mgcpAcif = (MgcpActivityContextInterfaceFactory) ctx.lookup("slee/resources/jainmgcp/2.0/acifactory/demo");

		} catch (Exception ne) {
			logger.severe("Could not set SBB context:", ne);
		}
	}

	public abstract EndpointIdentifier getEndpointIdentifier();

	public abstract void setEndpointIdentifier(EndpointIdentifier endpointIdentifier);

	public abstract CallIdentifier getCallIdentifier();

	public abstract void setCallIdentifier(CallIdentifier callIdentifier);

	public abstract HashMap getFromVsConnIdMap();

	public abstract void setFromVsConnIdMap(HashMap fromVsConnIdMap);

	public abstract HashMap<Integer, ServerTransaction> getTxIdVsServerTxMap();

	public abstract void setTxIdVsServerTxMap(HashMap<Integer, ServerTransaction> txIdVsServerTxMap);

	public void unsetSbbContext() {
		this.sbbContext = null;
		this.logger = null;
	}

	public void sbbCreate() throws CreateException {
	}

	public void sbbPostCreate() throws CreateException {
	}

	public void sbbActivate() {
	}

	public void sbbPassivate() {
	}

	public void sbbLoad() {
	}

	public void sbbStore() {
	}

	public void sbbRemove() {
	}

	public void sbbExceptionThrown(Exception exception, Object object, ActivityContextInterface activityContextInterface) {
	}

	public void sbbRolledBack(RolledBackContext rolledBackContext) {
	}
}
