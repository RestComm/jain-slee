/*
 * ***************************************************
 *                                                 *
 *  Mobicents: The Open Source JSLEE Platform      *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 ***************************************************
 */
package org.mobicents.slee.resource.lab.ra;

import javax.slee.resource.ActivityHandle;

/**
 * RAFActivityHandle represents the specific activity handle for RAFrame
 * resource adaptor. According to JSLEE 1.1 specification, "an activity handle
 * parameter is a distributable reference to an activity. The activity handle is
 * used by the SLEE to determine the activity context that represents the
 * underlying activity in the resource for an event to be delivered on. The
 * resource adaptor defines the activity handle and activity assocation however
 * this association must be a 1:1 relationship." <br>
 * In the case of RAFrame, the activity handle is bound to the "dialog" which is
 * indicated by the identifier (id) in incoming protocol messages. So, the id
 * itself is the activity handle and is wrapped and represented through
 * RAFActivityHandle. <br>
 * For further information, please refer to JSLEE v1.1 Specification, Early
 * Draft Review Page 314.
 * 
 * @author Michael Maretzke
 * @author amit bhayani
 */
public class MessageActivityHandle implements ActivityHandle {
	private String handle = null;

	public MessageActivityHandle(String id) {
		this.handle = id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((handle == null) ? 0 : handle.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final MessageActivityHandle other = (MessageActivityHandle) obj;
		if (handle == null) {
			if (other.handle != null)
				return false;
		} else if (!handle.equals(other.handle))
			return false;
		return true;
	}

}
