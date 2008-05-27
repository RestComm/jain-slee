/*
 * Created on Mar 5, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.mobicents.slee.runtime;

/**
 * 
 * This class represents the Invocation State of an sbb object
 * It is used in rollback handling to determine what to do since
 * this depends on whether an sbb object method invocation was happening
 * at the time the exception was thrown
 * 
 * @author Tim
 *
 * 
 */
public class SbbInvocationState {

	private static final int _NOT_INVOKING = 0;
	private static final int _INVOKING_SBB_CREATE = 1;
	private static final int _INVOKING_SBB_POSTCREATE = 2;	
	private static final int _INVOKING_SBB_LOAD = 5;
	private static final int _INVOKING_SBB_STORE = 6;
	private static final int _INVOKING_EVENT_HANDLER = 7;
	
	public static final SbbInvocationState NOT_INVOKING = new SbbInvocationState(_NOT_INVOKING);
	public static final SbbInvocationState INVOKING_SBB_CREATE = new SbbInvocationState(_INVOKING_SBB_CREATE);
	public static final SbbInvocationState INVOKING_SBB_POSTCREATE = new SbbInvocationState(_INVOKING_SBB_POSTCREATE);
	public static final SbbInvocationState INVOKING_SBB_LOAD = new SbbInvocationState(_INVOKING_SBB_LOAD);
	public static final SbbInvocationState INVOKING_SBB_STORE = new SbbInvocationState(_INVOKING_SBB_STORE);
	public static final SbbInvocationState INVOKING_EVENT_HANDLER = new SbbInvocationState(_INVOKING_EVENT_HANDLER);
	
	private int state;
	
	public int getState() { return state; }
	
	private SbbInvocationState(int state) {	this.state = state;	}
	
	public boolean equals(Object obj) {
		if (obj != null && obj.getClass() == this.getClass()) {
    		return ((SbbInvocationState)obj).state == this.state;
    	}
    	else {
    		return false;
    	}       
	}
	
	public int hashCode() { return state; }
}
