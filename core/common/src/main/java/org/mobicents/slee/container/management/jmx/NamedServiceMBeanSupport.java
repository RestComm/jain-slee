package org.mobicents.slee.container.management.jmx;

import javax.management.MBeanServer;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;

import org.jboss.system.ServiceMBeanSupport;

public abstract class NamedServiceMBeanSupport extends ServiceMBeanSupport {

	private ObjectName objectName;

	/**
	 * 
	 */
	public NamedServiceMBeanSupport(Class<?> type)
			throws NotCompliantMBeanException {
		super(type);
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
	 * 
	 * @see
	 * org.jboss.system.ServiceMBeanSupport#preRegister(javax.management.MBeanServer
	 * , javax.management.ObjectName)
	 */
	@Override
	public ObjectName preRegister(MBeanServer mbs, ObjectName oname)
			throws Exception {
		this.objectName = oname;
		return oname;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.management.MBeanRegistration#postRegister(java.lang.Boolean)
	 */
	@Override
	public void postRegister(Boolean arg0) {}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.management.MBeanRegistration#preDeregister()
	 */
	@Override
	public void preDeregister() throws Exception {}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.management.MBeanRegistration#postDeregister()
	 */
	@Override
	public void postDeregister() {}
}
