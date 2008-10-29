package org.mobicents.slee.resource.parlay;

import javax.slee.ActivityContextInterface;
import javax.slee.FactoryException;
import javax.slee.UnrecognizedActivityException;
import javax.slee.resource.ActivityHandle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mobicents.csapi.jr.slee.TpServiceIdentifier;
import org.mobicents.csapi.jr.slee.cc.gccs.TpCallIdentifier;
import org.mobicents.csapi.jr.slee.cc.mpccs.TpCallLegIdentifier;
import org.mobicents.csapi.jr.slee.cc.mpccs.TpMultiPartyCallIdentifier;
import org.mobicents.csapi.jr.slee.ui.TpUICallIdentifier;
import org.mobicents.csapi.jr.slee.ui.TpUIIdentifier;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.resource.ResourceAdaptorActivityContextInterfaceFactory;
import org.mobicents.slee.resource.SleeActivityHandle;
import org.mobicents.slee.resource.parlay.csapi.jr.ParlayServiceActivityHandle;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.gccs.TpCallActivityHandle;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.TpCallLegActivityHandle;
import org.mobicents.slee.resource.parlay.csapi.jr.cc.mpccs.TpMultiPartyCallActivityHandle;
import org.mobicents.slee.resource.parlay.csapi.jr.ui.TpUIActivityHandle;
import org.mobicents.slee.resource.parlay.csapi.jr.ui.TpUICallActivityHandle;
import org.mobicents.slee.runtime.ActivityContextFactory;
import org.mobicents.slee.runtime.ActivityContextInterfaceImpl;

/**
 *  
 */
public class ParlayActivityContextInterfaceFactoryImpl implements
        ParlayActivityContextInterfaceFactory,
        ResourceAdaptorActivityContextInterfaceFactory {

    private static final String CALLED = ") called.";

    private static final String ACTIVITY_CONTEXT_INTERFACE_ID_IS_NULL = "ParlayActivityContextInterfaceFactoryImpl.getActivityContextInterface(): id is null.";

    private static final String GET_ACTIVITY_CONTEXT_INTERFACE = "ParlayActivityContextInterfaceFactoryImpl.getActivityContextInterface(";

    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory
            .getLog(ParlayActivityContextInterfaceFactoryImpl.class);

    /**
     * @param serviceContainer
     *            the RA container
     * @param name
     *            The RA name
     */
    public ParlayActivityContextInterfaceFactoryImpl(
            SleeContainer serviceContainer, String name) {

        this.serviceContainer = serviceContainer;

        this.raEntityName = name;

        this.jndiName = "java:slee/resources/" + name + "/parlayacif";

        this.activityContextFactory = getActivityContextFactory();
        
        if (logger.isDebugEnabled()) {
            logger
                    .debug("ParlayActivityContextInterfaceFactoryImpl.jndiName = "
                            + jndiName);
        }
    }

    // reference to the SLEE for further usage
    private SleeContainer serviceContainer;

    // the JNDI name of the ActivityContextInterfaceFactory object
    private final String jndiName;

    private ActivityContextFactory activityContextFactory;

    private final String raEntityName;
    
    protected ActivityContextFactory getActivityContextFactory() {

        return serviceContainer
                .getActivityContextFactory();
        
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.ResourceAdaptorActivityContextInterfaceFactory#getJndiName()
     */
    public String getJndiName() {
        return jndiName;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.slee.resource.parlay.ParlayActivityConextInterfaceFactory#getActivityContextInterface(org.mobicents.csapi.jr.slee.cc.mpccs.MultiPartyCallControlManager)
     */
    public ActivityContextInterface getActivityContextInterface(
            TpServiceIdentifier serviceIdentifier) throws NullPointerException,
            UnrecognizedActivityException, FactoryException {

        if (logger.isDebugEnabled()) {
            logger.debug(GET_ACTIVITY_CONTEXT_INTERFACE
                            + serviceIdentifier + CALLED);
        }

        if (serviceIdentifier == null) {
            throw new IllegalArgumentException(
                    ACTIVITY_CONTEXT_INTERFACE_ID_IS_NULL);
        }
        else {
            ParlayServiceActivityHandle activityHandle = new ParlayServiceActivityHandle(
                    serviceIdentifier);

            return getActivityContextInterface(activityHandle);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.slee.resource.parlay.ParlayActivityConextInterfaceFactory#getActivityContextInterface(org.mobicents.csapi.jr.slee.cc.mpccs.MultiPartyCall)
     */
    public ActivityContextInterface getActivityContextInterface(
            TpMultiPartyCallIdentifier call) throws NullPointerException,
            UnrecognizedActivityException, FactoryException {

        if (logger.isDebugEnabled()) {
            logger.debug(GET_ACTIVITY_CONTEXT_INTERFACE
                            + call + CALLED);
        }

        if (call == null) {
            throw new IllegalArgumentException(
                    ACTIVITY_CONTEXT_INTERFACE_ID_IS_NULL);
        }
        else {
            TpMultiPartyCallActivityHandle activityHandle 
            	= new TpMultiPartyCallActivityHandle(call);
            
            return getActivityContextInterface(activityHandle);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.slee.resource.parlay.ParlayActivityConextInterfaceFactory#getActivityContextInterface(org.mobicents.csapi.jr.slee.cc.mpccs.TpCallLegIdentifier)
     */
    public ActivityContextInterface getActivityContextInterface(
            TpCallLegIdentifier callLeg) throws NullPointerException,
            UnrecognizedActivityException, FactoryException {

        if (logger.isDebugEnabled()) {
            logger.debug(GET_ACTIVITY_CONTEXT_INTERFACE
                            + callLeg + CALLED);
        }

        if (callLeg == null) {
            throw new IllegalArgumentException(
                    ACTIVITY_CONTEXT_INTERFACE_ID_IS_NULL);
        }
        else {
            TpCallLegActivityHandle activityHandle 
            	= new TpCallLegActivityHandle(callLeg);
            
            return getActivityContextInterface(activityHandle);
        }
    }

    /**
     * Returns a slee activity context interface for the specified handle.
     * 
     * @param activityHandle
     * @return
     */
    protected ActivityContextInterface getActivityContextInterface(
            ActivityHandle activityHandle) {
        
        SleeActivityHandle sleeActivityHandle = new SleeActivityHandle(
                raEntityName, activityHandle, serviceContainer);

        String activityContextID = activityContextFactory.getActivityContext(
                sleeActivityHandle).getActivityContextId();
        
        return new ActivityContextInterfaceImpl(activityContextID);
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.parlay.ParlayActivityContextInterfaceFactory#getActivityContextInterface(org.mobicents.csapi.jr.slee.cc.gccs.TpCallIdentifier)
     */
    public ActivityContextInterface getActivityContextInterface(TpCallIdentifier call) throws NullPointerException, UnrecognizedActivityException, FactoryException {
        if (logger.isDebugEnabled()) {
            logger.debug(GET_ACTIVITY_CONTEXT_INTERFACE
                            + call + CALLED);
        }

        if (call == null) {
            throw new IllegalArgumentException(
                    ACTIVITY_CONTEXT_INTERFACE_ID_IS_NULL);
        }

        TpCallActivityHandle activityHandle 
        	= new TpCallActivityHandle(call);
        
        return getActivityContextInterface(activityHandle);

    }

    public ActivityContextInterface getActivityContextInterface(TpUIIdentifier ui) throws NullPointerException, UnrecognizedActivityException, FactoryException {
        if (logger.isDebugEnabled()) {
            logger.debug(GET_ACTIVITY_CONTEXT_INTERFACE
                            + ui + CALLED);
        }

        if (ui == null) {
            throw new IllegalArgumentException(
                    ACTIVITY_CONTEXT_INTERFACE_ID_IS_NULL);
        }

        TpUIActivityHandle activityHandle 
            = new TpUIActivityHandle(ui);
        
        return getActivityContextInterface(activityHandle);
    }

    public ActivityContextInterface getActivityContextInterface(TpUICallIdentifier uiCall) throws NullPointerException, UnrecognizedActivityException, FactoryException {
        if (logger.isDebugEnabled()) {
            logger.debug(GET_ACTIVITY_CONTEXT_INTERFACE
                            + uiCall + CALLED);
        }

        if (uiCall == null) {
            throw new IllegalArgumentException(
                    ACTIVITY_CONTEXT_INTERFACE_ID_IS_NULL);
        }

        TpUICallActivityHandle activityHandle 
            = new TpUICallActivityHandle(uiCall);
        
        return getActivityContextInterface(activityHandle);
    }

}