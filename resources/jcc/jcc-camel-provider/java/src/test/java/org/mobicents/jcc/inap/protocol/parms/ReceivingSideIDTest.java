/*
 * ReceivingSideIDTest.java
 * JUnit based test
 *
 * Created on 25 Август 2007 г., 9:06
 */

package org.mobicents.jcc.inap.protocol.parms;

import java.io.IOException;
import junit.framework.*;

/**
 *
 * @author Oleg Kulikov
 */
public class ReceivingSideIDTest extends TestCase {
    
    private byte[] hex = new byte[] {
        (byte) 0x81, 0x01, 0x02
    };
    private byte[] bin = new byte[] {
        /*(byte) 0xA3, 0x03, */(byte) 0x81, 0x01, 0x02
    };
    
    public ReceivingSideIDTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(ReceivingSideIDTest.class);
        return suite;
    }

    public void testDecode() {
        try {
            LegID legID = new LegID(hex);
            assertEquals(LegID.RECEIVING_SIDE_ID, legID.getSideID());
            assertEquals(LegID.SECOND_LEG, legID.getLegID());
        } catch (IOException ex) {
            fail("Could not decode");
            ex.printStackTrace();
        }
    }
    
    /**
     * Test of toByteArray method, of class org.itech.jcc.inap.protocol.ReceivingSideID.
     */
    public void testToByteArray() {
        System.out.println("toByteArray");
        LegID legID = new LegID(LegID.RECEIVING_SIDE_ID, LegID.SECOND_LEG);
        byte[] data = legID.toByteArray();
        
        if (bin.length != data.length) {
            fail("The lenth of encoded operation should be " + bin.length + " bytes");
        }
        
        for (int i = 0; i < bin.length; i++) {
            if (bin[i] != data[i]) fail("Error at position " + i 
                    + ", expected " + bin[i] + " but found " + data[i]);
        }
    }
    
}
