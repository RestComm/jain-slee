/*
 * CauseTest.java
 * JUnit based test
 *
 * Created on 25 Август 2007 г., 11:55
 */

package org.mobicents.jcc.inap.protocol.parms;

import junit.framework.*;

/**
 *
 * @author Oleg Kulikov
 */
public class CauseTest extends TestCase {
    
    byte[] hex = new byte[] {(byte) 0x80, (byte)0x90};
    byte[] bin = new byte[] {(byte) 0x9E, 0x02, (byte) 0x80, (byte)0x90};
    
    public CauseTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(CauseTest.class);
        return suite;
    }

    public void testDecode() {
        try {
            Cause cause = new Cause(hex);
            assertEquals(Cause.NORMAL, cause.getCause());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
    /**
     * Test of toByteArray method, of class org.itech.jcc.inap.protocol.parms.Cause.
     */
    public void testToByteArray() {
        Cause cause = new Cause(Cause.NORMAL);
        byte[] data = cause.toByteArray();
        assertEquals(new String(bin), new String(data));
    }
    
}
