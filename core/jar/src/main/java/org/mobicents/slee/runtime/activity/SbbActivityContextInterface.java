package org.mobicents.slee.runtime.activity;

import javax.slee.SLEEException;
import javax.slee.SbbLocalObject;
import javax.slee.TransactionRequiredLocalException;
import javax.slee.TransactionRolledbackLocalException;

/**
 * Base class code for a concrete implementation of {@link javax.slee.ActivityContextInterface} by an sbb.
 * @author martins
 *
 */
public class SbbActivityContextInterface implements ActivityContextInterface {

	private final ActivityContextInterfaceImpl aciImpl;

	public SbbActivityContextInterface(ActivityContextInterfaceImpl aciImpl) {
		this.aciImpl = aciImpl;
	}

	public ActivityContextHandle getActivityContextHandle() {
		return aciImpl.getActivityContextHandle();
	}

	public ActivityContextInterfaceImpl getAciImpl() {
		return aciImpl;
	}
	
	@Override
    public int hashCode() {    	
    	return aciImpl.hashCode();
    }
    
    @Override
    public boolean equals(Object obj) {
    	if (obj != null && obj instanceof SbbActivityContextInterface) {
    		return ((SbbActivityContextInterface)obj).aciImpl.equals(this.aciImpl);
    	}
    	else {
    		return false;
    	}
    }

	public void attach(SbbLocalObject arg0) throws NullPointerException,
			TransactionRequiredLocalException,
			TransactionRolledbackLocalException, SLEEException {
		aciImpl.attach(arg0);	
	}

	public void detach(SbbLocalObject arg0) throws NullPointerException,
			TransactionRequiredLocalException,
			TransactionRolledbackLocalException, SLEEException {
		aciImpl.detach(arg0);
	}

	public Object getActivity() throws TransactionRequiredLocalException,
			SLEEException {		
		return aciImpl.getActivity();
	}

	public boolean isEnding() throws TransactionRequiredLocalException,
			SLEEException {
		return aciImpl.isEnding();
	}	
	
}
