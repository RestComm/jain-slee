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

import java.util.Arrays;

import javax.management.NotCompliantMBeanException;
import javax.management.Notification;
import javax.slee.InvalidArgumentException;
import javax.slee.facilities.TraceLevel;
import javax.slee.facilities.Tracer;
import javax.slee.management.NotificationSource;
import javax.slee.management.ResourceAdaptorEntityNotification;
import javax.slee.management.TraceNotification;

import org.mobicents.slee.container.management.jmx.TraceMBeanImpl;

import junit.framework.TestCase;

public class TracerFrameworkTest extends TestCase {

	//protected TraceFacilityImpl traceFacility = null;
	protected FakeTraceMBeanImpl traceMBean = null;
	protected TracerStorage ts = null;
	protected NotificationSource notificationSource = null;
    protected TracerStorage tracerStorage = null;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        // akward, this will create mbean with regular trace facility, but we
        // need to check some things, in trace facility
        //traceFacility = new TraceFacilityImpl(traceMBean);
        traceMBean = new FakeTraceMBeanImpl();
        notificationSource = new ResourceAdaptorEntityNotification("XxX");
        tracerStorage = new TracerStorage(notificationSource, traceMBean);
    }

    /**
     * Here we test default tracer which is always there.
     */
    public void testDefaultTracer() {
        try {
            Tracer tracer = this.tracerStorage.createTracer("", true);
            if (tracer == null) {
                fail("Returned tracer is null");
                return;
            }

            String parentTracerName = tracer.getParentTracerName();
            assertNull("Parent tracer name for root tracer must be null", parentTracerName);
            assertNotNull("Tracer name must not be null", tracer.getTracerName());
            assertTrue("Root tracer name must be: \"\"", tracer.getTracerName().compareTo("") == 0);
            assertNotNull("tracer level must not be null", tracer.getTraceLevel());
            assertTrue("tracer level must be equal to INFO, instead it is " + tracer.getTraceLevel(), tracer.getTraceLevel().equals(TraceLevel.INFO));
        } catch (Exception e) {
            e.printStackTrace();
            fail("Got exception: " + e);
        }
    }

    public void test1Creation() {
        try {
            Tracer tracer = this.tracerStorage.createTracer("org.mobicents.test", true);
            if (tracer == null) {
                fail("Returned tracer is null");
                return;
            }

            // here we have to check some recurency
            int loopGuard = 0;
            // ?
            int maxLoops = 3;
            // Start condition test
            String parentTracerName = tracer.getParentTracerName();
            assertNotNull("Parent tracer name for root tracer must not be null", parentTracerName);
            // this would indicate root
            String currentTracerName = null;
            String currentTracerParentName = null;
            while (tracer.getParentTracerName() != null) {
                if (maxLoops == loopGuard) {
                    fail("Too many loops, possibly there is ring of parent child relation, this should not happen.");
                    return;
                }
                if (currentTracerParentName != null) {
                    assertTrue("Parent name[" + currentTracerParentName + "] from tracer down in tree does not match this tracer name[" + tracer.getTracerName() + "]",
                            currentTracerParentName.compareTo(tracer.getTracerName()) == 0);
                }

                currentTracerName = tracer.getTracerName();
                currentTracerParentName = tracer.getParentTracerName();

                assertNotNull("Tracer name must not be null", tracer.getTracerName());
                assertNotNull("tracer level must not be null", tracer.getTraceLevel());
                assertTrue("tracer level must be equal to INFO", tracer.getTraceLevel().equals(TraceLevel.INFO));

                tracer = tracerStorage.createTracer(tracer.getParentTracerName(), false);
                if (tracer == null) {
                    fail("Parent tracer is null for tracer with name: " + currentTracerName + ", parent name: " + currentTracerParentName);
                    return;
                }

                loopGuard++;
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail("Got exception: " + e);
        }
    }

    public void test2TestEqulesesOfTracerObjects() throws InvalidArgumentException {
        Tracer org1 = this.tracerStorage.createTracer("org", true);
        Tracer org2 = this.tracerStorage.createTracer("org", false);
        Tracer org3 = this.tracerStorage.createTracer("org", true);

        assertEquals("Tracer object created with different boolean flag are different", org1, org2);
        assertEquals("Tracer object created with the same boolean flag are different", org1, org3);
    }

    /**
     * Thisd tests if levels are inherited accross Tracer object. If not explicitly set trace level should be inherited
     * from parent, its not told if inheritance should be constant or not, we follow constant, Until level is set, we
     * inherit
     */
    public void test3TraceLevelInheritance() {
        try {
            Tracer orgMobicentsTest = tracerStorage.createTracer("org.mobicents.test", true);
            Tracer orgMobicents = tracerStorage.createTracer("org.mobicents", true);
            Tracer org = tracerStorage.createTracer("org", true);

            // Root
            assertEquals("Trace level do not match case - 0", org.getTraceLevel(), TraceLevel.INFO);
            assertEquals("Trace level do not match case - 1", org.getTraceLevel(), orgMobicents.getTraceLevel());
            assertEquals("Trace level do not match case - 2", orgMobicents.getTraceLevel(), orgMobicentsTest.getTraceLevel());

            tracerStorage.setTracerLevel(TraceLevel.SEVERE, orgMobicents.getTracerName());
            // now orgMobicents and orgMobicentsXXX have sever trace level
            assertEquals("Trace level of tracer has not been changed to severe.", TraceLevel.SEVERE, orgMobicents.getTraceLevel());
            assertEquals("Trace level of child has not been changed to severe", TraceLevel.SEVERE, orgMobicentsTest.getTraceLevel());
            assertFalse("Trace level of parent tracer must be equal to root tracer, only its immediate children may have severe level", org.getTraceLevel() == TraceLevel.SEVERE);
            tracerStorage.unsetTracerLevel(orgMobicents.getTracerName());
        } catch (Exception e) {
            e.printStackTrace();
            fail("Got exception: " + e);
        }
    }

    public void test3GetTracersSet() {
        try {
            tracerStorage.createTracer("org.mobicents.test", true);
            Tracer tracer = tracerStorage.createTracer("org.mobicents", false);
            tracerStorage.createTracer("org", true);

            String[] tracersSet = tracerStorage.getDefinedTracerNames();

            assertNotNull("Tracers set must not be null");
            if (tracersSet == null) {
                return;
            }
            assertTrue("Tracers set must be one [" + tracersSet.length + "] now, it is not: " + Arrays.toString(tracersSet), tracersSet.length == 1);

            tracerStorage.setTracerLevel(TraceLevel.SEVERE, tracer.getTracerName());
            tracersSet = tracerStorage.getDefinedTracerNames();

            assertNotNull("Tracers set must not be null");
            if (tracersSet == null) {
                return;
            }
            assertTrue("Tracers set must have two elements, it does not", tracersSet.length == 2);
            assertNotNull("Tracer set element is null, it should not", tracersSet[1]);
            if (tracersSet[1] == null) {
                return;
            }
            assertTrue("Tracers set element must be equal to: " + tracer.getTracerName() + ", it is: " + tracersSet[1], tracersSet[1].compareTo(tracer.getTracerName()) == 0);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Got exception: " + e);
        }
    }

    public void test3GetTracersUsed() {
        try {
            tracerStorage.createTracer("org.mobicents.test", false);
            Tracer orgMobicents = tracerStorage.createTracer("org.mobicents", false);
            tracerStorage.createTracer("org", false);

            String[] definedTracers = tracerStorage.getRequestedTracerNames();

            assertNotNull("Defined tracers set must not be null");
            if (definedTracers == null) {
                return;
            }
            assertTrue("Defined tracers set must be empty[" + definedTracers.length + "] now, it is not: " + Arrays.toString(definedTracers), definedTracers.length == 0);

            orgMobicents = tracerStorage.createTracer("org.mobicents", true);
            orgMobicents = tracerStorage.createTracer("org.mobicents", false);
            definedTracers = tracerStorage.getRequestedTracerNames();

            assertNotNull("Defined tracers set must not be null");
            if (definedTracers == null) {
                return;
            }
            assertTrue("Defined tracers set must have one element, it does not", definedTracers.length == 1);
            assertNotNull("Defined tracers set element is null, it should not", definedTracers[0]);
            if (definedTracers[0] == null) {
                return;
            }
            assertTrue("Defined tracers set element must be equal to: " + orgMobicents.getTracerName() + ", it is: " + definedTracers[0],
                    definedTracers[0].compareTo(orgMobicents.getTracerName()) == 0);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Got exception: " + e);
        }
    }

    /**
     * Tests if notifications are sent properly and in proper cases. Possibly should be broken down.
     */
    public void test4TestNotificationType() {
        try {
            int localSeq = 0;
            tracerStorage.createTracer("org.mobicents.test", false);
            Tracer orgMobicents = tracerStorage.createTracer("org.mobicents", false);
            Tracer org = tracerStorage.createTracer("org", false);
            Tracer rootTracer = tracerStorage.createTracer("", false);
            tracerStorage.setTracerLevel(TraceLevel.SEVERE, orgMobicents.getTracerName());

            // Lets test
            traceMBean.setTestHarness(notificationSource.getTraceNotificationType(), localSeq++, notificationSource, TraceLevel.INFO, null, true);
            org.trace(TraceLevel.INFO, "TEST1");

            traceMBean.setTestHarness(notificationSource.getTraceNotificationType(), localSeq++, notificationSource, TraceLevel.INFO, null, true);
            rootTracer.trace(TraceLevel.INFO, "TEST2");

            traceMBean.setTestHarness(notificationSource.getTraceNotificationType(), localSeq, notificationSource, TraceLevel.INFO, null, false);
            orgMobicents.trace(TraceLevel.INFO, "TEST3");

            traceMBean.setTestHarness(notificationSource.getTraceNotificationType(), localSeq++, notificationSource, TraceLevel.INFO, null, true);
            rootTracer.trace(TraceLevel.INFO, "TEST4");

            traceMBean.setTestHarness(notificationSource.getTraceNotificationType(), localSeq, notificationSource, TraceLevel.CONFIG, null, false);
            org.config("TEST5");

            traceMBean.setTestHarness(notificationSource.getTraceNotificationType(), localSeq, notificationSource, TraceLevel.CONFIG, null, false);
            org.trace(TraceLevel.CONFIG, "TEST6");

            traceMBean.setTestHarness(notificationSource.getTraceNotificationType(), localSeq, notificationSource, TraceLevel.FINE, null, false);
            org.fine("TEST7");

            traceMBean.setTestHarness(notificationSource.getTraceNotificationType(), localSeq, notificationSource, TraceLevel.FINE, null, false);
            org.trace(TraceLevel.FINE, "TEST8");

            traceMBean.setTestHarness(notificationSource.getTraceNotificationType(), localSeq, notificationSource, TraceLevel.FINER, null, false);
            org.finer("TEST9");

            traceMBean.setTestHarness(notificationSource.getTraceNotificationType(), localSeq, notificationSource, TraceLevel.FINER, null, false);
            org.trace(TraceLevel.FINER, "TEST10");

            traceMBean.setTestHarness(notificationSource.getTraceNotificationType(), localSeq, notificationSource, TraceLevel.FINEST, null, false);
            org.finest("TEST11");

            traceMBean.setTestHarness(notificationSource.getTraceNotificationType(), localSeq, notificationSource, TraceLevel.FINEST, null, false);
            org.trace(TraceLevel.FINEST, "TEST12");

            traceMBean.setTestHarness(notificationSource.getTraceNotificationType(), localSeq++, notificationSource, TraceLevel.INFO, null, true);
            org.info("TEST13");

            traceMBean.setTestHarness(notificationSource.getTraceNotificationType(), localSeq++, notificationSource, TraceLevel.INFO, null, true);
            org.trace(TraceLevel.INFO, "TEST14");

            traceMBean.setTestHarness(notificationSource.getTraceNotificationType(), localSeq++, notificationSource, TraceLevel.SEVERE, null, true);
            org.severe("TEST15");

            traceMBean.setTestHarness(notificationSource.getTraceNotificationType(), localSeq++, notificationSource, TraceLevel.SEVERE, null, true);
            org.trace(TraceLevel.SEVERE, "TEST16");

			this.ts.unsetTracerLevel(orgMobicents.getTracerName());
        } catch (Exception e) {
            e.printStackTrace();
            fail("Got exception: " + e);
        }
    }

    private static class FakeTraceMBeanImpl extends TraceMBeanImpl {

        public FakeTraceMBeanImpl() throws NotCompliantMBeanException {
            super(null);
        }

        private String expectedNotificationType = null;
        private int expectedNotificationSequence = 0;
        private NotificationSource expectedNotificationSource = null;
        private TraceLevel expectedNotificatioTraceLevel = null;
        private boolean shouldReceiveNotification = false;

        public void setTestHarness(String expectedNotificationType, int expectedNotificationSequence,
                NotificationSource expectedNotificationSource, TraceLevel expectedNotificatioTraceLevel,
                Throwable expectedCause, boolean shouldReceiveNotification) {
            this.expectedNotificationType = expectedNotificationType;
            this.expectedNotificationSequence = expectedNotificationSequence;
            this.expectedNotificationSource = expectedNotificationSource;
            this.expectedNotificatioTraceLevel = expectedNotificatioTraceLevel;
            this.shouldReceiveNotification = shouldReceiveNotification;
        }

        @Override
        public void sendNotification(Notification _notification) {
            if (_notification == null) {
                fail("Passed notification is null");
                return;
            }

            if (_notification instanceof TraceNotification) {
            } else {
                fail("Received notification is not instance of TraceNotification");
                return;
            }

            TraceNotification notification = (TraceNotification) _notification;

            if (!this.shouldReceiveNotification) {
                fail("Received notification when it should not be passed. Notification: " + notification);
                return;
            }

			assertEquals("Notification types does not match, notification: " + notification, this.expectedNotificationType, notification.getType());
			assertEquals("Notification sequence does not match, notification: " + notification, this.expectedNotificationSequence, notification.getSequenceNumber());

			assertEquals("Notification source does not match, notification: " + notification, this.expectedNotificationSource, notification.getNotificationSource());
			assertEquals("Notification level does not match, notification: " + notification, this.expectedNotificatioTraceLevel, notification.getTraceLevel());
			// FIXME: add cause check
		}

	}
}
