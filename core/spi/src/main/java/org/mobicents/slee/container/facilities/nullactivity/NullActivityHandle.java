/**
 * 
 */
package org.mobicents.slee.container.facilities.nullactivity;

import java.io.Externalizable;

import javax.slee.connection.ExternalActivityHandle;
import javax.slee.resource.ActivityHandle;

import org.mobicents.slee.container.activity.ActivityContextHandle;

/**
 * @author martins
 *
 */
public interface NullActivityHandle extends ExternalActivityHandle,ActivityHandle,Externalizable {

	public ActivityContextHandle getActivityContextHandle();
	
}
