/**
 * 
 */
package org.mobicents.slee.container.management.jmx;

import javax.management.MBeanServer;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;

import org.jboss.system.ServiceMBeanSupport;
import org.mobicents.slee.container.SleeContainer;

/**
 * @author martins
 *
 */
public abstract class MobicentsServiceMBeanSupport extends ServiceMBeanSupport {

	private ObjectName objectName;
	
	private final SleeContainer sleeContainer;
	
	/**
	 * 
	 */
	public MobicentsServiceMBeanSupport(Class<?> type) throws NotCompliantMBeanException {
		this(null,type);
	}
	
	/**
	 * 
	 */
	public MobicentsServiceMBeanSupport(SleeContainer sleeContainer, Class<?> type) throws NotCompliantMBeanException {
		super(type);
		this.sleeContainer = sleeContainer;
	}
	
	/**
	 * Retrieves 
	 * @return the sleeContainer
	 */
	public SleeContainer getSleeContainer() {
		return sleeContainer;
	}
	
	/**
	 * 
	 * @return
	 */
	public ObjectName getObjectName() {
		return objectName;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.jboss.system.ServiceMBeanSupport#preRegister(javax.management.MBeanServer, javax.management.ObjectName)
	 */
	public ObjectName preRegister(MBeanServer mbs, ObjectName oname) throws Exception {
		this.objectName = oname;
		return oname;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.management.MBeanRegistration#postRegister(java.lang.Boolean)
	 */
	@Override
	public void postRegister(Boolean arg0) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.management.MBeanRegistration#preDeregister()
	 */
	@Override
	public void preDeregister() throws Exception {
		// TODO Auto-generated method stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.management.MBeanRegistration#postDeregister()
	 */
	@Override
	public void postDeregister() {
		// TODO Auto-generated method stub
	}
	
}
