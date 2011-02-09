/**
 * 
 */
package org.mobicents.slee.container.management;

import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;

import org.mobicents.slee.container.SleeContainerModule;
import org.mobicents.slee.container.component.profile.ProfileSpecificationComponent;
import org.mobicents.slee.container.component.ra.ResourceAdaptorComponent;
import org.mobicents.slee.container.component.service.ServiceComponent;
import org.mobicents.slee.container.management.jmx.ProfileTableUsageMBean;
import org.mobicents.slee.container.management.jmx.ResourceUsageMBean;
import org.mobicents.slee.container.management.jmx.ServiceUsageMBean;
import org.mobicents.slee.container.usage.UsageMBeansConfiguration;

/**
 * @author martins
 * 
 */
public interface UsageParametersManagement extends SleeContainerModule {

	/**
	 * 
	 * @return
	 */
	public UsageMBeansConfiguration getConfiguration();

	/**
	 * 
	 * @param profileTableName
	 * @param component
	 * @return
	 * @throws NotCompliantMBeanException
	 * @throws MalformedObjectNameException
	 * @throws NullPointerException
	 */
	public ProfileTableUsageMBean newProfileTableUsageMBean(
			String profileTableName, ProfileSpecificationComponent component)
			throws NotCompliantMBeanException, MalformedObjectNameException,
			NullPointerException;

	/**
	 * 
	 * @param entityName
	 * @param component
	 * @return
	 * @throws NotCompliantMBeanException
	 * @throws MalformedObjectNameException
	 * @throws NullPointerException
	 */
	public ResourceUsageMBean newResourceUsageMBean(String entityName,
			ResourceAdaptorComponent component)
			throws NotCompliantMBeanException, MalformedObjectNameException,
			NullPointerException;

	/**
	 * 
	 * @param component
	 * @return
	 * @throws NotCompliantMBeanException
	 * @throws MalformedObjectNameException
	 * @throws NullPointerException
	 */
	public ServiceUsageMBean newServiceUsageMBean(ServiceComponent component)
			throws NotCompliantMBeanException, MalformedObjectNameException,
			NullPointerException;

}
