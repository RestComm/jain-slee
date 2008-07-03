/*
 * TimerTest.java
 * JUnit based test
 *
 * Created on 18 Сентябрь 2007 г., 20:27
 */

package org.mobicents.util;

import junit.framework.*;

/**
 *
 * @author mitrenko
 */
public class TimerTest extends TestCase {
    
    private boolean executed = false;
    
    public TimerTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        executed = false;
    }

    @Override
    protected void tearDown() throws Exception {
    }

    /**
     * Test of schedule method, of class org.itech.util.Timer.
     */
    public void testSchedule() {
        System.out.println("schedule");
        LocalTimer timer = new LocalTimer();
        timer.schedule(new TestTask(), 3);
        assertEquals(false, executed);
        System.out.println("Waiting");
        try {
            Thread.currentThread().sleep(5000);
        } catch (InterruptedException e) {
        }
        System.out.println("Checking");
        assertEquals(true, executed);
    }

    /**
     * Test of stop method, of class org.itech.util.Timer.
     */
    public void testStop() {
        System.out.println("Test stop");
        executed = false;
        LocalTimer timer = new LocalTimer();
        timer.schedule(new TestTask(), 3);
        timer.stop();
        try {
            Thread.currentThread().sleep(4000);
        } catch (InterruptedException e) {
        }
        assertEquals(false, executed);
    }

    /**
     * Test of reset method, of class org.itech.util.Timer.
     */
    public void testReset() {
        System.out.println("Test reset");
        LocalTimer timer = new LocalTimer();
        timer.schedule(new TestTask(), 6);
        try {
            Thread.currentThread().sleep(2000);
        } catch (InterruptedException e) {
        }
        assertEquals(false, executed);
        timer.reset(2);
        try {
            Thread.currentThread().sleep(3000);
        } catch (InterruptedException e) {
        }
        assertEquals(true, executed);
    }
 
    private class TestTask implements Runnable {
        public void run() {
            System.out.println("Set executed");
            executed = true;
        }
    }
}
