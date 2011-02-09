package org.mobicents.slee.runtime.facilities.nullactivity;

import javax.slee.ActivityContextInterface;
import javax.slee.FactoryException;
import javax.slee.TransactionRequiredLocalException;
import javax.slee.UnrecognizedActivityException;
import javax.slee.management.SleeState;
import javax.slee.nullactivity.NullActivity;

import org.mobicents.slee.container.AbstractSleeContainerModule;
import org.mobicents.slee.container.activity.ActivityContext;
import org.mobicents.slee.container.activity.ActivityContextHandle;
import org.mobicents.slee.container.facilities.nullactivity.NullActivityContextInterfaceFactory;
import org.mobicents.slee.container.util.JndiRegistrationManager;

/**
 *Implementation of null activity context interface.
 *
 *@author M. Ranganathan
 *@author martins
 */
public class NullActivityContextInterfaceFactoryImpl extends AbstractSleeContainerModule implements
        NullActivityContextInterfaceFactory {

	/* (non-Javadoc)
	 * @see org.mobicents.slee.core.AbstractSleeContainerModule#sleeStarting()
	 */
	@Override
	public void sleeStarting() {
		JndiRegistrationManager.registerWithJndi("slee/nullactivity",
				"nullactivitycontextinterfacefactory",
				this);
	}
	
    /* (non-Javadoc)
     * @see javax.slee.nullactivity.NullActivityContextInterfaceFactory#getActivityContextInterface(javax.slee.nullactivity.NullActivity)
     */
    public ActivityContextInterface getActivityContextInterface(
            NullActivity nullActivity) throws NullPointerException,
            TransactionRequiredLocalException, UnrecognizedActivityException,
            FactoryException {
     
        if (! (nullActivity instanceof NullActivityImpl)) 
            throw new UnrecognizedActivityException ("unrecognized activity");
        
        if (nullActivity == null ) 
            throw new NullPointerException ("null NullActivity ! huh!!");
        
        if ( sleeContainer.getSleeState() == SleeState.STOPPING) {
            return null;
            
        }
        NullActivityImpl nullActivityImpl = (NullActivityImpl) nullActivity;
        ActivityContextHandle ach = new NullActivityContextHandle(nullActivityImpl.getHandle());
        ActivityContext ac = sleeContainer.getActivityContextFactory().getActivityContext(ach);
        if (ac == null) {
        	throw new UnrecognizedActivityException(nullActivity);
        }
        return ac.getActivityContextInterface();
        
    }

}

