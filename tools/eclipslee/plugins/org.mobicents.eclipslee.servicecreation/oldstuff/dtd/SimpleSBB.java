/*
 * Created on May 4, 2005
 * 
 * The Open SLEE Project
 * 
 * A SLEE for the People
 * 
 * The source code contained in this file is in in the public domain.          
 * It can be used in any project or product without prior permission, 	      
 * license or royalty payments. There is no claim of correctness and
 * NO WARRANTY OF ANY KIND provided with this code.
 */
package gov.nist.slee.examples;

import javax.sip.RequestEvent;
import javax.slee.ActivityContextInterface;
import javax.slee.CreateException;
import javax.slee.RolledBackContext;
import javax.slee.Sbb;
import javax.slee.SbbContext;

/**
 * 
 * TODO Class Description
 * 
 * @author F.Moggia
 */
public abstract class SimpleSBB implements Sbb {
    public void onRegisterEvent(RequestEvent event, ActivityContextInterface ac) {
        System.out.println("RECEIVED REGISTER EVENT");
    }
    
    
    public void setSbbContext(SbbContext arg0) {
        // TODO Auto-generated method stub
        
    }

    public void unsetSbbContext() {
        // TODO Auto-generated method stub
        
    }

    public void sbbCreate() throws CreateException {
        // TODO Auto-generated method stub
        
    }

    public void sbbPostCreate() throws CreateException {
        // TODO Auto-generated method stub
        
    }

    public void sbbActivate() {
        // TODO Auto-generated method stub
        
    }

    public void sbbPassivate() {
        // TODO Auto-generated method stub
        
    }

    public void sbbLoad() {
        // TODO Auto-generated method stub
        
    }

    public void sbbStore() {
        // TODO Auto-generated method stub
        
    }

    public void sbbRemove() {
        // TODO Auto-generated method stub
        
    }

    public void sbbExceptionThrown(Exception arg0, Object arg1, ActivityContextInterface arg2) {
        // TODO Auto-generated method stub
        
    }

    public void sbbRolledBack(RolledBackContext arg0) {
        // TODO Auto-generated method stub
        
    }

}
