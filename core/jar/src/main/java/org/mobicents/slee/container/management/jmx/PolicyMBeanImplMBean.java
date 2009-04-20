/**
 * Start time:08:53:04 2009-04-16<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
package org.mobicents.slee.container.management.jmx;

import org.jboss.system.ServiceMBean;

/**
 * Start time:08:53:04 2009-04-16<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * 
 * Simple MBean to allow access to some info so we know whats going on inside and provide means of polite instalation of policy.
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 */
public interface PolicyMBeanImplMBean extends ServiceMBean {

	/**
	 * Returns String form of urls that point files loaded
	 * @return
	 */
	public String getPolicyFilesURL();
	/**
	 * Return Arrays.toString form of code source URLs loaded into policy
	 * @return
	 */
	public String getCodeSources();
	
	public boolean isUseMPolicy();
	
	public void setUseMPolicy(boolean useMPolicy);
	
}
