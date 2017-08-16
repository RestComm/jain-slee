/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2017, Telestax Inc and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

/**
 * Start time:09:19:15 2009-04-16<br>
 * Project: restcomm-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
package org.mobicents.slee.container.component.management.jmx;

import java.security.Policy;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.component.security.PolicyFileImpl;
import org.mobicents.slee.container.management.jmx.MobicentsServiceMBeanSupport;

/**
 * Start time:09:19:15 2009-04-16<br>
 * Project: restcomm-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 */
public class PolicyMBeanImpl extends MobicentsServiceMBeanSupport implements PolicyMBeanImplMBean {

	private static final Logger log = Logger.getLogger(PolicyMBeanImpl.class);
	
	/**
	 * Holds defualt policy object
	 */
	private Policy previousPolicy = null;

	private PolicyFileImpl mPolicyFile = new PolicyFileImpl();

	public String getCodeSources() {
		return this.mPolicyFile.getCodeSources();
	}

	public String getPolicyFilesURL() {
		return this.mPolicyFile.getPolicyFilesURL();
	}

	public boolean isUseMPolicy() {
		return Policy.getPolicy() instanceof PolicyFileImpl;
	}

	public void setUseMPolicy(boolean useMPolicy) {
		if (useMPolicy) {
			// we have to check :)
			if (previousPolicy != null) {
				log.info("Already switched policy, not performing switch");
				return;
			}

			if (isUseMPolicy()) {
				log.info("Already switched policy from different bean, not performing switch");
				return;
			}
			
			this.previousPolicy = Policy.getPolicy();
			//AccessControlContext acc = AccessController.getContext();
			//AccessController.doPrivileged(new PrivilegedAction22(this.mPolicyFile));
			
			this.mPolicyFile.refresh();
			Policy.setPolicy(this.mPolicyFile);
			
			
		} else {
			if (!isUseMPolicy()) {
				log.info("Policy is not Restcomm policy, can not remove it.");
				return;
			}

			// we have to check :)
			if (previousPolicy == null) {
				log.info("Default policy is not present, ca not switch.");
				return;
			}

			this.previousPolicy.refresh();
			
			Policy.setPolicy(this.previousPolicy);
		}
		
		
	}
		
	@Override
	public void postDeregister() {
		super.postDeregister();
		this.setUseMPolicy(false);
	}

}
