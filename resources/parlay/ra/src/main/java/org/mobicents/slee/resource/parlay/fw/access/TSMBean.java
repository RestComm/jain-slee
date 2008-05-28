package org.mobicents.slee.resource.parlay.fw.access;

import org.csapi.fw.TpDomainID;
import org.csapi.fw.TpProperty;
import org.csapi.fw.fw_access.trust_and_security.IpInitial;
import org.csapi.fw.fw_application.discovery.IpServiceDiscovery;
import org.mobicents.csapi.jr.slee.fw.TerminateAccessEvent;
import org.mobicents.slee.resource.parlay.fw.FwSessionProperties;
import org.mobicents.slee.resource.parlay.fw.application.SABean;
import org.mobicents.slee.resource.parlay.util.corba.ORBHandler;

import EDU.oswego.cs.dl.util.concurrent.Executor;


/**
 *  *  This interface represents the trust and security management Framework services.
 *  <BR>
 *  It is used by clients to perform its part of the mutual
 *  authentication process with the Framework necessary to be allowed to use any
 *  of the other interfaces supported by the Framework. It can then be used to
 *  access the other interfaces. This is a higher level bean which encompasses
 *  all of the interfaces within
 *  the Parlay trust_and_security module. <BR>
 */
public interface TSMBean {

    /**
     * Adds application listener to the bean.
     */
    void addTSMBeanListener(TSMBeanListener listener);

    /**
     * Removes registered application listener.
     */
    void removeTSMBeanListener(TSMBeanListener listener);

    /**
     * This method authenticates a client with the Parlay Framework.
     */
    void authenticate() throws TSMBeanException;

    /**
     *Creates an instance of the ServiceAgreementManagementBean. 
     */
    SABean createSABean() throws TSMBeanException;

    /**
     * This method releases an instance of the ServiceAgreementManagementBean.
     */
    void destroySABean() throws TSMBeanException;

    /**
     * This method ends a client's access session with the Parlay Framework.
     */
    void endAccess(TpProperty[] endAccessProperties) throws TSMBeanException;
    
    ///**
    // * This method ends a client's access session with the Parlay Framework.
    // */
    //void terminateAccess() throws TSMBeanException;

    /**
     * Indicated if an other object is equal to this one.
     */
    boolean equals(Object obj);

//    /**
//     * Returns the Access object used by this bean.
//     */
//    IpAccess getAccess();

    /**
     * Retrieves the client domain ID to be used for checking the HA database.
     */
    String getClientDomain(TpDomainID domainID);

    /**
     * Returns the client domain ID to be used for authentication.
     */
    TpDomainID getClientDomainID();

    /**
     * Returns the client ID (without type) to be used for authentication.
     */
    String getClientID();

    /**
     * Returns the encryption method chosen by the framework during the authentication sequence.
     */
    String getEncryptionMethod();

    /**
     * Returns the Executor of events.
     */
    Executor getEventsQueue();

    /**
     * Returns a previously created instance of the ServiceAgreementManagementBean
     */
    SABean getSABean();

    /**
     * Returns the current state of this bean
     */
    int getState();

    /**
     * Returns a hash code value for the object.
     */
    int hashCode();

    /**
     * This method must be called before attempting to make an Parlay requests via this object.
     */
    void initialize() throws TSMBeanException;

    /**
     * Returns an instance of the ServiceDiscovery interface.
     */
    IpServiceDiscovery obtainDiscoveryInterface() throws TSMBeanException;

    /**
     * Releases an instance of the ServiceDiscovery interface.
     */
    void releaseDiscoveryInterface() throws TSMBeanException;

    /**
     * Stores the client domain id to be used for authentication.
     */
    void setClientDomainID(TpDomainID id);

    /**
     *This method must be called when an application has no more use for this object to ensure a graceful shutdown.
     */
    void shutdown();

    /**
     * Defines a method used to provide the caller with a String represenation of the class.
     */
    String toString();
    /**
     * Returns the Initial object used by this bean.
     *
     * @return the object reference.
     */
    IpInitial getInitial();

    /**
     * This method will clean up all internal object references with the exception
     * of registered listeners which are the responsibility of the application.
     * The PCP will use this when the object is no longer to be used.
     */
    void cleanup();

    /**
     * Returns an object that can be used as a monitor for authentication.
     *
     * @return the object reference.
     */
    Object getAuthenticationMonitor();

    /**
     * Called to indicate to this bean that the framework has indicated
     * successful authentication.
     *
     * @param value true if it successful.
     */
    void setFwAuthenticationSucceeded(boolean value);

    /**
     * Method fireTerminateAccess.
     * @param e
     */
    void fireTerminateAccess(TerminateAccessEvent e);

    /**
     * @return The framework specific properties.
     */
    FwSessionProperties getFwProperties();

    /**
     * @param properties
     */
    void setFwProperties(FwSessionProperties properties);
//
//    /**
//     * @param properties
//     */
//    void setOrbProperties(ORBProperties properties);
    
	/**
	 * @return the ORBHandler for this TSM session.
	 */
	ORBHandler getOrbHandler();

//	/**
//	 * @return The callback
//	 */
//	IpAPILevelAuthentication getApiLevelAuthentication();
//
//	/**
//	 * @return The gateway interface
//	 */
//	IpClientAccess getClientAccess();
//
//	/**
//	 * @return the callback
//	 */
//	IpClientAPILevelAuthentication getClientAPILevelAuthentication() ;
//
//	/**
//	 * @return The gateway interface
//	 */
//	IpServiceDiscovery getServiceDiscovery() ;
}
