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
package org.mobicents.slee.tools.twiddle;

import gnu.getopt.LongOpt;

/**
 * Small class with OName utils methods and static values with <b>VALID</b> jmx object names as strings.
 * @author baranowb
 *
 */
public final class JMXNameUtility {

	private JMXNameUtility()
	{
		//no need for instances.
		
	}

	//standard jmx mgmt from specs
	public static final String SLEE_ALARM = javax.slee.management.AlarmMBean.OBJECT_NAME;
	public static final String SLEE_DEPLOYMENT = javax.slee.management.DeploymentMBean.OBJECT_NAME;
	public static final String SLEE_PROFILE_PROVISIONING = javax.slee.management.ProfileProvisioningMBean.OBJECT_NAME;
	public static final String SLEE_RESOURCE_MANAGEMENT = javax.slee.management.ResourceManagementMBean.OBJECT_NAME;
	public static final String SLEE_SERVICE_MANAGEMENT = javax.slee.management.ServiceManagementMBean.OBJECT_NAME;
	public static final String SLEE_MANAGEMENT = javax.slee.management.SleeManagementMBean.OBJECT_NAME;
	public static final String SLEE_TRACE = javax.slee.management.TraceMBean.OBJECT_NAME;

	
	//some mobicents specific;
	public static final String MC_ACTIVITY_MANAGEMENT = "org.mobicents.slee:name=ActivityManagementMBean";
	public static final String MC_CONGESTION_CONTROL = "org.mobicents.slee:name=CongestionControlConfiguration";
	public static final String MC_DEPLOYER = "org.mobicents.slee:name=DeployerMBean";
	public static final String MC_EVENT_ROUTER = "org.mobicents.slee:name=EventRouterConfiguration";
	public static final String MC_EVENT_ROUTER_STATS = "org.mobicents.slee:name=EventRouterStatistics";
	public static final String MC_LOG_MANAGEMENT = "org.mobicents.slee:name=LogManagementMBean";
	public static final String MC_POLICY_MANAGEMENT = "org.mobicents.slee:name=PolicyManagementMBean";
	public static final String MC_SBB_ENTITIES = "org.mobicents.slee:name=SbbEntitiesMBean";
	public static final String MC_TIMER_FACILITY = "org.mobicents.slee:name=TimerFacilityConfiguration";
	public static final String MC_MANAGEMENT = "org.mobicents.slee:service=MobicentsManagement";
	public static final String MC_PROFILE_OBJECT_POOL = "org.mobicents.slee:service=ProfileObjectPoolManagement";
	public static final String MC_SBB_OBJECT_POOL = "org.mobicents.slee:service=SbbObjectPoolManagement";
	
	
	
	
	
	
	
	
	
	public static void main(String[] args)
	{
		String sopts = "-:a:d:u:i:o:c"; // "-" is required to allow non option args!, ":" is for req, argument, lack of it after option means no args.
		
		LongOpt[] lopts = { new LongOpt("activate", LongOpt.REQUIRED_ARGUMENT, null, 'a'),
				new LongOpt("deactivate", LongOpt.REQUIRED_ARGUMENT, null, 'd'),
				new LongOpt("serviceUsageMBean", LongOpt.REQUIRED_ARGUMENT, null, 'u'), 
				new LongOpt("services", LongOpt.REQUIRED_ARGUMENT, null, 'i'),
				new LongOpt("state", LongOpt.REQUIRED_ARGUMENT, null, 'o'),
				new LongOpt("deactivateAndActivate", LongOpt.NO_ARGUMENT, null, 'c'),
		};
		
		
	}
	
	
}
