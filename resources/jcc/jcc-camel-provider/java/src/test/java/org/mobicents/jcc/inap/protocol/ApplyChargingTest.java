package org.mobicents.jcc.inap.protocol;

import junit.framework.*;
import org.mobicents.jcc.inap.protocol.parms.LegID;

/*
 * ApplyChargingTest.java
 * JUnit based test
 *
 * Created on 23 Август 2007 г., 7:38
 */

/**
 *
 * @author Oleg Kulikov
 */
public class ApplyChargingTest extends TestCase {
    
    private byte[] bin = new byte[] {
        0x02, 0x01, 0x23, 0x30, 0x12, (byte)0x80,
        0x0B, (byte)0xA0, 0x09, (byte)0x80, 0x02, 0x46, 0x50, (byte)0xA1,
        0x03, 0x01, 0x01, 0x01, (byte)0xA2, 0x03, (byte)0x80, 0x01, 0x02
    };
    
    public ApplyChargingTest(String testName) {
        super(testName);
    }
    
    @Override
    protected void setUp() throws Exception {
    }
    
    @Override
    protected void tearDown() throws Exception {
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite(ApplyChargingTest.class);
        return suite;
    }
    
    /**
     * Test of toByteArray method, of class org.itech.jcc.inap.protocol.ApplyCharging.
     */
    public void testToByteArray() {
        System.out.println("toByteArray");
        
        ApplyCharging applyCharging = new ApplyCharging(LegID.SENDING_SIDE_ID, LegID.SECOND_LEG);
        byte[] data = applyCharging.toByteArray();

        if (bin.length != data.length) {
            fail("The lenth of encoded operation should be " + bin.length + " bytes");
        }
        
        for (int i = 0; i < bin.length; i++) {
            if (bin[i] != data[i]) fail("Error at position " + i 
                    + ", expected " + bin[i] + " but found " + data[i]);
        }
        //assertEquals(bin, data);
        
    }
    
}
