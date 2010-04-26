package org.mobicents.slee.container.management;

import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;

import org.mobicents.slee.container.AbstractSleeContainerModule;
import org.mobicents.slee.container.component.profile.ProfileSpecificationComponent;
import org.mobicents.slee.container.component.ra.ResourceAdaptorComponent;
import org.mobicents.slee.container.component.service.ServiceComponent;
import org.mobicents.slee.container.management.jmx.ProfileTableUsageMBean;
import org.mobicents.slee.container.management.jmx.ProfileTableUsageMBeanImpl;
import org.mobicents.slee.container.management.jmx.ResourceUsageMBean;
import org.mobicents.slee.container.management.jmx.ResourceUsageMBeanImpl;
import org.mobicents.slee.container.management.jmx.ServiceUsageMBean;
import org.mobicents.slee.container.management.jmx.ServiceUsageMBeanImpl;
import org.mobicents.slee.container.usage.UsageMBeansConfiguration;

/**
 * @author martins
 * 
 */
public class UsageParametersManagementImpl extends AbstractSleeContainerModule
		implements UsageParametersManagement {

	private final UsageMBeansConfiguration configuration;

	/**
	 * @param configuration
	 */
	public UsageParametersManagementImpl(UsageMBeansConfiguration configuration) {
		this.configuration = configuration;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.mobicents.slee.container.management.UsageParametersManagement#
	 * getConfiguration()
	 */
	public UsageMBeansConfiguration getConfiguration() {
		return configuration;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.mobicents.slee.container.management.UsageParametersManagement#
	 * newProfileTableUsageMBean(java.lang.String,
	 * org.mobicents.slee.core.component.profile.ProfileSpecificationComponent)
	 */
	public ProfileTableUsageMBean newProfileTableUsageMBean(
			String profileTableName, ProfileSpecificationComponent component)
			throws NotCompliantMBeanException, MalformedObjectNameException,
			NullPointerException {
		return new ProfileTableUsageMBeanImpl(profileTableName, component,
				sleeContainer);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.mobicents.slee.container.management.UsageParametersManagement#
	 * newResourceUsageMBean(java.lang.String,
	 * org.mobicents.slee.core.component.ra.ResourceAdaptorComponent)
	 */
	public ResourceUsageMBean newResourceUsageMBean(String entityName,
			ResourceAdaptorComponent component)
			throws NotCompliantMBeanException, MalformedObjectNameException,
			NullPointerException {
		return new ResourceUsageMBeanImpl(entityName, component, sleeContainer);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.mobicents.slee.container.management.UsageParametersManagement#
	 * newServiceUsageMBean
	 * (org.mobicents.slee.core.component.service.ServiceComponent)
	 */
	public ServiceUsageMBean newServiceUsageMBean(ServiceComponent component)
			throws NotCompliantMBeanException, MalformedObjectNameException,
			NullPointerException {
		return new ServiceUsageMBeanImpl(component, sleeContainer);
	}

}
