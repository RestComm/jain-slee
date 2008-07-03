/*
 * CallConnectedElapsedTimeTest.java
 * JUnit based test
 *
 * Created on 25 Август 2007 г., 12:15
 */

package org.mobicents.jcc.inap.protocol.parms;

import junit.framework.*;

/**
 *
 * @author Oleg Kulikov
 */
public class CallConnectedElapsedTimeTest extends TestCase {
    
    byte[] hex = new byte[] {(byte)0x82, 0x02, 0x01, 0x38 };
    byte[] bin = new byte[] {(byte)0xA1, 0x04, (byte)0x82, 0x02, 0x01, 0x38 };
    
    public CallConnectedElapsedTimeTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(CallConnectedElapsedTimeTest.class);
        return suite;
    }

    public void testDecode() {
        try {
            CallConnectedElapsedTime duration = new CallConnectedElapsedTime(hex);
            assertEquals(312, duration.getValue());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
    
    /**
     * Test of toByteArray method, of class org.itech.jcc.inap.protocol.parms.CallConnectedElapsedTime.
     */
    public void testToByteArray() {
        System.out.println("toByteArray");
        CallConnectedElapsedTime duration = new CallConnectedElapsedTime(312);
        byte[] data = duration.toByteArray();
        if (bin.length != data.length) {
            fail("The lenth of encoded operation should be " + bin.length + " bytes");
        }
        
        for (int i = 0; i < bin.length; i++) {
            if (bin[i] != data[i]) fail("Error at position " + i 
                    + ", expected " + bin[i] + " but found " + data[i]);
        }
    }
    
}
