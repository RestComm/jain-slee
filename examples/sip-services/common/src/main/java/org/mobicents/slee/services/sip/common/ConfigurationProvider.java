package org.mobicents.slee.services.sip.common;

import javax.management.MBeanServer;
import javax.management.ObjectInstance;
import javax.management.ObjectName;

import org.mobicents.slee.container.SleeContainer;

public class ConfigurationProvider {

	/**
	 * Creates a copy of current proxy configuration MBean - its values are copied, so for we can have its image
	 * which can be used during whole call. Subsequent calls can return different copy.
	 * @return
	 */
	public static Object getCopy(String prefix,String name) {
		return getCopy(prefix+name);
	}
	
	public static Object getCopy(String mBeanName) {
			
		MBeanServer mbs = SleeContainer.lookupFromJndi().getMBeanServer();
		try {
			ObjectName on=new ObjectName(mBeanName);
			ObjectInstance oi=mbs.getObjectInstance(on);
			Object o=mbs.invoke(on, "clone", null, null);
			return o;
		} catch (Exception e) {
			e.printStackTrace();
		}		
		
		return null;
	}
}
