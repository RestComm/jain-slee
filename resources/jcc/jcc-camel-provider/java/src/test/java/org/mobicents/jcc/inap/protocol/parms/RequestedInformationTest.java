/*
 * RequestedInformationTest.java
 * JUnit based test
 *
 * Created on 27 Август 2007 г., 13:09
 */

package org.mobicents.jcc.inap.protocol.parms;

import junit.framework.*;

/**
 *
 * @author Oleg Kulikov
 */
public class RequestedInformationTest extends TestCase {
    
    private byte[] hex = new byte[] {
        (byte)0x80, 0x01, 0x02,  (byte)0xA1, 0x04, (byte)0x82, 0x02, 0x01, 0x38
    };

    private byte[] bin = new byte[] {
        0x30, (byte)0x09, (byte)0x80, 0x01, 0x02, 
        (byte)0xA1, 0x04, (byte)0x82, 0x02, 0x01, 0x38
    };
    
    public RequestedInformationTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(RequestedInformationTest.class);
        return suite;
    }

    /**
     * Test of getType method, of class org.itech.jcc.inap.protocol.parms.RequestedInformation.
     */
    public void testGetType() {
        try {
            RequestedInformation ri = new RequestedInformation(hex);
            assertEquals(RequestedInformationType.CALL_CONNECTED_ELAPSED_TIME, ri.getType());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test of getValue method, of class org.itech.jcc.inap.protocol.parms.RequestedInformation.
     */
    public void testGetValue() {
        try {
            RequestedInformation ri = new RequestedInformation(hex);
            CallConnectedElapsedTime dur = (CallConnectedElapsedTime) ri.getValue();
            assertEquals(312, dur.getValue());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test of toByteArray method, of class org.itech.jcc.inap.protocol.parms.RequestedInformation.
     */
    public void testToByteArray() {
        CallConnectedElapsedTime duration = new CallConnectedElapsedTime(312);
        RequestedInformation ri = new RequestedInformation(
                RequestedInformationType.CALL_CONNECTED_ELAPSED_TIME,
                duration);
        byte[] data = ri.toByteArray();
        
        if (bin.length != data.length) {
            fail("The lenth of encoded data should be " + bin.length + " bytes");
        }
        
        for (int i = 0; i < bin.length; i++) {
            if (bin[i] != data[i]) fail("Error at position " + i
                    + ", expected " + bin[i] + " but found " + data[i]);
        }
    }
    
}
