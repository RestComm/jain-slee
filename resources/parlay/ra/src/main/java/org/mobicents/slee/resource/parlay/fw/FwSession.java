package org.mobicents.slee.resource.parlay.fw;

import org.csapi.fw.TpProperty;
import org.csapi.fw.TpServiceProperty;
import org.omg.PortableServer.POA;

/**
 * Forms the API for the Provider to the Parlay Framework.
 * 
 * The implementation of this class will abstract all detailed framework
 * interactions from the core RA implementation.
 */
public interface FwSession {

    /**
     * Initialises FwSession, only use when created with default constructor.
     * 
     * @throws FwSessionException
     */
    void init() throws FwSessionException;

    /**
     * Adds an application listener.
     */
    void addFwSessionListener(FwSessionListener listener);

    /**
     * Removes an application listener.
     */
    void removeFwSessionListener(FwSessionListener listener);

    /**
     * This method authenticates a client with the Parlay framework.
     */
    void authenticate() throws FwSessionException;

    /**
     * This method ends a client's access session with the Parlay Framework
     * 
     * @param endAccessProperties
     *            properties to be passed to gateway framework on endAccess.
     */
    void endAccess(TpProperty[] endAccessProperties) throws FwSessionException;

    /**
     * Indicates if another object is equal to this one.
     */
    boolean equals(Object obj);

    /**
     * Returns a hash code value for the object
     */
    int hashCode();

    /**
     * Releses the specified Service.
     */
    void releaseService(ServiceAndToken service) throws FwSessionException;

    /**
     * Returns a reference to a Service interface.
     * 
     * @param serviceTypeName
     * @param serviceProperties
     * 
     * @return a service and token for the selected service
     */
    ServiceAndToken getService(String serviceTypeName,
            TpServiceProperty[] serviceProperties) throws FwSessionException;

    /**
     * This method will shutdown all resources used by this object.
     */
    void shutdown();

    /**
     * Returns a reference to the ORB used by this FwSession.
     * 
     * @return
     */
    org.omg.CORBA.ORB getORB();

    /**
     * Defines a method used to provide the caller with a string
     * representationof the class.
     */
    String toString();

    /**
     * @return
     */
    FwSessionProperties getFwSessionProperties();

    /**
     * Returns the root poa
     * 
     * @return
     */
    POA getRootPOA();

}