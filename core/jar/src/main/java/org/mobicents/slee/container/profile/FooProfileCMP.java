/***************************************************
 *                                                 *
 *  Mobicents: The Open Source JSLEE Platform      *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 ***************************************************/
/*
 * FooProfileCMP.java
 * 
 * Created on Oct 3, 2004
 *
 */
package org.mobicents.slee.container.profile;

import javax.slee.profile.ProfileID;

/**
 * @author DERUELLE Jean <a href="mailto:jean.deruelle@gmail.com">jean.deruelle@gmail.com</a>
 *
 */
public interface FooProfileCMP {
    public long getDate();
    public void setDate(long date);
    
    public String getSubscriberName();
    public void setSubscriberName(String subscriberName);
    
    //accessor methods to a QOS Profile in a separate QOS Profile
    public ProfileID getQOSProfile();
    public void setQOSProfile(ProfileID profileID);
}
