package org.mobicents.slee.runtime.facilities;

import javax.slee.management.ResourceAdaptorEntityNotification;

import junit.framework.TestCase;

public class TracerImplTest extends TestCase {

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
			TracerImpl.checkTracerName(tracerName, notificationSource);

		} catch (Exception e) {
			assertTrue("Failed, tracer name["+tracerName+"] was valid, yet we got exception: " + e, false);
		}
		
		tracerName = "olo";
		try {
			TracerImpl.checkTracerName(tracerName, notificationSource);

		} catch (Exception e) {
			assertTrue("Failed, tracer name["+tracerName+"] was valid, yet we got exception: " + e, false);
		}
		
		tracerName = "olo9";
		try {
			TracerImpl.checkTracerName(tracerName, notificationSource);

		} catch (Exception e) {
			assertTrue("Failed, tracer name["+tracerName+"] was valid, yet we got exception: " + e, false);
		}
		
		tracerName = "olo9.adsf";
		try {
			TracerImpl.checkTracerName(tracerName, notificationSource);

		} catch (Exception e) {
			assertTrue("Failed, tracer name["+tracerName+"] was valid, yet we got exception: " + e, false);
		}
		
		
		
	}
	
	public void testCheckTracerNameMethodIllegalNames() {
		String tracerName = ".";

		try {
			TracerImpl.checkTracerName(tracerName, notificationSource);
			assertTrue("Failed, tracer name["+tracerName+"] was invalid, yet, test passed", false);
		} catch (Exception e) {
			assertTrue("Passed, tracer name["+tracerName+"] was invalid, we got exception: " + e, true);
		}
		
		tracerName = "asd.";

		try {
			TracerImpl.checkTracerName(tracerName, notificationSource);
			assertTrue("Failed, tracer name["+tracerName+"] was invalid, yet, test passed", false);
		} catch (Exception e) {
			assertTrue("Passed, tracer name["+tracerName+"] was invalid, we got exception: " + e, true);
		}
		tracerName = "asd..asfaf";

		try {
			TracerImpl.checkTracerName(tracerName, notificationSource);
			assertTrue("Failed, tracer name["+tracerName+"] was invalid, yet, test passed", false);
		} catch (Exception e) {
			assertTrue("Passed, tracer name["+tracerName+"] was invalid, we got exception: " + e, true);
		}
		tracerName = "asd. .asfaf";

		try {
			TracerImpl.checkTracerName(tracerName, notificationSource);
			assertTrue("Passed, tracer name["+tracerName+"]", true);
		} catch (Exception e) {
			assertTrue("Failed, tracer name["+tracerName+"] was valid, yet we got exception: " + e, false);
		}
		
		tracerName = "asd.a a.asfaf";

		try {
			TracerImpl.checkTracerName(tracerName, notificationSource);
			assertTrue("Passed, tracer name["+tracerName+"]", true);
		} catch (Exception e) {
			assertTrue("Failed, tracer name["+tracerName+"] was valid, yet we got exception: " + e, false);
		}
		
		tracerName = "asd.a_a.asfaf";

		try {
			TracerImpl.checkTracerName(tracerName, notificationSource);
			assertTrue("Passed, tracer name["+tracerName+"]", true);
		} catch (Exception e) {
			assertTrue("Failed, tracer name["+tracerName+"] was valid, yet we got exception: " + e, false);
		}
		
	}

}
