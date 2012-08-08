/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

/**
 * Start time:09:19:15 2009-04-16<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
package org.mobicents.slee.container.component.management.jmx;

import java.security.Policy;

import org.jboss.system.ServiceMBeanSupport;
import org.mobicents.slee.container.component.security.PolicyFileImpl;

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
