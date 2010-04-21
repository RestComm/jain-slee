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
package org.mobicents.slee.service.common;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sip.SipProvider;
import javax.slee.CreateException;
import javax.slee.Sbb;
import javax.slee.SbbContext;
import javax.slee.SbbID;
import javax.slee.facilities.Tracer;
import javax.slee.nullactivity.NullActivityContextInterfaceFactory;
import javax.slee.nullactivity.NullActivityFactory;

import net.java.slee.resource.mgcp.JainMgcpProvider;
import net.java.slee.resource.mgcp.MgcpActivityContextInterfaceFactory;
import net.java.slee.resource.sip.SipActivityContextInterfaceFactory;


import org.apache.log4j.Logger;

//import org.mobicents.slee.resource.tts.ratype.TTSActivityContextInterfaceFactory;
//import org.mobicents.slee.resource.tts.ratype.TTSProvider;
import org.mobicents.slee.util.CacheException;
import org.mobicents.slee.util.CacheFactory;
import org.mobicents.slee.util.CacheUtility;
import org.mobicents.slee.util.SipUtils;
import org.mobicents.slee.util.SipUtilsFactorySingleton;

/**
 * CommonSbb is the base SBB for all the BounceSbb
 * 
 * @author amit bhayani
 * @author baranowb
 */

public abstract class CommonSbb implements Sbb {

	public final static String JBOSS_BIND_ADDRESS = System.getProperty("jboss.bind.address", "127.0.0.1");
	public static final int MGCP_PEER_PORT = 2427;
	public static final int MGCP_PORT = 2727;
	
	private CacheUtility cache;

	// the identifier for this sbb
	private SbbID sbbId;

	// the sbb's sbbContext
	private SbbContext sbbContext;

	private NullActivityContextInterfaceFactory nullACIFactory;

	private NullActivityFactory nullActivityFactory;

	private SipUtils sipUtils;

	

	private SipProvider sipProvider;

	private SipActivityContextInterfaceFactory sipActivityContextInterfaceFactory;
	
	private MgcpActivityContextInterfaceFactory mgcpActivityContestInterfaceFactory;
	
	private JainMgcpProvider mgcpProvider;


	private Tracer logger =null;

	/** Creates a new instance of BounceSbb */
	public CommonSbb() {
	}

	/**
	 * implements javax.slee.Sbb Please refer to JSLEE v1.1 Specification, Early
	 * Draft Review Page 54 for further information. <br>
	 * The SLEE invokes this method after a new instance of the SBB abstract
	 * class is created. During this method, an SBB entity has not been assigned
	 * to the SBB object. The SBB object can take advantage of this method to
	 * allocate and initialize state or connect to resources that are to be held
	 * by the SBB object during its lifetime. Such state and resources cannot be
	 * specific to an SBB entity because the SBB object might be reused during
	 * its lifetime to serve multiple SBB entities. <br>
	 * This method indicates a transition from state "DOES NOT EXIST" to
	 * "POOLED" (see page 52)
	 */
	public void setSbbContext(SbbContext sbbContext) {
		this.logger = sbbContext.getTracer("Common");
		this.sbbContext = sbbContext;
		sbbId = sbbContext.getSbb();

		try {

			// Create the cache used for the session association
			cache = CacheFactory.getInstance().getCache();

			if (logger.isFineEnabled()) {
				logger.fine("Got Cache instance!");
			}

			Context ctx = (Context) new InitialContext()
					.lookup("java:comp/env");

			nullACIFactory = (NullActivityContextInterfaceFactory) ctx
					.lookup("slee/nullactivity/activitycontextinterfacefactory");
			nullActivityFactory = (NullActivityFactory) ctx
					.lookup("slee/nullactivity/factory");

			sipUtils = SipUtilsFactorySingleton.getInstance().getSipUtils();
			sipProvider = (SipProvider) ctx
			.lookup("slee/resources/jainsip/1.2/provider");

	
			sipActivityContextInterfaceFactory = (SipActivityContextInterfaceFactory) ctx
			.lookup("slee/resources/jainsip/1.2/acifactory");
			// Getting JAIN SIP Resource Adaptor interfaces
			mgcpProvider = (JainMgcpProvider) ctx.lookup("slee/resources/jainmgcp/2.0/provider/demo");
			mgcpActivityContestInterfaceFactory = (MgcpActivityContextInterfaceFactory) ctx.lookup("slee/resources/jainmgcp/2.0/acifactory/demo");
			

		} catch (NamingException ne) {
			logger.severe("NamingException in CommonSbb", ne);
		} catch (CacheException ce) {
			logger.severe("CacheException while trying to create Cahe", ce);
		}
	}

	/**
	 * implements javax.slee.Sbb Please refer to JSLEE v1.1 Specification, Early
	 * Draft Review Page 54 for further information. <br>
	 * The SLEE invokes this method before terminating the life of the SBB
	 * object. The SBB object can take advantage of this method to free state or
	 * resources that are held by the SBB object. These state and resources
	 * typically had been allocated by the setSbbContext method. <br>
	 * This method indicates a transition from state "POOLED" to "DOES NOT
	 * EXIST" (see page 52)
	 */
	public void unsetSbbContext() {
		logger.info("CommonSbb: " + this + ": unsetSbbContext() called.");
	}

	/**
	 * implements javax.slee.Sbb Please refer to JSLEE v1.1 Specification, Early
	 * Draft Review Page 55 for further information. <br>
	 * The SLEE invokes this method on an SBB object before the SLEE creates a
	 * new SBB entity in response to an initial event or an invocation of the
	 * create method on a ChildRelation object. This method should initialize
	 * the SBB object using the CMP field get and set accessor methods, such
	 * that when this method returns, the persistent representation of the SBB
	 * entity can be created. <br>
	 * This method is the first part of a transition from state "POOLED" to
	 * "READY" (see page 52)
	 */
	public void sbbCreate() throws javax.slee.CreateException {
		logger.info("CommonSbb: " + this + ": sbbCreate() called.");
	}

	/**
	 * implements javax.slee.Sbb Please refer to JSLEE v1.1 Specification, Early
	 * Draft Review Page 55 for further information. <br>
	 * The SLEE invokes this method on an SBB object after the SLEE creates a
	 * new SBB entity. The SLEE invokes this method after the persistent
	 * representation of the SBB entity has been created and the SBB object is
	 * assigned to the created SBB entity. This method gives the SBB object a
	 * chance to initialize additional transient state and acquire additional
	 * resources that it needs while it is in the Ready state. <br>
	 * This method is the second part of a transition from state "POOLED" to
	 * "READY" (see page 52)
	 */
	public void sbbPostCreate() throws CreateException {
		logger.info("CommonSbb: " + this + ": sbbPostCreate() called.");
	}

	/**
	 * implements javax.slee.Sbb Please refer to JSLEE v1.1 Specification, Early
	 * Draft Review Page 55 for further information. <br>
	 * The SLEE invokes this method on an SBB object when the SLEE picks the SBB
	 * object in the pooled state and assigns it to a specific SBB entity. This
	 * method gives the SBB object a chance to initialize additional transient
	 * state and acquire additional resources that it needs while it is in the
	 * Ready state. <br>
	 * This method indicates a transition from state "POOLED" to "READY" (see
	 * page 52)
	 */
	public void sbbActivate() {
		logger.info("CommonSbb: " + this + ": sbbActivate() called.");
	}

	/**
	 * implements javax.slee.Sbb Please refer to JSLEE v1.1 Specification, Early
	 * Draft Review Page 56 for further information. <br>
	 * The SLEE invokes this method on an SBB object when the SLEE decides to
	 * disassociate the SBB object from the SBB entity, and to put the SBB
	 * object back into the pool of available SBB objects. This method gives the
	 * SBB object the chance to release any state or resources that should not
	 * be held while the SBB object is in the pool. These state and resources
	 * typically had been allocated during the sbbActivate method. <br>
	 * This method indicates a transition from state "READY" to "POOLED" (see
	 * page 52)
	 */
	public void sbbPassivate() {
		logger.info("CommonSbb: " + this + ": sbbPassivate() called.");
	}

	/**
	 * implements javax.slee.Sbb Please refer to JSLEE v1.1 Specification, Early
	 * Draft Review Page 56 for further information. <br>
	 * The SLEE invokes the sbbRemove method on an SBB object before the SLEE
	 * removes the SBB entity assigned to the SBB object. <br>
	 * This method indicates a transition from state "READY" to "POOLED" (see
	 * page 52)
	 */
	public void sbbRemove() {
		logger.info("CommonSbb: " + this + ": sbbRemove() called.");
	}

	/**
	 * implements javax.slee.Sbb Please refer to JSLEE v1.1 Specification, Early
	 * Draft Review Page 56 for further information. <br>
	 * The SLEE calls this method to synchronize the state of an SBB object with
	 * its assigned SBB entity�s persistent state. The SBB Developer can assume
	 * that the SBB object�s persistent state has been loaded just before this
	 * method is invoked. <br>
	 * This method indicates a transition from state "READY" to "READY" (see
	 * page 52)
	 */
	public void sbbLoad() {
		logger.info("CommonSbb: " + this + ": sbbLoad() called.");
	}

	/**
	 * implements javax.slee.Sbb Please refer to JSLEE v1.1 Specification, Early
	 * Draft Review Page 57 for further information. <br>
	 * The SLEE calls this method to synchronize the state of the SBB entity�s
	 * persistent state with the state of the SBB object. The SBB Developer
	 * should use this method to update the SBB object using the CMP field
	 * accessor methods before its persistent state is synchronized. <br>
	 * This method indicates a transition from state "READY" to "READY" (see
	 * page 52)
	 */
	public void sbbStore() {
		logger.info("CommonSbb: " + this + ": sbbStore() called.");
	}

	/**
	 * implements javax.slee.Sbb Please refer to JSLEE v1.1 Specification, Early
	 * Draft Review Page 67 for further information. <br>
	 * The SLEE invokes the sbbRolledBack callback method after a transaction
	 * used in a SLEE originated invocation has rolled back.
	 */
	public void sbbRolledBack(javax.slee.RolledBackContext rolledBackContext) {
		logger.info("CommonSbb: " + this + ": sbbRolledBack() called.");
	}

	/**
	 * implements javax.slee.Sbb Please refer to JSLEE v1.1 Specification, Early
	 * Draft Review Page 65 for further information. <br>
	 * The SLEE invokes this method after a SLEE originated invocation of a
	 * transactional method of the SBB object returns by throwing a
	 * RuntimeException.
	 */
	public void sbbExceptionThrown(Exception exception, Object obj,
			javax.slee.ActivityContextInterface activityContextInterface) {
		logger.info("CommonSbb: " + this + ": sbbExceptionThrown() called.");
	}

	protected SbbContext getSbbContext() {
		return this.sbbContext;
	}

	protected NullActivityContextInterfaceFactory getNullACIFactory() {
		return this.nullACIFactory;
	}

	protected NullActivityFactory getNullActivityFactory() {
		return this.nullActivityFactory;
	}

	protected SipUtils getSipUtils() {
		return this.sipUtils;
	}

	protected SipProvider getSipProvider() {
		return this.sipProvider;
	}

	protected SipActivityContextInterfaceFactory getSipActivityContextInterfaceFactory() {
		return this.sipActivityContextInterfaceFactory;
	}

	protected CacheUtility getCacheUtility() {
		return this.cache;
	}

	public MgcpActivityContextInterfaceFactory getMgcpActivityContestInterfaceFactory() {
		return mgcpActivityContestInterfaceFactory;
	}

	public JainMgcpProvider getMgcpProvider() {
		return mgcpProvider;
	}
	

}
