package org.mobicents.slee.container.rmi;

import java.io.Serializable;

/**
 * 
 * @author amit.bhayani
 *
 */
public interface RMIProxy extends Serializable {
	static final long serialVersionUID = 3106067731196293020L;

	public boolean isLocal();
}
