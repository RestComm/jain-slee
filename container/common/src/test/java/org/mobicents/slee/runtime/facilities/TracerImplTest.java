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
