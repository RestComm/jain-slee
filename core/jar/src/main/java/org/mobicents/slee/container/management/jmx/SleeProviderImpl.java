package org.mobicents.slee.container.management.jmx;

import javax.management.ObjectName;
import javax.slee.SLEEException;
import javax.slee.management.SleeManagementMBean;
import javax.slee.management.SleeProvider;

/**
 * Implementation of the {@link SleeProvider}
 *
 * @author Eduardo Martins
 */
public class SleeProviderImpl implements SleeProvider {

	public ObjectName getSleeManagementMBean() {
		try {
			return new ObjectName(SleeManagementMBean.OBJECT_NAME);
		} catch (Throwable e) {
			throw new SLEEException(e.getMessage(),e);
		}		
	}

}

