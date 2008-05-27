/***************************************************
 *                                                 *
 *  Mobicents: The Open Source JSLEE Platform      *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 ***************************************************/
/*
 * FooProfileManagement.java
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
public interface FooProfileManagement {
    //accessor methods declared in FooProfileCMP
    public ProfileID getQOSProfile();
    public void setQOSProfile(ProfileID profileID);
    
    //substitute for getDate et setDate
    public String getMMDDYYDate();
    public void setMMDDYYDate(String date);
    
    //
    public String getSubscriberName();
    public void setSubscriberName(String subscriberName);
}
