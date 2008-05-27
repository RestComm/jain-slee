/***************************************************
 *                                                 *
 *  Mobicents: The Open Source JSLEE Platform      *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 ***************************************************/
/*
 * FooProfileManagementImpl.java
 * 
 * Created on Oct 3, 2004
 *
 */
package org.mobicents.slee.container.profile;

import java.util.Date;

import javax.slee.profile.ProfileManagement;
import javax.slee.profile.ProfileVerificationException;

/**
 * @author DERUELLE Jean <a href="mailto:jean.deruelle@gmail.com">jean.deruelle@gmail.com</a>
 *
 */
public abstract class FooProfileManagementImpl implements ProfileManagement,
        FooProfileCMP, FooProfileManagement {

    /**
     * 
     */
    public FooProfileManagementImpl() {}

    /* (non-Javadoc)
     * @see javax.slee.profile.ProfileManagement#profileInitialize()
     */
    public void profileInitialize() {
        setSubscriberName("eve-marie");
    }

    /* (non-Javadoc)
     * @see javax.slee.profile.ProfileManagement#profileLoad()
     */
    public void profileLoad() {        
    }

    /* (non-Javadoc)
     * @see javax.slee.profile.ProfileManagement#profileStore()
     */
    public void profileStore() {
    }

    /* (non-Javadoc)
     * @see javax.slee.profile.ProfileManagement#profileVerify()
     */
    public void profileVerify() throws ProfileVerificationException {
    }
    
    public abstract boolean isProfileDirty();

    /* (non-Javadoc)
     * @see org.mobicents.slee.container.profile.FooProfileManagement#getMMDDYYDate()
     */
    public String getMMDDYYDate() {
        // TODO Auto-generated method stub
        return new Date(getDate()).toString();
    }

    public abstract long getDate() ;

    /* (non-Javadoc)
     * @see org.mobicents.slee.container.profile.FooProfileManagement#setMMDDYYDate(java.lang.String)
     */
    public void setMMDDYYDate(String date) {
        // TODO Auto-generated method stub
        setDate(new Date().getTime());
    }

}
