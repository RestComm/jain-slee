/**
 * 
 */
package org.mobicents.slee.resource.cluster;

import java.io.Serializable;

import javax.slee.resource.ActivityHandle;

/**
 * A {@link ReplicatedData} that is replicated in the SLEE cluster and its local
 * resources require fail over callbacks.
 * 
 * @author martins
 * 
 */
public interface ReplicatedDataWithFailover<K extends Serializable & ActivityHandle, V extends Serializable>
		extends ReplicatedData<K, V> {

}
