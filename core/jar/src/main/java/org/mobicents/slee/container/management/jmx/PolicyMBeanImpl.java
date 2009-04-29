/**
 * Start time:09:19:15 2009-04-16<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
package org.mobicents.slee.container.management.jmx;

import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.Policy;
import java.security.PrivilegedAction;
import java.util.Timer;
import java.util.TimerTask;

import org.jboss.system.ServiceMBeanSupport;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.component.security.PolicyFile;

/**
 * Start time:09:19:15 2009-04-16<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 */
public class PolicyMBeanImpl extends ServiceMBeanSupport implements PolicyMBeanImplMBean {

	/**
	 * Holds defualt policy object
	 */
	private Policy previousPolicy = null;

	private PolicyFile mPolicyFile = new PolicyFile();

	public String getCodeSources() {
		return this.mPolicyFile.getCodeSources();
	}

	public String getPolicyFilesURL() {
		return this.mPolicyFile.getPolicyFilesURL();
	}

	public boolean isUseMPolicy() {
		return Policy.getPolicy() instanceof PolicyFile;
	}

	public void setUseMPolicy(boolean useMPolicy) {
		if (useMPolicy) {
			// we have to check :)
			if (previousPolicy != null) {
				super.log.info("Already switched policy, not performing switch");
				return;
			}

			if (isUseMPolicy()) {
				super.log.info("Already switched policy from different bean, not performing switch");
				return;
			}
			
			this.previousPolicy = Policy.getPolicy();
			//AccessControlContext acc = AccessController.getContext();
			//AccessController.doPrivileged(new PrivilegedAction22(this.mPolicyFile));
			
			this.mPolicyFile.refresh();
			Policy.setPolicy(this.mPolicyFile);
			
			
		} else {
			if (!isUseMPolicy()) {
				super.log.info("Policy is not Mobicents policy, can not remove it.");
				return;
			}

			// we have to check :)
			if (previousPolicy == null) {
				super.log.info("Default policy is not present, ca not switch.");
				return;
			}

			this.previousPolicy.refresh();
			
			Policy.setPolicy(this.previousPolicy);
		}
		
		
	}
	
	

	@Override
	protected void startService() throws Exception {
		// TODO Auto-generated method stub
		super.startService();

	}

	@Override
	protected void stopService() throws Exception {
		// TODO Auto-generated method stub
		super.stopService();
		this.setUseMPolicy(false);

	}


}
