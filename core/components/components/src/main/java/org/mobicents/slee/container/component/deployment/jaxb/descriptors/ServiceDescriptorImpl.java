package org.mobicents.slee.container.component.deployment.jaxb.descriptors;

import javax.slee.EventTypeID;
import javax.slee.SbbID;
import javax.slee.ServiceID;
import javax.slee.management.DeploymentException;

import org.mobicents.slee.container.component.deployment.jaxb.descriptors.service.MService;
import org.mobicents.slee.container.component.service.ServiceDescriptor;

/**
 * Start time:17:15:13 2009-01-20<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class ServiceDescriptorImpl extends AbstractComponentDescriptor implements ServiceDescriptor {

	private final ServiceID serviceID;
	private final SbbID rootSbbID;
	private final byte defaultPriority;
	private final String addressProfileTable;
	private final String resourceInfoProfileTable;
	
	public ServiceDescriptorImpl(MService mService) throws DeploymentException {
		super(mService.isSlee11());
		try {
			this.defaultPriority = mService.getDefaultPriority();
			this.serviceID = new ServiceID(mService.getServiceName(),mService.getServiceVendor(),mService.getServiceVersion());
			this.rootSbbID = new SbbID(mService.getRootSbb().getSbbName(),mService.getRootSbb().getSbbVendor(),mService.getRootSbb().getSbbVersion());
			this.addressProfileTable = mService.getAddressProfileTable();
			this.resourceInfoProfileTable = mService.getResourceInfoProfileTable();
			dependenciesSet.add(rootSbbID);
			// even if the root sbb does not declares it, all services depend on standard service started event(s)
			dependenciesSet.add(new EventTypeID("javax.slee.serviceactivity.ServiceStartedEvent", "javax.slee",
			"1.0"));
			if (isSlee11()) {
				dependenciesSet.add(new EventTypeID("javax.slee.serviceactivity.ServiceStartedEvent", "javax.slee",
				"1.1"));
			}
		} catch (Exception e) {
			throw new DeploymentException("failed to build service descriptor",e);
		}
	}

	/* (non-Javadoc)
	 * @see org.mobicents.slee.core.component.service.ServiceDescriptor#getDefaultPriority()
	 */
	public byte getDefaultPriority() {
		return defaultPriority;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.core.component.service.ServiceDescriptor#getRootSbbID()
	 */
	public SbbID getRootSbbID() {
		return rootSbbID;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.core.component.service.ServiceDescriptor#getServiceID()
	 */
	public ServiceID getServiceID() {
		return serviceID;
	}

	/* (non-Javadoc)
	 * @see org.mobicents.slee.core.component.service.ServiceDescriptor#getAddressProfileTable()
	 */
	public String getAddressProfileTable() {
		return addressProfileTable;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.core.component.service.ServiceDescriptor#getResourceInfoProfileTable()
	 */
	public String getResourceInfoProfileTable() {
		return resourceInfoProfileTable;
	}
}
