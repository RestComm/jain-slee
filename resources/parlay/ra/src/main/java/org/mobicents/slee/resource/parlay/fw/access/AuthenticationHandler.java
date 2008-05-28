package org.mobicents.slee.resource.parlay.fw.access;

import org.csapi.IpInterface;
import org.csapi.fw.TpAuthDomain;
import org.csapi.fw.fw_access.trust_and_security.IpAccess;

/**
 * Defines a suite of utility operations for the TSMBean's
 * authentication process.
 */
public interface AuthenticationHandler {
    /**
     *  Calls Initial.initiateAuthentication() using the Initial interface held
     *  by the TSMBean.
     */
    void initiateAuthentication(TpAuthDomain clientAuthDomain)
        throws TSMBeanException;
    
    /**
     *  Calls Initial.initiateAuthenticationWithVersion() using the Initial interface held
     *  by the TSMBean.
     */
    void initiateAuthenticationWithVersion(TpAuthDomain clientAuthDomain, String version)
        throws TSMBeanException;
        
    /**
     *  Calls Authentication.selectEncryptionMethod() using the Authentication 
     *  interface held by the TSMBean.
     *
     *  @param authCapabilityList : the clients auth capability list.
     *
     *  @return the framework's chosen authentication method.
     */
    String selectEncryptionMethod(String authCapabilityList)
        throws TSMBeanException;
        
    /**
     *  Calls Authentication.selectAuthenticationMechanism() using the Authentication 
     *  interface held by the TSMBean.
     *
     *  @param authMechanismList: the clients auth mechanism list.
     *
     *  @return the framework's chosen authentication method.
     */
    String selectAuthenticationMechanism(String authMechanismList)
        throws TSMBeanException;
    
    /**
     *  Calls Authentication.authenticate() using the Authentication 
     *  interface held by the TSMBean.
     *
     *  @param encryptionMethod: the framework's chosen authentication method.
     */
    void authenticate(String encryptionMethod) throws TSMBeanException;
    
    
    /**
     * Calls Authentication.authenticate() using the Authentication 
     * interface held by the TSMBean.
     *
     * @param authMechanism: the framework's chosen authentication method.
     */
    void challenge(String authMechanism) throws TSMBeanException;
    
    /**
     *  Calls Authentication.authenticationSucceeded() using the Authentication 
     *  interface held by the TSMBean.
     */
    void authenticationSucceeded() throws TSMBeanException;
    
    /**
     * Destroys resources to enable garbage collection.
     */
    void cleanup();
    
    /**
     * Calls Authentication.requestAccess
     * @param clientAccess
     * @return
     * @throws TSMBeanException
     */
    IpAccess requestAccess(IpInterface clientAccess) throws TSMBeanException;
    
    /**
     * Calls Authentication.abortAuthentication
     */
    void abortAuthentication();
        
}
