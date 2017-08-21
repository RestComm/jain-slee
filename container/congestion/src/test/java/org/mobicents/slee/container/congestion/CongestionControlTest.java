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

package org.mobicents.slee.container.congestion;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mobicents.slee.container.management.jmx.CongestionControlConfiguration;

public class CongestionControlTest {
	
	private CongestionControlConfiguration configuration;
	private TCongestionControlImpl congestionControl;
	private ScheduledExecutorService scheduler;
	
	@Before
	public void before() {
		configuration = new CongestionControlConfiguration();
		scheduler = new ScheduledThreadPoolExecutor(1);
		congestionControl = new TCongestionControlImpl(configuration,scheduler);
		congestionControl.sleeStarting();
	}
	
	@After
	public void after() {
		congestionControl.sleeStopping();
		scheduler.shutdownNow();
	}
	
	@Test
	public void testCongestionControl() throws Exception {
		
		// set initial config
		configuration.setRefuseFireEvent(true);
		configuration.setRefuseStartActivity(true);
		configuration.setPeriodBetweenChecks(0);
		configuration.setMinFreeMemoryToTurnOn(10);
		configuration.setMinFreeMemoryToTurnOff(20);
		
		// setup congestion control memory to 9%
		congestionControl.setFreeMemory(9L);
		congestionControl.setMaxMemory(100L);
		
		// turn on by setting period to 1s
		configuration.setPeriodBetweenChecks(1);
		Thread.sleep(2000);
		
		// assert alarm is raised and that it is refusing activity start and event firing
		Assert.assertTrue("Alarm not raised", congestionControl.isAlarmRaised());
		Assert.assertTrue("Not refusing start activity", congestionControl.refuseStartActivity());
		Assert.assertTrue("Not refusing event firing", congestionControl.refuseFireEvent());
		
		// change config
		configuration.setRefuseFireEvent(false);
		Assert.assertFalse("Refusing event firing", congestionControl.refuseFireEvent());
		configuration.setRefuseStartActivity(false);
		Assert.assertFalse("Refusing start activity", congestionControl.refuseStartActivity());
		
		// restore config to refuse everything
		configuration.setRefuseFireEvent(true);
		configuration.setRefuseStartActivity(true);
		
		// setup congestion control memory to 11% and wait 2s to ensure timer task runs 
		congestionControl.setFreeMemory(11L);
		Thread.sleep(2000);
		
		// assert alarm is still raised and that it is refusing activity start and event firing
		Assert.assertTrue("Alarm not raised", congestionControl.isAlarmRaised());
		Assert.assertTrue("Not refusing start activity", congestionControl.refuseStartActivity());
		Assert.assertTrue("Not refusing event firing", congestionControl.refuseFireEvent());
		
		// setup congestion control memory to 21% and force config update so everything is off
		congestionControl.setFreeMemory(21L);
		configuration.setPeriodBetweenChecks(1);
		Thread.sleep(1000);
		Assert.assertFalse("Alarm raised", congestionControl.isAlarmRaised());
		Assert.assertFalse("Refusing start activity", congestionControl.refuseStartActivity());
		Assert.assertFalse("Refusing event firing", congestionControl.refuseFireEvent());
		
		// setup congestion control memory to 9% and force config update so everything is on again
		congestionControl.setFreeMemory(9L);
		configuration.setPeriodBetweenChecks(1);
		Thread.sleep(1000);
		Assert.assertTrue("Alarm not raised", congestionControl.isAlarmRaised());
		Assert.assertTrue("Not refusing start activity", congestionControl.refuseStartActivity());
		Assert.assertTrue("Not refusing event firing", congestionControl.refuseFireEvent());
		
		// turn off and
		configuration.setPeriodBetweenChecks(0);
		
		// assert alarm is not raised and that it is not refusing activity start and event firing
		Assert.assertFalse("Alarm raised", congestionControl.isAlarmRaised());
		Assert.assertFalse("Refusing start activity", congestionControl.refuseStartActivity());
		Assert.assertFalse("Refusing event firing", congestionControl.refuseFireEvent());
		
		// test succeed
	}
}
