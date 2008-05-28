package org.mobicents.slee.resource.parlay.fw.access;

/**
 * This interface defines the public constants used by the TSMBean.
 * These constants are public to provide users visibility of the operations
 * supported on this bean.
 */
public interface TSMBeanConstants {

    /**
     * Passed to obtainInterface method for returning a Discovery interface.
     *
     * Value = "P_DISCOVERY"
     */
    String P_DISCOVERY = "P_DISCOVERY";
    
    /**
     * Passed to obtainInterface method for returning a Service Agreement 
     * Management interface.
     *
     * Value = "P_SERVICE_AGREEMENT_MANAGEMENT"
     */
    String P_SERVICE_AGREEMENT_MANAGEMENT = "P_SERVICE_AGREEMENT_MANAGEMENT";
    
    /**
     * Access type specified during requestAccess().
     *
     * Value = "P_OSA_ACCESS"
     */
    String ACCESS_TYPE = "P_OSA_ACCESS";
    
    /**
     * Authentication type specified during initiateAuthentication().
     *
     * Value = "P_OSA_AUTHENTICATION"
     */
    String AUTHENTICATION_TYPE = "P_OSA_AUTHENTICATION";
    
    /**
     * Value included in authentication capability list for NULL authentication.
     *
     * Value = "NULL"
     */
    String NULL_AUTH = "NULL";
    
    /**
     * Value included in authentication capability list for P_RSA_1024 authentication.
     *
     * Value = "P_RSA_1024"
     */
    String P_RSA_1024 = "P_RSA_1024";
    
    /**
     * Value included in authentication capability list for P_RSA_512 authentication.
     *
     * Value = "P_RSA_512"
     */
    String P_RSA_512 = "P_RSA_512";
    
    /**
     * The idle state.
     *
     * This state indicates the TSMBean has not yet authenticated the application.
     *
     * Value = 0
     */
    int IDLE_STATE = 0;
    
    /**
     * The active state.
     *
     * This state indicates the TSMBean has authenticated the application.
     *
     * Value = 1
     */
    int ACTIVE_STATE = 1;
    
    /**
     * The invalid state.
     *
     * This state indicates the TSMBean has ended the application access session
     * and may no longer be used.
     *
     * Value = 2
     */
    int INVALID_STATE = 2;
    
    /**
     * The intitialized state.
     *
     * This state indicates the TSMBean has initialised and looked up the 
     * gateway's initial point of contact.
     *
     * Value = 3
     */
    int INITIALIZED_STATE = 3;
}
