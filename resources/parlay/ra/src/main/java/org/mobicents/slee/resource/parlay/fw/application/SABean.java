package org.mobicents.slee.resource.parlay.fw.application;

import org.csapi.fw.fw_application.service_agreement.IpAppServiceAgreementManagement;
import org.csapi.fw.fw_application.service_agreement.IpServiceAgreementManagement;
import org.mobicents.csapi.jr.slee.fw.TerminateServiceAgreementEvent;
import org.mobicents.slee.resource.parlay.fw.ServiceAndToken;
import org.mobicents.slee.resource.parlay.fw.access.TSMBean;


/**
 * This interface represents the framework application service agreement module.
 *
 * The application may use this class to identify which services it wishes to
 * use and sign the corresponding service agreements.
 */
public interface SABean{

    
    void initialise() throws SABeanException;
    
   /**
    * Defines a method used to provide the caller with a string representationof the class.
    */
   String toString();
   
   /**
    * Stores the ServiceAgreementManagement interface to be used by the bean.
    */
   void setServiceAgreementManagement(IpServiceAgreementManagement serviceAgreement);
   
   /**
    * Returns the ServiceAgreementManagement interface.
    */
   IpServiceAgreementManagement getServiceAgreementManagement();
   
   /**
    * Returns the AppServiceAgreementManagement interface.
    */
   IpAppServiceAgreementManagement getAppServiceAgreementManagement();
   
   /**
    * Adds an application listener to the bean.
    */
   void addSABeanListener(SABeanListener listener);
   
   /**
    * Removes the registered application listener.
    */
   void removeSABeanListener(SABeanListener listener);
   
   /**
    * This method terminates the service agreement between the client and the Parlay framework.
    */
   void terminateServiceAgreement(String serviceToken, String terminationText) throws SABeanException;
   
   /**
    * This method selects a service and signs an agreement for it with the Parlay framework.
    */
   ServiceAndToken selectAndSignServiceAgreement(String serviceID, String agreementText) throws SABeanException;
   
    /**
     * Returns an object that can be used as a monitor for signing service 
     * agreements.
     *
     * @return the object reference.
     */
    Object getServiceAgreementMonitor();
    
    String removeSigningAlgorithm(String serviceToken);
    
    void putServiceTokenSigningAlgorithm(String serviceToken, String signingAlgorithm);
    
    /**
     * Called to indicate sign service agreement has been called by the 
     * framework.
     *
     * @param value true if it has been called.
     */
    void setIsAgreementSigned(boolean value);
    
    /**
     * Called to verify signature received from framework
     * 
     * @return boolean
     */
    boolean verifyDigitalSignature(String text, String serviceToken, String signingAlgorithm,byte[] digitalSignature);
    
    /**
     * Called to genearte digital signature of service token and text received from framework
     * 
     * @return digitalSignature
     */
    byte[] generateDigitalSignature(String text, String serviceToken, String signingAlgorithm);
   /**
    * This method cleans up all internal object references with the exception of registered listeners which are the responsibility of the application.
    * The PCP uses this method when the object is no longer to be used.
    */
   void cleanup();
   
    /**
     * Returns the tsmBean.
     * @return TSMBean
     */
    TSMBean getTSMBean();
    
    void fireTerminateServiceAgreement(TerminateServiceAgreementEvent e);
}
