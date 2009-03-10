package org.mobicents.slee.runtime.facilities;

import javax.slee.management.ResourceAdaptorEntityNotification;

import junit.framework.TestCase;

public class TraceFacilityImplTest extends TestCase {

	private ResourceAdaptorEntityNotification notificationSource = new ResourceAdaptorEntityNotification("XXX");

	@Override
	protected void setUp() throws Exception {
		// TODO Auto-generated method stub
		super.setUp();
	}

	@Override
	protected void tearDown() throws Exception {
		// TODO Auto-generated method stub
		super.tearDown();
	}

	public void testCheckTracerNameMethodOK() {
		String tracerName = "";

		try {
			TraceFacilityImpl.checkTracerName(tracerName, notificationSource);

		} catch (Exception e) {
			assertTrue("Failed, tracer name["+tracerName+"] was valid, yet we got exception: " + e, false);
		}
		
		tracerName = "olo";
		try {
			TraceFacilityImpl.checkTracerName(tracerName, notificationSource);

		} catch (Exception e) {
			assertTrue("Failed, tracer name["+tracerName+"] was valid, yet we got exception: " + e, false);
		}
		
		tracerName = "olo9";
		try {
			TraceFacilityImpl.checkTracerName(tracerName, notificationSource);

		} catch (Exception e) {
			assertTrue("Failed, tracer name["+tracerName+"] was valid, yet we got exception: " + e, false);
		}
		
		tracerName = "olo9.adsf";
		try {
			TraceFacilityImpl.checkTracerName(tracerName, notificationSource);

		} catch (Exception e) {
			assertTrue("Failed, tracer name["+tracerName+"] was valid, yet we got exception: " + e, false);
		}
		
		
		
	}
	
	public void testCheckTracerNameMethodIllegalNames() {
		String tracerName = ".";

		try {
			TraceFacilityImpl.checkTracerName(tracerName, notificationSource);
			assertTrue("Failed, tracer name["+tracerName+"] was invalid, yet, test passed", false);
		} catch (Exception e) {
			assertTrue("Passed, tracer name["+tracerName+"] was invalid, we got exception: " + e, true);
		}
		
		tracerName = "asd.";

		try {
			TraceFacilityImpl.checkTracerName(tracerName, notificationSource);
			assertTrue("Failed, tracer name["+tracerName+"] was invalid, yet, test passed", false);
		} catch (Exception e) {
			assertTrue("Passed, tracer name["+tracerName+"] was invalid, we got exception: " + e, true);
		}
		tracerName = "asd..asfaf";

		try {
			TraceFacilityImpl.checkTracerName(tracerName, notificationSource);
			assertTrue("Failed, tracer name["+tracerName+"] was invalid, yet, test passed", false);
		} catch (Exception e) {
			assertTrue("Passed, tracer name["+tracerName+"] was invalid, we got exception: " + e, true);
		}
		tracerName = "asd. .asfaf";

		try {
			TraceFacilityImpl.checkTracerName(tracerName, notificationSource);
			assertTrue("Failed, tracer name["+tracerName+"] was invalid, yet, test passed", false);
		} catch (Exception e) {
			assertTrue("Passed, tracer name["+tracerName+"] was invalid, we got exception: " + e, true);
		}
		
		tracerName = "asd.a a.asfaf";

		try {
			TraceFacilityImpl.checkTracerName(tracerName, notificationSource);
			assertTrue("Failed, tracer name["+tracerName+"] was invalid, yet, test passed", false);
		} catch (Exception e) {
			assertTrue("Passed, tracer name["+tracerName+"] was invalid, we got exception: " + e, true);
		}
		
		tracerName = "asd.a_a.asfaf";

		try {
			TraceFacilityImpl.checkTracerName(tracerName, notificationSource);
			assertTrue("Failed, tracer name["+tracerName+"] was invalid, yet, test passed", false);
		} catch (Exception e) {
			assertTrue("Passed, tracer name["+tracerName+"] was invalid, we got exception: " + e, true);
		}
		
	}

}
