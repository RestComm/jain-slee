/*
 * CallingPartyNumberTest.java
 * JUnit based test
 *
 * Created on 24 Август 2007 г., 16:59
 */

package org.mobicents.jcc.inap.protocol.parms;

import junit.framework.*;
import java.io.IOException;

/**
 *
 * @author Oleg Kulikov
 */
public class CallingPartyNumberTest extends TestCase {
    
    private byte[] bin = new byte[] {
        0x03, 0x13, 0x09, 0x32, 0x26, 0x59, 0x18,
    };
    
    public CallingPartyNumberTest(String testName) {
        super(testName);
    }
    
    protected void setUp() throws Exception {
    }
    
    protected void tearDown() throws Exception {
    }
    
    public void testDecode() {
        try {
            CallingPartyNumber cpn = new CallingPartyNumber(bin);
            assertEquals("9023629581", cpn.getAddress());
        } catch (IOException ex) {
            fail(ex.getMessage());
        }
    }
    
    public void testToByteArray() {
        try {
            CallingPartyNumber cpn = new CallingPartyNumber(bin);
            CallingPartyNumber cpn1 = new CallingPartyNumber(
                    cpn.getNai(), cpn.getNii(), cpn.getNpi(),
                    cpn.getApri(), cpn.getSi(), cpn.getAddress());
            byte[] data = cpn1.toByteArray();
            if (bin.length != data.length) {
                fail("The lenth of encoded operation should be " + bin.length + " bytes");
            }
            
            for (int i = 0; i < bin.length; i++) {
                if (bin[i] != data[i]) fail("Error at position " + i
                        + ", expected " + bin[i] + " but found " + data[i]);
            }
        } catch (IOException ex) {
            fail(ex.getMessage());
        }
    }
}
