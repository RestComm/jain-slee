/*
 * JBoss, Home of Professional Open Source
 * Copyright XXXX, Red Hat Middleware LLC, and individual contributors as indicated
 * by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a full listing
 * of individual contributors.
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License, v. 2.0.
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License,
 * v. 2.0 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 */
package org.mobicents.tools.twiddle.jslee;

import javax.management.ObjectName;

/**
 * @author baranowb
 *
 */
public class ProfileUsageCommand extends AbstractUsageCommand {

	/**
	 * 
	 */
	public ProfileUsageCommand() {
		super("usage.profile",
				"This command performs operations on JSLEE MBeans which are associated with usage parameter sets - be it specific MBean for parameter set, notifiaction management MBean...");
	}

	/* (non-Javadoc)
	 * @see org.mobicents.tools.twiddle.jslee.AbstractUsageCommand#getProvisioningMBeanName()
	 */
	@Override
	public ObjectName getProvisioningMBeanName() {
		return super.PROFILE_PROVISIONING_MBEAN;
	}

	/* (non-Javadoc)
	 * @see org.mobicents.tools.twiddle.jslee.AbstractUsageCommand#getUsageMGMTMBeanOperation()
	 */
	@Override
	public String getUsageMGMTMBeanOperation() {
		
		return super.PROFILE_GET_METHOD;
	}

}
