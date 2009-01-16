/*
 * Created on Mar 5, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.mobicents.slee.runtime.eventrouter;

/**
 * 
 * This class represents the Invocation State of an sbb object
 * It is used in rollback handling to determine what to do since
 * this depends on whether an sbb object method invocation was happening
 * at the time the exception was thrown
 * 
 * @author Tim
 * @author martins
 *
 * 
 */
public enum SbbInvocationState {
	
	NOT_INVOKING, INVOKING_SBB_CREATE, INVOKING_SBB_POSTCREATE, INVOKING_SBB_LOAD, INVOKING_SBB_STORE, INVOKING_EVENT_HANDLER
}
