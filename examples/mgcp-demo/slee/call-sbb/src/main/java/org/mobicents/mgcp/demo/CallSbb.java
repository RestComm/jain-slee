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

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sip.RequestEvent;
import javax.sip.ServerTransaction;
import javax.sip.header.FromHeader;
import javax.sip.header.ToHeader;
import javax.sip.message.MessageFactory;
import javax.sip.message.Request;
import javax.sip.message.Response;
import javax.slee.ActivityContextInterface;
import javax.slee.ChildRelation;
import javax.slee.CreateException;
import javax.slee.RolledBackContext;
import javax.slee.Sbb;
import javax.slee.SbbContext;
import javax.slee.SbbLocalObject;

import net.java.slee.resource.sip.SleeSipProvider;

import org.apache.log4j.Logger;


/**
 * This SBB just acts as decision maker. For 1010 the INVITE event is routed to
 * CRCXSbb
 * 
 * @author Oleg Kulikov
 * @author amit.bhayani
 */
public abstract class CallSbb implements Sbb {

	public final static String CRCX_CONNECTIONID_DEMO = "2010";
	public final static String CRCX_ENDPOINTID_DEMO = "2011";
	public final static String MDCX_DEMO = "2012";
	public final static String RQNT_DEMO = "2013";

	private SbbContext sbbContext;

	// SIP
	private SleeSipProvider provider;

	private MessageFactory messageFactory;

	private Logger logger = Logger.getLogger(CallSbb.class);

	/** Creates a new instance of CallSbb */
	public CallSbb() {
	}

	public void onInvite(RequestEvent evt, ActivityContextInterface aci) {
		Request request = evt.getRequest();

		FromHeader from = (FromHeader) request.getHeader(FromHeader.NAME);
		ToHeader to = (ToHeader) request.getHeader(ToHeader.NAME);

		logger.info("Incoming call " + from + " " + to);

		String destination = to.toString();
		if (destination.indexOf(CRCX_CONNECTIONID_DEMO) > 0) {
			ChildRelation relation = getCRCXSbbChild();
			forwardEvent(relation, aci, evt);
		} else if (destination.indexOf(CRCX_ENDPOINTID_DEMO) > 0) {
			ChildRelation relation = getCRCXEndpointSbbChild();
			forwardEvent(relation, aci, evt);
		} else if (destination.indexOf(MDCX_DEMO) > 0) {
			ChildRelation relation = getMDCXSbbChild();
			forwardEvent(relation, aci, evt);
		} else if (destination.indexOf(RQNT_DEMO) > 0) {
			ChildRelation relation = getRQNTSbbChild();
			forwardEvent(relation, aci, evt);
		} else {
			logger.info("MGCP Demo can understand only " + CRCX_CONNECTIONID_DEMO + ", " + CRCX_ENDPOINTID_DEMO
					+ " and " + MDCX_DEMO + " dialed numbers");
		}
		return;
		// respond(evt, Response.RINGING);
	}

	private void forwardEvent(ChildRelation relation, ActivityContextInterface aci, RequestEvent evt) {
		try {
			respond(evt, Response.TRYING);
			SbbLocalObject child = relation.create();
			aci.attach(child);
			aci.detach(sbbContext.getSbbLocalObject());
		} catch (Exception e) {
			logger.error("Unexpected error: ", e);
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

	public void setSbbContext(SbbContext sbbContext) {
		this.sbbContext = sbbContext;
		try {
			Context ctx = (Context) new InitialContext().lookup("java:comp/env");

			// initialize SIP API
			provider = (SleeSipProvider) ctx.lookup("slee/resources/jainsip/1.2/provider");

			messageFactory = provider.getMessageFactory();

		} catch (Exception ne) {
			logger.error("Could not set SBB context:", ne);
		}
	}

	public abstract ChildRelation getCRCXSbbChild();

	public abstract ChildRelation getCRCXEndpointSbbChild();

	public abstract ChildRelation getMDCXSbbChild();

	public abstract ChildRelation getRQNTSbbChild();

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
