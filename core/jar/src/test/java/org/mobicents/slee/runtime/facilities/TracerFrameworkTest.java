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

	@Override
	protected void setUp() throws Exception {

		super.setUp();
		// akward, this will create mbean with regular trace facility, but we
		// need to check some things, in trace facility
		traceMBean = new FakeTraceMBeanImpl();
		//traceFacility = new TraceFacilityImpl(traceMBean);
		this.notificationSource = new ResourceAdaptorEntityNotification("XxX");
		ts = new TracerStorage(this.notificationSource, this.traceMBean);
	}

	@Override
	protected void tearDown() throws Exception {
		// TODO Auto-generated method stub
		super.tearDown();
	}

	/**
	 * Here we test default tracer which is always there.
	 */
	public void testDefaultTracer() {
		try {
			Tracer t = this.ts.createTracer("", true);
			if (t == null) {
				fail("Returned tracer is null");
				return;
			}

			String parentTracerName = t.getParentTracerName();
			assertNull("Parent tracer name for root tracer must be null", parentTracerName);
			assertNotNull("Tracer name must not be null", t.getTracerName());
			assertTrue("Root tracer name must be: \"\"", t.getTracerName().compareTo("") == 0);
			assertNotNull("tracer level must not be null", t.getTraceLevel());
			assertTrue("tracer level must be equal to INFO", t.getTraceLevel().equals(TraceLevel.INFO));
		} catch (Exception e) {
			e.printStackTrace();
			fail("Got exception: " + e);
		}

	}

	public void test1Creation() {
		try {
			Tracer t = this.ts.createTracer("org.mobicents.test", true);
			if (t == null) {
				fail("Returned tracer is null");
				return;
			}

			// here we have to check some recurency

			int loopGuard = 0;
			// ?
			int maxLoops = 3;
			// Start condition test
			String parentTracerName = t.getParentTracerName();
			assertNotNull("Parent tracer name for root tracer must not be null", parentTracerName);
			// this would indicate root
			String currentTracerName = null;
			String currentTracerParentName = null;
			while (t.getParentTracerName() != null) {
				if (maxLoops == loopGuard) {
					fail("Too many loops, possibly there is ring of parent child relation, this should not happen.");
					return;
				}
				if (currentTracerParentName != null) {
					assertTrue("Parent name[" + currentTracerParentName + "] from tracer down in tree does not match this tracer name[" + t.getTracerName() + "]", currentTracerParentName.compareTo(t
							.getTracerName()) == 0);

				}

				currentTracerName = t.getTracerName();
				currentTracerParentName = t.getParentTracerName();

				assertNotNull("Tracer name must not be null", t.getTracerName());
				assertNotNull("tracer level must not be null", t.getTraceLevel());
				assertTrue("tracer level must be equal to INFO", t.getTraceLevel().equals(TraceLevel.INFO));

				t = ts.createTracer(t.getParentTracerName(), false);
				if (t == null) {
					fail("Parent tracer is null for tracer with name: " + currentTracerName + ", parent name: " + currentTracerParentName);
					return;
				} else {

				}

				loopGuard++;
			}

		} catch (Exception e) {
			e.printStackTrace();
			fail("Got exception: " + e);
		}

	}

	public void test2TestEqulesesOfTracerObjects() throws InvalidArgumentException {
		Tracer org1 = this.ts.createTracer("org", true);
		Tracer org2 = this.ts.createTracer("org", false);
		Tracer org3 = this.ts.createTracer("org", true);

		assertEquals("Tracer object created with different boolean flag are different", org1, org2);
		assertEquals("Tracer object created with the same boolean flag are different", org1, org3);
	}

	/**
	 * Thisd tests if levels are inherited accross Tracer object. If not
	 * explicitly set trace level should be inherited from parent, its not told
	 * if inheritance should be constant or not, we follow constant, Until level
	 * is set, we inherit
	 */
	public void test3TraceLevelInheritance() {
		try {
			Tracer orgMobicentsTest = this.ts.createTracer("org.mobicents.test", true);
			Tracer orgMobicents = this.ts.createTracer("org.mobicents", true);
			Tracer org = this.ts.createTracer("org", true);

			// Root
			assertEquals("Trace level do not match case - 0", org.getTraceLevel(), TraceLevel.INFO);
			assertEquals("Trace level do not match case - 1", org.getTraceLevel(), orgMobicents.getTraceLevel());
			assertEquals("Trace level do not match case - 2", orgMobicents.getTraceLevel(), orgMobicentsTest.getTraceLevel());

			this.ts.setTracerLevel(TraceLevel.SEVERE, orgMobicents.getTracerName());
			// now orgMobicents and orgMobicentsXXX have sever trace level
			assertEquals("Trace level of tracer has not been changed to severe.", TraceLevel.SEVERE, orgMobicents.getTraceLevel());
			assertEquals("Trace level of child has not been changed to severe", TraceLevel.SEVERE, orgMobicentsTest.getTraceLevel());
			assertFalse("Trace level of parent tracer must be equal to root tracer, only its immediate children may have severe level", org.getTraceLevel() == TraceLevel.SEVERE);
			this.ts.unsetTracerLevel(orgMobicents.getTracerName());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Got exception: " + e);
		}
	}

	public void test3GetTracersSet() {
		try {
			Tracer orgMobicentsTest = this.ts.createTracer("org.mobicents.test", true);
			Tracer orgMobicents = this.ts.createTracer("org.mobicents", false);
			Tracer org = this.ts.createTracer("org", true);

			String[] tracersSet = this.ts.getDefinedTracerNames();

			assertNotNull("Tracers set must not be null");
			if (tracersSet == null)
				return;
			assertTrue("Tracers set must be empty[" + tracersSet.length + "] now, it is not: " + Arrays.toString(tracersSet), tracersSet.length == 0);

			this.ts.setTracerLevel(TraceLevel.SEVERE, orgMobicents.getTracerName());
			tracersSet = this.ts.getDefinedTracerNames();

			assertNotNull("Tracers set must not be null");
			if (tracersSet == null)
				return;
			assertTrue("Tracers set must have one element, it does not", tracersSet.length == 1);
			assertNotNull("Tracer set element is null, it should not", tracersSet[0]);
			if (tracersSet[0] == null)
				return;
			assertTrue("Tracers set element must be equal to: " + orgMobicents.getTracerName() + ", it is: " + tracersSet[0], tracersSet[0].compareTo(orgMobicents.getTracerName()) == 0);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Got exception: " + e);
		}
	}

	public void test3GetTracersUsed() {
		try {
			Tracer orgMobicentsTest = this.ts.createTracer("org.mobicents.test", false);
			Tracer orgMobicents = this.ts.createTracer("org.mobicents", false);
			Tracer org = this.ts.createTracer("org", false);

			String[] definedTracers = this.ts.getRequestedTracerNames();

			assertNotNull("Defined tracers set must not be null");
			if (definedTracers == null)
				return;
			assertTrue("Defined tracers set must be empty[" + definedTracers.length + "] now, it is not: " + Arrays.toString(definedTracers), definedTracers.length == 0);

			orgMobicents = this.ts.createTracer("org.mobicents", true);
			orgMobicents = this.ts.createTracer("org.mobicents", false);
			definedTracers = this.ts.getRequestedTracerNames();

			assertNotNull("Defined tracers set must not be null");
			if (definedTracers == null)
				return;
			assertTrue("Defined tracers set must have one element, it does not", definedTracers.length == 1);
			assertNotNull("Defined tracers set element is null, it should not", definedTracers[0]);
			if (definedTracers[0] == null)
				return;
			assertTrue("Defined tracers set element must be equal to: " + orgMobicents.getTracerName() + ", it is: " + definedTracers[0],
					definedTracers[0].compareTo(orgMobicents.getTracerName()) == 0);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Got exception: " + e);
		}
	}

	/**
	 * Tests if notifications are sent properly and in proper cases. Possibly
	 * should be broken down.
	 */
	public void test4TestNotificationType() {
		try {
			int localSeq = 0;
			Tracer orgMobicentsTest = this.ts.createTracer("org.mobicents.test", false);
			Tracer orgMobicents = this.ts.createTracer("org.mobicents", false);
			Tracer org = this.ts.createTracer("org", false);
			Tracer rootTracer = this.ts.createTracer("", false);
			this.ts.setTracerLevel(TraceLevel.SEVERE, orgMobicents.getTracerName());

			// Lets test
			this.traceMBean.setTestHarness(this.notificationSource.getTraceNotificationType(), localSeq++, this.notificationSource, TraceLevel.INFO, null, true);
			org.trace(TraceLevel.INFO, "TEST1");

			this.traceMBean.setTestHarness(this.notificationSource.getTraceNotificationType(), localSeq++, this.notificationSource, TraceLevel.INFO, null, true);
			rootTracer.trace(TraceLevel.INFO, "TEST2");

			this.traceMBean.setTestHarness(this.notificationSource.getTraceNotificationType(), localSeq, this.notificationSource, TraceLevel.INFO, null, false);
			orgMobicents.trace(TraceLevel.INFO, "TEST3");

			this.traceMBean.setTestHarness(this.notificationSource.getTraceNotificationType(), localSeq++, this.notificationSource, TraceLevel.INFO, null, true);
			rootTracer.trace(TraceLevel.INFO, "TEST4");

			this.traceMBean.setTestHarness(this.notificationSource.getTraceNotificationType(), localSeq, this.notificationSource, TraceLevel.CONFIG, null, false);
			org.config("TEST5");

			this.traceMBean.setTestHarness(this.notificationSource.getTraceNotificationType(), localSeq, this.notificationSource, TraceLevel.CONFIG, null, false);
			org.trace(TraceLevel.CONFIG, "TEST6");

			this.traceMBean.setTestHarness(this.notificationSource.getTraceNotificationType(), localSeq, this.notificationSource, TraceLevel.FINE, null, false);
			org.fine("TEST7");

			this.traceMBean.setTestHarness(this.notificationSource.getTraceNotificationType(), localSeq, this.notificationSource, TraceLevel.FINE, null, false);
			org.trace(TraceLevel.FINE, "TEST8");

			this.traceMBean.setTestHarness(this.notificationSource.getTraceNotificationType(), localSeq, this.notificationSource, TraceLevel.FINER, null, false);
			org.finer("TEST9");

			this.traceMBean.setTestHarness(this.notificationSource.getTraceNotificationType(), localSeq, this.notificationSource, TraceLevel.FINER, null, false);
			org.trace(TraceLevel.FINER, "TEST10");

			this.traceMBean.setTestHarness(this.notificationSource.getTraceNotificationType(), localSeq, this.notificationSource, TraceLevel.FINEST, null, false);
			org.finest("TEST11");

			this.traceMBean.setTestHarness(this.notificationSource.getTraceNotificationType(), localSeq, this.notificationSource, TraceLevel.FINEST, null, false);
			org.trace(TraceLevel.FINEST, "TEST12");

			this.traceMBean.setTestHarness(this.notificationSource.getTraceNotificationType(), localSeq++, this.notificationSource, TraceLevel.INFO, null, true);
			org.info("TEST13");

			this.traceMBean.setTestHarness(this.notificationSource.getTraceNotificationType(), localSeq++, this.notificationSource, TraceLevel.INFO, null, true);
			org.trace(TraceLevel.INFO, "TEST14");

			this.traceMBean.setTestHarness(this.notificationSource.getTraceNotificationType(), localSeq++, this.notificationSource, TraceLevel.SEVERE, null, true);
			org.severe("TEST15");

			this.traceMBean.setTestHarness(this.notificationSource.getTraceNotificationType(), localSeq++, this.notificationSource, TraceLevel.SEVERE, null, true);
			org.trace(TraceLevel.SEVERE, "TEST16");

			// Now severe llevel, here we should not receive anytihn

		} catch (Exception e) {
			e.printStackTrace();
			fail("Got exception: " + e);
		}
	}

	private class FakeTraceMBeanImpl extends TraceMBeanImpl {

		public FakeTraceMBeanImpl() throws NotCompliantMBeanException {
			super();
			// TODO Auto-generated constructor stub
		}

		private String expectedNotificationType = null;
		private int expectedNotificationSequence = 0;
		private NotificationSource expectedNotificationSource = null;
		private TraceLevel expectedNotificatioTraceLevel = null;
		private boolean shouldReceiveNotification = false;
		private Throwable expectedCause = null;

		public void setTestHarness(String expectedNotificationType, int expectedNotificationSequence, NotificationSource expectedNotificationSource, TraceLevel expectedNotificatioTraceLevel,
				Throwable expectedCause, boolean shouldReceiveNotification) {

			this.expectedNotificationType = expectedNotificationType;
			this.expectedNotificationSequence = expectedNotificationSequence;
			this.expectedNotificationSource = expectedNotificationSource;
			this.expectedNotificatioTraceLevel = expectedNotificatioTraceLevel;
			this.shouldReceiveNotification = shouldReceiveNotification;
			this.expectedCause = expectedCause;
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
