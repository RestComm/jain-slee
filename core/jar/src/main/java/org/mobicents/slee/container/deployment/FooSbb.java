/***************************************************
 *                                                 *
 *  Mobicents: The Open Source JSLEE Platform      *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 ***************************************************/
/*
 * FooBar.java
 * 
 * Created on Aug 1, 2004
 *
 */
package org.mobicents.slee.container.deployment;

import org.mobicents.slee.container.profile.FooProfileCMP;

import java.util.Date;

import javax.slee.ActivityContextInterface;
import javax.slee.Address;
import javax.slee.ChildRelation;
import javax.slee.CreateException;
import javax.slee.RolledBackContext;
import javax.slee.Sbb;
import javax.slee.SbbContext;
import javax.slee.profile.ProfileID;
import javax.slee.profile.UnrecognizedProfileNameException;
import javax.slee.profile.UnrecognizedProfileTableNameException;

/**
 * Dummy SBB used as a base for dynamic runtime class generation.
 * 
 * @author DERUELLE Jean <a href="mailto:jean.deruelle@gmail.com">jean.deruelle@gmail.com</a>
 *
 */
public abstract class FooSbb implements Sbb {

    /* accessor methods for counter CMP field */
    public abstract int getCounter();
    public abstract void setCounter(int value);
    /* accessor methods for counterLastUpdated CMP field */
    public abstract Date getCounterLastUpdated();
    public abstract void setCounterLastUpdated(Date value);
    /* accessor methods for FooSbbLocalObject CMP field */
    public abstract FooSbbLocalObject getPeerFooSbb();
    public abstract void setPeerFooSbb(FooSbbLocalObject value);
    
    public abstract void fireStartEvent(FooEvent event,
            ActivityContextInterface activity,
            Address address);
    /*
     * narrow method
     */
    public abstract FooSbbActivityContextInterface asSbbActivityContextInterface(
            ActivityContextInterface ac);
    /*
     * child relation method
     */
    public abstract ChildRelation getFooSbb();
    
    /*
     * profile cmp method
     */    
    public abstract FooProfileCMP getFooProfileCMP(ProfileID profileID)
    	throws UnrecognizedProfileTableNameException, UnrecognizedProfileNameException;
    
    /**
     * 
     */
    public FooSbb() {
        super();
        // TODO Auto-generated constructor stub
    }

    /* (non-Javadoc)
     * @see javax.slee.Sbb#setSbbContext(javax.slee.SbbContext)
     */
    public void setSbbContext(SbbContext arg0) {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see javax.slee.Sbb#unsetSbbContext()
     */
    public void unsetSbbContext() {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see javax.slee.Sbb#sbbCreate()
     */
    public void sbbCreate() throws CreateException {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see javax.slee.Sbb#sbbPostCreate()
     */
    public void sbbPostCreate() throws CreateException {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see javax.slee.Sbb#sbbActivate()
     */
    public void sbbActivate() {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see javax.slee.Sbb#sbbPassivate()
     */
    public void sbbPassivate() {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see javax.slee.Sbb#sbbLoad()
     */
    public void sbbLoad() {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see javax.slee.Sbb#sbbStore()
     */
    public void sbbStore() {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see javax.slee.Sbb#sbbRemove()
     */
    public void sbbRemove() {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see javax.slee.Sbb#sbbExceptionThrown(java.lang.Exception, java.lang.Object, javax.slee.ActivityContextInterface)
     */
    public void sbbExceptionThrown(Exception arg0, Object arg1,
            ActivityContextInterface arg2) {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see javax.slee.Sbb#sbbRolledBack(javax.slee.RolledBackContext)
     */
    public void sbbRolledBack(RolledBackContext arg0) {
        // TODO Auto-generated method stub

    }

}
