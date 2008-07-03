/*
 * CalledPartyBcdNumberTest.java
 * JUnit based test
 *
 * Created on 24 Август 2007 г., 17:21
 */

package org.mobicents.jcc.inap.protocol.parms;

import junit.framework.*;
import java.io.IOException;

/**
 *
 * @author Oleg Kulikov
 */
public class CalledPartyBcdNumberTest extends TestCase {
    
    private byte[] bin = new byte[] {
        (byte)0x81, (byte)0x98, 0x40, 0x34, 0x14, 0x00, (byte)0xF4
    };
    
    public CalledPartyBcdNumberTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }

    public void testDecode() {
        try {
            CalledPartyBcdNumber cpn = new CalledPartyBcdNumber(bin);
            assertEquals("89044341004", cpn.getAddress());
        } catch (IOException ex) {
            fail(ex.getMessage());
        }
    }
    
}
