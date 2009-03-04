/***************************************************
 *                                                 *
 *  Mobicents: The Open Source JSLEE Platform      *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 ***************************************************/
/*
 * DefaultUsageInterceptor.java
 * 
 * Created on Jul 26, 2004
 *
 */
package org.mobicents.slee.container.deployment.interceptors;

import java.lang.reflect.Method;

import javax.slee.SLEEException;
import javax.slee.profile.ProfileID;
import javax.slee.profile.UnrecognizedProfileNameException;
import javax.slee.profile.UnrecognizedProfileTableNameException;
import javax.transaction.SystemException;

import org.jboss.logging.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.sbb.MGetProfileCMPMethod;
import org.mobicents.slee.container.profile.SleeProfileManager;
import org.mobicents.slee.runtime.sbb.SbbObject;
import org.mobicents.slee.runtime.sbb.SbbObjectState;
import org.mobicents.slee.runtime.sbbentity.SbbEntity;

/**
 * @author Ivelin Ivanov
 *
 */
public class DefaultSBBProfileCMPInterceptor implements SBBProfileCMPInterceptor {
    SbbEntity sbbEntity=null;
    
    private static Logger logger = Logger.getLogger(DefaultSBBProfileCMPInterceptor.class);
    
    /* (non-Javadoc)
     * @see java.lang.reflect.InvocationHandler#invoke(java.lang.Object, java.lang.reflect.Method, java.lang.Object[])
     */
    public Object invoke(Object proxy, Method method, Object[] args)
            throws Throwable {
        
    	
    	MGetProfileCMPMethod mGetProfileCMPMethod = sbbEntity.getSbbComponent().getDescriptor().getGetProfileCMPMethods().get(method.getName());
        if (mGetProfileCMPMethod == null) throw new AbstractMethodError ("Profile CMP Method not found");
        SbbObject sbbObject = sbbEntity.getSbbObject();
        
        if ( sbbObject.getState() != SbbObjectState.READY) {
            logger.error("InvalidState ! " + sbbObject.getState());
            throw new IllegalStateException("InvalidState! " + sbbObject.getState());
        }
        return callGetProfileMethod(mGetProfileCMPMethod,(ProfileID)args[0]);                
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.container.deployment.interceptors.ChildRelationInterceptor#getSbbEntity()
     */
    public SbbEntity getSbbEntity() {
        // TODO Auto-generated method stub
        return sbbEntity;
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.container.deployment.interceptors.ChildRelationInterceptor#setSbbEntity(gov.nist.slee.runtime.SbbEntity)
     */
    public void setSbbEntity(SbbEntity sbbEntity) {
        // TODO Auto-generated method stub
        this.sbbEntity=sbbEntity;
    }
    
    /**
     * Get an instance of a profile specified by the profileID given in parameter
     * @param method
     * @param profileID
     * @return
     * @throws SystemException
     */
    private Object callGetProfileMethod(MGetProfileCMPMethod mGetProfileCMPMethod, ProfileID profileID) 
    	throws UnrecognizedProfileTableNameException, UnrecognizedProfileNameException, SystemException{

        SleeProfileManager sleeProfileManager=SleeContainer.lookupFromJndi().getSleeProfileManager();
        
        try {
            if(sleeProfileManager.findProfileSpecId(profileID.getProfileTableName())==null)
                throw new UnrecognizedProfileTableNameException();
        } catch (SystemException e) {
            throw new SLEEException("low-level failure");
        }

        if(sleeProfileManager.findProfileMBean(profileID.getProfileTableName(),profileID.getProfileName())==null)
                throw new UnrecognizedProfileNameException();

        //TODO call this method under a valid transaction context as defined in the spec
        return sleeProfileManager.getSbbCMPProfile(profileID);        
    }
    
}
