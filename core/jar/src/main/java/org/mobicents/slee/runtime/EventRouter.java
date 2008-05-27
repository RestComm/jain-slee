
/*
 *
 * The source code contained in this file is in in the public domain.          
 * It can be used in any project or product without prior permission, 	      
 * license or royalty payments. There is no claim of correctness and
 * NO WARRANTY OF ANY KIND provided with this code.
 *
 */

package org.mobicents.slee.runtime;


/**
 * Interface for Event Router from Appendix B.12 of the SLEE spec, supporting
 * the execution of tasks serialized with routing of events on a activity.
 * 
 * @author M. Ranganathan
 * 
 */
public interface EventRouter {


	/**
	 * Through this method SLEE can serialize a runnable task submiting it to
	 * the executor assigned to the activity, if there is such executor. If
	 * there is no executor assigned, because no event has been routed on the
	 * activity, or because it alreasy ended, pickups the next executor
	 * available.
	 */
	public void serializeTaskForActivity(Runnable r, Object activity);
    
	public void routeEvent(DeferredEvent de);
	
	
}
