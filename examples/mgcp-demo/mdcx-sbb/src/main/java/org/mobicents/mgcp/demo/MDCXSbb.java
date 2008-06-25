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
import jain.protocol.ip.mgcp.message.ModifyConnection;
import jain.protocol.ip.mgcp.message.ModifyConnectionResponse;
import jain.protocol.ip.mgcp.message.parms.CallIdentifier;
import jain.protocol.ip.mgcp.message.parms.ConnectionDescriptor;
import jain.protocol.ip.mgcp.message.parms.ConnectionIdentifier;
import jain.protocol.ip.mgcp.message.parms.ConnectionMode;
import jain.protocol.ip.mgcp.message.parms.EndpointIdentifier;
import jain.protocol.ip.mgcp.message.parms.ReturnCode;

import java.text.ParseException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sip.Dialog;
import javax.sip.InvalidArgumentException;
import javax.sip.RequestEvent;
import javax.sip.ServerTransaction;
import javax.sip.SipException;
import javax.sip.SipProvider;
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
import javax.slee.RolledBackContext;
import javax.slee.Sbb;
import javax.slee.SbbContext;
import javax.slee.UnrecognizedActivityException;

import net.java.slee.resource.mgcp.JainMgcpProvider;
import net.java.slee.resource.mgcp.MgcpActivityContextInterfaceFactory;
import net.java.slee.resource.mgcp.MgcpConnectionActivity;

import org.apache.log4j.Logger;
import org.mobicents.slee.resource.sip.SipActivityContextInterfaceFactory;
import org.mobicents.slee.resource.sip.SipResourceAdaptorSbbInterface;

/**
 * 
 * @author amit.bhayani
 */
public abstract class MDCXSbb implements Sbb {

	private static int CALL_ID_GEN = 1;
	private static int GEN = 2000;

	public final static String ENDPOINT_NAME = "media/test/trunk/Loopback/1";
	public final static String HELLO_WORLD = "http://localhost/mgcpdemo/audio/helloworld.wav";

	private SbbContext sbbContext;

	// SIP
	private SipResourceAdaptorSbbInterface fp;
	private SipProvider sipProvider;
	private AddressFactory addressFactory;
	private HeaderFactory headerFactory;
	private MessageFactory messageFactory;
	private SipActivityContextInterfaceFactory acif;

	// MGCP
	private JainMgcpProvider mgcpProvider;
	private MgcpActivityContextInterfaceFactory mgcpAcif;

	private Logger logger = Logger.getLogger(MDCXSbb.class);

	/** Creates a new instance of CallSbb */
	public MDCXSbb() {
	}

	public void onCallCreated(RequestEvent evt, ActivityContextInterface aci) {
		Request request = evt.getRequest();

		FromHeader from = (FromHeader) request.getHeader(FromHeader.NAME);
		ToHeader to = (ToHeader) request.getHeader(ToHeader.NAME);

		logger.info("Incoming call " + from + " " + to);

		// create Dialog and attach SBB to the Dialog Activity
		ActivityContextInterface daci = null;
		try {
			Dialog dialog = sipProvider.getNewDialog(evt.getServerTransaction());
			dialog.terminateOnBye(true);
			daci = acif.getActivityContextInterface(dialog);
			daci.attach(sbbContext.getSbbLocalObject());
		} catch (Exception e) {
			logger.error("Error during dialog creation", e);
			respond(evt, Response.SERVER_INTERNAL_ERROR);
			return;
		}

		String remoteSdp = new String(evt.getRequest().getRawContent());
		this.setRemoteSdp(remoteSdp);

		CallIdentifier callID = new CallIdentifier(Integer.toHexString(CALL_ID_GEN++));
		this.setCallIdentifier(callID.toString());

		EndpointIdentifier endpointID = new EndpointIdentifier(ENDPOINT_NAME, "localhost:2729");

		CreateConnection createConnection = new CreateConnection(this, callID, endpointID, ConnectionMode.SendRecv);

		int txID = GEN++;
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
	}

	public void onCreateConnectionResponse(CreateConnectionResponse event, ActivityContextInterface aci)
			throws ParseException {
		logger.info("Receive CRCX response: " + event.getTransactionHandle());

		ServerTransaction txn = getServerTransaction();
		Request request = txn.getRequest();

		ReturnCode status = event.getReturnCode();

		switch (status.getValue()) {
		case ReturnCode.TRANSACTION_EXECUTED_NORMALLY:

			this.setConnectionIdentifier(event.getConnectionIdentifier().toString());

			CallIdentifier callIdentifier = new CallIdentifier(this.getCallIdentifier());

			EndpointIdentifier endpointID = new EndpointIdentifier(ENDPOINT_NAME, "localhost:2729");

			ModifyConnection modifyConnection = new ModifyConnection(this, callIdentifier, endpointID, event
					.getConnectionIdentifier());

			ConnectionDescriptor connectionDescriptor = new ConnectionDescriptor(this.getRemoteSdp());
			modifyConnection.setRemoteConnectionDescriptor(connectionDescriptor);

			int txID = GEN++;
			modifyConnection.setTransactionHandle(txID);

			mgcpProvider.sendMgcpEvents(new JainMgcpEvent[] { modifyConnection });

			break;
		default:
			try {
				Response response = messageFactory.createResponse(Response.SERVER_INTERNAL_ERROR, request);
				txn.sendResponse(response);
			} catch (Exception ex) {
			}
		}
	}

	public void onModifyConnectionResponse(ModifyConnectionResponse event, ActivityContextInterface aci) {
		logger.info("Receive MDCX response: " + event.getTransactionHandle());
		ContentTypeHeader contentType = null;
		ServerTransaction txn = getServerTransaction();
		Response response = null;
		Request request = txn.getRequest();

		String sdp = event.getLocalConnectionDescriptor().toString();

		try {
			contentType = headerFactory.createContentTypeHeader("application", "sdp");
		} catch (ParseException ex) {
		}

		String localAddress = sipProvider.getListeningPoints()[0].getIPAddress();
		int localPort = sipProvider.getListeningPoints()[0].getPort();

		Address contactAddress = null;
		try {
			contactAddress = addressFactory.createAddress("sip:" + localAddress + ":" + localPort);
		} catch (ParseException ex) {
		}
		ContactHeader contact = headerFactory.createContactHeader(contactAddress);

		try {
			response = messageFactory.createResponse(Response.OK, request, contentType, sdp.getBytes());
		} catch (ParseException ex) {
			logger.error("ParseException while trying to create the OK Response for MDCX", ex);
		}

		response.setHeader(contact);
		try {
			txn.sendResponse(response);
		} catch (InvalidArgumentException ex) {
			logger.error("InvalidArgumentException while trying to send the OK Response for MDCX", ex);
		} catch (SipException ex) {
			logger.error("SipException while trying to send the OK Response for MDCX", ex);
		}

	}

	public void onCallTerminated(RequestEvent evt, ActivityContextInterface aci) {
		EndpointIdentifier endpointID = new EndpointIdentifier(ENDPOINT_NAME, "localhost:2729");
		DeleteConnection deleteConnection = new DeleteConnection(this, endpointID);

		deleteConnection.setConnectionIdentifier(new ConnectionIdentifier(this.getConnectionIdentifier()));

		int txID = GEN++;

		deleteConnection.setTransactionHandle(txID);
		mgcpProvider.sendMgcpEvents(new JainMgcpEvent[] { deleteConnection });

		ServerTransaction tx = evt.getServerTransaction();
		Request request = evt.getRequest();

		try {
			Response response = messageFactory.createResponse(Response.OK, request);
			tx.sendResponse(response);
		} catch (Exception e) {
		}
	}

	private void respond(RequestEvent evt, int cause) {
		Request request = evt.getRequest();
		ServerTransaction tx = evt.getServerTransaction();
		try {
			Response response = messageFactory.createResponse(cause, request);
			tx.sendResponse(response);
		} catch (Exception e) {
			logger.warn("Unexpected error: ", e);
		}
	}

	private ServerTransaction getServerTransaction() {
		ActivityContextInterface[] activities = sbbContext.getActivities();
		for (ActivityContextInterface activity : activities) {
			if (activity.getActivity() instanceof ServerTransaction) {
				return (ServerTransaction) activity.getActivity();
			}
		}
		return null;
	}

	public void setSbbContext(SbbContext sbbContext) {
		this.sbbContext = sbbContext;
		try {
			Context ctx = (Context) new InitialContext().lookup("java:comp/env");

			// initialize SIP API
			fp = (SipResourceAdaptorSbbInterface) ctx.lookup("slee/resources/jainsip/1.2/provider");
			sipProvider = fp.getSipProvider();
			addressFactory = fp.getAddressFactory();
			headerFactory = fp.getHeaderFactory();
			messageFactory = fp.getMessageFactory();
			acif = (SipActivityContextInterfaceFactory) ctx.lookup("slee/resources/jainsip/1.2/acifactory");

			// initialize media api

			mgcpProvider = (JainMgcpProvider) ctx.lookup("slee/resources/jainmgcp/2.0/provider/demo");
			mgcpAcif = (MgcpActivityContextInterfaceFactory) ctx.lookup("slee/resources/jainmgcp/2.0/acifactory/demo");

		} catch (Exception ne) {
			logger.error("Could not set SBB context:", ne);
		}
	}

	public abstract String getConnectionIdentifier();

	public abstract void setConnectionIdentifier(String connectionIdentifier);

	public abstract String getCallIdentifier();

	public abstract void setCallIdentifier(String CallIdentifier);

	public abstract String getRemoteSdp();

	public abstract void setRemoteSdp(String remoteSdp);

	public void unsetSbbContext() {
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
