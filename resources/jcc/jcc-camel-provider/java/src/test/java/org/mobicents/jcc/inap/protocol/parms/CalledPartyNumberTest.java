/*
 * CalledPartyNumberTest.java
 * JUnit based test
 *
 * Created on 24 Август 2007 г., 15:32
 */

package org.mobicents.jcc.inap.protocol.parms;

import junit.framework.*;

/**
 *
 * @author Oleg Kulikov
 */
public class CalledPartyNumberTest extends TestCase {
    
    private byte[] bin = new byte[] {
        (byte)0x84, 0x10, (byte)0x97, 0x40,  0x34, (byte)0x54, (byte)0x76, 0x03
    };
    
    public CalledPartyNumberTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }

    public void testDecode() {
        try {
            CalledPartyNumber cpn = new CalledPartyNumber(bin);
            assertEquals("79044345673", cpn.getAddress());
            assertEquals(4, cpn.getNai());
            assertEquals(0, cpn.getInni());
            assertEquals(1, cpn.getNpi());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
    /**
     * Test of toByteArray method, of class org.itech.jcc.inap.protocol.CalledPartyNumber.
     */
    public void testToByteArray() {
        System.out.println("toByteArray");
        
        CalledPartyNumber cpn = new CalledPartyNumber(CalledPartyNumber.INTERNATIONAL, 0, 1, "79044345673");
        byte[] data = cpn.toByteArray();
        
        if (bin.length != data.length) {
            fail("The lenth of encoded operation should be " + bin.length + " bytes");
        }
        
        for (int i = 0; i < bin.length; i++) {
            if (bin[i] != data[i]) fail("Error at position " + i 
                    + ", expected " + bin[i] + " but found " + data[i]);
        }        
    }
    
}
