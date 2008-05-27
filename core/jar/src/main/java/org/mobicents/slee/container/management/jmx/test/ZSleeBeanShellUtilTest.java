/***************************************************
 *                                                 *
 *  Mobicents: The Open Source JSLEE Platform      *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 ***************************************************/
package org.mobicents.slee.container.management.jmx.test;

import java.io.File;

import javax.slee.management.DeployableUnitDescriptor;
import javax.slee.management.DeployableUnitID;
import javax.slee.management.ServiceState;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.mobicents.slee.container.management.jmx.SleeBeanShellUtil;

/**
 * Unit test to test all the methods of SleeBeanShellUtil Make sure that server
 * is started before this test is executed. You can use 'ant test
 * -Dtest.name=org.mobicents.slee.container.management.jmx.test.ZSleeBeanShellUtilTest'
 * to execute this test. Please make sure that 'ant
 * build-test-sleebeanshellutil-du' is called before executing this one off
 * test.
 * 
 * Or call 'ant run-junit-2-xml' to execute all the tests
 * 
 * @author amit.bhayani
 * 
 */
public class ZSleeBeanShellUtilTest extends TestCase {

	SleeBeanShellUtil sleeBeanShellUtil = null;

	String pathToSBBDuJar = null;

	String serviceId = null;

	String sbbId = null;

	public ZSleeBeanShellUtilTest(String arg0) {
		super(arg0);
	}

	public static Test suite() {
		return new TestSuite(ZSleeBeanShellUtilTest.class);
	}

	protected void setUp() throws Exception {
		super.setUp();

		sleeBeanShellUtil = new SleeBeanShellUtil();

		String mobicents_home = System.getProperty("MOBICENTS_HOME");

		// mobicents_home can be null if one of test is executed.
		// If run-junit-2-xml target is called MOBICENTS_HOME is set to current
		// directory i.e., '.'
		if (mobicents_home == null
				|| (mobicents_home != null && mobicents_home.equals("."))) {
			try {
				mobicents_home = new java.io.File(".").getCanonicalPath();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		pathToSBBDuJar = "file:" + mobicents_home + File.separator + "build"
				+ File.separator + "slee-bean-shell-util-test-du.jar";
		int index = pathToSBBDuJar.indexOf("\\");
			
		//Hack for Windows OS
		if (index != -1) {
			pathToSBBDuJar = pathToSBBDuJar.replaceAll("\\\\", "/");
		}
		
		serviceId = "ServiceID[SleeBeanShellUtilService#org.mobicents#1.0]";
		sbbId = "SbbID[SleeBeanShellUtilSbb#org.mobicents#1.0]";

	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testService() throws Exception {
		DeployableUnitID deployableUnitIDOfService = null;

		// Test Install
		deployableUnitIDOfService = (DeployableUnitID) sleeBeanShellUtil
				.install(pathToSBBDuJar);
		assertNotNull("DeployableUnitID of Service Installed = "
				+ deployableUnitIDOfService.toString(),
				deployableUnitIDOfService);

		// Sleep for 2 sec so that sbb-du deployment is complete
		try {
			Thread.sleep(2000);
		} catch (InterruptedException ie) {
			// ignore
		}

		// Test getDeploymentId
		DeployableUnitID tmpDeployableUnitID = (DeployableUnitID) sleeBeanShellUtil
				.getDeploymentId(pathToSBBDuJar);
		assertNotNull(tmpDeployableUnitID);
		assertEquals("getDeploymentId returns DeployableUnitID of Service = "
				+ tmpDeployableUnitID.toString(), tmpDeployableUnitID,
				deployableUnitIDOfService);

		// Test getDescriptor
		DeployableUnitDescriptor tmpDeployableUnitDescriptor = (DeployableUnitDescriptor) sleeBeanShellUtil
				.getDescriptor(deployableUnitIDOfService.toString());
		assertNotNull(tmpDeployableUnitDescriptor);

		// Test getServiceState At this time state should be 'Inactive'
		ServiceState serviceState = (ServiceState) sleeBeanShellUtil
				.getServiceState(serviceId);
		assertNotNull(serviceState);
		assertEquals(ServiceState.INACTIVE, serviceState);

		// Test activateService
		sleeBeanShellUtil.activateService(serviceId);

		// Sleep for 2 sec so that Activation of Service is complete
		try {
			Thread.sleep(2000);
		} catch (InterruptedException ie) {
			// ignore
		}

		// Test getServiceState At this time state should be 'Active'
		serviceState = (ServiceState) sleeBeanShellUtil
				.getServiceState(serviceId);
		assertNotNull(serviceState);
		assertEquals(ServiceState.ACTIVE, serviceState);

		// Test deactivateService
		sleeBeanShellUtil.deactivateService(serviceId);

		// Test unInstall using the url to sbb-du.jar
		sleeBeanShellUtil.unInstall(pathToSBBDuJar);

		// Sleep for 2 sec so that unInstall of Service is complete
		try {
			Thread.sleep(2000);
		} catch (InterruptedException ie) {
			// ignore
		}

	}

	// TODO Add one test method each for testing operations on RA and Profile

}
