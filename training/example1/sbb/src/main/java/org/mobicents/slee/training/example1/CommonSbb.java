/*
 * ***************************************************
 *                                                 *
 *  Mobicents: The Open Source JSLEE Platform      *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 ***************************************************
 */
package org.mobicents.slee.training.example1;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.slee.CreateException;
import javax.slee.Sbb;
import javax.slee.SbbContext;
import javax.slee.SbbID;
import javax.slee.facilities.Tracer;

import org.mobicents.slee.resource.lab.ratype.MessageResourceAdaptorSbbInterface;

/**
 * CommonSbb is the base SBB for all the Sbb's used in examples
 * 
 * @author amit bhayani
 * 
 */

public abstract class CommonSbb implements Sbb {

	private String className = null;

	protected Tracer tracer;
	// the identifier for this sbb
	private SbbID sbbId;

	// the sbb's sbbContext
	private SbbContext sbbContext;

	// the interface from the sbb into the resource adaptor
	private MessageResourceAdaptorSbbInterface sbb2ra;

	/** Creates a new instance of BounceSbb */
	public CommonSbb(String className) {
		this.className = className;
	}

	public void setSbbContext(SbbContext sbbContext) {
		
		this.sbbContext = sbbContext;
		sbbId = sbbContext.getSbb();
		this.tracer = sbbContext.getTracer(this.className);
		
		if (this.tracer.isFineEnabled()) {
			this.tracer.fine("setSbbContext() called. " + this);
		}
		
		try {
			Context ctx = (Context) new InitialContext()
					.lookup("java:comp/env");

			// get the reference to the RAFrameProvider class which implements
			// RAFrameResourceAdaptorSbbInterface
			sbb2ra = (MessageResourceAdaptorSbbInterface) ctx
					.lookup("slee/resources/raframe/2.0/sbb2ra");
		} catch (NamingException ne) {
			this.tracer.severe("NamingException received ", ne);
		}
	}

	public void unsetSbbContext() {
		if (this.tracer.isFineEnabled()) {
			this.tracer.fine("unsetSbbContext() called. " + this);
		}
	}

	public void sbbCreate() throws javax.slee.CreateException {
		if (this.tracer.isFineEnabled()) {
			this.tracer.fine("sbbCreate() called. " + this);
		}
	}

	public void sbbPostCreate() throws CreateException {
		if (this.tracer.isFineEnabled()) {
			this.tracer.fine("sbbCreate() called " + this);
		}
	}

	public void sbbActivate() {
		if (this.tracer.isFineEnabled()) {
			this.tracer.fine("sbbActivate() called " + this);
		}
	}

	public void sbbPassivate() {
		if (this.tracer.isFineEnabled()) {
			this.tracer.fine("sbbPassivate() called " + this);
		}
	}

	public void sbbRemove() {
		if (this.tracer.isFineEnabled()) {
			this.tracer.fine("sbbRemove() called " + this);
		}
	}

	public void sbbLoad() {
		if (this.tracer.isFineEnabled()) {
			this.tracer.fine("sbbLoad() called " + this);
		}
	}

	public void sbbStore() {
		if (this.tracer.isFineEnabled()) {
			this.tracer.fine("sbbStore() called " + this);
		}
	}

	public void sbbRolledBack(javax.slee.RolledBackContext rolledBackContext) {
		if (this.tracer.isFineEnabled()) {
			this.tracer.fine("sbbRolledBack() called " + this);
		}
	}

	public void sbbExceptionThrown(Exception exception, Object obj,
			javax.slee.ActivityContextInterface activityContextInterface) {
		if (this.tracer.isFineEnabled()) {
			this.tracer.fine("sbbExceptionThrown() called " + this);
		}
	}

	protected MessageResourceAdaptorSbbInterface getMessageResourceAdaptorSbbInterface() {
		return this.sbb2ra;
	}
}
