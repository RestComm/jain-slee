package org.mobicents.slee.container.component.validator.sbb.abstracts.localinterface;

import java.io.Serializable;

import javax.naming.Context;
import javax.slee.ActivityContextInterface;
import javax.slee.NoSuchObjectLocalException;
import javax.slee.RolledBackContext;
import javax.slee.SLEEException;
import javax.slee.SbbContext;
import javax.slee.SbbLocalObject;
import javax.slee.TransactionRequiredLocalException;
import javax.slee.TransactionRolledbackLocalException;



public abstract  class SbbConstraintsSbbLocalInterfaceWrongThrowsSbb implements javax.slee.Sbb{

	/**
	 * Called when an sbb object is instantied and enters the pooled state.
	 */
	public void setSbbContext(SbbContext context) {
	
	}

	/*
	 * Init the connection and retrieve data when the service is activated by SLEE
	 */
	public void onServiceStartedEvent(
			javax.slee.serviceactivity.ServiceStartedEvent event,
			ActivityContextInterface aci) {
	
	}
	
	public void unsetSbbContext() {
	
	}

	public void sbbCreate() throws javax.slee.CreateException {
	
	}

	public void sbbPostCreate() throws javax.slee.CreateException {
		
	}

	public void sbbActivate() {
		
	}

	public void sbbPassivate() {
		
	}

	public void sbbRemove() {
		
	}

	public void sbbLoad() {
		
	}

	public void sbbStore() {
		
	}

	public void sbbExceptionThrown(Exception exception, Object event,
			ActivityContextInterface activity) {
		
	}

	public void sbbRolledBack(RolledBackContext sbbRolledBack) {
		
	}

	protected SbbContext getSbbContext() {
		return null;
	}

	public void makeSomeStupidThing(String paramOne, int makeCounterParamTwo)
			throws Error {
		// TODO Auto-generated method stub
		
	}

	public int makeSomeThingDifferent(Serializable ser, SbbLocalObject local)
			throws IllegalStateException {
		// TODO Auto-generated method stub
		return 0;
	}

	public int makeSomeThingDifferentGood(Serializable ser, SbbLocalObject local)
			throws IllegalArgumentException, IllegalStateException {
		// TODO Auto-generated method stub
		return 0;
	}



}