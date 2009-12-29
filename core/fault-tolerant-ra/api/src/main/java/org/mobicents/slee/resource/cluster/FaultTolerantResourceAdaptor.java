/**
 * 
 */
package org.mobicents.slee.resource.cluster;

import java.io.Serializable;

import javax.slee.resource.ResourceAdaptor;

/**
 * 
 * Abstract class for a fault tolerant JAIN SLEE 1.1 RA
 * 
 * @author martins
 * 
 */
public interface FaultTolerantResourceAdaptor<K extends Serializable, V extends Serializable>
		extends ResourceAdaptor {

	/**
	 * Callback from SLEE when the local RA was selected to recover the state
	 * for a replicated data key, which was owned by a cluster member that failed
	 * 
	 * @param key
	 */
	public void failOver(K key);

	/**
	 * Invoked by SLEE to provide the fault tolerant context.
	 * 
	 * @param context
	 */
	public void setFaultTolerantResourceAdaptorContext(
			FaultTolerantResourceAdaptorContext<K, V> context);

	/**
	 * Invoked by SLEE to indicate that any references to the fault tolerant
	 * context should be removed.
	 */
	public void unsetFaultTolerantResourceAdaptorContext();
}
