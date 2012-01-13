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
import javax.slee.facilities.Tracer;

import net.java.slee.resource.sip.SleeSipProvider;

/**
 * This SBB just acts as decision maker. For 1010 the INVITE event is routed to CRCXSbb
 * 
 * @author Oleg Kulikov
 * @author amit bhayani
 */
public abstract class CallSbb implements Sbb {

	public final static String IVR_DEMO = "2010";
	public final static String RECORDER_DEMO = "2011";
	public final static String CONFERENCE_DEMO = "2012";
	public final static String TTS_DEMO = "2013";

	private SbbContext sbbContext;

	// SIP
	private SleeSipProvider provider;

	private MessageFactory messageFactory;

	private Tracer logger;

	/** Creates a new instance of CallSbb */
	public CallSbb() {
	}

	public void onInvite(RequestEvent evt, ActivityContextInterface aci) {
		Request request = evt.getRequest();

		FromHeader from = (FromHeader) request.getHeader(FromHeader.NAME);
		ToHeader to = (ToHeader) request.getHeader(ToHeader.NAME);

		logger.info("Incoming call " + from + " " + to);

		String destination = to.toString();
		if (destination.indexOf(IVR_DEMO) > 0) {
			ChildRelation relation = getIVRSbbChild();
			forwardEvent(relation, aci, evt);
		} else if (destination.indexOf(RECORDER_DEMO) > 0) {
			ChildRelation relation = getRecorderSbb();
			forwardEvent(relation, aci, evt);
		} else if (destination.indexOf(CONFERENCE_DEMO) > 0) {
			// This should have been taken care
			return;
//		not implemented yet	
//		} else if (destination.indexOf(TTS_DEMO) > 0) {
//			ChildRelation relation = getTTSSbbChild();
//			forwardEvent(relation, aci, evt);
		} else {
			logger.info("MGCP Demo can understand only " + IVR_DEMO + ", " + RECORDER_DEMO + " and " + CONFERENCE_DEMO
					+ " dialed numbers");
			respond(evt, Response.SERVICE_UNAVAILABLE);
			return;
		}
	}

	private void forwardEvent(ChildRelation relation, ActivityContextInterface aci, RequestEvent evt) {
		try {
			respond(evt, Response.TRYING);
			SbbLocalObject child = relation.create();
			aci.attach(child);
			aci.detach(sbbContext.getSbbLocalObject());
		} catch (Exception e) {
			logger.severe("Unexpected error: ", e);
		}
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

	public void setSbbContext(SbbContext sbbContext) {
		this.sbbContext = sbbContext;
		this.logger = sbbContext.getTracer(CallSbb.class.getSimpleName());
		try {
			Context ctx = (Context) new InitialContext().lookup("java:comp/env");

			// initialize SIP API
			provider = (SleeSipProvider) ctx.lookup("slee/resources/jainsip/1.2/provider");

			messageFactory = provider.getMessageFactory();

		} catch (Exception ne) {
			logger.severe("Could not set SBB context:", ne);
		}
	}

	public abstract ChildRelation getRecorderSbb();

	// public abstract ChildRelation getConferenceSbbChild();

	public abstract ChildRelation getIVRSbbChild();

	public abstract ChildRelation getTTSSbbChild();

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
